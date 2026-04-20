package productos.puertos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import modelo.Usuario;
import productos.repositoriosModelo.IRepositorioUsuario;
import productos.servicio.IServiciosProductos;


@Component
public class ManejadorEventos {
	
	@Autowired
	private IRepositorioUsuario repositorioUsuarios;
	
	// INYECTAMOS TU SERVICIO DE PRODUCTOS
	@Autowired
	private IServiciosProductos servicioProductos;
	
	public void usuarioCreado(String id, String email, String nombre, String apellidos) {
		Usuario usuario = new Usuario(id, email, nombre, apellidos);
		this.repositorioUsuarios.save(usuario);
	}
	
	// NUEVO MÉTODO PARA LA TAREA 7
	public void compraventaCreada(String idProducto) {
		try {
			this.servicioProductos.marcarComoVendido(idProducto);
			System.out.println(" [x] Producto " + idProducto + " marcado como VENDIDO a través del Bus de Eventos.");
		} catch (Exception e) {
			System.err.println("Error procesando evento de compraventa: " + e.getMessage());
		}
	}
}