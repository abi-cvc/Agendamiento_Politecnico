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
@WebServlet("/GestionarDoctores")
public class GestionarDoctoresController extends HttpServlet {
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
                gestionarDoctores(request, response); // metodo principal
                break;
            case "buscar":
                buscarDoctor(request, response); // filtro independiente
                break;
            case "NuevoDoctor":
                // 1.3: solicitarNuevoDoctor (mostrar formulario)
                mostrarFormulario(request, response);
                break;
            case "solicitarEdicionDoctor":
                // 3: solicitarEdicionDoctor(cedula)
                solicitarEdicionDoctor(request, response);
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
                // 1.3: solicitarNuevoDoctor
                solicitarNuevoDoctor(request, response);
                break;
            case "actualizar":
            	// 1.8: obtenerDoctor(idDoctor) y 1.9: mostrarConfirmacion
                actualizarDoctor(request, response);
                break;
            case "confirmarDesactivacion":
                // 2.2: confirmarDesactivacion
                confirmarDesactivacion(request, response);
                break;
              default:
                  response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                  break;
         }
     }
    
    /**
     * 1: gestionarDoctores() - Método inicial según diagrama de robustez
     * 1.1: obtener(): doctores[]
     * 1.2: mostrar(doctores)
     */
    private void gestionarDoctores(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 1.1: obtener(): doctores[] - Con DAOFactory(activos e inactivos)
            List<Doctor> doctores = obtener();
            // 1.2: mostrar(doctores)
            request.setAttribute("doctores", doctores);
            mostrar(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar doctores: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/inicio-admin.jsp");
        }
    }
    
    /**
     * Busca un doctor por cédula
     * // 3.1: obtenerDoctor(cedula)
     * (Este método realiza búsqueda puntual y también prepara especialidades para el formulario)
     */
    private void buscarDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String cedula = request.getParameter("cedula");
        
        try {
            if (cedula != null && !cedula.trim().isEmpty()) {
            	// 3.1: obtenerDoctor(cedula)
                Doctor doctor = factory.getDoctorDAO().obtenerPorCedula(cedula);
                
                if (doctor != null) {
                    // Lista con doctor encontrado
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
            // Sólo cargar especialidades si se solicitó el formulario por modal (compatibilidad)
            if ("nuevo".equals(request.getParameter("modal"))) {
                List<Especialidad> especialidades = factory.getEspecialidadDAO().getAll();
                request.setAttribute("especialidades", especialidades);
                // marcar para mostrar el formulario si se requiere
                request.setAttribute("mostrarNuevoDoctorForm", true);
            }

            // mostrar lista
            mostrar(request, response);
         } catch (Exception e) {
             e.printStackTrace();
             request.getSession().setAttribute("error", "Error en la búsqueda: " + e.getMessage());
             response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
         }
     }
    
    /**
     * 1.3: solicitarNuevoDoctor
     * 1.6: creaNuevoDoctor(datos)
     * 1.7: crearNuevoDoctor(datosDoctor)
     * 1.8: mostrarConfirmacion
     */
    private void solicitarNuevoDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 1.6: creaNuevoDoctor(datos)
            Doctor nuevoDoctor = creaNuevoDoctor(request);
            
            // Validaciones mínimas (se mantiene la lógica existente)
            if (nuevoDoctor.getCedula() == null || nuevoDoctor.getCedula().trim().isEmpty()) {
                request.getSession().setAttribute("error", "Cédula es requerida");
                response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
                return;
            }
            
            // Verificar que no exista un doctor con esa cédula
            if (factory.getDoctorDAO().obtenerPorCedula(nuevoDoctor.getCedula()) != null) {
                request.getSession().setAttribute("error", "Ya existe un doctor con esa cédula");
                response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
                return;
            }
            
            // 1.7: crearNuevoDoctor(datosDoctor)
            crearNuevoDoctor(nuevoDoctor);
            
            // 1.8: mostrarConfirmacion
            mostrarConfirmacion(request, "Doctor creado exitosamente");
            response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al crear doctor: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
        }
    }
    
    /**
     * Actualiza un doctor existente (solo telefono y password)
     * 3.4: actualizarDoctor()
     * obtenerDoctor(idDoctor)
     * 3.5: guardarDoctor(datosDoctor)
     * 3.6: notificarExitoEdicion
     */
    private void actualizarDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String telefono = request.getParameter("telefono");
            String password = request.getParameter("password");
            
            // obtenerDoctor(idDoctor)
            Doctor doctor = obtenerDoctor(id);
            
            if (doctor != null) {
                // Actualizar solo los campos permitidos
                doctor.setTelefono(telefono);
                if (password != null && !password.trim().isEmpty()) {
                    doctor.setPassword(password);
                }
                
                // 3.5: guardarDoctor(datosDoctor)
                factory.getDoctorDAO().update(doctor);
                
                // 3.6: notificarExitoEdicion
                request.getSession().setAttribute("mensaje", "Doctor actualizado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Doctor no encontrado");
            }
            
            response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al actualizar doctor: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
        }
    }
    
    /**
     * 1.1: obtener(): doctores[]
     */
    private List<Doctor> obtener() throws Exception {
        return factory.getDoctorDAO().getAll();
    }
    
    /**
     * 1.4 y 3.2: obtenerNombreEspecialidades(): especialidades[]
     */
    private List<Especialidad> obtenerNombreEspecialidades() throws Exception {
        return factory.getEspecialidadDAO().getAll();
    }
    
    /**
     * 1.2: mostrar(doctores) = 2.4: actualizarVista
     */
    private void mostrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/admin/gestionar-doctores.jsp").forward(request, response);
    }
    
    /**
     * 1.5: mostrarFormulario(especialidades)
     * 1.4: obtenerNombreEspecialidades(): especialidades[]
     */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        	// 1.4: obtenerNombreEspecialidades(): especialidades[] y preparar datos del formulario
            List<Doctor> doctores = obtener();
            List<Especialidad> especialidades = obtenerNombreEspecialidades();
             request.setAttribute("doctores", doctores);
             request.setAttribute("especialidades", especialidades);
              // Preservar valores de formulario por errores de validación
              // (llamada desde solicitarNuevoDoctor con parámetros en request)
              String cedula = request.getParameter("cedula");
              String nombre = request.getParameter("nombre");
              String apellido = request.getParameter("apellido");
              String email = request.getParameter("email");
              String telefono = request.getParameter("telefono");
              String idEspecialidad = request.getParameter("idEspecialidad");
              if (cedula != null) request.setAttribute("formCedula", cedula);
              if (nombre != null) request.setAttribute("formNombre", nombre);
              if (apellido != null) request.setAttribute("formApellido", apellido);
              if (email != null) request.setAttribute("formEmail", email);
              if (telefono != null) request.setAttribute("formTelefono", telefono);
              if (idEspecialidad != null) request.setAttribute("formIdEspecialidad", idEspecialidad);
              // JSP muestra el formulario de NuevoDoctor
              request.setAttribute("mostrarNuevoDoctorForm", true);
            mostrar(request, response);
           } catch (Exception e) {
               e.printStackTrace();
               request.getSession().setAttribute("error", "Error al preparar formulario: " + e.getMessage());
               response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
           }
      }
    
    /**
     * 1.6: creaNuevoDoctor(datos)
     */
    private Doctor creaNuevoDoctor(HttpServletRequest request) {
        String cedula = request.getParameter("cedula");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String password = request.getParameter("password");
        int idEspecialidad = -1;
        Especialidad especialidad = null;
        try {
            if (request.getParameter("idEspecialidad") != null && !request.getParameter("idEspecialidad").isEmpty()) {
                idEspecialidad = Integer.parseInt(request.getParameter("idEspecialidad"));
                especialidad = factory.getEspecialidadDAO().getById(idEspecialidad);
            }
        } catch (Exception ex) {
            // ignorar formato
        }
        Doctor nuevoDoctor = new Doctor(cedula, nombre, apellido, email, especialidad);
        nuevoDoctor.setTelefono(telefono);
        nuevoDoctor.setPassword(password);
        nuevoDoctor.setActivo(true);
        return nuevoDoctor;
    }
    
    /**
     * 1.7: crearNuevoDoctor(datosDoctor)
     */
    private void crearNuevoDoctor(Doctor doctor) throws Exception {
        factory.getDoctorDAO().create(doctor);
    }
    
    /**
     * obtenerDoctor(idDoctor)
     */
    private Doctor obtenerDoctor(int id) {
        return factory.getDoctorDAO().getById(id);
    }
    
    /**
     * 1.8: mostrarConfirmacion
     */
    private void mostrarConfirmacion(HttpServletRequest request, String mensaje) {
        request.getSession().setAttribute("mensaje", mensaje);
    }
    
    /**
     * 3: solicitarEdicionDoctor
     * 3.2: obtenerNombreEspecialidades(): especialidades[]
     * 3.3: mostrarFormulario(especialidades)
     */
    private void solicitarEdicionDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            // obtenerDoctor(idDoctor)
            Doctor doctor = obtenerDoctor(id);
            
            if (doctor != null) {
                request.setAttribute("doctor", doctor);
                
                // 3.2: obtenerNombreEspecialidades(): especialidades[]
                List<Especialidad> especialidades = obtenerNombreEspecialidades();
                request.setAttribute("especialidades", especialidades);
                
                // 3.3: mostrarFormulario(especialidades)
                request.setAttribute("mostrarEditarDoctorForm", true);
            } else {
                request.getSession().setAttribute("error", "Doctor no encontrado");
            }
            mostrar(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar datos para edición: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
        }
    }
    
    /**
     * 2.2: confirmarDesactivacion()
     * 2.3: cambiarEstadoDoctor(nuevoEstado)
     * 2.4: actualizarVista
     */
    private void confirmarDesactivacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            // obtenerDoctor(idDoctor)
            Doctor doctor = factory.getDoctorDAO().getById(id);
            
            if (doctor != null) {
                // 2.3: cambiarEstadoDoctor(nuevoEstado) - Si estaba activo -> desactivar; si estaba inactivo -> activar
                boolean nuevoEstado = !doctor.isActivo();
                doctor.setActivo(nuevoEstado);
                // Guardar cambios
                factory.getDoctorDAO().update(doctor);
                // 2.4: actualizarVista
                String msg = nuevoEstado ? "Doctor activado exitosamente" : "Doctor desactivado exitosamente";
                request.getSession().setAttribute("mensaje", msg);
            } else {
                request.getSession().setAttribute("error", "Doctor no encontrado");
            }
            
            response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al desactivar doctor: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
        }
    }
    
}