/* Rutas del perfil de usuario (requieren auth):
   GET  /perfil        → muestra datos del usuario + sus productos en venta
   POST /perfil        → actualiza datos del usuario vía API (PATCH /api/usuarios/:id) */

const express = require('express');
const router = express.Router();

router.get('/perfil', function(req, res) {
    res.render('perfil', { title: 'Perfil de Usuario' });
});

router.post('/perfil', function(req, res) {
    res.send('Perfil actualizado (simulado)');
});

module.exports = router;
