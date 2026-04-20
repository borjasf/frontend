/* Servicio de autenticación. Encapsula las llamadas a la API relacionadas con usuarios y auth:
   - login(email, clave)     → POST /auth/login → devuelve { token, identificador, nombre, rol }
   - registro(datos)         → POST /api/usuarios → crea un nuevo usuario */

const axios = require('axios');

async function registro(datos) {
    try {
        const response = await axios.post(`${process.env.ARSO_API_URL}/api/usuarios`, datos);
        return response.data;
    } catch (error) {
        throw error;
    }
}

async function login(email, password) {
      try {
         const response = await axios.post(`${process.env.ARSO_API_URL}/auth/login`, { email, clave: password });
         return response.data;
      } catch (error) {
         throw error;
      }
}

module.exports = {
    registro, 
    login
};
