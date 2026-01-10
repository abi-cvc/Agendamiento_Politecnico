package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase Singleton para gestionar la conexión a la base de datos MySQL.
 * Garantiza una única instancia de conexión en toda la aplicación.
 * 
 * PATRÓN: Singleton
 * PROPÓSITO: Centralizar y controlar el acceso a la base de datos
 */
public class DatabaseConnection {
    
    // ========================================
    // CONFIGURACIÓN DE BASE DE DATOS
    // ========================================
    
    // TODO: CONFIGURAR ESTOS VALORES SEGÚN TU BASE DE DATOS
    
    /**
     * URL de conexión a MySQL
     * FORMATO: jdbc:mysql://[HOST]:[PUERTO]/[NOMBRE_BD]?serverTimezone=UTC
     * 
     * CONFIGURACIÓN PARA XAMPP (MySQL Default):
     * - Host: localhost
     * - Puerto: 3306
     * - Base de datos: agendamiento_politecnico
     * - useSSL=false: Desactiva SSL (no necesario en desarrollo local)
     * - serverTimezone=America/Guayaquil: Zona horaria de Ecuador
     * - allowPublicKeyRetrieval=true: Necesario para MySQL 8.x con root sin password
     * 
     * EJEMPLOS:
     * - Local XAMPP: "jdbc:mysql://localhost:3306/agendamiento_politecnico?useSSL=false&serverTimezone=America/Guayaquil&allowPublicKeyRetrieval=true"
     * - Remoto: "jdbc:mysql://192.168.1.100:3306/agendamiento_politecnico?useSSL=false&serverTimezone=America/Guayaquil"
     */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/agendamiento_politecnico?useSSL=false&serverTimezone=America/Guayaquil&allowPublicKeyRetrieval=true";
    
    /**
     * Usuario de MySQL
     * DEFAULT en XAMPP/WAMP: "root"
     * 
     * TODO: Cambiar por tu usuario de MySQL
     */
    private static final String DB_USER = "root";
    
    /**
     * Contraseña de MySQL
     * DEFAULT en XAMPP: "" (vacío)
     * DEFAULT en WAMP: "" (vacío)
     * 
     * TODO: Cambiar por tu contraseña de MySQL
     */
    private static final String DB_PASSWORD = "1234";
    
    /**
     * Driver JDBC de MySQL
     * NO CAMBIAR - Este es el driver correcto para MySQL 8.x
     */
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // ========================================
    // SINGLETON PATTERN
    // ========================================
    
    /**
     * Instancia única de la conexión (Singleton)
     */
    private static DatabaseConnection instance;
    
    /**
     * Objeto Connection de JDBC
     */
    private Connection connection;
    
    /**
     * Constructor privado para evitar instanciación externa
     * PATRÓN SINGLETON: Solo se puede crear desde dentro de la clase
     */
    private DatabaseConnection() {
        try {
            // Cargar el driver de MySQL
            Class.forName(DB_DRIVER);
            System.out.println("✅ Driver MySQL cargado correctamente");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ ERROR: Driver MySQL no encontrado");
            System.err.println("   Asegúrate de tener mysql-connector-j en el pom.xml");
            e.printStackTrace();
        }
    }
    
    /**
     * Método para obtener la instancia única (Singleton)
     * THREAD-SAFE: Sincronizado para evitar problemas con múltiples hilos
     * 
     * @return Instancia única de DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Obtiene una conexión a la base de datos
     * Si la conexión está cerrada o es null, crea una nueva
     * 
     * @return Connection activa a la base de datos
     * @throws SQLException si hay error al conectar
     */
    public Connection getConnection() throws SQLException {
        try {
            // Verificar si la conexión existe y está activa
            if (connection == null || connection.isClosed()) {
                System.out.println("🔄 Creando nueva conexión a la base de datos...");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("✅ Conexión establecida exitosamente");
                System.out.println("   📍 Base de datos: " + connection.getCatalog());
            }
        } catch (SQLException e) {
            System.err.println("❌ ERROR al conectar con la base de datos");
            System.err.println("   Verifica:");
            System.err.println("   1. MySQL está corriendo (XAMPP/WAMP)");
            System.err.println("   2. URL de conexión: " + DB_URL);
            System.err.println("   3. Usuario: " + DB_USER);
            System.err.println("   4. La base de datos existe");
            throw e;
        }
        return connection;
    }
    
    /**
     * Cierra la conexión a la base de datos
     * Usar al finalizar la aplicación
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("❌ ERROR al cerrar la conexión");
            e.printStackTrace();
        }
    }
    
    /**
     * Verifica si la conexión está activa
     * 
     * @return true si la conexión está activa, false en caso contrario
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Método de prueba para verificar la conexión
     * 
     * @return true si la conexión fue exitosa, false en caso contrario
     */
    public boolean testConnection() {
        try {
            Connection testConn = getConnection();
            System.out.println("\n=== TEST DE CONEXIÓN ===");
            System.out.println("✅ Estado: CONECTADO");
            System.out.println("📍 Base de datos: " + testConn.getCatalog());
            System.out.println("🌐 URL: " + DB_URL);
            System.out.println("👤 Usuario: " + DB_USER);
            System.out.println("========================\n");
            return true;
        } catch (SQLException e) {
            System.err.println("\n=== TEST DE CONEXIÓN ===");
            System.err.println("❌ Estado: ERROR");
            System.err.println("❌ Mensaje: " + e.getMessage());
            System.err.println("========================\n");
            return false;
        }
    }
    
    // ========================================
    // MÉTODOS DE INFORMACIÓN
    // ========================================
    
    /**
     * Obtiene la URL de conexión configurada
     * 
     * @return URL de la base de datos
     */
    public String getDatabaseUrl() {
        return DB_URL;
    }
    
    /**
     * Obtiene el usuario configurado
     * 
     * @return Usuario de MySQL
     */
    public String getDatabaseUser() {
        return DB_USER;
    }
    
    // ========================================
    // MAIN DE PRUEBA
    // ========================================
    
    /**
     * Método main para probar la conexión
     * Ejecutar esta clase directamente para verificar la configuración
     */
    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("  PRUEBA DE CONEXIÓN A BD");
        System.out.println("=================================\n");
        
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        
        if (dbConnection.testConnection()) {
            System.out.println("✅ La conexión funciona correctamente");
            System.out.println("✅ Puedes usar esta configuración");
            dbConnection.closeConnection();
        } else {
            System.out.println("❌ Error en la conexión");
            System.out.println("⚠️ Revisa la configuración en DatabaseConnection.java:");
            System.out.println("   - DB_URL");
            System.out.println("   - DB_USER");
            System.out.println("   - DB_PASSWORD");
        }
    }
}
