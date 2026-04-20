package compraventas.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NuevaCompraventaDTO {

	@NotNull(message = "El id del producto no puede ser nulo")
	@NotBlank(message = "El id del producto no puede estar vacío")
	private String idProducto;

	@NotNull(message = "El id del comprador no puede ser nulo")
	@NotBlank(message = "El id del comprador no puede estar vacío")
	private String idComprador;

	public NuevaCompraventaDTO() {}

	public String getIdProducto() { return idProducto; }
	public void setIdProducto(String idProducto) { this.idProducto = idProducto; }
	public String getIdComprador() { return idComprador; }
	public void setIdComprador(String idComprador) { this.idComprador = idComprador; }
}