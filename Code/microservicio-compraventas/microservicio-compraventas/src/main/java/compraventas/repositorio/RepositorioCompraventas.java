package compraventas.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import compraventas.modelo.Compraventa;

public interface RepositorioCompraventas extends MongoRepository<Compraventa, String> {

	//CAMBIAMOS LIST POR PAGE 
	// 1. Recuperar las compras de un usuario
	Page<Compraventa> findByIdComprador(String idComprador, Pageable pageable);
	
	// 2. Recuperar las ventas de un usuario
	Page<Compraventa> findByIdVendedor(String idVendedor, Pageable pageable);
	
	// 3. Recuperar las compraventas entre un comprador y un vendedor
	Page<Compraventa> findByIdCompradorAndIdVendedor(String idComprador, String idVendedor, Pageable pageable);
}