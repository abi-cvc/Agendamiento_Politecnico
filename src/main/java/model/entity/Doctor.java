package model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Entidad Doctor - Representa a un doctor en el sistema
 * Relación: ManyToOne con Especialidad
 * Relación: OneToMany con Disponibilidad
 * Relación: OneToMany con Cita
 */
@Entity
@Table(name = "doctor")
public class Doctor implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doctor")
    private int idDoctor;
    
    @Column(name = "cedula", nullable = false, unique = true, length = 10)
    private String cedula;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;
    
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "telefono", length = 15)
    private String telefono;
    
    @Column(name = "foto", length = 255)
    private String foto;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "activo")
    private boolean activo = true;
    
    // Relación ManyToOne con Especialidad
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_especialidad", nullable = false)
    private Especialidad especialidad;
    
    // Relación OneToMany con Disponibilidad
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Disponibilidad> disponibilidades;
    
    // Relación OneToMany con Cita
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cita> citas;
    
    // Constructores
    public Doctor() {
    }
    
    public Doctor(String cedula, String nombre, String apellido, String email, Especialidad especialidad) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.especialidad = especialidad;
    }
    
    // Getters y Setters
    public int getIdDoctor() {
        return idDoctor;
    }
    
    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }
    
    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getFoto() {
        return foto;
    }
    
    public void setFoto(String foto) {
        this.foto = foto;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public Especialidad getEspecialidad() {
        return especialidad;
    }
    
    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }
    
    public List<Disponibilidad> getDisponibilidades() {
        return disponibilidades;
    }
    
    public void setDisponibilidades(List<Disponibilidad> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }
    
    public List<Cita> getCitas() {
        return citas;
    }
    
    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }
    
    // Método auxiliar para obtener nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    @Override
    public String toString() {
        return "Doctor{" +
                "idDoctor=" + idDoctor +
                ", cedula='" + cedula + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", especialidad=" + (especialidad != null ? especialidad.getTitulo() : "null") +
                ", activo=" + activo +
                '}';
    }
}
