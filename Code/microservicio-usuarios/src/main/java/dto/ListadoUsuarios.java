package dto;

import java.util.List;

public class ListadoUsuarios {

	// Clase estática interna que junta la URL con el Resumen
	public static class UsuarioResumen {
		private String url;
		private UsuarioResumenDTO resumen;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public UsuarioResumenDTO getResumen() {
			return resumen;
		}

		public void setResumen(UsuarioResumenDTO resumen) {
			this.resumen = resumen;
		}
	}

	private List<UsuarioResumen> usuarios;

	public List<UsuarioResumen> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<UsuarioResumen> usuarios) {
		this.usuarios = usuarios;
	}
}