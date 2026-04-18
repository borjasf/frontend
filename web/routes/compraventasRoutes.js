/* Rutas de compraventas del usuario autenticado (todas requieren auth):
   GET /mis-compras   → listado de productos que el usuario ha comprado
   GET /mis-ventas    → listado de productos que el usuario ha vendido */

const express = require('express');
const router = express.Router();

router.get('/mis-compras', function(req, res) {
    res.render('misCompras', { title: 'Mis Compras' });
});

router.get('/mis-ventas', function(req, res) {
    res.render('misVentas', { title: 'Mis Ventas' });
});

module.exports = router;
