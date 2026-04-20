package usuarios.eventos;

public class EventoCompraventaCreada extends Evento {

	private String emailComprador;
	private String emailVendedor;

	public EventoCompraventaCreada() {
	}

	public String getEmailComprador() {
		return emailComprador;
	}

	public void setEmailComprador(String emailComprador) {
		this.emailComprador = emailComprador;
	}

	public String getEmailVendedor() {
		return emailVendedor;
	}

	public void setEmailVendedor(String emailVendedor) {
		this.emailVendedor = emailVendedor;
	}
}