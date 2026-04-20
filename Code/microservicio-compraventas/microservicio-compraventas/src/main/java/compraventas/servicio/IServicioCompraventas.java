package compraventas.servicio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import compraventas.modelo.Compraventa;

public interface IServicioCompraventas {
	
	// Funcionalidad 1: Compraventa de un producto.
	String registrarCompraventa(String idProducto, String idComprador);
	
	// Nuevo método para recuperar una individual (Para HATEOAS)
	Compraventa getCompraventa(String id);
	
	// Funcionalidad 2: Recuperar las compras de un usuario.
	Page<Compraventa> getComprasUsuario(String idComprador, Pageable pageable);
	
	// Funcionalidad 3: Recuperar las ventas de un usuario.
	Page<Compraventa> getVentasUsuario(String idVendedor, Pageable pageable);
	
	// Funcionalidad 4: Recuperar las compraventas entre un comprador y un vendedor.
	Page<Compraventa> getCompraventas(String idComprador, String idVendedor, Pageable pageable);
}