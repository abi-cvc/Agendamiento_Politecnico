<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Evaluaciones de ${doctor.nombreCompleto}</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo_epn.png">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/framework.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>

<body>
    <header>
        <div class="logo">
            <img src="<%= request.getContextPath() %>/images/logo.svg" alt="Logo">
        </div>
        <nav>
            <ul>
                <li><a href="<%= request.getContextPath() %>/inicio.jsp">Inicio</a></li>
                <li><a href="<%= request.getContextPath() %>/evaluaciones?accion=listarAdmin" class="font-bold">Volver a Evaluaciones</a></li>
                <li class="login mt-2 mb-2">
                    <a href="<%= request.getContextPath() %>/LogoutServlet" class="font-bold">Cerrar Sesión</a>
                </li>
            </ul>
        </nav>
    </header>
    
    <div class="admin-container">
        <!-- Header del Doctor -->
        <div class="doctor-header">
            <div class="doctor-info">
                <h1>👨‍⚕️ ${doctor.nombreCompleto}</h1>
                <p class="especialidad">${doctor.especialidad.titulo}</p>
                <c:if test="${not empty doctor.email}">
                    <p class="contacto">📧 ${doctor.email}</p>
                </c:if>
            </div>
            <div class="doctor-actions">
                <a href="<%= request.getContextPath() %>/evaluaciones?accion=reporteDoctor&idDoctor=${doctor.idDoctor}" 
                   class="btn btn-primary">
                    📄 Generar Reporte
                </a>
            </div>
        </div>

        <!-- Estadísticas del Doctor -->
        <div class="estadisticas-doctor">
            <h2>📊 Estadísticas de Evaluaciones</h2>
            
            <div class="stats-grid">
                <!-- Promedio -->
                <div class="stat-card destacado">
                    <div class="stat-icon">⭐</div>
                    <div class="stat-info">
                        <h3>Calificación Promedio</h3>
                        <p class="stat-number-large">
                            <c:choose>
                                <c:when test="${estadisticas.promedio != null}">
                                    ${String.format("%.2f", estadisticas.promedio)}
                                </c:when>
                                <c:otherwise>0.00</c:otherwise>
                            </c:choose>
                        </p>
                        <div class="estrellas-visual">
                            <c:set var="promedioRedondeado" value="${Math.round(estadisticas.promedio)}" />
                            <c:forEach begin="1" end="${promedioRedondeado}">⭐</c:forEach>
                            <c:forEach begin="${promedioRedondeado + 1}" end="5">☆</c:forEach>
                        </div>
                    </div>
                </div>

                <!-- Total Evaluaciones -->
                <div class="stat-card">
                    <div class="stat-icon">📝</div>
                    <div class="stat-info">
                        <h3>Total Evaluaciones</h3>
                        <p class="stat-number">${estadisticas.total}</p>
                    </div>
                </div>
            </div>

            <!-- Distribución por Estrellas -->
            <div class="distribucion-estrellas">
                <h3>Distribución de Calificaciones</h3>
                
                <!-- 5 estrellas -->
                <div class="estrella-bar">
                    <span class="estrella-label">5 ⭐</span>
                    <div class="progress-bar">
                        <div class="progress-fill" 
                             style="width: ${estadisticas.porcentaje_5}%">
                        </div>
                    </div>
                    <span class="estrella-count">
                        ${estadisticas.estrellas_5} 
                        (${String.format("%.1f", estadisticas.porcentaje_5)}%)
                    </span>
                </div>
                
                <!-- 4 estrellas -->
                <div class="estrella-bar">
                    <span class="estrella-label">4 ⭐</span>
                    <div class="progress-bar">
                        <div class="progress-fill" 
                             style="width: ${estadisticas.porcentaje_4}%">
                        </div>
                    </div>
                    <span class="estrella-count">
                        ${estadisticas.estrellas_4} 
                        (${String.format("%.1f", estadisticas.porcentaje_4)}%)
                    </span>
                </div>
                
                <!-- 3 estrellas -->
                <div class="estrella-bar">
                    <span class="estrella-label">3 ⭐</span>
                    <div class="progress-bar">
                        <div class="progress-fill" 
                             style="width: ${estadisticas.porcentaje_3}%">
                        </div>
                    </div>
                    <span class="estrella-count">
                        ${estadisticas.estrellas_3} 
                        (${String.format("%.1f", estadisticas.porcentaje_3)}%)
                    </span>
                </div>
                
                <!-- 2 estrellas -->
                <div class="estrella-bar">
                    <span class="estrella-label">2 ⭐</span>
                    <div class="progress-bar">
                        <div class="progress-fill" 
                             style="width: ${estadisticas.porcentaje_2}%">
                        </div>
                    </div>
                    <span class="estrella-count">
                        ${estadisticas.estrellas_2} 
                        (${String.format("%.1f", estadisticas.porcentaje_2)}%)
                    </span>
                </div>
                
                <!-- 1 estrella -->
                <div class="estrella-bar">
                    <span class="estrella-label">1 ⭐</span>
                    <div class="progress-bar">
                        <div class="progress-fill" 
                             style="width: ${estadisticas.porcentaje_1}%">
                        </div>
                    </div>
                    <span class="estrella-count">
                        ${estadisticas.estrellas_1} 
                        (${String.format("%.1f", estadisticas.porcentaje_1)}%)
                    </span>
                </div>
            </div>
        </div>

        <!-- Lista de Evaluaciones -->
        <div class="evaluaciones-detalle">
            <h2>💬 Comentarios de Estudiantes</h2>
            
            <c:choose>
                <c:when test="${not empty evaluaciones}">
                    <div class="evaluaciones-grid">
                        <c:forEach var="eval" items="${evaluaciones}">
                            <div class="evaluacion-card">
                                <div class="evaluacion-header">
                                    <div class="evaluacion-estudiante">
                                        <strong>
                                            <c:choose>
                                                <c:when test="${not empty eval.estudiante}">
                                                    ${eval.estudiante.nombreEstudiante} ${eval.estudiante.apellidoEstudiante}
                                                </c:when>
                                                <c:otherwise>
                                                    Estudiante Anónimo
                                                </c:otherwise>
                                            </c:choose>
                                        </strong>
                                    </div>
                                    <div class="evaluacion-calificacion">
                                        <c:forEach begin="1" end="${eval.calificacion}">⭐</c:forEach>
                                        <c:forEach begin="${eval.calificacion + 1}" end="5">☆</c:forEach>
                                    </div>
                                </div>
                                
                                <div class="evaluacion-body">
                                    <c:choose>
                                        <c:when test="${not empty eval.comentario}">
                                            <p class="comentario">"${eval.comentario}"</p>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="comentario sin-comentario"><em>Sin comentario</em></p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                
                                <div class="evaluacion-footer">
                                    <small class="fecha">
                                        📅 ${eval.fechaFormateada}
                                    </small>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <p>🔭 Este doctor aún no tiene evaluaciones.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Botón de Regreso -->
        <div class="acciones-footer">
            <a href="<%= request.getContextPath() %>/evaluaciones?accion=listarAdmin" class="btn btn-secondary">
                ← Volver a todas las evaluaciones
            </a>
        </div>
    </div>

    <footer>
        <div class="footer-bottom">
            <p>&copy; 2025 Bienestar Politécnico - Panel Administrador</p>
        </div>
    </footer>
</body>
</html>