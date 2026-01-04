package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.entity.Cita;
import model.entity.Especialidad;
import util.JPAUtil;

import java.time.LocalDate;
import java.util.List;

public class CitaDAO {
	
	/**
	 * Guarda una nueva cita en la base de datos usando ORM
	 * @param cita Objeto Cita a guardar
	 */
	public void guardar(Cita cita) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cita);  // ← ORM: JPA genera el INSERT automáticamente
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
	 * Obtiene todas las citas de la base de datos
	 * @return Lista de citas con sus especialidades (ORM carga la relación)
	 */
	public List<Cita> obtenerTodas() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<Cita> query = em.createQuery(
				"SELECT c FROM Cita c ORDER BY c.fechaCita DESC, c.horaCita DESC", 
				Cita.class
			);
			return query.getResultList();  // ← ORM: JPA mapea automáticamente
		} finally {
			em.close();
		}
	}
	
	/**
	 * Obtiene una cita por su ID usando ORM
	 * @param id ID de la cita
	 * @return Cita encontrada o null
	 */
	public Cita obtenerPorId(int id) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			return em.find(Cita.class, id);  // ← ORM: find automático
		} finally {
			em.close();
		}
	}
	
	/**
	 * Obtiene citas por especialidad usando la relación ORM
	 * @param especialidad Objeto Especialidad
	 * @return Lista de citas de esa especialidad
	 */
	public List<Cita> obtenerPorEspecialidad(Especialidad especialidad) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<Cita> query = em.createQuery(
				"SELECT c FROM Cita c WHERE c.especialidad = :especialidad ORDER BY c.fechaCita", 
				Cita.class
			);
			query.setParameter("especialidad", especialidad);  // ← ORM: relación
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	/**
	 * Obtiene citas por fecha
	 * @param fecha Fecha a buscar
	 * @return Lista de citas de esa fecha
	 */
	public List<Cita> obtenerPorFecha(LocalDate fecha) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<Cita> query = em.createQuery(
				"SELECT c FROM Cita c WHERE c.fechaCita = :fecha ORDER BY c.horaCita", 
				Cita.class
			);
			query.setParameter("fecha", fecha);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	/**
	 * Obtiene citas por estado
	 * @param estado Estado de la cita (ej: "Agendada", "Completada", "Cancelada")
	 * @return Lista de citas con ese estado
	 */
	public List<Cita> obtenerPorEstado(String estado) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<Cita> query = em.createQuery(
				"SELECT c FROM Cita c WHERE c.estadoCita = :estado ORDER BY c.fechaCita", 
				Cita.class
			);
			query.setParameter("estado", estado);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	/**
	 * Actualiza una cita existente usando ORM
	 * @param cita Cita a actualizar
	 */
	public void actualizar(Cita cita) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(cita);  // ← ORM: JPA genera el UPDATE automáticamente
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
	 * Elimina una cita por su ID
	 * @param id ID de la cita a eliminar
	 */
	public void eliminar(int id) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Cita cita = em.find(Cita.class, id);
			if (cita != null) {
				em.remove(cita);  // ← ORM: JPA genera el DELETE automáticamente
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
