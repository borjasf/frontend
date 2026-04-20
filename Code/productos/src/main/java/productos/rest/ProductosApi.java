package productos.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import modelo.Categoria;
import modelo.EstadoProducto;
import productos.dto.LugarRecogidaDTO;
import productos.dto.ModificarProductoDTO;
import productos.dto.NuevoProductoDTO;
import productos.dto.ProductoDTO;
import productos.servicio.ProductoResumen;

public interface ProductosApi {

	 @Operation(summary = "Obtener producto", description = "Obtiene un producto por su id")
	 @GetMapping("/{id}")
	 public EntityModel<ProductoDTO> getProductoById(@PathVariable String id) throws Exception;

	 @Operation(summary = "Obtener categorias", description = "Obtiene todas las categorias disponibles")
	 @GetMapping("/categorias")
	 public ResponseEntity<List<Categoria>> getCategorias() throws Exception;

	 @Operation(summary = "Dar de alta producto", description = "Da de alta un nuevo producto")
	 @PostMapping
	 public ResponseEntity<Void> darAltaProducto(@Valid @RequestBody NuevoProductoDTO nuevoProducto) throws Exception;

	 @Operation(summary = "Asignar lugar de recogida", description = "Asigna un lugar de recogida a un producto")
	 @PatchMapping("/{id}/recogida")
	 public ResponseEntity<Void> asignarLugarRecogida(@PathVariable String id, @RequestBody LugarRecogidaDTO lugarRecogida) throws Exception;

	 @Operation(summary = "Modificar producto", description = "Modifica parcialmente el precio y/o la descripcion de un producto")
	 @PatchMapping("/{id}")
	 public ResponseEntity<Void> modificarProducto(@PathVariable String id, @RequestBody ModificarProductoDTO productoModificado) throws Exception;

	 @Operation(summary = "Añadir visualizacion", description = "Incrementa en uno las visualizaciones de un producto")
	 @PatchMapping("/{id}/visualizaciones")
	 public ResponseEntity<Void> aniadirVisualizacion(@PathVariable String id) throws Exception;

	 @Operation(summary = "Historial del mes", description = "Obtiene los productos publicados en un mes y año concretos")
	 @GetMapping("/historial/anyo/{anyo}/mes/{mes}")
	 public PagedModel<EntityModel<ProductoDTO>> historialMes(@PathVariable int anyo, @PathVariable int mes, Pageable paginacion) throws Exception;

	 @Operation(summary = "Buscar productos en venta", description = "Busca productos disponibles para la venta con filtros opcionales")
	 @GetMapping("/buscar")
	 public PagedModel<EntityModel<ProductoDTO>> buscarProductosVenta(
			 @RequestParam(required = false) String categoria,
			 @RequestParam(required = false) String texto,
			 @RequestParam(required = false) EstadoProducto estado,
			 @RequestParam(required = false) Double precioMax,
			 Pageable paginacion) throws Exception;

	 @Operation(summary = "Listado paginado", description = "Obtiene un listado paginado de productos con enlaces HATEOAS")
	 @GetMapping
	 public PagedModel<EntityModel<ProductoResumen>> getProductosPaginado(Pageable paginacion) throws Exception;
}
