package test;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CitaDAO;
import model.entity.Cita;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet temporal para probar la conexión y datos de citas
 */
@WebServlet("/test/citas")
public class TestCitasServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Test Citas</title>");
        out.println("<style>body{font-family:monospace;padding:20px;background:#f0f0f0;}");
        out.println(".success{color:green;} .error{color:red;} .info{color:blue;}</style></head><body>");
        out.println("<h1>🔍 Test de Citas - Base de Datos</h1>");
        out.println("<hr>");
        
        try {
            CitaDAO citaDAO = new CitaDAO();
            out.println("<p class='info'>✅ CitaDAO creado correctamente</p>");
            
            List<Cita> citas = citaDAO.obtenerTodas();
            out.println("<p class='success'>✅ Consulta ejecutada correctamente</p>");
            
            out.println("<h2>Total de citas: " + (citas != null ? citas.size() : 0) + "</h2>");
            
            if (citas != null && !citas.isEmpty()) {
                out.println("<h3>Lista de Citas:</h3>");
                out.println("<table border='1' cellpadding='10' style='border-collapse:collapse;'>");
                out.println("<tr style='background:#001f3f;color:white;'>");
                out.println("<th>ID</th><th>Fecha</th><th>Hora</th><th>Estado</th>");
                out.println("<th>Especialidad</th><th>Doctor</th><th>Estudiante</th><th>Motivo</th></tr>");
                
                for (Cita cita : citas) {
                    out.println("<tr>");
                    out.println("<td>" + cita.getIdCita() + "</td>");
                    out.println("<td>" + cita.getFechaCita() + "</td>");
                    out.println("<td>" + cita.getHoraCita() + "</td>");
                    out.println("<td><strong>" + cita.getEstadoCita() + "</strong></td>");
                    
                    // Especialidad
                    if (cita.getEspecialidad() != null) {
                        out.println("<td>" + cita.getEspecialidad().getTitulo() + "</td>");
                    } else {
                        out.println("<td><em>Sin especialidad</em></td>");
                    }
                    
                    // Doctor
                    if (cita.getDoctor() != null) {
                        out.println("<td>Dr(a). " + cita.getDoctor().getNombre() + " " + 
                                   cita.getDoctor().getApellido() + "</td>");
                    } else {
                        out.println("<td><em>Sin doctor</em></td>");
                    }
                    
                    // Estudiante
                    if (cita.getEstudiante() != null) {
                        out.println("<td>" + cita.getEstudiante().getNombreEstudiante() + " " + 
                                   cita.getEstudiante().getApellidoEstudiante() + "</td>");
                    } else {
                        out.println("<td><em>Sin estudiante</em></td>");
                    }
                    
                    out.println("<td>" + cita.getMotivoConsulta() + "</td>");
                    out.println("</tr>");
                }
                
                out.println("</table>");
            } else {
                out.println("<p class='error'>⚠️ No hay citas en la base de datos</p>");
                out.println("<p>Posibles causas:</p>");
                out.println("<ul>");
                out.println("<li>La tabla 'cita' está vacía</li>");
                out.println("<li>No se han ejecutado los scripts SQL de inserción</li>");
                out.println("<li>La base de datos no está correctamente configurada</li>");
                out.println("</ul>");
            }
            
        } catch (Exception e) {
            out.println("<p class='error'>❌ ERROR: " + e.getMessage() + "</p>");
            out.println("<h3>StackTrace:</h3>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        }
        
        out.println("<hr>");
        out.println("<p><a href='" + request.getContextPath() + "/ConsultarCitasAgendadasController'>Ir a Consultar Citas</a></p>");
        out.println("<p><a href='" + request.getContextPath() + "/'>Volver al inicio</a></p>");
        out.println("</body></html>");
        out.close();
    }
}
