package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CitaDAO;
import model.entity.Cita;

import java.io.IOException;

/**
 * AtenderCitaController - Según diagrama de robustez
 * Maneja el proceso de atender una cita médica
 * 
 * FLUJO SEGÚN DIAGRAMA:
 * 1: atenderCita
 * 5: agregarObservacionCita(observacion)
 * 6: mostrarConfirmacion
 * 7: confirmarAgregarObservacion
 * 8: guardarObservacion(observacion, "Atendida")
 * 9: actualizarVista(citasDia)
 */
@WebServlet("/AtenderCitaController")
public class AtenderCitaController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private CitaDAO citaDAO;
    
    @Override
    public void init() throws ServletException {
        citaDAO = new CitaDAO();
        System.out.println("✅ AtenderCitaController inicializado");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("\n=== INICIO: AtenderCitaController ===");
        
        // Obtener parámetros
        String idCitaParam = request.getParameter("idCita");
        String observacion = request.getParameter("observacion");
        String confirmar = request.getParameter("confirmar");
        
        if (idCitaParam == null || idCitaParam.isEmpty()) {
            enviarError(request, response, "ID de cita no válido");
            return;
        }
        
        try {
            int idCita = Integer.parseInt(idCitaParam);
            
            // ===== 1. ATENDER CITA =====
            Cita cita = citaDAO.obtenerPorId(idCita);
            
            if (cita == null) {
                enviarError(request, response, "Cita no encontrada");
                return;
            }
            
            // Verificar que la cita se pueda atender
            if ("Completada".equals(cita.getEstadoCita())) {
                enviarError(request, response, "La cita ya fue atendida");
                return;
            }
            
            if ("Cancelada".equals(cita.getEstadoCita())) {
                enviarError(request, response, "No se puede atender una cita cancelada");
                return;
            }
            
            // ===== 5 y 6. AGREGAR OBSERVACIÓN Y MOSTRAR CONFIRMACIÓN =====
            if (observacion == null || observacion.trim().isEmpty()) {
                // Redirigir a página con modal para ingresar observación
                System.out.println("⚠️ Sin observación - Debe ingresar observaciones");
                enviarError(request, response, "Debe ingresar observaciones para atender la cita");
                return;
            }
            
            // ===== 7 y 8. CONFIRMAR Y GUARDAR OBSERVACIÓN =====
            if ("true".equals(confirmar)) {
                System.out.println("📋 Atendiendo cita ID: " + idCita);
                System.out.println("📝 Observación: " + observacion);
                
                // Guardar observación y cambiar estado a "Completada"
                String observacionAnterior = cita.getObservacionCita();
                cita.setObservacionCita(
                    (observacionAnterior != null ? observacionAnterior + "\n\n" : "") +
                    "=== ATENCIÓN MÉDICA ===\n" + observacion
                );
                cita.setEstadoCita("Completada");
                
                // Actualizar en BD usando ORM
                citaDAO.actualizar(cita);
                
                System.out.println("✅ Cita atendida exitosamente");
                System.out.println("✅ Estado actualizado a: Completada");
                System.out.println("=== FIN: AtenderCitaController ===\n");
                
                // ===== 9. ACTUALIZAR VISTA =====
                response.sendRedirect(request.getContextPath() + 
                    "/ConsultarCitaAsignadaController?success=atendida");
            } else {
                // Redirigir para confirmar
                response.sendRedirect(request.getContextPath() + 
                    "/ConsultarCitaAsignadaController");
            }
            
        } catch (NumberFormatException e) {
            enviarError(request, response, "ID de cita inválido");
        } catch (Exception e) {
            System.err.println("❌ ERROR en AtenderCitaController:");
            e.printStackTrace();
            enviarError(request, response, "Error al atender la cita: " + e.getMessage());
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GET redirige al POST
        doPost(request, response);
    }
    
    /**
     * Envía un mensaje de error y redirige
     */
    private void enviarError(HttpServletRequest request, HttpServletResponse response, 
                            String mensaje) throws IOException {
        System.err.println("❌ Error: " + mensaje);
        response.sendRedirect(request.getContextPath() + 
            "/ConsultarCitaAsignadaController?error=" + 
            java.net.URLEncoder.encode(mensaje, "UTF-8"));
    }
}
