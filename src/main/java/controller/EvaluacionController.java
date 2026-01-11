package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.DAOFactory;
import model.entity.Evaluacion;
import model.entity.Doctor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller para manejar las peticiones relacionadas con Evaluaciones
 * El administrador puede:
 * - Listar todas las evaluaciones
 * - Filtrar evaluaciones por doctor
 * - Ver estadísticas de doctores
 * - Generar reportes
 */
@WebServlet("/evaluaciones")
public class EvaluacionController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DAOFactory factory;
    
    @Override
    public void init() throws ServletException {
        factory = DAOFactory.getFactory();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        if (accion == null) {
            accion = "listar";
        }
        
        switch (accion) {
            case "listar":
                listarEvaluaciones(request, response);
                break;
            case "listarAdmin":
                listarEvaluacionesAdmin(request, response);
                break;
            case "porDoctor":
                listarPorDoctor(request, response);
                break;
            case "mejoresCalificados":
                mostrarMejoresCalificados(request, response);
                break;
            case "reporteDoctor":
                generarReporteDoctor(request, response);
                break;
            default:
                listarEvaluaciones(request, response);
                break;
        }
    }
    
    /**
     * Lista todas las evaluaciones (para vista pública)
     */
    private void listarEvaluaciones(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Evaluacion> evaluaciones = factory.getEvaluacionDAO().getAll();
        request.setAttribute("evaluaciones", evaluaciones);
        request.getRequestDispatcher("/views/evaluaciones.jsp").forward(request, response);
    }
    
    /**
     * Lista todas las evaluaciones para el panel de administrador
     */
    private void listarEvaluacionesAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Evaluacion> evaluaciones = factory.getEvaluacionDAO().getAll();
        List<Doctor> doctores = factory.getDoctorDAO().getAll();
        
        request.setAttribute("doctores", doctores);
        request.setAttribute("evaluaciones", evaluaciones);
        
        request.getRequestDispatcher("/views/admin/gestionarEvaluaciones.jsp").forward(request, response);
    }
    
    /**
     * Lista evaluaciones filtradas por doctor
     */
    private void listarPorDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idDoctorStr = request.getParameter("idDoctor");
        
        if (idDoctorStr != null && !idDoctorStr.isEmpty()) {
            try {
                int idDoctor = Integer.parseInt(idDoctorStr);
                
                // Obtener doctor
                Doctor doctor = factory.getDoctorDAO().getById(idDoctor);
                
                // Obtener evaluaciones
                List<Evaluacion> evaluaciones = factory.getEvaluacionDAO().obtenerPorDoctor(idDoctor);
                
                // Obtener estadísticas
                Map<String, Object> estadisticas = factory.getEvaluacionDAO().obtenerEstadisticasDoctor(idDoctor);
                
                // Enviar datos a la vista
                request.setAttribute("doctor", doctor);
                request.setAttribute("evaluaciones", evaluaciones);
                request.setAttribute("estadisticas", estadisticas);
                
                request.getRequestDispatcher("/views/admin/evaluaciones-por-doctor.jsp").forward(request, response);
                
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de doctor inválido");
            }
        } else {
            response.sendRedirect("evaluaciones?accion=listarAdmin");
        }
    }
    
    /**
     * Muestra página con los doctores mejor calificados
     */
    private void mostrarMejoresCalificados(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String limiteStr = request.getParameter("limite");
        int limite = 10; // Por defecto 10
        
        if (limiteStr != null && !limiteStr.isEmpty()) {
            try {
                limite = Integer.parseInt(limiteStr);
            } catch (NumberFormatException e) {
                limite = 10;
            }
        }
        
        List<Map<String, Object>> mejoresCalificados = 
            factory.getEvaluacionDAO().obtenerDoctoresMejorCalificados(limite);
        
        request.setAttribute("mejoresCalificados", mejoresCalificados);
        request.getRequestDispatcher("/views/admin/mejores-calificados.jsp").forward(request, response);
    }
    
    /**
     * Genera un reporte completo de un doctor
     */
    private void generarReporteDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idDoctorStr = request.getParameter("idDoctor");
        
        if (idDoctorStr != null && !idDoctorStr.isEmpty()) {
            try {
                int idDoctor = Integer.parseInt(idDoctorStr);
                
                // Obtener doctor
                Doctor doctor = factory.getDoctorDAO().getById(idDoctor);
                
                // Obtener evaluaciones
                List<Evaluacion> evaluaciones = factory.getEvaluacionDAO().obtenerPorDoctor(idDoctor);
                
                // Obtener estadísticas
                Map<String, Object> estadisticas = factory.getEvaluacionDAO().obtenerEstadisticasDoctor(idDoctor);
                
                // Calcular datos adicionales
                double promedio = (Double) estadisticas.get("promedio");
                long total = (Long) estadisticas.get("total");
                
                // Determinar estado del doctor
                String estado;
                if (promedio >= 4.5) {
                    estado = "EXCELENTE";
                } else if (promedio >= 4.0) {
                    estado = "MUY BUENO";
                } else if (promedio >= 3.5) {
                    estado = "BUENO";
                } else if (promedio >= 3.0) {
                    estado = "REGULAR";
                } else {
                    estado = "REQUIERE ATENCIÓN";
                }
                
                // Enviar datos a la vista
                request.setAttribute("doctor", doctor);
                request.setAttribute("evaluaciones", evaluaciones);
                request.setAttribute("estadisticas", estadisticas);
                request.setAttribute("estado", estado);
                request.setAttribute("promedio", promedio);
                request.setAttribute("total", total);
                
                request.getRequestDispatcher("/views/admin/reporte-doctor.jsp").forward(request, response);
                
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de doctor inválido");
            }
        } else {
            response.sendRedirect("evaluaciones?accion=listarAdmin");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // El administrador NO crea ni edita evaluaciones
        // Solo las consulta
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, 
            "El administrador no puede crear o editar evaluaciones");
    }
}