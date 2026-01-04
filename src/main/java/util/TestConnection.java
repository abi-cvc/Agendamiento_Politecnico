package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.entity.Especialidad;
import model.entity.Doctor;

import java.sql.Connection;
import java.util.List;

/**
 * Clase de prueba para verificar las conexiones a la base de datos
 * 
 * PROPÓSITO:
 * 1. Probar conexión JDBC (DatabaseConnection)
 * 2. Probar conexión JPA (JPAUtil)
 * 3. Verificar que las entidades funcionan correctamente
 * 
 * INSTRUCCIONES:
 * - Ejecutar esta clase como Java Application
 * - Verificar que XAMPP MySQL esté corriendo
 * - Verificar que la base de datos 'agendamiento_politecnico' existe
 */
public class TestConnection {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║  PRUEBA DE CONEXIÓN - AGENDAMIENTO POLITÉCNICO ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        // Prueba 1: Conexión JDBC
        testJDBCConnection();
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Prueba 2: Conexión JPA
        testJPAConnection();
        
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║              PRUEBAS FINALIZADAS              ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }
    
    /**
     * Prueba 1: Conexión JDBC tradicional usando DatabaseConnection
     */
    private static void testJDBCConnection() {
        System.out.println("🔹 PRUEBA 1: CONEXIÓN JDBC (DatabaseConnection)");
        System.out.println("-".repeat(50));
        
        try {
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            Connection conn = dbConnection.getConnection();
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión JDBC exitosa");
                System.out.println("   📍 Base de datos: " + conn.getCatalog());
                System.out.println("   🌐 URL: " + dbConnection.getDatabaseUrl());
                System.out.println("   👤 Usuario: " + dbConnection.getDatabaseUser());
                System.out.println("   ⏱️  Tiempo de respuesta: OK");
                
                // Verificar que la conexión es válida
                if (conn.isValid(2)) {
                    System.out.println("   ✅ Conexión válida y funcional");
                }
            } else {
                System.out.println("❌ Error: Conexión es null o está cerrada");
            }
            
        } catch (Exception e) {
            System.out.println("❌ ERROR en conexión JDBC:");
            System.out.println("   " + e.getMessage());
            System.out.println("\n⚠️  VERIFICAR:");
            System.out.println("   1. XAMPP MySQL está corriendo");
            System.out.println("   2. Puerto 3306 está disponible");
            System.out.println("   3. Base de datos 'agendamiento_politecnico' existe");
            System.out.println("   4. Usuario 'root' con password vacío");
            e.printStackTrace();
        }
    }
    
    /**
     * Prueba 2: Conexión JPA usando JPAUtil y consultas a entidades
     */
    private static void testJPAConnection() {
        System.out.println("🔹 PRUEBA 2: CONEXIÓN JPA (JPAUtil + Entidades)");
        System.out.println("-".repeat(50));
        
        EntityManager em = null;
        
        try {
            // Obtener EntityManager
            em = JPAUtil.getEntityManager();
            System.out.println("✅ EntityManager creado correctamente");
            
            // Verificar que el EntityManager esté abierto
            if (em.isOpen()) {
                System.out.println("✅ EntityManager está abierto y listo");
            }
            
            // Prueba 3: Consultar especialidades
            testQueryEspecialidades(em);
            
            System.out.println();
            
            // Prueba 4: Consultar doctores
            testQueryDoctores(em);
            
        } catch (Exception e) {
            System.out.println("❌ ERROR en conexión JPA:");
            System.out.println("   " + e.getMessage());
            System.out.println("\n⚠️  VERIFICAR:");
            System.out.println("   1. persistence.xml está configurado correctamente");
            System.out.println("   2. Las tablas existen en la base de datos");
            System.out.println("   3. Las entidades JPA están bien mapeadas");
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
                System.out.println("\n✅ EntityManager cerrado correctamente");
            }
        }
    }
    
    /**
     * Consulta las especialidades en la base de datos
     */
    private static void testQueryEspecialidades(EntityManager em) {
        try {
            System.out.println("\n📊 Consultando especialidades...");
            
            List<Especialidad> especialidades = em.createQuery(
                "SELECT e FROM Especialidad e", Especialidad.class
            ).getResultList();
            
            if (especialidades.isEmpty()) {
                System.out.println("⚠️  No se encontraron especialidades");
                System.out.println("   Ejecuta init_database.sql para insertar datos");
            } else {
                System.out.println("✅ Especialidades encontradas: " + especialidades.size());
                System.out.println("\n   Lista de especialidades:");
                for (Especialidad esp : especialidades) {
                    System.out.println("   - " + esp.getTitulo() + " (ID: " + esp.getIdEspecialidad() + ")");
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al consultar especialidades: " + e.getMessage());
        }
    }
    
    /**
     * Consulta los doctores en la base de datos
     */
    private static void testQueryDoctores(EntityManager em) {
        try {
            System.out.println("\n📊 Consultando doctores...");
            
            List<Doctor> doctores = em.createQuery(
                "SELECT d FROM Doctor d", Doctor.class
            ).getResultList();
            
            if (doctores.isEmpty()) {
                System.out.println("⚠️  No se encontraron doctores");
                System.out.println("   Ejecuta init_database.sql para insertar datos");
            } else {
                System.out.println("✅ Doctores encontrados: " + doctores.size());
                System.out.println("\n   Lista de doctores:");
                for (Doctor doc : doctores) {
                    System.out.println("   - Dr(a). " + doc.getNombreCompleto() + 
                                     " - " + doc.getEspecialidad().getTitulo());
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al consultar doctores: " + e.getMessage());
        }
    }
}
