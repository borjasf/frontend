/* Controlador del panel de administración (solo accesible con rol admin).
   - listarUsuarios: obtiene todos los usuarios vía GET /api/usuarios y renderiza admin/usuarios.hbs
   - listarCompraventas: obtiene todas las compraventas con filtros opcionales y renderiza admin/compraventas.hbs */


const usuariosService = require('../services/usuariosService');
const compraventasService = require('../services/compraventasService');


const adminController = {
      listarUsuarios: async (req, res) => {
         try {
            //Primero leemos el token de la cookie de req.cookies.jwt
            const token = req.cookies.jwt;
            //Llamamos a usuariosService.getTodosUsuarios(token) para obtener la lista de usuarios
            const data = await usuariosService.getTodosUsuarios(token);                                                                                                  
            const usuarios = data.usuarios.map(u => u.resumen);
            //Renderizamos la vista admin/usuarios.hbs inyectando la lista de usuarios
            res.render('admin/usuarios', { title: 'Gestión de Usuarios', usuarios });
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
            //Solo llamar a la API si hay al menos uno de los dos filtros — si no, Java devuelve 400 porque son parámetros requeridos
            if (filtros.comprador || filtros.vendedor) {
               //Llamamos a compraventasService.getTodasCompraventas(token, filtros) para obtener la lista de compraventas
               const compraventas = await compraventasService.getTodasCompraventas(filtros.comprador, filtros.vendedor, token);
              // La respuesta es HATEOAS paginado (_embedded), extraer el array con Object.values(data._embedded)[0]
               const listaCompraventas = Object.values(compraventas._embedded)[0];
               //Renderizamos la vista admin/compraventas.hbs inyectando la lista de compraventas
               res.render('admin/compraventas', { title: 'Gestión de Compraventas', compraventas: listaCompraventas, filtros });
            } else {
               res.render('admin/compraventas', { title: 'Gestión de Compraventas', compraventas: [], filtros });

         }

         } catch (error) {
            console.error("Error al cargar la lista de compraventas:", error);
            res.render('error', { mensaje: 'No se pudieron cargar las compraventas. Inténtalo más tarde.' });
         }
      }};
   
module.exports = adminController;  
