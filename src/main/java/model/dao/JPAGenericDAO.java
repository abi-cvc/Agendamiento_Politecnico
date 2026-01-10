package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import util.JPAUtil;

import java.util.List;

/**
 * Implementación base de GenericDAO usando JPA
 * Sigue el patrón del diagrama de arquitectura con JPA Generic DAO
 * 
 * @param <T> Tipo de la entidad
 * @param <ID> Tipo del identificador
 */
public abstract class JPAGenericDAO<T, ID> implements GenericDAO<T, ID> {
    
    private final Class<T> entityClass;
    
    /**
     * Constructor que requiere la clase de la entidad
     * @param entityClass Clase de la entidad que se va a manejar
     */
    public JPAGenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    @Override
    public T create(T entity) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al crear entidad: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public T update(T entity) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            T merged = em.merge(entity);
            em.getTransaction().commit();
            return merged;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar entidad: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public void delete(ID id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar entidad: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public T getById(ID id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<T> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<T> query = em.createQuery(jpql, entityClass);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Método auxiliar para obtener el EntityManager
     * Útil para queries personalizadas en las subclases
     */
    protected EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }
    
    /**
     * Método auxiliar para ejecutar una query personalizada
     */
    protected List<T> executeQuery(String jpql) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<T> query = em.createQuery(jpql, entityClass);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
