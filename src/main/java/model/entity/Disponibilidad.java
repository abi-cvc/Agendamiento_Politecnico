package model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entidad Disponibilidad - Representa los horarios disponibles de un doctor
 * Relación: ManyToOne con Doctor
 */
@Entity
@Table(name = "disponibilidad")
public class Disponibilidad implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disponibilidad")
    private int idDisponibilidad;
    
    // Relación ManyToOne con Doctor
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_doctor", nullable = false)
    private Doctor doctor;
    
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;
    
    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;
    
    @Column(name = "disponible")
    private boolean disponible = true;
    
    @Column(name = "dia_semana", length = 20)
    private String diaSemana; // Lunes, Martes, etc.
    
    // Constructores
    public Disponibilidad() {
    }
    
    public Disponibilidad(Doctor doctor, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.doctor = doctor;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.disponible = true;
        this.diaSemana = fecha.getDayOfWeek().toString();
    }
    
    // Getters y Setters
    public int getIdDisponibilidad() {
        return idDisponibilidad;
    }
    
    public void setIdDisponibilidad(int idDisponibilidad) {
        this.idDisponibilidad = idDisponibilidad;
    }
    
    public Doctor getDoctor() {
        return doctor;
    }
    
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
        if (fecha != null) {
            this.diaSemana = fecha.getDayOfWeek().toString();
        }
    }
    
    public LocalTime getHoraInicio() {
        return horaInicio;
    }
    
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }
    
    public LocalTime getHoraFin() {
        return horaFin;
    }
    
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
    
    public boolean isDisponible() {
        return disponible;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    public String getDiaSemana() {
        return diaSemana;
    }
    
    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }
    
    // Métodos auxiliares
    public String getRangoHorario() {
        return horaInicio.toString() + " - " + horaFin.toString();
    }
    
    @Override
    public String toString() {
        return "Disponibilidad{" +
                "idDisponibilidad=" + idDisponibilidad +
                ", doctor=" + (doctor != null ? doctor.getNombreCompleto() : "null") +
                ", fecha=" + fecha +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", disponible=" + disponible +
                '}';
    }
}
