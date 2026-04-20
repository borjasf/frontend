package modelo;

import java.time.LocalDateTime;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "productos")
public class Producto {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String id;
	
	private String titulo; 
	
	@Lob
	private String descripcion;
	private double precio;
	
	@Enumerated(EnumType.STRING)
	private EstadoProducto estado;
	
	private LocalDateTime fechaPublicacion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;
	
	private int visualizaciones;
	private boolean envioDisponible;
	
	//Nueva variable para poder infromar de que ha sido vendido
	private boolean vendido;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "descripcion", column = @Column(name = "lugar_descripcion")),
		@AttributeOverride(name = "longitud", column = @Column(name = "lugar_longitud")),
		@AttributeOverride(name = "latitud", column = @Column(name = "lugar_latitud"))
	})
	private LugarRecogida lugarRecogida;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendedor_id")
	private Usuario vendedor; 
	
	public Producto() {
	}
	
	public Producto(String titulo, String descripcion, double precio, EstadoProducto estado, LocalDateTime fechaPublicacion,
			Categoria categoria, int visualizaciones, boolean envioDisponible, LugarRecogida lugarRecogida,
			Usuario vendedor) {
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.precio = precio;
		this.estado = estado;
		this.fechaPublicacion = fechaPublicacion;
		this.categoria = categoria;
		this.visualizaciones = visualizaciones;
		this.envioDisponible = envioDisponible;
		this.lugarRecogida = lugarRecogida;
		this.vendedor = vendedor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public LocalDateTime getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public int getVisualizaciones() {
		return visualizaciones;
	}

	public void setVisualizaciones(int visualizaciones) {
		this.visualizaciones = visualizaciones;
	}

	public boolean isEnvioDisponible() {
		return envioDisponible;
	}

	public void setEnvioDisponible(boolean envioDisponible) {
		this.envioDisponible = envioDisponible;
	}

	public LugarRecogida getLugarRecogida() {
		return lugarRecogida;
	}

	public void setLugarRecogida(LugarRecogida lugarRecogida) {
		this.lugarRecogida = lugarRecogida;
	}

	public Usuario getVendedor() {
		return vendedor;
	}

	public void setVendedor(Usuario vendedor) {
		this.vendedor = vendedor;
	}

	public boolean isVendido() {
		return vendido;
	}

	public void setVendido(boolean vendido) {
		this.vendido = vendido;
	}
	
}
