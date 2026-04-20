const axios = require('axios');

function crearApiClient(token) {
    return axios.create({
        baseURL: process.env.ARSO_API_URL,
        headers: token ? { Authorization: `Bearer ${token}` } : {}
    });
}

module.exports = { crearApiClient };
