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

import java.io.IOException;
import java.util.List;

/**
 * ConsultarCitasAgendadasController - Según diagrama de robustez
 * Maneja la consulta de citas agendadas por estudiante
 * ACTUALIZADO: Usa DAOFactory para obtener instancias de DAOs
 * 
 * FLUJO SEGÚN DIAGRAMA:
 * 1: consultarCitasAgendadas(idEstudiante)
 * 2: obtenerCitasPorEstudiante(idEstudiante): citasAgendadas[]
 * 3: obtenerNombreDoctor(idDoctor) - Cargado por ORM con EAGER
 * 4: obtenerNombreEspecialidad(idEspecialidad) - Cargado por ORM con EAGER
 * 5: mostrar(citasAgendadasDetalladas) - Forward al JSP
 */
@WebServlet("/ConsultarCitasAgendadasController")
public class ConsultarCitasAgendadasController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private DAOFactory factory;
    
    @Override
    public void init() throws ServletException {
        factory = DAOFactory.getFactory();
        System.out.println("✅ ConsultarCitasAgendadasController inicializado con Factory");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("\n=== INICIO: ConsultarCitasAgendadasController ===");
        
        // ===== 1. VERIFICAR SESIÓN =====
        HttpSession session = request.getSession(false);
        
        // TEMPORAL: Permitir acceso sin sesión para debugging
        boolean modoDebug = true; // Cambia a false en producción
        
        if (!modoDebug) {
            if (session == null || session.getAttribute("usuario") == null) {
                System.out.println("❌ Sin sesión - Redirigiendo a login");
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }
        } else {
            System.out.println("⚠️ MODO DEBUG: Sesión no requerida");
        }
        
        try {
            // ===== 2. OBTENER TODAS LAS CITAS (TEMPORAL) =====
            // TODO: Filtrar por estudiante cuando esté en producción
            System.out.println("📊 Consultando citas en la base de datos...");
            
            List<Cita> citasAgendadas = factory.getCitaDAO().getAll();
            
            // ===== 3 y 4. ORM CARGA AUTOMÁTICAMENTE =====
            // Las relaciones Doctor y Especialidad se cargan con FetchType.EAGER
            // No se necesita hacer consultas adicionales
            
            System.out.println("✅ Citas obtenidas: " + (citasAgendadas != null ? citasAgendadas.size() : 0));
            
            if (citasAgendadas != null && !citasAgendadas.isEmpty()) {
                System.out.println("📋 Detalle de la primera cita:");
                Cita primera = citasAgendadas.get(0);
                System.out.println("   - ID: " + primera.getIdCita());
                System.out.println("   - Fecha: " + primera.getFechaCita());
                System.out.println("   - Estado: " + primera.getEstadoCita());
                System.out.println("   - Especialidad: " + (primera.getEspecialidad() != null ? 
                    primera.getEspecialidad().getTitulo() : "NULL"));
                System.out.println("   - Doctor: " + (primera.getDoctor() != null ? 
                    primera.getDoctor().getNombre() + " " + primera.getDoctor().getApellido() : "NULL"));
            } else {
                System.out.println("⚠️ No hay citas en la base de datos");
            }
            
            // ===== 5. PASAR DATOS AL JSP Y HACER FORWARD =====
            request.setAttribute("citas", citasAgendadas);
            System.out.println("✅ Atributo 'citas' agregado al request");
            
            System.out.println("➡️ Haciendo forward a /consultar-citas.jsp");
            request.getRequestDispatcher("/consultar-citas.jsp").forward(request, response);
            
            System.out.println("=== FIN: ConsultarCitasAgendadasController ===\n");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR en ConsultarCitasAgendadasController:");
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            
            // Mostrar error al usuario
            request.setAttribute("error", "Error al cargar las citas: " + e.getMessage());
            request.setAttribute("citas", null);
            request.getRequestDispatcher("/consultar-citas.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
