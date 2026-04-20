package segundum.pasarela.dto;

public class CredencialesDTO {
	private String email;
	private String clave;

	public CredencialesDTO() {}
	
	public CredencialesDTO(String email, String clave) {
		this.email = email;
		this.clave = clave;
	}

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getClave() { return clave; }
	public void setClave(String clave) { this.clave = clave; }
}