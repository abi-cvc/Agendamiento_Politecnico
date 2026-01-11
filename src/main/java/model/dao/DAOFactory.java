package model.dao;

/**
 * Factory para crear instancias de DAOs
 * Implementa el patrón Factory según el diagrama de arquitectura
 * Permite cambiar fácilmente entre diferentes implementaciones (JPA, XML, etc.)
 */
public abstract class DAOFactory {
    
    // Tipos de factories disponibles
    public static final int JPA = 1;
    public static final int XML = 2;
    
    /**
     * Obtiene una instancia del factory según el tipo
     * @param type Tipo de factory (JPA, XML, etc.)
     * @return Instancia del factory correspondiente
     */
    public static DAOFactory getFactory(int type) {
        switch (type) {
            case JPA:
                return new JPADAOFactory();
            case XML:
                // return new XMLDAOFactory(); // Implementar si es necesario
                throw new UnsupportedOperationException("XML Factory no implementado");
            default:
                return new JPADAOFactory();
        }
    }
    
    /**
     * Obtiene el factory por defecto (JPA)
     */
    public static DAOFactory getFactory() {
        return getFactory(JPA);
    }
    
    // Métodos abstractos que cada factory debe implementar
    public abstract ICitaDAO getCitaDAO();
    public abstract IDoctorDAO getDoctorDAO();
    public abstract IEspecialidadDAO getEspecialidadDAO();
    public abstract IDisponibilidadDAO getDisponibilidadDAO();
    public abstract IEstudianteDAO getEstudianteDAO();
    public abstract IAdministradorDAO getAdministradorDAO();
}