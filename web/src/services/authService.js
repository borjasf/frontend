/* Servicio de autenticación. Encapsula las llamadas a la API relacionadas con usuarios y auth:
   - login(email, clave)     → POST /auth/login → devuelve { token, identificador, nombre, rol }
   - registro(datos)         → POST /api/usuarios → crea un nuevo usuario */

const axios = require('axios');
const jwt = require('jsonwebtoken');

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
         // BYPASS: Si es admin@segundum.com/admin123, generar JWT localmente
         if (email === 'admin@segundum.com' && password === 'admin123') {
            const payload = {
               sub: 'admin-001',
               email: 'admin@segundum.com',
               nombre: 'Admin',
               rol: 'ADMINISTRADOR',
               iat: Math.floor(Date.now() / 1000),
               exp: Math.floor(Date.now() / 1000) + (24 * 60 * 60) // 24 horas
            };
            const token = jwt.sign(payload, 'secreto');
            console.log('Login admin generado localmente');
            return {
               token: token,
               identificador: 'admin-001',
               nombre: 'Admin',
               rol: 'ADMINISTRADOR'
            };
         }
         
         // Login normal contra Java
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
