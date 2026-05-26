package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.DisponibilidadDAO;
import model.entity.Disponibilidad;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet para obtener horarios disponibles de un doctor en una fecha específica
 */
@WebServlet("/api/disponibilidad")
public class DisponibilidadServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Obtener parámetros
            String idDoctorStr = request.getParameter("idDoctor");
            String fechaStr = request.getParameter("fecha");
            
            if (idDoctorStr == null || fechaStr == null) {
                enviarError(out, "Parámetros faltantes: idDoctor y fecha son requeridos");
                return;
            }
            
            int idDoctor = Integer.parseInt(idDoctorStr);
            LocalDate fecha = LocalDate.parse(fechaStr);
            
            // Obtener disponibilidades desde la BD
            DisponibilidadDAO dao = new DisponibilidadDAO();
            List<Disponibilidad> disponibilidades = dao.obtenerPorDoctorYFecha(idDoctor, fecha);
            
            // Convertir a formato JSON amigable
            List<Map<String, String>> horarios = new ArrayList<>();
            for (Disponibilidad disp : disponibilidades) {
                Map<String, String> horario = new HashMap<>();
                horario.put("id", String.valueOf(disp.getIdDisponibilidad()));
                horario.put("horaInicio", disp.getHoraInicio().toString());
                horario.put("horaFin", disp.getHoraFin().toString());
                horario.put("disponible", String.valueOf(disp.isDisponible()));
                
                // Formato legible: "08:00 - 09:00"
                String horaInicioStr = formatearHora(disp.getHoraInicio());
                String horaFinStr = formatearHora(disp.getHoraFin());
                horario.put("horarioFormateado", horaInicioStr + " - " + horaFinStr);
                
                horarios.add(horario);
            }
            
            // Crear respuesta JSON
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("success", true);
            respuesta.put("idDoctor", idDoctor);
            respuesta.put("fecha", fechaStr);
            respuesta.put("cantidad", horarios.size());
            respuesta.put("horarios", horarios);
            
            // Convertir a JSON y enviar
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(respuesta);
            out.print(json);
            
        } catch (NumberFormatException e) {
            enviarError(out, "ID de doctor inválido");
        } catch (Exception e) {
            enviarError(out, "Error al obtener disponibilidades: " + e.getMessage());
            e.printStackTrace();
        } finally {
            out.flush();
        }
    }
    
    /**
     * Formatea una hora de LocalTime a formato AM/PM
     */
    private String formatearHora(LocalTime hora) {
        int hour = hora.getHour();
        int minute = hora.getMinute();
        
        String period = hour >= 12 ? "pm" : "am";
        int hour12 = hour == 0 ? 12 : (hour > 12 ? hour - 12 : hour);
        
        return String.format("%d:%02d%s", hour12, minute, period);
    }
    
    /**
     * Envía un mensaje de error en formato JSON
     */
    private void enviarError(PrintWriter out, String mensaje) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", mensaje);
        
        Gson gson = new Gson();
        out.print(gson.toJson(error));
    }
}
