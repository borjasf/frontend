package productos.repositoriosModelo; 

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import modelo.Usuario;

@NoRepositoryBean
public interface IRepositorioUsuario extends CrudRepository<Usuario, String> {


}