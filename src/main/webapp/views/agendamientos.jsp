<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<%-- 
  Agendamientos JSP - Según diagrama de robustez
  Paso 6: mostrar(doctores)
  Los datos vienen desde AgendarCitasController.solicitarCita()
  que ejecuta: obtenerPorEspecialidad(idEspecialidad): doctores[] (paso 5)
  
  También muestra horarios (paso 9: mostrar(horarios))
--%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agendar Cita Médica - Bienestar Politécnico</title>

    <!-- CSS -->
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo_epn.png">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/framework.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">

    <!-- Google Font -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@400..700&display=swap" rel="stylesheet">
</head>

<body>

<!-- ================= HEADER ================= -->
<header>
    <div class="logo">
        <img src="<%= request.getContextPath() %>/images/logo.svg" alt="Logo">
    </div>
    <nav>
        <ul>
            <li><a href="<%= request.getContextPath() %>/inicio.html" class="font-bold">Inicio</a></li>
            <li><a href="<%= request.getContextPath() %>/AgendarCitasController" class="font-bold">Especialidades</a></li>
            <li><a href="<%= request.getContextPath() %>/ConsultarCitasAgendadasController" class="font-bold">Mis Citas</a></li>
            <li><a href="<%= request.getContextPath() %>/inicio.html#reseñas" class="font-bold">Reseñas</a></li>
            <li class="login mt-2 mb-2" id="authButton">
                <a href="<%= request.getContextPath() %>/index.html" class="font-bold">Login</a>
            </li>
        </ul>
    </nav>
</header>

<!-- ================= MAIN ================= -->
<main>
    <h1>Agendar Cita Médica</h1>
    <p>Bienvenido/a, <span id="userName">Usuario</span></p>
</main>

<!-- ================= AGENDAMIENTO ================= -->
<section class="agendamientos-page">
    <div class="agendamiento-container">

        <!-- FORMULARIO -->
        <div class="agendamiento-form">
            <h3>Nueva Cita</h3>
            <div id="formMessage" class="form-message"></div>

            <form id="agendamientoForm"
                  action="../AgendarCitasController"
                  method="post">
                  
                <%-- Paso 7 del diagrama: crearCita(idDoctor, fecha, motivo) --%>
                <input type="hidden" name="accion" value="crearCita">

                <div class="form-group">
                    <label for="especialidad">Especialidad</label>
                    <select id="especialidad" name="especialidad" required 
                            <c:if test="${not empty especialidadSeleccionada}">disabled</c:if>>
                        <option value="">Seleccione una especialidad</option>
                        <c:forEach var="esp" items="${especialidades}">
                            <option value="${esp.nombre}" 
                                    data-id="${esp.idEspecialidad}"
                                    <c:if test="${esp.nombre eq especialidadSeleccionada}">selected</c:if>>
                                ${esp.icono} ${esp.titulo}
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty especialidadSeleccionada}">
                        <input type="hidden" name="especialidad" value="${especialidadSeleccionada}">
                        <small class="text-muted">✅ Especialidad preseleccionada</small>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="doctor">Doctor</label>
                    <select id="doctor" name="doctor" required 
                            <c:if test="${empty doctoresDisponibles}">disabled</c:if>>
                        <c:choose>
                            <c:when test="${empty doctoresDisponibles}">
                                <option value="">Primero seleccione una especialidad</option>
                            </c:when>
                            <c:otherwise>
                                <option value="">Seleccione un doctor</option>
                                <c:forEach var="doc" items="${doctoresDisponibles}">
                                    <option value="${doc.idDoctor}" 
                                            data-email="${doc.email}"
                                            data-especialidad="${doc.especialidad.nombre}">
                                        ${doc.nombreCompleto}
                                    </option>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </select>
                </div>

                <div class="form-group">
                    <label for="fecha">Seleccione una fecha</label>
                    <input type="hidden" id="fecha" name="fecha" required>
                    
                    <!-- Calendario Visual -->
                    <div id="calendario" class="calendario-container" style="display: none;">
                        <div class="calendario-header">
                            <button type="button" class="btn-mes" id="mesAnterior">‹</button>
                            <h3 id="mesActual">Mes</h3>
                            <button type="button" class="btn-mes" id="mesSiguiente">›</button>
                        </div>
                        <div class="calendario-dias-semana">
                            <div>Dom</div>
                            <div>Lun</div>
                            <div>Mar</div>
                            <div>Mié</div>
                            <div>Jue</div>
                            <div>Vie</div>
                            <div>Sáb</div>
                        </div>
                        <div id="calendarioDias" class="calendario-dias">
                            <!-- Días generados por JavaScript -->
                        </div>
                    </div>
                    <p class="fecha-seleccionada" id="fechaSeleccionadaTexto" style="display: none;">
                        <strong>Fecha seleccionada:</strong> <span id="fechaTexto"></span>
                    </p>
                </div>

                <div class="form-group" style="display: none;">
                    <label for="hora">Hora</label>
                    <input type="time" id="hora" name="hora" required>
                </div>

                <div class="form-group">
                    <label for="motivo">Motivo de la consulta</label>
                    <textarea id="motivo" name="motivo"
                              placeholder="Describe brevemente el motivo de tu consulta"
                              required></textarea>
                </div>

                <button type="submit" class="btn-agendar" disabled>
                    Agendar Cita
                </button>
            </form>
        </div>

        <!-- HORARIOS -->
        <div class="horarios-disponibles">
            <h3>Horarios Disponibles</h3>
            <div id="horariosContainer" class="horarios-container">
                <p class="no-horarios">
                    Seleccione un doctor y fecha para ver los horarios disponibles
                </p>
            </div>
        </div>

    </div>
</section>

<!-- ================= FOOTER ================= -->
<footer>
    <div class="footer-main">
        <div class="footer-section">
            <div class="footer-logo">Bienestar Politécnico</div>
            <p class="footer-tagline">
                Cuidando de ti, cuidamos el futuro de la Politécnica.
            </p>
        </div>
    </div>

    <div class="footer-bottom">
        <p>
            &copy; 2025 Bienestar Politécnico - Escuela Politécnica Nacional.
            Todos los derechos reservados.
        </p>
    </div>
</footer>

<!-- ================= JS ================= -->
<script src="<%= request.getContextPath() %>/js/auth-temporal.js"></script>
<script src="<%= request.getContextPath() %>/js/agendamientos-calendario.js"></script>

</body>
</html>
