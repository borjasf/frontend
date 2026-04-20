package compraventas.rest;

import java.net.URI;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import compraventas.modelo.Compraventa;
import compraventas.rest.dto.CompraventaDTO;
import compraventas.rest.dto.NuevaCompraventaDTO;
import compraventas.servicio.IServicioCompraventas;

@RestController
@RequestMapping("/compraventas")
public class CompraventasController {

	private IServicioCompraventas servicio;
	
	@Autowired
	private PagedResourcesAssembler<Compraventa> pagedAssembler;
	
	@Autowired
	private CompraventaDTOAssembler dtoAssembler;

	@Autowired
	public CompraventasController(IServicioCompraventas servicio) {
		this.servicio = servicio;
	}

	// Regla 1: Rol USUARIO y que el usuario solicitante sea el comprador
	@PreAuthorize("hasRole('USUARIO') and principal.equals(#dto.idComprador)")
	@PostMapping
	public ResponseEntity<Void> registrarCompraventa(@Valid @RequestBody NuevaCompraventaDTO dto) {
		String id = servicio.registrarCompraventa(dto.getIdProducto(), dto.getIdComprador());

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(id)
				.toUri();

		return ResponseEntity.created(location).build();
	}

	// (Auxiliar HATEOAS: Se deja accesible para usuarios autenticados)
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{id}")
	public EntityModel<CompraventaDTO> getCompraventa(@PathVariable String id) {
		Compraventa cv = servicio.getCompraventa(id);
		return dtoAssembler.toModel(cv);
	}

	// Regla 2: Rol USUARIO y que coincida con el usuario de la consulta
	@PreAuthorize("hasRole('USUARIO') and principal.equals(#idComprador)")
	@GetMapping("/compras/{idComprador}")
	public PagedModel<EntityModel<CompraventaDTO>> getComprasUsuario(
			@PathVariable String idComprador, Pageable pageable) {
		
		Page<Compraventa> compras = servicio.getComprasUsuario(idComprador, pageable);
		return pagedAssembler.toModel(compras, dtoAssembler);
	}

	// Regla 3: Rol USUARIO y que coincida con el vendedor de la consulta
	@PreAuthorize("hasRole('USUARIO') and principal.equals(#idVendedor)")
	@GetMapping("/ventas/{idVendedor}")
	public PagedModel<EntityModel<CompraventaDTO>> getVentasUsuario(
			@PathVariable String idVendedor, Pageable pageable) {
		
		Page<Compraventa> ventas = servicio.getVentasUsuario(idVendedor, pageable);
		return pagedAssembler.toModel(ventas, dtoAssembler);
	}

	// Regla 4: Solo ADMINISTRADOR puede ver compraventas entre dos usuarios específicos
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@GetMapping
	public PagedModel<EntityModel<CompraventaDTO>> getTransacciones(
			@RequestParam String idComprador, 
			@RequestParam String idVendedor, 
			Pageable pageable) {
		
		Page<Compraventa> transacciones = servicio.getCompraventas(idComprador, idVendedor, pageable);
		return pagedAssembler.toModel(transacciones, dtoAssembler);
	}
}