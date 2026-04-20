package modelo;

import java.time.LocalDate;
import repositorio.Identificable;
import javax.persistence.*;

@Entity
public class Usuario implements Identificable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;
	
	private String email;
	private String nombre;
	private String apellidos; 
	private String gitHubId; // Nuevo campo para almacenar el ID de GitHub

	@Lob
	private String clave;
	
	private LocalDate fechaNacimiento;
	private String telefono;
	private boolean esAdmin;
	
	//Nuevos campos para los contadores de compras y ventas
	private int contadorCompras;
	private int contadorVentas;
	
	public Usuario() {	
	}
	
	public Usuario(String email, String nombre, String apellidos, String clave, LocalDate fechaNacimiento, String telefono, boolean esAdmin) {
		this.email = email;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.clave = clave;
		this.fechaNacimiento = fechaNacimiento;
		this.telefono = telefono;
		this.esAdmin = esAdmin;
		this.contadorCompras = 0; // Inicializamos el contador de compras
		this.contadorVentas = 0; // Inicializamos el contador de ventas
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public boolean isAdmin() {
		return esAdmin;
	}

	public void setAdmin(boolean esAdmin) {
		this.esAdmin = esAdmin;
	}

	public boolean isEsAdmin() {
		return esAdmin;
	}

	public void setEsAdmin(boolean esAdmin) {
		this.esAdmin = esAdmin;
	}

	public int getContadorCompras() {
		return contadorCompras;
	}

	public void setContadorCompras(int contadorCompras) {
		this.contadorCompras = contadorCompras;
	}

	public int getContadorVentas() {
		return contadorVentas;
	}

	public void setContadorVentas(int contadorVentas) {
		this.contadorVentas = contadorVentas;
	}

	public void incrementarContadorCompras() {
		this.contadorCompras++;
		
	}

	public void incrementarContadorVentas() {
		this.contadorVentas++;
		
	}
	
	public String getGitHubId() {
		return gitHubId;
	}

	public void setGitHubId(String gitHubId) {
		this.gitHubId = gitHubId;
	}
	
	
	
	
}
