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
         // Si es admin, intentar primero con Java para tener un JWT valido
         if (email === 'admin@segundum.com' && password === 'admin123') {
            try {
               const response = await axios.post(`${process.env.ARSO_API_URL}/auth/login`, { email, clave: password });
               return response.data;
            } catch (error) {
               console.warn('Login admin con Java fallo, usando JWT local');
               const payload = {
                  sub: 'admin-001',
                  email: 'admin@segundum.com',
                  nombre: 'Admin',
                  rol: 'ADMINISTRADOR',
                  iat: Math.floor(Date.now() / 1000),
                  exp: Math.floor(Date.now() / 1000) + (24 * 60 * 60) // 24 horas
               };
               const token = jwt.sign(payload, 'secreto');
               return {
                  token: token,
                  identificador: 'admin-001',
                  nombre: 'Admin',
                  rol: 'ADMINISTRADOR'
               };
            }
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
