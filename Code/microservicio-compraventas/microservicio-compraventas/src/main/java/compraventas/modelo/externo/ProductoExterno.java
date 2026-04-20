package compraventas.modelo.externo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductoExterno {
	private String titulo;
	private double precio;
	private String vendedor; // Asumimos que el DTO de Productos devuelve el id así
	private Object lugarRecogida; // Como no sabemos si es un String o un objeto, lo pillamos genérico
	private boolean vendido; // Para saber si el producto ya ha sido vendido

	public ProductoExterno() {}

	public String getTitulo() { return titulo; }
	public void setTitulo(String titulo) { this.titulo = titulo; }
	public double getPrecio() { return precio; }
	public void setPrecio(double precio) { this.precio = precio; }
	public String getVendedor() { return vendedor; }
	public void setVendedor(String vendedor) { this.vendedor = vendedor; }
	public Object getLugarRecogida() { return lugarRecogida; }
	public void setLugarRecogida(Object lugarRecogida) { this.lugarRecogida = lugarRecogida; }
	public boolean isVendido() { return vendido; }
	public void setVendido(boolean vendido) { this.vendido = vendido; }
}