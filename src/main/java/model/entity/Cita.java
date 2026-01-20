package model.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entidad Cita - Representa una cita médica agendada
 * Relación: ManyToOne con Especialidad
 * Relación: ManyToOne con Doctor
 * Relación: ManyToOne con Estudiante
 */
@Entity
@Table(name = "cita")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cita implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cita")
	private int idCita;
	
	@Column(name = "fecha_cita", nullable = false)
	private LocalDate fechaCita;
	
	@Column(name = "hora_cita", nullable = false)
	private LocalTime horaCita;
	
	@Column(name = "motivo_consulta", nullable = false, columnDefinition = "TEXT")
	private String motivoConsulta;
	
	@Column(name = "estado_cita", length = 50)
	private String estadoCita = "Agendada";
	
	@Column(name = "observacion_cita", columnDefinition = "TEXT")
	private String observacionCita;
	
	@Column(name = "fecha_registro", updatable = false)
	private LocalDateTime fechaRegistro;
	
	@Column(name = "fecha_actualizacion")
	private LocalDateTime fechaActualizacion;
	
	// ===== RELACIÓN ORM CON ESPECIALIDAD =====
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_especialidad", nullable = false)
	@JsonIgnore
	private Especialidad especialidad;
	
	// ===== RELACIÓN ORM CON DOCTOR (según diagrama de robustez) =====
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_doctor")
	@JsonIgnore
	private Doctor doctor;
	
	// ===== RELACIÓN ORM CON ESTUDIANTE =====
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_estudiante")
	@JsonIgnore
	private Estudiante estudiante;
	
	// ===== MÉTODOS DE CICLO DE VIDA JPA =====
	@PrePersist
	protected void onCreate() {
		fechaRegistro = LocalDateTime.now();
		fechaActualizacion = LocalDateTime.now();
		if (estadoCita == null) {
			estadoCita = "Agendada";
		}
	}
	
	@PreUpdate
	protected void onUpdate() {
		fechaActualizacion = LocalDateTime.now();
	}
	
	// ===== CONSTRUCTORES =====
	public Cita() {
	}
	
	public Cita(LocalDate fechaCita, LocalTime horaCita, String motivoConsulta, 
	            Especialidad especialidad, Doctor doctor) {
		this.fechaCita = fechaCita;
		this.horaCita = horaCita;
		this.motivoConsulta = motivoConsulta;
		this.especialidad = especialidad;
		this.doctor = doctor;
		this.estadoCita = "Agendada";
	}
	
	// ===== MÉTODOS DE NEGOCIO (según diagrama de clases) =====
	
	/**
	 * Método crear() - Valida y prepara la cita para ser persistida
	 * @return true si la cita puede ser creada, false en caso contrario
	 */
	public boolean crear() {
		// Validar que los campos obligatorios estén presentes
		if (fechaCita == null || horaCita == null || 
		    motivoConsulta == null || motivoConsulta.trim().isEmpty() ||
		    especialidad == null) {
			return false;
		}
		
		// Validar que la fecha no sea pasada
		if (fechaCita.isBefore(LocalDate.now())) {
			return false;
		}
		
		// Si la fecha es hoy, validar que la hora sea futura
		if (fechaCita.isEqual(LocalDate.now()) && horaCita.isBefore(LocalTime.now())) {
			return false;
		}
		
		// Establecer estado inicial
		this.estadoCita = "Agendada";
		return true;
	}
	
	/**
	 * Método cancelar() - Cancela la cita si está en estado válido
	 * @return true si la cita fue cancelada, false en caso contrario
	 */
	public boolean cancelar() {
		// Solo se pueden cancelar citas que estén agendadas o confirmadas
		if (!"Agendada".equals(this.estadoCita) && !"Confirmada".equals(this.estadoCita)) {
			return false;
		}
		
		// No se pueden cancelar citas pasadas
		if (fechaCita.isBefore(LocalDate.now())) {
			return false;
		}
		
		// Si es hoy, verificar que no haya pasado la hora
		if (fechaCita.isEqual(LocalDate.now()) && horaCita.isBefore(LocalTime.now())) {
			return false;
		}
		
		this.estadoCita = "Cancelada";
		return true;
	}
	
	// ===== GETTERS Y SETTERS =====
	
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
	
	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}
	
	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	public LocalDateTime getFechaActualizacion() {
		return fechaActualizacion;
	}
	
	public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
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
	
	// ===== GETTERS Y SETTERS PARA ESTUDIANTE =====
	public Estudiante getEstudiante() {
		return estudiante;
	}

	public void setEstudiante(Estudiante estudiante) {
		this.estudiante = estudiante;
	}
	
	@Override
	public String toString() {
		return "Cita{" +
			   "idCita=" + idCita + 
			   ", fechaCita=" + fechaCita + 
			   ", horaCita=" + horaCita + 
			   ", motivoConsulta='" + motivoConsulta + '\'' + 
			   ", estadoCita='" + estadoCita + '\'' + 
			   ", especialidad=" + (especialidad != null ? especialidad.getTitulo() : "null") + 
			   ", doctor=" + (doctor != null ? doctor.getNombreCompleto() : "null") + 
			   ", estudiante=" + (estudiante != null ? estudiante.getNombreCompleto() : "null") + 
			   ", fechaRegistro=" + fechaRegistro +
			   ", fechaActualizacion=" + fechaActualizacion +
			   '}';
	}
	
}
