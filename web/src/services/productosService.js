/* Servicio de productos. 
   VERSIÓN DIRECTA: El .env se mantiene intacto (con el 9090), 
   pero aquí forzamos la ruta al 8083 para saltarnos la pasarela rota. */

const API_BASE_URL = 'http://localhost:8083/productos';

const productosService = {
    obtenerProductos: async (filtros) => {
        try {
            const queryParams = new URLSearchParams();
            if (filtros.page) queryParams.append('pagina', filtros.page);
            if (filtros.categoria) queryParams.append('categoria', filtros.categoria);
            if (filtros.estado) queryParams.append('estado', filtros.estado);
            if (filtros.precioMax) queryParams.append('precioMax', filtros.precioMax);
            if (filtros.texto) queryParams.append('texto', filtros.texto);

            const tieneFiltros = filtros.categoria || filtros.estado || filtros.precioMax || filtros.texto;
            
            const url = tieneFiltros 
                ? `${API_BASE_URL}/buscar?${queryParams.toString()}`
                : `${API_BASE_URL}?${queryParams.toString()}`;

            console.log(`Node.js saltándose Zuul y pidiendo directo a Java en: ${url}`);

            const response = await fetch(url);

            if (!response.ok) {
                throw new Error(`Error en la API de Java. Código: ${response.status}`);
            }

            return await response.json();

        } catch (error) {
            console.error("❌ Error en productosService.obtenerProductos:", error.message);
            return { _embedded: { productos: [] }, page: { totalElements: 0 } }; 
        }
    },
    
    getProducto: async (id) => {},
    getCategorias: async () => {},
    crearProducto: async (datos) => {
        try {
            console.log(`Enviando NUEVO PRODUCTO a Java en: ${API_BASE_URL}`, datos);

            const response = await fetch(API_BASE_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(datos)
            });

            if (!response.ok) {
                throw new Error(`Java rechazó la creación. Código: ${response.status}`);
            }

            return await response.json();
        } catch (error) {
            console.error("Error en productosService.crearProducto:", error.message);
            throw error; 
        }
    },
    editarProducto: async (id, datos, token) => {},
    incrementarVisualizaciones: async (id, token) => {}
};

module.exports = productosService;