package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.CitaDAO;
import model.entity.Cita;
import model.entity.Doctor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * ConsultarCitaAsignadaController - Según diagrama de robustez
 * Maneja las citas asignadas a un doctor
 * 
 * FLUJO SEGÚN DIAGRAMA:
 * 1: consultarCitasAgendadasMes(idDoctor) + seleccionarDiaMes(fecha)
 * 2: obtenerCitasAgendadasDoctorMes(idDoctor): citasMes[]
 * 3: mostrar(citasMes)
 * 4: seleccionarDiaMes(fecha)
 * 5: obtenerCitasDoctorDia(fechaActual): citasDia[]
 * 6: obtenerNombreEstudiante(idEstudiante) - Cargado por ORM EAGER
 * 7: mostrar(citasDiaDetallada)
 */
@WebServlet("/ConsultarCitaAsignadaController")
public class ConsultarCitaAsignadaController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private CitaDAO citaDAO;
    
    @Override
    public void init() throws ServletException {
        citaDAO = new CitaDAO();
        System.out.println("✅ ConsultarCitaAsignadaController inicializado");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("\n=== INICIO: ConsultarCitaAsignadaController ===");
        System.out.println("⚠️ MODO SIN LOGIN: Mostrando TODAS las citas del sistema");
        
        // ===== SIN FILTRO DE DOCTOR (mientras no hay login) =====
        String nombreDoctor = "Panel de Atención de Citas";
        
        try {
            // Obtener parámetros de fecha
            String fechaParam = request.getParameter("fecha");
            LocalDate fechaSeleccionada = (fechaParam != null && !fechaParam.isEmpty()) 
                ? LocalDate.parse(fechaParam) 
                : LocalDate.now();
            
            System.out.println("📅 Fecha seleccionada: " + fechaSeleccionada);
            
            // ===== 2 y 5. OBTENER TODAS LAS CITAS DEL DÍA =====
            List<Cita> citasDia = citaDAO.obtenerPorFecha(fechaSeleccionada);
            System.out.println("📊 Total citas del día " + fechaSeleccionada + ": " + citasDia.size());
            
            // ===== 6. ORM CARGA AUTOMÁTICAMENTE =====
            // obtenerNombreEstudiante(idEstudiante) se carga con FetchType.EAGER
            // obtenerNombreDoctor(idDoctor) se carga con FetchType.EAGER
            
            // Calcular estadísticas del mes
            YearMonth mesActual = YearMonth.from(fechaSeleccionada);
            List<Cita> citasMes = citaDAO.obtenerPorMes(mesActual);
            
            System.out.println("📊 Total citas del mes: " + citasMes.size());
            
            // ===== 7. PASAR DATOS AL JSP =====
            request.setAttribute("citas", citasDia);
            request.setAttribute("citasMes", citasMes);
            request.setAttribute("fechaSeleccionada", fechaSeleccionada);
            request.setAttribute("fechaSeleccionadaDate", java.sql.Date.valueOf(fechaSeleccionada)); // Para fmt:formatDate
            request.setAttribute("nombreDoctor", nombreDoctor);
            
            System.out.println("✅ Datos agregados al request");
            System.out.println("➡️ Forward a /atender-cita.jsp");
            
            request.getRequestDispatcher("/atender-cita.jsp").forward(request, response);
            
            System.out.println("=== FIN: ConsultarCitaAsignadaController ===\n");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR en ConsultarCitaAsignadaController:");
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("error", "Error al cargar las citas: " + e.getMessage());
            request.setAttribute("citas", null);
            request.getRequestDispatcher("/atender-cita.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
