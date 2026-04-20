package segundum.pasarela.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import segundum.pasarela.dto.UsuarioDTO;
import segundum.pasarela.dto.UsuarioRegistroDTO;
import retrofit2.Response;

import javax.servlet.http.Cookie;

@Component
public class SecuritySuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private RetrofitUsuarios retrofitUsuarios;
	private static final int JWT_TIEMPO_VALIDEZ = 60 * 60;
	private static final String NODE_CALLBACK_URL = "http://localhost:3001/auth/github/callback";

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		DefaultOAuth2User usuario = (DefaultOAuth2User) authentication.getPrincipal();

		String githubId = String.valueOf(usuario.getAttributes().get("id"));
		System.out.println("=== ATRIBUTOS DE GITHUB ===");
		usuario.getAttributes().forEach((k, v) -> System.out.println(k + " = " + v));
		System.out.println("===========================");
		String emailGitHub = String.valueOf(usuario.getAttributes().get("email"));
		String loginGitHub = String.valueOf(usuario.getAttributes().get("login"));
		String nameGitHub = String.valueOf(usuario.getAttributes().get("name"));

		try {
			Response<UsuarioDTO> respuestaRetrofit = retrofitUsuarios.buscarUsuarioPorGitHub(githubId).execute();
			UsuarioDTO usuarioDTO = null;

			if (respuestaRetrofit.isSuccessful() && respuestaRetrofit.body() != null) {
				usuarioDTO = respuestaRetrofit.body();
			} else if (emailGitHub != null && !emailGitHub.equals("null")) {
				Response<UsuarioDTO> respuestaEmail = retrofitUsuarios.buscarUsuarioPorEmail(emailGitHub).execute();
				if (respuestaEmail.isSuccessful() && respuestaEmail.body() != null) {
					usuarioDTO = respuestaEmail.body();
					retrofitUsuarios.vincularGitHub(usuarioDTO.getId(), githubId).execute();
					System.out.println("GitHub ID vinculado automáticamente al usuario: " + usuarioDTO.getNombre());
				}
			}

			// Auto-registro: si el usuario no existe, lo creamos con los datos de GitHub
			if (usuarioDTO == null) {
				String email = (emailGitHub != null && !emailGitHub.equals("null"))
						? emailGitHub
						: loginGitHub + "@github.placeholder";

				String fullName = (nameGitHub != null && !nameGitHub.equals("null") && !nameGitHub.isBlank())
						? nameGitHub : loginGitHub;

				String nombre = fullName.contains(" ")
						? fullName.substring(0, fullName.indexOf(' '))
						: fullName;

				String apellidos = fullName.contains(" ")
						? fullName.substring(fullName.indexOf(' ') + 1)
						: "-";

				UsuarioRegistroDTO nuevoUsuario = new UsuarioRegistroDTO();
				nuevoUsuario.setNombre(nombre);
				nuevoUsuario.setApellidos(apellidos);
				nuevoUsuario.setEmail(email);
				nuevoUsuario.setClave(UUID.randomUUID().toString());
				nuevoUsuario.setFechaNacimiento("2000-01-01");
				nuevoUsuario.setTelefono("");
				nuevoUsuario.setAdmin(false);

				Response<Void> createResp = retrofitUsuarios.crearUsuario(nuevoUsuario).execute();

				if (createResp.isSuccessful()) {
					System.out.println("Usuario de GitHub registrado automáticamente: " + nombre);
					// Buscamos el usuario recién creado por email para obtener su ID
					Response<UsuarioDTO> buscado = retrofitUsuarios.buscarUsuarioPorEmail(email).execute();
					if (buscado.isSuccessful() && buscado.body() != null) {
						usuarioDTO = buscado.body();
						retrofitUsuarios.vincularGitHub(usuarioDTO.getId(), githubId).execute();
					}
				}
			}

			if (usuarioDTO != null) {
				Map<String, Object> claims = new HashMap<>();
				claims.put("sub", usuarioDTO.getId());
				claims.put("roles", usuarioDTO.isAdmin() ? "ADMIN" : "USER");
				String jwt = JwtUtils.generateToken(claims);

				Cookie cookie = new Cookie("jwt", jwt);
				cookie.setMaxAge(JWT_TIEMPO_VALIDEZ);
				cookie.setHttpOnly(true);
				cookie.setPath("/");
				response.addCookie(cookie);

				response.sendRedirect(NODE_CALLBACK_URL + "?token=" + jwt);
			} else {
				response.sendRedirect("http://localhost:3001/login?error=github");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("http://localhost:3001/login?error=github");
		}
	}

}
