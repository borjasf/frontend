package usuarios.eventos;

public class EventoUsuarioCreado extends Evento{
	
	private String email;
	private String nombre;
	private String apellidos;
	
	public EventoUsuarioCreado() {
		
	} //POJO
	
	public EventoUsuarioCreado(String id, String email, String nombre, String apellidos) {
		super(id, "usuario-creado");
		this.email = email;
		this.nombre = nombre;
		this.apellidos = apellidos;
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

}
