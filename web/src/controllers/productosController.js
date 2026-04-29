/* Controlador de productos. */
const productosService = require('../services/productosService');
const compraventasService = require('../services/compraventasService');
const fs = require('fs');
const path = require('path');

// Función auxiliar para agregar rutaImagen a un producto
const agregarRutaImagen = (producto) => {
    const extensiones = ['.jpg', '.jpeg', '.png', '.gif', '.webp'];
    for (const ext of extensiones) {
        const rutaLocal = path.join(__dirname, `../../public/uploads/${producto.id}_imagen${ext}`);
        if (fs.existsSync(rutaLocal)) {
            producto.rutaImagen = `/uploads/${producto.id}_imagen${ext}`;
            return producto;
        }
    }
    return producto;
};

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
            
            // NUEVO: Agregar rutas de imágenes locales a cada producto
            let arrayProductos = [];
            if (respuestaAPI._embedded) {
                arrayProductos = Object.values(respuestaAPI._embedded)[0] || [];
            } else if (respuestaAPI.content) {
                arrayProductos = respuestaAPI.content;
            } else if (Array.isArray(respuestaAPI)) {
                arrayProductos = respuestaAPI;
            }
            
            arrayProductos.forEach(agregarRutaImagen);
            
            // Cargar las categorías desde la API
            let categorias = [];
            try {
                categorias = await productosService.getCategorias();
            } catch (error) {
                console.warn("No se pudieron cargar las categorías:", error.message);
                // Fallback a categorías vacías - React mostrará el fallback hardcodeado
                categorias = [];
            }

            res.render('productos/listado', {
                title: 'Catálogo de Productos',
                productosInyectados: JSON.stringify(respuestaAPI),
                categoriasInyectadas: JSON.stringify(categorias)
            });

        }catch (error) {
            console.error("Error al cargar el listado de productos:", error);
            res.render('error', { mensaje: 'No se pudieron cargar los productos. Inténtalo más tarde.' });
        }
    },

    detalle: async (req, res) => { 
        try {
            const idProducto = req.params.id;
            let productoVisualizar;

            try {
                // 1. ¡NUEVO! Disparamos el aumento de visualizaciones de forma silenciosa
                await productosService.incrementarVisualizaciones(idProducto);

                // 2. Intentamos pedir el producto real a Java (ya vendrá con el +1)
                productoVisualizar = await productosService.getProducto(idProducto);
                
                // 3. Calculamos dinámicamente si el que mira es el vendedor
                const esVendedor = res.locals.usuario && 
                                  (res.locals.usuario.id === productoVisualizar.vendedor);
                
                // 4. Verificamos si el producto ya ha sido vendido
                const estaVendido = productoVisualizar.vendido === true;

                const fechaPublicacion = productoVisualizar.fechaPublicacion; 
                if (Array.isArray(fechaPublicacion)) {
                    productoVisualizar.fechaPublicacion = `${String(fechaPublicacion[2]).padStart(2,'0')}-${String(fechaPublicacion[1]).padStart(2,'0')}-${fechaPublicacion[0]}`;
                } else if (typeof fechaPublicacion === 'string') {
                    productoVisualizar.fechaPublicacion = fechaPublicacion.substring(0, 10).split('-').reverse().join('-');
                }
                
                // 5. NUEVO: Verificar si existe imagen local uploadada
                const fs = require('fs');
                const path = require('path');
                const extensiones = ['.jpg', '.jpeg', '.png', '.gif', '.webp'];
                let rutaImagen = null;
                
                for (const ext of extensiones) {
                    const rutaLocal = path.join(__dirname, `../../public/uploads/${idProducto}_imagen${ext}`);
                    if (fs.existsSync(rutaLocal)) {
                        rutaImagen = `/uploads/${idProducto}_imagen${ext}`;
                        break;
                    }
                }
                
                // Renderizamos la vista inyectando los datos
                res.render('productos/detalle', { 
                    title: productoVisualizar.titulo,
                    producto: productoVisualizar,
                    rutaImagen: rutaImagen, // Nueva: ruta de la imagen local si existe
                    esVendedor: esVendedor, // Ahora los botones cambian bien
                    estaVendido: estaVendido // Pasamos el estado de venta
                });

            } catch (apiError) {
                // Si Java falla, seguimos usando el Mock de tu compañero
                console.log("⚠️ Usando producto de prueba (Mock) para diseño visual.");

                productoVisualizar = {
                    id: idProducto,
                    titulo: "Bicicleta de Montaña Orbea (MOCK)",
                    precio: 350.00,
                    estado: "BUEN_ESTADO",
                    categoria: { nombre: "Deportes" },
                    descripcion: "Bicicleta en perfecto estado, ideal para rutas de montaña. Cuadro de aluminio, frenos de disco y suspensión delantera. Se vende por falta de uso.",
                    vendedor: { nombre: "Alejandro", email: "a.carrion@um.es" },
                    visualizaciones: 45,
                    envioDisponible: false,
                    fechaPublicacion: "2026-04-15", 
                    lugarRecogida: "Murcia Centro", 
                    imagen: "https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=800",
                    vendido: false
                };

                res.render('productos/detalle', { 
                    title: productoVisualizar.titulo,
                    producto: productoVisualizar,
                    esVendedor: false,
                    estaVendido: false
                });
            }

        } catch (error) {
            console.error("Error fatal al cargar el detalle:", error);
            res.redirect('/productos');
        }
    },
    mostrarCrear: async (req, res) => { 
        // Inventamos un par de categorías temporales para que el formulario funcione visualmente
        const categorias = await productosService.getCategorias();
        res.render('productos/crear', { 
            title: 'Nuevo Producto',
            categorias: categorias
        }); 
    },

    crear: async (req, res) => {
        try {
            const token = req.cookies.jwt;
            const nuevoProducto = {
                titulo: req.body.titulo,
                precio: parseFloat(req.body.precio),
                estado: req.body.estado,
                descripcion: req.body.descripcion,
                idCategoria: req.body.categoria,
                lugarRecogida: req.body.lugarRecogida,
                latitud: parseFloat(req.body.latitud) || 0,
                longitud: parseFloat(req.body.longitud) || 0,
                envioDisponible: req.body.envioDisponible === 'on',
                idVendedor: res.locals.usuario.id
            };

            // Crear el producto en Java
            const idProductoCreado = await productosService.crearProducto(nuevoProducto, token);

            // Si se subió una imagen, renombrarla con el ID del producto
            if (req.file) {
                const fs = require('fs').promises;
                const path = require('path');
                const oldPath = req.file.path;
                const ext = path.extname(req.file.originalname);
                const newPath = path.join(__dirname, `../../public/uploads/${idProductoCreado}_imagen${ext}`);
                
                try {
                    await fs.rename(oldPath, newPath);
                    console.log(`Imagen guardada: ${idProductoCreado}_imagen${ext}`);
                } catch (err) {
                    console.error('Error al renombrar imagen:', err);
                }
            }

            // Si todo va bien, redirigimos al catálogo para ver nuestro nuevo producto
            console.log("Producto creado con éxito. Redirigiendo al catálogo...");
            res.redirect('/productos');

        } catch (error) {
            console.error("Error al guardar el producto:", error);
            // Si se subió un archivo y hay error, eliminarlo
            if (req.file) {
                const fs = require('fs').promises;
                try {
                    await fs.unlink(req.file.path);
                } catch (err) {
                    console.error('Error al eliminar archivo:', err);
                }
            }
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
    comprar: async (req, res) => {
        try {
            // Atrapamos el ID del producto que viene en la propia URL (/productos/:id/comprar)
            const idProducto = req.params.id;
            
            // Extraemos el ID del comprador gracias al middleware de autenticación
            const idComprador = res.locals.usuario.id;
            
            // Recuperamos el Token de la cookie para que Java confíe en nosotros
            const token = req.cookies.jwt;

            console.log(`Procesando compra: Usuario ${idComprador} solicita Producto ${idProducto}`);

            // Lanzamos la orden a la API Java a través del servicio
            await compraventasService.comprar(idProducto, idComprador, token);

            // Redirigimos al usuario a su página de compras
            res.redirect('/perfil/mis-compras');

        } catch (error) {
            console.error("Error al procesar la compra:", error.message);
            
            res.render('error', { 
                mensaje: 'No se ha podido procesar tu solicitud de compra. Es posible que el producto ya esté vendido o haya un problema de conexión con el servidor.',
                statusCode: error.response?.status || 500 
            });
        }
    },
    verCard: async (req, res) => {
        try {
            const producto = await productosService.getProducto(req.params.id);
            const fp = producto.fechaPublicacion;
            if (Array.isArray(fp)) {
                producto.fechaPublicacion = `${String(fp[2]).padStart(2,'0')}-${String(fp[1]).padStart(2,'0')}-${fp[0]}`;
            } else if (typeof fp === 'string') {
                producto.fechaPublicacion = fp.substring(0, 10).split('-').reverse().join('-');
            }
            res.render('productos/cardVentas', { title: producto.titulo, producto });
        } catch (error) {
            res.redirect('/perfil/mis-ventas');
        }
    },

    verCardCompras: async (req, res) => {
        try {
            const producto = await productosService.getProducto(req.params.id);
            const fp = producto.fechaPublicacion;
            if (Array.isArray(fp)) {
                producto.fechaPublicacion = `${String(fp[2]).padStart(2,'0')}-${String(fp[1]).padStart(2,'0')}-${fp[0]}`;
            } else if (typeof fp === 'string') {
                producto.fechaPublicacion = fp.substring(0, 10).split('-').reverse().join('-');
            }
            res.render('productos/cardCompras', { title: producto.titulo, producto });
        } catch (error) {
            res.redirect('/perfil/mis-compras');
        }
    },

    eliminar: async (req, res) => {
        try {
            const idProducto = req.params.id;
            const token = req.cookies.jwt;

            console.log(`Eliminando producto: ${idProducto}`);

            await productosService.deleteProducto(idProducto, token);

            res.redirect('/productos');

        } catch (error) {
            console.error("Error al eliminar el producto:", error.message);
            
            res.render('error', { 
                mensaje: 'No se ha podido eliminar el producto. Es posible que no tengas permiso o haya un problema de conexión con el servidor.',
                statusCode: error.response?.status || 500 
            });
        }
    }
};

module.exports = productosController;