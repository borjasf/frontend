package usuarios.eventos;

public class EventoUsuarioContadorVentas extends Evento{
	
	private String email;
	private int contadorVentas;
	
	public EventoUsuarioContadorVentas() {
		
	} //POJO
	
	public EventoUsuarioContadorVentas(String id, String email, int contadorVentas) {
		super(id, "usuario-contador-ventas");
		this.email = email;
		this.contadorVentas = contadorVentas;
		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getcontadorVentas() {
		return contadorVentas;
	}

	public void setcontadorVentas(int contadorVentas) {
		this.contadorVentas = contadorVentas;
	}
	
	

}
