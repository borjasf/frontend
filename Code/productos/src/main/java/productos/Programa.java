package productos;

import java.time.LocalDate;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import modelo.EstadoProducto;
import modelo.Usuario;
import productos.dto.ProductoDTO;
import productos.repositoriosModelo.IRepositorioUsuario;
import productos.servicio.IServiciosCategorias;
import productos.servicio.IServiciosProductos;

/**
 * Programa principal para probar la funcionalidad del MICROSERVICIO PRODUCTOS.
 */
public class Programa {
	
	public static void main(String[] args) throws Exception {
		System.out.println("=== INICIO PRUEBAS MICROSERVICIO PRODUCTOS ===");
		
		try {
			// 1. INICIALIZACIÓN DE USUARIOS DUMMY
			ConfigurableApplicationContext contexto = SpringApplication.run(ProductosApplication.class, args);;
			inicializarUsuariosDummy(contexto);
			
			String idUsuarioVendedor = "vendedor1"; 
			String idCategoria = "5709"; // ID de Arte y Ocio
			String idProductoPrueba = null;

			// 2. Obtenemos los servicios
			IServiciosProductos servicioProductos = contexto.getBean(IServiciosProductos.class); 
			IServiciosCategorias servicioCategorias = contexto.getBean(IServiciosCategorias.class);

			// --- PRUEBAS DE CATEGORÍAS ---
			System.out.println("\n[Prueba] Cargando Categorías...");
			servicioCategorias.cargarCategorias("src/main/java/META-INF/categoriasXML/Arte_y_ocio.xml");
			
			// --- PRUEBAS DE PRODUCTOS ---
			System.out.println("\n\n--- INICIO PRUEBAS PRODUCTOS ---");

			// --- Alta Producto ---
			System.out.println("\n[Prueba H3] Alta Producto...");
			idProductoPrueba = servicioProductos.altaProducto(
				"Set Acuarelas Van Gogh", 
				"Caja 12 pastillas + pincel. Poco uso.", 
				18.50,
				EstadoProducto.COMO_NUEVO, 
				idCategoria, 
				true, 
				idUsuarioVendedor 
			);
			System.out.println("--> Producto creado con ID: " + idProductoPrueba);

			if (idProductoPrueba != null) {
				
				// --- Asignar Lugar ---
				System.out.println("\n[Prueba H5] Asignar Lugar Recogida...");
				servicioProductos.asignarLugarRecogida(idProductoPrueba, "Junto a estatua Plaza Héroes de Cavite", -0.985, 37.598);

				// --- Modificar ---
				System.out.println("\n[Prueba H4] Modificar Producto...");
				servicioProductos.modificarProducto(idProductoPrueba, 17.0, "Caja 12 pastillas + pincel. Poco uso. Algún color gastado.");

				// --- Visualización ---
				System.out.println("\n[Prueba] Añadir Visualización...");
				servicioProductos.anadirVisualizacion(idProductoPrueba);

				// --- Historial del Mes ---
				System.out.println("\n[Prueba H6] Historial del Mes...");
				int mesActual = LocalDate.now().getMonthValue();
				int anoActual = LocalDate.now().getYear();
				
				//Primero creamos el objeto de paginacion, por ejemplo, para obtener la primera página con 10 elementos
				Pageable paginacion = PageRequest.of(0, 10); // Página 0 (primera) con 10 elementos por página
				
				Page<ProductoDTO> historial = servicioProductos.historialDelMesPaginado(mesActual, anoActual, paginacion);
				
				System.out.println("--> Historial obtenido. Productos en esta página: " + historial.getNumberOfElements() + " de un total de " + historial.getTotalElements());
				historial.getContent().forEach(p -> System.out.println("    - " + p.getTitulo() + " (Precio: " + p.getPrecio() + ")"));

				// --- Buscar Productos ---
				System.out.println("\n[Prueba H7] Buscar Productos...");
				System.out.println("   Buscando 'pincel'...");

				Page<ProductoDTO> encontrados1 = servicioProductos.buscarProductosPaginado(null, "pincel", null, null, paginacion);
				encontrados1.getContent().forEach(p -> System.out.println("       - " + p.getTitulo() + " (ID: "+p.getId()+")"));

				System.out.println("\n   Buscando en categoría '" + idCategoria + "' <= 20 EUR...");
				Page<ProductoDTO> encontrados2 = servicioProductos.buscarProductosPaginado(idCategoria, null, null, 20.0, paginacion);
				encontrados2.getContent().forEach(p -> System.out.println("       - " + p.getTitulo() + " (" + p.getPrecio() + "€)"));
			}
			
			// --- Categorías Raíz ---
			System.out.println("\n[Prueba] Consultando Categorías Raíz...");
			servicioCategorias.obtenerCategoriasRaiz().forEach(c -> 
				System.out.println(" - " + c.getNombre())
			);

		} catch (Exception e) {
			System.err.println("(!) ERROR FATAL EN PRUEBAS: ");
			e.printStackTrace();
		}
		
		System.out.println("\n=== FIN DE PRUEBAS PRODUCTOS ===");
	}

	private static void inicializarUsuariosDummy(ConfigurableApplicationContext contexto) {
		System.out.println("[Init] Inicializando usuarios réplica...");
		
		IRepositorioUsuario repoUsuario = contexto.getBean(IRepositorioUsuario.class);
		try {
			try {
				repoUsuario.findById("vendedor1");
			} catch (Exception e) {
				Usuario u = new Usuario("vendedor1", "vendedor@test.com", "Pepe", "Vendedor");
				repoUsuario.save(u);
			}
		} catch (Exception e) {
			System.err.println("Error inicializando usuarios: " + e.getMessage());
		}
	}
}