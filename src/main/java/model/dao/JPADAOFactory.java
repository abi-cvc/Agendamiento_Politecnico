package model.dao;

/**
 * Factory concreto para crear DAOs basados en JPA
 * Implementa el patrón según el diagrama de arquitectura
 */
public class JPADAOFactory extends DAOFactory {
    
    @Override
    public ICitaDAO getCitaDAO() {
        return new CitaDAO();
    }
    
    @Override
    public IDoctorDAO getDoctorDAO() {
        return new DoctorDAO();
    }
    
    @Override
    public IEspecialidadDAO getEspecialidadDAO() {
        return new EspecialidadDAO();
    }
    
    @Override
    public IDisponibilidadDAO getDisponibilidadDAO() {
        return new DisponibilidadDAO();
    }
    
    @Override
    public IEstudianteDAO getEstudianteDAO() {
        return new EstudianteDAO();
    }
    
    @Override
    public IAdministradorDAO getAdministradorDAO() {
        return new AdministradorDAO();
    }
}