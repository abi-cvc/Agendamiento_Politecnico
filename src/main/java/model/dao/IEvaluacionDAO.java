package model.dao;

import model.entity.Evaluacion;
import model.entity.Doctor;
import model.entity.Estudiante;
import java.util.List;
import java.util.Map;

/**
 * Interfaz específica para operaciones de Evaluacion
 * Extiende GenericDAO y agrega métodos específicos del negocio
 */
public interface IEvaluacionDAO extends GenericDAO<Evaluacion, Integer> {
    
    /**
     * Obtiene todas las evaluaciones de un doctor específico
     * @param idDoctor ID del doctor
     * @return Lista de evaluaciones
     */
    List<Evaluacion> obtenerPorDoctor(int idDoctor);
    
    /**
     * Obtiene todas las evaluaciones de un doctor específico
     * @param doctor Objeto Doctor
     * @return Lista de evaluaciones
     */
    List<Evaluacion> obtenerPorDoctor(Doctor doctor);
    
    /**
     * Obtiene todas las evaluaciones realizadas por un estudiante
     * @param idEstudiante ID del estudiante
     * @return Lista de evaluaciones
     */
    List<Evaluacion> obtenerPorEstudiante(int idEstudiante);
    
    /**
     * Obtiene todas las evaluaciones realizadas por un estudiante
     * @param estudiante Objeto Estudiante
     * @return Lista de evaluaciones
     */
    List<Evaluacion> obtenerPorEstudiante(Estudiante estudiante);
    
    /**
     * Obtiene evaluaciones por calificación
     * @param calificacion Calificación (1-5)
     * @return Lista de evaluaciones
     */
    List<Evaluacion> obtenerPorCalificacion(int calificacion);
    
    /**
     * Calcula el promedio de calificaciones de un doctor
     * @param idDoctor ID del doctor
     * @return Promedio de calificaciones
     */
    Double calcularPromedioDoctor(int idDoctor);
    
    /**
     * Cuenta el total de evaluaciones de un doctor
     * @param idDoctor ID del doctor
     * @return Número de evaluaciones
     */
    long contarEvaluacionesDoctor(int idDoctor);
    
    /**
     * Obtiene estadísticas completas de un doctor
     * @param idDoctor ID del doctor
     * @return Map con estadísticas (promedio, total, por estrella)
     */
    Map<String, Object> obtenerEstadisticasDoctor(int idDoctor);
    
    /**
     * Obtiene las últimas N evaluaciones
     * @param limite Número de evaluaciones a obtener
     * @return Lista de evaluaciones recientes
     */
    List<Evaluacion> obtenerRecientes(int limite);
    
    /**
     * Verifica si un estudiante ya evaluó una cita específica
     * @param idCita ID de la cita
     * @return true si ya existe evaluación
     */
    boolean existeEvaluacionParaCita(int idCita);
    
    /**
     * Obtiene doctores mejor calificados
     * @param limite Número de doctores a retornar
     * @return Lista de doctores ordenados por calificación
     */
    List<Map<String, Object>> obtenerDoctoresMejorCalificados(int limite);
}