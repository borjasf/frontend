package auth;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class PruebasAuth {

	public static void main(String[] args) throws InterruptedException {

		System.out.println("========================================");
		System.out.println("  PRUEBAS DE AUTENTICACIÓN Y AUTORIZACIÓN");
		System.out.println("========================================\n");

		pruebaGeneracionYValidacionToken();
		pruebaRolesAutorizacion();
		pruebaTokenExpirado();
		pruebaTokenInvalido();
		pruebaClaveIncorrecta();

		System.out.println("\n========================================");
		System.out.println("  TODAS LAS PRUEBAS FINALIZADAS");
		System.out.println("========================================");
	}

	/**
	 * Prueba 1: Generación y validación correcta de un token.
	 * Comprueba que el token generado con JwtUtils contiene el sujeto esperado.
	 */
	private static void pruebaGeneracionYValidacionToken() {
		System.out.println("--- Prueba 1: Generación y validación de token ---");

		// Preparar claims
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("sub", "usuario1");
		claims.put("roles", "PROFESOR");

		// Generar token usando JwtUtils
		String token = JwtUtils.generateToken(claims);
		System.out.println("Token generado: " + token);

		// Validar token
		Claims claimsRecuperados = JwtUtils.validateToken(token);
		String sujeto = claimsRecuperados.getSubject();
		String roles = claimsRecuperados.get("roles", String.class);

		System.out.println("Sujeto recuperado: " + sujeto);
		System.out.println("Roles recuperados: " + roles);

		if ("usuario1".equals(sujeto) && "PROFESOR".equals(roles)) {
			System.out.println(">> RESULTADO: OK - El token se generó y validó correctamente.\n");
		} else {
			System.out.println(">> RESULTADO: FALLO - Los datos del token no coinciden.\n");
		}
	}

	/**
	 * Prueba 2: Autorización basada en roles.
	 * Comprueba que se pueden asignar y verificar distintos roles en el token.
	 */
	private static void pruebaRolesAutorizacion() {
		System.out.println("--- Prueba 2: Autorización basada en roles ---");

		// Token con rol ADMIN
		Map<String, Object> claimsAdmin = new HashMap<String, Object>();
		claimsAdmin.put("sub", "adminUser");
		claimsAdmin.put("roles", "ADMIN");

		String tokenAdmin = JwtUtils.generateToken(claimsAdmin);
		Claims recuperadosAdmin = JwtUtils.validateToken(tokenAdmin);
		String rolAdmin = recuperadosAdmin.get("roles", String.class);
		System.out.println("Rol del token admin: " + rolAdmin);

		// Token con múltiples roles
		Map<String, Object> claimsMulti = new HashMap<String, Object>();
		claimsMulti.put("sub", "superUser");
		claimsMulti.put("roles", "ADMIN,PROFESOR");

		String tokenMulti = JwtUtils.generateToken(claimsMulti);
		Claims recuperadosMulti = JwtUtils.validateToken(tokenMulti);
		String rolesMulti = recuperadosMulti.get("roles", String.class);
		System.out.println("Roles del token múltiple: " + rolesMulti);

		boolean adminCorrecto = "ADMIN".equals(rolAdmin);
		boolean multiCorrecto = rolesMulti.contains("ADMIN") && rolesMulti.contains("PROFESOR");

		if (adminCorrecto && multiCorrecto) {
			System.out.println(">> RESULTADO: OK - Los roles se asignan y recuperan correctamente.\n");
		} else {
			System.out.println(">> RESULTADO: FALLO - Los roles no se recuperaron correctamente.\n");
		}
	}

	/**
	 * Prueba 3: Token expirado.
	 * Genera un token con 1 segundo de validez, espera a que expire y comprueba
	 * que la validación lanza una excepción.
	 */
	private static void pruebaTokenExpirado() throws InterruptedException {
		System.out.println("--- Prueba 3: Token expirado ---");

		// Generar token manualmente con caducidad de 1 segundo
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("sub", "usuario_temporal");
		claims.put("roles", "PROFESOR");

		Date caducidad = Date.from(Instant.now().plusSeconds(1));

		String token = Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS256, "secreto")
				.setExpiration(caducidad)
				.compact();

		System.out.println("Token generado con caducidad de 1 segundo: " + token);

		// Validar inmediatamente (debería funcionar)
		Claims claimsValidos = JwtUtils.validateToken(token);
		System.out.println("Validación inmediata - Sujeto: " + claimsValidos.getSubject());

		// Esperar a que expire
		System.out.println("Esperando 2 segundos para que el token expire...");
		Thread.sleep(2000);

		// Intentar validar el token expirado
		try {
			JwtUtils.validateToken(token);
			System.out.println(">> RESULTADO: FALLO - El token expirado no lanzó excepción.\n");
		} catch (ExpiredJwtException e) {
			System.out.println("Excepción capturada: " + e.getMessage());
			System.out.println(">> RESULTADO: OK - El token expirado fue rechazado correctamente.\n");
		} catch (Exception e) {
			System.out.println("Excepción inesperada: " + e.getClass().getName() + " - " + e.getMessage());
			System.out.println(">> RESULTADO: FALLO - Se esperaba ExpiredJwtException.\n");
		}
	}

	/**
	 * Prueba 4: Token inválido (manipulado).
	 * Modifica un token válido y comprueba que la validación falla.
	 */
	private static void pruebaTokenInvalido() {
		System.out.println("--- Prueba 4: Token inválido (manipulado) ---");

		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("sub", "usuario1");
		claims.put("roles", "PROFESOR");

		String token = JwtUtils.generateToken(claims);
		// Manipular el token (invertir la firma para garantizar invalidez)
		String[] partes = token.split("\\.");
		String firmaOriginal = partes[2];
		String firmaManipulada = new StringBuilder(firmaOriginal).reverse().toString();
		String tokenManipulado = partes[0] + "." + partes[1] + "." + firmaManipulada;

		System.out.println("Token original:    " + token);
		System.out.println("Token manipulado:  " + tokenManipulado);

		try {
			JwtUtils.validateToken(tokenManipulado);
			System.out.println(">> RESULTADO: FALLO - El token manipulado no lanzó excepción.\n");
		} catch (Exception e) {
			System.out.println("Excepción capturada: " + e.getClass().getSimpleName() + " - " + e.getMessage());
			System.out.println(">> RESULTADO: OK - El token manipulado fue rechazado correctamente.\n");
		}
	}

	/**
	 * Prueba 5: Token firmado con clave incorrecta.
	 * Genera un token con una clave distinta y comprueba que la validación falla.
	 */
	private static void pruebaClaveIncorrecta() {
		System.out.println("--- Prueba 5: Token con clave de firma incorrecta ---");

		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("sub", "usuario_intruso");
		claims.put("roles", "ADMIN");

		Date caducidad = Date.from(Instant.now().plusSeconds(3600));

		// Firmar con una clave distinta a la usada en JwtUtils ("secreto")
		String tokenClaveIncorrecta = Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS256, "otra_clave_secreta")
				.setExpiration(caducidad)
				.compact();

		System.out.println("Token firmado con clave incorrecta: " + tokenClaveIncorrecta);

		try {
			JwtUtils.validateToken(tokenClaveIncorrecta);
			System.out.println(">> RESULTADO: FALLO - El token con clave incorrecta no lanzó excepción.\n");
		} catch (Exception e) {
			System.out.println("Excepción capturada: " + e.getClass().getSimpleName() + " - " + e.getMessage());
			System.out.println(">> RESULTADO: OK - El token con clave incorrecta fue rechazado correctamente.\n");
		}
	}
}