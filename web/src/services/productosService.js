/* Servicio de productos. 
   VERSIÓN DIRECTA: El .env se mantiene intacto (con el 9090), 
   pero aquí forzamos la ruta al 8083 para saltarnos la pasarela rota. */

const API_BASE_URL = 'http://localhost:9090/api/productos';

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
            console.error("Error en productosService.obtenerProductos:", error.message);
            return { _embedded: { productos: [] }, page: { totalElements: 0 } }; 
        }
    },
    
    getProducto: async (id) => {
        try {
            const url = `${API_BASE_URL}/${id}`;
            console.log(`Solicitando detalle a Java en: ${url}`);
            
            const response = await fetch(url);

            if (!response.ok) {
                throw new Error(`Java no encontró el producto. Código: ${response.status}`);
            }

            return await response.json();
        } catch (error) {
            console.error(` Error en getProducto (${id}):`, error.message);
            throw error; // Lanzamos el error para que el Controlador decida qué hacer
        }
    },
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
    editarProducto: async (id, datos, token) => {
        //hacemos PATCH a  a ${API_BASE_URL}/${id} con { precio, descripcion } en el body
        console.log(`Intentando editar producto ${id} con datos:`, datos);
        try {
            const url = `${API_BASE_URL}/${id}`;
            const response = await fetch(url, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    // añadimos el header de autorización con el token JWT
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(datos)
            });
            // Si la respuesta no es OK, lanzamos un error con el código de estado
            if (!response.ok) {
                throw new Error(`Java rechazó la edición. Código: ${response.status}`);
            }
        } catch (error) {
            console.error(`Error en productosService.editarProducto (${id}):`, error.message);
            throw error;
        }

    },
    incrementarVisualizaciones: async (id, token) => {}
};

module.exports = productosService;