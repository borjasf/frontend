package usuarios.eventos;

public class EventoUsuarioContadorCompras extends Evento{
	
	private String email;
	private int contadorCompras;
	
	public EventoUsuarioContadorCompras() {
		
	} //POJO
	
	public EventoUsuarioContadorCompras(String id, String email, int contadorCompras) {
		super(id, "usuario-contador-compras");
		this.email = email;
		this.contadorCompras = contadorCompras;
		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getContadorCompras() {
		return contadorCompras;
	}

	public void setContadorCompras(int contadorCompras) {
		this.contadorCompras = contadorCompras;
	}
	
	

}
