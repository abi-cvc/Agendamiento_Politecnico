package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.entity.Administrador;
import util.JPAUtil;

import java.util.List;

/**
 * DAO para la entidad Administrador
 * Maneja todas las operaciones CRUD usando JPA/ORM
 */
public class AdministradorDAO {
    
    /**
     * Guarda un nuevo administrador en la base de datos
     * @param administrador Objeto Administrador a guardar
     */
    public void guardar(Administrador administrador) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(administrador);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    /**
     * Actualiza un administrador existente
     * @param administrador Objeto Administrador con los datos actualizados
     */
    public void actualizar(Administrador administrador) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(administrador);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    /**
     * Elimina un administrador de la base de datos
     * @param id ID del administrador a eliminar
     */
    public void eliminar(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Administrador administrador = em.find(Administrador.class, id);
            if (administrador != null) {
                em.remove(administrador);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca un administrador por su ID
     * @param id ID del administrador
     * @return Administrador encontrado o null
     */
    public Administrador buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Administrador.class, id);
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca un administrador por su ID de admin
     * @param idAdmin ID de admin
     * @return Administrador encontrado o null
     */
    public Administrador buscarPorIdAdmin(String idAdmin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Administrador> query = em.createQuery(
                "SELECT a FROM Administrador a WHERE a.idAdmin = :idAdmin", 
                Administrador.class
            );
            query.setParameter("idAdmin", idAdmin);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca un administrador por su correo electrónico
     * @param correo Correo del administrador
     * @return Administrador encontrado o null
     */
    public Administrador buscarPorCorreo(String correo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Administrador> query = em.createQuery(
                "SELECT a FROM Administrador a WHERE a.correoAdmin = :correo", 
                Administrador.class
            );
            query.setParameter("correo", correo);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtiene todos los administradores
     * @return Lista de todos los administradores
     */
    public List<Administrador> obtenerTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Administrador> query = em.createQuery(
                "SELECT a FROM Administrador a ORDER BY a.nombreAdmin, a.apellidoAdmin", 
                Administrador.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtiene todos los administradores activos
     * @return Lista de administradores activos
     */
    public List<Administrador> obtenerActivos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Administrador> query = em.createQuery(
                "SELECT a FROM Administrador a WHERE a.activo = true " +
                "ORDER BY a.nombreAdmin, a.apellidoAdmin", 
                Administrador.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Valida las credenciales de un administrador para login
     * @param idAdmin ID de admin
     * @param password Contraseña del administrador
     * @return Administrador si las credenciales son válidas, null en caso contrario
     */
    public Administrador validarCredenciales(String idAdmin, String password) {
        Administrador admin = buscarPorIdAdmin(idAdmin);
        
        if (admin != null && admin.isActivo() && admin.iniciarSesion(password)) {
            return admin;
        }
        
        return null;
    }
    
    /**
     * Verifica si un administrador existe por ID de admin
     * @param idAdmin ID de admin a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existePorIdAdmin(String idAdmin) {
        return buscarPorIdAdmin(idAdmin) != null;
    }
    
    /**
     * Verifica si un administrador existe por correo
     * @param correo Correo a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existePorCorreo(String correo) {
        return buscarPorCorreo(correo) != null;
    }
    
    /**
     * Cambia el estado activo de un administrador
     * @param id ID del administrador
     * @param activo Nuevo estado
     */
    public void cambiarEstado(int id, boolean activo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Administrador administrador = em.find(Administrador.class, id);
            if (administrador != null) {
                administrador.setActivo(activo);
                em.merge(administrador);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
