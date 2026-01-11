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
 * Controller completo para gestión de reseñas/evaluaciones
 * Trabaja exclusivamente con JSP (sin JSON)
 */
@WebServlet("/resenas")
public class ResenasController extends HttpServlet {
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
            accion = "cargarPagina";
        }
        
        switch (accion) {
            case "cargarPagina":
                cargarPaginaResenas(request, response);
                break;
            case "filtrarPorEspecialidad":
                filtrarPorEspecialidad(request, response);
                break;
            case "filtrarPorDoctor":
                filtrarPorDoctor(request, response);
                break;
            case "verEstadisticas":
                verEstadisticas(request, response);
                break;
            case "topDoctores":
                verTopDoctores(request, response);
                break;
            case "misResenas":
                verMisResenas(request, response);
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
        
        if (accion == null) {
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
    
    /**
     * Carga la página principal de reseñas con todas las especialidades y evaluaciones
     */
    private void cargarPaginaResenas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Cargar especialidades para el select
            IEspecialidadDAO especialidadDAO = factory.getEspecialidadDAO();
            List<Especialidad> especialidades = especialidadDAO.getAll();
            
            // Cargar doctores para el select opcional
            IDoctorDAO doctorDAO = factory.getDoctorDAO();
            List<Doctor> doctores = doctorDAO.getAll();
            
            // Cargar todas las evaluaciones
            IEvaluacionDAO evaluacionDAO = factory.getEvaluacionDAO();
            List<Evaluacion> evaluaciones = evaluacionDAO.getAll();
            
            // Enviar datos a la vista
            request.setAttribute("especialidades", especialidades);
            request.setAttribute("doctores", doctores);
            request.setAttribute("evaluaciones", evaluaciones);
            request.setAttribute("totalEvaluaciones", evaluaciones.size());
            
            System.out.println("✅ Página de reseñas cargada: " + especialidades.size() + 
                             " especialidades, " + doctores.size() + " doctores, " + 
                             evaluaciones.size() + " evaluaciones");
            
            request.getRequestDispatcher("/resenas.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar datos: " + e.getMessage());
            request.getRequestDispatcher("/resenas.jsp").forward(request, response);
        }
    }
    
    /**
     * Crea una nueva reseña/evaluación
     */
    private void crearResena(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        try {
            // Verificar sesión
            if (session == null || session.getAttribute("usuario") == null) {
                session = request.getSession();
                session.setAttribute("error", "Debes iniciar sesión para dejar una reseña");
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }
            
            // Verificar que sea estudiante
            String rol = (String) session.getAttribute("rol");
            if (!"estudiante".equals(rol)) {
                session.setAttribute("error", "Solo los estudiantes pueden dejar reseñas");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            // Obtener estudiante de la sesión
            Estudiante estudiante = (Estudiante) session.getAttribute("usuario");
            
            // Obtener parámetros del formulario
            String idDoctorStr = request.getParameter("idDoctor");
            String calificacionStr = request.getParameter("calificacion");
            String comentario = request.getParameter("comentario");
            String idCitaStr = request.getParameter("idCita");
            
            // Validaciones
            if (idDoctorStr == null || calificacionStr == null || comentario == null) {
                session.setAttribute("error", "Todos los campos son requeridos");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            int idDoctor = Integer.parseInt(idDoctorStr);
            int calificacion = Integer.parseInt(calificacionStr);
            
            if (calificacion < 1 || calificacion > 5) {
                session.setAttribute("error", "La calificación debe ser entre 1 y 5 estrellas");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            if (comentario.trim().length() < 20) {
                session.setAttribute("error", "El comentario debe tener al menos 20 caracteres");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            // Obtener el doctor
            Doctor doctor = factory.getDoctorDAO().getById(idDoctor);
            if (doctor == null) {
                session.setAttribute("error", "Doctor no encontrado");
                response.sendRedirect(request.getContextPath() + "/resenas");
                return;
            }
            
            // Crear la evaluación
            Evaluacion evaluacion = new Evaluacion();
            evaluacion.setCalificacion(calificacion);
            evaluacion.setComentario(comentario.trim());
            evaluacion.setFechaEvaluacion(LocalDateTime.now());
            evaluacion.setDoctor(doctor);
            evaluacion.setEstudiante(estudiante);
            
            // Si viene de una cita específica, asociarla
            if (idCitaStr != null && !idCitaStr.isEmpty()) {
                try {
                    // Aquí podrías setear la cita si tu entidad Evaluacion tiene ese campo
                    // evaluacion.setIdCita(Integer.parseInt(idCitaStr));
                } catch (NumberFormatException e) {
                    // Ignorar si no es válido
                }
            }
            
            // Guardar en la base de datos
            IEvaluacionDAO evaluacionDAO = factory.getEvaluacionDAO();
            evaluacionDAO.create(evaluacion);
            
            session.setAttribute("mensaje", "¡Reseña publicada exitosamente! Gracias por tu opinión.");
            response.sendRedirect(request.getContextPath() + "/resenas");
            
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Datos numéricos inválidos");
            response.sendRedirect(request.getContextPath() + "/resenas");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Error al guardar la reseña: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/resenas");
        }
    }
    
    /**
     * Filtra evaluaciones por especialidad
     */
    private void filtrarPorEspecialidad(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String idEspecialidadStr = request.getParameter("idEspecialidad");
            
            // Cargar especialidades y doctores (para los selects)
            IEspecialidadDAO especialidadDAO = factory.getEspecialidadDAO();
            List<Especialidad> especialidades = especialidadDAO.getAll();
            
            IDoctorDAO doctorDAO = factory.getDoctorDAO();
            List<Doctor> doctores = doctorDAO.getAll();
            
            request.setAttribute("especialidades", especialidades);
            request.setAttribute("doctores", doctores);
            
            // Filtrar evaluaciones
            IEvaluacionDAO evaluacionDAO = factory.getEvaluacionDAO();
            List<Evaluacion> evaluaciones;
            
            if (idEspecialidadStr != null && !idEspecialidadStr.isEmpty() && !"todas".equals(idEspecialidadStr)) {
                int idEspecialidad = Integer.parseInt(idEspecialidadStr);
                evaluaciones = evaluacionDAO.getByEspecialidad(idEspecialidad);
                
                // Buscar la especialidad para mostrar el nombre
                Especialidad espSeleccionada = especialidades.stream()
                    .filter(e -> e.getIdEspecialidad() == idEspecialidad)
                    .findFirst()
                    .orElse(null);
                
                request.setAttribute("especialidadSeleccionada", espSeleccionada);
            } else {
                evaluaciones = evaluacionDAO.getAll();
            }
            
            request.setAttribute("evaluaciones", evaluaciones);
            request.setAttribute("totalEvaluaciones", evaluaciones.size());
            request.setAttribute("filtroActivo", idEspecialidadStr);
            
            request.getRequestDispatcher("/resenas.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al filtrar: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/resenas");
        }
    }
    
    /**
     * Filtra evaluaciones por doctor
     */
    private void filtrarPorDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idDoctor = Integer.parseInt(request.getParameter("idDoctor"));
            
            // Cargar datos base
            IEspecialidadDAO especialidadDAO = factory.getEspecialidadDAO();
            List<Especialidad> especialidades = especialidadDAO.getAll();
            
            IDoctorDAO doctorDAO = factory.getDoctorDAO();
            List<Doctor> doctores = doctorDAO.getAll();
            Doctor doctorSeleccionado = doctorDAO.getById(idDoctor);
            
            // Filtrar evaluaciones del doctor
            IEvaluacionDAO evaluacionDAO = factory.getEvaluacionDAO();
            List<Evaluacion> evaluaciones = evaluacionDAO.getByDoctor(idDoctor);
            
            request.setAttribute("especialidades", especialidades);
            request.setAttribute("doctores", doctores);
            request.setAttribute("doctorSeleccionado", doctorSeleccionado);
            request.setAttribute("evaluaciones", evaluaciones);
            request.setAttribute("totalEvaluaciones", evaluaciones.size());
            
            request.getRequestDispatcher("/resenas.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al filtrar por doctor: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/resenas");
        }
    }
    

    /**
     * Muestra estadísticas de evaluaciones
     */
    private void verEstadisticas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            IEvaluacionDAO evaluacionDAO = factory.getEvaluacionDAO();
            
            // Calcular estadísticas
            long totalResenas = evaluacionDAO.contarTodas();
            double promedioGeneral = evaluacionDAO.obtenerPromedioGeneral();
            
            request.setAttribute("totalResenas", totalResenas);
            request.setAttribute("promedioGeneral", promedioGeneral);
            
            // Cargar datos base
            cargarDatosBase(request);
            
            request.getRequestDispatcher("/views/admin/estadisticas-resenas.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al obtener estadísticas: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/resenas");
        }
    }
    
    /**
     * Muestra top doctores mejor calificados
     */
    private void verTopDoctores(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int limite = 10; // Top 10 por defecto
            String limiteParam = request.getParameter("limite");
            if (limiteParam != null) {
                limite = Integer.parseInt(limiteParam);
            }
            
            IEvaluacionDAO evaluacionDAO = factory.getEvaluacionDAO();
            IDoctorDAO doctorDAO = factory.getDoctorDAO();
            
            List<Doctor> doctores = doctorDAO.getAll();
            
            request.setAttribute("doctores", doctores);
            request.setAttribute("limite", limite);
            
            // Cargar datos base
            cargarDatosBase(request);
            
            request.getRequestDispatcher("/views/admin/top-doctores.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al obtener top doctores: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/resenas");
        }
    }
    
    /**
     * Muestra las reseñas del estudiante logueado
     */
    private void verMisResenas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        try {
            if (session == null || session.getAttribute("usuario") == null) {
                session = request.getSession();
                session.setAttribute("error", "Debes iniciar sesión");
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }
            
            Estudiante estudiante = (Estudiante) session.getAttribute("usuario");
            
            IEvaluacionDAO evaluacionDAO = factory.getEvaluacionDAO();
            List<Evaluacion> misEvaluaciones = evaluacionDAO.getByEstudiante(estudiante.getIdEstudiante());
            
            request.setAttribute("evaluaciones", misEvaluaciones);
            request.setAttribute("totalEvaluaciones", misEvaluaciones.size());
            
            // Cargar datos base
            cargarDatosBase(request);
            
            request.getRequestDispatcher("/views/mis-resenas.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Error al cargar tus reseñas: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/resenas");
        }
    }
    
    /**
     * Método auxiliar para cargar datos comunes
     */
    private void cargarDatosBase(HttpServletRequest request) {
        try {
            IEspecialidadDAO especialidadDAO = factory.getEspecialidadDAO();
            IDoctorDAO doctorDAO = factory.getDoctorDAO();
            
            List<Especialidad> especialidades = especialidadDAO.getAll();
            List<Doctor> doctores = doctorDAO.getAll();
            
            request.setAttribute("especialidades", especialidades);
            request.setAttribute("doctores", doctores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}