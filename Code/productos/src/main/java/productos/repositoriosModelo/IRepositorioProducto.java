package productos.repositoriosModelo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import modelo.EstadoProducto;
import modelo.Producto;

/**
 * Interfaz para el repositorio de la entidad Producto.
 * Hereda las operaciones CRUD genéricas.
 */
@NoRepositoryBean
public interface IRepositorioProducto extends PagingAndSortingRepository<Producto, String>{
	// No se necesitan métodos específicos adicionales por ahora.
	// Métodos heredados: add, update, delete, getById, getAll
	Page<Producto> findProductosByMonthAndYear(int mes, int ano, Pageable pageable);
    
    List<Producto> findByVendedorIdOrderByFechaPublicacionDesc(String idVendedor);
    Page<Producto> findProductosByCriteria(List<String> idsCategorias, String textoDescripcion, 
            EstadoProducto estadoMinimo, Double precioMax, Pageable pageable);
}