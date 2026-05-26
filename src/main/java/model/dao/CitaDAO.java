package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.entity.Cita;
import model.entity.Especialidad;
import util.JPAUtil;

import java.time.LocalDate;
import java.util.List;

/**
 * DAO para la entidad Cita
 * Extiende JPAGenericDAO e implementa ICitaDAO según el patrón del diagrama de arquitectura
 */
public class CitaDAO extends JPAGenericDAO<Cita, Integer> implements ICitaDAO {
	
	public CitaDAO() {
		super(Cita.class);
	}
	
	/**
	 * Guarda una nueva cita en la base de datos usando ORM
	 * @param cita Objeto Cita a guardar
	 * @deprecated Usar create(Cita) del GenericDAO
	 */
	@Deprecated
	public void guardar(Cita cita) {
		create(cita);
    }
	
	/**
	 * Obtiene todas las citas de la base de datos
	 * @return Lista de citas con sus especialidades (ORM carga la relación)
	 */
	public List<Cita> obtenerTodas() {
		EntityManager em = getEntityManager();
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
	 * @deprecated Usar getById(Integer) del GenericDAO
	 */
	@Deprecated
	public Cita obtenerPorId(int id) {
		return getById(id);
	}
	
	// ===== IMPLEMENTACIÓN DE ICitaDAO =====
	
	@Override
	public List<Cita> obtenerPorFecha(LocalDate fecha) {
		EntityManager em = getEntityManager();
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
	
	@Override
	public List<Cita> obtenerPorEspecialidad(Especialidad especialidad) {
		EntityManager em = getEntityManager();
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
	
	@Override
	public List<Cita> obtenerPorDoctor(int idDoctor) {
		EntityManager em = getEntityManager();
		try {
			TypedQuery<Cita> query = em.createQuery(
				"SELECT c FROM Cita c WHERE c.doctor.idDoctor = :idDoctor ORDER BY c.fechaCita DESC, c.horaCita DESC", 
				Cita.class
			);
			query.setParameter("idDoctor", idDoctor);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	@Override
	public List<Cita> obtenerPorDoctorYMes(int idDoctor, java.time.YearMonth mes) {
		EntityManager em = getEntityManager();
		try {
			LocalDate primerDia = mes.atDay(1);
			LocalDate ultimoDia = mes.atEndOfMonth();
			
			TypedQuery<Cita> query = em.createQuery(
				"SELECT c FROM Cita c WHERE c.doctor.idDoctor = :idDoctor AND c.fechaCita BETWEEN :inicio AND :fin ORDER BY c.fechaCita, c.horaCita", 
				Cita.class
			);
			query.setParameter("idDoctor", idDoctor);
			query.setParameter("inicio", primerDia);
			query.setParameter("fin", ultimoDia);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	// ===== MÉTODOS ESPECÍFICOS DEL NEGOCIO =====
	
	/**
	 * Obtiene citas por especialidad usando la relación ORM
	 * @param especialidad Objeto Especialidad
	 * @return Lista de citas de esa especialidad
	 * @deprecated Usar obtenerPorEspecialidad(Especialidad) de ICitaDAO
	 */
	@Deprecated
	public List<Cita> obtenerPorEspecialidad_old(Especialidad especialidad) {
		return obtenerPorEspecialidad(especialidad);
	}
	
	/**
	 * Obtiene citas por fecha
	 * @param fecha Fecha a buscar
	 * @return Lista de citas de esa fecha
	 * @deprecated Usar obtenerPorFecha(LocalDate) de ICitaDAO
	 */
	@Deprecated
	public List<Cita> obtenerPorFecha_old(LocalDate fecha) {
		return obtenerPorFecha(fecha);
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
	 * Obtiene citas por estudiante (según diagrama de robustez)
	 * 2: obtenerCitasPorEstudiante(idEstudiante): citasAgendadas[]
	 * @param idEstudiante ID del estudiante
	 * @return Lista de citas del estudiante ordenadas por fecha
	 */
	public List<Cita> obtenerPorEstudiante(int idEstudiante) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<Cita> query = em.createQuery(
				"SELECT c FROM Cita c WHERE c.estudiante.idEstudiante = :idEstudiante ORDER BY c.fechaCita DESC, c.horaCita DESC", 
				Cita.class
			);
			query.setParameter("idEstudiante", idEstudiante);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	/**
	 * Obtiene citas por doctor y fecha específica
	 * 5: obtenerCitasDoctorDia(fechaActual): citasDia[]
	 * @param idDoctor ID del doctor
	 * @param fecha Fecha específica
	 * @return Lista de citas del doctor en esa fecha
	 */
	public List<Cita> obtenerPorDoctorYFecha(int idDoctor, LocalDate fecha) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<Cita> query = em.createQuery(
				"SELECT c FROM Cita c WHERE c.doctor.idDoctor = :idDoctor AND c.fechaCita = :fecha ORDER BY c.horaCita", 
				Cita.class
			);
			query.setParameter("idDoctor", idDoctor);
			query.setParameter("fecha", fecha);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	/**
	 * Obtiene todas las citas de un mes específico
	 * @param mesActual Mes a consultar (YearMonth)
	 * @return Lista de citas del mes
	 */
	public List<Cita> obtenerPorMes(java.time.YearMonth mesActual) {
		EntityManager em = getEntityManager();
		try {
			LocalDate primerDia = mesActual.atDay(1);
			LocalDate ultimoDia = mesActual.atEndOfMonth();
			
			TypedQuery<Cita> query = em.createQuery(
				"SELECT c FROM Cita c WHERE c.fechaCita BETWEEN :inicio AND :fin ORDER BY c.fechaCita, c.horaCita", 
				Cita.class
			);
			query.setParameter("inicio", primerDia);
			query.setParameter("fin", ultimoDia);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
	
	/**
	 * Actualiza una cita existente usando ORM
	 * @param cita Cita a actualizar
	 * @deprecated Usar update(Cita) del GenericDAO
	 */
	@Deprecated
	public void actualizar(Cita cita) {
		update(cita);
	}
	
	/**
	 * Elimina una cita por su ID
	 * @param id ID de la cita a eliminar
	 * @deprecated Usar delete(Integer) del GenericDAO
	 */
	@Deprecated
	public void eliminar(int id) {
		delete(id);
	}
}