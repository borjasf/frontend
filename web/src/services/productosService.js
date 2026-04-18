/* Servicio de productos. Encapsula las llamadas a la API de productos:
   - getProductos(pagina)                    → GET /api/productos?pagina=N
   - buscarProductos(filtros, pagina)         → GET /api/productos/buscar?categoria&texto&estado&precioMax
   - getProducto(id)                          → GET /api/productos/:id
   - getCategorias()                          → GET /api/productos/categorias
   - crearProducto(datos, token)              → POST /api/productos
   - editarProducto(id, datos, token)         → PATCH /api/productos/:id
   - incrementarVisualizaciones(id, token)    → PATCH /api/productos/:id/visualizaciones */



/* Servicio de productos. Encapsula las llamadas a la API de Java (Zuul en el puerto 8090) */

// La URL base de vuestra pasarela de microservicios
const API_BASE_URL = 'http://localhost:8090/api/productos';

const productosService = {

    // 1. Obtener listado general o filtrado (El que usa nuestro Controlador)
    obtenerProductos: async (filtros) => {
        try {
            // URLSearchParams es una herramienta nativa para construir parámetros (?llave=valor)
            const queryParams = new URLSearchParams();
            
            if (filtros.page) queryParams.append('pagina', filtros.page);
            if (filtros.categoria) queryParams.append('categoria', filtros.categoria);
            if (filtros.estado) queryParams.append('estado', filtros.estado);
            if (filtros.precioMax) queryParams.append('precioMax', filtros.precioMax);
            if (filtros.texto) queryParams.append('texto', filtros.texto);

            // Comprobamos si el usuario ha usado algún filtro
            const tieneFiltros = filtros.categoria || filtros.estado || filtros.precioMax || filtros.texto;
            
            // Decidimos a qué endpoint de Java llamar basándonos en si hay filtros o no
            const url = tieneFiltros 
                ? `${API_BASE_URL}/buscar?${queryParams.toString()}`
                : `${API_BASE_URL}?pagina=${filtros.page || 0}`;

            console.log(`📡 Node.js pidiendo datos a Java en: ${url}`);

            // Hacemos la llamada por GET usando fetch
            const response = await fetch(url);

            if (!response.ok) {
                throw new Error(`Error en la API de Java. Código: ${response.status}`);
            }

            // Convertimos la respuesta que nos da Java a un objeto JSON de JavaScript
            const data = await response.json();
            return data;

        } catch (error) {
            console.error("❌ Error en productosService.obtenerProductos:", error.message);
            // Si Java está apagado o falla, devolvemos una estructura vacía para que la web no colapse
            return { contenido: [], totalPages: 0 }; 
        }
    },

    // ---------------------------------------------------------
    // HUECOS PREPARADOS PARA LAS DEMÁS FUNCIONES DE TU COMPAÑERO
    // ---------------------------------------------------------

    getProducto: async (id) => {
        // -> GET /api/productos/:id
    },

    getCategorias: async () => {
        // -> GET /api/productos/categorias
    },

    crearProducto: async (datos, token) => {
        // -> POST /api/productos (Requiere meter el token en la cabecera Authorization)
    },

    editarProducto: async (id, datos, token) => {
        // -> PATCH /api/productos/:id
    },

    incrementarVisualizaciones: async (id, token) => {
        // -> PATCH /api/productos/:id/visualizaciones
    }
};

module.exports = productosService;