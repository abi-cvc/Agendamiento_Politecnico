package model.dao;

import model.entity.Disponibilidad;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Interfaz específica para operaciones de Disponibilidad
 * Extiende GenericDAO y agrega métodos específicos del negocio
 */
public interface IDisponibilidadDAO extends GenericDAO<Disponibilidad, Integer> {
    
    /**
     * 8: obtenerHorariosDisponiblesPorDoctor(idDoctor): horarios[] - Según diagrama
     * Obtiene todos los horarios disponibles de un doctor
     */
    default List<Disponibilidad> obtenerHorariosDisponiblesPorDoctor(int idDoctor) {
        return obtenerPorDoctor(idDoctor);
    }
    
    /**
     * Obtiene todos los horarios (disponibles y no disponibles) de un doctor
     */
    List<Disponibilidad> obtenerPorDoctor(int idDoctor);
    
    /**
     * Verifica si un doctor está disponible en una fecha y hora
     */
    boolean verificarDisponibilidad(int idDoctor, LocalDate fecha, LocalTime hora);
    
    /**
     * Obtiene disponibilidades por doctor y fecha
     */
    List<Disponibilidad> obtenerPorDoctorYFecha(int idDoctor, LocalDate fecha);
    
    /**
     * Obtiene una disponibilidad específica por doctor, fecha y hora
     */
    Disponibilidad obtenerPorDoctorYFechaYHora(int idDoctor, LocalDate fecha, LocalTime hora);
}
