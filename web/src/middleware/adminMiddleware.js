/* Middleware de rol administrador.
   Se aplica después de authMiddleware. Comprueba que res.locals.usuario.rol === 'admin'.
   Si no es admin devuelve 403 o redirige a la página principal. */
