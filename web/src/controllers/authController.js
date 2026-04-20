/* Controlador de autenticación.
   Gestiona el flujo login/logout y registro:
   - login: llama a authService, recibe el JWT y lo guarda en cookie httpOnly; también guarda nombre/id en sesión
   - logout: elimina la cookie jwt y redirige a /login
   - registro: llama a authService para crear el usuario y redirige al login */

const authService = require('../services/authService');

async function registro(req, res) {
   try {
         const { nombre, apellidos,email, password, fechaNacimiento, isAdmin } = req.body;
         await authService.registro({
        nombre,
        apellidos,
        email,
        clave: password,
        fechaNacimiento,
        admin: isAdmin === 'on'
      });
         res.redirect('/login');
      }catch (error) {
         console.error('Error en el registro:', error);
         res.status(500).send('Error al registrar el usuario');
      }
}

async function login(req, res) {
   try {
      const { email, password } = req.body;
      const data = await authService.login(email, password);
      res.cookie('jwt', data.token, { httpOnly: true });
      res.redirect('/productos'); // Redirige al catálogo tras el login
   } catch (error) {
      console.error('Error en el login:', error);
      res.status(401).send('Credenciales inválidas');
   }
}

module.exports = {
    registro, 
    login
};