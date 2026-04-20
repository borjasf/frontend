package compraventas.servicio;
import compraventas.modelo.externo.UsuarioExterno;

public interface IUsuariosPort {
	UsuarioExterno obtenerUsuario(String idUsuario);
}