package model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entidad Administrador - Representa a un administrador del sistema
 * Relación: OneToOne con Especialidad (puede gestionar una especialidad)
 */
@Entity
@Table(name = "administrador")
public class Administrador implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_administrador")
    private int idAdministrador;
    
    @Column(name = "id_admin", nullable = false, unique = true, length = 20)
    private String idAdmin; // Usuario o cédula del administrador
    
    @Column(name = "nombre_admin", nullable = false, length = 100)
    private String nombreAdmin;
    
    @Column(name = "apellido_admin", nullable = false, length = 100)
    private String apellidoAdmin;
    
    @Column(name = "correo_admin", nullable = false, unique = true, length = 100)
    private String correoAdmin;
    
    @Column(name = "password_admin", nullable = false, length = 255)
    private String passwordAdmin; // Contraseña encriptada
    
    @Column(name = "rol", nullable = false, length = 50)
    private String rol = "ADMIN"; // Rol del administrador
    
    @Column(name = "activo")
    private boolean activo = true;
    
    // ===== CONSTRUCTORES =====
    public Administrador() {
    }
    
    public Administrador(String idAdmin, String nombreAdmin, String apellidoAdmin, 
                        String correoAdmin, String passwordAdmin) {
        this.idAdmin = idAdmin;
        this.nombreAdmin = nombreAdmin;
        this.apellidoAdmin = apellidoAdmin;
        this.correoAdmin = correoAdmin;
        this.passwordAdmin = passwordAdmin;
    }
    
    // ===== MÉTODOS DE NEGOCIO (según diagrama de clases) =====
    
    /**
     * Método iniciarSesion() - Valida las credenciales del administrador
     * @param password Contraseña a validar
     * @return true si las credenciales son válidas
     */
    public boolean iniciarSesion(String password) {
        // En producción, aquí se debe validar con hash (BCrypt, etc.)
        return this.activo && this.passwordAdmin != null && 
               this.passwordAdmin.equals(password);
    }
    
    /**
     * Método gestionarDoctores() - Gestiona los doctores del sistema
     * @return true si tiene permisos para gestionar doctores
     */
    public boolean gestionarDoctores() {
        return this.activo && ("ADMIN".equals(this.rol) || "SUPER_ADMIN".equals(this.rol));
    }
    
    /**
     * Método gestionarEspecialidades() - Gestiona las especialidades
     * @return true si tiene permisos para gestionar especialidades
     */
    public boolean gestionarEspecialidades() {
        return this.activo && ("ADMIN".equals(this.rol) || "SUPER_ADMIN".equals(this.rol));
    }

    /**
     * Método gestionarEstudiantes() - Indica si el administrador puede gestionar estudiantes
     * @return true si tiene permisos para gestionar estudiantes
     */
    public boolean gestionarEstudiantes() {
        return this.activo && ("ADMIN".equals(this.rol) || "SUPER_ADMIN".equals(this.rol));
    }
    
    /**
     * Método cerrarSesion() - Cierra la sesión del administrador
     */
    public void cerrarSesion() {
        // Lógica para cerrar sesión (limpieza de datos de sesión)
    }
    
    // ===== GETTERS Y SETTERS =====
    
    public int getIdAdministrador() {
        return idAdministrador;
    }
    
    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }
    
    public String getIdAdmin() {
        return idAdmin;
    }
    
    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }
    
    public String getNombreAdmin() {
        return nombreAdmin;
    }
    
    public void setNombreAdmin(String nombreAdmin) {
        this.nombreAdmin = nombreAdmin;
    }
    
    public String getApellidoAdmin() {
        return apellidoAdmin;
    }
    
    public void setApellidoAdmin(String apellidoAdmin) {
        this.apellidoAdmin = apellidoAdmin;
    }
    
    public String getCorreoAdmin() {
        return correoAdmin;
    }
    
    public void setCorreoAdmin(String correoAdmin) {
        this.correoAdmin = correoAdmin;
    }
    
    public String getPasswordAdmin() {
        return passwordAdmin;
    }
    
    public void setPasswordAdmin(String passwordAdmin) {
        this.passwordAdmin = passwordAdmin;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    // Método auxiliar para obtener nombre completo
    public String getNombreCompleto() {
        return nombreAdmin + " " + apellidoAdmin;
    }
    
    @Override
    public String toString() {
        return "Administrador{" +
                "idAdministrador=" + idAdministrador +
                ", idAdmin='" + idAdmin + '\'' +
                ", nombreAdmin='" + nombreAdmin + '\'' +
                ", apellidoAdmin='" + apellidoAdmin + '\'' +
                ", correoAdmin='" + correoAdmin + '\'' +
                ", rol='" + rol + '\'' +
                ", activo=" + activo +
                '}';
    }
}