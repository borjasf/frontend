/* Controlador de productos. */
const productosService = require('../services/productosService');

const productosController = {
    // PANTALLA PRINCIPAL: Catálogo de productos
    listado: async (req, res) => {
        try {
            const filtros = {
                page: req.query.page || 0,
                categoria: req.query.categoria || '',
                estado: req.query.estado || '',
                precioMax: req.query.precioMax || '',
                texto: req.query.q || ''
            };

            const respuestaAPI = await productosService.obtenerProductos(filtros);

            res.render('productos/listado', {
                title: 'Catálogo de Productos',
                productosInyectados: JSON.stringify(respuestaAPI)
            });

        } catch (error) {
            console.error("Error al cargar el listado de productos:", error);
            res.render('error', { mensaje: 'No se pudieron cargar los productos. Inténtalo más tarde.' });
        }
    },

    detalle: async (req, res) => { 
        try {
            const idProducto = req.params.id;
            let productoVisualizar;

            try {
                // 1. Intentamos pedir el producto real a Java
                productoVisualizar = await productosService.getProducto(idProducto);
            } catch (apiError) {
                // 2. Si Java falla, usamos un Mock con TODOS los campos del enunciado
                console.log("⚠️ Usando producto de prueba (Mock) para diseño visual.");
                productoVisualizar = {
                    id: idProducto,
                    titulo: "Bicicleta de Montaña Orbea (MOCK)",
                    precio: 350.00,
                    estado: "BUENO",
                    categoria: { nombre: "Deportes" },
                    descripcion: "Bicicleta en perfecto estado, ideal para rutas de montaña. Cuadro de aluminio, frenos de disco y suspensión delantera. Se vende por falta de uso.",
                    vendedor: { nombre: "Alejandro", email: "a.carrion@um.es" },
                    visualizaciones: 45,
                    envioDisponible: false,
                    fechaPublicacion: "2026-04-15", // ¡NUEVO!
                    lugarRecogida: "Murcia Centro", // ¡NUEVO!
                    imagen: "https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=800"
                };
            }

            // 3. Renderizamos la vista inyectando los datos
            res.render('productos/detalle', { 
                title: productoVisualizar.titulo,
                producto: productoVisualizar,
                esVendedor: false // Temporal: Simulamos que somos el comprador
            });

        } catch (error) {
            console.error("Error fatal al cargar el detalle:", error);
            res.redirect('/productos');
        }
    },
    mostrarCrear: (req, res) => { 
        // Inventamos un par de categorías temporales para que el formulario funcione visualmente
        const categoriasTemporales = [
            { id: '1', nombre: 'Electrónica' },
            { id: '2', nombre: 'Deportes' },
            { id: '3', nombre: 'Hogar' }
        ];
        res.render('productos/crear', { 
            title: 'Nuevo Producto',
            categorias: categoriasTemporales
        }); 
    },

    crear: async (req, res) => {
        try {
            // 1. Atrapamos los datos que vienen del HTML (req.body)
            const nuevoProducto = {
                titulo: req.body.titulo,
                precio: parseFloat(req.body.precio),
                estado: req.body.estado,
                descripcion: req.body.descripcion,
                categoria_id: req.body.categoria, 
                envioDisponible: req.body.envioDisponible === 'on' // El checkbox devuelve 'on' si está marcado
            };

            // 2. Se lo pasamos al servicio para que viaje a Java
            await productosService.crearProducto(nuevoProducto);

            // 3. Si todo va bien, redirigimos al catálogo para ver nuestro nuevo producto
            console.log("✅ Producto creado con éxito. Redirigiendo al catálogo...");
            res.redirect('/productos');

        } catch (error) {
            console.error("Error al guardar el producto:", error);
            // Si falla, recargamos la página de crear pero podríamos añadir un mensaje de error
            res.redirect('/productos/crear'); 
        }
    },
    mostrarEditar: async (req, res) => {
        try {
            //Primero leemos req.params.id y req.cookies.jwt
            const idProducto = req.params.id;
            const tokenJWT = req.cookies.jwt;
            // Llamar a productosService.getProducto(id) para obtener los datos actuales
            const productoActual = await productosService.getProducto(idProducto);
            // Renderizar la vista de edición con los datos actuales
            res.render('productos/editar', {
                title: `Editar ${productoActual.titulo}`,
                producto: productoActual
            });
        } catch (error) {
            console.error("Error al cargar el producto para editar:", error);
            res.redirect('/productos');
        }
    },
    editar: async (req, res) => {
        try {
             //Primero leemos req.params.id y req.cookies.jwt
            const idProducto = req.params.id;
            const tokenJWT = req.cookies.jwt;
            // del body leemos los campos que queremos actualizar (precio y descripción)
            const datosActualizados = {
                precio: parseFloat(req.body.precio),
                descripcion: req.body.descripcion
            };
            // Llamar a productosService.editarProducto(id, datos, token) para enviar los cambios a Java
            await productosService.editarProducto(idProducto, datosActualizados, tokenJWT);
            // Si todo va bien, redirigimos al catálogo para ver los cambios
            console.log("Producto editado con éxito. Redirigiendo al catálogo...");
            //Redirigimos a la página de detalle del producto editado para ver los cambios reflejados si ha salido bien
            res.redirect(`/productos/${idProducto}`);
        } catch (error) {
            console.error("Error al actualizar el producto:", error);
            // Si falla, recargamos la página de edición pero podríamos añadir un mensaje de error
            res.redirect(`/productos/${req.params.id}/editar`);
        }
    },
    comprar: async (req, res) => {}
};

module.exports = productosController;