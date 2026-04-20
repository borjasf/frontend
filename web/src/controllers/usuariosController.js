/* Controlador de perfil de usuario.
   - verPerfil: obtiene los datos del usuario autenticado + sus productos en venta y renderiza perfil.hbs
   - editarPerfil: procesa el PATCH /api/usuarios/:id para actualizar datos del usuario */
/* Controlador de perfil de usuario. */
const usuariosController = {
    
    // verPerfil: obtiene los datos del usuario + sus productos y renderiza perfil.hbs
    verPerfil: async (req, res) => {
        try {
            // MOCK: Datos simulados para desarrollo (Práctica 5 - Handlebars)
            const usuarioMock = {
                nombre: "Juan",
                apellidos: "Pérez",
                email: "juan.perez@um.es",
                telefono: "600 123 456",
                fechaNacimiento: "1990-05-15",
                fechaRegistro: "01/09/2025"
            };

            // Estadísticas para las tarjetas (Cards) de Bootstrap
            const estadisticas = {
                enVenta: 3,
                vendidos: 12,
                comprados: 5
            };

            res.render('usuario/perfil', {
                title: 'Mi Panel de Control',
                usuario: usuarioMock,
                stats: estadisticas
            });
        } catch (error) {
            console.error("Error al renderizar el perfil:", error);
            res.status(500).send("Error interno del servidor");
        }
    },

    // misVentas: Muestra los productos que el usuario ha publicado
    misVentas: async (req, res) => {
        try {
            const misProductosMock = [
                { 
                    id: 1, titulo: "Bicicleta de Montaña Orbea", precio: 350.00, 
                    estado: "BUENO", categoria: "Deportes", visualizaciones: 45, 
                    estatus: "En Venta", badgeColor: "bg-primary" // <--- Color azul
                },
                { 
                    id: 2, titulo: "Casco Bici", precio: 25.00, 
                    estado: "COMO_NUEVO", categoria: "Deportes", visualizaciones: 12, 
                    estatus: "Vendido", badgeColor: "bg-success" // <--- Color verde
                }
            ];

            // Reutilizamos el mock del usuario para la tarjeta lateral
            const usuarioMock = { nombre: "Juan", apellidos: "Pérez", fechaRegistro: "01/09/2025" };

            res.render('usuario/misVentas', {
                title: 'Mis Anuncios y Ventas',
                usuario: usuarioMock,
                productos: misProductosMock
            });
        } catch (error) {
            console.error("Error al cargar mis ventas:", error);
            res.status(500).send("Error interno");
        }
    },

    // misCompras: Muestra los productos adquiridos por el usuario
    misCompras: async (req, res) => {
        try {
            // MOCK: Simulamos historial de compras
            const comprasMock = [
                { 
                    id: 101, 
                    titulo: "iPhone 13 Pro (MOCK)", 
                    precio: 650.00, 
                    fechaCompra: "10/02/2026", 
                    vendedor: "Carlos G.", 
                    estado: "Recibido",
                    badgeColor: "bg-success"
                },
                { 
                    id: 102, 
                    titulo: "Monitor Gaming 27\"", 
                    precio: 180.00, 
                    fechaCompra: "15/03/2026", 
                    vendedor: "Elena M.", 
                    estado: "En camino",
                    badgeColor: "bg-warning text-dark"
                }
            ];

            const usuarioMock = { nombre: "Juan", apellidos: "Pérez", fechaRegistro: "01/09/2025" };

            res.render('usuario/misCompras', {
                title: 'Mis Compras Realizadas',
                usuario: usuarioMock,
                compras: comprasMock
            });
        } catch (error) {
            console.error("Error al cargar mis compras:", error);
            res.status(500).send("Error interno");
        }
    },
    
    // editarPerfil: procesa la actualización de datos (PATCH)
    // De momento lo dejamos preparado pero vacío
    editarPerfil: async (req, res) => {
        res.send('Funcionalidad de edición en desarrollo');
    }
    
};

module.exports = usuariosController;