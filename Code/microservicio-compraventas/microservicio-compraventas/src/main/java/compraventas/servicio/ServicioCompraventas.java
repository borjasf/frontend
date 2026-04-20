package compraventas.servicio;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import compraventas.modelo.Compraventa;
import compraventas.modelo.externo.ProductoExterno;
import compraventas.modelo.externo.UsuarioExterno;
import compraventas.repositorio.RepositorioCompraventas;
import compraventas.puertos.PublicadorEventos;
import compraventas.eventos.EventoCompraventaCreada;

@Service 
public class ServicioCompraventas implements IServicioCompraventas {

	private RepositorioCompraventas repositorio;
	private IProductosPort productosPort;
	private IUsuariosPort usuariosPort;
	private PublicadorEventos publicadorEventos;

	@Autowired
	public ServicioCompraventas(RepositorioCompraventas repositorio, 
								IProductosPort productosPort,
								IUsuariosPort usuariosPort,
								PublicadorEventos publicadorEventos) {
		this.repositorio = repositorio;
		this.productosPort = productosPort;
		this.usuariosPort = usuariosPort;
		this.publicadorEventos = publicadorEventos;
	}

	@Override
	public String registrarCompraventa(String idProducto, String idComprador) {
		
		ProductoExterno producto = productosPort.obtenerProducto(idProducto);
		
		if (producto.isVendido()) {
		    throw new IllegalArgumentException("El producto ya ha sido vendido y no puede comprarse.");
		}

		UsuarioExterno comprador = usuariosPort.obtenerUsuario(idComprador);

		UsuarioExterno vendedor = usuariosPort.obtenerUsuario(producto.getVendedor());


		Compraventa cv = new Compraventa();
		
		cv.setIdProducto(idProducto);
		cv.setTitulo(producto.getTitulo());
		cv.setPrecio(producto.getPrecio());
		
		// El enunciado dice: "la información con la recogida se almacena de forma simplificada como cadena de texto"
		cv.setRecogida(producto.getLugarRecogida() != null ? producto.getLugarRecogida().toString() : "No especificado");

		cv.setIdComprador(idComprador);
		cv.setNombreComprador(comprador.getNombre() + " " + comprador.getApellidos());

		cv.setIdVendedor(producto.getVendedor());
		cv.setNombreVendedor(vendedor.getNombre() + " " + vendedor.getApellidos());

		cv.setFecha(LocalDateTime.now());

		Compraventa guardada = repositorio.save(cv);
		
		// Construimos el evento y lo publicamos en el bus
        EventoCompraventaCreada evento = new EventoCompraventaCreada(
                guardada.getId(), 
                guardada.getIdProducto(), 
                guardada.getIdComprador(), 
                guardada.getIdVendedor()
        );
        publicadorEventos.emitirEventoCompraventaCreada(evento);

		return guardada.getId();
	}

	@Override
	public Compraventa getCompraventa(String id) {
		return repositorio.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Compraventa no encontrada"));
	}

	@Override
	public Page<Compraventa> getComprasUsuario(String idComprador, Pageable pageable) {
		return repositorio.findByIdComprador(idComprador, pageable);
	}

	@Override
	public Page<Compraventa> getVentasUsuario(String idVendedor, Pageable pageable) {
		return repositorio.findByIdVendedor(idVendedor, pageable);
	}

	@Override
	public Page<Compraventa> getCompraventas(String idComprador, String idVendedor, Pageable pageable) {
		return repositorio.findByIdCompradorAndIdVendedor(idComprador, idVendedor, pageable);
	}
}