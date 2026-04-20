package usuarios.eventos;

public class EventoUsuarioRol extends Evento{
	
	private String email;
	private String nombre;
	private boolean admin;
	
	
	public EventoUsuarioRol() {
		
	} //POJO
	
	public EventoUsuarioRol(String id, String email, String nombre, boolean admin) {
		super(id, "usuario-rol-admin");
		this.email = email;
		this.nombre = nombre;
		this.admin = admin;
		
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

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	
	
	
}

