/* Componente React: grid de tarjetas de productos.
   Props: productos[], cargando (bool)
   Renderiza una cuadrícula responsiva de tarjetas, cada una con: imagen, título, precio, estado y enlace al detalle.
   Muestra un spinner mientras carga y un mensaje si no hay resultados. */

// Sacamos las herramientas que necesitamos de React (que ya están cargadas en el navegador)
const { useState, useEffect } = React;
const { createRoot } = ReactDOM;

function ListaProductos() {
    // 1. EL ESTADO (La Memoria del Componente)
    // Leemos los datos que Node.js nos dejó en el objeto global "window"
    const datosIniciales = window.PRODUCTOS_INICIALES || { contenido: [] };
    
    // Creamos una variable 'productos' y una función para actualizarla 'setProductos'.
    const [productos, setProductos] = useState(datosIniciales.contenido || []);

    // 2. QUÉ PASA SI NO HAY DATOS
    if (productos.length === 0) {
        return (
            //Se usa className porque en JSX no se puede usar "class" (es una palabra reservada de JavaScript)
            <div className="alert alert-info text-center mt-4" role="alert">
                No se han encontrado productos en el catálogo.
            </div>
        );
    }

    // 3. DIBUJAR LA INTERFAZ (El Render)
    return (
        // row-cols-md-2: En tablets pon 2 tarjetas por fila. lg-3: En PC pon 3 tarjetas. g-4: Espacio entre ellas.
        <div className="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4 mt-2">
            
            {/* Recorremos la lista de productos con un bucle map() */}
            {productos.map(producto => (
                <div className="col" key={producto.id}>
                    <div className="card h-100 shadow-sm">
                        {/* Imagen de prueba genérica */}
                        <img 
                            src="https://via.placeholder.com/300x200?text=Sin+Imagen" 
                            className="card-img-top" 
                            alt={producto.titulo} 
                        />
                        <div className="card-body">
                            <h5 className="card-title">{producto.titulo}</h5>
                            {/* text-truncate corta el texto si es muy largo y le pone ... */}
                            <p className="card-text text-truncate text-muted">{producto.descripcion}</p>
                            
                            <div className="d-flex justify-content-between align-items-center mb-2">
                                <h6 className="text-primary fw-bold mb-0">{producto.precio} €</h6>
                                <span className="badge bg-secondary">{producto.estado}</span>
                            </div>
                        </div>
                        <div className="card-footer bg-white border-top-0">
                            <a href={`/productos/${producto.id}`} className="btn btn-outline-primary w-100">
                                Ver detalles
                            </a>
                        </div>
                    </div>
                </div>
            ))}

        </div>
    );
}

// 4. INYECTAR REACT EN EL HTML
// Buscamos el "hueco" que dejamos en listado.hbs y metemos este componente dentro.
const rootElement = document.getElementById('root-lista-productos');
if (rootElement) {
    const root = createRoot(rootElement);
    root.render(<ListaProductos />);
}
