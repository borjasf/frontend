package productos.rest;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import modelo.Categoria;
import modelo.EstadoProducto;
import productos.dto.LugarRecogidaDTO;
import productos.dto.ModificarProductoDTO;
import productos.dto.NuevoProductoDTO;
import productos.dto.ProductoDTO;
import productos.servicio.IServiciosCategorias;
import productos.servicio.IServiciosProductos;
import productos.servicio.ProductoResumen;

@RestController
@RequestMapping("/productos")
public class ProductosController implements ProductosApi {

	private IServiciosProductos servicio;
	private IServiciosCategorias servicioCategorias;
	
	@Autowired
	private PagedResourcesAssembler<ProductoResumen> pagedResourcesAssembler;
	
	@Autowired
	private PagedResourcesAssembler<ProductoDTO> pagedResourcesAssemblerDTO;
	
	@Autowired
	private ProductosResumenAssembler productosResumenAssembler;
	
	@Autowired
	private ProductoDTOAssembler productoDTOAssembler;
	
	@Autowired
	public ProductosController(IServiciosProductos servicio, IServiciosCategorias servicioCategorias) {
		this.servicio = servicio;
		this.servicioCategorias = servicioCategorias;
	}
	
	// REGLA: Usuario registrado (rol USUARIO). El usuario que la solicita debe ser el propietario.
	@PreAuthorize("hasRole('USUARIO') and principal.equals(#nuevoProducto.idVendedor)")
	@PostMapping
	public ResponseEntity<Void> darAltaProducto(
			 @Valid @RequestBody NuevoProductoDTO nuevoProducto) throws Exception {
		
		String id = this.servicio.altaProducto(nuevoProducto.getTitulo(),
				nuevoProducto.getDescripcion(),
				nuevoProducto.getPrecio(),
				nuevoProducto.getEstado(),
				nuevoProducto.getIdCategoria(), 
				nuevoProducto.isEnvioDisponible(), 
				nuevoProducto.getIdVendedor());
	
		 // Construye la URL completa del nuevo recurso
		 URI nuevaURL = ServletUriComponentsBuilder.fromCurrentRequest()
				 			.path("/{id}").buildAndExpand(id).toUri();
		
		 return ResponseEntity.created(nuevaURL).build();
	}

	// REGLA: Pública.
	@PreAuthorize("permitAll()")
	@GetMapping("/{id}")
	public EntityModel<ProductoDTO> getProductoById(@PathVariable String id) throws Exception {
		ProductoDTO producto = this.servicio.getProductoDTO(id);
		return productoDTOAssembler.toModel(producto); 
	}

	// REGLA: Pública.
	@PreAuthorize("permitAll()")
	@GetMapping("/categorias")
	public ResponseEntity<List<Categoria>> getCategorias() throws Exception {
		return ResponseEntity.ok(this.servicioCategorias.obtenerTodasLasCategorias());
	}

	// REGLA: Usuario registrado (rol USUARIO) y propietario del producto.
	@PreAuthorize("hasRole('USUARIO')")
	@PatchMapping("/{id}/recogida")
	public ResponseEntity<Void> asignarLugarRecogida(@PathVariable String id,
			@Valid @RequestBody LugarRecogidaDTO lugarRecogida) throws Exception {
		
		// NOTA: La validación de que el 'principal' es el dueño real del producto con 'id'
		// se debe hacer dentro de this.servicio.asignarLugarRecogida(...)
		this.servicio.asignarLugarRecogida(id, lugarRecogida.getDescripcion(),
				lugarRecogida.getLongitud(), lugarRecogida.getLatitud());
		return ResponseEntity.noContent().build();
	}

	// REGLA: Usuario registrado (rol USUARIO) y propietario del producto.
	@PreAuthorize("hasRole('USUARIO')")
	@PatchMapping("/{id}")
	public ResponseEntity<Void> modificarProducto(@PathVariable String id,
			@Valid @RequestBody ModificarProductoDTO productoModificado) throws Exception {
		
		// NOTA: Igual que arriba, validar pertenencia en el servicio
		this.servicio.modificarProducto(id, productoModificado.getPrecio(), productoModificado.getDescripcion());
		return ResponseEntity.noContent().build();
	}

	// REGLA: Pública.
	@PreAuthorize("permitAll()")
	@PatchMapping("/{id}/visualizaciones")
	public ResponseEntity<Void> aniadirVisualizacion(@PathVariable String id) throws Exception {
		this.servicio.anadirVisualizacion(id);
		return ResponseEntity.noContent().build();
	}

	// REGLA: Pública.
	@PreAuthorize("permitAll()")
	@GetMapping("/historial/anyo/{anyo}/mes/{mes}")
	public PagedModel<EntityModel<ProductoDTO>> historialMes(@PathVariable int anyo, @PathVariable int mes, Pageable paginacion) throws Exception {
		Page<ProductoDTO> resultado = this.servicio.historialDelMesPaginado(mes, anyo, paginacion);
		return this.pagedResourcesAssemblerDTO.toModel(resultado, productoDTOAssembler);
	}

	// REGLA: Pública.
	@PreAuthorize("permitAll()")
	@GetMapping("/buscar")
	public PagedModel<EntityModel<ProductoDTO>> buscarProductosVenta(
			@RequestParam(required = false) String categoria,
			@RequestParam(required = false) String texto,
			@RequestParam(required = false) EstadoProducto estado,
			@RequestParam(required = false) Double precioMax,
			Pageable paginacion) throws Exception {
		Page<ProductoDTO> resultado = this.servicio.buscarProductosPaginado(categoria, texto, estado, precioMax, paginacion);
		return this.pagedResourcesAssemblerDTO.toModel(resultado, productoDTOAssembler);
	}

	// REGLA: Pública.
	@PreAuthorize("permitAll()")
	@GetMapping
	public PagedModel<EntityModel<ProductoResumen>> getProductosPaginado(Pageable paginacion) throws Exception {
		Page<ProductoResumen> resultado = this.servicio.getListadoPaginado(paginacion);
		return this.pagedResourcesAssembler.toModel(resultado, productosResumenAssembler);
	}
}