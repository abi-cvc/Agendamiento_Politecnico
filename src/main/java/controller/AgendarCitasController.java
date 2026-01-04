package controller;

import model.dao.CitaDAO;
import model.entity.Cita;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@WebServlet("/agendarCita")
public class AgendarCitasController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("solicitarCita".equals(accion)) {
            solicitarCita(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("agendarCita".equals(accion)) {
            agendarCita(request, response);
        }
    }

    // ===== OPERACIONES DEL CASO DE USO =====

    private void solicitarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Muestra el formulario
        request.getRequestDispatcher("agendarCita.jsp").forward(request, response);
    }

    private void agendarCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Recoge datos del formulario
        Cita cita = crearCita(request);

        // Persiste
        new CitaDAO().guardar(cita);

        // Confirma
        confirmar(request, response, cita);
    }

    private Cita crearCita(HttpServletRequest request) {

        Cita cita = new Cita();
        cita.setFechaCita(LocalDate.parse(request.getParameter("fecha")));
        cita.setHoraCita(LocalTime.parse(request.getParameter("hora")));
        cita.setMotivoConsulta(request.getParameter("motivo"));
        cita.setEstadoCita("Agendada");
        cita.setObservacionCita("Sin observaciones");

        return cita;
    }

    private void confirmar(HttpServletRequest request, HttpServletResponse response, Cita cita)
            throws ServletException, IOException {

        request.setAttribute("cita", cita);
        request.getRequestDispatcher("confirmacion.jsp").forward(request, response);
    }
}
