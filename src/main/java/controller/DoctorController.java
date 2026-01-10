package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.DAOFactory;
import model.dao.EspecialidadDAO;
import model.dao.IDoctorDAO;
import model.dao.IEspecialidadDAO;
import model.entity.Doctor;
import model.entity.Especialidad;

import java.io.IOException;
import java.util.List;

/**
 * Controller para manejar las peticiones relacionadas con Doctores
 * Según el diagrama de robustez: "Devuelve doctor"
 * ACTUALIZADO: Usa DAOFactory para obtener instancias de DAOs
 */
@WebServlet("/doctores")
public class DoctorController extends HttpServlet {
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
            accion = "listar";
        }
        
        switch (accion) {
            case "listar":
                listarDoctores(request, response);
                break;
            case "porEspecialidad":
                listarPorEspecialidad(request, response);
                break;
            case "detalle":
                verDetalle(request, response);
                break;
            case "inicializar":
                inicializarDatos(request, response);
                break;
            default:
                listarDoctores(request, response);
                break;
        }
    }
    
    /**
     * Lista todos los doctores
     */
    private void listarDoctores(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Doctor> doctores = factory.getDoctorDAO().obtenerDoctoresActivos();
        request.setAttribute("doctores", doctores);
        request.getRequestDispatcher("/views/doctores.jsp").forward(request, response);
    }
    
    /**
     * Lista doctores por especialidad (según diagrama de robustez)
     * Este es el flujo: Selecciona especialidad → Muestra lista de doctores
     */
    private void listarPorEspecialidad(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombreEspecialidad = request.getParameter("especialidad");
        
        if (nombreEspecialidad != null && !nombreEspecialidad.isEmpty()) {
            // Obtener doctores por especialidad usando Factory
            List<Doctor> doctores = factory.getDoctorDAO().obtenerPorEspecialidad(nombreEspecialidad);
            
            // Obtener datos de la especialidad
            Especialidad especialidad = factory.getEspecialidadDAO().obtenerPorNombre(nombreEspecialidad);
            
            // Pasar datos a la vista
            request.setAttribute("doctores", doctores);
            request.setAttribute("especialidad", especialidad);
            request.setAttribute("nombreEspecialidad", nombreEspecialidad);
            
            // Mostrar JSP de lista de doctores
            request.getRequestDispatcher("/views/lista-doctores.jsp").forward(request, response);
        } else {
            // Si no hay especialidad, redirigir a especialidades
            response.sendRedirect("especialidades?accion=listar");
        }
    }
    
    /**
     * Muestra el detalle de un doctor
     */
    private void verDetalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int idDoctor = Integer.parseInt(idStr);
                Doctor doctor = factory.getDoctorDAO().getById(idDoctor);
                
                if (doctor != null) {
                    request.setAttribute("doctor", doctor);
                    request.getRequestDispatcher("/views/detalle-doctor.jsp").forward(request, response);
                } else {
                    response.sendRedirect("doctores?accion=listar");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("doctores?accion=listar");
            }
        } else {
            response.sendRedirect("doctores?accion=listar");
        }
    }
    
    /**
     * Inicializa datos de prueba para doctores
     */
    private void inicializarDatos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Obtener especialidades
            EspecialidadDAO espDAO = new EspecialidadDAO();
            Especialidad nutricion = espDAO.obtenerPorNombre("nutricion");
            Especialidad odontologia = espDAO.obtenerPorNombre("odontologia");
            Especialidad psicologia = espDAO.obtenerPorNombre("psicologia");
            Especialidad medicina = espDAO.obtenerPorNombre("medicina-general");
            
            // Crear doctores para Nutrición
            if (nutricion != null) {
                Doctor doc1 = new Doctor("1234567890", "María", "González", "maria.gonzalez@epn.edu.ec", nutricion);
                doc1.setTelefono("0987654321");
                doc1.setDescripcion("Nutricionista especializada en nutrición deportiva y clínica");
                factory.getDoctorDAO().create(doc1);
                
                Doctor doc2 = new Doctor("1234567891", "Carlos", "Ramírez", "carlos.ramirez@epn.edu.ec", nutricion);
                doc2.setTelefono("0987654322");
                doc2.setDescripcion("Experto en nutrición vegetariana y vegana");
                factory.getDoctorDAO().create(doc2);
            }
            
            // Crear doctores para Odontología
            if (odontologia != null) {
                Doctor doc3 = new Doctor("1234567892", "Ana", "Pérez", "ana.perez@epn.edu.ec", odontologia);
                doc3.setTelefono("0987654323");
                doc3.setDescripcion("Odontóloga general con especialización en ortodoncia");
                factory.getDoctorDAO().create(doc3);
                
                Doctor doc4 = new Doctor("1234567893", "Luis", "Torres", "luis.torres@epn.edu.ec", odontologia);
                doc4.setTelefono("0987654324");
                doc4.setDescripcion("Especialista en endodoncia y estética dental");
                factory.getDoctorDAO().create(doc4);
            }
            
            // Crear doctores para Psicología
            if (psicologia != null) {
                Doctor doc5 = new Doctor("1234567894", "Laura", "Mendoza", "laura.mendoza@epn.edu.ec", psicologia);
                doc5.setTelefono("0987654325");
                doc5.setDescripcion("Psicóloga clínica especializada en terapia cognitivo-conductual");
                factory.getDoctorDAO().create(doc5);
                
                Doctor doc6 = new Doctor("1234567895", "Diego", "Salazar", "diego.salazar@epn.edu.ec", psicologia);
                doc6.setTelefono("0987654326");
                doc6.setDescripcion("Psicólogo con enfoque en salud mental juvenil");
                factory.getDoctorDAO().create(doc6);
            }
            
            // Crear doctores para Medicina General
            if (medicina != null) {
                Doctor doc7 = new Doctor("1234567896", "Patricia", "Vega", "patricia.vega@epn.edu.ec", medicina);
                doc7.setTelefono("0987654327");
                doc7.setDescripcion("Médico general con amplia experiencia en atención primaria");
                factory.getDoctorDAO().create(doc7);
            }
            
            request.setAttribute("mensaje", "Doctores inicializados exitosamente");
            request.getRequestDispatcher("doctores?accion=listar").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al inicializar doctores: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
