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

// ===== IMPORTS PARA CONSUMIR REST API =====
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import com.recursos.JacksonConfig;

/**
 * Controller para gestionar estudiantes desde el panel de administrador
 * Maneja: listar, buscar, crear, actualizar, cambiar estado
 * 
 * ACTUALIZADO: Ahora consume el servicio REST en lugar de acceder directamente al DAO
 */
@WebServlet(urlPatterns = {"/GestionarEstudiantes"})
public class GestionarEstudiantesController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // ===== COMENTADO: Ya no se usa directamente, se usa vía REST =====
    // private DAOFactory factory;
    
    // ===== NUEVO: Cliente REST para consumir RecursoEstudiante =====
    private Client client;
    private String baseUrl;

    @Override
    public void init() throws ServletException {
        // ===== COMENTADO: Inicialización antigua del DAO =====
        // factory = DAOFactory.getFactory();
        
        // ===== NUEVO: Inicializar cliente REST con JacksonConfig =====
        ClientConfig config = new ClientConfig();
        config.register(JacksonConfig.class); // ✅ IMPORTANTE: Registrar configurador de Jackson
        
        client = ClientBuilder.newClient(config);
        
        // URL base del servicio REST (ajustar según tu contexto)
        String contextPath = getServletContext().getContextPath();
        baseUrl = "http://localhost:8080" + contextPath + "/rest/estudiantes";
        System.out.println("✅ GestionarEstudiantesController inicializado con REST API: " + baseUrl);
        System.out.println("✅ Cliente Jersey configurado con JacksonConfig");
    }
    
    @Override
    public void destroy() {
        // Cerrar el cliente REST al destruir el servlet
        if (client != null) {
            client.close();
        }
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "gestionarEstudiantes"; // 1: gestionarEstudiantes() - Método inicial según diagrama
        }

        switch (accion) {
            case "gestionarEstudiantes":
                gestionarEstudiantes(request, response); // método principal
                break;
            case "buscar":
                buscarEstudiante(request, response); // Buscar: Filtro puntual por cédula
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
                // 2.2: confirmarDesactivacion()
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
            
            // Preparar datos para la vista (antes estaba en el JSP)
            prepararDatosVista(request, estudiantes, false, null);
            
            // 1.2: mostrar(estudiantes)
            mostrar(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al cargar estudiantes: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/inicio-admin.jsp");
        }
    }

    /**
     * 1.1: obtener(): estudiantes[]
     * ACTUALIZADO: Consume REST API en lugar de DAO directo
     */
    private List<Estudiante> obtener() throws Exception {
        // ===== ANTIGUO: Acceso directo al DAO =====
        // return factory.getEstudianteDAO().getAll();
        
        // ===== NUEVO: Consumir servicio REST =====
        try {
            WebTarget target = client.target(baseUrl);
            Response response = target.request(MediaType.APPLICATION_JSON).get();
            
            if (response.getStatus() == 200) {
                List<Estudiante> estudiantes = response.readEntity(new GenericType<List<Estudiante>>() {});
                System.out.println("✅ REST: Obtenidos " + estudiantes.size() + " estudiantes");
                return estudiantes;
            } else {
                throw new Exception("Error REST: " + response.getStatus());
            }
        } catch (Exception e) {
            System.err.println("❌ ERROR al consumir REST /estudiantes: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Prepara todos los datos necesarios para la vista
     * Lógica que antes estaba en el JSP ahora se centraliza aquí
     */
    private void prepararDatosVista(HttpServletRequest request, List<Estudiante> estudiantes, 
                                     boolean abrirModalCrear, Estudiante estudianteEditar) {
        // Asegurar que la lista nunca sea null
        if (estudiantes == null) {
            estudiantes = new java.util.ArrayList<>();
        }
        request.setAttribute("estudiantes", estudiantes);
        
        // Determinar si mostrar modal de creación
        request.setAttribute("abrirModalCrear", abrirModalCrear);
        
        // Determinar si mostrar modal de edición
        boolean abrirModalEditar = (estudianteEditar != null);
        request.setAttribute("abrirModalEditar", abrirModalEditar);
        request.setAttribute("estudianteEditar", estudianteEditar);
    }

    /**
     * 1.2: mostrar(estudiantes) = 2.4: actualizarVista
     */
    private void mostrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/admin/gestionar-estudiantes.jsp").forward(request, response);
    }

    /**
     * Busca un estudiante por cédula (idPaciente)
     * // 3.1: obtenerEstudiante() = y 2: desactivarEstudiante()
     * ACTUALIZADO: Consume REST API
     */
    private void buscarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idPaciente = request.getParameter("cedula");
        try {
            List<Estudiante> estudiantes;
            
            if (idPaciente != null && !idPaciente.trim().isEmpty()) {
                // ===== ANTIGUO: Acceso directo al DAO =====
                // Estudiante estudiante = factory.getEstudianteDAO().buscarPorIdPaciente(idPaciente);
                
                // ===== NUEVO: Consumir servicio REST /buscar/{cedula} =====
                WebTarget target = client.target(baseUrl + "/buscar/" + idPaciente);
                Response restResponse = target.request(MediaType.APPLICATION_JSON).get();
                
                if (restResponse.getStatus() == 200) {
                    Estudiante estudiante = restResponse.readEntity(Estudiante.class);
                    estudiantes = java.util.Collections.singletonList(estudiante);
                    System.out.println("✅ REST: Estudiante encontrado con cédula " + idPaciente);
                } else {
                    estudiantes = new java.util.ArrayList<>();
                    request.getSession().setAttribute("error", "No se encontró ningún estudiante con ese ID de paciente");
                }
            } else {
                // Si no hay filtro, listar todos
                estudiantes = obtener();
            }
            
            // Preparar datos para la vista
            prepararDatosVista(request, estudiantes, false, null);
            mostrar(request, response);
            
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
        // En este método centralizamos la creación y la respuesta
        crearNuevoEstudiante(request, response);
    }

    /**
     * 1.4: mostrarFormulario
     */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Estudiante> estudiantes = obtener();
            
            // Preparar datos para la vista con modal de creación abierto
            prepararDatosVista(request, estudiantes, true, null);
            
            mostrar(request, response);
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
        nuevo.setActivo(true);
        return nuevo;
    }

    /**
     * 1.6: crearNuevoEstudiante(datosEstudiante)
     * 1.7: mostrarConfirmacion
     * ACTUALIZADO: Consume REST API
     */
    private void crearNuevoEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idPaciente = request.getParameter("cedula");
            
            // Validación mínima
            if (idPaciente == null || idPaciente.trim().isEmpty()) {
                request.getSession().setAttribute("error", "Cédula (ID paciente) es requerida");
                response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
                return;
            }

            // ===== ANTIGUO: Validación con DAO directo =====
            // if (factory.getEstudianteDAO().existePorIdPaciente(idPaciente)) {
            //     request.getSession().setAttribute("error", "Ya existe un estudiante con ese ID de paciente");
            //     response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
            //     return;
            // }

            // 1.5: creaNuevoEstudiante(datos)
            Estudiante nuevoEstudiante = creaNuevoEstudiante(request);
            
            // ===== ANTIGUO: Crear con DAO directo =====
            // factory.getEstudianteDAO().create(nuevoEstudiante);
            
            // ===== NUEVO: Crear vía REST POST /estudiantes/add =====
            WebTarget target = client.target(baseUrl + "/add");
            Response restResponse = target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(nuevoEstudiante, MediaType.APPLICATION_JSON));
            
            if (restResponse.getStatus() == 200 || restResponse.getStatus() == 204) {
                System.out.println("✅ REST: Estudiante creado exitosamente");
                // 1.7: mostrarConfirmacion
                mostrarConfirmacion(request, "Estudiante creado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al crear estudiante: " + restResponse.getStatus());
            }
            
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error al crear estudiante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionarEstudiantes?accion=gestionarEstudiantes");
        }
    }

    /**
     * 1.7: mostrarConfirmacion
     */
    private void mostrarConfirmacion(HttpServletRequest request, String mensaje) {
        request.getSession().setAttribute("mensaje", mensaje);
    }

    /**
     * 3: editarEstudiante(cedula)
     * 3.1: obtenerEstudiante(cedula)
     * 3.2: mostrarFormulario (editar)
     * ACTUALIZADO: Consume REST API
     */
    private void editarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cedula = request.getParameter("cedula");
        String idParam = request.getParameter("id");

        try {
            Estudiante estudiante = null;
            
            // 3.1: obtenerEstudiante(cedula o id)
            if (cedula != null && !cedula.trim().isEmpty()) {
                // ===== ANTIGUO: Acceso directo al DAO =====
                // estudiante = factory.getEstudianteDAO().buscarPorIdPaciente(cedula);
                
                // ===== NUEVO: Consumir REST /buscar/{cedula} =====
                WebTarget target = client.target(baseUrl + "/buscar/" + cedula);
                Response restResponse = target.request(MediaType.APPLICATION_JSON).get();
                if (restResponse.getStatus() == 200) {
                    estudiante = restResponse.readEntity(Estudiante.class);
                    System.out.println("✅ REST: Estudiante encontrado para editar (cédula: " + cedula + ")");
                }
            } else if (idParam != null && !idParam.trim().isEmpty()) {
                int id = Integer.parseInt(idParam);
                
                // ===== ANTIGUO: Acceso directo al DAO =====
                // estudiante = factory.getEstudianteDAO().getById(id);
                
                // ===== NUEVO: Consumir REST /{id} =====
                WebTarget target = client.target(baseUrl + "/" + id);
                Response restResponse = target.request(MediaType.APPLICATION_JSON).get();
                if (restResponse.getStatus() == 200) {
                    estudiante = restResponse.readEntity(Estudiante.class);
                    System.out.println("✅ REST: Estudiante encontrado para editar (ID: " + id + ")");
                }
            }

            if (estudiante != null) {
                List<Estudiante> estudiantes = obtener();
                
                // 3.2: mostrarFormulario - modal editar
                prepararDatosVista(request, estudiantes, false, estudiante);
                mostrar(request, response);
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
     * ACTUALIZADO: Consume REST API
     */
    private void actualizarEstudiante(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String idPaciente = request.getParameter("cedula");
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            // ===== ANTIGUO: Obtener y actualizar con DAO directo =====
            // Estudiante estudiante = factory.getEstudianteDAO().getById(id);
            // if (estudiante != null) {
            //     estudiante.setNombreEstudiante(nombre);
            //     estudiante.setApellidoEstudiante(apellido);
            //     estudiante.setCorreoEstudiante(email);
            //     if (password != null && !password.trim().isEmpty()) {
            //         estudiante.setPasswordEstudiante(password);
            //     }
            //     // 3.4: guardarEstudiante
            //     factory.getEstudianteDAO().update(estudiante);
            //     // 3.5: notificarExitoEdicion
            //     mostrarConfirmacion(request, "Estudiante actualizado exitosamente");
            // } else {
            //     request.getSession().setAttribute("error", "Estudiante no encontrado");
            // }
            
            // ===== NUEVO: Primero obtener el estudiante vía REST =====
            WebTarget targetGet = client.target(baseUrl + "/" + id);
            Response getResponse = targetGet.request(MediaType.APPLICATION_JSON).get();
            
            if (getResponse.getStatus() == 200) {
                Estudiante estudiante = getResponse.readEntity(Estudiante.class);
                
                // Actualizar datos
                estudiante.setNombreEstudiante(nombre);
                estudiante.setApellidoEstudiante(apellido);
                estudiante.setCorreoEstudiante(email);
                if (password != null && !password.trim().isEmpty()) {
                    estudiante.setPasswordEstudiante(password);
                }
                
                // ===== NUEVO: Actualizar vía REST PUT /estudiantes/update =====
                WebTarget targetUpdate = client.target(baseUrl + "/update");
                Response updateResponse = targetUpdate.request(MediaType.APPLICATION_JSON)
                        .put(Entity.entity(estudiante, MediaType.APPLICATION_JSON));
                
                if (updateResponse.getStatus() == 200 || updateResponse.getStatus() == 204) {
                    System.out.println("✅ REST: Estudiante actualizado exitosamente");
                    // 3.5: notificarExitoEdicion
                    mostrarConfirmacion(request, "Estudiante actualizado exitosamente");
                } else {
                    request.getSession().setAttribute("error", "Error al actualizar estudiante: " + updateResponse.getStatus());
                }
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
     * ACTUALIZADO: Consume REST API
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
                
                // ===== ANTIGUO: Acceso directo al DAO =====
                // estudiante = factory.getEstudianteDAO().getById(id);
                
                // ===== NUEVO: Consumir REST /{id} =====
                WebTarget target = client.target(baseUrl + "/" + id);
                Response restResponse = target.request(MediaType.APPLICATION_JSON).get();
                if (restResponse.getStatus() == 200) {
                    estudiante = restResponse.readEntity(Estudiante.class);
                    System.out.println("✅ REST: Estudiante encontrado para cambiar estado (ID: " + id + ")");
                }
            } else if (cedula != null && !cedula.trim().isEmpty()) {
                // ===== ANTIGUO: Acceso directo al DAO =====
                // estudiante = factory.getEstudianteDAO().buscarPorIdPaciente(cedula);
                
                // ===== NUEVO: Consumir REST /buscar/{cedula} =====
                WebTarget target = client.target(baseUrl + "/buscar/" + cedula);
                Response restResponse = target.request(MediaType.APPLICATION_JSON).get();
                if (restResponse.getStatus() == 200) {
                    estudiante = restResponse.readEntity(Estudiante.class);
                    System.out.println("✅ REST: Estudiante encontrado para cambiar estado (cédula: " + cedula + ")");
                }
            }

            if (estudiante != null) {
                // 2.3: cambiarEstadoEstudiante(nuevoEstado)
                estudiante.setActivo(!estudiante.isActivo());
                
                // ===== ANTIGUO: Actualizar con DAO directo =====
                // factory.getEstudianteDAO().update(estudiante);
                
                // ===== NUEVO: Actualizar vía REST PUT /estudiantes/update =====
                WebTarget targetUpdate = client.target(baseUrl + "/update");
                Response updateResponse = targetUpdate.request(MediaType.APPLICATION_JSON)
                        .put(Entity.entity(estudiante, MediaType.APPLICATION_JSON));
                
                if (updateResponse.getStatus() == 200 || updateResponse.getStatus() == 204) {
                    String nuevoEstado = estudiante.isActivo() ? "activado" : "desactivado";
                    System.out.println("✅ REST: Estudiante " + nuevoEstado + " exitosamente");
                    mostrarConfirmacion(request, "Estudiante " + nuevoEstado + " exitosamente");
                } else {
                    request.getSession().setAttribute("error", "Error al cambiar estado: " + updateResponse.getStatus());
                }
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