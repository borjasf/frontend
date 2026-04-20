package productos.servicio;

import java.util.List;

import modelo.Categoria;
import productos.repositorio.EntidadNoEncontrada;
import productos.repositorio.RepositorioException;

public interface IServiciosCategorias {
	
	void modificarCategoria(String idCategoria, String descripcion) throws RepositorioException, EntidadNoEncontrada;
	
	//Funcionalidad: a partir de un fichero XML con la estructura de categorias, cargar las categorias en el repositorio
	void cargarCategorias(String ruta) throws RepositorioException, Exception;

	List<Categoria> obtenerCategoriasRaiz() throws RepositorioException, EntidadNoEncontrada;

	List<Categoria> obtenerDescendientes(String idCategoriaPadre) throws RepositorioException, EntidadNoEncontrada;
	
	//Hecho por Borja, lo necesito para la historia 7
	List<Categoria> recuperarTodosDescendientes(String idCategoriaPadre) throws RepositorioException, EntidadNoEncontrada;

	//Devuelve una lista con todas las categorias del sistema
	List<Categoria> obtenerTodasLasCategorias() throws RepositorioException;
}
