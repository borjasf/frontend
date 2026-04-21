const { useState, useEffect } = React;
const { createRoot } = ReactDOM;

// Este mensaje nos avisará en la consola (F12) de que el archivo ha sido leído por Babel
console.log("🚀 El archivo ListaProductos.jsx ha despertado correctamente.");

function ListaProductos() {
    // 1. EL ESTADO (La Memoria del Componente)
    const datosIniciales = window.PRODUCTOS_INICIALES || {};
    console.log("📦 Datos inyectados desde Node.js:", datosIniciales);
    
    // Detector inteligente de datos: 
    // Java Spring Boot usa 'content', nosotros usamos 'contenido', o puede venir un Array directo.
    let arrayProductos = [];
    if (Array.isArray(datosIniciales)) {
        arrayProductos = datosIniciales;
    } else if (datosIniciales.content) {
        arrayProductos = datosIniciales.content; // Formato típico de Spring Boot
    } else if (datosIniciales.contenido) {
        arrayProductos = datosIniciales.contenido;
    }

    const [productos, setProductos] = useState(arrayProductos);

    // 2. QUÉ PASA SI NO HAY DATOS
    if (productos.length === 0) {
        return (
            <div className="alert alert-info text-center mt-4 shadow-sm" role="alert">
                <i className="fa-solid fa-box-open fa-2x mb-2"></i>
                <br/>
                No se han encontrado productos en el catálogo o no hay conexión con Java.
            </div>
        );
    }

    // 3. DIBUJAR LA INTERFAZ (El Render)
    return (
        <div className="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4 mt-2">
            {productos.map(producto => (
    <div className="col" key={producto.id || Math.random()}>
        {/* Hacemos la tarjeta clickeable agregando cursor pointer y onClick */}
        <div 
            className="card h-100 shadow-sm border-0 position-relative"
            style={{ cursor: 'pointer' }}
            onClick={() => {
                console.log(`🔗 Navegando a /productos/${producto.id}`);
                window.location.href = `/productos/${producto.id}`;
            }}
        >
            <img 
                src={producto.imagen || "https://via.placeholder.com/300x200?text=Sin+Imagen"} 
                className="card-img-top" 
                alt={producto.titulo} 
                style={{ height: '200px', objectFit: 'cover' }}
            />
            <div className="card-body">
                <h5 className="card-title fw-bold">{producto.titulo || 'Sin título'}</h5>
                <p className="card-text text-truncate text-muted">{producto.descripcion || 'Sin descripción'}</p>
                
                <div className="d-flex justify-content-between align-items-center mt-3">
                    <h5 className="text-primary fw-bold mb-0">{producto.precio || 0} €</h5>
                    <span className="badge bg-secondary">{producto.estado || 'N/A'}</span>
                </div>
            </div>
            <div className="card-footer bg-white border-top-0 pt-0">
                <button 
                    type="button"
                    className="btn btn-outline-primary w-100"
                    onClick={(e) => {
                        e.stopPropagation(); // Evitar doble navegación
                        window.location.href = `/productos/${producto.id}`;
                    }}
                >
                    Ver detalles
                </button>
            </div>
        </div>
    </div>
))}
        </div>
    );
}

// 4. INYECTAR REACT EN EL HTML
const rootElement = document.getElementById('root-lista-productos');
if (rootElement) {
    const root = createRoot(rootElement);
    root.render(<ListaProductos />);
} else {
    console.error("❌ Error: No se encontró el 'root-lista-productos' en el HTML.");
}