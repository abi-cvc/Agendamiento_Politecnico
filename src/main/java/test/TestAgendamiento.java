package test;

import model.dao.EspecialidadDAO;
import model.dao.DoctorDAO;
import model.entity.Especialidad;
import model.entity.Doctor;
import java.util.List;

/**
 * Test rápido para verificar que la funcionalidad de especialidades y doctores funciona
 */
public class TestAgendamiento {
    
    public static void main(String[] args) {
        System.out.println("=== TEST AGENDAMIENTO - ESPECIALIDADES Y DOCTORES ===\n");
        
        try {
            // Test 1: Cargar todas las especialidades
            System.out.println("1️⃣ Test: Cargar todas las especialidades");
            EspecialidadDAO especialidadDAO = new EspecialidadDAO();
            List<Especialidad> especialidades = especialidadDAO.obtenerEspecialidades();
            
            if (especialidades != null && !especialidades.isEmpty()) {
                System.out.println("✅ Se encontraron " + especialidades.size() + " especialidades:");
                for (Especialidad esp : especialidades) {
                    System.out.println("   - " + esp.getIcono() + " " + esp.getTitulo() + " (" + esp.getNombre() + ")");
                }
            } else {
                System.out.println("❌ No se encontraron especialidades");
                return;
            }
            
            System.out.println("\n" + "=".repeat(60) + "\n");
            
            // Test 2: Obtener una especialidad por nombre
            System.out.println("2️⃣ Test: Buscar especialidad 'nutricion'");
            Especialidad nutricion = especialidadDAO.obtenerPorNombre("nutricion");
            
            if (nutricion != null) {
                System.out.println("✅ Especialidad encontrada:");
                System.out.println("   ID: " + nutricion.getIdEspecialidad());
                System.out.println("   Nombre: " + nutricion.getNombre());
                System.out.println("   Título: " + nutricion.getTitulo());
                System.out.println("   Descripción: " + nutricion.getDescripcion().substring(0, 100) + "...");
            } else {
                System.out.println("❌ Especialidad 'nutricion' no encontrada");
                return;
            }
            
            System.out.println("\n" + "=".repeat(60) + "\n");
            
            // Test 3: Obtener doctores de la especialidad
            System.out.println("3️⃣ Test: Cargar doctores de Nutrición");
            DoctorDAO doctorDAO = new DoctorDAO();
            List<Doctor> doctores = doctorDAO.obtenerPorEspecialidad(nutricion);
            
            if (doctores != null && !doctores.isEmpty()) {
                System.out.println("✅ Se encontraron " + doctores.size() + " doctores de Nutrición:");
                for (Doctor doc : doctores) {
                    System.out.println("   - " + doc.getNombreCompleto());
                    System.out.println("     Email: " + doc.getEmail());
                    System.out.println("     Especialidad: " + doc.getEspecialidad().getTitulo());
                    System.out.println("     Activo: " + (doc.isActivo() ? "Sí" : "No"));
                    System.out.println();
                }
            } else {
                System.out.println("⚠️  No se encontraron doctores para Nutrición");
            }
            
            System.out.println("=".repeat(60) + "\n");
            
            // Test 4: Probar con todas las especialidades
            System.out.println("4️⃣ Test: Doctores por cada especialidad\n");
            for (Especialidad esp : especialidades) {
                List<Doctor> docs = doctorDAO.obtenerPorEspecialidad(esp);
                System.out.println(esp.getIcono() + " " + esp.getTitulo() + ": " + 
                                   (docs != null ? docs.size() : 0) + " doctor(es)");
                if (docs != null) {
                    for (Doctor doc : docs) {
                        System.out.println("     → " + doc.getNombreCompleto());
                    }
                }
                System.out.println();
            }
            
            System.out.println("=".repeat(60));
            System.out.println("✅ ¡TODOS LOS TESTS COMPLETADOS EXITOSAMENTE!");
            System.out.println("=".repeat(60));
            
        } catch (Exception e) {
            System.err.println("\n❌ ERROR EN EL TEST:");
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("\nStack trace:");
            e.printStackTrace();
            
            System.err.println("\n⚠️  POSIBLES CAUSAS:");
            System.err.println("1. MySQL no está corriendo");
            System.err.println("2. La base de datos 'agendamiento_politecnico' no existe");
            System.err.println("3. Las tablas no están creadas (ejecutar init_database.sql)");
            System.err.println("4. Credenciales incorrectas (verificar DatabaseConnection.java)");
        }
    }
}
