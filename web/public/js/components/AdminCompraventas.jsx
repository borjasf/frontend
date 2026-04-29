// Componente React para listar compraventas en admin panel
const { useState, useMemo } = React;

function AdminCompraventas({ compraventas: compraventasInicial = [] }) {
    const [busqueda, setBusqueda] = useState('');
    const [ordenar, setOrdenar] = useState('fecha');
    const [filtroComprador, setFiltroComprador] = useState('');
    const [filtroVendedor, setFiltroVendedor] = useState('');

    // Filtrar
    const compraventasFiltradas = useMemo(() => {
        return compraventasInicial.filter(c => {
            const coincidenBusqueda = !busqueda || 
                c.IDPRODUCTO?.toString().includes(busqueda) ||
                c.NOMBREPRODUCTO?.toLowerCase().includes(busqueda.toLowerCase()) ||
                c.NOMBRE_COMPRADOR?.toLowerCase().includes(busqueda.toLowerCase()) ||
                c.NOMBRE_VENDEDOR?.toLowerCase().includes(busqueda.toLowerCase());
            
            const coincidenComprador = !filtroComprador || c.IDCOMPRADOR?.toString() === filtroComprador;
            const coincidenVendedor = !filtroVendedor || c.IDVENDEDOR?.toString() === filtroVendedor;
            
            return coincidenBusqueda && coincidenComprador && coincidenVendedor;
        });
    }, [busqueda, filtroComprador, filtroVendedor, compraventasInicial]);

    // Ordenar
    const compraventasOrdenadas = useMemo(() => {
        const copia = [...compraventasFiltradas];
        if (ordenar === 'fecha') {
            copia.sort((a, b) => new Date(b.FECHACOMPRA) - new Date(a.FECHACOMPRA));
        } else if (ordenar === 'precio') {
            copia.sort((a, b) => (b.PRECIO || 0) - (a.PRECIO || 0));
        } else if (ordenar === 'producto') {
            copia.sort((a, b) => (a.NOMBREPRODUCTO || '').localeCompare(b.NOMBREPRODUCTO || ''));
        }
        return copia;
    }, [compraventasFiltradas, ordenar]);

    return (
        <div className="admin-compraventas">
            <div className="filters-container">
                <div className="search-box">
                    <i className="fa-solid fa-search"></i>
                    <input
                        type="text"
                        placeholder="Buscar por producto, comprador o vendedor..."
                        value={busqueda}
                        onChange={(e) => setBusqueda(e.target.value)}
                    />
                </div>

                <div className="filters-row">
                    <div className="filter-group">
                        <label>Comprador:</label>
                        <input
                            type="text"
                            placeholder="ID comprador..."
                            value={filtroComprador}
                            onChange={(e) => setFiltroComprador(e.target.value)}
                        />
                    </div>

                    <div className="filter-group">
                        <label>Vendedor:</label>
                        <input
                            type="text"
                            placeholder="ID vendedor..."
                            value={filtroVendedor}
                            onChange={(e) => setFiltroVendedor(e.target.value)}
                        />
                    </div>

                    <div className="sort-select">
                        <select value={ordenar} onChange={(e) => setOrdenar(e.target.value)}>
                            <option value="fecha">Ordenar por: Fecha (reciente)</option>
                            <option value="precio">Ordenar por: Precio (mayor)</option>
                            <option value="producto">Ordenar por: Producto</option>
                        </select>
                    </div>
                </div>
            </div>

            <div className="info-count">
                <span>{compraventasOrdenadas.length} transacciones encontradas</span>
            </div>

            {compraventasOrdenadas.length > 0 ? (
                <div className="table-responsive">
                    <table className="table table-hover">
                        <thead className="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Producto</th>
                                <th>Comprador</th>
                                <th>Vendedor</th>
                                <th className="text-center">Precio</th>
                                <th>Fecha</th>
                            </tr>
                        </thead>
                        <tbody>
                            {compraventasOrdenadas.map((compra) => (
                                <tr key={compra.ID}>
                                    <td className="mono">{compra.ID}</td>
                                    <td>
                                        <span className="producto-name">{compra.NOMBREPRODUCTO}</span>
                                        <br />
                                        <small className="text-muted">ID: {compra.IDPRODUCTO}</small>
                                    </td>
                                    <td>
                                        <span>{compra.NOMBRE_COMPRADOR}</span>
                                        <br />
                                        <small className="text-muted">ID: {compra.IDCOMPRADOR}</small>
                                    </td>
                                    <td>
                                        <span>{compra.NOMBRE_VENDEDOR}</span>
                                        <br />
                                        <small className="text-muted">ID: {compra.IDVENDEDOR}</small>
                                    </td>
                                    <td className="text-center">
                                        <span className="badge bg-success">${compra.PRECIO?.toFixed(2) || '0.00'}</span>
                                    </td>
                                    <td className="date">
                                        {new Date(compra.FECHACOMPRA).toLocaleDateString('es-ES', {
                                            year: 'numeric',
                                            month: 'short',
                                            day: 'numeric',
                                            hour: '2-digit',
                                            minute: '2-digit'
                                        })}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                <div className="empty-state">
                    <i className="fa-solid fa-inbox"></i>
                    <p>No hay transacciones que coincidan con los filtros</p>
                </div>
            )}
        </div>
    );
}
