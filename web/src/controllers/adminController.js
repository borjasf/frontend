/* Controlador del panel de administración (solo accesible con rol admin).
   - dashboard: renderiza el dashboard principal del admin
   - listarUsuarios: obtiene todos los usuarios vía GET /api/usuarios y renderiza admin/usuarios.hbs
   - listarCompraventas: obtiene todas las compraventas con filtros opcionales y renderiza admin/compraventas.hbs */


const usuariosService = require('../services/usuariosService');
const compraventasService = require('../services/compraventasService');


const adminController = {
      dashboard: async (req, res) => {
         try {
            // Obtener estadísticas básicas para el dashboard
            const token = req.cookies.jwt;
            
            // Obtener total de usuarios
            let totalUsuarios = 0;
            try {
               const dataUsuarios = await usuariosService.getTodosUsuarios(token);
               totalUsuarios = dataUsuarios.usuarios ? dataUsuarios.usuarios.length : 0;
            } catch (error) {
               console.warn("No se pudieron contar los usuarios:", error.message);
            }
            
            res.render('admin/dashboard', { 
               title: 'Panel de Administración',
               totalUsuarios: totalUsuarios
            });
         } catch (error) {
            console.error("Error al cargar el dashboard:", error);
            res.render('error', { mensaje: 'Error al cargar el dashboard. Inténtalo más tarde.' });
         }
      },

      listarUsuarios: async (req, res) => {
         try {
            //Primero leemos el token de la cookie de req.cookies.jwt
            const token = req.cookies.jwt;
            //Llamamos a usuariosService.getTodosUsuarios(token) para obtener la lista de usuarios
            let usuarios = [];
            try {
               const data = await usuariosService.getTodosUsuarios(token);
               let lista = [];

               if (Array.isArray(data)) {
                  lista = data;
               } else if (data.usuarios) {
                  lista = data.usuarios.map(u => u.resumen || u);
               } else if (data._embedded) {
                  lista = Object.values(data._embedded)[0] || [];
               }

               const normalizarUsuario = (u) => ({
                  ID: u.ID || u.id || u.identificador || '',
                  NOMBRE: u.NOMBRE || u.nombre || '',
                  APELLIDOS: u.APELLIDOS || u.apellidos || '',
                  EMAIL: u.EMAIL || u.email || '',
                  ESADMIN: (u.ESADMIN ?? u.esAdmin ?? u.admin) ? 1 : 0,
                  CONTADORCOMPRAS: u.CONTADORCOMPRAS ?? u.contadorCompras ?? 0,
                  CONTADORVENTAS: u.CONTADORVENTAS ?? u.contadorVentas ?? 0
               });

               usuarios = lista.map(normalizarUsuario);

               const faltanDetalles = usuarios.some((u) => !u.APELLIDOS && !u.CONTADORCOMPRAS && !u.CONTADORVENTAS);
               if (faltanDetalles) {
                  const ids = usuarios.map((u) => u.ID).filter(Boolean);
                  const detalles = await Promise.all(ids.map((id) => usuariosService.getUsuario(id, token)));
                  const detallesPorId = new Map(detalles.map((u) => [u.ID || u.id || u.identificador, u]));
                  usuarios = usuarios.map((u) => {
                     const extra = detallesPorId.get(u.ID);
                     return extra ? normalizarUsuario({ ...u, ...extra }) : u;
                  });

               }

               // Calcular compras/ventas reales por usuario
               const idsParaConteo = usuarios.map((u) => u.ID).filter(Boolean);
               const conteos = await Promise.all(idsParaConteo.map(async (id) => {
                  try {
                     const [ventasData, comprasData] = await Promise.all([
                        compraventasService.getMisVentas(id, token),
                        compraventasService.getMisCompras(id, token)
                     ]);

                     const ventas = ventasData?.page?.totalElements ?? 0;
                     const compras = comprasData?.page?.totalElements ?? 0;
                     return { id, ventas, compras };
                  } catch (error) {
                     console.warn(`No se pudieron contar compras/ventas para usuario ${id}:`, error.message);
                     return { id, ventas: 0, compras: 0 };
                  }
               }));

               const conteosPorId = new Map(conteos.map((c) => [c.id, c]));
               usuarios = usuarios.map((u) => {
                  const conteo = conteosPorId.get(u.ID);
                  if (!conteo) {
                     return u;
                  }

                  return {
                     ...u,
                     CONTADORVENTAS: conteo.ventas,
                     CONTADORCOMPRAS: conteo.compras
                  };
               });
            } catch (apiError) {
               console.warn("No se pudieron cargar los usuarios desde la API:", apiError.message);
            }
            
            //Renderizamos la vista admin/usuarios.hbs inyectando la lista de usuarios como JSON
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
