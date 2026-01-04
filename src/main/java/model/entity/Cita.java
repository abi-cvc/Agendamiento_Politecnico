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
	
	
}
