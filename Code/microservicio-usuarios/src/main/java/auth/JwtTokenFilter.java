package auth;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Priority;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Claims;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtTokenFilter implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;

	@Context
	private HttpServletRequest servletRequest;

	@Override
	public void filter(ContainerRequestContext requestContext) {

		String path = requestContext.getUriInfo().getPath();
		String method = requestContext.getMethod();

		// Comprobamos si la ruta tiene la anotación @PermitAll
		if (resourceInfo.getResourceMethod().isAnnotationPresent(PermitAll.class)) {
			return;
		}

		// 1. REGLAS PÚBLICAS: POST /usuarios (Alta), POST /auth/login (Login)
		// Y AHORA: GET /usuarios/{id}/nombre (Para el microservicio de compraventas)
		if (path.startsWith("auth/login") || (path.startsWith("usuarios") && method.equals("POST"))
				|| (path.startsWith("usuarios") && path.endsWith("/nombre") && method.equals("GET"))) {
			return; // Dejamos pasar sin pedir token
		}

		// Implementación del control de autorización
		String authorization = requestContext.getHeaderString("Authorization");

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
					.entity("No se adjunta el token correctamente").build());
		} else {
			String token = authorization.substring("Bearer ".length()).trim();
			try {
				// Validar el token ...
				Claims claims = JwtUtils.validateToken(token);

				this.servletRequest.setAttribute("claims", claims);

				String rolValue = claims.get("rol", String.class);
				Set<String> roles = rolValue != null ? new HashSet<>(Arrays.asList(rolValue.split(","))) : new HashSet<>();

				// Consulta si la operación está protegida por rol
				if (this.resourceInfo.getResourceMethod().isAnnotationPresent(RolesAllowed.class)) {

					String[] allowedRoles = resourceInfo.getResourceMethod().getAnnotation(RolesAllowed.class).value();

					if (roles.stream().noneMatch(userRole -> Arrays.asList(allowedRoles).contains(userRole))) {
						requestContext.abortWith(
								Response.status(Response.Status.FORBIDDEN).entity("no tiene rol de acceso").build());
					}
				}
			} catch (Exception e) { // Error de validación
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build());
			}
		}

	}
}
