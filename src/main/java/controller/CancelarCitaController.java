package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.DAOFactory;
import model.dao.ICitaDAO;
import model.dao.IDisponibilidadDAO;
import model.entity.Cita;
import model.entity.Disponibilidad;

import java.io.IOException;

/**
 * CancelarCitaController - Según diagrama de robustez
 * Maneja la cancelación de citas médicas
 * ACTUALIZADO: Usa DAOFactory para obtener instancias de DAOs
 * 
 * FLUJO SEGÚN DIAGRAMA:
 * 1: cancelarCita(idCita)
 * 2: mostrarConfirmacion
 * 3: confirmarCancelacion(idCita)
 * 4: actualizarEstadoCita(idCita)
 * 5: liberarHorario(idHorario)
 * 6: mostrarExitoCancelar
 * 7: actualizarVista
 */
@WebServlet("/CancelarCitaController")
public class CancelarCitaController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private DAOFactory factory;
    
    @Override
    public void init() throws ServletException {
        factory = DAOFactory.getFactory();
        System.out.println("✅ CancelarCitaController inicializado con Factory");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("\n=== INICIO: CancelarCitaController ===");
        
        // Obtener parámetros
        String idCitaParam = request.getParameter("idCita");
        String confirmar = request.getParameter("confirmar");
        String vieneDesde = request.getParameter("from"); // "atender" o "consultar"
        
        if (idCitaParam == null || idCitaParam.isEmpty()) {
            enviarError(request, response, "ID de cita no válido", vieneDesde);
            return;
        }
        
        try {
            int idCita = Integer.parseInt(idCitaParam);
            
            // ===== 1 y 2. MOSTRAR CONFIRMACIÓN (se hace con JavaScript en el cliente) =====
            
            // ===== 3. CONFIRMAR CANCELACIÓN =====
            if ("true".equals(confirmar)) {
                System.out.println("📋 Procesando cancelación de cita ID: " + idCita);
                
                // Obtener la cita usando Factory
                Cita cita = factory.getCitaDAO().getById(idCita);
                
                if (cita == null) {
                    enviarError(request, response, "Cita no encontrada", vieneDesde);
                    return;
                }
                
                // Verificar que la cita se pueda cancelar
                if ("Cancelada".equals(cita.getEstadoCita())) {
                    enviarError(request, response, "La cita ya está cancelada", vieneDesde);
                    return;
                }
                
                if ("Completada".equals(cita.getEstadoCita())) {
                    enviarError(request, response, "No se puede cancelar una cita completada", vieneDesde);
                    return;
                }
                
                // ===== 4. ACTUALIZAR ESTADO DE LA CITA =====
                String estadoAnterior = cita.getEstadoCita();
                cita.setEstadoCita("Cancelada");
                cita.setObservacionCita(
                    (cita.getObservacionCita() != null ? cita.getObservacionCita() + "\n" : "") +
                    "Cita cancelada. Estado anterior: " + estadoAnterior
                );
                
                factory.getCitaDAO().update(cita);
                System.out.println("✅ Estado de cita actualizado a: Cancelada");
                
                // ===== 5. LIBERAR HORARIO (si existe disponibilidad asociada) =====
                if (cita.getDoctor() != null) {
                    try {
                        Disponibilidad disponibilidad = factory.getDisponibilidadDAO().obtenerPorDoctorYFechaYHora(
                            cita.getDoctor().getIdDoctor(),
                            cita.getFechaCita(),
                            cita.getHoraCita()
                        );
                        
                        if (disponibilidad != null) {
                            disponibilidad.setDisponible(true);
                            factory.getDisponibilidadDAO().update(disponibilidad);
                            System.out.println("✅ Horario liberado: " + cita.getFechaCita() + " " + cita.getHoraCita());
                        }
                    } catch (Exception e) {
                        // No es crítico si no se puede liberar el horario
                        System.out.println("⚠️ No se pudo liberar el horario: " + e.getMessage());
                    }
                }
                
                // ===== 6. MOSTRAR ÉXITO Y 7. ACTUALIZAR VISTA =====
                System.out.println("✅ Cita cancelada exitosamente");
                System.out.println("=== FIN: CancelarCitaController ===\n");
                
                // Redirigir a la página correspondiente con mensaje de éxito
                String redirectUrl = determinarRedirect(vieneDesde, request);
                response.sendRedirect(redirectUrl + "?success=cancelada");
                
            } else {
                // Solo mostrar confirmación (esto se maneja en el cliente)
                response.sendRedirect(request.getContextPath() + "/consultar-citas.jsp");
            }
            
        } catch (NumberFormatException e) {
            enviarError(request, response, "ID de cita inválido", vieneDesde);
        } catch (Exception e) {
            System.err.println("❌ ERROR en CancelarCitaController:");
            e.printStackTrace();
            enviarError(request, response, "Error al cancelar la cita: " + e.getMessage(), vieneDesde);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GET redirige al POST para mantener consistencia
        doPost(request, response);
    }
    
    /**
     * Envía un mensaje de error y redirige
     */
    private void enviarError(HttpServletRequest request, HttpServletResponse response, 
                            String mensaje, String vieneDesde) throws IOException {
        System.err.println("❌ Error: " + mensaje);
        String redirectUrl = determinarRedirect(vieneDesde, request);
        response.sendRedirect(redirectUrl + "?error=" + java.net.URLEncoder.encode(mensaje, "UTF-8"));
    }
    
    /**
     * Determina a qué página redirigir según el origen
     */
    private String determinarRedirect(String vieneDesde, HttpServletRequest request) {
        String contextPath = request.getContextPath();
        
        if ("atender".equals(vieneDesde)) {
            return contextPath + "/ConsultarCitaAsignadaController";
        } else {
            return contextPath + "/ConsultarCitasAgendadasController";
        }
    }
}
