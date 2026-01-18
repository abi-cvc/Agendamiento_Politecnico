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
            // Default: iniciar flujo gestionarEstudiantes (1)
            accion = "gestionarEstudiantes";
        }

        switch (accion) {
            case "gestionarEstudiantes":
                // 1: gestionarEstudiantes() - inicia flujo
                // 1.1: obtener(): estudiantes[]
                // 1.2: mostrar(estudiantes)
                gestionarEstudiantes(request, response);
                break;
            case "buscar":
                // Buscar: Filtro puntual por cédula
                // Usa el flujo de búsqueda para preparar la vista con 0 o 1 resultados
                buscarEstudiante(request, response);
                break;
            case "NuevoEstudiante":
                // 1.3: solicitarNuevoEstudiante (mostrar formulario)
                // Alineado con GestionarDoctores: acción visible en la URL para solicitar el formulario
                mostrarFormulario(request, response);
                break;
            case "mostrarFormulario":
                // Alias por compatibilidad: mostrar formulario
                mostrarFormulario(request, response);
                break;
            case "editarEstudiante":
                // 3: editarEstudiante(cedula)
                // 3.1: obtenerEstudiante(cedula)
                // 3.2: mostrarFormulario (editar)
                editarEstudiante(request, response);
                break;
            default:
                // Si la acción no se reconoce, mostrar lista por defecto
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
            case "crearNuevoEstudiante":
            case "crear":
                // Creación (POST)
                // - si el formulario envía 'crearNuevoEstudiante' o alias, crear la entidad
                // 1.5/1.6: crearNuevoEstudiante(datos) -> persistir
                solicitarNuevoEstudiante(request, response);
                break;
            case "actualizar":
                // Edición: actualizar datos del estudiante
                // 3.3/3.4: actualizarDatos + guardarEstudiante
                actualizarEstudiante(request, response);
                break;
            case "confirmarDesactivacion":
            case "cambiarEstado":
                // 2.x: confirmarDesactivacion / cambiarEstadoEstudiante -> cambiar estado
                confirmarDesactivacion(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                break;
        }
    }

    // ----------------------- FLUJO PRINCIPAL (1) -----------------------
    /**
     * 1: gestionarEstudiantes() - Método inicial
     * 1.1: obtener(): estudiantes[]
     * 1.2: mostrar(estudiantes)
     */
    private void gestionarEstudiantes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Estudiante> estudiantes = obtener(); // 1.1
            mostrar(request, response, estudiantes); // 1.2
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar estudiantes: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/inicio-admin.jsp");
        }
    }

    /**
     * Obtiene todos los estudiantes (1.1)
     */
    private List<Estudiante> obtener() throws Exception {
        return factory.getEstudianteDAO().getAll();
    }

    /**
     * Mostrar (1.2) - wrapper que prepara la vista con la lista
     */
    private void mostrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Estudiante> estudiantes = obtener();
            request.setAttribute("estudiantes", estudiantes);
            request.getRequestDispatcher("/views/admin/gestionar-estudiantes.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
     * Mostrar con lista (versión existente reutilizable)
     */
    private void mostrar(HttpServletRequest request, HttpServletResponse response, List<Estudiante> estudiantes)
            throws ServletException, IOException {
        request.setAttribute("estudiantes", estudiantes);
        request.getRequestDispatcher("/views/admin/gestionar-estudiantes.jsp").forward(request, response);
    }

    // ----------------------- BÚSQUEDA -----------------------
    /**
     * Busca un estudiante por cédula (idPaciente) y reutiliza mostrar() para presentar resultado
     * (Se usa en el flujo 2 para localizar un estudiante antes de desactivar/activar o editar)
     */
    private void buscarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idPaciente = request.getParameter("cedula");
        try {
            if (idPaciente != null && !idPaciente.trim().isEmpty()) {
                // 3.1 / 2.x: obtenerEstudiante(cedula)
                Estudiante estudiante = factory.getEstudianteDAO().buscarPorIdPaciente(idPaciente);
                if (estudiante != null) {
                    List<Estudiante> estudiantes = java.util.Collections.singletonList(estudiante);
                    // Mostrar lista con el estudiante encontrado (útil para confirmaciones antes de desactivar/editar)
                    mostrar(request, response, estudiantes);
                    return;
                } else {
                    request.setAttribute("estudiantes", java.util.Collections.emptyList());
                    request.getSession().setAttribute("error", "No se encontró ningún estudiante con ese ID de paciente");
                    request.getRequestDispatcher("/views/admin/gestionar-estudiantes.jsp").forward(request, response);
                    return;
                }
            }
            // Si no hay filtro, mostrar todos (1.2)
            mostrar(request, response, obtener());
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error en la búsqueda: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
        }
    }

    // ----------------------- FLUJO CREACIÓN (1.3 - 1.7) -----------------------
    /**
     * 1.3: solicitarNuevoEstudiante - en este diseño POST "solicitarNuevoEstudiante" crea la entidad
     * Aquí mantenemos compat a la vista: si la petición es GET "NuevoEstudiante" mostramos formulario,
     * si es POST con acción 'solicitarNuevoEstudiante' creamos el estudiante.
     */
    private void solicitarNuevoEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // En este método centralizamos la creación y la respuesta (seguir la lógica previa)
        crearNuevoEstudiante(request, response);
    }

    /**
     * 1.4: mostrarFormulario - puede ser un forward hacia la misma vista con flag para mostrar modal
     * Prepara los datos necesarios para el formulario y marca la vista para abrir el modal.
     */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Preserve compatibility: allow modal param or servlet to instruct showing the form
            request.setAttribute("modal", request.getParameter("modal") != null ? request.getParameter("modal") : "nuevo");
            // Flag used by JSP to know that the servlet requested showing the "nuevo" modal
            request.setAttribute("mostrarNuevoEstudianteForm", true);
            // 1.2: mostrar lista junto al formulario
            mostrar(request, response, obtener());
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al mostrar formulario: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
        }
    }

    /**
     * 1.5: creaNuevoEstudiante(datos) - construir la entidad Estudiante desde request
     * Separado de la persistencia para seguir el diagrama y facilitar pruebas.
     */
    private Estudiante creaNuevoEstudiante(HttpServletRequest request) {
        String idPaciente = request.getParameter("cedula");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Construir objeto entidad (1.5)
        Estudiante nuevo = new Estudiante(idPaciente, nombre, apellido, email);
        if (password != null && !password.trim().isEmpty()) {
            nuevo.setPasswordEstudiante(password);
        }
        // Por defecto, mantener activo true
        nuevo.setActivo(true);
        return nuevo;
    }

    /**
     * 1.6: crearNuevoEstudiante(datosEstudiante) - persistir en BD
     * 1.7: mostrarConfirmacion
     */
    private void crearNuevoEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1.5: construir entidad
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
            // Persistir en la BD (1.6)
            factory.getEstudianteDAO().create(nuevoEstudiante);

            // 1.7: mostrarConfirmacion
            mostrarConfirmacion(request, "Estudiante creado exitosamente");
            // 1.2/1.7: actualizarVista
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al crear estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
        }
    }

    private void mostrarConfirmacion(HttpServletRequest request, String mensaje) {
        request.getSession().setAttribute("mensaje", mensaje);
    }

    // ----------------------- FLUJO EDICIÓN (3) -----------------------
    /**
     * 3: editarEstudiante(cedula) / solicitarEdicionEstudiante - buscar y mostrar formulario para edición
     * 3.1: obtenerEstudiante(cedula)
     * 3.2: mostrarFormulario (editar)
     */
    private void editarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cedula = request.getParameter("cedula");
        String idParam = request.getParameter("id");

        try {
            Estudiante estudiante = null;
            // 3.1: obtenerEstudiante por cedula o id
            if (cedula != null && !cedula.trim().isEmpty()) {
                estudiante = factory.getEstudianteDAO().buscarPorIdPaciente(cedula);
            } else if (idParam != null && !idParam.trim().isEmpty()) {
                int id = Integer.parseInt(idParam);
                estudiante = factory.getEstudianteDAO().getById(id);
            }

            if (estudiante != null) {
                // 3.2: pasar entidad a la vista y marcar modal editar
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
     * 3.3 / 3.4 / 3.5: actualizarDatos + guardarEstudiante + notificarExitoEdicion
     */
    private void actualizarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            // 3.1/3.3: obtenerEstudiante(id) y actualizar campos permitidos
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

            // 3.5/3.4: actualizarVista
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al actualizar estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
        }
    }

    // ----------------------- FLUJO DESACTIVACIÓN / ACTIVACIÓN (2) -----------------------
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
                // 2.3: invertir estado y persistir
                estudiante.setActivo(!estudiante.isActivo());
                factory.getEstudianteDAO().update(estudiante);
                String estadoNuevo = estudiante.isActivo() ? "activado" : "desactivado";
                mostrarConfirmacion(request, "Estudiante " + estadoNuevo + " exitosamente");
            } else {
                request.getSession().setAttribute("error", "Estudiante no encontrado");
            }

            // 2.4: actualizarVista -> redirigir a la lista
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cambiar estado del estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
        }
    }
}
