package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.entity.Evaluacion;
import model.entity.Doctor;
import model.entity.Estudiante;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO para la entidad Evaluacion
 * Extiende JPAGenericDAO e implementa IEvaluacionDAO
 */
public class EvaluacionDAO extends JPAGenericDAO<Evaluacion, Integer> implements IEvaluacionDAO {
    
    public EvaluacionDAO() {
        super(Evaluacion.class);
    }
    
    @Override
    public List<Evaluacion> obtenerPorDoctor(int idDoctor) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Evaluacion> query = em.createQuery(
                "SELECT e FROM Evaluacion e WHERE e.doctor.idDoctor = :idDoctor ORDER BY e.fechaEvaluacion DESC",
                Evaluacion.class
            );
            query.setParameter("idDoctor", idDoctor);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener evaluaciones por doctor: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Evaluacion> obtenerPorDoctor(Doctor doctor) {
        if (doctor == null) {
            return new ArrayList<>();
        }
        return obtenerPorDoctor(doctor.getIdDoctor());
    }
    
    @Override
    public List<Evaluacion> obtenerPorEstudiante(int idEstudiante) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Evaluacion> query = em.createQuery(
                "SELECT e FROM Evaluacion e WHERE e.estudiante.idEstudiante = :idEstudiante ORDER BY e.fechaEvaluacion DESC",
                Evaluacion.class
            );
            query.setParameter("idEstudiante", idEstudiante);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener evaluaciones por estudiante: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Evaluacion> obtenerPorEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            return new ArrayList<>();
        }
        return obtenerPorEstudiante(estudiante.getIdEstudiante());
    }
    
    @Override
    public List<Evaluacion> obtenerPorCalificacion(int calificacion) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Evaluacion> query = em.createQuery(
                "SELECT e FROM Evaluacion e WHERE e.calificacion = :calificacion ORDER BY e.fechaEvaluacion DESC",
                Evaluacion.class
            );
            query.setParameter("calificacion", calificacion);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener evaluaciones por calificación: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
    
    @Override
    public Double calcularPromedioDoctor(int idDoctor) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                "SELECT AVG(e.calificacion) FROM Evaluacion e WHERE e.doctor.idDoctor = :idDoctor",
                Double.class
            );
            query.setParameter("idDoctor", idDoctor);
            Double promedio = query.getSingleResult();
            return promedio != null ? promedio : 0.0;
        } catch (Exception e) {
            System.err.println("Error al calcular promedio: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        } finally {
            em.close();
        }
    }
    
    @Override
    public long contarEvaluacionesDoctor(int idDoctor) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Evaluacion e WHERE e.doctor.idDoctor = :idDoctor",
                Long.class
            );
            query.setParameter("idDoctor", idDoctor);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error al contar evaluaciones: " + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            em.close();
        }
    }
    
    @Override
    public Map<String, Object> obtenerEstadisticasDoctor(int idDoctor) {
        EntityManager em = getEntityManager();
        Map<String, Object> estadisticas = new HashMap<>();
        
        try {
            // Promedio general
            Double promedio = calcularPromedioDoctor(idDoctor);
            estadisticas.put("promedio", promedio);
            
            // Total de evaluaciones
            long total = contarEvaluacionesDoctor(idDoctor);
            estadisticas.put("total", total);
            
            // Contar evaluaciones por estrella (1-5)
            for (int i = 1; i <= 5; i++) {
                TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(e) FROM Evaluacion e WHERE e.doctor.idDoctor = :idDoctor AND e.calificacion = :calificacion",
                    Long.class
                );
                query.setParameter("idDoctor", idDoctor);
                query.setParameter("calificacion", i);
                long count = query.getSingleResult();
                estadisticas.put("estrellas_" + i, count);
                
                // Calcular porcentaje
                double porcentaje = total > 0 ? (count * 100.0 / total) : 0;
                estadisticas.put("porcentaje_" + i, porcentaje);
            }
            
            return estadisticas;
        } catch (Exception e) {
            System.err.println("Error al obtener estadísticas: " + e.getMessage());
            e.printStackTrace();
            return estadisticas;
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Evaluacion> obtenerRecientes(int limite) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Evaluacion> query = em.createQuery(
                "SELECT e FROM Evaluacion e ORDER BY e.fechaEvaluacion DESC",
                Evaluacion.class
            );
            query.setMaxResults(limite);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener evaluaciones recientes: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
    
    @Override
    public boolean existeEvaluacionParaCita(int idCita) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Evaluacion e WHERE e.cita.idCita = :idCita",
                Long.class
            );
            query.setParameter("idCita", idCita);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            System.err.println("Error al verificar evaluación: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Map<String, Object>> obtenerDoctoresMejorCalificados(int limite) {
        EntityManager em = getEntityManager();
        List<Map<String, Object>> resultado = new ArrayList<>();
        
        try {
            // Query nativa para obtener doctores con su promedio
            String jpql = "SELECT e.doctor.idDoctor, e.doctor.nombre, e.doctor.apellido, " +
                         "AVG(e.calificacion) as promedio, COUNT(e) as total " +
                         "FROM Evaluacion e " +
                         "GROUP BY e.doctor.idDoctor, e.doctor.nombre, e.doctor.apellido " +
                         "HAVING COUNT(e) >= 1 " +
                         "ORDER BY promedio DESC";
            
            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            query.setMaxResults(limite);
            
            List<Object[]> resultados = query.getResultList();
            
            for (Object[] row : resultados) {
                Map<String, Object> doctor = new HashMap<>();
                doctor.put("idDoctor", row[0]);
                doctor.put("nombre", row[1]);
                doctor.put("apellido", row[2]);
                doctor.put("promedio", row[3]);
                doctor.put("totalEvaluaciones", row[4]);
                resultado.add(doctor);
            }
            
            return resultado;
        } catch (Exception e) {
            System.err.println("Error al obtener doctores mejor calificados: " + e.getMessage());
            e.printStackTrace();
            return resultado;
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtiene la evaluación de una cita específica
     */
    public Evaluacion obtenerPorCita(int idCita) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Evaluacion> query = em.createQuery(
                "SELECT e FROM Evaluacion e WHERE e.cita.idCita = :idCita",
                Evaluacion.class
            );
            query.setParameter("idCita", idCita);
            List<Evaluacion> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } catch (Exception e) {
            System.err.println("Error al obtener evaluación por cita: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
}