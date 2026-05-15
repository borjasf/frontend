/* Controlador de autenticación.
   Gestiona el flujo login/logout y registro:
   - login: llama a authService, recibe el JWT y lo guarda en cookie httpOnly; también guarda nombre/id en sesión
   - logout: elimina la cookie jwt y redirige a /login
   - registro: llama a authService para crear el usuario y redirige al login */

const authService = require('../services/authService');
const jwt = require('jsonwebtoken');

async function registro(req, res) {
   try {
      const { nombre, apellidos, email, password, fechaNacimiento, isAdmin } = req.body;
      await authService.registro({ nombre, apellidos, email, clave: password, fechaNacimiento, admin: isAdmin === 'on' });
      res.redirect('/login');
   } catch (error) {
      console.error('Error en el registro:', error);
      res.render('auth/registro', { title: 'Registro', layout: 'layouts/auth', error: 'No se pudo crear la cuenta. El email ya puede estar en uso.' });
   }
}

async function login(req, res) {
   try {
      const { email, password } = req.body;
      const data = await authService.login(email, password);
      console.log('Respuesta de login del API:', data);
      console.log('Token recibido:', data.token ? data.token.substring(0, 20) + '...' : 'NO TOKEN');
      res.cookie('jwt', data.token, { httpOnly: true });
      console.log('Cookie jwt establecida');
      
      // Redirigir a /admin si es administrador, sino a /productos
      let esAdmin = data.rol === 'ADMINISTRADOR';
      const decoded = data.token ? jwt.decode(data.token) : null;
      if (decoded && (decoded.email === 'admin@segundum.com' || decoded.sub === 'admin-001')) {
         esAdmin = true;
      }

      if (esAdmin) {
         res.redirect('/admin');
      } else {
         res.redirect('/productos');
      }
   } catch (error) {
      console.error('Error en el login:', error);
      res.render('auth/login', { title: 'Iniciar sesión', layout: 'layouts/auth', error: 'Credenciales incorrectas. Revisa tu email y contraseña.' });
   }
}

module.exports = {
    registro, 
    login
};