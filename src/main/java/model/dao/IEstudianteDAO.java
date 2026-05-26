package model.dao;

import model.entity.Estudiante;
import model.entity.Cita;

import java.util.List;

/**
 * Interfaz específica para operaciones sobre Estudiante
 * Extiende GenericDAO con métodos del dominio
 */
public interface IEstudianteDAO extends GenericDAO<Estudiante, Integer> {

    /**
     * Busca un estudiante por su ID de paciente (cédula)
     * @param idPaciente cédula o identificador del estudiante
     * @return Estudiante encontrado o null
     */
    Estudiante buscarPorIdPaciente(String idPaciente);

    /**
     * Busca un estudiante por su correo electrónico
     * @param correo correo del estudiante
     * @return Estudiante encontrado o null
     */
    Estudiante buscarPorCorreo(String correo);

    /**
     * Obtiene los estudiantes activos (si aplica)
     * @return lista de estudiantes activos
     */
    List<Estudiante> obtenerActivos();

    /**
     * Obtiene las citas asociadas a un estudiante
     * @param idEstudiante id interno del estudiante
     * @return lista de citas
     */
    List<Cita> obtenerCitasDeEstudiante(int idEstudiante);

    /**
     * Valida credenciales del estudiante (implementación concreta puede variar)
     * Nota: si la entidad Estudiante no contiene password en la BD, la
     * implementación debe adaptarse (por ejemplo, validar por correo temporalmente).
     * @param idPaciente id del estudiante (cédula)
     * @param password contraseña a validar
     * @return Estudiante si las credenciales son válidas, null en caso contrario
     */
    Estudiante validarCredenciales(String idPaciente, String password);

    /**
     * Cambia el estado activo de un estudiante (si aplica)
     * @param id id del estudiante
     * @param activo nuevo estado
     */
    void cambiarEstado(int id, boolean activo);

    /**
     * Verifica si existe un estudiante por su idPaciente
     * @param idPaciente idPaciente a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existePorIdPaciente(String idPaciente);

    /**
     * Verifica si existe un estudiante por correo
     * @param correo correo a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existePorCorreo(String correo);
}
