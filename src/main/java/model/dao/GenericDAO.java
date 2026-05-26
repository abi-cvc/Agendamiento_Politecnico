package model.dao;

import java.util.List;

/**
 * Interfaz genérica para operaciones CRUD básicas
 * Sigue el patrón DAO genérico del diagrama de arquitectura
 * 
 * @param <T> Tipo de la entidad
 * @param <ID> Tipo del identificador (Integer, Long, String, etc.)
 */
public interface GenericDAO<T, ID> {
    
    /**
     * Crea una nueva entidad en la base de datos
     * @param entity Entidad a crear
     * @return La entidad persistida con su ID generado
     */
    T create(T entity);
    
    /**
     * Actualiza una entidad existente
     * @param entity Entidad con los datos actualizados
     * @return La entidad actualizada
     */
    T update(T entity);
    
    /**
     * Elimina una entidad por su ID
     * @param id ID de la entidad a eliminar
     */
    void delete(ID id);
    
    /**
     * Obtiene una entidad por su ID
     * @param id ID de la entidad
     * @return La entidad encontrada o null si no existe
     */
    T getById(ID id);
    
    /**
     * Obtiene todas las entidades
     * @return Lista de todas las entidades
     */
    List<T> getAll();
}
