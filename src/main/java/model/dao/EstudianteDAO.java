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
     * Debido a que en el script de BD actual no existe columna de password para estudiantes,
     * esta implementación valida simplemente la existencia del idPaciente en la base de datos.
     * Si en el futuro se agrega password en la BD, este método debe actualizarse.
     * @param idPaciente ID de paciente (cédula)
     * @param password Contraseña del estudiante (no usada en la versión actual)
     * @return Estudiante si las credenciales son válidas, null en caso contrario
     */
    @Override
    public Estudiante validarCredenciales(String idPaciente, String password) {
        return buscarPorIdPaciente(idPaciente);
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
     * Obtiene los estudiantes activos. Dado que la tabla `estudiante` no contiene
     * columna `activo` en el script actual, devolvemos todos los estudiantes.
     */
    @Override
    public List<Estudiante> obtenerActivos() {
        try {
            return getAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Cambia el estado activo de un estudiante - NO SOPORTADO
     * La tabla `estudiante` del script actual no contiene columna `activo`.
     * Por compatibilidad con la interfaz se implementa lanzando una excepción informativa.
     */
    @Override
    public void cambiarEstado(int id, boolean activo) {
        throw new UnsupportedOperationException("La entidad Estudiante no contiene el campo 'activo' en la base de datos.");
    }
}