const { crearApiClient } = require('./apiService');

async function getTodosUsuarios(token) {
    const api = crearApiClient(token);
    const response = await api.get('/api/usuarios');
    return response.data;
}

async function getUsuario(id, token) {
    const api = crearApiClient(token);
    const response = await api.get(`/api/usuarios/${id}`);
    return response.data;
}

async function actualizarUsuario(id, datos, token) {
    const api = crearApiClient(token);
    await api.patch(`/api/usuarios/${id}`, datos);
}

module.exports = { getTodosUsuarios, getUsuario, actualizarUsuario };
