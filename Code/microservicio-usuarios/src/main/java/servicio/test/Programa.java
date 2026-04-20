package servicio.test;

import java.time.LocalDate;
import modelo.Usuario;
import repositorio.EntidadNoEncontrada;
import servicio.FactoriaServicios;
import servicio.IServicioUsuario;

/**
 * Programa principal para probar la funcionalidad del MICROSERVICIO USUARIOS.
 */
public class Programa {
	
	public static void main(String[] args) {
		System.out.println("=== INICIO PRUEBAS MICROSERVICIO USUARIOS ===");
		
		try {
			// 1. Obtenemos el servicio de usuarios
			IServicioUsuario servicioUsuario = FactoriaServicios.getServicio(IServicioUsuario.class);
			
			// --- PRUEBA 1: REGISTRO DE USUARIO ---
			System.out.println("\n[Prueba 1] Registrando usuario nuevo...");
			
			String nombre = "Pepe";
			String apellidos = "Pérez";
			String email = "pruebas@um.es";
			String clave = "password123";
			LocalDate fechaNacimiento = LocalDate.now().minusYears(20);

			String idUsuario = servicioUsuario.registrarUsuario(nombre, apellidos, email, clave, fechaNacimiento, null, false);
			
			System.out.println("--> Usuario creado con ID: " + idUsuario);
			System.out.println("    Datos: " + nombre + " " + apellidos + ", " + email);
			
			
			// --- PRUEBA 2: ACTUALIZACIÓN DE DATOS ---
			System.out.println("\n[Prueba 2] Actualizando email del usuario...");
			
			String nuevoEmail = "pepe_nuevo@gmail.com";
			
			servicioUsuario.actualizarDatosUsuario(idUsuario, nombre, apellidos, nuevoEmail, clave, fechaNacimiento, "666777888");
			
			System.out.println("--> Usuario actualizado correctamente. Nuevo email: " + nuevoEmail);
			
			
			// --- PRUEBA 3: INICIO DE SESIÓN ---
			System.out.println("\n[Prueba 3] Probando inicio de sesión...");
			
			// Intento correcto
			Usuario logueado = servicioUsuario.iniciarSesion(nuevoEmail, clave);
			System.out.println("--> Login EXITOSO: Bienvenido " + logueado.getNombre());
			
			// Intento fallido (clave mal)
			try {
				System.out.println("    Intentando login con clave errónea...");
				servicioUsuario.iniciarSesion(nuevoEmail, "clave_falsa");
			} catch (Exception e) {
				System.out.println("--> Login FALLIDO (Correcto): " + e.getMessage());
			}
			
			
			// --- PRUEBA 4: GESTIÓN DE ADMIN ---
			System.out.println("\n[Prueba 4] Creando administrador...");
			
			String idAdmin = servicioUsuario.registrarUsuario("Admin", "Supremo", "admin@um.es", "admin123", LocalDate.now().minusYears(30), null, false);
			servicioUsuario.asignarRolAdmin(idAdmin);
			
			Usuario adminUser = servicioUsuario.iniciarSesion("admin@um.es", "admin123");
			System.out.println("--> Usuario creado. ¿Es admin? " + adminUser.isAdmin());

            // --- NOTA IMPORTANTE ---
            // Las pruebas de categorías y productos se han eliminado porque
            // este microservicio NO tiene acceso a esa lógica.
            // Esa parte irá en el proyecto 'microservicio-productos'.

		} catch (Exception e) {
			System.err.println("(!) ERROR EN LA EJECUCIÓN: ");
			e.printStackTrace();
		}
		
		System.out.println("\n=== FIN DE PRUEBAS USUARIOS ===");
	}
}