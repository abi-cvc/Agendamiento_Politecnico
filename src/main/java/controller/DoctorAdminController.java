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
            accion = "listar";
        }
        
        switch (accion) {
            case "listar":
                listarDoctores(request, response);
                break;
            case "buscar":
                buscarDoctor(request, response);
                break;
            default:
                listarDoctores(request, response);
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
                crearDoctor(request, response);
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
     * Lista todos los doctores
     */
    private void listarDoctores(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Obtener todos los doctores (activos e inactivos para el admin)
            List<Doctor> doctores = factory.getDoctorDAO().getAll();
            
            // Obtener especialidades para el select del formulario (necesario para crear/editar)
            List<Especialidad> especialidades = factory.getEspecialidadDAO().getAll();
            
            request.setAttribute("doctores", doctores);
            request.setAttribute("especialidades", especialidades);
            
            // Forward to JSP under views/admin
            request.getRequestDispatcher("/views/admin/gestionar-doctores.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar doctores: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/inicio-admin.jsp");
        }
    }
    
    /**
     * Busca un doctor por cédula
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
            
            // Obtener especialidades para el select
            List<Especialidad> especialidades = factory.getEspecialidadDAO().getAll();
            request.setAttribute("especialidades", especialidades);
            
            request.getRequestDispatcher("/views/admin/gestionar-doctores.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error en la búsqueda: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=listar");
        }
    }
    
    /**
     * Crea un nuevo doctor
     */
    private void crearDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Obtener datos del formulario
            String cedula = request.getParameter("cedula");
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String password = request.getParameter("password");
            int idEspecialidad = -1; // opcional, ya no requerimos especialidad en lista; mantener si viene
            
            // Verificar que no exista un doctor con esa cédula
            if (factory.getDoctorDAO().obtenerPorCedula(cedula) != null) {
                request.getSession().setAttribute("error", "Ya existe un doctor con esa cédula");
                response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=listar");
                return;
            }
            
            // Obtener especialidad
            Especialidad especialidad = null;
            try {
                if (request.getParameter("idEspecialidad") != null && !request.getParameter("idEspecialidad").isEmpty()) {
                    idEspecialidad = Integer.parseInt(request.getParameter("idEspecialidad"));
                    especialidad = factory.getEspecialidadDAO().getById(idEspecialidad);
                }
            } catch (NumberFormatException ex) {
                // ignorar, especialidad permanecerá null
            }
            
            // Crear nuevo doctor (sin foto/descripcion)
            Doctor nuevoDoctor = new Doctor(cedula, nombre, apellido, email, especialidad);
            nuevoDoctor.setTelefono(telefono);
            nuevoDoctor.setPassword(password);
            nuevoDoctor.setActivo(true);
            
            // Guardar en la base de datos
            factory.getDoctorDAO().create(nuevoDoctor);
            
            request.getSession().setAttribute("mensaje", "Doctor creado exitosamente");
            response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=listar");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al crear doctor: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=listar");
        }
    }
    
    /**
     * Actualiza un doctor existente (solo telefono, foto, descripcion)
     */
    private void actualizarDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String telefono = request.getParameter("telefono");
            String password = request.getParameter("password");
            
            // Buscar doctor
            Doctor doctor = factory.getDoctorDAO().getById(id);
            
            if (doctor != null) {
                // Actualizar solo los campos permitidos
                doctor.setTelefono(telefono);
                if (password != null && !password.trim().isEmpty()) {
                    doctor.setPassword(password);
                }
                
                // Guardar cambios
                factory.getDoctorDAO().update(doctor);
                
                request.getSession().setAttribute("mensaje", "Doctor actualizado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Doctor no encontrado");
            }
            
            response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=listar");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al actualizar doctor: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=listar");
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
            response.sendRedirect(request.getContextPath() + "/DoctorAdminController?accion=listar");
        }
    }
    
}