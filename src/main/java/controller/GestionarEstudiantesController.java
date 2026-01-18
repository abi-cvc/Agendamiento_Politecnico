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
 * Se refactorizó para seguir el mismo patrón que GestionarDoctoresController y
 * para reflejar exactamente el diagrama de robustez:
 *
 * Flujos (resumen):
 * 1: gestionarEstudiantes
 *   1.1: obtener(): estudiantes[]
 *   1.2: mostrar(estudiantes)
 *
 * 1.3 - 1.7: creación de estudiante
 *   1.3: solicitarNuevoEstudiante (mostrar formulario)
 *   1.4: mostrarFormulario (preparar datos para el formulario)
 *   1.5: creaNuevoEstudiante(datos) -> construir entidad
 *   1.6: crearNuevoEstudiante(datosEstudiante) -> persistencia
 *   1.7: mostrarConfirmacion (mensaje de éxito)
 *
 * 2: desactivar/activar estudiante
 *   2.1: mostrarConfirmacionDesactivar (UI)
 *   2.2: confirmarDesactivacion(cedula)
 *   2.3: cambiarEstadoEstudiante(nuevoEstado)
 *   2.4: actualizarVista (redirigir a lista)
 *
 * 3: editar estudiante
 *   3.1: obtenerEstudiante(cedula)
 *   3.2: mostrarFormulario (editar)
 *   3.3: actualizarDatos(datosEstudiante)
 *   3.4: guardarEstudiante(datosEstudiante)
 *   3.5: notificarExitoEdicion
 */
@WebServlet(urlPatterns = {"/GestionarEstudiantes"})
public class GestionarEstudiantesController extends HttpServlet {
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
        	// 1: gestionarEstudiantes() - Método inicial según diagrama
            accion = "gestionarEstudiantes";
        }

        switch (accion) {
            case "gestionarEstudiantes":
            	// metodo principal
                gestionarEstudiantes(request, response);
                break;
            case "buscar":
                // Buscar: Filtro puntual por cédula
                buscarEstudiante(request, response);
                break;
            case "NuevoEstudiante":
                // 1.3: solicitarNuevoEstudiante (mostrar formulario)
                mostrarFormulario(request, response);
                break;
            case "editarEstudiante":
                // 3: editarEstudiante()
                editarEstudiante(request, response);
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
            case "solicitarNuevoEstudiante":
                // 1.6: crearNuevoEstudiante(datosEstudiante)
                solicitarNuevoEstudiante(request, response);
                break;
            case "actualizar":
                // 3.3: actualizarDatos(datosEstudiante)
                actualizarEstudiante(request, response);
                break;
            case "confirmarDesactivacion":
            	//2.2: confirmarDesactivacion()
                confirmarDesactivacion(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                break;
        }
    }

    /**
     * 1: gestionarEstudiantes() - Método inicial
     * 1.1: obtener(): estudiantes[]
     * 1.2: mostrar(estudiantes)
     */
    private void gestionarEstudiantes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        	// 1.1: obtener(): estudiantes[]
            List<Estudiante> estudiantes = obtener();
            // 1.2: mostrar(estudiantes)
            mostrar(request, response, estudiantes);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar estudiantes: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/inicio-admin.jsp");
        }
    }

    /**
     * 1.1: obtener(): estudiantes[]
     */
    private List<Estudiante> obtener() throws Exception {
        return factory.getEstudianteDAO().getAll();
    }

    /**
     * 1.2: mostrar(estudiantes)
     */
    private void mostrar(HttpServletRequest request, HttpServletResponse response, List<Estudiante> estudiantes)
            throws ServletException, IOException {
        request.setAttribute("estudiantes", estudiantes);
        request.getRequestDispatcher("/views/admin/gestionar-estudiantes.jsp").forward(request, response);
    }

    /**
     * Busca un estudiante por cédula (idPaciente)
     * // 3.1: obtenerEstudiante() = y 2: desactivarEstudiante()
     * (Se usa en el flujo 2 para localizar un estudiante antes de desactivar/activar o editar)
     */
    private void buscarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idPaciente = request.getParameter("cedula");
        try {
            if (idPaciente != null && !idPaciente.trim().isEmpty()) {
                // 3.1: obtenerEstudiante() y 2: desactivarEstudiante()
                Estudiante estudiante = factory.getEstudianteDAO().buscarPorIdPaciente(idPaciente);
                if (estudiante != null) {
                    List<Estudiante> estudiantes = java.util.Collections.singletonList(estudiante);
                    // Mostrar lista con el estudiante encontrado
                    mostrar(request, response, estudiantes);
                    return;
                } else {
                    request.setAttribute("estudiantes", java.util.Collections.emptyList());
                    request.getSession().setAttribute("error", "No se encontró ningún estudiante con ese ID de paciente");
                    request.getRequestDispatcher("/views/admin/gestionar-estudiantes.jsp").forward(request, response);
                    return;
                }
            }
            // Si no hay filtro 1.2: mostrar(estudiantes)
            mostrar(request, response, obtener());
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error en la búsqueda: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
        }
    }

    /**
     * 1.3: solicitarNuevoEstudiante
     */
    private void solicitarNuevoEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // En este método centralizamos la creación y la respuesta (seguir la lógica previa)
        crearNuevoEstudiante(request, response);
    }

    /**
     * 1.4: mostrarFormulario
     */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("modal", request.getParameter("modal") != null ? request.getParameter("modal") : "nuevo");
            request.setAttribute("mostrarNuevoEstudianteForm", true);
            mostrar(request, response, obtener());
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al mostrar formulario: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
        }
    }

    /**
     * 1.5: creaNuevoEstudiante(datos)
     */
    private Estudiante creaNuevoEstudiante(HttpServletRequest request) {
        String idPaciente = request.getParameter("cedula");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Estudiante nuevo = new Estudiante(idPaciente, nombre, apellido, email);
        if (password != null && !password.trim().isEmpty()) {
            nuevo.setPasswordEstudiante(password);
        }
        // Por defecto, mantener activo true
        nuevo.setActivo(true);
        return nuevo;
    }

    /**
     * 1.6: crearNuevoEstudiante(datosEstudiante)
     * 1.7: mostrarConfirmacion
     */
    private void crearNuevoEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idPaciente = request.getParameter("cedula");
            // Validación mínima (evitar duplicados y ausencia de cédula)
            if (idPaciente == null || idPaciente.trim().isEmpty()) {
                request.getSession().setAttribute("error", "Cédula (ID paciente) es requerida");
                response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
                return;
            }

            if (factory.getEstudianteDAO().existePorIdPaciente(idPaciente)) {
                request.getSession().setAttribute("error", "Ya existe un estudiante con ese ID de paciente");
                response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
                return;
            }

            Estudiante nuevoEstudiante = creaNuevoEstudiante(request);
            // 1.6: crearNuevoEstudiante(datosEstudiante)
            factory.getEstudianteDAO().create(nuevoEstudiante);

            // 1.7: mostrarConfirmacion
            mostrarConfirmacion(request, "Estudiante creado exitosamente");
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al crear estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
        }
    }

    // 1.7: mostrarConfirmacion
    private void mostrarConfirmacion(HttpServletRequest request, String mensaje) {
        request.getSession().setAttribute("mensaje", mensaje);
    }

    /**
     * 3: editarEstudiante(cedula)
     * 3.1: obtenerEstudiante(cedula)
     * 3.2: mostrarFormulario (editar)
     */
    private void editarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cedula = request.getParameter("cedula");
        String idParam = request.getParameter("id");

        try {
            Estudiante estudiante = null;
            // 3.1: obtenerEstudiante(cedula)
            if (cedula != null && !cedula.trim().isEmpty()) {
                estudiante = factory.getEstudianteDAO().buscarPorIdPaciente(cedula);
            } else if (idParam != null && !idParam.trim().isEmpty()) {
                int id = Integer.parseInt(idParam);
                estudiante = factory.getEstudianteDAO().getById(id);
            }

            if (estudiante != null) {
                // 3.2: mostrarFormulario - modal editar
                request.setAttribute("estudianteEdicion", estudiante);
                request.setAttribute("modal", "editar");
                mostrar(request, response, obtener());
            } else {
                request.getSession().setAttribute("error", "Estudiante no encontrado para edición");
                response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al preparar edición: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
        }
    }

    /**
     * 3.3: actualizarDatos(datosEstudiante)
     * 3.4: guardarEstudiante(datosEstudiante)
     * 3.5: notificarExitoEdicion
     */
    private void actualizarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            Estudiante estudiante = factory.getEstudianteDAO().getById(id);
            if (estudiante != null) {
                estudiante.setNombreEstudiante(nombre);
                estudiante.setApellidoEstudiante(apellido);
                estudiante.setCorreoEstudiante(email);
                if (password != null && !password.trim().isEmpty()) {
                    estudiante.setPasswordEstudiante(password);
                }
                // 3.4: guardarEstudiante
                factory.getEstudianteDAO().update(estudiante);
                // 3.5: notificarExitoEdicion
                mostrarConfirmacion(request, "Estudiante actualizado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Estudiante no encontrado");
            }

            // actualizarVista
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al actualizar estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
        }
    }

    /**
     * 2.2: confirmarDesactivacion(cedula)
     * 2.3: cambiarEstadoEstudiante(nuevoEstado)
     * 2.4: actualizarVista
     *
     * Cambia el estado activo/inactivo del estudiante (si estaba activo -> se desactiva y viceversa)
     */
    private void confirmarDesactivacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            String cedula = request.getParameter("cedula");
            Estudiante estudiante = null;

            // 2.2: obtenerEstudiante por id o cedula
            if (idParam != null && !idParam.trim().isEmpty()) {
                int id = Integer.parseInt(idParam);
                estudiante = factory.getEstudianteDAO().getById(id);
            } else if (cedula != null && !cedula.trim().isEmpty()) {
                estudiante = factory.getEstudianteDAO().buscarPorIdPaciente(cedula);
            }

            if (estudiante != null) {
                // 2.3: cambiarEstadoEstudiante(nuevoEstado)
                estudiante.setActivo(!estudiante.isActivo());
                factory.getEstudianteDAO().update(estudiante);
                String nuevoEstado = estudiante.isActivo() ? "activado" : "desactivado";
                mostrarConfirmacion(request, "Estudiante " + nuevoEstado + " exitosamente");
            } else {
                request.getSession().setAttribute("error", "Estudiante no encontrado");
            }

            // 2.4: actualizarVista
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cambiar estado del estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
        }
    }
}
