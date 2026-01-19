<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<%--
    =========================================
    VISTA: citas-agendadas.jsp
    =========================================
    Muestra TODAS las citas del mes en un calendario visual
    Los datos son cargados por ConsultarCitaAsignadaController con vista=calendario
--%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Citas Agendadas - Bienestar Politécnico</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo_epn.png">

    <!-- CSS -->
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
                <a href="${pageContext.request.contextPath}/inicio.jsp" class="font-bold">Inicio</a>
            </li>
            <li class="flex">
                <a href="${pageContext.request.contextPath}/ConsultarCitaAsignadaController?vista=calendario" class="font-bold">Citas Agendadas</a>
            </li>
            <li class="flex">
                <a href="${pageContext.request.contextPath}/ConsultarCitaAsignadaController" class="font-bold">Atender Cita</a>
            </li>
            <li class="login mt-2 mb-2" id="authButton">
                <a href="${pageContext.request.contextPath}/index.jsp" class="font-bold">Login</a>
            </li>
        </ul>
    </nav>
</header>

<!-- Main Content -->
<main class="citas-agendadas-doctor-page">
    <h1 class="text-center mb-lg">
        📅 Calendario de Citas - ${nombreDoctor}
    </h1>

    <p class="text-center subtitle-text mb-2xl">
        Visualiza todas tus citas médicas del mes
    </p>

    <c:if test="${not empty error}">
        <div class="alert alert-error text-center mb-xl">
            <strong>Error:</strong> ${error}
        </div>
    </c:if>

    <!-- Estadísticas del mes -->
    <div class="container-lg mb-2xl">
        <div class="stats-grid grid grid-auto-md gap-lg">
            <div class="stat-card card">
                <div class="stat-icon">📋</div>
                <div class="stat-info">
                    <h3>${citasMes.size()}</h3>
                    <p>Total Citas</p>
                </div>
            </div>
            <div class="stat-card card">
                <div class="stat-icon">⏳</div>
                <div class="stat-info">
                    <h3>
                        <c:set var="agendadas" value="0"/>
                        <c:forEach var="cita" items="${citasMes}">
                            <c:if test="${cita.estadoCita == 'Agendada' || cita.estadoCita == 'Confirmada'}">
                                <c:set var="agendadas" value="${agendadas + 1}"/>
                            </c:if>
                        </c:forEach>
                        ${agendadas}
                    </h3>
                    <p>Agendadas</p>
                </div>
            </div>
            <div class="stat-card card">
                <div class="stat-icon">✅</div>
                <div class="stat-info">
                    <h3>
                        <c:set var="completadas" value="0"/>
                        <c:forEach var="cita" items="${citasMes}">
                            <c:if test="${cita.estadoCita == 'Completada'}">
                                <c:set var="completadas" value="${completadas + 1}"/>
                            </c:if>
                        </c:forEach>
                        ${completadas}
                    </h3>
                    <p>Completadas</p>
                </div>
            </div>
            <div class="stat-card card">
                <div class="stat-icon">❌</div>
                <div class="stat-info">
                    <h3>
                        <c:set var="canceladas" value="0"/>
                        <c:forEach var="cita" items="${citasMes}">
                            <c:if test="${cita.estadoCita == 'Cancelada'}">
                                <c:set var="canceladas" value="${canceladas + 1}"/>
                            </c:if>
                        </c:forEach>
                        ${canceladas}
                    </h3>
                    <p>Canceladas</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Listado de Citas -->
    <div class="container-lg">
        <h2 class="mb-xl">📋 Todas las Citas del Mes</h2>
        
        <c:choose>
            <c:when test="${empty citasMes}">
                <!-- Mensaje vacío -->
                <div class="mensaje-vacio card">
                    <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                        <line x1="16" y1="2" x2="16" y2="6"></line>
                        <line x1="8" y1="2" x2="8" y2="6"></line>
                        <line x1="3" y1="10" x2="21" y2="10"></line>
                    </svg>
                    <p class="text-center mt-lg">
                        No hay citas agendadas para este mes
                    </p>
                </div>
            </c:when>
            
            <c:otherwise>
                <!-- Lista de citas -->
                <div class="atender-citas-list">
                    <c:forEach var="cita" items="${citasMes}">
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
                                    <span class="icono">📅</span>
                                    <div>
                                        <small>Fecha</small>
                                        <strong>
                                            ${cita.fechaCita}
                                        </strong>
                                    </div>
                                </div>
                                
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
                                    <button class="btn btn-secondary btn-cancelar" 
                                            onclick="cancelarCita(${cita.idCita}, 'calendario')">
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
        &copy; 2026 Escuela Politécnica Nacional. Todos los derechos reservados.
    </p>
</footer>

<!-- JS -->
<script src="${pageContext.request.contextPath}/js/auth-temporal.js"></script>
<script src="${pageContext.request.contextPath}/js/atender-cita-nuevo.js"></script>
<script src="${pageContext.request.contextPath}/js/cancelar-cita-rest.js"></script>

<!-- Debug: Mostrar citas en consola -->
<script>
    console.log('========================================');
    console.log('🔍 DEBUG CITAS-AGENDADAS: Citas del doctor desde BD');
    console.log('========================================');
    
    // Verificar que los atributos existan
    console.log('🔍 citasMes existe: ${not empty citasMes}');
    console.log('🔍 citasMes es null: ${citasMes == null}');
    
    // Información general
    console.log('📊 Total de citas del mes: ${citasMes != null ? citasMes.size() : "NULL"}');
    console.log('👨‍⚕️ Doctor: ${nombreDoctor != null ? nombreDoctor : "NULL"}');
    console.log('📅 Mes consultado: ${mesActual != null ? mesActual : "NULL"}');
    
    <c:choose>
        <c:when test="${empty citasMes}">
            console.log('\n⚠️ NO SE ENCONTRARON CITAS');
            console.log('⚠️ Verifica que en la BD existan citas del doctor ID 9 en este mes');
            console.log('⚠️ SQL: SELECT * FROM cita WHERE id_doctor = 9 AND fecha_cita LIKE "2026-01%"');
        </c:when>
        <c:otherwise>
            // Mostrar cada cita con detalles
            <c:forEach var="cita" items="${citasMes}" varStatus="status">
                console.log('\n📋 CITA #${status.index + 1}:');
                console.log('  ID: ${cita.idCita}');
                console.log('  Fecha: ${cita.fechaCita}');
                console.log('  Hora: ${cita.horaCita}');
                console.log('  Estado: ${cita.estadoCita}');
                console.log('  Estudiante: ${not empty cita.estudiante ? cita.estudiante.nombreEstudiante : "N/A"} ${not empty cita.estudiante ? cita.estudiante.apellidoEstudiante : ""}');
                console.log('  Correo: ${not empty cita.estudiante ? cita.estudiante.correoEstudiante : "N/A"}');
                console.log('  Doctor: ${not empty cita.doctor ? cita.doctor.nombre : "N/A"} ${not empty cita.doctor ? cita.doctor.apellido : ""}');
                console.log('  Especialidad: ${not empty cita.especialidad ? cita.especialidad.titulo : "N/A"}');
                console.log('  Motivo: ${cita.motivoConsulta}');
                <c:if test="${not empty cita.observacionCita}">
                console.log('  Observación: ${cita.observacionCita}');
                </c:if>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    
    console.log('\n========================================');
    console.log('✅ Fin del debug CITAS-AGENDADAS');
    console.log('========================================\n');
</script>

</body>
</html>
