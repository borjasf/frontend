package productos.dto;

import modelo.EstadoProducto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
public class NuevoProductoDTO {
	//Ahora ponemos aquellas que no puedan ser nulas con @NotNull
	@NotNull
	private String titulo;
	private String descripcion;
	@NotNull
	@DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo.")
	private double precio;
	@NotNull
	private EstadoProducto estado;
	@NotNull
	private String idCategoria;
	private boolean envioDisponible;
	@NotNull
	private String idVendedor;
	
	//POJO 
	public NuevoProductoDTO() {
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public EstadoProducto getEstado() {
		return estado;
	}

	public void setEstado(EstadoProducto estado) {
		this.estado = estado;
	}

	public String getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(String idCategoria) {
		this.idCategoria = idCategoria;
	}

	public boolean isEnvioDisponible() {
		return envioDisponible;
	}

	public void setEnvioDisponible(boolean envioDisponible) {
		this.envioDisponible = envioDisponible;
	}

	public String getIdVendedor() {
		return idVendedor;
	}

	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}
	
	
	
}
