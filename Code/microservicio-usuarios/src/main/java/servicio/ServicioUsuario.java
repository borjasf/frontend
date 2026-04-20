package servicio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import modelo.Usuario;
import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;
import repositoriosAdHoc.RepositorioUsuarioAdHoc;
import usuarios.eventos.EventoUsuarioActualizado;
import usuarios.eventos.EventoUsuarioContadorCompras;
import usuarios.eventos.EventoUsuarioContadorVentas;
import usuarios.eventos.EventoUsuarioCreado;
import usuarios.eventos.EventoUsuarioRol;
import usuarios.puertos.PublicadorEventos;

public class ServicioUsuario implements IServicioUsuario {
	
	//Definimos el repositorio de usuarios
	private RepositorioUsuarioAdHoc repositorioAdHoc = FactoriaRepositorios.getRepositorio(RepositorioUsuarioAdHoc.class);
	private Repositorio<Usuario, String> repositorio = FactoriaRepositorios.getRepositorio(Usuario.class);
	private PublicadorEventos publicadorEventos = FactoriaServicios.getServicio(PublicadorEventos.class);
	
	/**
	 * Funcionalidad: Registrar un nuevo usuario. 
	 */
	@Override
	public String registrarUsuario(String nombre, String apellidos, String email, String clave, LocalDate fechaNacimiento, String telefono, boolean admin) throws RepositorioException, IOException {
		
		//hacemos un control de integridad de los datos
		if (nombre == null || nombre.isEmpty()) {

			throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
		}
		
		if (apellidos == null || apellidos.isEmpty()) {
			throw new IllegalArgumentException("Los apellidos no pueden ser nulos o vacíos");
		}
		
		if (email == null || email.isEmpty() || !email.contains("@")) {
			throw new IllegalArgumentException("El email no es válido");
		}
		
		if (clave == null || clave.isEmpty() || clave.length() < 6) {
			throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres y no puede ser nula o vacía");
		}
		
		if (fechaNacimiento == null || fechaNacimiento.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("La fecha de nacimiento no es válida");
		}
		
		//Como telefono es opcional, no hacemos control de integridad
		
		//Comprobamos que no existe ya un uusario con ese email 
		if (repositorioAdHoc.buscarPorEmail(email) != null) {
			throw new RepositorioException("Ya existe un usuario registrado con ese email");
		}
		
		//Creamos el nuevo usuario
		Usuario nuevoUsuario = new Usuario(email, nombre, apellidos, clave, fechaNacimiento, telefono, admin);

		//lo almacenamos en el repositorio y devolvemos el id generado
		String idGenerado = repositorio.add(nuevoUsuario);
		
		//Se crea el evento
		EventoUsuarioCreado evento = new EventoUsuarioCreado(idGenerado, email, nombre, apellidos);
		this.publicadorEventos.publicarEvento(evento);
		
		return idGenerado; 


	}
	
	
	/**
	 * Funcionalidad: Actualizar datos de usuario.
	 * */
	@Override
	public void actualizarDatosUsuario(String idUsuario, String nombre, String apellidos, String email, String clave, LocalDate fechaNacimiento, String telefono) throws RepositorioException, EntidadNoEncontrada, IOException {
		
		if(email == null || email.isEmpty() || !email.contains("@")) {
			throw new IllegalArgumentException("El email no es válido");
		}
		if (nombre == null || nombre.isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
		}
		if (apellidos == null || apellidos.isEmpty()) {
			throw new IllegalArgumentException("Los apellidos no pueden ser nulos o vacíos");
		}
		if (clave == null || clave.isEmpty() || clave.length() < 6) {
			throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres y no puede ser nula o vacía");
		}
		if (fechaNacimiento == null || fechaNacimiento.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("La fecha de nacimiento no es válida");
		}
		
		Usuario usuarioExistente = repositorio.getById(idUsuario);
		
		if (usuarioExistente == null) {
			throw new EntidadNoEncontrada("No se encontró el usuario con ID: " + idUsuario);
		}
		
		//ahora comprobamos si el email que quiere poner ya lo tiene otro usuario
		if( !usuarioExistente.getEmail().equals(email)) {
			Usuario usuarioConEmail = repositorioAdHoc.buscarPorEmail(email);
			if (usuarioConEmail != null) {
				throw new RepositorioException("Ya existe un usuario registrado con ese email");
			}
		}
		
		//Actualizamos los datos del usuario
		usuarioExistente.setNombre(nombre);
		usuarioExistente.setApellidos(apellidos);
		usuarioExistente.setEmail(email);
		usuarioExistente.setClave(clave);
		usuarioExistente.setFechaNacimiento(fechaNacimiento);
		usuarioExistente.setTelefono(telefono);
		
		//Guardamos los cambios en el repositorio
		repositorio.update(usuarioExistente);	
		
		//Creamos el evento de usuario actualizado
		EventoUsuarioActualizado evento = new EventoUsuarioActualizado(idUsuario, email, nombre, apellidos, clave, telefono, fechaNacimiento.toString());
		this.publicadorEventos.publicarEvento(evento);
	}
	
	// ELIMINADO: public void modificarCategoria(...)
    // Este método sobraba porque este microservicio ya no gestiona categorías.


	@Override
	public void asignarRolAdmin(String idUsuario2) throws RepositorioException, EntidadNoEncontrada, IOException {
		
		Usuario usuario = repositorio.getById(idUsuario2);
		
		usuario.setAdmin(true);
		
		repositorio.update(usuario);
		
		//Creamos el evento de usuario actualizado
		EventoUsuarioRol evento = new EventoUsuarioRol(idUsuario2, usuario.getEmail(), usuario.getNombre(), true);
		this.publicadorEventos.publicarEvento(evento);
	}

	@Override
	public Usuario iniciarSesion(String email, String clave) throws RepositorioException, EntidadNoEncontrada {
		Usuario usuario = repositorioAdHoc.buscarPorEmail(email);
		
		if (usuario == null) {
			throw new EntidadNoEncontrada("No se encontró el usuario con email: " + email);
		}
		if (!usuario.getClave().equals(clave)) {
			throw new RepositorioException("Contraseña incorrecta");
		}
		return usuario;	
	}
	
	//Nueva funcionalidad: Recuperar un usuario por su id
	@Override
	public Usuario recuperar(String id) throws RepositorioException, EntidadNoEncontrada {
		return repositorio.getById(id);
	}
	
	// Recuperar todos
	@Override
	public List<Usuario> recuperarTodos() throws RepositorioException {
		return repositorio.getAll();	
	}
	
	//Recuperamos al usuario por su id de Github
	@Override
	public Usuario getByGitHubId(String githubId) throws RepositorioException, EntidadNoEncontrada {
		Usuario usuario = repositorioAdHoc.getByGitHubId(githubId);
		
		if (usuario == null) {
			throw new EntidadNoEncontrada("No se encontró el usuario con GitHub ID: " + githubId);
		}
		
		return usuario;
	}


	@Override
	public boolean autenticarUsuario(String email, String password) throws RepositorioException, EntidadNoEncontrada {
		
		Usuario user = repositorioAdHoc.buscarPorEmail(email);
		if (user == null || !user.getClave().equals(password)) {
			return false;
		}
		return true;
		
	}


	@Override
	public void incrementarContadorCompras(String id) throws RepositorioException, EntidadNoEncontrada, IOException {
		// Cambiamos buscarPorEmail por getById
		Usuario usuario = repositorio.getById(id);
		if (usuario == null) {
			throw new EntidadNoEncontrada("No se encontró el usuario con ID: " + id);
		}
		
		usuario.incrementarContadorCompras();
		repositorio.update(usuario);
		
		EventoUsuarioContadorCompras evento = new EventoUsuarioContadorCompras(usuario.getId(), usuario.getEmail(), usuario.getContadorCompras());
		this.publicadorEventos.publicarEvento(evento);
	}

	@Override
	public void incrementarContadorVentas(String id) throws RepositorioException, EntidadNoEncontrada, IOException {
		// Cambiamos buscarPorEmail por getById
		Usuario usuario = repositorio.getById(id);
		if (usuario == null) {
			throw new EntidadNoEncontrada("No se encontró el usuario con ID: " + id);
		}
		
		usuario.incrementarContadorVentas();
		repositorio.update(usuario);
		
		EventoUsuarioContadorVentas evento = new EventoUsuarioContadorVentas(usuario.getId(), usuario.getEmail(), usuario.getContadorVentas());
		this.publicadorEventos.publicarEvento(evento);
	}
	
	@Override
	public Usuario getByEmail(String email) throws RepositorioException, EntidadNoEncontrada {
		Usuario usuario = repositorioAdHoc.buscarPorEmail(email);
		
		if (usuario == null) {
			throw new EntidadNoEncontrada("No se encontró el usuario con email: " + email);
		}
		
		return usuario;
	}


	@Override
	public void vincularGitHub(String id, String githubId)
			throws RepositorioException, EntidadNoEncontrada, IOException {
		Usuario usuarioExistente = repositorio.getById(id);
		if (usuarioExistente == null) {
			throw new EntidadNoEncontrada("No se encontró el usuario con ID: " + id);
		}
		usuarioExistente.setGitHubId(githubId);
		repositorio.update(usuarioExistente);
		
		//Creamos el evento de actualización de GitHub ID
		EventoUsuarioActualizado evento = new EventoUsuarioActualizado(usuarioExistente.getId(), usuarioExistente.getEmail(), usuarioExistente.getNombre(), usuarioExistente.getApellidos(), usuarioExistente.getClave(), usuarioExistente.getTelefono(), usuarioExistente.getFechaNacimiento().toString());
		this.publicadorEventos.publicarEvento(evento);
		
	}
}