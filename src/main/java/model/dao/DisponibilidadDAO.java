package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.entity.Disponibilidad;
import model.entity.Doctor;
import util.JPAUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Disponibilidad
 * Extiende JPAGenericDAO e implementa IDisponibilidadDAO según el patrón del diagrama de arquitectura
 */
public class DisponibilidadDAO extends JPAGenericDAO<Disponibilidad, Integer> implements IDisponibilidadDAO {
    
	public DisponibilidadDAO() {
		super(Disponibilidad.class);
	}
	
	// ===== IMPLEMENTACIÓN DE IDisponibilidadDAO =====
	
	@Override
	public List<Disponibilidad> obtenerPorDoctor(int idDoctor) {
		EntityManager em = getEntityManager();
        try {
            TypedQuery<Disponibilidad> query = em.createQuery(
                "SELECT d FROM Disponibilidad d WHERE d.doctor.idDoctor = :idDoctor " +
                "ORDER BY d.fecha, d.horaInicio",
                Disponibilidad.class
            );
            query.setParameter("idDoctor", idDoctor);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener disponibilidades por doctor: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
	}
	
	@Override
	public boolean verificarDisponibilidad(int idDoctor, LocalDate fecha, LocalTime hora) {
		EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(d) FROM Disponibilidad d WHERE d.doctor.idDoctor = :idDoctor " +
                "AND d.fecha = :fecha AND d.horaInicio = :hora AND d.disponible = true",
                Long.class
            );
            query.setParameter("idDoctor", idDoctor);
            query.setParameter("fecha", fecha);
            query.setParameter("hora", hora);
            
            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            System.err.println("Error al verificar disponibilidad: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
	}
	
	@Override
	public List<Disponibilidad> obtenerPorDoctorYFecha(int idDoctor, LocalDate fecha) {
		EntityManager em = getEntityManager();
        try {
            TypedQuery<Disponibilidad> query = em.createQuery(
                "SELECT d FROM Disponibilidad d WHERE d.doctor.idDoctor = :idDoctor " +
                "AND d.fecha = :fecha AND d.disponible = true ORDER BY d.horaInicio",
                Disponibilidad.class
            );
            query.setParameter("idDoctor", idDoctor);
            query.setParameter("fecha", fecha);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener disponibilidades por doctor y fecha: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
	}
	
	@Override
	public Disponibilidad obtenerPorDoctorYFechaYHora(int idDoctor, LocalDate fecha, LocalTime hora) {
		EntityManager em = getEntityManager();
        try {
            TypedQuery<Disponibilidad> query = em.createQuery(
                "SELECT d FROM Disponibilidad d WHERE d.doctor.idDoctor = :idDoctor " +
                "AND d.fecha = :fecha AND d.horaInicio = :hora",
                Disponibilidad.class
            );
            query.setParameter("idDoctor", idDoctor);
            query.setParameter("fecha", fecha);
            query.setParameter("hora", hora);
            
            List<Disponibilidad> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } catch (Exception e) {
            System.err.println("Error al obtener disponibilidad específica: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
	}
	
	// ===== MÉTODOS ESPECÍFICOS ADICIONALES =====
	
    /**
     * Obtiene todas las disponibilidades
     * @deprecated Usar getAll() del GenericDAO
     */
	@Deprecated
    public List<Disponibilidad> obtenerTodas() {
    	return getAll();
    }
    
    /**
     * Obtiene fechas disponibles de un doctor (para el calendario)
     */
    public List<LocalDate> obtenerFechasDisponibles(int idDoctor) {
        EntityManager em = getEntityManager();
        
        try {
            TypedQuery<LocalDate> query = em.createQuery(
                "SELECT DISTINCT d.fecha FROM Disponibilidad d " +
                "WHERE d.doctor.idDoctor = :idDoctor AND d.disponible = true " +
                "AND d.fecha >= :hoy ORDER BY d.fecha",
                LocalDate.class
            );
            query.setParameter("idDoctor", idDoctor);
            query.setParameter("hoy", LocalDate.now());
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener fechas disponibles: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtiene una disponibilidad por ID
     */
    public Disponibilidad obtenerPorId(int idDisponibilidad) {
        EntityManager em = JPAUtil.getEntityManager();
        Disponibilidad disponibilidad = null;
        
        try {
            disponibilidad = em.find(Disponibilidad.class, idDisponibilidad);
        } catch (Exception e) {
            System.err.println("Error al obtener disponibilidad por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return disponibilidad;
    }
    
    /**
     * Guarda una nueva disponibilidad
     * @deprecated Usar create(Disponibilidad) del GenericDAO
     */
    @Deprecated
    public void guardar(Disponibilidad disponibilidad) {
        create(disponibilidad);
        System.out.println("Disponibilidad guardada exitosamente");
    }
    
    /**
     * Actualiza una disponibilidad existente
     * @deprecated Usar update(Disponibilidad) del GenericDAO
     */
    @Deprecated
    public void actualizar(Disponibilidad disponibilidad) {
        update(disponibilidad);
        System.out.println("Disponibilidad actualizada exitosamente");
    }
    
    /**
     * Marca una disponibilidad como no disponible (cuando se agenda una cita)
     */
    public void marcarNoDisponible(int idDisponibilidad) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            Disponibilidad disponibilidad = em.find(Disponibilidad.class, idDisponibilidad);
            if (disponibilidad != null) {
                disponibilidad.setDisponible(false);
                em.merge(disponibilidad);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error al marcar no disponible: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    /**
     * Elimina una disponibilidad
     */
    public void eliminar(int idDisponibilidad) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            Disponibilidad disponibilidad = em.find(Disponibilidad.class, idDisponibilidad);
            if (disponibilidad != null) {
                em.remove(disponibilidad);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error al eliminar disponibilidad: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}