package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.entity.Doctor;
import model.entity.Especialidad;
import util.JPAUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Doctor
 * Extiende JPAGenericDAO e implementa IDoctorDAO según el patrón del diagrama de arquitectura
 */
public class DoctorDAO extends JPAGenericDAO<Doctor, Integer> implements IDoctorDAO {
    
	public DoctorDAO() {
		super(Doctor.class);
	}
	
	// ===== IMPLEMENTACIÓN DE IDoctorDAO =====
	
	@Override
	public List<Doctor> obtenerDoctoresActivos() {
		EntityManager em = getEntityManager();
        try {
            TypedQuery<Doctor> query = em.createQuery(
                "SELECT d FROM Doctor d WHERE d.activo = true ORDER BY d.apellido", 
                Doctor.class
            );
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener doctores: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
	}
	
	@Override
	public List<Doctor> obtenerPorEspecialidad(Especialidad especialidad) {
		EntityManager em = getEntityManager();
        try {
            TypedQuery<Doctor> query = em.createQuery(
                "SELECT d FROM Doctor d WHERE d.especialidad = :especialidad AND d.activo = true ORDER BY d.apellido",
                Doctor.class
            );
            query.setParameter("especialidad", especialidad);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener doctores por especialidad: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
	}
	
	// ===== MÉTODOS ESPECÍFICOS ADICIONALES =====
	
    /**
     * Obtiene todos los doctores activos
     * @deprecated Usar obtenerDoctoresActivos() de IDoctorDAO
     */
	@Deprecated
    public List<Doctor> obtenerDoctores() {
    	return obtenerDoctoresActivos();
    }
    
    /**
     * Obtiene doctores por especialidad (según diagrama de robustez)
     */
    @Override
    public List<Doctor> obtenerPorEspecialidad(String nombreEspecialidad) {
        EntityManager em = getEntityManager();
        
        try {
            TypedQuery<Doctor> query = em.createQuery(
                "SELECT d FROM Doctor d WHERE d.especialidad.nombre = :nombre AND d.activo = true ORDER BY d.apellido",
                Doctor.class
            );
            query.setParameter("nombre", nombreEspecialidad);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener doctores por especialidad: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtiene doctores por ID de especialidad
     */
    public List<Doctor> obtenerPorEspecialidad(int idEspecialidad) {
        EntityManager em = getEntityManager();
        
        try {
            TypedQuery<Doctor> query = em.createQuery(
                "SELECT d FROM Doctor d WHERE d.especialidad.idEspecialidad = :idEspecialidad AND d.activo = true ORDER BY d.apellido",
                Doctor.class
            );
            query.setParameter("idEspecialidad", idEspecialidad);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener doctores por ID de especialidad: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtiene un doctor por ID
     * @deprecated Usar getById(Integer) del GenericDAO
     */
    @Deprecated
    public Doctor obtenerPorId(int idDoctor) {
        return getById(idDoctor);
    }
    
    /**
     * Obtiene un doctor por cédula
     */
    public Doctor obtenerPorCedula(String cedula) {
        EntityManager em = getEntityManager();
        
        try {
            TypedQuery<Doctor> query = em.createQuery(
                "SELECT d FROM Doctor d WHERE d.cedula = :cedula",
                Doctor.class
            );
            query.setParameter("cedula", cedula);
            List<Doctor> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } catch (Exception e) {
            System.err.println("Error al obtener doctor por cédula: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
    
    /**
     * Guarda un nuevo doctor
     * @deprecated Usar create(Doctor) del GenericDAO
     */
    @Deprecated
    public void guardar(Doctor doctor) {
        create(doctor);
        System.out.println("Doctor guardado exitosamente: " + doctor.getNombreCompleto());
    }
    
    /**
     * Actualiza un doctor existente
     * @deprecated Usar update(Doctor) del GenericDAO
     */
    @Deprecated
    public void actualizar(Doctor doctor) {
        update(doctor);
        System.out.println("Doctor actualizado exitosamente: " + doctor.getNombreCompleto());
    }
    
    /**
     * Elimina (desactiva) un doctor
     * @deprecated Usar delete(Integer) del GenericDAO o el método específico eliminarLogico
     */
    @Deprecated
    public void eliminar(int idDoctor) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            Doctor doctor = em.find(Doctor.class, idDoctor);
            if (doctor != null) {
                doctor.setActivo(false);
                em.merge(doctor);
                System.out.println("Doctor desactivado exitosamente: " + doctor.getNombreCompleto());
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error al eliminar doctor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    /**
     * Cuenta doctores por especialidad
     */
    public long contarPorEspecialidad(String nombreEspecialidad) {
        EntityManager em = getEntityManager();
        
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(d) FROM Doctor d WHERE d.especialidad.nombre = :nombre AND d.activo = true",
                Long.class
            );
            query.setParameter("nombre", nombreEspecialidad);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error al contar doctores: " + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            em.close();
        }
    }
}
