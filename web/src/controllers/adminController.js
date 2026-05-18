/* Controlador del panel de administración (solo accesible con rol admin).
   - dashboard: renderiza el dashboard principal del admin
   - listarUsuarios: obtiene todos los usuarios vía GET /api/usuarios y renderiza admin/usuarios.hbs
   - listarCompraventas: obtiene todas las compraventas con filtros opcionales y renderiza admin/compraventas.hbs */


const usuariosService = require('../services/usuariosService');
const compraventasService = require('../services/compraventasService');
const productosService = require('../services/productosService');


const adminController = {
      dashboard: async (req, res) => {
         try {
            // Obtener estadísticas básicas para el dashboard
            const token = req.cookies.jwt;
            const idUsuario = res.locals.usuario.id || res.locals.usuario.sub; 
            
            // Obtener total de usuarios
            let totalUsuarios = 0;
            try {
               const [dataUsuarios, dataAdmin] = await Promise.all([
                  usuariosService.getTodosUsuarios(token),
                  usuariosService.getUsuario(idUsuario, token)
               ]);
               totalUsuarios = dataUsuarios.usuarios ? dataUsuarios.usuarios.length : 0;
               usuarioReal = dataAdmin.resumen || dataAdmin; // En caso de que la API devuelva un resumen en lugar del usuario completo
            } catch (error) {
               console.warn("No se pudieron contar los usuarios:", error.message);
            }
            
            res.render('admin/dashboard', { 
               title: 'Panel de Administración',
               totalUsuarios: totalUsuarios,
               usuario: {usuarioReal, rol: res.locals.usuario.rol || 'ADMINISTRADOR'} 
            });
         } catch (error) {
            console.error("Error al cargar el dashboard:", error);
            res.render('error', { mensaje: 'Error al cargar el dashboard. Inténtalo más tarde.' });
         }
      },

listarUsuarios: async (req, res) => {
         try {
            // 1. Obtenemos el token de la cookie
            const token = req.cookies.jwt;
            let usuarios = [];

            try {
               // 2. Llamamos al servicio de usuarios
               const data = await usuariosService.getTodosUsuarios(token);
               let lista = [];

               // Manejo de diferentes formatos de respuesta de la API
               if (Array.isArray(data)) {
                  lista = data;
               } else if (data.usuarios) {
                  lista = data.usuarios.map(u => u.resumen || u);
               } else if (data._embedded) {
                  lista = Object.values(data._embedded)[0] || [];
               }

               // 3. Función para normalizar los datos (mapear nombres de campos de la API a nuestro modelo)
               const normalizarUsuario = (u) => ({
                  ID: u.ID || u.id || u.identificador || '',
                  NOMBRE: u.NOMBRE || u.nombre || '',
                  APELLIDOS: u.APELLIDOS || u.apellidos || '',
                  EMAIL: u.EMAIL || u.email || '',
                  ESADMIN: (u.ESADMIN ?? u.esAdmin ?? u.admin) ? 1 : 0,
                  CONTADORCOMPRAS: u.contadorCompras ?? u.CONTADORCOMPRAS ?? 0,
                  CONTADORVENTAS: u.contadorVentas ?? u.CONTADORVENTAS ?? 0
               });

               usuarios = lista.map(normalizarUsuario);

               // 4. Si la lista general no trajo detalles (apellidos o contadores), los pedimos uno a uno
               // Esto es común cuando la lista devuelve un "ResumenDTO" y necesitamos el "UsuarioDTO" completo
               const faltanDetalles = usuarios.some((u) => !u.APELLIDOS && u.CONTADORCOMPRAS === 0 && u.CONTADORVENTAS === 0);
               
               if (faltanDetalles) {
                  const ids = usuarios.map((u) => u.ID).filter(Boolean);
                  const detalles = await Promise.all(
                     ids.map((id) => usuariosService.getUsuario(id, token).catch(() => null))
                  );
                  
                  const detallesPorId = new Map(
                     detalles.filter(d => d).map((u) => [u.ID || u.id || u.identificador, u])
                  );
                  
                  usuarios = usuarios.map((u) => {
                     const extra = detallesPorId.get(u.ID);
                     return extra ? normalizarUsuario({ ...u, ...extra }) : u;
                  });
               }

               // 5. Obtener los contadores reales de compraventas para cada usuario
               // El admin puede ver TODAS las compraventas, así que las obtenemos una sola vez y contamos localmente
               try {
                  const allCompraventas = await compraventasService.getTodasCompraventas('', '', token);
                  let compraventasList = [];
                  
                  if (Array.isArray(allCompraventas)) {
                     compraventasList = allCompraventas;
                  } else if (allCompraventas._embedded) {
                     compraventasList = Object.values(allCompraventas._embedded)[0] || [];
                  }
                  
                  // Contar compraventas por usuario (idVendedor para ventas, idComprador para compras)
                  const ventasPorVendedor = new Map();
                  const comprasPorComprador = new Map();
                  
                  compraventasList.forEach((c) => {
                     const idVendedor = c.IDVENDEDOR || c.idVendedor;
                     const idComprador = c.IDCOMPRADOR || c.idComprador;
                     
                     if (idVendedor) {
                        ventasPorVendedor.set(idVendedor, (ventasPorVendedor.get(idVendedor) || 0) + 1);
                     }
                     if (idComprador) {
                        comprasPorComprador.set(idComprador, (comprasPorComprador.get(idComprador) || 0) + 1);
                     }
                  });
                  
                  // Actualizar contadores en usuarios
                  usuarios = usuarios.map((u) => ({
                     ...u,
                     CONTADORVENTAS: ventasPorVendedor.get(u.ID) || 0,
                     CONTADORCOMPRAS: comprasPorComprador.get(u.ID) || 0
                  }));
               } catch (error) {
                  console.warn("Error al obtener contadores de compraventas:", error.message);
               }

               // 6. Obtener productos en venta por usuario (misma fuente que el perfil)
               try {
                  const enVenta = await Promise.all(
                     usuarios.map(async (u) => {
                        const cantidad = await productosService.getCountProductosEnVenta(u.ID);
                        return { id: u.ID, cantidad };
                     })
                  );

                  const enVentaPorId = new Map(enVenta.map((item) => [item.id, item.cantidad]));
                  usuarios = usuarios.map((u) => ({
                     ...u,
                     PRODUCTOSENVENTA: enVentaPorId.get(u.ID) || 0
                  }));
               } catch (error) {
                  console.warn("Error al obtener productos en venta por usuario:", error.message);
               }

               // 7. Excluir al administrador del listado de gestión de usuarios
               usuarios = usuarios.filter((u) => {
                  const email = (u.EMAIL || '').toLowerCase();
                  return !u.ESADMIN && u.ID !== 'admin-001' && email !== 'admin@segundum.com';
               });

            } catch (apiError) {
               console.warn("No se pudieron cargar los usuarios desde la API:", apiError.message);
            }
            
            // 8. Renderizamos la vista inyectando la lista procesada
            res.render('admin/usuarios', { 
               title: 'Gestión de Usuarios', 
               usuarios: JSON.stringify(usuarios),
               usuariosCount: usuarios.length
            });

         } catch (error) {
            console.error("Error al cargar la lista de usuarios:", error);
            res.render('error', { mensaje: 'No se pudieron cargar los usuarios. Inténtalo más tarde.' });
         }
      },

      listarCompraventas: async (req, res) => {
         try {
            //Leemos el token de la cookie de req.cookies.jwt
            const token = req.cookies.jwt;
            //Leemos los filtros de query params: comprador, vendedor, page
            const filtros = {
               comprador: req.query.idComprador || '',
               vendedor: req.query.idVendedor || '',
               page: req.query.page || 0
            };
            
            let compraventas = [];
            try {
               //Llamar a la API para obtener todas las compraventas
               const data = await compraventasService.getTodasCompraventas(filtros.comprador, filtros.vendedor, token);
               if (Array.isArray(data)) {
                  compraventas = data;
               } else if (data._embedded) {
                  compraventas = Object.values(data._embedded)[0] || [];
               }

               compraventas = compraventas.map((c) => ({
                  ID: c.ID || c.id || '',
                  IDPRODUCTO: c.IDPRODUCTO || c.idProducto || '',
                  NOMBREPRODUCTO: c.NOMBREPRODUCTO || c.titulo || c.producto || '',
                  IDCOMPRADOR: c.IDCOMPRADOR || c.idComprador || '',
                  NOMBRE_COMPRADOR: c.NOMBRE_COMPRADOR || c.nombreComprador || '',
                  IDVENDEDOR: c.IDVENDEDOR || c.idVendedor || '',
                  NOMBRE_VENDEDOR: c.NOMBRE_VENDEDOR || c.nombreVendedor || '',
                  FECHACOMPRA: c.FECHACOMPRA || c.fecha || '',
                  PRECIO: c.PRECIO ?? c.precio ?? 0
               }));

            } catch (apiError) {
               console.warn("No se pudieron cargar las compraventas desde la API:", apiError.message);
            }
            
            //Renderizamos la vista admin/compraventas.hbs inyectando la lista como JSON
            res.render('admin/compraventas', { 
               title: 'Gestión de Compraventas', 
               compraventas: JSON.stringify(compraventas),
               compraventasCount: compraventas.length,
               filtros 
            });
         } catch (error) {
            console.error("Error al cargar la lista de compraventas:", error);
            res.render('error', { mensaje: 'No se pudieron cargar las compraventas. Inténtalo más tarde.' });
         }
      }};
   
module.exports = adminController;  
