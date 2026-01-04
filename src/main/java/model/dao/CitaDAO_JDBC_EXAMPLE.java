package model.dao;

import model.entity.Cita;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * EJEMPLO: CitaDAO usando JDBC directo con DatabaseConnection Singleton
 * 
 * NOTA: Tu proyecto actualmente usa JPA/ORM para Cita, por lo que este
 * archivo es solo un EJEMPLO de cómo usar DatabaseConnection si decides
 * usar JDBC directo para alguna entidad.
 * 
 * ⚠️ NO USAR ESTE ARCHIVO - Es solo referencia
 * ✅ Usa el CitaDAO actual con JPA/ORM
 */
public class CitaDAO_JDBC_EXAMPLE {
    
    // ===== SINGLETON DE CONEXIÓN =====
    private DatabaseConnection dbConnection;
    
    public CitaDAO_JDBC_EXAMPLE() {
        // Obtener instancia Singleton de DatabaseConnection
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * EJEMPLO: Obtener todas las citas usando JDBC
     */
    public List<Cita> obtenerTodas() {
        List<Cita> citas = new ArrayList<>();
        
        // SQL Query
        String sql = "SELECT * FROM cita ORDER BY fecha_cita DESC, hora_cita DESC";
        
        // ✅ Usar try-with-resources para cerrar automáticamente
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Cita cita = new Cita();
                
                // Mapear campos desde ResultSet
                cita.setIdCita(rs.getInt("id_cita"));
                cita.setFechaCita(rs.getObject("fecha_cita", LocalDate.class));
                cita.setHoraCita(rs.getObject("hora_cita", LocalTime.class));
                cita.setMotivoConsulta(rs.getString("motivo_consulta"));
                cita.setEstadoCita(rs.getString("estado_cita"));
                cita.setObservacionCita(rs.getString("observacion_cita"));
                
                // TODO: Cargar relaciones (Especialidad, Doctor) si es necesario
                // int idEspecialidad = rs.getInt("id_especialidad");
                // Especialidad esp = especialidadDAO.obtenerPorId(idEspecialidad);
                // cita.setEspecialidad(esp);
                
                citas.add(cita);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener citas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return citas;
    }
    
    /**
     * EJEMPLO: Guardar una cita usando JDBC
     */
    public boolean guardar(Cita cita) {
        String sql = "INSERT INTO cita (fecha_cita, hora_cita, motivo_consulta, estado_cita, id_especialidad, id_doctor) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Establecer parámetros
            ps.setDate(1, Date.valueOf(cita.getFechaCita()));
            ps.setTime(2, Time.valueOf(cita.getHoraCita()));
            ps.setString(3, cita.getMotivoConsulta());
            ps.setString(4, cita.getEstadoCita());
            
            // TODO: Establecer IDs de especialidad y doctor
            if (cita.getEspecialidad() != null) {
                ps.setInt(5, cita.getEspecialidad().getIdEspecialidad());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            
            if (cita.getDoctor() != null) {
                ps.setInt(6, cita.getDoctor().getIdDoctor());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            
            // Ejecutar INSERT
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                // Obtener ID generado
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cita.setIdCita(generatedKeys.getInt(1));
                    }
                }
                System.out.println("✅ Cita guardada exitosamente con ID: " + cita.getIdCita());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar cita: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * EJEMPLO: Actualizar una cita usando JDBC
     */
    public boolean actualizar(Cita cita) {
        String sql = "UPDATE cita SET fecha_cita = ?, hora_cita = ?, motivo_consulta = ?, " +
                     "estado_cita = ?, observacion_cita = ? WHERE id_cita = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, Date.valueOf(cita.getFechaCita()));
            ps.setTime(2, Time.valueOf(cita.getHoraCita()));
            ps.setString(3, cita.getMotivoConsulta());
            ps.setString(4, cita.getEstadoCita());
            ps.setString(5, cita.getObservacionCita());
            ps.setInt(6, cita.getIdCita());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("✅ Cita actualizada exitosamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar cita: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * EJEMPLO: Eliminar una cita usando JDBC
     */
    public boolean eliminar(int idCita) {
        String sql = "DELETE FROM cita WHERE id_cita = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCita);
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("✅ Cita eliminada exitosamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar cita: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * EJEMPLO: Obtener cita por ID usando JDBC
     */
    public Cita obtenerPorId(int idCita) {
        String sql = "SELECT * FROM cita WHERE id_cita = ?";
        Cita cita = null;
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCita);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cita = new Cita();
                    cita.setIdCita(rs.getInt("id_cita"));
                    cita.setFechaCita(rs.getObject("fecha_cita", LocalDate.class));
                    cita.setHoraCita(rs.getObject("hora_cita", LocalTime.class));
                    cita.setMotivoConsulta(rs.getString("motivo_consulta"));
                    cita.setEstadoCita(rs.getString("estado_cita"));
                    cita.setObservacionCita(rs.getString("observacion_cita"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener cita por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return cita;
    }
    
    /**
     * EJEMPLO: Obtener citas por estado usando JDBC
     */
    public List<Cita> obtenerPorEstado(String estado) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT * FROM cita WHERE estado_cita = ? ORDER BY fecha_cita, hora_cita";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, estado);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita cita = new Cita();
                    cita.setIdCita(rs.getInt("id_cita"));
                    cita.setFechaCita(rs.getObject("fecha_cita", LocalDate.class));
                    cita.setHoraCita(rs.getObject("hora_cita", LocalTime.class));
                    cita.setMotivoConsulta(rs.getString("motivo_consulta"));
                    cita.setEstadoCita(rs.getString("estado_cita"));
                    cita.setObservacionCita(rs.getString("observacion_cita"));
                    citas.add(cita);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener citas por estado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return citas;
    }
    
    /**
     * EJEMPLO: Contar citas por estado usando JDBC
     */
    public int contarPorEstado(String estado) {
        String sql = "SELECT COUNT(*) as total FROM cita WHERE estado_cita = ?";
        int count = 0;
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, estado);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("total");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al contar citas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return count;
    }
}
