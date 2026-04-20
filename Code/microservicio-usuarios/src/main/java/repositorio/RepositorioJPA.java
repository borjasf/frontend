package repositorio; 

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;



import utils.EntityManagerHelper;

public abstract class RepositorioJPA<T extends Identificable> implements RepositorioString<T> {
    
    public abstract Class<T> getClase();

    @Override
    public String add(T entity) throws RepositorioException {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            em.getTransaction().begin();
            //las transacciones las hacemos nosotros la abrimos con begin, guardo y confirmo la transaccion 
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new RepositorioException("Error al guardar la entidad", e);
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            EntityManagerHelper.closeEntityManager();
        }
        return entity.getId();
    }

    @Override
    public void update(T entity) throws RepositorioException, EntidadNoEncontrada {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            em.getTransaction().begin();
            //compruebo si la entidad no existe
            T instancia = em.find(getClase(), entity.getId());
            if(instancia == null) {
            	//si es nula es que no esta 
                throw new EntidadNoEncontrada(entity.getId() + " no existe en el repositorio");
            }
            //como no estoy seguro de que el entity esta dentro lo meto otra vez
            entity = em.merge(entity);          
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            throw new RepositorioException("Error al actualizar la entidad con id "+entity.getId(),e);
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            EntityManagerHelper.closeEntityManager();
        }
    }

    @Override
    public void delete(T entity) throws RepositorioException, EntidadNoEncontrada {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
        	//empezamos una transaccion
            em.getTransaction().begin();
            //la buscamos
            T instancia = em.find(getClase(), entity.getId());
            if(instancia == null) {
                throw new EntidadNoEncontrada(entity.getId() + " no existe en el repositorio");
            }
            //borramos la instancia que hemos cogido 
            em.remove(instancia);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            throw new RepositorioException("Error al borrar la entidad con id "+entity.getId(),e);
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            EntityManagerHelper.closeEntityManager();
        }
    }

    @Override
    public T getById(String id) throws EntidadNoEncontrada, RepositorioException {
        try {   
        EntityManager em = EntityManagerHelper.getEntityManager();
                
            T instancia = em.find(getClase(), id);
            
            if (instancia == null) {
                throw new EntidadNoEncontrada(id + " no existe en el repositorio");             
            } else {
            	//el refresh lo que hace es cargar en memoria con lo que esta actualizado en la bbdd
            	//hago el refresh y me aseguro
                em.refresh(instancia);
            }
            return instancia;

        } catch (RuntimeException e) {
            throw new RepositorioException("Error al recuperar la entidad con id "+id,e);
        }
        finally {
            EntityManagerHelper.closeEntityManager();
        }
    }
    
    
    //Hacemos una consulta SELECT * FROM ENCUESTA
    @Override
    public List<T> getAll() throws RepositorioException{
        try {
        EntityManager em = EntityManagerHelper.getEntityManager();
        
            final String queryString = " SELECT t from " + getClase().getSimpleName() + " t ";

            TypedQuery<T> query = em.createQuery(queryString, getClase());

            //SE PUEDE SALTAR
            query.setHint(QueryHints.REFRESH, HintValues.TRUE);

            return query.getResultList();

        } catch (RuntimeException e) {

            throw new RepositorioException("Error buscando todas las entidades de "+getClase().getSimpleName(),e);

        }
        finally {
            EntityManagerHelper.closeEntityManager();
        }
    }

    @Override
    public List<String> getIds() throws RepositorioException{
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            final String queryString = " SELECT t.id from " + getClase().getSimpleName() + " t ";

            Query query = em.createQuery(queryString);

            query.setHint(QueryHints.REFRESH, HintValues.TRUE);

            return query.getResultList();

        } catch (RuntimeException e) {

            throw new RepositorioException("Error buscando todos los ids de "+getClase().getSimpleName(),e);

        }
        finally {
            EntityManagerHelper.closeEntityManager();
        }
    }

}