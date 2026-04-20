package segundum.pasarela.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import segundum.pasarela.dto.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class ControladorAuth {
	
    private RetrofitUsuarios retrofitUsuarios;
    private final long JWT_TIEMPO_VALIDEZ_LOGIN = 3600;

    public ControladorAuth() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://usuarios:8080/api/") // Apunta al microservicio Usuarios
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.retrofitUsuarios = retrofit.create(RetrofitUsuarios.class);
    }
    /*
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credenciales, HttpServletResponse response) {
        try {
            // 1. Verificamos las credenciales llamando al microservicio Usuarios
        	//Creamos un DTO de credenciales para enviar al microservicio
        	CredencialesDTO credencialesDTO = new CredencialesDTO(credenciales.get("username"), credenciales.get("password"));
            UsuarioDTO usuarioValidado = retrofitUsuarios.verificarCredenciales(credencialesDTO).execute().body();

            if (usuarioValidado == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // 2. Extraemos los datos del usuario
            String id = (String) usuarioValidado.getId();
            String nombreCompleto = usuarioValidado.getNombre() + " " + usuarioValidado.getApellidos();
            //Si es administrador, el rol es "admin", si es usuario normal, el rol es "user"
            String rol = String.valueOf(usuarioValidado.isAdmin() ? "ADMIN" : "USER");

            // 3. Generamos el token JWT
            String jwt = Jwts.builder()
                    .setSubject(id)
                    .claim("nombre", nombreCompleto)
                    .claim("rol", rol)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + JWT_TIEMPO_VALIDEZ))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                    .compact();

            // 4. Metemos el JWT en una Cookie Http-Only
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setMaxAge((int) (JWT_TIEMPO_VALIDEZ / 1000));
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            // 5. Devolvemos el JSON con los datos (para que el frontend los lea)
            Map<String, String> body = new HashMap<>();
            body.put("token", jwt);
            body.put("identificador", id);
            body.put("nombre", nombreCompleto);
            body.put("rol", rol);

            return ResponseEntity.ok(body);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Opcional: Operación para cerrar sesión (Nota 3 del enunciado)
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0); // Borra la cookie inmediatamente
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
    
*/
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody CredencialesDTO credenciales, HttpServletResponse response) {
		//Para la autenticacion se va a comunicar con el microservicio de usuario a traves de retrofit
		System.out.println("PostMan ha llegado a la pasarela con email: " + credenciales.getEmail() + " y clave: " + credenciales.getClave());
		try {
			System.out.println("Intentando verificar credenciales con el microservicio de Usuario, llamando a retrofit...");
			Response<UsuarioDTO> respuesta = retrofitUsuarios.verificarCredenciales(credenciales).execute();
			System.out.println("Respuesta del microservicio de Usuario recibida. Código: " + respuesta.code());
			if (respuesta.isSuccessful() && respuesta.body() != null) {
				UsuarioDTO usuarioValidado = respuesta.body();
				System.out.println(" Usuario encontrado: " + usuarioValidado.getNombre());
				//Creamos el token JWT
				Map<String, Object> claims = new HashMap<>();
				claims.put("sub", usuarioValidado.getId());
				claims.put("rol", usuarioValidado.isAdmin() ? "ADMINISTRADOR" : "USUARIO");
				System.out.println("Generando token JWT con los siguientes claims: " + claims);
				String token = JwtUtils.generateToken(claims);
				System.out.println("Token JWT generado: " + token);
				
				//Creamos la cookie con el token JWT
	            Cookie cookie = new Cookie("jwt", token);
	            cookie.setMaxAge((int)JWT_TIEMPO_VALIDEZ_LOGIN);
	            cookie.setHttpOnly(true);
	            cookie.setPath("/");
	            response.addCookie(cookie);

	            // 5. Devolvemos el JSON con los datos (para que el frontend los lea)
	            Map<String, String> body = new HashMap<>();
	            body.put("token", token);
	            body.put("identificador", usuarioValidado.getId());
	            body.put("nombre", usuarioValidado.getNombre() + " " + usuarioValidado.getApellidos());
	            body.put("rol", usuarioValidado.isAdmin() ? "ADMIN" : "USER");

	            return ResponseEntity.ok(body);
			} else {
                // Credenciales incorrectas
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
            }

	        } catch (Exception e) {
	        	e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	}
}