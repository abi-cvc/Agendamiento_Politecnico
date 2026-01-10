package model.dao;

import model.entity.Doctor;
import model.entity.Especialidad;
import java.util.List;

/**
 * Interfaz específica para operaciones de Doctor
 * Extiende GenericDAO y agrega métodos específicos del negocio
 */
public interface IDoctorDAO extends GenericDAO<Doctor, Integer> {
    
    /**
     * Obtiene doctores por especialidad
     */
    List<Doctor> obtenerPorEspecialidad(Especialidad especialidad);
    
    /**
     * Obtiene doctores por nombre de especialidad
     */
    List<Doctor> obtenerPorEspecialidad(String nombreEspecialidad);
    
    /**
     * Obtiene doctores activos
     */
    List<Doctor> obtenerDoctoresActivos();
}
