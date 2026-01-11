package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.DAOFactory;
import model.entity.Estudiante;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Controller para gestionar estudiantes desde el panel de administrador
 * Maneja: listar, buscar, crear, actualizar
 */
@WebServlet("/EstudianteAdminController")
public class EstudianteAdminController extends HttpServlet {
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
                listarEstudiantes(request, response);
                break;
            case "buscar":
                buscarEstudiante(request, response);
                break;
            default:
                listarEstudiantes(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        if (accion == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no especificada");
            return;
        }
        
        switch (accion) {
            case "crear":
                crearEstudiante(request, response);
                break;
            case "actualizar":
                actualizarEstudiante(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                break;
        }
    }
    
    /**
     * Lista todos los estudiantes
     */
    private void listarEstudiantes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Obtener todos los estudiantes
            List<Estudiante> estudiantes = factory.getEstudianteDAO().getAll();
            
            // Si piden JSON devolver JSON
            String format = request.getParameter("format");
            if ("json".equalsIgnoreCase(format) || "XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.setContentType("application/json;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.print(estudiantesToJson(estudiantes));
                    out.flush();
                    return;
                }
            }
            
            request.setAttribute("estudiantes", estudiantes);
            
            request.getRequestDispatcher("/gestionar-estudiantes.html").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar estudiantes: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/inicio-admin.html");
        }
    }
    
    /**
     * Busca un estudiante por cédula
     */
    private void buscarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String cedula = request.getParameter("cedula");
        
        try {
            if (cedula != null && !cedula.trim().isEmpty()) {
                Estudiante estudiante = factory.getEstudianteDAO().buscarPorIdPaciente(cedula);
                
                if (estudiante != null) {
                    // Crear lista con un solo estudiante
                    List<Estudiante> estudiantes = java.util.Collections.singletonList(estudiante);
                    // Responder JSON si se pidió
                    String format = request.getParameter("format");
                    if ("json".equalsIgnoreCase(format) || "XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                        response.setContentType("application/json;charset=UTF-8");
                        try (PrintWriter out = response.getWriter()) {
                            out.print(estudiantesToJson(estudiantes));
                            out.flush();
                            return;
                        }
                    }
                    request.setAttribute("estudiantes", estudiantes);
                } else {
                    request.setAttribute("estudiantes", java.util.Collections.emptyList());
                    request.getSession().setAttribute("error", "No se encontró ningún estudiante con esa cédula");
                }
            } else {
                // Si no hay cédula, listar todos
                List<Estudiante> estudiantes = factory.getEstudianteDAO().getAll();
                request.setAttribute("estudiantes", estudiantes);
            }
            
            request.getRequestDispatcher("/gestionar-estudiantes.html").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error en la búsqueda: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/estudiantes?accion=listar");
        }
    }
    
    /**
     * Crea un nuevo estudiante
     */
    private void crearEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Obtener datos del formulario
            String cedula = request.getParameter("cedula");
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String email = request.getParameter("email");
            
            // Verificar que no exista un estudiante con esa cédula
            if (factory.getEstudianteDAO().existePorIdPaciente(cedula)) {
                request.getSession().setAttribute("error", "Ya existe un estudiante con esa cédula");
                response.sendRedirect(request.getContextPath() + "/admin/estudiantes?accion=listar");
                return;
            }
            
            // Crear nuevo estudiante usando solo campos presentes en la BD
            Estudiante nuevoEstudiante = new Estudiante(cedula, nombre, apellido, email);
            
            // Guardar en la base de datos
            factory.getEstudianteDAO().create(nuevoEstudiante);
            
            request.getSession().setAttribute("mensaje", "Estudiante creado exitosamente");
            response.sendRedirect(request.getContextPath() + "/admin/estudiantes?accion=listar");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al crear estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/estudiantes?accion=listar");
        }
    }
    
    /**
     * Actualiza un estudiante existente (solo los campos existentes en BD)
     */
    private void actualizarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String email = request.getParameter("email");
            
            // Buscar estudiante
            Estudiante estudiante = factory.getEstudianteDAO().getById(id);
            
            if (estudiante != null) {
                // Actualizar solo los campos permitidos (según BD)
                estudiante.setNombreEstudiante(nombre);
                estudiante.setApellidoEstudiante(apellido);
                estudiante.setCorreoEstudiante(email);
                
                // Guardar cambios
                factory.getEstudianteDAO().update(estudiante);
                
                request.getSession().setAttribute("mensaje", "Estudiante actualizado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Estudiante no encontrado");
            }
            
            response.sendRedirect(request.getContextPath() + "/admin/estudiantes?accion=listar");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al actualizar estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/estudiantes?accion=listar");
        }
    }
    
    // ====== JSON helpers ======
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
    
    private String estudiantesToJson(List<Estudiante> estudiantes) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (Estudiante e : estudiantes) {
            if (!first) sb.append(',');
            first = false;
            sb.append('{');
            sb.append("\"idEstudiante\":").append(e.getIdEstudiante()).append(',');
            sb.append("\"idPaciente\":\"").append(escapeJson(e.getIdPaciente())).append("\"").append(',');
            sb.append("\"nombreEstudiante\":\"").append(escapeJson(e.getNombreEstudiante())).append("\"").append(',');
            sb.append("\"apellidoEstudiante\":\"").append(escapeJson(e.getApellidoEstudiante())).append("\"").append(',');
            sb.append("\"correoEstudiante\":\"").append(escapeJson(e.getCorreoEstudiante())).append("\"");
            sb.append('}');
        }
        sb.append(']');
        return sb.toString();
    }
}