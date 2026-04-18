/* Servicio de autenticación. Encapsula las llamadas a la API relacionadas con usuarios y auth:
   - login(email, clave)     → POST /auth/login → devuelve { token, identificador, nombre, rol }
   - registro(datos)         → POST /api/usuarios → crea un nuevo usuario */
