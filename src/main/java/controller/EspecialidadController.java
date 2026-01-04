package controller;

import model.dao.EspecialidadDAO;
import model.entity.Especialidad;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/especialidades")
public class EspecialidadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EspecialidadDAO especialidadDAO;
	
	@Override
	public void init() throws ServletException {
		especialidadDAO = new EspecialidadDAO();
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
		
		if ("guardar".equals(accion)) {
			guardarEspecialidad(request, response);
		}
	}
	
	/**
	 * Lista todas las especialidades
	 */
	private void listarEspecialidades(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		List<Especialidad> especialidades = especialidadDAO.obtenerEspecialidades();
		request.setAttribute("especialidades", especialidades);
		request.getRequestDispatcher("especialidades.jsp").forward(request, response);
	}
	
	/**
	 * Obtiene una especialidad específica por nombre
	 */
	private void obtenerEspecialidad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String nombre = request.getParameter("nombre");
		Especialidad especialidad = especialidadDAO.obtenerPorNombre(nombre);
		
		if (especialidad != null) {
			request.setAttribute("especialidad", especialidad);
			request.getRequestDispatcher("detalle-especialidad.jsp").forward(request, response);
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
		especialidadDAO.guardar(especialidad);
		
		response.sendRedirect("especialidades?accion=listar");
	}
	
	/**
	 * Inicializa la base de datos con especialidades predefinidas
	 * Solo se ejecuta si no existen especialidades
	 */
	private void inicializarEspecialidades(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// Verificar si ya existen especialidades
		if (especialidadDAO.existenEspecialidades()) {
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
		
		// Guardar en la base de datos
		especialidadDAO.guardar(nutricion);
		especialidadDAO.guardar(odontologia);
		especialidadDAO.guardar(psicologia);
		especialidadDAO.guardar(medicinaGeneral);
		especialidadDAO.guardar(enfermeria);
		
		response.getWriter().println("Especialidades inicializadas correctamente.");
		response.getWriter().println("<br><a href='especialidades?accion=listar'>Ver especialidades</a>");
	}
}
