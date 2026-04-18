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

// Importamos el Controlador ("El Maitre")
const productosController = require('../src/controllers/productosController');

// 1. PANTALLA PRINCIPAL: Catálogo de productos
router.get('/productos', productosController.listado);

// ---------------------------------------------------------
// HUECOS PREPARADOS PARA EL RESTO DE FUNCIONES
// ---------------------------------------------------------

// IMPORTANTE: Las rutas fijas como '/nuevo' deben ir SIEMPRE ANTES que las rutas con parámetros dinámicos como '/:id'
// Mostrar formulario de crear
router.get('/productos/nuevo', productosController.mostrarCrear);

// Procesar la creación (vía POST)
router.post('/productos', productosController.crear);

// Ver detalle de un producto concreto
router.get('/productos/:id', productosController.detalle);

// Mostrar formulario de edición
router.get('/productos/:id/editar', productosController.mostrarEditar);

// Procesar la edición
router.post('/productos/:id', productosController.editar);

// Solicitar compra
router.post('/productos/:id/comprar', productosController.comprar);

module.exports = router;