/* Cliente HTTP base para comunicarse con la API ArSo (gateway en localhost:9090).
   Crea una instancia de axios con la baseURL de ARSO_API_URL y gestiona:
   - Inyección del JWT en la cabecera Authorization: Bearer <token>
   - Manejo centralizado de errores de red/API (40x, 50x)
   Todos los demás servicios importan esta instancia en lugar de axios directamente. */
