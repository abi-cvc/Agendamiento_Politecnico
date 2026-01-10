package controller;

import model.dao.DAOFactory;
import model.dao.IEspecialidadDAO;
import model.entity.Especialidad;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * Controller para manejar las peticiones relacionadas con Especialidades
 * ACTUALIZADO: Usa DAOFactory para obtener instancias de DAOs
 */
@WebServlet("/especialidades")
public class EspecialidadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DAOFactory factory;
	
	@Override
	public void init() throws ServletException {
		factory = DAOFactory.getFactory();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String accion = request.getParameter("accion");
		
		if (accion == null) {
			accion = "listar";
		}
		
		switch (accion) {
			case "listar":
				listarEspecialidades(request, response);
				break;
			case "listarAdmin":
				listarEspecialidadesAdmin(request, response);
				break;
			case "nuevo":
				mostrarFormularioNuevo(request, response);
				break;
			case "editar":
				mostrarFormularioEditar(request, response);
				break;
			case "obtener":
				obtenerEspecialidad(request, response);
				break;
			case "inicializar":
				inicializarEspecialidades(request, response);
				break;
			default:
				listarEspecialidades(request, response);
				break;
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String accion = request.getParameter("accion");
		
		if (accion == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no especificada");
			return;
		}
		
		switch (accion) {
			case "guardar":
				guardarEspecialidad(request, response);
				break;
			case "crear":
				crearEspecialidad(request, response);
				break;
			case "actualizar":
				actualizarEspecialidad(request, response);
				break;
			case "eliminar":
				eliminarEspecialidad(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
				break;
		}
	}
	
	/**
	 * Lista todas las especialidades
	 */
	private void listarEspecialidades(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		List<Especialidad> especialidades = factory.getEspecialidadDAO().getAll();
		request.setAttribute("especialidades", especialidades);
		request.getRequestDispatcher("/views/especialidades.jsp").forward(request, response);
	}
	
	/**
	 * Obtiene una especialidad específica por nombre
	 */
	private void obtenerEspecialidad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String nombre = request.getParameter("nombre");
		Especialidad especialidad = factory.getEspecialidadDAO().obtenerPorNombre(nombre);
		
		if (especialidad != null) {
			request.setAttribute("especialidad", especialidad);
			request.getRequestDispatcher("/views/detalle-especialidad.jsp").forward(request, response);
		} else {
			response.sendRedirect("especialidades?accion=listar");
		}
	}
	
	/**
	 * Guarda una nueva especialidad
	 */
	private void guardarEspecialidad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String nombre = request.getParameter("nombre");
		String titulo = request.getParameter("titulo");
		String descripcion = request.getParameter("descripcion");
		String servicios = request.getParameter("servicios");
		String icono = request.getParameter("icono");
		
		Especialidad especialidad = new Especialidad(nombre, titulo, descripcion, servicios, icono);
		factory.getEspecialidadDAO().create(especialidad);
		
		response.sendRedirect("especialidades?accion=listar");
	}
	
	/**
	 * Inicializa la base de datos con especialidades predefinidas
	 * Solo se ejecuta si no existen especialidades
	 */
	private void inicializarEspecialidades(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// Verificar si ya existen especialidades
		if (factory.getEspecialidadDAO().existenEspecialidades()) {
			response.getWriter().println("Ya existen especialidades en la base de datos.");
			return;
		}
		
		// Crear especialidades iniciales
		Especialidad nutricion = new Especialidad(
			"nutricion",
			"Nutrición",
			"Nuestro equipo de nutricionistas certificados te ayudará a alcanzar tus metas de salud mediante planes personalizados. Ya sea que busques mejorar tu rendimiento deportivo, controlar tu peso o manejar condiciones específicas de salud, te brindamos el acompañamiento necesario con un enfoque científico y humano.",
			"Evaluación nutricional personalizada|Planes de alimentación según objetivos|Seguimiento y control de peso|Asesoría en nutrición deportiva|Educación en autocuidado",
			"🥗"
		);
		
		Especialidad odontologia = new Especialidad(
			"odontologia",
			"Odontología",
			"Mantén tu sonrisa saludable con nuestros servicios odontológicos de calidad. Contamos con tecnología moderna y profesionales experimentados que te brindarán atención preventiva, diagnóstica y terapéutica. Tu salud bucal es fundamental para tu bienestar general, y estamos aquí para cuidarla.",
			"Limpieza dental y profilaxis|Tratamiento de caries|Extracciones dentales|Endodoncia|Prevención y educación en salud bucal",
			"🦷"
		);
		
		Especialidad psicologia = new Especialidad(
			"psicologia",
			"Psicología",
			"En un entorno confidencial y de apoyo, nuestros psicólogos te acompañan en tu proceso de crecimiento personal y bienestar emocional. Entendemos los desafíos que enfrentas como estudiante y te ofrecemos herramientas efectivas para manejar el estrés, la ansiedad y otros aspectos que afectan tu desarrollo académico y personal.",
			"Consulta psicológica individual|Manejo de estrés y ansiedad|Orientación vocacional|Apoyo en crisis emocionales|Talleres de desarrollo personal",
			"🧠"
		);
		
		Especialidad medicinaGeneral = new Especialidad(
			"medicina-general",
			"Medicina General",
			"Recibe atención médica integral de profesionales capacitados para diagnosticar, tratar y prevenir enfermedades. Desde consultas de rutina hasta seguimiento de condiciones crónicas, nuestro servicio de medicina general es tu primera línea de atención en salud, con derivaciones oportunas cuando sea necesario.",
			"Consulta médica general|Diagnóstico y tratamiento|Control de enfermedades crónicas|Certificados médicos|Derivaciones a especialistas",
			"⚕️"
		);
		
		Especialidad enfermeria = new Especialidad(
			"enfermeria",
			"Enfermería",
			"Nuestro equipo de enfermería profesional está disponible para brindarte cuidados de calidad, procedimientos de enfermería y educación en salud. Con calidez humana y profesionalismo, te asistimos en el control de tu salud, administración de medicamentos y prevención de enfermedades para que mantengas tu bienestar durante tu vida académica.",
			"Toma de signos vitales|Curaciones y procedimientos menores|Inyecciones y vacunación|Control de medicación|Educación en salud preventiva",
			"💉"
		);
		
		// Guardar en la base de datos usando Factory + GenericDAO
		factory.getEspecialidadDAO().create(nutricion);
		factory.getEspecialidadDAO().create(odontologia);
		factory.getEspecialidadDAO().create(psicologia);
		factory.getEspecialidadDAO().create(medicinaGeneral);
		factory.getEspecialidadDAO().create(enfermeria);
		
		response.getWriter().println("Especialidades inicializadas correctamente.");
		response.getWriter().println("<br><a href='especialidades?accion=listar'>Ver especialidades</a>");
	}
	
	/**
	 * Lista especialidades para el panel de administrador
	 */
	private void listarEspecialidadesAdmin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		List<Especialidad> especialidades = factory.getEspecialidadDAO().getAll();
		request.setAttribute("especialidades", especialidades);
		request.getRequestDispatcher("/views/admin/gestionarEspecialidades.jsp").forward(request, response);
	}
	
	/**
	 * Muestra el formulario para crear una nueva especialidad para el Admin
	 */
	private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// No hay especialidad (modo creación)
		request.setAttribute("especialidad", null);
		request.setAttribute("accion", "crear");
		request.getRequestDispatcher("/views/admin/form-especialidad.jsp").forward(request, response);
	}
	
	/**
	 * Muestra el formulario para editar una especialidad existente - Admin
	 */
	private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		int id = Integer.parseInt(request.getParameter("id"));
		Especialidad especialidad = factory.getEspecialidadDAO().getById(id);
		
		if (especialidad != null) {
			request.setAttribute("especialidad", especialidad);
			request.setAttribute("accion", "editar");
			request.getRequestDispatcher("/views/admin/form-especialidad.jsp").forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/especialidades?accion=listarAdmin");
		}
	}
	
	/**
	 * Crea una nueva especialidad
	 */
	private void crearEspecialidad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String nombre = request.getParameter("nombre");
		String titulo = request. getParameter("titulo");
		String descripcion = request.getParameter("descripcion");
		String servicios = request.getParameter("servicios");
		String icono = request.getParameter("icono");
		
		Especialidad especialidad = new Especialidad(nombre, titulo, descripcion, servicios, icono);
		factory.getEspecialidadDAO().create(especialidad);
		
		// Mensaje de éxito
		request.getSession().setAttribute("mensaje", "Especialidad creada exitosamente");
		response.sendRedirect(request.getContextPath() + "/especialidades?accion=listarAdmin");
	}
	
	/**
	 * Actualiza una especialidad existente
	 */
	private void actualizarEspecialidad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		int id = Integer.parseInt(request.getParameter("id"));
		Especialidad especialidad = factory.getEspecialidadDAO().getById(id);
		
		if (especialidad != null) {
			especialidad.setNombre(request.getParameter("nombre"));
			especialidad.setTitulo(request.getParameter("titulo"));
			especialidad.setDescripcion(request.getParameter("descripcion"));
			especialidad.setServicios(request.getParameter("servicios"));
			especialidad.setIcono(request.getParameter("icono"));
			
			factory.getEspecialidadDAO().update(especialidad);
			
			// Mensaje de éxito
			request.getSession().setAttribute("mensaje", "Especialidad actualizada exitosamente");
		}
		
		response.sendRedirect(request.getContextPath() + "/especialidades?accion=listarAdmin");
	}
	
	/**
	 * Elimina una especialidad
	 */
	private void eliminarEspecialidad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		int id = Integer.parseInt(request.getParameter("id"));
		
		try {
			factory.getEspecialidadDAO().delete(id);
			request.getSession().setAttribute("mensaje", "Especialidad eliminada exitosamente");
		} catch (Exception e) {
			request.getSession().setAttribute("error", "No se pudo eliminar la especialidad:  " + e.getMessage());
		}
		
		response.sendRedirect(request.getContextPath() + "/especialidades?accion=listarAdmin");
	}

}
