/* Rutas exclusivas del administrador (requieren auth + rol admin):
   GET /admin/usuarios          → lista todos los usuarios del sistema
   GET /admin/compraventas      → lista todas las compraventas (filtrable por comprador/vendedor) */

const express = require('express');
const router = express.Router();

router.get('/usuarios', (req, res) => {
   res.render('admin_usuarios', { title: 'Gestión de Usuarios' });
});

router.get('/compraventas', (req, res) => {
   res.render('admin_compraventas', { title: 'Gestión de Compraventas' });
});

module.exports = router;