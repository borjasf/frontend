/* Middleware de rol administrador.
   Se aplica después de authMiddleware. Comprueba que res.locals.usuario.rol === 'admin'.
   Si no es admin devuelve 403 o redirige a la página principal. */

function adminMiddleware(req, res, next) {
   if (res.locals.usuario && res.locals.usuario.rol === 'ADMINISTRADOR') {
      next();
   }
   else {
      res.status(403).send('Acceso denegado. Solo administradores pueden acceder a esta página.');
      // O redirigir a la página principal:
      // res.redirect('/');
   }
}

module.exports = adminMiddleware;