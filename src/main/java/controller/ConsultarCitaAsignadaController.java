package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.DAOFactory;
import model.dao.ICitaDAO;
import model.entity.Cita;
import model.entity.Doctor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * ConsultarCitaAsignadaController - Según diagrama de robustez
 * Maneja las citas asignadas a un doctor
 * ACTUALIZADO: Usa DAOFactory para obtener instancias de DAOs
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
    
    private DAOFactory factory;
    
    @Override
    public void init() throws ServletException {
        factory = DAOFactory.getFactory();
        System.out.println("✅ ConsultarCitaAsignadaController inicializado con Factory");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("\n=== INICIO: ConsultarCitaAsignadaController ===");
        System.out.println("⚠️ MODO SIN LOGIN: Mostrando TODAS las citas del sistema");
        
        // ===== SIN FILTRO DE DOCTOR (mientras no hay login) =====
        String nombreDoctor = "Panel de Atención de Citas";
        
        // Determinar qué vista mostrar
        String vista = request.getParameter("vista");
        boolean esCalendario = "calendario".equals(vista);
        
        System.out.println("📄 Vista solicitada: " + (esCalendario ? "Calendario" : "Atender Cita"));
        
        try {
            // Obtener parámetros de fecha
            String fechaParam = request.getParameter("fecha");
            String mesParam = request.getParameter("mes");
            String anioParam = request.getParameter("anio");
            
            LocalDate fechaSeleccionada;
            YearMonth mesActual;
            
            // Determinar fecha y mes según parámetros
            if (esCalendario && mesParam != null && anioParam != null) {
                // Modo calendario con mes específico
                mesActual = YearMonth.of(Integer.parseInt(anioParam), Integer.parseInt(mesParam));
                fechaSeleccionada = mesActual.atDay(1);
            } else if (fechaParam != null && !fechaParam.isEmpty()) {
                // Fecha específica proporcionada
                fechaSeleccionada = LocalDate.parse(fechaParam);
                mesActual = YearMonth.from(fechaSeleccionada);
            } else {
                // Usar fecha actual
                fechaSeleccionada = LocalDate.now();
                mesActual = YearMonth.from(fechaSeleccionada);
            }
            
            System.out.println("📅 Fecha seleccionada: " + fechaSeleccionada);
            System.out.println("📅 Mes actual: " + mesActual);
            System.out.println("📅 Mes número: " + mesActual.getMonthValue());
            System.out.println("📅 Año: " + mesActual.getYear());
            
            // ===== 2. OBTENER CITAS DEL MES SEGÚN DIAGRAMA DE ROBUSTEZ =====
            // 2: obtenerCitasAgendadasDoctorMes(idDoctor): citasMes[]
            
            if (esCalendario) {
                // ===== MODO CALENDARIO: Obtener citas del doctor ID 9 directamente =====
                System.out.println("\n🔍 Consultando citas del doctor ID 9 en la BD para el mes " + mesActual + "...");
                System.out.println("📅 Primer día del mes: " + mesActual.atDay(1));
                System.out.println("📅 Último día del mes: " + mesActual.atEndOfMonth());
                
                // Usar el método del DAO que filtra por doctor y mes - USANDO FACTORY
                List<Cita> citasDoctor = factory.getCitaDAO().obtenerPorDoctorYMes(9, mesActual);
                
                System.out.println("📊 Total citas del doctor ID 9 en " + mesActual + ": " + citasDoctor.size());
                
                if (citasDoctor.isEmpty()) {
                    System.out.println("⚠️ WARNING: No se encontraron citas para el doctor ID 9 en " + mesActual);
                    System.out.println("⚠️ Verifica que existan registros en la tabla 'cita' para este doctor y mes");
                } else {
                    System.out.println("✅ Citas del doctor encontradas:");
                    for (int i = 0; i < Math.min(3, citasDoctor.size()); i++) {
                        Cita c = citasDoctor.get(i);
                        System.out.println("   - Cita #" + c.getIdCita() + ": " + c.getFechaCita() + " " + c.getHoraCita() + " - " + c.getEstadoCita());
                    }
                    if (citasDoctor.size() > 3) {
                        System.out.println("   ... y " + (citasDoctor.size() - 3) + " más");
                    }
                }
                
                // ===== 3. MOSTRAR (citasMes) =====
                // Primero agregar las citas al request (ANTES del JSON)
                request.setAttribute("citasMes", citasDoctor);
                request.setAttribute("nombreDoctor", "Dr. Enf. Sofía Morales (ID: 9)");
                request.setAttribute("mesActual", mesActual);
                
                // Intentar convertir citas a JSON (opcional)
                String citasMesJson = "[]";
                try {
                    com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .excludeFieldsWithoutExposeAnnotation() // Evitar relaciones circulares
                        .create();
                    citasMesJson = gson.toJson(citasDoctor);
                } catch (Exception jsonEx) {
                    System.err.println("⚠️ WARNING: Error al convertir citas a JSON: " + jsonEx.getMessage());
                    // Continuar sin JSON
                }
                request.setAttribute("citasMesJson", citasMesJson);
                
                System.out.println("✅ Datos agregados al request para doctor ID 9");
                System.out.println("   - citasMes size: " + citasDoctor.size());
                System.out.println("   - nombreDoctor: Dr. Enf. Sofía Morales (ID: 9)");
                System.out.println("   - mesActual: " + mesActual);
                System.out.println("   - Verificando atributos en request:");
                System.out.println("     * citasMes != null: " + (request.getAttribute("citasMes") != null));
                System.out.println("     * nombreDoctor != null: " + (request.getAttribute("nombreDoctor") != null));
                System.out.println("➡️ Forward a /citas-agendadas.jsp");
                
                request.getRequestDispatcher("/citas-agendadas.jsp").forward(request, response);
                
            } else {
                // ===== MODO ATENDER CITA: Obtener citas del día =====
                // ===== 5. OBTENER TODAS LAS CITAS DEL DÍA =====
                System.out.println("\n🔍 Consultando citas del día " + fechaSeleccionada + " en la BD...");
                List<Cita> citasDia = factory.getCitaDAO().obtenerPorFecha(fechaSeleccionada);
                System.out.println("📊 Total citas del día " + fechaSeleccionada + ": " + citasDia.size());
                
                // ===== 6. ORM CARGA AUTOMÁTICAMENTE =====
                // obtenerNombreEstudiante(idEstudiante) se carga con FetchType.EAGER
                // obtenerNombreDoctor(idDoctor) se carga con FetchType.EAGER
                
                // ===== 7. PASAR DATOS AL JSP =====
                request.setAttribute("citas", citasDia);
                request.setAttribute("fechaSeleccionada", fechaSeleccionada);
                request.setAttribute("fechaSeleccionadaDate", java.sql.Date.valueOf(fechaSeleccionada)); // Para fmt:formatDate
                request.setAttribute("nombreDoctor", nombreDoctor);
                
                System.out.println("✅ Datos agregados al request");
                System.out.println("➡️ Forward a /atender-cita.jsp");
                
                request.getRequestDispatcher("/atender-cita.jsp").forward(request, response);
            }
            
            System.out.println("=== FIN: ConsultarCitaAsignadaController ===\n");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR en ConsultarCitaAsignadaController:");
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("error", "Error al cargar las citas: " + e.getMessage());
            request.setAttribute("citas", null);
            
            String jspDestino = esCalendario ? "/citas-agendadas.jsp" : "/atender-cita.jsp";
            request.getRequestDispatcher(jspDestino).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
