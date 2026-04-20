/* Rutas de autenticación (públicas):
   GET  /login        → muestra formulario de login
   POST /login        → procesa login, guarda JWT en cookie httpOnly
   GET  /logout       → destruye la cookie y redirige a inicio
   GET  /registro     → muestra formulario de registro (opcional)
   POST /registro     → crea nuevo usuario vía API */

const express = require('express');
const router = express.Router();
const authController = require('../src/controllers/authController');
// Ruta raíz: redirige al login
router.get('/', (req, res) => {
   res.redirect('/productos');
});

// Mostrar formulario de login
router.get('/login', (req, res) => {
   res.render('auth/login', { title: 'Formulario de login', layout: 'layouts/auth' });
});

// Procesar login
router.post('/login', authController.login);
// Logout: elimina la cookie y redirige al login
router.get('/logout', (req, res) => {
  res.clearCookie('jwt');
  res.redirect('/login');
});

// Mostrar formulario de registro
router.get('/registro', (req, res) => {
  res.render('auth/registro', { title: 'Formulario de registro', layout: 'layouts/auth' });
});

// Procesar registro
router.post('/registro', authController.registro);

// Login con GitHub: redirige a la pasarela Spring que gestiona el OAuth
router.get('/auth/github', (req, res) => {
  res.redirect('http://localhost:9090/oauth2/authorization/github');
});

// Callback de GitHub OAuth: Spring redirige aquí con el token JWT
router.get('/auth/github/callback', (req, res) => {
  const token = req.query.token;
  if (!token) return res.redirect('/login?error=github');
  res.cookie('jwt', token, { httpOnly: true });
  res.redirect('/productos');
});

module.exports = router;
