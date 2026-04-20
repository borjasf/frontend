package repositoriosAdHoc;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import modelo.Usuario;
import repositorio.RepositorioException;
import repositoriosModelo.RepositorioUsuarioJPA;
import utils.EntityManagerHelper;


public class RepositorioUsuarioAdHocJPA extends RepositorioUsuarioJPA implements RepositorioUsuarioAdHoc {


	/**
	 * Esta función nos permite buscar un usuario por su email.
	 */
	@Override
	public Usuario buscarPorEmail(String email) throws RepositorioException {
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();
			String queryString = "SELECT u FROM Usuario u WHERE u.email = :emailParam";
			
			TypedQuery<Usuario> query = em.createQuery(queryString, Usuario.class);
			query.setHint(QueryHints.REFRESH, HintValues.TRUE);
			query.setParameter("emailParam", email);
			return query.getSingleResult();
		} catch (javax.persistence.NoResultException e) {
	        // Si no lo encuentra, devuelve null en lugar de lanzar una excepción
	        return null;
	    
		} catch (Exception e) {
			throw new RepositorioException("Error al buscar usuarios por email: " + e.getMessage(), e);
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
	}
	
	/*Esta funcion nos va a servir para poder buscar un usuario en funcion del id de Github*/
	@Override
	public Usuario getByGitHubId(String githubId) throws RepositorioException {
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();
			String queryString = "SELECT u FROM Usuario u WHERE u.githubId = :githubIdParam";
			
			TypedQuery<Usuario> query = em.createQuery(queryString, Usuario.class);
			query.setHint(QueryHints.REFRESH, HintValues.TRUE);
			query.setParameter("githubIdParam", githubId);
			return query.getSingleResult();
		} catch (javax.persistence.NoResultException e) {
	        // Si no lo encuentra, devuelve null en lugar de lanzar una excepción
	        return null;
	    
		} catch (Exception e) {
			throw new RepositorioException("Error al buscar usuarios por GitHub ID: " + e.getMessage(), e);
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
	}
		

}
