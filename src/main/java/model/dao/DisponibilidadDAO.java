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
 * Maneja operaciones de calendario según diagrama de robustez
 */
public class DisponibilidadDAO {
    
    /**
     * Obtiene todas las disponibilidades
     */
    public List<Disponibilidad> obtenerTodas() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Disponibilidad> disponibilidades = new ArrayList<>();
        
        try {
            TypedQuery<Disponibilidad> query = em.createQuery(
                "SELECT d FROM Disponibilidad d ORDER BY d.fecha, d.horaInicio",
                Disponibilidad.class
            );
            disponibilidades = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener disponibilidades: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return disponibilidades;
    }
    
    /**
     * Obtiene disponibilidades por doctor (según diagrama de robustez)
     */
    public List<Disponibilidad> obtenerPorDoctor(int idDoctor) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Disponibilidad> disponibilidades = new ArrayList<>();
        
        try {
            TypedQuery<Disponibilidad> query = em.createQuery(
                "SELECT d FROM Disponibilidad d WHERE d.doctor.idDoctor = :idDoctor " +
                "AND d.disponible = true AND d.fecha >= :hoy ORDER BY d.fecha, d.horaInicio",
                Disponibilidad.class
            );
            query.setParameter("idDoctor", idDoctor);
            query.setParameter("hoy", LocalDate.now());
            disponibilidades = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener disponibilidades por doctor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return disponibilidades;
    }
    
    /**
     * Obtiene disponibilidades por doctor y fecha
     */
    public List<Disponibilidad> obtenerPorDoctorYFecha(int idDoctor, LocalDate fecha) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Disponibilidad> disponibilidades = new ArrayList<>();
        
        try {
            TypedQuery<Disponibilidad> query = em.createQuery(
                "SELECT d FROM Disponibilidad d WHERE d.doctor.idDoctor = :idDoctor " +
                "AND d.fecha = :fecha AND d.disponible = true ORDER BY d.horaInicio",
                Disponibilidad.class
            );
            query.setParameter("idDoctor", idDoctor);
            query.setParameter("fecha", fecha);
            disponibilidades = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener disponibilidades por doctor y fecha: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return disponibilidades;
    }
    
    /**
     * Obtiene fechas disponibles de un doctor (para el calendario)
     */
    public List<LocalDate> obtenerFechasDisponibles(int idDoctor) {
        EntityManager em = JPAUtil.getEntityManager();
        List<LocalDate> fechas = new ArrayList<>();
        
        try {
            TypedQuery<LocalDate> query = em.createQuery(
                "SELECT DISTINCT d.fecha FROM Disponibilidad d " +
                "WHERE d.doctor.idDoctor = :idDoctor AND d.disponible = true " +
                "AND d.fecha >= :hoy ORDER BY d.fecha",
                LocalDate.class
            );
            query.setParameter("idDoctor", idDoctor);
            query.setParameter("hoy", LocalDate.now());
            fechas = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener fechas disponibles: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return fechas;
    }
    
    /**
     * Verifica si un horario está disponible
     */
    public boolean verificarDisponibilidad(int idDoctor, LocalDate fecha, LocalTime hora) {
        EntityManager em = JPAUtil.getEntityManager();
        boolean disponible = false;
        
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(d) FROM Disponibilidad d WHERE d.doctor.idDoctor = :idDoctor " +
                "AND d.fecha = :fecha AND d.horaInicio <= :hora AND d.horaFin > :hora " +
                "AND d.disponible = true",
                Long.class
            );
            query.setParameter("idDoctor", idDoctor);
            query.setParameter("fecha", fecha);
            query.setParameter("hora", hora);
            disponible = query.getSingleResult() > 0;
        } catch (Exception e) {
            System.err.println("Error al verificar disponibilidad: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        return disponible;
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
     */
    public void guardar(Disponibilidad disponibilidad) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            em.persist(disponibilidad);
            tx.commit();
            System.out.println("Disponibilidad guardada exitosamente");
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error al guardar disponibilidad: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    /**
     * Actualiza una disponibilidad existente
     */
    public void actualizar(Disponibilidad disponibilidad) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            em.merge(disponibilidad);
            tx.commit();
            System.out.println("Disponibilidad actualizada exitosamente");
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error al actualizar disponibilidad: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtiene una disponibilidad por doctor, fecha y hora específica
     * 5: liberarHorario(idHorario)
     * @param idDoctor ID del doctor
     * @param fecha Fecha de la cita
     * @param hora Hora de la cita
     * @return Disponibilidad encontrada o null
     */
    public Disponibilidad obtenerPorDoctorYFechaYHora(int idDoctor, LocalDate fecha, LocalTime hora) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            TypedQuery<Disponibilidad> query = em.createQuery(
                "SELECT d FROM Disponibilidad d WHERE d.doctor.idDoctor = :idDoctor " +
                "AND d.fecha = :fecha " +
                "AND d.horaInicio <= :hora " +
                "AND d.horaFin > :hora",
                Disponibilidad.class
            );
            query.setParameter("idDoctor", idDoctor);
            query.setParameter("fecha", fecha);
            query.setParameter("hora", hora);
            
            List<Disponibilidad> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } catch (Exception e) {
            System.err.println("Error al obtener disponibilidad: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }
    
    /**
     * Marca una disponibilidad como no disponible (cuando se agenda una cita)
     */
    public void marcarNoDisponible(int idDisponibilidad) {
        EntityManager em = JPAUtil.getEntityManager();
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