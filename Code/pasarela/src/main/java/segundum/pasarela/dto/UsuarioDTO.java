package segundum.pasarela.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioDTO {
	private String id;
	private String nombre;
	private String apellidos;
	private String email;
	private String fechaNacimiento;
	private String telefono;
	private boolean admin;

	public UsuarioDTO() {}

	public UsuarioDTO(String id, String nombre, String apellidos, String email,  String fechaNacimiento, String telefono, boolean admin) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.fechaNacimiento = fechaNacimiento;
		this.telefono = telefono;
		this.admin = admin;
	}

	// Genera los Getters y Setters para todos estos campos (id, nombre, apellidos, email, fechaNacimiento, telefono, admin)
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
	public String getApellidos() { return apellidos; }
	public void setApellidos(String apellidos) { this.apellidos = apellidos; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getFechaNacimiento() { return fechaNacimiento; }
	public String getTelefono() { return telefono; }
	public void setTelefono(String telefono) { this.telefono = telefono; }
	public boolean isAdmin() { return admin; }
	public void setAdmin(boolean admin) { this.admin = admin; }
}