package model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entidad Especialidad - Representa las especialidades médicas disponibles
 */
@Entity
@Table(name = "especialidad")
public class Especialidad implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_especialidad")
	private int idEspecialidad;
	
	@Column(name = "nombre", nullable = false, unique = true, length = 50)
	private String nombre;
	
	@Column(name = "titulo", nullable = false, length = 100)
	private String titulo;
	
	@Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
	private String descripcion;
	
	@Column(name = "servicios", nullable = false, columnDefinition = "TEXT")
	private String servicios;
	
	@Column(name = "icono", length = 50)
	private String icono;
	
	// Constructor vacío (requerido por JPA)
	public Especialidad() {
	}
	
	// Constructor con parámetros
	public Especialidad(String nombre, String titulo, String descripcion, String servicios, String icono) {
		this.nombre = nombre;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.servicios = servicios;
		this.icono = icono;
	}
	
	// Getters y Setters
	public int getIdEspecialidad() {
		return idEspecialidad;
	}
	
	public void setIdEspecialidad(int idEspecialidad) {
		this.idEspecialidad = idEspecialidad;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getServicios() {
		return servicios;
	}
	
	public void setServicios(String servicios) {
		this.servicios = servicios;
	}
	
	public String getIcono() {
		return icono;
	}
	
	public void setIcono(String icono) {
		this.icono = icono;
	}
	
	@Override
	public String toString() {
		return "Especialidad [idEspecialidad=" + idEspecialidad + ", nombre=" + nombre + ", titulo=" + titulo + "]";
	}
}
