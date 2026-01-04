package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.entity.Especialidad;
import util.JPAUtil;

import java.util.List;

public class EspecialidadDAO {
	
	/**
	 * Obtiene todas las especialidades de la base de datos
	 * @return Lista de especialidades
	 */
	public List<Especialidad> obtenerEspecialidades() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<Especialidad> query = em.createQuery(
				"SELECT e FROM Especialidad e ORDER BY e.nombre", 
				Especialidad.class
			);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	/**
	 * Obtiene una especialidad por su nombre
	 * @param nombre Nombre de la especialidad
	 * @return Especialidad encontrada o null
	 */
	public Especialidad obtenerPorNombre(String nombre) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<Especialidad> query = em.createQuery(
				"SELECT e FROM Especialidad e WHERE e.nombre = :nombre", 
				Especialidad.class
			);
			query.setParameter("nombre", nombre);
			List<Especialidad> resultados = query.getResultList();
			return resultados.isEmpty() ? null : resultados.get(0);
		} finally {
			em.close();
		}
	}
	
	/**
	 * Obtiene una especialidad por su ID
	 * @param id ID de la especialidad
	 * @return Especialidad encontrada o null
	 */
	public Especialidad obtenerPorId(int id) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			return em.find(Especialidad.class, id);
		} finally {
			em.close();
		}
	}
	
	/**
	 * Guarda una nueva especialidad en la base de datos
	 * @param especialidad Especialidad a guardar
	 */
	public void guardar(Especialidad especialidad) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(especialidad);
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
	 * Actualiza una especialidad existente
	 * @param especialidad Especialidad a actualizar
	 */
	public void actualizar(Especialidad especialidad) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(especialidad);
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
	 * Elimina una especialidad por su ID
	 * @param id ID de la especialidad a eliminar
	 */
	public void eliminar(int id) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Especialidad especialidad = em.find(Especialidad.class, id);
			if (especialidad != null) {
				em.remove(especialidad);
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
	 * Verifica si existe al menos una especialidad en la base de datos
	 * @return true si hay especialidades, false en caso contrario
	 */
	public boolean existenEspecialidades() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			Long count = em.createQuery(
				"SELECT COUNT(e) FROM Especialidad e", 
				Long.class
			).getSingleResult();
			return count > 0;
		} finally {
			em.close();
		}
	}
}
