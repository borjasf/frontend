package productos.repositoriosAdHoc;

import org.springframework.data.jpa.repository.JpaRepository;

import modelo.Usuario;
import productos.repositoriosModelo.IRepositorioUsuario;

public interface RepositorioUsuarioAdHoc extends JpaRepository<Usuario, String>, IRepositorioUsuario {
	// Implementación de métodos personalizados para Usuario si es necesario

}
