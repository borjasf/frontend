package rest;

import java.net.URI;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import dto.ListadoUsuarios;
import dto.ListadoUsuarios.UsuarioResumen;
import dto.UsuarioDTO;
import dto.UsuarioRegistroDTO;
import dto.UsuarioActualizacionDTO;
import dto.UsuarioResumenDTO;
import dto.CredencialesDTO;
import modelo.Usuario;
import servicio.FactoriaServicios;
import servicio.IServicioUsuario;

@Path("usuarios")

public class ControladorUsuario {

	private IServicioUsuario servicio = FactoriaServicios.getServicio(IServicioUsuario.class);

	@Context
	private UriInfo uriInfo;

	@Context
	private SecurityContext securityContext;

	// Dar de alta un usuario
	// curl -i -X POST -H "Content-Type: application/xml" -d
	// @test-files/usuario1.xml http://localhost:8080/api/usuarios/
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll // Permite el acceso sin token (registro público)
	public Response altaUsuario(UsuarioRegistroDTO dto) throws Exception {
		//primero comprobamos que la fecha no sea nula o esté vacía
		if (dto.getFechaNacimiento() == null || dto.getFechaNacimiento().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("La fecha de nacimiento es obligatoria").build();
		}
		LocalDate fecha = LocalDate.parse(dto.getFechaNacimiento());
		// El controlador extrae los datos del DTO específico de registro
		String id = servicio.registrarUsuario(dto.getNombre(), dto.getApellidos(), dto.getEmail(), dto.getClave(),
				fecha, dto.getTelefono(), dto.isAdmin());

		// Generar cabecera Location (HATEOAS)
		URI uri = uriInfo.getAbsolutePathBuilder().path(id).build();
		return Response.created(uri).build();
	}

	// Recuperar un usuario por su id (Nueva funcionalidad)
	// http://localhost:8080/api/usuarios/usuario1
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsuario(@PathParam("id") String id) throws Exception {
		Usuario usuario = servicio.recuperar(id);

		// Convertimos la Entidad del modelo a DTO (se omite la clave por seguridad)
		UsuarioDTO dto = new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getApellidos(),
				usuario.getEmail(),usuario.getFechaNacimiento().toString() , usuario.getTelefono(), usuario.isAdmin());

		return Response.status(Response.Status.OK).entity(dto).build();
	}

	// Modificar un usuario
	// curl -i -X PUT -H "Content-type: application/xml" -d @test-files/usuario1.xml
	// http://localhost:8080/api/usuarios/usuario1
	@PATCH
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") String id, UsuarioActualizacionDTO dto) throws Exception {

		// REGLA: "Solo el usuario podrá modificar sus datos"
		// Comprobamos si el ID del token coincide con el ID de la URL
		String idAutenticado = securityContext.getUserPrincipal().getName();

		if (!idAutenticado.equals(id)) {
			return Response.status(Response.Status.FORBIDDEN)
					.entity("Acceso denegado: Solo puedes modificar tus propios datos.").build();
		}
		
		LocalDate fecha = LocalDate.parse(dto.getFechaNacimiento());
		// Pasamos los datos del DTO al servicio para actualizar
		servicio.actualizarDatosUsuario(id, dto.getNombre(), dto.getApellidos(), dto.getEmail(), dto.getClave(),
				fecha, dto.getTelefono());

		return Response.status(Response.Status.NO_CONTENT).build();
	}

	// conseguir un listado de usuarios
	// curl -i -H "Accept: application/xml" http://localhost:8080/api/usuarios

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListadoUsuarios() throws Exception {

		List<Usuario> usuarios = servicio.recuperarTodos();

		ListadoUsuarios listado = new ListadoUsuarios();
		List<UsuarioResumen> listaResumenes = new LinkedList<>();

		for (Usuario u : usuarios) {
			UsuarioResumen res = new UsuarioResumen();

			// Usamos el DTO de resumen (asegúrate de que tu constructor reciba id, nombre,
			// email)
			UsuarioResumenDTO resumenDTO = new UsuarioResumenDTO(u.getId(), u.getNombre(), u.getEmail());
			res.setResumen(resumenDTO);

			String url = uriInfo.getAbsolutePathBuilder().path(u.getId()).build().toString();
			res.setUrl(url);

			listaResumenes.add(res);
		}

		listado.setUsuarios(listaResumenes);
		return Response.status(Response.Status.OK).entity(listado).build();
	}



	// Obtener solo el nombre de un usuario a partir de su ID (Público)
	@GET
	@Path("/{id}/nombre")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNombreUsuario(@PathParam("id") String id) throws Exception {

		// Recuperamos el usuario de la base de datos
		Usuario usuario = servicio.recuperar(id);

		// Construimos un JSON simple con el nombre y apellidos
		String jsonResponse = String.format("{\"nombre\": \"%s\", \"apellidos\": \"%s\"}", usuario.getNombre(),
				usuario.getApellidos());

		// Lo devolvemos con código 200 OK
		return Response.status(Response.Status.OK).entity(jsonResponse).build();
	}
	
	@POST 
	@Path("/verificar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response verificarCredenciales(CredencialesDTO credenciales) {
		try { 
			Usuario user = servicio.getByEmail(credenciales.getEmail());
			//Si las contraseñas coinciden, devolvemos un DTO con los datos del usuario (sin la clave)
			if (user != null && user.getClave().equals(credenciales.getClave())) {
				UsuarioDTO dto = new UsuarioDTO(user.getId(), user.getNombre(), user.getApellidos(),
						user.getEmail(), user.getFechaNacimiento().toString(),user.getTelefono(), user.isAdmin());
				return Response.ok(dto).build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciales incorrectas").build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al verificar credenciales").build();
		}
	}
	
	
	@GET
	@Path("/buscarPorGitHub")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response buscarPorGitHub(@QueryParam("githubId") String githubId) throws Exception {
	    Usuario usuario = servicio.getByGitHubId(githubId);
	    UsuarioDTO dto = new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getApellidos(),
	            usuario.getEmail(), usuario.getFechaNacimiento().toString(), usuario.getTelefono(), usuario.isAdmin());
	    return Response.ok(dto).build();
	}
	
	@GET
	@Path("/buscarPorEmail")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response buscarPorEmail(@QueryParam("email") String email) throws Exception {
	    Usuario usuario = servicio.getByEmail(email);
	    UsuarioDTO dto = new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getApellidos(),
	            usuario.getEmail(), usuario.getFechaNacimiento().toString(), usuario.getTelefono(), usuario.isAdmin());
	    return Response.ok(dto).build();
	}
	
	@POST
	@Path("/{id}/vincularGitHub")
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response vincularGitHub(@PathParam("id") String id, @QueryParam("githubId") String githubId) throws Exception {
	    servicio.vincularGitHub(id, githubId);
	    return Response.noContent().build();
	}

}
