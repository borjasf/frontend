/* Agrega y exporta todas las rutas de la aplicación.
   Importa authRoutes, productosRoutes, compraventasRoutes, usuariosRoutes y adminRoutes
   y los monta en el router principal que usa app.js. */

const express = require('express');
const router = express.Router();

const authRoutes = require('./authRoutes');
const productosRoutes = require('./productosRoutes');
const compraventasRoutes = require('./compraventasRoutes');
const usuariosRoutes = require('./usuariosRoutes');
const adminRoutes = require('./adminRoutes');

router.use('/', authRoutes);
router.use('/', productosRoutes);
router.use('/', compraventasRoutes);
router.use('/', usuariosRoutes);
router.use('/admin', adminRoutes);

module.exports = router;