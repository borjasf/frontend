package compraventas.rest;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import compraventas.modelo.Compraventa;
import compraventas.rest.dto.CompraventaDTO;

@Component
public class CompraventaDTOAssembler implements RepresentationModelAssembler<Compraventa, EntityModel<CompraventaDTO>> {

	@Override
	public EntityModel<CompraventaDTO> toModel(Compraventa cv) {
		
		// 1. Mapeamos los datos
		CompraventaDTO dto = new CompraventaDTO();
		dto.setId(cv.getId());
		dto.setIdProducto(cv.getIdProducto());
		dto.setTitulo(cv.getTitulo());
		dto.setPrecio(cv.getPrecio());
		dto.setRecogida(cv.getRecogida());
		dto.setIdVendedor(cv.getIdVendedor());
		dto.setNombreVendedor(cv.getNombreVendedor());
		dto.setIdComprador(cv.getIdComprador());
		dto.setNombreComprador(cv.getNombreComprador());
		dto.setFecha(cv.getFecha());
		
		// 2. Creamos el modelo y le añadimos el enlace "self"
		EntityModel<CompraventaDTO> modelo = EntityModel.of(dto);
		modelo.add(WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(CompraventasController.class).getCompraventa(cv.getId()))
				.withSelfRel());
		
		return modelo;
	}
}