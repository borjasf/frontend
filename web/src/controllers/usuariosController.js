/* Controlador de perfil de usuario.
   - verPerfil: obtiene los datos del usuario autenticado + sus productos en venta y renderiza perfil.hbs
   - editarPerfil: procesa el PATCH /api/usuarios/:id para actualizar datos del usuario */
/* Controlador de perfil de usuario. */
const usuariosService = require('../services/usuariosService');
const compraventasService = require('../services/compraventasService');

const usuariosController = {
    
    // verPerfil: obtiene los datos reales del usuario
    verPerfil: async (req, res) => {
        try {
            const idUsuario = res.locals.usuario.id || res.locals.usuario.sub;
            const token = req.cookies.jwt;

            // Le pedimos a Java tus datos reales
             const data = await usuariosService.getUsuario(idUsuario, token);
            const usuarioReal = data.resumen || data;   // extrae resumen HATEOAS

            res.render('usuario/perfil', {
                title: 'Mi Panel de Control',
                usuario: usuarioReal,
                stats: { enVenta: 0, vendidos: usuarioReal.contadorVentas || 0, comprados: usuarioReal.contadorCompras || 0 },
                activoPerfil: true
            });
            
        } catch (error) {
            console.error("Error al renderizar el perfil real:", error.message);
            res.render('error', { 
                mensaje: 'No hemos podido cargar tus datos de perfil.',
                statusCode: error.response?.status || 500
            });
        }
    },
    // misVentas: Muestra los productos que el usuario ha publicado
    misVentas: async (req, res) => {
        try {
            const idUsuario = res.locals.usuario.id;
            const token = req.cookies.jwt;

            const [dataUsuario, dataVentas] = await Promise.all([
                usuariosService.getUsuario(idUsuario, token),
                compraventasService.getMisVentas(idUsuario, token)
            ]);

            const usuarioReal = dataUsuario.resumen || dataUsuario;
            const ventas = dataVentas._embedded
                ? Object.values(dataVentas._embedded)[0].map(v => ({
                    idProducto: v.idProducto,
                    titulo: v.titulo,
                    precio: v.precio,
                    comprador: v.nombreComprador,
                    fecha: Array.isArray(v.fecha)
                        ? `${v.fecha[2]}/${v.fecha[1]}/${v.fecha[0]}`
                        : v.fecha?.substring(0, 10) ?? '—'
                }))
                : [];

            res.render('usuario/misVentas', {
                title: 'Mis Ventas',
                usuario: usuarioReal,
                activoVentas: true,
                ventas
            });
        } catch (error) {
            console.error("Error al cargar mis ventas:", error);
            res.render('error', { mensaje: 'No se pudieron cargar tus ventas.', statusCode: 500 });
        }
    },

    // misCompras: Muestra los productos adquiridos por el usuario
    misCompras: async (req, res) => {
        try {
            const idUsuario = res.locals.usuario.id;
            const token = req.cookies.jwt;

            const [dataUsuario, dataCompras] = await Promise.all([
                usuariosService.getUsuario(idUsuario, token),
                compraventasService.getMisCompras(idUsuario, token)
            ]);

            const usuarioReal = dataUsuario.resumen || dataUsuario;
            const compras = dataCompras._embedded
                ? Object.values(dataCompras._embedded)[0].map(c => ({
                    idProducto: c.idProducto,
                    titulo: c.titulo,
                    precio: c.precio,
                    vendedor: c.nombreVendedor,
                    fechaCompra: Array.isArray(c.fecha)
                        ? `${c.fecha[2]}/${c.fecha[1]}/${c.fecha[0]}`
                        : c.fecha?.substring(0, 10) ?? '—',
                    estado: 'Completada',
                    badgeColor: 'bg-success'
                }))
                : [];

            res.render('usuario/misCompras', {
                title: 'Mis Compras Realizadas',
                usuario: usuarioReal,
                activoCompras: true,
                compras
            });
        } catch (error) {
            console.error("Error al cargar mis compras:", error);
            res.render('error', { mensaje: 'No se pudieron cargar tus compras.', statusCode: 500 });
        }
    },
    
    mostrarEditarPerfil: async (req, res) => {
        try {
            const idUsuario = res.locals.usuario.id;
            const token = req.cookies.jwt;
            const data = await usuariosService.getUsuario(idUsuario, token);
            const usuarioReal = data.resumen || data;

            res.render('usuario/editarPerfil', {
                title: 'Editar Perfil',
                usuario: usuarioReal,
                activoPerfil: true
            });
        } catch (error) {
            console.error("Error al mostrar el formulario de edición:", error);
            res.render('error', { mensaje: 'No se pudo cargar el formulario de edición.', statusCode: 500 });
        }
    },

    editarPerfil: async (req, res) => {
        try {
            const idUsuario = res.locals.usuario.id;
            const token = req.cookies.jwt;
            const { nombre, apellidos, email, clave, fechaNacimiento, telefono } = req.body;

            await usuariosService.actualizarUsuario(idUsuario, { nombre, apellidos, email, clave, fechaNacimiento, telefono }, token);
            res.redirect('/perfil');
        } catch (error) {
            console.error("Error al editar el perfil:", error);
            res.render('error', { mensaje: 'No se pudo actualizar tu perfil.', statusCode: error.response?.status || 500 });
        }
    },

    cambiarContrasena: async (req, res) => {
        try {
            const idUsuario = res.locals.usuario.id;
            const token = req.cookies.jwt;
            const { nuevaClave } = req.body;

            // Obtenemos los datos actuales para no machacarlos en el PATCH
            const data = await usuariosService.getUsuario(idUsuario, token);
            const u = data.resumen || data;

            await usuariosService.actualizarUsuario(idUsuario, {
                nombre: u.nombre,
                apellidos: u.apellidos,
                email: u.email,
                clave: nuevaClave,
                fechaNacimiento: u.fechaNacimiento,
                telefono: u.telefono
            }, token);

            res.redirect('/perfil');
        } catch (error) {
            console.error("Error al cambiar contraseña:", error);
            res.render('error', { mensaje: 'No se pudo cambiar la contraseña.', statusCode: error.response?.status || 500 });
        }
    }

};

module.exports = usuariosController;