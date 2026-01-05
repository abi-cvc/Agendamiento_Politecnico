package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.EstudianteDAO;
import model.dao.AdministradorDAO;
import model.entity.Estudiante;
import model.entity.Administrador;

import java.io.IOException;

/**
 * Servlet para manejar el login del sistema
 * Soporta login de Estudiantes y Administradores
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private EstudianteDAO estudianteDAO;
    private AdministradorDAO administradorDAO;
    
    @Override
    public void init() throws ServletException {
        estudianteDAO = new EstudianteDAO();
        administradorDAO = new AdministradorDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Obtener parámetros del formulario
        String rol = request.getParameter("rol");
        String identificacion = request.getParameter("identificacion");
        String password = request.getParameter("password");
        
        System.out.println("=== INTENTO DE LOGIN ===");
        System.out.println("Rol: " + rol);
        System.out.println("Identificación: " + identificacion);
        System.out.println("Password: " + (password != null ? "****" : "null"));
        
        try {
            if ("estudiante".equals(rol)) {
                loginEstudiante(request, response, identificacion, password);
            } else if ("admin".equals(rol)) {
                loginAdministrador(request, response, identificacion, password);
            } else if ("doctor".equals(rol)) {
                loginDoctor(request, response, identificacion, password);
            } else {
                request.setAttribute("error", "Rol no válido");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar login: " + e.getMessage());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
    
    /**
     * Login para estudiantes
     */
    private void loginEstudiante(HttpServletRequest request, HttpServletResponse response,
                                 String idPaciente, String password) 
            throws ServletException, IOException {
        
        Estudiante estudiante = estudianteDAO.validarCredenciales(idPaciente, password);
        
        if (estudiante != null) {
            // Login exitoso
            HttpSession session = request.getSession();
            session.setAttribute("usuario", estudiante);
            session.setAttribute("rol", "estudiante");
            session.setAttribute("nombreUsuario", estudiante.getNombreCompleto());
            session.setAttribute("idUsuario", estudiante.getIdEstudiante());
            
            System.out.println("✅ Login exitoso - Estudiante: " + estudiante.getNombreCompleto());
            
            // Redirigir directamente a inicio
            response.sendRedirect(request.getContextPath() + "/inicio.jsp");
        } else {
            // Login fallido
            System.out.println("❌ Login fallido - Credenciales incorrectas");
            request.setAttribute("error", "Identificación o correo incorrectos");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
    
    /**
     * Login para administradores
     */
    private void loginAdministrador(HttpServletRequest request, HttpServletResponse response,
                                   String idAdmin, String password) 
            throws ServletException, IOException {
        
        Administrador admin = administradorDAO.validarCredenciales(idAdmin, password);
        
        if (admin != null) {
            // Login exitoso
            HttpSession session = request.getSession();
            session.setAttribute("usuario", admin);
            session.setAttribute("rol", "admin");
            session.setAttribute("nombreUsuario", admin.getNombreCompleto());
            session.setAttribute("idUsuario", admin.getIdAdministrador());
            
            System.out.println("✅ Login exitoso - Administrador: " + admin.getNombreCompleto());
            
            // Redirigir a panel de admin (por ahora a inicio)
            response.sendRedirect(request.getContextPath() + "/inicio.jsp");
        } else {
            // Login fallido
            System.out.println("❌ Login fallido - Credenciales incorrectas");
            request.setAttribute("error", "ID de admin o contraseña incorrectos");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
    
    /**
     * Login para doctores (por implementar)
     */
    private void loginDoctor(HttpServletRequest request, HttpServletResponse response,
                            String cedula, String password) 
            throws ServletException, IOException {
        
        // TODO: Implementar login de doctores
        request.setAttribute("error", "Login de doctores aún no implementado");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirigir a index.jsp
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}
