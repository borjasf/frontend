package productos.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import productos.dto.ProductoDTO;

@Component
public class ProductoDTOAssembler implements RepresentationModelAssembler<ProductoDTO, EntityModel<ProductoDTO>> {

    @Override
    public EntityModel<ProductoDTO> toModel(ProductoDTO productoDTO) {
        try {
            EntityModel<ProductoDTO> resultado = EntityModel.of(productoDTO,
                    linkTo(methodOn(ProductosController.class).getProductoById(productoDTO.getId())).withSelfRel());
            return resultado;
        } catch (Exception e) {
            return EntityModel.of(productoDTO);
        }
    }
}
