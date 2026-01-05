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
 * Maneja todas las operaciones CRUD con JPA/ORM
 */
public class DoctorDAO {
    
    /**
     * Obtiene todos los doctores activos
     */
    public List<Doctor> obtenerDoctores() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Doctor> doctores = new ArrayList<>();
        
        try {
            TypedQuery<Doctor> query = em.createQuery(
                "SELECT d FROM Doctor d WHERE d.activo = true ORDER BY d.apellido", 
                Doctor.class
            );
            doctores = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener doctores: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return doctores;
    }
    
    /**
     * Obtiene doctores por especialidad (según diagrama de robustez)
     */
    public List<Doctor> obtenerPorEspecialidad(String nombreEspecialidad) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Doctor> doctores = new ArrayList<>();
        
        try {
            TypedQuery<Doctor> query = em.createQuery(
                "SELECT d FROM Doctor d WHERE d.especialidad.nombre = :nombre AND d.activo = true ORDER BY d.apellido",
                Doctor.class
            );
            query.setParameter("nombre", nombreEspecialidad);
            doctores = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener doctores por especialidad: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return doctores;
    }
    
    /**
     * Obtiene doctores por objeto Especialidad
     */
    public List<Doctor> obtenerPorEspecialidad(Especialidad especialidad) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Doctor> doctores = new ArrayList<>();
        
        try {
            TypedQuery<Doctor> query = em.createQuery(
                "SELECT d FROM Doctor d WHERE d.especialidad = :especialidad AND d.activo = true ORDER BY d.apellido",
                Doctor.class
            );
            query.setParameter("especialidad", especialidad);
            doctores = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener doctores por especialidad: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return doctores;
    }
    
    /**
     * Obtiene doctores por ID de especialidad
     */
    public List<Doctor> obtenerPorEspecialidad(int idEspecialidad) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Doctor> doctores = new ArrayList<>();
        
        try {
            TypedQuery<Doctor> query = em.createQuery(
                "SELECT d FROM Doctor d WHERE d.especialidad.idEspecialidad = :idEspecialidad AND d.activo = true ORDER BY d.apellido",
                Doctor.class
            );
            query.setParameter("idEspecialidad", idEspecialidad);
            doctores = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener doctores por ID de especialidad: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return doctores;
    }
    
    /**
     * Obtiene un doctor por ID
     */
    public Doctor obtenerPorId(int idDoctor) {
        EntityManager em = JPAUtil.getEntityManager();
        Doctor doctor = null;
        
        try {
            doctor = em.find(Doctor.class, idDoctor);
        } catch (Exception e) {
            System.err.println("Error al obtener doctor por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return doctor;
    }
    
    /**
     * Obtiene un doctor por cédula
     */
    public Doctor obtenerPorCedula(String cedula) {
        EntityManager em = JPAUtil.getEntityManager();
        Doctor doctor = null;
        
        try {
            TypedQuery<Doctor> query = em.createQuery(
                "SELECT d FROM Doctor d WHERE d.cedula = :cedula",
                Doctor.class
            );
            query.setParameter("cedula", cedula);
            List<Doctor> resultados = query.getResultList();
            if (!resultados.isEmpty()) {
                doctor = resultados.get(0);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener doctor por cédula: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return doctor;
    }
    
    /**
     * Guarda un nuevo doctor
     */
    public void guardar(Doctor doctor) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            em.persist(doctor);
            tx.commit();
            System.out.println("Doctor guardado exitosamente: " + doctor.getNombreCompleto());
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error al guardar doctor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    /**
     * Actualiza un doctor existente
     */
    public void actualizar(Doctor doctor) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            em.merge(doctor);
            tx.commit();
            System.out.println("Doctor actualizado exitosamente: " + doctor.getNombreCompleto());
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error al actualizar doctor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    /**
     * Elimina (desactiva) un doctor
     */
    public void eliminar(int idDoctor) {
        EntityManager em = JPAUtil.getEntityManager();
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
        EntityManager em = JPAUtil.getEntityManager();
        long count = 0;
        
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(d) FROM Doctor d WHERE d.especialidad.nombre = :nombre AND d.activo = true",
                Long.class
            );
            query.setParameter("nombre", nombreEspecialidad);
            count = query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error al contar doctores: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return count;
    }
}
