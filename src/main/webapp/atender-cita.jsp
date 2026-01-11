<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<%--
    =========================================
    VISTA: atender-cita.jsp
    =========================================
    Esta página SOLO muestra datos.
    Los datos son cargados por ConsultarCitaAsignadaController
    
    Según diagrama de robustez:
    3: mostrar(citasMes)
    7: mostrar(citasDiaDetallada)
--%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Atender Cita - Bienestar Politécnico</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo_epn.png">

    <!-- CSS (NO se modifica, solo rutas correctas) -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/framework.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@400..700&display=swap" rel="stylesheet">
</head>
<body>

<!-- Header -->
<header class="flex-between">
    <div class="logo">
        <img src="${pageContext.request.contextPath}/images/logo.svg" alt="Logo">
    </div>
    <nav>
        <ul>
            <li class="flex">
                <a href="${pageContext.request.contextPath}/inicio.html" class="font-bold">Inicio</a>
            </li>
            <li class="flex">
                <a href="${pageContext.request.contextPath}/ConsultarCitaAsignadaController?vista=calendario" class="font-bold">Citas Agendadas</a>
            </li>
            <li class="flex">
                <a href="${pageContext.request.contextPath}/ConsultarCitaAsignadaController" class="font-bold">Atender Cita</a>
            </li>
            <li class="login mt-2 mb-2" id="authButton">
                <a href="${pageContext.request.contextPath}/index.html" class="font-bold">Login</a>
            </li>
        </ul>
    </nav>
            </li>
        </ul>
    </nav>
            </li>
        </ul>
    </nav>
</header>

<!-- Main Content -->
<main class="atender-cita-page">
    <h1 class="text-center mb-lg">
        ${nombreDoctor}
    </h1>

    <p class="text-center subtitle-text mb-2xl">
        Atiende y completa las citas programadas
    </p>
    
    <c:if test="${not empty error}">
        <div class="alert alert-error text-center mb-xl">
            <strong>Error:</strong> ${error}
        </div>
    </c:if>

    <!-- Selector de fecha -->
    <div class="container-sm mb-2xl">
        <div class="date-selector-card">
            <form action="${pageContext.request.contextPath}/ConsultarCitaAsignadaController" method="GET" style="display:inline;">
                <input type="date" 
                       name="fecha" 
                       id="filtroFechaAtencion" 
                       class="date-input-hidden"
                       value="${fechaSeleccionada}"
                       onchange="this.form.submit()">
            </form>
            <div class="date-display-box" onclick="document.getElementById('filtroFechaAtencion').showPicker()">
                <div class="date-display-content">
                    <fmt:formatDate value="${fechaSeleccionadaDate}" pattern="d" var="dia"/>
                    <fmt:formatDate value="${fechaSeleccionadaDate}" pattern="MMMM" var="mes"/>
                    <span class="date-day">${dia}</span>
                    <span class="date-month">${mes}</span>
                    <span class="date-count">${citas.size()} cita${citas.size() != 1 ? 's' : ''}</span>
                </div>
            </div>
        </div>
    </div>

    <!-- Listado de Citas -->
    <div class="container-sm">
        
        <c:choose>
            <c:when test="${empty citas}">
                <!-- Mensaje vacío -->
                <div class="mensaje-vacio card">
                    <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                        <line x1="16" y1="2" x2="16" y2="6"></line>
                        <line x1="8" y1="2" x2="8" y2="6"></line>
                        <line x1="3" y1="10" x2="21" y2="10"></line>
                    </svg>
                    <p class="text-center mt-lg">
                        No hay citas para la fecha seleccionada
                    </p>
                </div>
            </c:when>
            
            <c:otherwise>
                <!-- Lista de citas -->
                <div class="atender-citas-list">
                    
                    <c:forEach var="cita" items="${citas}">
                        <div class="cita-card atender-card" data-id="${cita.idCita}" data-estado="${cita.estadoCita}">
                            
                            <!-- Header -->
                            <div class="cita-header">
                                <div class="cita-info-principal">
                                    <h3>
                                        <c:choose>
                                            <c:when test="${not empty cita.estudiante}">
                                                ${cita.estudiante.nombreEstudiante} ${cita.estudiante.apellidoEstudiante}
                                            </c:when>
                                            <c:otherwise>
                                                Paciente no asignado
                                            </c:otherwise>
                                        </c:choose>
                                    </h3>
                                    <p class="cita-especialidad">
                                        ${not empty cita.especialidad ? cita.especialidad.titulo : 'Sin especialidad'}
                                    </p>
                                </div>
                                
                                <!-- Badge de estado -->
                                <span class="badge-estado badge-${cita.estadoCita}">
                                    ${cita.estadoCita}
                                </span>
                            </div>
                            
                            <!-- Detalles -->
                            <div class="cita-detalles">
                                <div class="cita-dato">
                                    <span class="icono">🕐</span>
                                    <div>
                                        <small>Hora</small>
                                        <strong>${cita.horaCita}</strong>
                                    </div>
                                </div>
                                
                                <div class="cita-dato">
                                    <span class="icono">👨‍⚕️</span>
                                    <div>
                                        <small>Doctor Asignado</small>
                                        <strong>
                                            <c:choose>
                                                <c:when test="${not empty cita.doctor}">
                                                    Dr(a). ${cita.doctor.nombre} ${cita.doctor.apellido}
                                                </c:when>
                                                <c:otherwise>
                                                    Por asignar
                                                </c:otherwise>
                                            </c:choose>
                                        </strong>
                                    </div>
                                </div>
                                
                                <div class="cita-dato">
                                    <span class="icono">📧</span>
                                    <div>
                                        <small>Correo Estudiante</small>
                                        <strong>
                                            ${not empty cita.estudiante ? cita.estudiante.correoEstudiante : 'N/A'}
                                        </strong>
                                    </div>
                                </div>
                                
                                <div class="cita-dato-motivo">
                                    <span class="icono">📝</span>
                                    <div>
                                        <small>Motivo de consulta</small>
                                        <p>${cita.motivoConsulta}</p>
                                    </div>
                                </div>
                                
                                <c:if test="${not empty cita.observacionCita}">
                                    <div class="cita-observacion">
                                        <strong>Observaciones:</strong>
                                        <p>${cita.observacionCita}</p>
                                    </div>
                                </c:if>
                            </div>
                            
                            <!-- Acciones -->
                            <c:if test="${cita.estadoCita == 'Agendada' || cita.estadoCita == 'Confirmada'}">
                                <div class="cita-acciones">
                                    <button class="btn btn-primary btn-atender" 
                                            onclick="atenderCita(${cita.idCita})">
                                        Atender Cita
                                    </button>
                                    <button class="btn btn-secondary btn-cancelar" 
                                            onclick="cancelarCita(${cita.idCita}, 'atender')">
                                        Cancelar
                                    </button>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                    
                </div>
            </c:otherwise>
        </c:choose>
        
    </div>
</main>

<!-- Footer -->
<footer class="text-center p-2xl bg-primary text-white mt-3xl">
    <div class="footer-content">
        <h3>Escuela Politécnica Nacional</h3>
        <p>Departamento de Bienestar Estudiantil</p>
        <p>Email: bienestar@epn.edu.ec | Teléfono: (02) 297-6300 Ext. 1234</p>
        <p class="mt-lg">Horario de Atención: Lunes a Viernes, 8:00 AM - 5:00 PM</p>
    </div>
    <p class="mt-xl">
        &copy; 2024 Escuela Politécnica Nacional. Todos los derechos reservados.
    </p>
</footer>

<!-- JS -->
<script src="${pageContext.request.contextPath}/js/auth-temporal.js"></script>
<script src="${pageContext.request.contextPath}/js/atender-cita-nuevo.js"></script>
<script src="${pageContext.request.contextPath}/js/cancelar-cita.js"></script>

<!-- Debug: Mostrar citas en consola -->
<script>
    console.log('========================================');
    console.log('🔍 DEBUG ATENDER-CITA: Citas cargadas desde la BDD');
    console.log('========================================');
    
    // Información general
    console.log('📊 Total de citas del DÍA: ${citas.size()}');
    console.log('📅 Fecha seleccionada: ${fechaSeleccionada}');
    
    // Mostrar citas del día
    console.log('\n--- CITAS DEL DÍA ---');
    <c:forEach var="cita" items="${citas}" varStatus="status">
        console.log('\n📋 CITA DÍA #${status.index + 1}:');
        console.log('  ID: ${cita.idCita}');
        console.log('  Fecha: ${cita.fechaCita}');
        console.log('  Hora: ${cita.horaCita}');
        console.log('  Estado: ${cita.estadoCita}');
        console.log('  Estudiante: ${not empty cita.estudiante ? cita.estudiante.nombreEstudiante : "N/A"} ${not empty cita.estudiante ? cita.estudiante.apellidoEstudiante : ""}');
        console.log('  Doctor: ${not empty cita.doctor ? cita.doctor.nombre : "N/A"} ${not empty cita.doctor ? cita.doctor.apellido : ""}');
        console.log('  Especialidad: ${not empty cita.especialidad ? cita.especialidad.titulo : "N/A"}');
    </c:forEach>
    
    console.log('\n========================================');
    console.log('✅ Fin del debug ATENDER-CITA');
    console.log('========================================\n');
</script>

</body>
</html>
