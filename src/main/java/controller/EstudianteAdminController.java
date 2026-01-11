package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.DAOFactory;
import model.entity.Estudiante;

import java.io.IOException;
import java.util.List;

/**
 * Controller para gestionar estudiantes desde el panel de administrador
 * Maneja: listar, buscar, crear, actualizar, cambiar estado
 */
@WebServlet(urlPatterns = {"/admin/estudiantes", "/EstudianteAdminController"})
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
            case "cambiarEstado":
                cambiarEstado(request, response);
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
            
            request.setAttribute("estudiantes", estudiantes);
            
            // Forward to JSP under views/admin
            request.getRequestDispatcher("/views/admin/gestionar-estudiantes.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar estudiantes: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/inicio-admin.jsp");
        }
    }
    
    /**
     * Busca un estudiante por ID de paciente (cédula)
     */
    private void buscarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idPaciente = request.getParameter("cedula"); // El parámetro se llama "cedula" pero es el idPaciente
        
        try {
            if (idPaciente != null && !idPaciente.trim().isEmpty()) {
                Estudiante estudiante = factory.getEstudianteDAO().buscarPorIdPaciente(idPaciente);
                
                if (estudiante != null) {
                    // Crear lista con un solo estudiante
                    List<Estudiante> estudiantes = java.util.Collections.singletonList(estudiante);
                    request.setAttribute("estudiantes", estudiantes);
                } else {
                    request.setAttribute("estudiantes", java.util.Collections.emptyList());
                    request.getSession().setAttribute("error", "No se encontró ningún estudiante con ese ID de paciente");
                }
            } else {
                // Si no hay idPaciente, listar todos
                List<Estudiante> estudiantes = factory.getEstudianteDAO().getAll();
                request.setAttribute("estudiantes", estudiantes);
            }
            
            request.getRequestDispatcher("/views/admin/gestionar-estudiantes.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error en la búsqueda: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=listar");
        }
    }
    
    /**
     * Crea un nuevo estudiante
     */
    private void crearEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Obtener datos del formulario - usar nombres de campos de la BD real
            String idPaciente = request.getParameter("cedula"); // El form usa "cedula" pero es idPaciente
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String email = request.getParameter("email");
            
            // Verificar que no exista un estudiante con ese idPaciente
            if (factory.getEstudianteDAO().existePorIdPaciente(idPaciente)) {
                request.getSession().setAttribute("error", "Ya existe un estudiante con ese ID de paciente");
                response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=listar");
                return;
            }
            
            // Crear nuevo estudiante usando el constructor correcto
            Estudiante nuevoEstudiante = new Estudiante(idPaciente, nombre, apellido, email);
            
            // Guardar en la base de datos
            factory.getEstudianteDAO().create(nuevoEstudiante);
            
            request.getSession().setAttribute("mensaje", "Estudiante creado exitosamente");
            response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=listar");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al crear estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=listar");
        }
    }
    
    /**
     * Actualiza un estudiante existente
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
                // Actualizar campos usando los métodos correctos según la entidad
                estudiante.setNombreEstudiante(nombre);
                estudiante.setApellidoEstudiante(apellido);
                estudiante.setCorreoEstudiante(email);
                
                // Guardar cambios
                factory.getEstudianteDAO().update(estudiante);
                
                request.getSession().setAttribute("mensaje", "Estudiante actualizado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Estudiante no encontrado");
            }
            
            response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=listar");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al actualizar estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=listar");
        }
    }
    
    /**
     * Cambia el estado de un estudiante (activo/inactivo)
     */
    private void cambiarEstado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            // Buscar estudiante
            Estudiante estudiante = factory.getEstudianteDAO().getById(id);
            
            if (estudiante != null) {
                // Cambiar estado (mirror DoctorAdminController)
                estudiante.setActivo(!estudiante.isActivo());
                
                // Guardar cambios
                factory.getEstudianteDAO().update(estudiante);
                
                String estadoNuevo = estudiante.isActivo() ? "activado" : "desactivado";
                request.getSession().setAttribute("mensaje", "Estudiante " + estadoNuevo + " exitosamente");
            } else {
                request.getSession().setAttribute("error", "Estudiante no encontrado");
            }
            
            response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=listar");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cambiar estado del estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=listar");
        }
    }
    
}