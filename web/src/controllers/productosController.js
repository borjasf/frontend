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

    detalle: async (req, res) => { res.send('Página de detalle en construcción'); },
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
    mostrarEditar: async (req, res) => {},
    editar: async (req, res) => {},
    comprar: async (req, res) => {}
};

module.exports = productosController;