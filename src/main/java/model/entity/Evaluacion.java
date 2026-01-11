package model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad Evaluacion - Representa las evaluaciones que los estudiantes hacen a los doctores
 * Relación: ManyToOne con Doctor
 * Relación: ManyToOne con Estudiante
 * Relación: OneToOne con Cita (opcional)
 */
@Entity
@Table(name = "evaluacion")
public class Evaluacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluacion")
    private int idEvaluacion;
    
    @Column(name = "calificacion", nullable = false)
    private int calificacion; // 1-5 estrellas
    
    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;
    
    @Column(name = "fecha_evaluacion", nullable = false, updatable = false)
    private LocalDateTime fechaEvaluacion;
    
    // Relación ManyToOne con Doctor
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_doctor", nullable = false)
    private Doctor doctor;
    
    // Relación ManyToOne con Estudiante
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;
    
    // Relación OneToOne con Cita (opcional)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita")
    private Cita cita;
    
    // Métodos de ciclo de vida JPA
    @PrePersist
    protected void onCreate() {
        fechaEvaluacion = LocalDateTime.now();
    }
    
    // Constructores
    public Evaluacion() {
    }
    
    public Evaluacion(int calificacion, String comentario, Doctor doctor, Estudiante estudiante) {
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.doctor = doctor;
        this.estudiante = estudiante;
    }
    
    public Evaluacion(int calificacion, String comentario, Doctor doctor, Estudiante estudiante, Cita cita) {
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.doctor = doctor;
        this.estudiante = estudiante;
        this.cita = cita;
    }
    
    // Getters y Setters
    public int getIdEvaluacion() {
        return idEvaluacion;
    }
    
    public void setIdEvaluacion(int idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }
    
    public int getCalificacion() {
        return calificacion;
    }
    
    public void setCalificacion(int calificacion) {
        if (calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }
        this.calificacion = calificacion;
    }
    
    public String getComentario() {
        return comentario;
    }
    
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    public LocalDateTime getFechaEvaluacion() {
        return fechaEvaluacion;
    }
    
    public void setFechaEvaluacion(LocalDateTime fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }
    
    public Doctor getDoctor() {
        return doctor;
    }
    
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    public Estudiante getEstudiante() {
        return estudiante;
    }
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public Cita getCita() {
        return cita;
    }
    
    public void setCita(Cita cita) {
        this.cita = cita;
    }
    
    // Métodos auxiliares
    public String getCalificacionEstrellas() {
        return "⭐".repeat(calificacion);
    }
    
    @Override
    public String toString() {
        return "Evaluacion{" +
                "idEvaluacion=" + idEvaluacion +
                ", calificacion=" + calificacion +
                ", doctor=" + (doctor != null ? doctor.getNombreCompleto() : "null") +
                ", estudiante=" + (estudiante != null ? estudiante.getNombreCompleto() : "null") +
                ", fechaEvaluacion=" + fechaEvaluacion +
                '}';
    }
}