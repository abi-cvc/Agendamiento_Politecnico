package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.entity.Estudiante;
import model.entity.Cita;
import util.JPAUtil;

import java.util.List;

/**
 * DAO para la entidad Estudiante
 * Maneja todas las operaciones CRUD usando JPA/ORM
 */
public class EstudianteDAO {
    
    /**
     * Guarda un nuevo estudiante en la base de datos
     * @param estudiante Objeto Estudiante a guardar
     */
    public void guardar(Estudiante estudiante) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(estudiante);
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
     * Actualiza un estudiante existente
     * @param estudiante Objeto Estudiante con los datos actualizados
     */
    public void actualizar(Estudiante estudiante) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(estudiante);
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
     * Elimina un estudiante de la base de datos
     * @param id ID del estudiante a eliminar
     */
    public void eliminar(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Estudiante estudiante = em.find(Estudiante.class, id);
            if (estudiante != null) {
                em.remove(estudiante);
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
     * Busca un estudiante por su ID
     * @param id ID del estudiante
     * @return Estudiante encontrado o null
     */
    public Estudiante buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Estudiante.class, id);
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca un estudiante por su ID de paciente (cédula)
     * @param idPaciente ID de paciente (cédula)
     * @return Estudiante encontrado o null
     */
    public Estudiante buscarPorIdPaciente(String idPaciente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Estudiante> query = em.createQuery(
                "SELECT e FROM Estudiante e WHERE e.idPaciente = :idPaciente", 
                Estudiante.class
            );
            query.setParameter("idPaciente", idPaciente);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca un estudiante por su correo electrónico
     * @param correo Correo del estudiante
     * @return Estudiante encontrado o null
     */
    public Estudiante buscarPorCorreo(String correo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Estudiante> query = em.createQuery(
                "SELECT e FROM Estudiante e WHERE e.correoEstudiante = :correo", 
                Estudiante.class
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
     * Obtiene todos los estudiantes
     * @return Lista de todos los estudiantes
     */
    public List<Estudiante> obtenerTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Estudiante> query = em.createQuery(
                "SELECT e FROM Estudiante e ORDER BY e.apellidoEstudiante, e.nombreEstudiante", 
                Estudiante.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtiene todas las citas de un estudiante
     * @param idEstudiante ID del estudiante
     * @return Lista de citas del estudiante
     */
    public List<Cita> obtenerCitasDeEstudiante(int idEstudiante) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cita> query = em.createQuery(
                "SELECT c FROM Cita c WHERE c.estudiante.idEstudiante = :idEstudiante " +
                "ORDER BY c.fechaCita DESC, c.horaCita DESC", 
                Cita.class
            );
            query.setParameter("idEstudiante", idEstudiante);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Valida las credenciales de un estudiante para login
     * @param idPaciente ID de paciente (cédula)
     * @param password Contraseña del estudiante
     * @return Estudiante si las credenciales son válidas, null en caso contrario
     */
    public Estudiante validarCredenciales(String idPaciente, String password) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Estudiante> query = em.createQuery(
                "SELECT e FROM Estudiante e WHERE e.idPaciente = :idPaciente " +
                "AND e.passwordEstudiante = :password", 
                Estudiante.class
            );
            query.setParameter("idPaciente", idPaciente);
            query.setParameter("password", password);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    /**
     * Verifica si un estudiante existe por ID de paciente
     * @param idPaciente ID de paciente a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existePorIdPaciente(String idPaciente) {
        return buscarPorIdPaciente(idPaciente) != null;
    }
    
    /**
     * Verifica si un estudiante existe por correo
     * @param correo Correo a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existePorCorreo(String correo) {
        return buscarPorCorreo(correo) != null;
    }
}
