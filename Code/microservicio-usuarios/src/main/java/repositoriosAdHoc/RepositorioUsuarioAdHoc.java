package repositoriosAdHoc;

import modelo.Usuario;
import repositorio.RepositorioException;
import repositorio.RepositorioString;

public interface RepositorioUsuarioAdHoc extends RepositorioString<Usuario>{
	
	//Aqui vamos a implementar metodos ad-hoc especificos para Usuario
	//Por ejemplo, buscar usuarios por rol, estado, etc.
	
	Usuario buscarPorEmail(String email) throws RepositorioException;
	Usuario getByGitHubId(String githubId) throws RepositorioException;

}
