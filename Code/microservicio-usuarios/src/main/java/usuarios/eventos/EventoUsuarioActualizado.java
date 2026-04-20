package usuarios.eventos;

public class EventoUsuarioActualizado extends Evento{
	
	private String email;
	private String nombre;
	private String apellidos;
	private String clave;
	private String telefono;
	private String fechaNacimiento;
	
	
	public EventoUsuarioActualizado() {
		
	} //POJO
	
	public EventoUsuarioActualizado(String id, String email, String nombre, String apellidos, String clave, String telefono, String fechaNacimiento) {
		super(id, "usuario-actualizado");
		this.email = email;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.clave = clave;
		this.telefono = telefono;
		this.fechaNacimiento = fechaNacimiento;
		
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	
	
	
}

