package model.dao;

import model.entity.Especialidad;
import java.util.List;

/**
 * Interfaz específica para operaciones de Especialidad
 * Extiende GenericDAO y agrega métodos específicos del negocio
 */
public interface IEspecialidadDAO extends GenericDAO<Especialidad, Integer> {
    
    /**
     * 2: obtener(): especialidades[] - Según diagrama de robustez
     * Obtiene todas las especialidades (alias de getAll())
     */
    default List<Especialidad> obtener() {
        return getAll();
    }
    
    /**
     * Obtiene una especialidad por su nombre
     */
    Especialidad obtenerPorNombre(String nombre);
    
    /**
     * Obtiene todas las especialidades activas
     */
    List<Especialidad> obtenerEspecialidadesActivas();
    
    /**
     * Verifica si existen especialidades en la base de datos
     */
    boolean existenEspecialidades();
}
