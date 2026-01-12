package model.dao;

import model.entity.Administrador;
import java.util.List;

/**
 * Interfaz específica para operaciones de Administrador
 * Extiende GenericDAO y agrega métodos específicos del negocio
 * Sigue el patrón del diagrama de arquitectura
 */
public interface IAdministradorDAO extends GenericDAO<Administrador, Integer> {
    
    /**
     * Busca un administrador por su ID de admin (usuario)
     * @param idAdmin ID de admin (usuario único)
     * @return Administrador encontrado o null
     */
    Administrador buscarPorIdAdmin(String idAdmin);
    
    /**
     * Busca un administrador por su correo electrónico
     * @param correo Correo del administrador
     * @return Administrador encontrado o null
     */
    Administrador buscarPorCorreo(String correo);
    
    /**
     * Obtiene todos los administradores activos
     * @return Lista de administradores activos
     */
    List<Administrador> obtenerActivos();
    
    /**
     * Valida las credenciales de un administrador para login
     * @param idAdmin ID de admin
     * @param password Contraseña
     * @return Administrador si las credenciales son válidas, null en caso contrario
     */
    Administrador validarCredenciales(String idAdmin, String password);
    
    /**
     * Cambia el estado activo de un administrador
     * @param id ID del administrador
     * @param activo Nuevo estado
     */
    void cambiarEstado(int id, boolean activo);
    
    /**
     * Verifica si existe un administrador por ID de admin
     * @param idAdmin ID de admin a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existePorIdAdmin(String idAdmin);
    
    /**
     * Verifica si existe un administrador por correo
     * @param correo Correo a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existePorCorreo(String correo);
}