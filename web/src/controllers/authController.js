/* Controlador de autenticación.
   Gestiona el flujo login/logout y registro:
   - login: llama a authService, recibe el JWT y lo guarda en cookie httpOnly; también guarda nombre/id en sesión
   - logout: elimina la cookie jwt y redirige a /login
   - registro: llama a authService para crear el usuario y redirige al login */
