/* Controlador de productos.
   - listado: obtiene productos paginados/filtrados de la API y renderiza listado.hbs con los datos (React toma el relevo en cliente)
   - detalle: obtiene un producto por id, incrementa visualizaciones vía API y renderiza detalle.hbs
   - mostrarCrear / crear: renderiza y procesa el formulario de nuevo producto
   - mostrarEditar / editar: renderiza y procesa el formulario de edición (precio/descripción)
   - comprar: llama a compraventasService para registrar la compra */




// Importamos el servicio que se comunicará con la API de Java
const productosService = require('../services/productosService');

const productosController = {

    // PANTALLA PRINCIPAL: Catálogo de productos
    listado: async (req, res) => {
        try {
            // A. Recoger los filtros que el usuario haya puesto en la URL
            // req.query captura parámetros como ?categoria=1&precioMax=50
            const filtros = {
                page: req.query.page || 0,
                categoria: req.query.categoria || '',
                estado: req.query.estado || '',
                precioMax: req.query.precioMax || '',
                texto: req.query.q || ''
            };

            // B. Llamar a la "cocina" para obtener los productos de Java
            // Usamos 'await' porque es una petición asíncrona (tarda un poco)
            const respuestaAPI = await productosService.obtenerProductos(filtros);

            // C. Inyectar los datos en la vista listado.hbs
            res.render('productos/listado', {
                title: 'Catálogo de Productos',
                // Convertimos el objeto a una cadena de texto (String) para que 
                // Handlebars y React puedan leerlo fácilmente al cargar la página
                productosInyectados: JSON.stringify(respuestaAPI)
            });

        } catch (error) {
            console.error("Error al cargar el listado de productos:", error);
            res.render('error', { mensaje: 'No se pudieron cargar los productos. Inténtalo más tarde.' });
        }
    },

    // ---------------------------------------------------------
    // HUECOS PREPARADOS PARA LAS SIGUIENTES TAREAS
    // ---------------------------------------------------------

    detalle: async (req, res) => {
        // Aquí irá el código para ver un producto concreto
        res.send('Página de detalle en construcción');
    },

    mostrarCrear: (req, res) => {
        // Aquí se mostrará el formulario de crear
        res.render('productos/crear', { title: 'Nuevo Producto' });
    },

    crear: async (req, res) => {
        // Aquí se procesarán los datos del formulario de crear
    },

    mostrarEditar: async (req, res) => {
        // Aquí se mostrará el formulario de edición
    },

    editar: async (req, res) => {
        // Aquí se procesarán los datos de edición
    },

    comprar: async (req, res) => {
        // Aquí se procesará la compra
    }
};

module.exports = productosController;