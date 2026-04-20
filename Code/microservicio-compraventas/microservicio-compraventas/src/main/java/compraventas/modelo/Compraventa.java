package compraventas.modelo;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "compraventas") // Así le decimos a Spring que lo guarde en la colección "compraventas" de Mongo
public class Compraventa {

	@Id 
	private String id;
	
	private String idProducto;
	private String titulo;
	private double precio;
	private String recogida;
	
	private String idVendedor;
	private String nombreVendedor;
	
	private String idComprador;
	private String nombreComprador;
	
	private LocalDateTime fecha;

	public Compraventa() {
		// Constructor vacío necesario para Spring y JSON
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	
	public String getIdProducto() { return idProducto; }
	public void setIdProducto(String idProducto) { this.idProducto = idProducto; }
	
	public String getTitulo() { return titulo; }
	public void setTitulo(String titulo) { this.titulo = titulo; }
	
	public double getPrecio() { return precio; }
	public void setPrecio(double precio) { this.precio = precio; }
	
	public String getRecogida() { return recogida; }
	public void setRecogida(String recogida) { this.recogida = recogida; }
	
	public String getIdVendedor() { return idVendedor; }
	public void setIdVendedor(String idVendedor) { this.idVendedor = idVendedor; }
	
	public String getNombreVendedor() { return nombreVendedor; }
	public void setNombreVendedor(String nombreVendedor) { this.nombreVendedor = nombreVendedor; }
	
	public String getIdComprador() { return idComprador; }
	public void setIdComprador(String idComprador) { this.idComprador = idComprador; }
	
	public String getNombreComprador() { return nombreComprador; }
	public void setNombreComprador(String nombreComprador) { this.nombreComprador = nombreComprador; }
	
	public LocalDateTime getFecha() { return fecha; }
	public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}