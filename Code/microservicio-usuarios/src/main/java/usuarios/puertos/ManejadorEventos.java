package usuarios.puertos;

import java.io.IOException;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;
import servicio.IServicioUsuario;

//Puerto de entrada

public class ManejadorEventos {

	private IServicioUsuario servicio = FactoriaServicios.getServicio(IServicioUsuario.class);

	// Método que será invocado cuando llegue el evento "compraventa-creada"
	public void procesarEventoCompraventaCreada(String emailComprador, String emailVendedor)
			throws RepositorioException, EntidadNoEncontrada, IOException {
		// Incrementamos los contadores utilizando la lógica que creamos anteriormente
		servicio.incrementarContadorCompras(emailComprador);
		servicio.incrementarContadorVentas(emailVendedor);
	}

	public void compraventaCreada(String idComprador, String idVendedor) {
		try {
			// 1. Incrementar compras del comprador
			if (idComprador != null && !idComprador.isEmpty()) {
				servicio.incrementarContadorCompras(idComprador);
			}

			// 2. Incrementar ventas del vendedor
			if (idVendedor != null && !idVendedor.isEmpty()) {
				servicio.incrementarContadorVentas(idVendedor);
			}

			System.out.println(" [Usuarios] Contadores actualizados para Comprador: " + idComprador + " y Vendedor: "
					+ idVendedor);

		} catch (Exception e) {
			System.err.println(" [Usuarios] Error al actualizar contadores: " + e.getMessage());
		}
	}

}