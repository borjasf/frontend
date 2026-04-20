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
const authMiddleware = require('../src/middleware/authMiddleware');

// Importamos el Controlador desde la carpeta 'src'
const productosController = require('../src/controllers/productosController');

// rutas estáticas (Fijas, siempre van primero)
router.get('/productos', productosController.listado);

// Mostrar formulario de crear (Hacemos que coincida con tu crear.hbs)
router.get('/productos/crear', authMiddleware, productosController.mostrarCrear);

// Procesar la creación cuando se envía el formulario (vía POST)
router.post('/productos/crear', authMiddleware, productosController.crear);


// Rutas dinámicas (Con parámetros como :id, siempre al final)
router.get('/productos/:id', productosController.detalle);
router.get('/productos/:id/editar', authMiddleware, productosController.mostrarEditar);
router.post('/productos/:id/editar', authMiddleware, productosController.editar);
router.post('/productos/:id/comprar', authMiddleware, productosController.comprar);

module.exports = router;