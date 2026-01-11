package model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Entidad Estudiante - Representa a un estudiante del Politécnico
 * Relación: OneToMany con Cita (un estudiante puede tener varias citas)
 */
@Entity
@Table(name = "estudiante")
public class Estudiante implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private int idEstudiante;
    
    @Column(name = "id_paciente", nullable = false, unique = true, length = 20)
    private String idPaciente; // Número de cédula o ID único del estudiante
    
    @Column(name = "nombre_estudiante", nullable = false, length = 100)
    private String nombreEstudiante;
    
    @Column(name = "apellido_estudiante", nullable = false, length = 100)
    private String apellidoEstudiante;
    
    @Column(name = "correo_estudiante", nullable = false, unique = true, length = 100)
    private String correoEstudiante;
    
    // Relación OneToMany con Cita
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cita> citas;
    
    // ===== CONSTRUCTORES =====
    public Estudiante() {
    }
    
    public Estudiante(String idPaciente, String nombreEstudiante, String apellidoEstudiante, String correoEstudiante) {
        this.idPaciente = idPaciente;
        this.nombreEstudiante = nombreEstudiante;
        this.apellidoEstudiante = apellidoEstudiante;
        this.correoEstudiante = correoEstudiante;
    }
    
    // ===== MÉTODOS DE NEGOCIO (según diagrama de clases) =====
    
    /**
     * Método iniciarSesion() - Valida las credenciales del estudiante
     * @return true si las credenciales son válidas
     */
    public boolean iniciarSesion() {
        // Validar que el estudiante tenga los datos necesarios
        return idPaciente != null && !idPaciente.trim().isEmpty() &&
               correoEstudiante != null && !correoEstudiante.trim().isEmpty();
    }
    
    /**
     * Método verDisponibilidad() - Verifica disponibilidad de citas
     * @return true si hay disponibilidad
     */
    public boolean verDisponibilidad() {
        // Lógica para verificar disponibilidad (implementada en el DAO)
        return true;
    }
    
    /**
     * Método agendarCita() - Agenda una nueva cita
     * @param cita La cita a agendar
     * @return true si la cita fue agendada exitosamente
     */
    public boolean agendarCita(Cita cita) {
        if (cita == null || !cita.crear()) {
            return false;
        }
        cita.setEstudiante(this);
        return true;
    }
    
    /**
     * Método cancelarCita() - Cancela una cita existente
     * @param cita La cita a cancelar
     * @return true si la cita fue cancelada exitosamente
     */
    public boolean cancelarCita(Cita cita) {
        if (cita == null || cita.getEstudiante() == null) {
            return false;
        }
        
        // Verificar que la cita pertenece a este estudiante
        if (cita.getEstudiante().getIdEstudiante() != this.idEstudiante) {
            return false;
        }
        
        return cita.cancelar();
    }
    
    /**
     * Método consultarMisCitas() - Obtiene todas las citas del estudiante
     * @return Lista de citas del estudiante
     */
    public List<Cita> consultarMisCitas() {
        return this.citas;
    }
    
    /**
     * Método cerrarSesion() - Cierra la sesión del estudiante
     */
    public void cerrarSesion() {
        // Lógica para cerrar sesión (limpieza de datos de sesión)
    }
    
    // ===== GETTERS Y SETTERS =====
    
    public int getIdEstudiante() {
        return idEstudiante;
    }
    
    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }
    
    public String getIdPaciente() {
        return idPaciente;
    }
    
    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }
    
    public String getNombreEstudiante() {
        return nombreEstudiante;
    }
    
    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }
    
    public String getApellidoEstudiante() {
        return apellidoEstudiante;
    }
    
    public void setApellidoEstudiante(String apellidoEstudiante) {
        this.apellidoEstudiante = apellidoEstudiante;
    }
    
    public String getCorreoEstudiante() {
        return correoEstudiante;
    }
    
    public void setCorreoEstudiante(String correoEstudiante) {
        this.correoEstudiante = correoEstudiante;
    }
    
    public List<Cita> getCitas() {
        return citas;
    }
    
    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }
    
    // Método auxiliar para obtener nombre completo
    public String getNombreCompleto() {
        return nombreEstudiante + " " + apellidoEstudiante;
    }
    
    @Override
    public String toString() {
        return "Estudiante{" +
                "idEstudiante=" + idEstudiante +
                ", idPaciente='" + idPaciente + '\'' +
                ", nombreEstudiante='" + nombreEstudiante + '\'' +
                ", apellidoEstudiante='" + apellidoEstudiante + '\'' +
                ", correoEstudiante='" + correoEstudiante + '\'' +
                '}';
    }
}