package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.entity.Especialidad;
import util.JPAUtil;

import java.util.List;

/**
 * DAO para la entidad Especialidad
 * Extiende JPAGenericDAO e implementa IEspecialidadDAO según el patrón del diagrama de arquitectura
 */
public class EspecialidadDAO extends JPAGenericDAO<Especialidad, Integer> implements IEspecialidadDAO {
	
	public EspecialidadDAO() {
		super(Especialidad.class);
	}
	
	// ===== IMPLEMENTACIÓN DE IEspecialidadDAO =====
	
	@Override
	public Especialidad obtenerPorNombre(String nombre) {
		EntityManager em = getEntityManager();
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
	
	@Override
	public List<Especialidad> obtenerEspecialidadesActivas() {
		// Si tienes un campo 'activo' en la entidad, usarlo
		// Por ahora retorna todas
		return getAll();
	}
	
	@Override
	public boolean existenEspecialidades() {
		EntityManager em = getEntityManager();
		try {
			TypedQuery<Long> query = em.createQuery(
				"SELECT COUNT(e) FROM Especialidad e", 
				Long.class
			);
			Long count = query.getSingleResult();
			return count > 0;
		} finally {
			em.close();
		}
	}
	
	// ===== MÉTODOS ESPECÍFICOS ADICIONALES =====
	
	/**
	 * Obtiene todas las especialidades de la base de datos
	 * @return Lista de especialidades
	 * @deprecated Usar getAll() del GenericDAO
	 */
	@Deprecated
	public List<Especialidad> obtenerEspecialidades() {
		return getAll();
	}
	
	/**
	 * Obtiene una especialidad por su ID
	 * @param id ID de la especialidad
	 * @return Especialidad encontrada o null
	 * @deprecated Usar getById(Integer) del GenericDAO
	 */
	@Deprecated
	public Especialidad obtenerPorId(int id) {
		return getById(id);
	}
	
	/**
	 * Guarda una nueva especialidad en la base de datos
	 * @param especialidad Especialidad a guardar
	 * @deprecated Usar create(Especialidad) del GenericDAO
	 */
	@Deprecated
	public void guardar(Especialidad especialidad) {
		create(especialidad);
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
}
