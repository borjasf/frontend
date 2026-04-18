/* Servicio de productos. Encapsula las llamadas a la API de productos:
   - getProductos(pagina)                    → GET /api/productos?pagina=N
   - buscarProductos(filtros, pagina)         → GET /api/productos/buscar?categoria&texto&estado&precioMax
   - getProducto(id)                          → GET /api/productos/:id
   - getCategorias()                          → GET /api/productos/categorias
   - crearProducto(datos, token)              → POST /api/productos
   - editarProducto(id, datos, token)         → PATCH /api/productos/:id
   - incrementarVisualizaciones(id, token)    → PATCH /api/productos/:id/visualizaciones */
