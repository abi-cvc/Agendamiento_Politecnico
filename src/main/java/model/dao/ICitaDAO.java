package model.dao;

import model.entity.Cita;
import model.entity.Especialidad;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Interfaz específica para operaciones de Cita
 * Extiende GenericDAO y agrega métodos específicos del negocio
 */
public interface ICitaDAO extends GenericDAO<Cita, Integer> {
    
    /**
     * Obtiene citas por fecha
     */
    List<Cita> obtenerPorFecha(LocalDate fecha);
    
    /**
     * Obtiene citas por especialidad
     */
    List<Cita> obtenerPorEspecialidad(Especialidad especialidad);
    
    /**
     * Obtiene citas por doctor
     */
    List<Cita> obtenerPorDoctor(int idDoctor);
    
    /**
     * Obtiene citas de un doctor en un mes específico
     */
    List<Cita> obtenerPorDoctorYMes(int idDoctor, YearMonth mes);
}
