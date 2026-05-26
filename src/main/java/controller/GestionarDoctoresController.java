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
                gestionarDoctores(request, response); // método principal
                break;
            case "buscar":
                buscarDoctor(request, response); // Buscar: Filtro puntual por cédula
                break;
            case "NuevoDoctor":
                // 1.3: solicitarNuevoDoctor (mostrar formulario)
                mostrarFormulario(request, response);
                break;
            case "solicitarEdicionDoctor":
                // 3: solicitarEdicionDoctor()
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
                // 3.4: actualizarDoctor()
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
            
            // Preparar datos para la vista (antes estaba en el JSP)
            prepararDatosVista(request, doctores, null, false, null);
            
            // 1.2: mostrar(doctores)
            mostrar(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar doctores: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/inicio-admin.jsp");
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
     * Prepara todos los datos necesarios para la vista
     * Lógica que antes estaba en el JSP ahora se centraliza aquí
     */
    private void prepararDatosVista(HttpServletRequest request, List<Doctor> doctores, 
                                     List<Especialidad> especialidades,
                                     boolean abrirModalCrear, Doctor doctorEditar) {
        // Asegurar que la lista nunca sea null
        if (doctores == null) {
            doctores = new java.util.ArrayList<>();
        }
        request.setAttribute("doctores", doctores);
        
        // Especialidades (puede ser null si no se necesitan)
        if (especialidades != null) {
            request.setAttribute("especialidades", especialidades);
        }
        
        // Determinar si mostrar modal de creación
        request.setAttribute("abrirModalCrear", abrirModalCrear);
        
        // Determinar si mostrar modal de edición
        boolean abrirModalEditar = (doctorEditar != null);
        request.setAttribute("abrirModalEditar", abrirModalEditar);
        request.setAttribute("doctorEditar", doctorEditar);
    }
    
    /**
     * 1.2: mostrar(doctores) = 2.4: actualizarVista
     */
    private void mostrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/admin/gestionar-doctores.jsp").forward(request, response);
    }
    
    /**
     * Busca un doctor por cédula
     * // 3.1: obtenerDoctor(cedula)
     */
    private void buscarDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String cedula = request.getParameter("cedula");
        
        try {
            List<Doctor> doctores;
            
            if (cedula != null && !cedula.trim().isEmpty()) {
                // 3.1: obtenerDoctor(cedula)
                Doctor doctor = factory.getDoctorDAO().obtenerPorCedula(cedula);
                
                if (doctor != null) {
                    doctores = java.util.Collections.singletonList(doctor);
                } else {
                    doctores = new java.util.ArrayList<>();
                    request.getSession().setAttribute("error", "No se encontró ningún doctor con esa cédula");
                }
            } else {
                // Si no hay cédula, listar todos
                doctores = factory.getDoctorDAO().getAll();
            }

            // Preparar datos para la vista
            prepararDatosVista(request, doctores, null, false, null);
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
            
            // Validaciones mínimas
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
     * 1.5: mostrarFormulario(especialidades)
     * 1.4: obtenerNombreEspecialidades(): especialidades[]
     */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1.4: obtenerNombreEspecialidades(): especialidades[]
            List<Doctor> doctores = obtener();
            List<Especialidad> especialidades = obtenerNombreEspecialidades();
            
            // Preparar datos para la vista con modal de creación abierto
            prepararDatosVista(request, doctores, especialidades, true, null);
            
            // 1.5: mostrarFormulario
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
                // 3.2: obtenerNombreEspecialidades(): especialidades[]
                List<Doctor> doctores = obtener();
                List<Especialidad> especialidades = obtenerNombreEspecialidades();
                
                // 3.3: mostrarFormulario(especialidades) con modal de edición
                prepararDatosVista(request, doctores, especialidades, false, doctor);
                
                mostrar(request, response);
            } else {
                request.getSession().setAttribute("error", "Doctor no encontrado");
                response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar datos para edición: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
        }
    }
    
    /**
     * 3.4: actualizarDoctor()
     * obtenerDoctor(idDoctor)
     * 3.5: guardarDoctor(datosDoctor)
     * 3.6: notificarExitoEdicion
     */
    private void actualizarDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String password = request.getParameter("password");
            String idEspecialidadStr = request.getParameter("idEspecialidad");
            
            // obtenerDoctor(idDoctor)
            Doctor doctor = obtenerDoctor(id);
            
            if (doctor != null) {
                // Actualizar campos permitidos
                doctor.setEmail(email);
                doctor.setTelefono(telefono);
                
                if (password != null && !password.trim().isEmpty()) {
                    doctor.setPassword(password);
                }
                
                // Actualizar especialidad
                if (idEspecialidadStr != null && !idEspecialidadStr.trim().isEmpty()) {
                    int idEspecialidad = Integer.parseInt(idEspecialidadStr);
                    Especialidad especialidad = factory.getEspecialidadDAO().getById(idEspecialidad);
                    doctor.setEspecialidad(especialidad);
                } else {
                    doctor.setEspecialidad(null);
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
                // 2.3: cambiarEstadoDoctor(nuevoEstado)
                boolean nuevoEstado = !doctor.isActivo();
                doctor.setActivo(nuevoEstado);
                factory.getDoctorDAO().update(doctor);
                
                String msg = nuevoEstado ? "Doctor activado exitosamente" : "Doctor desactivado exitosamente";
                request.getSession().setAttribute("mensaje", msg);
            } else {
                request.getSession().setAttribute("error", "Doctor no encontrado");
            }
            
            // 2.4: actualizarVista
            response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cambiar estado del doctor: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarDoctores?accion=gestionarDoctores");
        }
    }
}