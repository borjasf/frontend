/* Rutas exclusivas del administrador (requieren auth + rol admin):
   GET /admin/usuarios          → lista todos los usuarios del sistema
   GET /admin/compraventas      → lista todas las compraventas (filtrable por comprador/vendedor) */

const express = require('express');
const router = express.Router();
const authMiddleware = require('../src/middleware/authMiddleware');
const adminController = require('../src/controllers/adminController');
const adminMiddleware = require('../src/middleware/adminMiddleware');

router.use(authMiddleware); // Aplica el middleware de autenticación a todas las rutas de este router
router.use(adminMiddleware); // Aplica el middleware de rol admin a todas las rutas de este router

router.get('/usuarios', adminController.listarUsuarios);
router.get('/compraventas', adminController.listarCompraventas);

module.exports = router;