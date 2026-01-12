package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.DAOFactory;
import model.entity.Doctor;
import model.entity.Especialidad;

import java.io.IOException;
import java.util.List;

/**
 * Controller para gestionar doctores desde el panel de administrador
 * Maneja: listar, buscar, crear, actualizar, cambiar estado
 *
 * Mapado al diagrama de robustez (comentarios en código):
 * 1: gestionarDoctores()
 * 2: obtener(): doctores[]
 * 3: mostrar(doctores)
 * 4: solicitarNuevoDoctor(idDoctor)
 * 5: obtenerNombreEspecialidades(): especialidades[]
 * 6: mostrarFormulario(especialidades)
 * 7: crearNuevoDoctor(datos)
 * 8: CrearNuevoDoctor(datosDoctor)
 * 9: obtenerDoctor(idDoctor)
 * 10: MostrarConfirmacion
 */
@WebServlet("/DoctorAdminController")
public class DoctorAdminController extends HttpServlet {
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
            accion = "gestionarDoctores"; // 1: gestionarDoctores() - Método inicial según diagrama
        }
        
        switch (accion) {
            case "gestionarDoctores":
                gestionarDoctores(request, response); // 1: Método principal del diagrama
                break;
            case "buscar":
                buscarDoctor(request, response);
                break;
            default:
                gestionarDoctores(request, response);
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
            case "solicitarNuevoDoctor":
                solicitarNuevoDoctor(request, response); // 4: iniciar flujo de solicitud/creación
                break;
            case "actualizar":
                actualizarDoctor(request, response);
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
     * 1: gestionarDoctores() - Método inicial según diagrama de robustez
     * 2: obtener(): doctores[]
     * 3: mostrar(doctores)
     * 5: obtenerNombreEspecialidades(): especialidades[] (para el formulario)
     */
    private void gestionarDoctores(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 2: obtener(): doctores[] - Usando DAOFactory(activos e inactivos para el admin)
            List<Doctor> doctores = factory.getDoctorDAO().getAll();
            
            // 5: obtenerNombreEspecialidades(): especialidades[] - Usando DAOFactory para el select del formulario (necesario para crear/editar)
            List<Especialidad> especialidades = factory.getEspecialidadDAO().getAll();
            
            // 3: mostrar(doctores) - Enviar a la vista (la JSP mostrará la lista, y el formulario usará especialidades para el select)
            request.setAttribute("doctores", doctores);
            request.setAttribute("especialidades", especialidades);
            request.getRequestDispatcher("/views/admin/gestionar-doctores.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar doctores: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/inicio-admin.jsp");
        }
    }
    
    /**
     * Busca un doctor por cédula
     * (Este método realiza búsqueda puntual y también prepara especialidades para el formulario)
     */
    private void buscarDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String cedula = request.getParameter("cedula");
        
        try {
            if (cedula != null && !cedula.trim().isEmpty()) {
                Doctor doctor = factory.getDoctorDAO().obtenerPorCedula(cedula);
                
                if (doctor != null) {
                    // Crear lista con un solo doctor
                    List<Doctor> doctores = java.util.Collections.singletonList(doctor);
                    request.setAttribute("doctores", doctores);
                } else {
                    request.setAttribute("doctores", java.util.Collections.emptyList());
                    request.getSession().setAttribute("error", "No se encontró ningún doctor con esa cédula");
                }
            } else {
                // Si no hay cédula, listar todos
                List<Doctor> doctores = factory.getDoctorDAO().getAll();
                request.setAttribute("doctores", doctores);
            }
            
            // Obtener especialidades para el select (5)
            List<Especialidad> especialidades = factory.getEspecialidadDAO().getAll();
            request.setAttribute("especialidades", especialidades);
            
            request.getRequestDispatcher("/views/admin/gestionar-doctores.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error en la búsqueda: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=gestionarDoctores");
        }
    }
    
    /**
     * 4: solicitarNuevoDoctor(idDoctor) - recibir petición de nuevo doctor y procesar creación
     * 5: obtenerNombreEspecialidad(): especialidades[] (si fue enviada la id)
     * 6: mostrarFormulario(especialidades) - (la JSP usa el atributo 'especialidades' que se cargó en gestionarDoctores/buscar)
     * 7: crearNuevoDoctor(datos) - recoger datos del formulario
     * 8: CrearNuevoDoctor(datosDoctor) - crear entidad y persistir
     */
    private void solicitarNuevoDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Obtener datos del formulario (7)
            String cedula = request.getParameter("cedula");
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String password = request.getParameter("password");
            int idEspecialidad = -1; // opcional, puede venir o no
            
            // Verificar que no exista un doctor con esa cédula
            if (factory.getDoctorDAO().obtenerPorCedula(cedula) != null) {
                request.getSession().setAttribute("error", "Ya existe un doctor con esa cédula");
                response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=gestionarDoctores");
                return;
            }
            
            // 5: obtenerNombreEspecialidad() - si se envió idEspecialidad la buscamos
            Especialidad especialidad = null;
            try {
                if (request.getParameter("idEspecialidad") != null && !request.getParameter("idEspecialidad").isEmpty()) {
                    idEspecialidad = Integer.parseInt(request.getParameter("idEspecialidad"));
                    especialidad = factory.getEspecialidadDAO().getById(idEspecialidad);
                }
            } catch (NumberFormatException ex) {
                // ignorar, especialidad permanecerá null
            }
            
            // 8: CrearNuevoDoctor(datosDoctor) - crear entidad Doctor y persistir
            Doctor nuevoDoctor = new Doctor(cedula, nombre, apellido, email, especialidad);
            nuevoDoctor.setTelefono(telefono);
            nuevoDoctor.setPassword(password);
            nuevoDoctor.setActivo(true);
            
            // Guardar en la base de datos (7->8)
            factory.getDoctorDAO().create(nuevoDoctor);
            
            // 10: MostrarConfirmacion - se usa mensaje en sesión y redirección
            request.getSession().setAttribute("mensaje", "Doctor creado exitosamente");
            response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=gestionarDoctores");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al crear doctor: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=gestionarDoctores");
        }
    }
    
    /**
     * Actualiza un doctor existente (solo telefono y password)
     * 9: obtenerDoctor(idDoctor) - se realiza aquí con getById
     * 10: MostrarConfirmacion - mensaje en sesión
     */
    private void actualizarDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String telefono = request.getParameter("telefono");
            String password = request.getParameter("password");
            
            // 9: obtenerDoctor(idDoctor)
            Doctor doctor = factory.getDoctorDAO().getById(id);
            
            if (doctor != null) {
                // Actualizar solo los campos permitidos
                doctor.setTelefono(telefono);
                if (password != null && !password.trim().isEmpty()) {
                    doctor.setPassword(password);
                }
                
                // Guardar cambios
                factory.getDoctorDAO().update(doctor);
                
                // 10: MostrarConfirmacion
                request.getSession().setAttribute("mensaje", "Doctor actualizado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Doctor no encontrado");
            }
            
            response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=gestionarDoctores");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al actualizar doctor: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=gestionarDoctores");
        }
    }
    
    /**
     * Cambia el estado de un doctor (activo/inactivo)
     */
    private void cambiarEstado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            // Buscar doctor
            Doctor doctor = factory.getDoctorDAO().getById(id);
            
            if (doctor != null) {
                // Cambiar estado
                doctor.setActivo(!doctor.isActivo());
                
                // Guardar cambios
                factory.getDoctorDAO().update(doctor);
                
                String estadoNuevo = doctor.isActivo() ? "activado" : "desactivado";
                request.getSession().setAttribute("mensaje", "Doctor " + estadoNuevo + " exitosamente");
            } else {
                request.getSession().setAttribute("error", "Doctor no encontrado");
            }
            
            response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=listar");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cambiar estado: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=gestionarDoctores");
        }
    }
    
}