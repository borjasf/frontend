/* Rutas del perfil de usuario (requieren auth):
   GET  /perfil        → muestra datos del usuario + sus productos en venta
   POST /perfil        → actualiza datos del usuario vía API (PATCH /api/usuarios/:id) */

const express = require('express');
const router = express.Router();
const authMiddleware = require('../src/middleware/authMiddleware');
router.use(authMiddleware); // Aplica el middleware de autenticación a todas las rutas de este router

// Importamos el controlador
const usuariosController = require('../src/controllers/usuariosController');

/* Rutas del perfil de usuario */

// GET /perfil -> Llama a verPerfil en el controlador
router.get('/perfil', usuariosController.verPerfil);

// POST /perfil -> Llama a editarPerfil (para el futuro PATCH)
router.post('/perfil', usuariosController.editarPerfil);

// GET /perfil/mis-ventas -> Llama a misVentas para mostrar los productos que el usuario tiene en venta
router.get('/perfil/mis-ventas', usuariosController.misVentas);

// GET /perfil/mis-compras -> Llama a misCompras para mostrar los productos que el usuario ha comprado
router.get('/perfil/mis-compras', usuariosController.misCompras);

module.exports = router;
