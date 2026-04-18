/* Rutas de productos:
   GET  /             → listado público con filtros y paginación (monta componentes React)
   GET  /productos/:id        → detalle de un producto
   GET  /productos/nuevo      → formulario crear producto (requiere auth)
   POST /productos            → crea nuevo producto vía API (requiere auth)
   GET  /productos/:id/editar → formulario editar producto (requiere auth + ser vendedor)
   POST /productos/:id        → actualiza precio/descripción vía API (requiere auth)
   POST /productos/:id/comprar → solicita compra del producto (requiere auth) */

   const express = require('express');
   const router = express.Router();
   
   router.get('/productos/nuevo', function(req, res) {
         res.render('productoNuevo', { title: 'Nuevo Producto' });
   });

   router.get('/productos/:id', function(req, res) {
         res.render('productoDetalle', { title: 'Detalle del Producto' });
   });

   router.get('/productos/:id/editar', function(req, res) {
         res.render('productoEditar', { title: 'Editar Producto' });
   });

   router.post('/productos', function(req, res) {
         res.send('Producto creado (simulado)');
   });

   router.post('/productos/:id', function(req, res) {
         res.send('Producto actualizado (simulado)');
   });

   router.post('/productos/:id/comprar', function(req, res) {
         res.send('Compra solicitada (simulado)');
   });

   module.exports = router;