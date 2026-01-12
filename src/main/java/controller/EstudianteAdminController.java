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
 *
 * Mapeo al diagrama de robustez (comentarios en código):
 * 1: gestionarEstudiantes()
 * 2: obtener(): estudiantes[]
 * 3: mostrar(estudiantes)
 * 4: solicitarNuevoEstudiante()
 * 5: mostrarFormulario()
 * 6: crearNuevoEstudiante(datos)
 * 7: CrearNuevoEstudiante(datosEstudiante)
 * 8: obtenerEstudiante(idEstudiante)
 * 9: MostrarConfirmacion
 */
@WebServlet(urlPatterns = {"/EstudianteAdminController"})
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
        	accion = "gestionarEstudiantes()"; // 1: gestionarEstudiantes() - Método inicial según diagrama (nota: el switch usa 'gestionarEstudiantes')
        }
        
        switch (accion) {
            case "gestionarEstudiantes":
            	gestionarEstudiantes(request, response); // 1: gestionarEstudiantes() - inicia flujo
                break;
            case "buscar":
                buscarEstudiante(request, response);
                break;
            default:
                gestionarEstudiantes(request, response);
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
            case "solicilitarNuevoEstudiante":
            	solicilitarNuevoEstudiante(request, response); // 4: iniciar flujo de solicitud/creación de nuevo estudiante
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
     * 1: gestionarEstudiantes() - Método inicial según diagrama de robustez
     * 2: obtener(): estudiantes[]
     * 3: mostrar(estudiantes)
     */
    private void gestionarEstudiantes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
        	// 2: obtener(): estudiantes[] - obtenemos todos los estudiantes (activos e inactivos para el admin)
            List<Estudiante> estudiantes = factory.getEstudianteDAO().getAll();
            
            // 3: mostrar(estudiantes) - enviar la lista a la vista
            request.setAttribute("estudiantes", estudiantes);
            request.getRequestDispatcher("/views/admin/gestionar-estudiantes.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar estudiantes: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/inicio-admin.jsp");
        }
    }
    
    /**
     * Busca un estudiante por ID de paciente (cédula)
     * (método de filtro/búsqueda, no forma parte del flujo de creación principal, por eso no se modifica)
     */
    private void buscarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idPaciente = request.getParameter("cedula"); // El parámetro se llama "cedula" pero es el idPaciente
        
        try {
            if (idPaciente != null && !idPaciente.trim().isEmpty()) {
                Estudiante estudiante = factory.getEstudianteDAO().buscarPorIdPaciente(idPaciente);
                
                if (estudiante != null) {
                    // Crear lista con un solo estudiante (3: mostrar(estudiantes) con un único elemento)
                    List<Estudiante> estudiantes = java.util.Collections.singletonList(estudiante);
                    request.setAttribute("estudiantes", estudiantes);
                } else {
                    request.setAttribute("estudiantes", java.util.Collections.emptyList());
                    request.getSession().setAttribute("error", "No se encontró ningún estudiante con ese ID de paciente");
                }
            } else {
                // Si no hay idPaciente, listar todos (2: obtener())
                List<Estudiante> estudiantes = factory.getEstudianteDAO().getAll();
                request.setAttribute("estudiantes", estudiantes);
            }
            
            // Forward a la vista (3: mostrar)
            request.getRequestDispatcher("/views/admin/gestionar-estudiantes.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error en la búsqueda: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=gestionarEstudiantes");
        }
    }
    
    /**
     * 4: solicitarNuevoEstudiante() - recibe la petición de crear un estudiante desde el formulario
     * 5: mostrarFormulario() - el JSP corresponde a la vista que muestra el formulario (se renderiza en gestionarEstudiantes cuando se pasa modal=nuevo)
     * 6: crearNuevoEstudiante(datos) - recoger datos del formulario
     * 7: CrearNuevoEstudiante(datosEstudiante) - crear entidad y persistir en BD
     */
    private void solicilitarNuevoEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 6: crearNuevoEstudiante(datos) - Obtener datos del formulario
            String idPaciente = request.getParameter("cedula"); // El form usa "cedula" pero es idPaciente
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            
            // Validación: evitar duplicados
            if (factory.getEstudianteDAO().existePorIdPaciente(idPaciente)) {
                request.getSession().setAttribute("error", "Ya existe un estudiante con ese ID de paciente");
                response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=gestionarEstudiantes");
                return;
            }
            
            // 7: CrearNuevoEstudiante(datosEstudiante) - crear entidad Estudiante y persistir (7->8)
            Estudiante nuevoEstudiante = new Estudiante(idPaciente, nombre, apellido, email);
            if (password != null && !password.trim().isEmpty()) {
                nuevoEstudiante.setPasswordEstudiante(password);
            }
            factory.getEstudianteDAO().create(nuevoEstudiante);
            
            // 9: MostrarConfirmacion - usar mensaje en sesión y redirección
            request.getSession().setAttribute("mensaje", "Estudiante creado exitosamente");
            response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=gestionarEstudiantes");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al crear estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=gestionarEstudiantes");
        }
    }
    
    /**
     * Actualiza un estudiante existente
     * 8: obtenerEstudiante(idEstudiante) - se realiza aquí con getById
     * 9: MostrarConfirmacion - mensaje en sesión
     */
    private void actualizarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            
            // 8: obtenerEstudiante(idEstudiante)
            Estudiante estudiante = factory.getEstudianteDAO().getById(id);
            
            if (estudiante != null) {
                // Actualizar campos usando los métodos correctos según la entidad
                estudiante.setNombreEstudiante(nombre);
                estudiante.setApellidoEstudiante(apellido);
                estudiante.setCorreoEstudiante(email);
                // Actualizar contraseña solo si se proporcionó
                if (password != null && !password.trim().isEmpty()) {
                    estudiante.setPasswordEstudiante(password);
                }
                
                // Guardar cambios
                factory.getEstudianteDAO().update(estudiante);
                
                // 9: MostrarConfirmacion
                request.getSession().setAttribute("mensaje", "Estudiante actualizado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Estudiante no encontrado");
            }
            
            response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=gestionarEstudiantes");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al actualizar estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=gestionarEstudiantes");
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
            
            response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=gestionarEstudiantes");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cambiar estado del estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/EstudianteAdminController?accion=gestionarEstudiantes");
        }
    }
    
}