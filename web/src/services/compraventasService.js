/* Servicio de compraventas. Encapsula las llamadas a la API de compraventas:
   - comprar(idProducto, idComprador, token)  → POST /api/compraventas
   - getMisCompras(idComprador, token)         → GET /api/compraventas/compras/:idComprador
   - getMisVentas(idVendedor, token)           → GET /api/compraventas/ventas/:idVendedor
   - getTodasCompraventas(filtros, token)      → GET /api/compraventas?idComprador=&idVendedor= (solo admin) */
