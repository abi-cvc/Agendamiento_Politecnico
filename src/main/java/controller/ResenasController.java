package controller;

import model.dao.DAOFactory;
import model.dao.IEspecialidadDAO;
import model.dao.IEvaluacionDAO;
import model.dao.IDoctorDAO;
import model.entity.Especialidad;
import model.entity.Evaluacion;
import model.entity.Doctor;
import model.entity.Estudiante;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Controller para Reseñas (vista de ESTUDIANTE)
 * 
 * Funcionalidad:
 * - Ver todas las reseñas de otros estudiantes
 * - Crear nuevas reseñas (calificar doctores)
 * - Filtrar reseñas por especialidad
 * 
 * NO INCLUYE:
 * - Estadísticas (eso es para admin)
 * - Edición/eliminación de reseñas
 * - Rankings de doctores
 * 
 * @author Sistema de Agendamiento Politécnico
 */
@WebServlet("/resenas")
public class ResenasController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private DAOFactory factory;
    
    @Override
    public void init() throws ServletException {
        factory = DAOFactory.getFactory();
    }

    /**
     * Maneja las peticiones GET (visualización de reseñas)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        // Si no se especifica acción, cargar página principal
        if (accion == null || accion.isEmpty()) {
            cargarPaginaResenas(request, response);
            return;
        }
        
        // Manejo de acciones GET
        switch (accion) {
            case "filtrarPorEspecialidad":
                filtrarPorEspecialidad(request, response);
                break;
            default:
                cargarPaginaResenas(request, response);
                break;
        }
    }
    
    /**
     * Maneja las peticiones POST (creación de reseñas)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        if (accion == null || accion.isEmpty()) {
            request.getSession().setAttribute("error", "Acción no especificada");
            response.sendRedirect(request.getContextPath() + "/resenas");
            return;
        }
        
        // Solo hay una acción POST: crear reseña
        if ("crear".equals(accion)) {
            crearResena(request, response);
        } else {
            request.getSession().setAttribute("error", "Acción no válida");
            response.sendRedirect(request.getContextPath() + "/resenas");
        }
    }
    
    // ========== MÉTODOS PRINCIPALES ==========
    
    /**
     * Carga la página principal de reseñas
     * Muestra todas las reseñas de todos los estudiantes
     */
    private void cargarPaginaResenas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            System.out.println("📋 Cargando página de reseñas...");
            
            // 1. Cargar especialidades (para el select del formulario)
            IEspecialidadDAO especialidadDAO = factory.getEspecialidadDAO();
            List<Especialidad> especialidades = especialidadDAO.getAll();
            request.setAttribute("especialidades", especialidades);
            
            // 2. Cargar doctores (para el filtrado en cascada en JS)
            IDoctorDAO doctorDAO = factory.getDoctorDAO();
            List<Doctor> doctores = doctorDAO.getAll();
            request.setAttribute("doctores", doctores);
            
            // 3. Cargar TODAS las evaluaciones (de todos los estudiantes)
            IEvaluacionDAO evaluacionDAO = factory.getEvaluacionDAO();
            List<Evaluacion> evaluaciones = evaluacionDAO.getAll();
            request.setAttribute("evaluaciones", evaluaciones);
            request.setAttribute("totalEvaluaciones", evaluaciones.size());
            
            System.out.println("✅ Datos cargados: " + especialidades.size() + " especialidades, " 
                             + doctores.size() + " doctores, " + evaluaciones.size() + " reseñas");
            
            // 4. Forward a la vista JSP
            request.getRequestDispatcher("/resenas.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("❌ Error al cargar página de reseñas: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar datos: " + e.getMessage());
            request.getRequestDispatcher("/resenas.jsp").forward(request, response);
        }
    }
    
    /**
     * Crea una nueva reseña/evaluación
     * Solo estudiantes autenticados pueden crear reseñas
     */
    private void crearResena(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        try {
            System.out.println("📝 Intentando crear nueva reseña...");
            
            // Obtener estudiante desde la sesión
            Estudiante estudiante = (Estudiante) session.getAttribute("usuario");
            System.out.println("👤 Usuario autenticado: " + estudiante.getNombreCompleto());
            
            // ===== PASO 2: OBTENER Y VALIDAR PARÁMETROS =====
            String idDoctorStr = request.getParameter("idDoctor");
            String calificacionStr = request.getParameter("calificacion");
            String comentario = request.getParameter("comentario");
            
            System.out.println("📥 Datos recibidos - Doctor: " + idDoctorStr + 
                             ", Calificación: " + calificacionStr);
            
            // Validar que todos los campos estén presentes
            if (idDoctorStr == null || idDoctorStr.isEmpty() ||
                calificacionStr == null || calificacionStr.isEmpty() ||
                comentario == null || comentario.isEmpty()) {
                
                System.out.println("❌ Validación fallida: campos vacíos");
                session.setAttribute("error", "Todos los campos son requeridos");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            // Parsear valores numéricos
            int idDoctor;
            int calificacion;
            try {
                idDoctor = Integer.parseInt(idDoctorStr);
                calificacion = Integer.parseInt(calificacionStr);
            } catch (NumberFormatException e) {
                System.out.println("❌ Error de formato numérico");
                session.setAttribute("error", "Datos numéricos inválidos");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            // ===== PASO 3: VALIDACIONES DE NEGOCIO =====
            
            // Validar rango de calificación (1-5 estrellas)
            if (calificacion < 1 || calificacion > 5) {
                System.out.println("❌ Calificación fuera de rango: " + calificacion);
                session.setAttribute("error", "La calificación debe ser entre 1 y 5 estrellas");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            // Validar longitud del comentario
            String comentarioLimpio = comentario.trim();
            if (comentarioLimpio.length() < 20) {
                System.out.println("❌ Comentario muy corto: " + comentarioLimpio.length() + " caracteres");
                session.setAttribute("error", "El comentario debe tener al menos 20 caracteres");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            if (comentarioLimpio.length() > 500) {
                System.out.println("❌ Comentario muy largo: " + comentarioLimpio.length() + " caracteres");
                session.setAttribute("error", "El comentario no puede exceder 500 caracteres");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            // ===== PASO 4: VERIFICAR QUE EL DOCTOR EXISTE =====
            Doctor doctor = factory.getDoctorDAO().getById(idDoctor);
            if (doctor == null) {
                System.out.println("❌ Doctor no encontrado: ID " + idDoctor);
                session.setAttribute("error", "El doctor seleccionado no existe");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            System.out.println("✅ Doctor encontrado: Dr. " + doctor.getNombreCompleto());
            
            // ===== PASO 5: CREAR LA EVALUACIÓN =====
            Evaluacion evaluacion = new Evaluacion();
            evaluacion.setCalificacion(calificacion);
            evaluacion.setComentario(comentarioLimpio);
            evaluacion.setFechaEvaluacion(LocalDateTime.now());
            evaluacion.setDoctor(doctor);
            evaluacion.setEstudiante(estudiante);
            // cita es opcional, por ahora no lo manejamos
            
            // ===== PASO 6: GUARDAR EN BASE DE DATOS =====
            IEvaluacionDAO evaluacionDAO = factory.getEvaluacionDAO();
            Evaluacion evaluacionGuardada = evaluacionDAO.create(evaluacion);
            
            System.out.println("✅ Reseña guardada exitosamente - ID: " + evaluacionGuardada.getIdEvaluacion());
            
            // ===== PASO 7: RESPUESTA AL USUARIO =====
            session.setAttribute("mensaje", "¡Reseña publicada exitosamente! Gracias por compartir tu experiencia.");
            response.sendRedirect(request.getContextPath() + "/resenas");
            
        } catch (NumberFormatException e) {
            System.err.println("❌ Error de formato numérico: " + e.getMessage());
            session.setAttribute("error", "Los datos numéricos son inválidos");
            response.sendRedirect(request.getContextPath() + "/resenas");
            
        } catch (Exception e) {
            System.err.println("❌ Error inesperado al guardar reseña: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", "Error al guardar la reseña. Por favor, intenta nuevamente.");
            response.sendRedirect(request.getContextPath() + "/resenas");
        }
    }
    
    /**
     * Filtra las reseñas por especialidad
     * Muestra solo las reseñas de doctores de una especialidad específica
     */
    private void filtrarPorEspecialidad(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            System.out.println("🔍 Filtrando reseñas por especialidad...");
            
            String idEspecialidadStr = request.getParameter("idEspecialidad");
            
            // 1. Cargar especialidades y doctores (necesarios para los selects)
            IEspecialidadDAO especialidadDAO = factory.getEspecialidadDAO();
            List<Especialidad> especialidades = especialidadDAO.getAll();
            request.setAttribute("especialidades", especialidades);
            
            IDoctorDAO doctorDAO = factory.getDoctorDAO();
            List<Doctor> doctores = doctorDAO.getAll();
            request.setAttribute("doctores", doctores);
            
            // 2. Filtrar evaluaciones según la especialidad
            IEvaluacionDAO evaluacionDAO = factory.getEvaluacionDAO();
            List<Evaluacion> evaluaciones;
            
            // Si hay especialidad seleccionada y no es "todas"
            if (idEspecialidadStr != null && !idEspecialidadStr.isEmpty() 
                && !"todas".equals(idEspecialidadStr) && !"".equals(idEspecialidadStr)) {
                
                int idEspecialidad = Integer.parseInt(idEspecialidadStr);
                System.out.println("📌 Filtrando por especialidad ID: " + idEspecialidad);
                
                evaluaciones = evaluacionDAO.getByEspecialidad(idEspecialidad);
                
                // Buscar la especialidad seleccionada para mostrar info
                Especialidad especialidadSeleccionada = null;
                for (Especialidad esp : especialidades) {
                    if (esp.getIdEspecialidad() == idEspecialidad) {
                        especialidadSeleccionada = esp;
                        break;
                    }
                }
                
                request.setAttribute("especialidadSeleccionada", especialidadSeleccionada);
                System.out.println("✅ Filtrado: " + evaluaciones.size() + " reseñas encontradas");
                
            } else {
                // Mostrar todas las evaluaciones
                System.out.println("📌 Mostrando todas las reseñas");
                evaluaciones = evaluacionDAO.getAll();
            }
            
            // 3. Enviar datos a la vista
            request.setAttribute("evaluaciones", evaluaciones);
            request.setAttribute("totalEvaluaciones", evaluaciones.size());
            request.setAttribute("filtroActivo", idEspecialidadStr);
            
            // 4. Forward a la vista JSP
            request.getRequestDispatcher("/resenas.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            System.err.println("❌ Error en ID de especialidad: " + e.getMessage());
            request.getSession().setAttribute("error", "ID de especialidad inválido");
            response.sendRedirect(request.getContextPath() + "/resenas");
            
        } catch (Exception e) {
            System.err.println("❌ Error al filtrar: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al filtrar reseñas: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/resenas");
        }
    }
}