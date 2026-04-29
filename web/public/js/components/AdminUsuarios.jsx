// Componente React para listar usuarios en admin panel
const { useState, useMemo } = React;

function AdminUsuarios({ usuarios: usuariosInicial = [] }) {
    const [busqueda, setBusqueda] = useState('');
    const [ordenar, setOrdenar] = useState('nombre');

    // Filtrar por búsqueda
    const usuariosFiltrados = useMemo(() => {
        return usuariosInicial.filter(u =>
            u.NOMBRE?.toLowerCase().includes(busqueda.toLowerCase()) ||
            u.APELLIDOS?.toLowerCase().includes(busqueda.toLowerCase()) ||
            u.EMAIL?.toLowerCase().includes(busqueda.toLowerCase()) ||
            u.ID?.toString().includes(busqueda)
        );
    }, [busqueda, usuariosInicial]);

    // Ordenar
    const usuariosOrdenados = useMemo(() => {
        const copia = [...usuariosFiltrados];
        if (ordenar === 'nombre') {
            copia.sort((a, b) => (a.NOMBRE || '').localeCompare(b.NOMBRE || ''));
        } else if (ordenar === 'email') {
            copia.sort((a, b) => (a.EMAIL || '').localeCompare(b.EMAIL || ''));
        } else if (ordenar === 'compras') {
            copia.sort((a, b) => (b.CONTADORCOMPRAS || 0) - (a.CONTADORCOMPRAS || 0));
        } else if (ordenar === 'ventas') {
            copia.sort((a, b) => (b.CONTADORVENTAS || 0) - (a.CONTADORVENTAS || 0));
        }
        return copia;
    }, [usuariosFiltrados, ordenar]);

    return (
        <div className="admin-usuarios">
            <div className="search-filters">
                <div className="search-box">
                    <i className="fa-solid fa-search"></i>
                    <input
                        type="text"
                        placeholder="Buscar por nombre, email o ID..."
                        value={busqueda}
                        onChange={(e) => setBusqueda(e.target.value)}
                    />
                </div>

                <div className="sort-select">
                    <select value={ordenar} onChange={(e) => setOrdenar(e.target.value)}>
                        <option value="nombre">Ordenar por: Nombre</option>
                        <option value="email">Ordenar por: Email</option>
                        <option value="compras">Ordenar por: Compras (mayor)</option>
                        <option value="ventas">Ordenar por: Ventas (mayor)</option>
                    </select>
                </div>
            </div>

            <div className="info-count">
                <span>{usuariosOrdenados.length} usuarios encontrados</span>
            </div>

            {usuariosOrdenados.length > 0 ? (
                <div className="table-responsive">
                    <table className="table table-hover">
                        <thead className="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Apellidos</th>
                                <th>Email</th>
                                <th className="text-center">Compras</th>
                                <th className="text-center">Ventas</th>
                                <th className="text-center">Rol</th>
                            </tr>
                        </thead>
                        <tbody>
                            {usuariosOrdenados.map((usuario) => (
                                <tr key={usuario.ID}>
                                    <td className="mono">{usuario.ID}</td>
                                    <td>{usuario.NOMBRE}</td>
                                    <td>{usuario.APELLIDOS}</td>
                                    <td className="email">{usuario.EMAIL}</td>
                                    <td className="text-center">
                                        <span className="badge bg-info">{usuario.CONTADORCOMPRAS || 0}</span>
                                    </td>
                                    <td className="text-center">
                                        <span className="badge bg-success">{usuario.CONTADORVENTAS || 0}</span>
                                    </td>
                                    <td className="text-center">
                                        {usuario.ESADMIN === 1 ? (
                                            <span className="badge bg-danger">Admin</span>
                                        ) : (
                                            <span className="badge bg-secondary">Usuario</span>
                                        )}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                <div className="empty-state">
                    <i className="fa-solid fa-inbox"></i>
                    <p>No hay usuarios que coincidan con la búsqueda</p>
                </div>
            )}
        </div>
    );
}
