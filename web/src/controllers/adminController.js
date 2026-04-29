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
               usuarios = data.usuarios ? data.usuarios.map(u => u.resumen || u) : [];
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
               // La respuesta es HATEOAS paginado (_embedded), extraer el array
               if (data._embedded) {
                  compraventas = Object.values(data._embedded)[0] || [];
               }
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
