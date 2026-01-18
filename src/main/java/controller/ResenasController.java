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
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Controller para Reseñas (vista de ESTUDIANTE)
 */
@WebServlet("/resenas")
public class ResenasController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private DAOFactory factory;
    
    // Formatter para fechas en español
    private static final DateTimeFormatter FECHA_FORMATTER = 
        DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "EC"));
    
    @Override
    public void init() throws ServletException {
        factory = DAOFactory.getFactory();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        if (accion == null || accion.isEmpty()) {
            cargarPaginaResenas(request, response);
            return;
        }
        
        switch (accion) {
            case "filtrarPorEspecialidad":
                filtrarPorEspecialidad(request, response);
                break;
            default:
                cargarPaginaResenas(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        if (accion == null || accion.isEmpty()) {
            request.getSession().setAttribute("error", "Acción no especificada");
            response.sendRedirect(request.getContextPath() + "/resenas");
            return;
        }
        
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
     * IGUAL que AgendarCitasController.solicitarCita()
     */
    private void cargarPaginaResenas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            System.out.println("📋 Cargando página de reseñas...");
            
            // Obtener estudiante de la sesión
            HttpSession session = request.getSession(false);
            
            // DEBUG: Verificar sesión
            if (session == null) {
                System.out.println("❌ No hay sesión activa");
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }
            
            Estudiante estudiante = (Estudiante) session.getAttribute("usuario");
            
            // DEBUG: Verificar usuario
            if (estudiante == null) {
                System.out.println("❌ No hay usuario en sesión");
                System.out.println("Atributos disponibles en sesión:");
                java.util.Enumeration<String> attrs = session.getAttributeNames();
                while (attrs.hasMoreElements()) {
                    String attr = attrs.nextElement();
                    System.out.println("  - " + attr + " = " + session.getAttribute(attr));
                }
                
                // CAMBIO: Cargar datos sin filtro por estudiante temporalmente
                System.out.println("⚠️ Cargando reseñas SIN filtro de estudiante (temporal)");
            }
            
            // 1. Cargar especialidades
            IEspecialidadDAO especialidadDAO = factory.getEspecialidadDAO();
            List<Especialidad> especialidades = especialidadDAO.getAll();
            request.setAttribute("especialidades", especialidades);
            System.out.println("✅ Especialidades cargadas: " + especialidades.size());
            
            // 2. Cargar TODOS los doctores (para el filtrado en cascada)
            IDoctorDAO doctorDAO = factory.getDoctorDAO();
            List<Doctor> doctores = doctorDAO.getAll();
            request.setAttribute("doctores", doctores);
            System.out.println("✅ Doctores cargados: " + doctores.size());
            
            // 3. Cargar evaluaciones (CON o SIN filtro según autenticación)
            IEvaluacionDAO evaluacionDAO = factory.getEvaluacionDAO();
            List<Evaluacion> evaluaciones;
            
            if (estudiante != null) {
                // FILTRAR: Solo reseñas del estudiante
                evaluaciones = evaluacionDAO.getByEstudiante(estudiante.getIdEstudiante());
                System.out.println("✅ Evaluaciones del estudiante ID " + estudiante.getIdEstudiante() + ": " + evaluaciones.size());
            } else {
                // TEMPORAL: Cargar TODAS las reseñas si no hay autenticación
                evaluaciones = evaluacionDAO.getAll();
                System.out.println("⚠️ Cargando TODAS las evaluaciones (sin filtro): " + evaluaciones.size());
            }
            
            // 4. FORMATEAR FECHAS
            Map<Integer, String> fechasFormateadas = new HashMap<>();
            for (Evaluacion eval : evaluaciones) {
                String fechaFormateada = eval.getFechaEvaluacion().format(FECHA_FORMATTER);
                fechasFormateadas.put(eval.getIdEvaluacion(), fechaFormateada);
            }
            
            request.setAttribute("evaluaciones", evaluaciones);
            request.setAttribute("fechasFormateadas", fechasFormateadas);
            request.setAttribute("totalEvaluaciones", evaluaciones.size());
            
            // 5. Forward a la vista JSP
            request.getRequestDispatcher("/resenas.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("❌ ERROR FATAL en cargarPaginaResenas: " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("error", "Error al cargar datos: " + e.getMessage());
            request.setAttribute("stackTrace", e.getStackTrace());
            
            try {
                request.getRequestDispatcher("/resenas.jsp").forward(request, response);
            } catch (Exception ex) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                    "Error crítico: " + e.getMessage());
            }
        }
    }


    
    /**
     * Filtra reseñas por especialidad
     */
    private void filtrarPorEspecialidad(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            System.out.println("🔍 Filtrando reseñas por especialidad...");
            
            // Obtener estudiante de la sesión (puede ser null)
            HttpSession session = request.getSession(false);
            Estudiante estudiante = null;
            
            if (session != null) {
                estudiante = (Estudiante) session.getAttribute("usuario");
            }
            
            String idEspecialidadStr = request.getParameter("idEspecialidad");
            
            // 1. Cargar especialidades y doctores
            IEspecialidadDAO especialidadDAO = factory.getEspecialidadDAO();
            List<Especialidad> especialidades = especialidadDAO.getAll();
            request.setAttribute("especialidades", especialidades);
            
            IDoctorDAO doctorDAO = factory.getDoctorDAO();
            List<Doctor> doctores = doctorDAO.getAll();
            request.setAttribute("doctores", doctores);
            
            // 2. Filtrar evaluaciones
            IEvaluacionDAO evaluacionDAO = factory.getEvaluacionDAO();
            List<Evaluacion> evaluaciones;
            
            if (idEspecialidadStr != null && !idEspecialidadStr.isEmpty() 
                && !"todas".equals(idEspecialidadStr)) {
                
                int idEspecialidad = Integer.parseInt(idEspecialidadStr);
                System.out.println("📌 Filtrando por especialidad ID: " + idEspecialidad);
                
                if (estudiante != null) {
                    // FILTRAR: Estudiante + Especialidad
                    evaluaciones = evaluacionDAO.getByEstudianteYEspecialidad(
                        estudiante.getIdEstudiante(), 
                        idEspecialidad
                    );
                } else {
                    // TEMPORAL: Solo especialidad
                    evaluaciones = evaluacionDAO.getByEspecialidad(idEspecialidad);
                }
                
            } else {
                System.out.println("📌 Mostrando todas las reseñas");
                
                if (estudiante != null) {
                    evaluaciones = evaluacionDAO.getByEstudiante(estudiante.getIdEstudiante());
                } else {
                    evaluaciones = evaluacionDAO.getAll();
                }
            }
            
            // 3. FORMATEAR FECHAS
            Map<Integer, String> fechasFormateadas = new HashMap<>();
            for (Evaluacion eval : evaluaciones) {
                String fechaFormateada = eval.getFechaEvaluacion().format(FECHA_FORMATTER);
                fechasFormateadas.put(eval.getIdEvaluacion(), fechaFormateada);
            }
            
            request.setAttribute("evaluaciones", evaluaciones);
            request.setAttribute("fechasFormateadas", fechasFormateadas);
            request.setAttribute("totalEvaluaciones", evaluaciones.size());
            request.setAttribute("filtroActivo", idEspecialidadStr);
            
            request.getRequestDispatcher("/resenas.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("❌ Error al filtrar: " + e.getMessage());
            e.printStackTrace();
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.setAttribute("error", "Error al filtrar: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/resenas");
        }
    }


    
    /**
     * Crea una nueva reseña
     */
    private void crearResena(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        try {
            System.out.println("📝 Intentando crear nueva reseña...");
            
            Estudiante estudiante = (Estudiante) session.getAttribute("usuario");
            System.out.println("👤 Usuario autenticado: " + estudiante.getNombreCompleto());
            
            String idDoctorStr = request.getParameter("idDoctor");
            String calificacionStr = request.getParameter("calificacion");
            String comentario = request.getParameter("comentario");
            
            System.out.println("📥 Datos - Doctor: " + idDoctorStr + ", Calificación: " + calificacionStr);
            
            if (idDoctorStr == null || idDoctorStr.isEmpty() ||
                calificacionStr == null || calificacionStr.isEmpty() ||
                comentario == null || comentario.isEmpty()) {
                
                System.out.println("❌ Campos vacíos");
                session.setAttribute("error", "Todos los campos son requeridos");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            int idDoctor;
            int calificacion;
            try {
                idDoctor = Integer.parseInt(idDoctorStr);
                calificacion = Integer.parseInt(calificacionStr);
            } catch (NumberFormatException e) {
                System.out.println("❌ Error formato numérico");
                session.setAttribute("error", "Datos numéricos inválidos");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            if (calificacion < 1 || calificacion > 5) {
                System.out.println("❌ Calificación fuera de rango: " + calificacion);
                session.setAttribute("error", "La calificación debe ser entre 1 y 5");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            String comentarioLimpio = comentario.trim();
            if (comentarioLimpio.length() < 20) {
                System.out.println("❌ Comentario muy corto: " + comentarioLimpio.length());
                session.setAttribute("error", "El comentario debe tener al menos 20 caracteres");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            if (comentarioLimpio.length() > 500) {
                System.out.println("❌ Comentario muy largo: " + comentarioLimpio.length());
                session.setAttribute("error", "El comentario no puede exceder 500 caracteres");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            Doctor doctor = factory.getDoctorDAO().getById(idDoctor);
            if (doctor == null) {
                System.out.println("❌ Doctor no encontrado: ID " + idDoctor);
                session.setAttribute("error", "El doctor seleccionado no existe");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            System.out.println("✅ Doctor encontrado: Dr. " + doctor.getNombreCompleto());
            
            Evaluacion evaluacion = new Evaluacion();
            evaluacion.setCalificacion(calificacion);
            evaluacion.setComentario(comentarioLimpio);
            evaluacion.setFechaEvaluacion(LocalDateTime.now());
            evaluacion.setDoctor(doctor);
            evaluacion.setEstudiante(estudiante);
            
            IEvaluacionDAO evaluacionDAO = factory.getEvaluacionDAO();
            Evaluacion evaluacionGuardada = evaluacionDAO.create(evaluacion);
            
            System.out.println("✅ Reseña guardada - ID: " + evaluacionGuardada.getIdEvaluacion());
            
            session.setAttribute("mensaje", "¡Reseña publicada exitosamente!");
            response.sendRedirect(request.getContextPath() + "/resenas");
            
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", "Error al guardar la reseña: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/resenas");
        }
    }
}
