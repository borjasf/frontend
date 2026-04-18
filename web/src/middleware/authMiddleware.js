/* Middleware de autenticación.
   Lee la cookie jwt httpOnly de la request y verifica que existe y es válido.
   Si no hay token o es inválido redirige a /login.
   Si es válido añade los datos del usuario (id, nombre, rol) a res.locals para que las vistas los usen. */
