const { crearApiClient } = require('./apiService');

async function getTodasCompraventas(idComprador, idVendedor, token) {
    const api = crearApiClient(token);
    const response = await api.get('/api/compraventas', {
        params: { idComprador, idVendedor }
    });
    return response.data;
}

async function getMisCompras(idComprador, token) {
    const api = crearApiClient(token);
    const response = await api.get(`/api/compraventas/compras/${idComprador}`);
    return response.data;
}

async function getMisVentas(idVendedor, token) {
    const api = crearApiClient(token);
    const response = await api.get(`/api/compraventas/ventas/${idVendedor}`);
    return response.data;
}

async function comprar(idProducto, idComprador, token) {
    const api = crearApiClient(token);
    await api.post('/api/compraventas', { idProducto, idComprador });
}

module.exports = { getTodasCompraventas, getMisCompras, getMisVentas, comprar };
