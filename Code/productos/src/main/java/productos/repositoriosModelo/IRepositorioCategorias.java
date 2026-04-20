package productos.repositoriosModelo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import modelo.Categoria;
import productos.repositorio.EntidadNoEncontrada;
import productos.repositorio.RepositorioException;

@NoRepositoryBean
public interface IRepositorioCategorias extends CrudRepository<Categoria, String> {
	//Definimos los métodos específicos para Categoria si es necesario
	List<Categoria> buscarCategoriasRaiz() throws RepositorioException, EntidadNoEncontrada;
    List<Categoria> buscarDescendientes(String idPadre) throws RepositorioException, EntidadNoEncontrada;

}
