package productos.servicio;

import modelo.Producto;
import modelo.Usuario;
import productos.dto.ProductoDTO;
import productos.repositorio.EntidadNoEncontrada;
import productos.repositorio.RepositorioException;
import productos.repositoriosAdHoc.RepositorioProductoAdHoc;
import productos.repositoriosModelo.IRepositorioCategorias;
import productos.repositoriosModelo.IRepositorioProducto;
import productos.repositoriosModelo.IRepositorioUsuario;
import modelo.Categoria;
import modelo.EstadoProducto;
import modelo.LugarRecogida;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;

@Service 
@Transactional
public class ServicioProductos implements IServiciosProductos {

	private IRepositorioProducto repositorioProducto;
	private IRepositorioUsuario repositorioUsuario; // Asumo que es Spring Data (CrudRepository)
	private IRepositorioCategorias repositorioCategoria;
	private RepositorioProductoAdHoc repositorioProductoAdHoc; 
	private IServiciosCategorias servicioCategorias; 
	
	@Autowired
	public ServicioProductos(IRepositorioProducto repositorioProducto, IRepositorioUsuario repositorioUsuario, IRepositorioCategorias repositorioCategoria, RepositorioProductoAdHoc repositorioProductoAdHoc, IServiciosCategorias servicioCategorias) {
		this.repositorioProducto = repositorioProducto;
		this.repositorioUsuario = repositorioUsuario;
		this.repositorioCategoria = repositorioCategoria;
		this.repositorioProductoAdHoc = repositorioProductoAdHoc;
		this.servicioCategorias = servicioCategorias;
	}
	
	@Override
	public String altaProducto(String titulo, String descripcion, double precio, EstadoProducto estado,
							String idCategoria, boolean envioDisponible, String idVendedor)
							throws EntidadNoEncontrada, IllegalArgumentException {

		if (titulo == null || titulo.trim().isEmpty() || precio < 0 || estado == null || idCategoria == null || idVendedor == null) {
			 throw new IllegalArgumentException("Faltan datos obligatorios o son inválidos para dar de alta el producto.");
		}
		
		// Spring Data JPA usa Optional para las búsquedas por ID
		Usuario vendedor = repositorioUsuario.findById(idVendedor)
				.orElseThrow(() -> new EntidadNoEncontrada("Vendedor no encontrado: " + idVendedor));
		
		Categoria categoria = repositorioCategoria.findById(idCategoria)
				.orElseThrow(() -> new EntidadNoEncontrada("Categoría no encontrada: " + idCategoria));

		Producto nuevoProducto = new Producto();
		nuevoProducto.setTitulo(titulo);
		nuevoProducto.setDescripcion(descripcion);
		nuevoProducto.setPrecio(precio);
		nuevoProducto.setEstado(estado);
		nuevoProducto.setCategoria(categoria);
		nuevoProducto.setEnvioDisponible(envioDisponible);
		nuevoProducto.setVendedor(vendedor);
		nuevoProducto.setFechaPublicacion(LocalDateTime.now());
		nuevoProducto.setVisualizaciones(0);

		// Usamos save() de CrudRepository
		return repositorioProducto.save(nuevoProducto).getId();
	}

	@Override
	public void asignarLugarRecogida(String idProducto, String descripcionLugar, double longitud, double latitud)
						throws EntidadNoEncontrada, IllegalArgumentException {

		// 1. Reutilizamos tu método interno
		Producto producto = getProducto(idProducto); 

		// 2. COMPROBACIÓN DE SEGURIDAD: ¿Es el dueño?
		String idAutenticado = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!idAutenticado.equals(producto.getVendedor().getId())) { 
			throw new AccessDeniedException("Error: No puedes modificar un producto que no te pertenece.");
		}

		if (descripcionLugar == null || descripcionLugar.trim().isEmpty()) {
		    throw new IllegalArgumentException("La descripción del lugar de recogida no puede estar vacía.");
		}
		LugarRecogida nuevoLugar = new LugarRecogida(descripcionLugar, longitud, latitud);
		producto.setLugarRecogida(nuevoLugar);

		repositorioProducto.save(producto);
	}

	@Override
	public void modificarProducto(String id, Double precio, String descripcion) 
            throws EntidadNoEncontrada, IllegalArgumentException {
		// 1. Reutilizamos tu método interno
		Producto producto = getProducto(id);
		
		// 2. COMPROBACIÓN DE SEGURIDAD: ¿Es el dueño?
		String idAutenticado = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!idAutenticado.equals(producto.getVendedor().getId())) { 
			throw new AccessDeniedException("Error: No puedes modificar un producto que no te pertenece.");
		}

		// 3. Modificamos y guardamos
		if (precio != null) {
			producto.setPrecio(precio);
		}
		if (descripcion != null) {
			producto.setDescripcion(descripcion);
		}
		repositorioProducto.save(producto);
	}

	@Override
	public void anadirVisualizacion(String idProducto)
						throws EntidadNoEncontrada {

		Producto producto = getProducto(idProducto);
		int visualizacionesActuales = producto.getVisualizaciones();
		producto.setVisualizaciones(visualizacionesActuales + 1);
		repositorioProducto.save(producto);
	}

	@Override
	public ProductoDTO getProductoDTO(String idProducto) throws EntidadNoEncontrada {
		if (idProducto == null || idProducto.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacio");
		
		Optional<Producto> productoOpt = repositorioProducto.findById(idProducto);
		if(productoOpt.isPresent() == false) {
			throw new EntidadNoEncontrada("No se encontró el producto con id: " + idProducto);
		} else {
			return ProductoDTO.fromEntity(productoOpt.get());
		}
	}
	
	public Producto getProducto(String id) throws EntidadNoEncontrada{
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacio");
		
		Optional<Producto> productoOpt = repositorioProducto.findById(id);
		if(productoOpt.isPresent() == false) {
			throw new EntidadNoEncontrada("No se encontró el producto con id: " + id);
		} else {
			return productoOpt.get();
		}
	}
	    
	@Override
	public List<ProductoDTO> getProductosPorVendedor(String idVendedor) 
			throws RepositorioException, IllegalArgumentException {
		
		if (idVendedor == null || idVendedor.trim().isEmpty()) {
			throw new IllegalArgumentException("El ID del vendedor no puede ser nulo o vacío.");
		}
		
		List<Producto> productosDelVendedor = repositorioProductoAdHoc.findByVendedorIdOrderByFechaPublicacionDesc(idVendedor);
		
		List<ProductoDTO> productosDTO = new ArrayList<>();
		for (Producto p : productosDelVendedor) {
			productosDTO.add(ProductoDTO.fromEntity(p)); 
		}
		
		return productosDTO;
	}

	@Override
	public Page<ProductoResumen> getListadoPaginado(Pageable pageable) {
		return this.repositorioProducto.findAll(pageable).map(producto -> {
			ProductoResumen resumen = new ProductoResumen();
			resumen.setId(producto.getId());
			resumen.setTitulo(producto.getTitulo());
			resumen.setPrecio(producto.getPrecio());
			resumen.setEstado(producto.getEstado());
			resumen.setFechaPublicacion(producto.getFechaPublicacion());
			resumen.setEnvioDisponible(producto.isEnvioDisponible());
			if (producto.getCategoria() != null)
				resumen.setCategoriaNombre(producto.getCategoria().getDescripcion());
			return resumen;
		});
	}
	
	@Override
	public Page<ProductoDTO> historialDelMesPaginado(int mes, int anyo, Pageable paginacion) throws RepositorioException {
	    if (mes < 1 || mes > 12 || anyo <= 0) {
	        throw new IllegalArgumentException("Mes o año inválido.");
	    }
	    
	    Page<Producto> paginaProductos = repositorioProductoAdHoc.findProductosByMonthAndYear(mes, anyo, paginacion);
	    return paginaProductos.map(producto -> ProductoDTO.fromEntity(producto));
	}

	@Override
	public Page<ProductoDTO> buscarProductosPaginado(String idCategoria, String textoDescripcion, EstadoProducto estadoMinimo, Double precioMax, Pageable paginacion) throws RepositorioException, EntidadNoEncontrada { 

	    List<String> idsCategoriasParaBuscar = null;

	    if (idCategoria != null && !idCategoria.trim().isEmpty()) {
	        idsCategoriasParaBuscar = new ArrayList<>();

	        Categoria catRaiz = repositorioCategoria.findById(idCategoria)
	                .orElseThrow(() -> new EntidadNoEncontrada("Categoría no encontrada: " + idCategoria));
	        
	        idsCategoriasParaBuscar.add(catRaiz.getId());

	        List<Categoria> descendientes = servicioCategorias.recuperarTodosDescendientes(idCategoria); 

	        if (descendientes != null) {
	            for (Categoria cat : descendientes) {
	                if (!idsCategoriasParaBuscar.contains(cat.getId())) {
	                    idsCategoriasParaBuscar.add(cat.getId());
	                }
	            }
	        }
	    }

	    Page<Producto> paginaProductos = repositorioProductoAdHoc.findProductosByCriteria(
	            idsCategoriasParaBuscar, textoDescripcion, estadoMinimo, precioMax, paginacion);
	    
	    return paginaProductos.map(producto -> ProductoDTO.fromEntity(producto));
	}
	
	@Override
	public void marcarComoVendido(String idProducto) throws EntidadNoEncontrada {
		Producto producto = getProducto(idProducto);
		producto.setVendido(true);
		repositorioProducto.save(producto);
	}
}