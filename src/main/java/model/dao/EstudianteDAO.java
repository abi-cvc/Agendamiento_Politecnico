package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.entity.Estudiante;
import model.entity.Cita;

import java.util.List;
import java.util.ArrayList;

import util.JPAUtil;

/**
 * DAO para la entidad Estudiante
 * Maneja todas las operaciones CRUD usando JPA
 */
public class EstudianteDAO extends JPAGenericDAO<Estudiante, Integer> implements IEstudianteDAO {

    public EstudianteDAO() {
        super(Estudiante.class);
    }
    
    /**
     * Guarda un nuevo estudiante en la base de datos
     * @param estudiante Objeto Estudiante a guardar
     */
    @Override
    public Estudiante create(Estudiante estudiante) {
        return super.create(estudiante);
    }
    
    /**
     * Actualiza un estudiante existente
     * @param estudiante Objeto Estudiante con los datos actualizados
     */
    @Override
    public Estudiante update(Estudiante estudiante) {
        return super.update(estudiante);
    }
    
    /**
     * Elimina un estudiante de la base de datos
     * @param id ID del estudiante a eliminar
     */
    @Override
    public void delete(Integer id) {
        super.delete(id);
    }
    
    /**
     * Busca un estudiante por su ID
     * @param id ID del estudiante
     * @return Estudiante encontrado o null
     */
    @Override
    public Estudiante getById(Integer id) {
        return super.getById(id);
    }
    
    /**
     * Busca un estudiante por su ID de paciente (cédula)
     * @param idPaciente ID de paciente (cédula)
     * @return Estudiante encontrado o null
     */
    @Override
    public Estudiante buscarPorIdPaciente(String idPaciente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Estudiante> query = em.createQuery(
                "SELECT e FROM Estudiante e WHERE e.idPaciente = :idPaciente", 
                Estudiante.class
            );
            query.setParameter("idPaciente", idPaciente);
            List<Estudiante> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
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
    @Override
    public Estudiante buscarPorCorreo(String correo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Estudiante> query = em.createQuery(
                "SELECT e FROM Estudiante e WHERE e.correoEstudiante = :correo", 
                Estudiante.class
            );
            query.setParameter("correo", correo);
            List<Estudiante> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
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
    @Override
    public List<Estudiante> getAll() {
        return super.getAll();
    }
    
    /**
     * Obtiene todas las citas de un estudiante
     * @param idEstudiante ID del estudiante
     * @return Lista de citas del estudiante
     */
    @Override
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
     * Ahora comprueba password_estudiante y campo activo en la BD
     * @param idPaciente ID de paciente (cédula)
     * @param password Contraseña del estudiante
     * @return Estudiante si las credenciales son válidas, null en caso contrario
     */
    @Override
    public Estudiante validarCredenciales(String idPaciente, String password) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Estudiante> query = em.createQuery(
                "SELECT e FROM Estudiante e WHERE e.idPaciente = :idPaciente AND e.activo = true",
                Estudiante.class
            );
            query.setParameter("idPaciente", idPaciente);
            List<Estudiante> results = query.getResultList();
            if (results.isEmpty()) {
                return null;
            }
            Estudiante est = results.get(0);
            // Comprobar contraseña (en texto plano, según esquema actual)
            if (password != null && password.equals(est.getPasswordEstudiante())) {
                return est;
            }
            return null;
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
    @Override
    public boolean existePorIdPaciente(String idPaciente) {
        return buscarPorIdPaciente(idPaciente) != null;
    }
    
    /**
     * Verifica si un estudiante existe por correo
     * @param correo Correo a verificar
     * @return true si existe, false en caso contrario
     */
    @Override
    public boolean existePorCorreo(String correo) {
        return buscarPorCorreo(correo) != null;
    }

    /**
     * Obtiene los estudiantes activos.
     */
    @Override
    public List<Estudiante> obtenerActivos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Estudiante> query = em.createQuery(
                "SELECT e FROM Estudiante e WHERE e.activo = true ORDER BY e.nombreEstudiante, e.apellidoEstudiante",
                Estudiante.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Cambia el estado activo de un estudiante
     */
    @Override
    public void cambiarEstado(int id, boolean activo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Estudiante estudiante = em.find(Estudiante.class, id);
            if (estudiante != null) {
                estudiante.setActivo(activo);
                em.merge(estudiante);
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