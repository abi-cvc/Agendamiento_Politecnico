package model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "cita")

public class Cita {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCita;
	
	private LocalDate fechaCita;
	private LocalTime horaCita;
	private String motivoConsulta;
	private String estadoCita;
	private String observacionCita;
	
	// ===== RELACIÓN ORM CON ESPECIALIDAD =====
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idEspecialidad")
	private Especialidad especialidad;
	
	// ===== RELACIÓN ORM CON DOCTOR (según diagrama de robustez) =====
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idDoctor")
	private Doctor doctor;
	
	public Cita() {

	}
	
	public int getIdCita() {
		return idCita;
	}

	public void setIdCita(int idCita) {
		this.idCita = idCita;
	}

	public LocalDate getFechaCita() {
		return fechaCita;
	}

	public void setFechaCita(LocalDate fechaCita) {
		this.fechaCita = fechaCita;
	}

	public LocalTime getHoraCita() {
		return horaCita;
	}

	public void setHoraCita(LocalTime horaCita) {
		this.horaCita = horaCita;
	}

	public String getMotivoConsulta() {
		return motivoConsulta;
	}

	public void setMotivoConsulta(String motivoConsulta) {
		this.motivoConsulta = motivoConsulta;
	}

	public String getEstadoCita() {
		return estadoCita;
	}

	public void setEstadoCita(String estadoCita) {
		this.estadoCita = estadoCita;
	}

	public String getObservacionCita() {
		return observacionCita;
	}

	public void setObservacionCita(String observacionCita) {
		this.observacionCita = observacionCita;
	}

	// ===== GETTERS Y SETTERS PARA ESPECIALIDAD =====
	public Especialidad getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(Especialidad especialidad) {
		this.especialidad = especialidad;
	}
	
	// ===== GETTERS Y SETTERS PARA DOCTOR =====
	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	
	@Override
	public String toString() {
		return "Cita [idCita=" + idCita + 
			   ", fechaCita=" + fechaCita + 
			   ", horaCita=" + horaCita + 
			   ", motivoConsulta=" + motivoConsulta + 
			   ", estadoCita=" + estadoCita + 
			   ", especialidad=" + (especialidad != null ? especialidad.getTitulo() : "null") + 
			   ", doctor=" + (doctor != null ? doctor.getNombreCompleto() : "null") + "]";
	}
	
}
