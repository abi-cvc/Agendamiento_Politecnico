package controller;

import model.dao.DAOFactory;
import model.dao.ICitaDAO;
import model.dao.IDoctorDAO;
import model.dao.IDisponibilidadDAO;
import model.dao.IEspecialidadDAO;
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
 * ACTUALIZADO: Usa DAOFactory para obtener instancias de DAOs
 */
@WebServlet("/AgendarCitasController")
public class AgendarCitasController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Factory para obtener DAOs - Patrón Factory
	private DAOFactory factory;
	
	@Override
	public void init() throws ServletException {
		// Obtener factory una sola vez al inicializar el servlet
		factory = DAOFactory.getFactory();
	}

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        String accion = request.getParameter("accion");
        
        if (accion == null) {
            accion = "agendarCita"; // 1: agendarCita() - Método inicial según diagrama
        }

        try {
            switch (accion) {
                case "agendarCita":
                    agendarCita(request, response); // 1: Método principal del diagrama
                    break;
                case "solicitarCita":
                    solicitarCita(request, response); // 4: solicitarCita(idEspecialidad)
                    break;
                case "obtenerDoctores":
                    obtenerPorEspecialidad(request, response); // 5: obtenerPorEspecialidad
                    break;
                default:
                    agendarCita(request, response);
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
            if ("crearCita".equals(accion)) {
                // 7: crearCita(idDoctor, fecha, motivo) - Según diagrama de secuencia
                crearCita(request, response);
            } else if ("confirmar".equals(accion)) {
                // 10: confirmar(idHorario) - Confirmación final
                confirmarCita(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/AgendarCitasController?accion=agendarCita");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al agendar cita: " + e.getMessage());
            request.getRequestDispatcher("/views/agendamientos.jsp").forward(request, response);
        }
    }

    // ===== OPERACIONES SEGÚN DIAGRAMA DE ROBUSTEZ =====
    
    /**
     * 1: agendarCita() - Método inicial según diagrama de robustez
     * 2: obtener(): especialidades[]
     * 3: mostrar(especialidades)
     */
    private void agendarCita(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 2: obtener(): especialidades[] - Usando DAOFactory
        List<Especialidad> especialidades = factory.getEspecialidadDAO().getAll();
        
        // 3: mostrar(especialidades) - Enviar a la vista
        request.setAttribute("especialidades", especialidades);
        request.getRequestDispatcher("/views/especialidades.jsp").forward(request, response);
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
        List<Especialidad> especialidades = factory.getEspecialidadDAO().getAll();
        request.setAttribute("especialidades", especialidades);
        
        if (nombreEspecialidad != null && !nombreEspecialidad.trim().isEmpty()) {
            Especialidad espSeleccionada = factory.getEspecialidadDAO().obtenerPorNombre(nombreEspecialidad);
            
            if (espSeleccionada != null) {
                // 5: obtenerPorEspecialidad(idEspecialidad): doctores[]
                List<Doctor> doctores = factory.getDoctorDAO().obtenerPorEspecialidad(espSeleccionada);
                
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
            Especialidad especialidad = factory.getEspecialidadDAO().obtenerPorNombre(especialidadNombre);
            
            if (especialidad != null) {
                List<Doctor> doctores = factory.getDoctorDAO().obtenerPorEspecialidad(especialidad);
                request.setAttribute("doctores", doctores);
            }
        }
        
        request.getRequestDispatcher("/views/agendamientos.jsp").forward(request, response);
    }

    /**
     * 7: crearCita(idDoctor, fecha, motivo) - Según diagrama de secuencia
     * 8: obtenerHorariosDisponiblesPorDoctor(idDoctor): horarios[]
     * 9: mostrar(horarios)
     * 11: crearCita(cita) - Persistir en BD
     */
    private void crearCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 7: crearCita(idDoctor, fecha, motivo) - Obtener parámetros
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
            
            // Obtener entidades relacionadas usando Factory
            Especialidad especialidad = factory.getEspecialidadDAO().obtenerPorNombre(especialidadNombre);
            Doctor doctor = factory.getDoctorDAO().getById(idDoctor);
            
            if (especialidad == null || doctor == null) {
                throw new IllegalArgumentException("Especialidad o doctor no encontrado");
            }
            
            // 8: obtenerHorariosDisponiblesPorDoctor(idDoctor): horarios[]
            // Verificar disponibilidad
            boolean disponible = factory.getDisponibilidadDAO().verificarDisponibilidad(idDoctor, fecha, hora);
            
            if (!disponible) {
                request.setAttribute("error", "El horario seleccionado ya no está disponible");
                // 9: mostrar(horarios) - Mostrar horarios disponibles
                mostrarHorarios(request, response, idDoctor);
                return;
            }
            
            // 11: crearCita(cita) - Crear objeto Cita
            Cita cita = new Cita(fecha, hora, motivo, especialidad, doctor);
            
            // Validar la cita
            boolean creada = cita.crear();
            
            if (!creada) {
                throw new Exception("No se pudo validar la cita");
            }
            
            // Guardar en BD usando Factory + GenericDAO
            factory.getCitaDAO().create(cita);
            
            // ===== MARCAR HORARIO COMO NO DISPONIBLE (ORM) =====
            System.out.println("🔒 Bloqueando horario: " + fecha + " " + hora);
            
            // Buscar la disponibilidad exacta
            Disponibilidad disponibilidadOcupada = factory.getDisponibilidadDAO().obtenerPorDoctorYFechaYHora(
                idDoctor, 
                fecha, 
                hora
            );
            
            if (disponibilidadOcupada != null) {
                // Marcar como NO disponible usando Factory + GenericDAO
                disponibilidadOcupada.setDisponible(false);
                factory.getDisponibilidadDAO().update(disponibilidadOcupada);
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
     * 9: mostrar(horarios) - Mostrar horarios disponibles
     * 8: obtenerHorariosDisponiblesPorDoctor(idDoctor): horarios[]
     */
    private void mostrarHorarios(HttpServletRequest request, HttpServletResponse response, int idDoctor)
            throws ServletException, IOException {
        
        // 8: obtenerHorariosDisponiblesPorDoctor(idDoctor): horarios[]
        List<Disponibilidad> horarios = factory.getDisponibilidadDAO().obtenerPorDoctor(idDoctor);
        
        // 9: mostrar(horarios)
        request.setAttribute("horariosDisponibles", horarios);
        request.setAttribute("idDoctor", idDoctor);
        request.getRequestDispatcher("/views/agendamientos.jsp").forward(request, response);
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
        response.sendRedirect(request.getContextPath() + "/ConsultarCitasAgendadasController");
    }
    
    /**
     * 10: confirmar(idHorario) - Confirmación final desde formulario
     */
    private void confirmarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idHorarioStr = request.getParameter("idHorario");
        
        if (idHorarioStr != null) {
            int idHorario = Integer.parseInt(idHorarioStr);
            
            // Obtener la disponibilidad y confirmar
            Disponibilidad horario = factory.getDisponibilidadDAO().getById(idHorario);
            
            if (horario != null && horario.isDisponible()) {
                // Marcar como no disponible
                horario.setDisponible(false);
                factory.getDisponibilidadDAO().update(horario);
                
                request.setAttribute("mensaje", "Horario confirmado exitosamente");
            } else {
                request.setAttribute("error", "El horario ya no está disponible");
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/ConsultarCitasAgendadasController");
    }
}
