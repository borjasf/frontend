/* Controlador de autenticación.
   Gestiona el flujo login/logout y registro:
   - login: llama a authService, recibe el JWT y lo guarda en cookie httpOnly; también guarda nombre/id en sesión
   - logout: elimina la cookie jwt y redirige a /login
   - registro: llama a authService para crear el usuario y redirige al login */

const authService = require('../services/authService');

async function registro(req, res) {
   try {
      const { nombre, apellidos, email, password, fechaNacimiento, isAdmin } = req.body;
      await authService.registro({ nombre, apellidos, email, clave: password, fechaNacimiento, admin: isAdmin === 'on' });
      res.redirect('/login');
   } catch (error) {
      //Creamos un mensaje con el mensaje de error que venga del backend, o un mensaje genérico si no está disponible o es un mensaje que 
      // el usuario no querería ver (como un error de validación detallado)
      const mensajeError = error.response?.data?.message || error.response?.data || 'No se pudo crear la cuenta.';
      console.error('Error en el registro:', error);
      res.render('auth/registro', { title: 'Registro', layout: 'layouts/auth', error: mensajeError });
   }
}

async function login(req, res) {
   try {
      const { email, password } = req.body;
      const data = await authService.login(email, password);
      res.cookie('jwt', data.token, { httpOnly: true });

      // Redirigir a /admin si es administrador, sino a /productos
      const esAdmin = data.rol === 'ADMINISTRADOR';

      if (esAdmin) {
         res.redirect('/admin');
      } else {
         res.redirect('/productos');
      }
   } catch (error) {
      const mensajeError = error.response?.data?.message || error.response?.data || 'Credenciales incorrectas. Revisa tu email y contraseña.';
      console.error('Error en el login:', error);
      res.render('auth/login', { title: 'Iniciar sesión', layout: 'layouts/auth', error: mensajeError });
   }
}

module.exports = {
    registro, 
    login
};