	package productos.servicio;
	
	import java.util.List;
	
	import org.springframework.data.domain.Page;
	import org.springframework.data.domain.Pageable;
	
	import modelo.EstadoProducto;
	import productos.dto.ProductoDTO;
	import productos.repositorio.EntidadNoEncontrada;
	import productos.repositorio.RepositorioException;
	
	/**
	 * Interfaz que define los servicios relacionados con la gestión de Productos.
	 */
	public interface IServiciosProductos {
	
		 //Da de alta un nuevo producto en el sistema. (Historia 3)
		
		String altaProducto(String titulo, String descripcion, double precio, EstadoProducto estado,
		                    String idCategoria, boolean envioDisponible, String idVendedor)
		                    throws EntidadNoEncontrada, IllegalArgumentException;
	
		// Asigna o actualiza el lugar de recogida para un producto existente. (Historia 5)
		void asignarLugarRecogida(String idProducto, String descripcionLugar, double longitud, double latitud)
		                    throws EntidadNoEncontrada;
	
		// Modifica el precio y/o la descripción de un producto existente. (Historia 4)
		void modificarProducto(String idProducto, Double nuevoPrecio, String nuevaDescripcion)
		                    throws EntidadNoEncontrada, IllegalArgumentException;
	
		// Incrementa en uno el contador de visualizaciones de un producto.
		
		void anadirVisualizacion(String idProducto)
		                    throws EntidadNoEncontrada;
	
		ProductoDTO getProductoDTO(String idProducto) throws EntidadNoEncontrada; 
		
		/*
		 Recupera un listado de productos (como DTOs) puestos a la venta por un vendedor específico.
		 * Requerido por la Práctica 2.
		 */
		List<ProductoDTO> getProductosPorVendedor(String idVendedor) 
				throws RepositorioException, IllegalArgumentException;
	
		Page<ProductoResumen> getListadoPaginado(Pageable paginacion);
		
		Page<ProductoDTO> historialDelMesPaginado(int mes, int anyo, Pageable paginacion) throws RepositorioException;
	
		Page<ProductoDTO> buscarProductosPaginado(String idCategoria, String textoDescripcion, EstadoProducto estadoMinimo, Double precioMax, Pageable paginacion) throws RepositorioException, EntidadNoEncontrada;
		
		void marcarComoVendido(String idProducto) throws EntidadNoEncontrada;
	
	}