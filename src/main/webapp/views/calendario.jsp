<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="model.dao.DoctorDAO" %>
<%@ page import="model.dao.DisponibilidadDAO" %>
<%@ page import="model.entity.Doctor" %>
<%@ page import="model.entity.Disponibilidad" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<%
    // ===== CARGA DINÁMICA DE DOCTOR Y DISPONIBILIDAD =====
    String idDoctorStr = request.getParameter("idDoctor");
    String nombreEspecialidad = request.getParameter("especialidad");
    
    Doctor doctor = null;
    List<Disponibilidad> disponibilidades = null;
    List<LocalDate> fechasDisponibles = null;
    
    if (idDoctorStr != null && !idDoctorStr.isEmpty()) {
        try {
            int idDoctor = Integer.parseInt(idDoctorStr);
            DoctorDAO doctorDAO = new DoctorDAO();
            DisponibilidadDAO dispDAO = new DisponibilidadDAO();
            
            doctor = doctorDAO.obtenerPorId(idDoctor);
            disponibilidades = dispDAO.obtenerPorDoctor(idDoctor);
            fechasDisponibles = dispDAO.obtenerFechasDisponibles(idDoctor);
            
            request.setAttribute("doctor", doctor);
            request.setAttribute("disponibilidades", disponibilidades);
            request.setAttribute("fechasDisponibles", fechasDisponibles);
            request.setAttribute("nombreEspecialidad", nombreEspecialidad);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID de doctor inválido");
        }
    } else {
        request.setAttribute("error", "No se especificó un doctor");
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="<%= request.getContextPath() %>">
    <title>Bienestar Politécnico - Calendario de Disponibilidad</title>
    <link rel="icon" type="image/png" href="../images/logo_epn.png">
    <link rel="stylesheet" href="./css/framework.css">
    <link rel="stylesheet" href="./css/styles.css">
    
    <style>
        .calendario-section {
            padding: 4rem 2rem;
            max-width: 1000px;
            margin: 0 auto;
        }
        
        .doctor-info-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 2rem;
            border-radius: 12px;
            margin-bottom: 2rem;
            display: flex;
            align-items: center;
            gap: 2rem;
        }
        
        .doctor-avatar {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background: rgba(255,255,255,0.2);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 2rem;
        }
        
        .calendario-grid {
            display: grid;
            gap: 1rem;
        }
        
        .fecha-grupo {
            background: white;
            border-radius: 12px;
            padding: 1.5rem;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        .fecha-titulo {
            font-size: 1.2rem;
            color: #2c3e50;
            margin-bottom: 1rem;
            padding-bottom: 0.5rem;
            border-bottom: 2px solid #667eea;
        }
        
        .horarios-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
            gap: 1rem;
        }
        
        .horario-btn {
            padding: 1rem;
            background: #f8f9fa;
            border: 2px solid #dee2e6;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.3s ease;
            text-align: center;
        }
        
        .horario-btn:hover {
            background: #667eea;
            color: white;
            border-color: #667eea;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(102, 126, 234, 0.3);
        }
        
        .horario-btn.selected {
            background: #667eea;
            color: white;
            border-color: #667eea;
        }
        
        .no-disponibilidad {
            text-align: center;
            padding: 3rem;
            background: #f8f9fa;
            border-radius: 12px;
        }
        
        .form-motivo {
            background: white;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            margin-top: 2rem;
        }
        
        .form-motivo textarea {
            width: 100%;
            padding: 1rem;
            border: 2px solid #dee2e6;
            border-radius: 8px;
            resize: vertical;
            min-height: 100px;
            font-family: inherit;
        }
        
        .breadcrumb {
            padding: 1rem 0;
            color: #666;
        }
        
        .breadcrumb a {
            color: #667eea;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <header>
        <div class="logo">
            <img src="../images/logo.svg" alt="Logo">
        </div>
        <nav>
            <ul>
                <li><a href="../inicio.jsp" class="font-bold">Inicio</a></li>
                <li><a href="../especialidades.jsp" class="font-bold">Especialidades</a></li>
                <li><a href="../ConsultarCitasAgendadasController" class="font-bold">Mis Citas</a></li>
                <li><a href="../resenas.jsp" class="font-bold">Mis Reseñas</a></li>
                <li class="login mt-2 mb-2" id="authButton">
                    <a href="../index.jsp" class="font-bold">Login</a>
                </li>
            </ul>
        </nav>
    </header>

    <main>
        <div class="breadcrumb">
            <a href="../inicio.jsp">Inicio</a> / 
            <a href="../especialidades.jsp">Especialidades</a> / 
            <c:if test="${not empty nombreEspecialidad}">
                <a href="../doctores?accion=porEspecialidad&especialidad=${nombreEspecialidad}">Doctores</a> /
            </c:if>
            <span>Calendario</span>
        </div>
        
        <h1>Calendario de Disponibilidad</h1>
        <p>Selecciona una fecha y hora para tu cita</p>
    </main>

    <section class="calendario-section">
        <c:choose>
            <c:when test="${not empty doctor}">
                <!-- Información del Doctor -->
                <div class="doctor-info-header">
                    <div class="doctor-avatar">👨‍⚕️</div>
                    <div>
                        <h2>Dr(a). ${doctor.nombreCompleto}</h2>
                        <p>${doctor.especialidad.titulo}</p>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${not empty disponibilidades}">
                        <form id="formAgendarCita" action="<%= request.getContextPath() %>/agendarCita" method="POST">
                            <input type="hidden" name="idDoctor" value="${doctor.idDoctor}">
                            <input type="hidden" name="especialidad" value="${nombreEspecialidad}">
                            <input type="hidden" id="fechaSeleccionada" name="fechaCita" value="">
                            <input type="hidden" id="horaSeleccionada" name="horaCita" value="">
                            
                            <div class="calendario-grid">
                                <c:set var="fechaActual" value="" />
                                <c:forEach var="disp" items="${disponibilidades}">
                                    <c:if test="${disp.fecha != fechaActual}">
                                        <c:if test="${not empty fechaActual}">
                                            </div></div> <!-- Cerrar grupo anterior -->
                                        </c:if>
                                        <div class="fecha-grupo">
                                            <h3 class="fecha-titulo">${disp.fecha}</h3>
                                            <div class="horarios-grid">
                                        <c:set var="fechaActual" value="${disp.fecha}" />
                                    </c:if>
                                    
                                    <div class="horario-btn" 
                                         data-fecha="${disp.fecha}" 
                                         data-hora="${disp.horaInicio}"
                                         onclick="seleccionarHorario(this)">
                                        <strong>${disp.horaInicio}</strong>
                                        <div style="font-size: 0.85rem; color: #666;">
                                            ${disp.horaInicio} - ${disp.horaFin}
                                        </div>
                                    </div>
                                </c:forEach>
                                </div></div> <!-- Cerrar último grupo -->
                            </div>

                            <!-- Formulario de Motivo -->
                            <div class="form-motivo">
                                <h3>Motivo de la Consulta</h3>
                                <textarea 
                                    name="motivoConsulta" 
                                    placeholder="Describe brevemente el motivo de tu consulta..."
                                    required></textarea>
                                
                                <div style="margin-top: 1rem;">
                                    <button type="submit" class="btn btn-primary" id="btnAgendar" disabled>
                                        Agendar Cita
                                    </button>
                                    <a href="lista-doctores.jsp?especialidad=${nombreEspecialidad}" class="btn btn-secondary">
                                        Volver
                                    </a>
                                </div>
                            </div>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <div class="no-disponibilidad">
                            <h3>Sin disponibilidad</h3>
                            <p>Este doctor no tiene horarios disponibles en este momento.</p>
                            <a href="../doctores?accion=porEspecialidad&especialidad=${nombreEspecialidad}" 
                               class="btn btn-primary">
                                Ver otros doctores
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <div class="no-disponibilidad">
                    <h3>Error</h3>
                    <p>${error != null ? error : "No se pudo cargar la información del doctor"}</p>
                    <a href="../especialidades.jsp" class="btn btn-primary">Volver a Especialidades</a>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <footer>
        <div class="footer-main">
            <div class="footer-section">
                <div class="footer-logo">Bienestar Politécnico</div>
                <p class="footer-tagline">Cuidando de ti, cuidamos el futuro de la Politécnica.</p>
            </div>
        </div>
        <div class="footer-bottom">
            <p>&copy; 2025 Bienestar Politécnico. Todos los derechos reservados.</p>
        </div>
    </footer>

    <script src="../js/auth-temporal.js"></script>
    <script>
        let horarioSeleccionado = null;
        
        function seleccionarHorario(elemento) {
            // Remover selección anterior
            if (horarioSeleccionado) {
                horarioSeleccionado.classList.remove('selected');
            }
            
            // Seleccionar nuevo horario
            elemento.classList.add('selected');
            horarioSeleccionado = elemento;
            
            // Obtener fecha y hora
            const fecha = elemento.getAttribute('data-fecha');
            const hora = elemento.getAttribute('data-hora');
            
            // Actualizar campos ocultos
            document.getElementById('fechaSeleccionada').value = fecha;
            document.getElementById('horaSeleccionada').value = hora;
            
            // Habilitar botón de agendar
            document.getElementById('btnAgendar').disabled = false;
        }
        
        // Validar antes de enviar
        document.getElementById('formAgendarCita').addEventListener('submit', function(e) {
            if (!horarioSeleccionado) {
                e.preventDefault();
                alert('Por favor selecciona una fecha y hora');
                return false;
            }
        });
    </script>
</body>
</html>
