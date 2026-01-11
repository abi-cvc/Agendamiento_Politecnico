<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<%--
    =========================================
    VISTA: consultar-citas.jsp
    =========================================
    Esta página SOLO muestra datos.
    Los datos son cargados por ConsultarCitasAgendadasController
    
    Según diagrama de robustez:
    5: mostrar(citasAgendadasDetalladas)
    
    El controller ya hizo:
    - 1: consultarCitasAgendadas(idEstudiante)
    - 2: obtenerCitasPorEstudiante(idEstudiante)
    - 3: obtenerNombreDoctor(idDoctor) [ORM EAGER]
    - 4: obtenerNombreEspecialidad(idEspecialidad) [ORM EAGER]
    
    Este JSP solo recibe el atributo 'citas' y lo renderiza.
--%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Citas - Bienestar Politécnico</title>

    <!-- CSS -->
    <link rel="icon" type="image/png" href="images/logo_epn.png">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/framework.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">

    <!-- Google Font -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@400..700&display=swap" rel="stylesheet">
</head>

<body>

<header>
    <div class="logo">
        <img src="${pageContext.request.contextPath}/images/logo.svg" alt="Logo">
    </div>

    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/inicio.jsp" class="font-bold">Inicio</a></li>
            <li><a href="${pageContext.request.contextPath}/AgendarCitasController" class="font-bold">Especialidades</a></li>
            <li><a href="${pageContext.request.contextPath}/ConsultarCitasAgendadasController" class="font-bold">Mis Citas</a></li>
            <li><a href="${pageContext.request.contextPath}/resenas" class="font-bold">Mis Reseñas</a></li>
            <li class="login mt-2 mb-2" id="authButton">
                <a href="${pageContext.request.contextPath}/index.jsp" class="font-bold">Login</a>
            </li>
        </ul>
    </nav>
</header>

<main>
    <h1>Mis Citas Médicas</h1>
    <p>Bienvenido/a, <span id="userName">Usuario</span></p>
</main>

<section class="consultar-citas-page">
    <div class="consultar-container">

        <div class="citas-header">
            <h2>Historial de Citas</h2>

            <div class="filtros">
                <select id="filtroEstado" class="filtro-select">
                    <option value="todas">Todas las citas</option>
                    <option value="Pendiente">Pendientes</option>
                    <option value="Completada">Completadas</option>
                    <option value="Cancelada">Canceladas</option>
                </select>
            </div>
        </div>

        <!-- Lista de Citas Dinámica desde BD -->
        <div id="listaCitas" class="lista-citas-consulta">
            
            <c:choose>
                <c:when test="${empty citas}">
                    <p class="no-citas">No hay citas agendadas en el sistema</p>
                </c:when>
                <c:otherwise>
                    <!-- Iteración de citas con JSTL -->
                    <c:forEach var="cita" items="${citas}">
                        <div class="cita-card" data-estado="${cita.estadoCita}">
                            
                            <!-- Header de la cita -->
                            <div class="cita-header">
                                <div class="cita-fecha">
                                    <span class="fecha-icon">📅</span>
                                    <div>
                                        <div class="fecha-principal">${cita.fechaCita}</div>
                                        <div class="hora-cita">🕐 ${cita.horaCita}</div>
                                    </div>
                                </div>
                                
                                <!-- Badge de estado -->
                                <span class="badge-estado badge-${cita.estadoCita}">
                                    ${cita.estadoCita}
                                </span>
                            </div>
                            
                            <!-- Información de la especialidad -->
                            <div class="cita-info">
                                <div class="info-row">
                                    <span class="info-label">🏥 Especialidad:</span>
                                    <span class="info-value">
                                        <c:choose>
                                            <c:when test="${not empty cita.especialidad}">
                                                ${cita.especialidad.titulo}
                                            </c:when>
                                            <c:otherwise>
                                                No especificada
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                                
                                <!-- Información del doctor -->
                                <div class="info-row">
                                    <span class="info-label">👨‍⚕️ Doctor:</span>
                                    <span class="info-value">
                                        <c:choose>
                                            <c:when test="${not empty cita.doctor}">
                                                Dr(a). ${cita.doctor.nombre} ${cita.doctor.apellido}
                                            </c:when>
                                            <c:otherwise>
                                                Por asignar
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                                
                                <!-- Información del estudiante -->
                                <c:if test="${not empty cita.estudiante}">
                                    <div class="info-row">
                                        <span class="info-label">👤 Estudiante:</span>
                                        <span class="info-value">
                                            ${cita.estudiante.nombreEstudiante} ${cita.estudiante.apellidoEstudiante}
                                        </span>
                                    </div>
                                </c:if>
                                
                                <!-- Motivo de consulta -->
                                <div class="info-row motivo-consulta">
                                    <span class="info-label">📝 Motivo:</span>
                                    <span class="info-value">${cita.motivoConsulta}</span>
                                </div>
                                
                                <!-- Observaciones (si existen) -->
                                <c:if test="${not empty cita.observacionCita}">
                                    <div class="info-row observacion">
                                        <span class="info-label">💬 Observaciones:</span>
                                        <span class="info-value">${cita.observacionCita}</span>
                                    </div>
                                </c:if>
                            </div>
                            
                            <!-- Footer con ID y fecha de registro -->
                            <div class="cita-footer">
                                <small class="cita-id">ID: #${cita.idCita}</small>
                                <c:if test="${not empty cita.fechaRegistro}">
                                    <small class="fecha-registro">
                                        Registrada: ${cita.fechaRegistro.toLocalDate()} ${cita.fechaRegistro.toLocalTime().toString().substring(0, 5)}
                                    </small>
                                </c:if>
                            </div>
                            
                            <!-- Botón de cancelar (solo si está Agendada o Confirmada) -->
                            <c:if test="${cita.estadoCita == 'Agendada' || cita.estadoCita == 'Confirmada'}">
                                <div class="cita-acciones" style="margin-top: 1rem;">
                                    <button class="btn-cancelar-consulta" 
                                            onclick="cancelarCita(${cita.idCita}, 'consultar')">
                                        🚫 Cancelar Cita
                                    </button>
                                </div>
                            </c:if>
                            
                        </div>
                    </c:forEach>
                    
                    <!-- Contador de citas -->
                    <div class="citas-contador">
                        <p>Total de citas: <strong>${citas.size()}</strong></p>
                    </div>
                </c:otherwise>
            </c:choose>
            
        </div>

    </div>
</section>

<footer>
    <div class="footer-main">

        <!-- Marca -->
        <div class="footer-section">
            <div class="footer-logo">Bienestar Politécnico</div>
            <p class="footer-tagline">
                Cuidando de ti, cuidamos el futuro de la Politécnica.
            </p>

            <div class="social-links">
                <a href="https://facebook.com/DBPEPN" class="social-link" title="Facebook">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                        <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
                    </svg>
                </a>
                <a href="#" class="social-link" title="Instagram">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                        <path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z"/>
                    </svg>
                </a>
                <a href="#" class="social-link" title="Twitter/X">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                        <path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z"/>
                    </svg>
                </a>
            </div>
        </div>

        <!-- Enlaces -->
        <div class="footer-section">
            <h3>Enlaces Rápidos</h3>
            <ul class="footer-links">
                <li><a href="${pageContext.request.contextPath}/inicio.jsp">Inicio</a></li>
                <li><a href="${pageContext.request.contextPath}/AgendarCitasController">Especialidades</a></li>
                <li><a href="${pageContext.request.contextPath}/ConsultarCitasAgendadasController">Mis Citas</a></li>
                <li><a href="${pageContext.request.contextPath}/resenas">Mis Reseñas</a></li>
            </ul>
        </div>

        <!-- Contacto -->
        <div class="footer-section">
            <h3>Contacto</h3>
            
            <div class="contact-item">
                <div class="contact-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M1.5 8.67v8.58a3 3 0 003 3h15a3 3 0 003-3V8.67l-8.928 5.493a3 3 0 01-3.144 0L1.5 8.67z" />
                        <path d="M22.5 6.908V6.75a3 3 0 00-3-3h-15a3 3 0 00-3 3v.158l9.714 5.978a1.5 1.5 0 001.572 0L22.5 6.908z" />
                    </svg>
                </div>
                <div class="contact-info">
                    <span>Email</span>
                    <p>bienestar@epn.edu.ec</p>
                </div>
            </div>
            
            <div class="contact-item">
                <div class="contact-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                        <path fill-rule="evenodd" d="M1.5 4.5a3 3 0 013-3h1.372c.86 0 1.61.586 1.819 1.42l1.105 4.423a1.875 1.875 0 01-.694 1.955l-1.293.97c-.135.101-.164.249-.126.352a11.285 11.285 0 006.697 6.697c.103.038.25.009.352-.126l.97-1.293a1.875 1.875 0 011.955-.694l4.423 1.105c.834.209 1.42.959 1.42 1.82V19.5a3 3 0 01-3 3h-2.25C8.552 22.5 1.5 15.448 1.5 6.75V4.5z" clip-rule="evenodd" />
                    </svg>
                </div>
                <div class="contact-info">
                    <span>Teléfono</span>
                    <p>(+593) 2 2976 300 ext. 1132</p>
                </div>
            </div>
            
            <div class="contact-item">
                <div class="contact-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                        <path fill-rule="evenodd" d="M11.54 22.351l.07.04.028.016a.76.76 0 00.723 0l.028-.015.071-.041a16.975 16.975 0 001.144-.742 19.58 19.58 0 002.683-2.282c1.944-1.99 3.963-4.98 3.963-8.827a8.25 8.25 0 00-16.5 0c0 3.846 2.02 6.837 3.963 8.827a19.58 19.58 0 002.682 2.282 16.975 16.975 0 001.145.742zM12 13.5a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd" />
                    </svg>
                </div>
                <div class="contact-info">
                    <span>Ubicación</span>
                    <p>Facultad de Sistemas – EPN</p>
                </div>
            </div>
        </div>

    </div>

    <div class="footer-bottom">
        <p>&copy; 2025 Bienestar Politécnico - Escuela Politécnica Nacional</p>
    </div>
</footer>

<!-- JS -->
<script src="${pageContext.request.contextPath}/js/auth-temporal.js"></script>
<script src="${pageContext.request.contextPath}/js/consultar-citas.js"></script>
<script src="${pageContext.request.contextPath}/js/cancelar-cita.js"></script>

</body>
</html>
