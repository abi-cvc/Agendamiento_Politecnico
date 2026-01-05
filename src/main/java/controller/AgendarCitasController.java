package controller;

import model.dao.CitaDAO;
import model.dao.DoctorDAO;
import model.dao.DisponibilidadDAO;
import model.dao.EspecialidadDAO;
import model.entity.Cita;
import model.entity.Doctor;
import model.entity.Disponibilidad;
import model.entity.Especialidad;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * AgendarCitasController - Según diagrama de robustez
 * Maneja todo el flujo de agendamiento de citas
 */
@WebServlet("/AgendarCitasController")
public class AgendarCitasController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// DAOs - Según diagrama de robustez
	private CitaDAO citaDAO;
	private EspecialidadDAO especialidadDAO;
	private DoctorDAO doctorDAO;
	private DisponibilidadDAO disponibilidadDAO;
	
	@Override
	public void init() throws ServletException {
		citaDAO = new CitaDAO();
		especialidadDAO = new EspecialidadDAO();
		doctorDAO = new DoctorDAO();
		disponibilidadDAO = new DisponibilidadDAO();
	}

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        String accion = request.getParameter("accion");
        
        if (accion == null) {
            accion = "mostrarEspecialidades";
        }

        try {
            switch (accion) {
                case "mostrarEspecialidades":
                    mostrarEspecialidades(request, response);
                    break;
                case "solicitarCita":
                    solicitarCita(request, response);
                    break;
                case "obtenerDoctores":
                    obtenerPorEspecialidad(request, response);
                    break;
                default:
                    mostrarEspecialidades(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {
            if ("agendarCita".equals(accion)) {
                procesarAgendamiento(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/especialidades.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al agendar cita: " + e.getMessage());
            request.getRequestDispatcher("/views/agendamientos.jsp").forward(request, response);
        }
    }

    // ===== OPERACIONES SEGÚN DIAGRAMA DE ROBUSTEZ =====
    
    /**
     * 3: mostrar(especialidades)
     * 2: obtener(): especialidades[]
     */
    private void mostrarEspecialidades(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Especialidad> especialidades = especialidadDAO.obtenerEspecialidades();
        request.setAttribute("especialidades", especialidades);
        request.getRequestDispatcher("/especialidades.jsp").forward(request, response);
    }

    /**
     * 4: solicitarCita(idEspecialidad)
     * 5: obtenerPorEspecialidad(idEspecialidad): doctores[]
     * 6: mostrar(doctores)
     */
    private void solicitarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombreEspecialidad = request.getParameter("especialidad");
        
        // 2: obtener(): especialidades[]
        List<Especialidad> especialidades = especialidadDAO.obtenerEspecialidades();
        request.setAttribute("especialidades", especialidades);
        
        if (nombreEspecialidad != null && !nombreEspecialidad.trim().isEmpty()) {
            Especialidad espSeleccionada = especialidadDAO.obtenerPorNombre(nombreEspecialidad);
            
            if (espSeleccionada != null) {
                // 5: obtenerPorEspecialidad(idEspecialidad): doctores[]
                List<Doctor> doctores = doctorDAO.obtenerPorEspecialidad(espSeleccionada);
                
                request.setAttribute("especialidadSeleccionada", nombreEspecialidad);
                request.setAttribute("especialidadObj", espSeleccionada);
                request.setAttribute("doctoresDisponibles", doctores);
            }
        }
        
        // 6: mostrar(doctores)
        request.getRequestDispatcher("/views/agendamientos.jsp").forward(request, response);
    }
    
    /**
     * 5: obtenerPorEspecialidad(idEspecialidad): doctores[]
     */
    private void obtenerPorEspecialidad(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String especialidadNombre = request.getParameter("especialidad");
        
        if (especialidadNombre != null && !especialidadNombre.trim().isEmpty()) {
            Especialidad especialidad = especialidadDAO.obtenerPorNombre(especialidadNombre);
            
            if (especialidad != null) {
                List<Doctor> doctores = doctorDAO.obtenerPorEspecialidad(especialidad);
                request.setAttribute("doctores", doctores);
            }
        }
        
        request.getRequestDispatcher("/views/agendamientos.jsp").forward(request, response);
    }

    /**
     * 7: crear(idDoctor, fecha, motivo)
     * 11: crearCita(cita)
     * Según diagrama de robustez
     */
    private void procesarAgendamiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Obtener parámetros del formulario
            String idDoctorStr = request.getParameter("doctor");
            String especialidadNombre = request.getParameter("especialidad");
            String fechaStr = request.getParameter("fecha");
            String horaStr = request.getParameter("hora");
            String motivo = request.getParameter("motivo");
            
            // Validar parámetros
            if (idDoctorStr == null || especialidadNombre == null || 
                fechaStr == null || horaStr == null || motivo == null) {
                throw new IllegalArgumentException("Faltan datos obligatorios");
            }
            
            // Parsear datos
            int idDoctor = Integer.parseInt(idDoctorStr);
            LocalDate fecha = LocalDate.parse(fechaStr);
            LocalTime hora = LocalTime.parse(horaStr);
            
            // Obtener entidades relacionadas
            Especialidad especialidad = especialidadDAO.obtenerPorNombre(especialidadNombre);
            Doctor doctor = doctorDAO.obtenerPorId(idDoctor);
            
            if (especialidad == null || doctor == null) {
                throw new IllegalArgumentException("Especialidad o doctor no encontrado");
            }
            
            // Verificar disponibilidad (8: obtenerHorariosDisponiblesPorDoctor)
            boolean disponible = disponibilidadDAO.verificarDisponibilidad(idDoctor, fecha, hora);
            
            if (!disponible) {
                request.setAttribute("error", "El horario seleccionado ya no está disponible");
                solicitarCita(request, response);
                return;
            }
            
            // 11: crearCita(cita)
            Cita cita = new Cita(fecha, hora, motivo, especialidad, doctor);
            
            // 7: crear(idDoctor, fecha, motivo)
            boolean creada = cita.crear();
            
            if (!creada) {
                throw new Exception("No se pudo validar la cita");
            }
            
            // Guardar en BD usando ORM
            citaDAO.guardar(cita);
            
            // ===== MARCAR HORARIO COMO NO DISPONIBLE (ORM) =====
            System.out.println("🔒 Bloqueando horario: " + fecha + " " + hora);
            
            // Buscar la disponibilidad exacta
            Disponibilidad disponibilidadOcupada = disponibilidadDAO.obtenerPorDoctorYFechaYHora(
                idDoctor, 
                fecha, 
                hora
            );
            
            if (disponibilidadOcupada != null) {
                // Marcar como NO disponible usando ORM
                disponibilidadOcupada.setDisponible(false);
                disponibilidadDAO.actualizar(disponibilidadOcupada);
                System.out.println("✅ Horario bloqueado exitosamente - disponible = false");
            } else {
                System.out.println("⚠️ No se encontró disponibilidad para bloquear");
            }
            
            // 10: confirmar(idHorario)
            confirmar(request, response, cita);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Datos numéricos inválidos");
            solicitarCita(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al crear la cita: " + e.getMessage());
            solicitarCita(request, response);
        }
    }

    /**
     * 7: crear(idDoctor, fecha, motivo)
     * Crea el objeto Cita según los parámetros
     */
    private Cita crearCita(HttpServletRequest request) {

        Cita cita = new Cita();
        cita.setFechaCita(LocalDate.parse(request.getParameter("fecha")));
        cita.setHoraCita(LocalTime.parse(request.getParameter("hora")));
        cita.setMotivoConsulta(request.getParameter("motivo"));
        cita.setEstadoCita("Agendada");
        cita.setObservacionCita("Sin observaciones");
        
        // ===== RELACIÓN ORM CON ESPECIALIDAD =====
        String nombreEspecialidad = request.getParameter("especialidad");
        if (nombreEspecialidad != null && !nombreEspecialidad.isEmpty()) {
        	Especialidad especialidad = especialidadDAO.obtenerPorNombre(nombreEspecialidad);
        	if (especialidad != null) {
        		cita.setEspecialidad(especialidad);  // ← Relación ORM
        	}
        }
        
        // ===== RELACIÓN ORM CON DOCTOR =====
        String idDoctorStr = request.getParameter("doctor");
        if (idDoctorStr != null && !idDoctorStr.isEmpty()) {
            try {
                int idDoctor = Integer.parseInt(idDoctorStr);
                Doctor doctor = doctorDAO.obtenerPorId(idDoctor);
                if (doctor != null) {
                    cita.setDoctor(doctor); // ← Relación ORM
                }
            } catch (NumberFormatException e) {
                // Ignorar si el ID no es válido
            }
        }

        return cita;
    }

    /**
     * 10: confirmar(idHorario)
     * Confirma la cita y muestra la información
     */
    private void confirmar(HttpServletRequest request, HttpServletResponse response, Cita cita)
            throws ServletException, IOException {

        request.setAttribute("exito", "Cita agendada exitosamente");
        request.setAttribute("cita", cita);
        
        // Redirigir a consultar citas
        response.sendRedirect(request.getContextPath() + "/consultar-citas.jsp?exito=true");
    }
}
