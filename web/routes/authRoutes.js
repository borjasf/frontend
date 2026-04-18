/* Rutas de autenticación (públicas):
   GET  /login        → muestra formulario de login
   POST /login        → procesa login, guarda JWT en cookie httpOnly
   GET  /logout       → destruye la cookie y redirige a inicio
   GET  /registro     → muestra formulario de registro (opcional)
   POST /registro     → crea nuevo usuario vía API */

const express = require('express');
const router = express.Router();

// Ruta raíz: redirige al login
router.get('/', (req, res) => {
   res.redirect('/login');
});

// Mostrar formulario de login
router.get('/login', (req, res) => {
   res.render('auth/login', { title: 'Formulario de login' });
});

// Procesar login
router.post('/login', (req, res) => {
  res.send('Formulario de login procesado (simulado)');
});

// Logout: elimina la cookie y redirige al login
router.get('/logout', (req, res) => {
  res.clearCookie('jwt');
  res.redirect('/login');
});

// Mostrar formulario de registro
router.get('/registro', (req, res) => {
  res.render('auth/registro', { title: 'Formulario de registro' });
});

// Procesar registro
router.post('/registro', (req, res) => {
  res.send('Formulario de registro procesado (simulado)');
});

// Login con GitHub: redirige a la pasarela Spring que gestiona el OAuth
router.get('/auth/github', (req, res) => {
  res.redirect('http://localhost:9090/oauth2/authorization/github');
});

module.exports = router;
