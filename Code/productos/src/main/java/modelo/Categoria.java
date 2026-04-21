package modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class Categoria{

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	
	private String nombre;
	
	@Lob
	private String descripcion;
	@Lob
	private String ruta; 
	

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Categoria> subcategorias = new ArrayList<>();
    
    
	public Categoria() {
	}
	
	public Categoria(String nombre, String descripcion, String ruta) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.ruta = ruta;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}
	
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}


	public List<Categoria> getSubcategorias() {
		return subcategorias;
	}

	public void setSubcategorias(List<Categoria> subcategorias) {
		this.subcategorias = subcategorias;
	}
	
	
}
