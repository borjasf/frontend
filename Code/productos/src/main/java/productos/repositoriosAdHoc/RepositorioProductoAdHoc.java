package productos.repositoriosAdHoc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import modelo.EstadoProducto;
import modelo.Producto;
import productos.repositoriosModelo.IRepositorioProducto;

/**
 * Interfaz para operaciones de consulta AdHoc sobre Productos,
 * específicamente para búsquedas con criterios complejos.
 */

@Repository
public interface RepositorioProductoAdHoc extends IRepositorioProducto, JpaRepository<Producto, String> {

	// Spring implementa esto automáticamente por convención de nombres se implementa solo por convencion
	
    //List<Producto> findByVendedorIdOrderByFechaPublicacionDesc(String idVendedor);

    // Consulta personalizada para obtener productos publicados en un mes y año específicos, ordenados por visualizaciones
	@Override
    @Query("SELECT p FROM Producto p " +
           "WHERE FUNCTION('YEAR', p.fechaPublicacion) = :ano " +
           "AND FUNCTION('MONTH', p.fechaPublicacion) = :mes " +
           "ORDER BY p.visualizaciones DESC")
    Page<Producto> findProductosByMonthAndYear(@Param("mes") int mes, @Param("ano") int ano, Pageable pageable);
	
	
	@Override
    @Query("SELECT p FROM Producto p " +
           "WHERE (:idsCategorias IS NULL OR p.categoria.id IN :idsCategorias) " +
           "AND (:textoDescripcion IS NULL OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :textoDescripcion, '%'))) " +
           "AND (:estadoMinimo IS NULL OR p.estado = :estadoMinimo) " + 
           "AND (:precioMax IS NULL OR p.precio <= :precioMax)")
    Page<Producto> findProductosByCriteria(
            @Param("idsCategorias") List<String> idsCategorias, 
            @Param("textoDescripcion") String textoDescripcion, 
            @Param("estadoMinimo") EstadoProducto estadoMinimo, 
            @Param("precioMax") Double precioMax, 
            Pageable pageable);
}