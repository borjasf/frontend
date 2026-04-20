package repositoriosModelo;

import modelo.Usuario;
import repositorio.RepositorioJPA;

public class RepositorioUsuarioJPA  extends RepositorioJPA<Usuario> implements IRepositorioUsuario{

	@Override
	public Class<Usuario> getClase() {
		return Usuario.class;
	}
	
	

}
