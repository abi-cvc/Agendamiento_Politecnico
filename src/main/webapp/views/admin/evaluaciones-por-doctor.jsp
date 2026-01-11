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
                <li><a href="<%= request.getContextPath() %>/doctores?accion=listarAdmin">Gestionar Doctores</a></li>
                <li><a href="<%= request.getContextPath() %>/estudiantes?accion=listarAdmin">Gestionar Estudiantes</a></li>
                <li><a href="<%= request.getContextPath() %>/especialidades?accion=listarAdmin" class="font-bold">Gestionar Especialidades</a></li>
                <li><a href="<%= request.getContextPath() %>/evaluaciones?accion=listarAdmin">Gestionar Evaluaciones</a></li>
                <li class="user-logged">
                <div class="user-menu">
                    <img src="<%= request.getContextPath() %>/images/user.svg" alt="Usuario" class="user-avatar">
                    <span class="user-name">Admin</span>
                    <div class="user-dropdown">
                        <div class="dropdown-header">
                            <strong>Admin Bienestar</strong>
                            <small>admin@epn.edu.ec</small>
                        </div>
                        <a href="<%= request.getContextPath() %>/index.jsp" onclick="logout(); return false;">
                            🚪 Cerrar Sesión
                        </a>
                    </div>
                </div>
            </li>
                
            </ul>
        </nav>
    </header>
    
    <!-- Usar contenedor más amplio -->
    <div class="reporte-container">
        <!-- Header del Doctor - Reutilizando estilos del reporte -->
        <div class="reporte-header">
            <div class="reporte-logo">
                <span style="font-size: 4rem;">👨‍⚕️</span>
            </div>
            <div class="reporte-title">
                <h1>${doctor.nombreCompleto}</h1>
                <p class="reporte-subtitle">${doctor.especialidad.titulo}</p>
                <c:if test="${not empty doctor.email}">
                    <p class="reporte-fecha">📧 ${doctor.email}</p>
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
        <section class="reporte-resumen">
            <h2>📊 Estadísticas de Evaluaciones</h2>
            
            <div class="resumen-cards">
                <!-- Promedio -->
                <div class="resumen-card promedio">
                    <div class="resumen-icon">⭐</div>
                    <div class="resumen-content">
                        <h3>Calificación Promedio</h3>
                        <p class="resumen-numero">
                            <c:choose>
                                <c:when test="${estadisticas.promedio != null}">
                                    ${String.format("%.2f", estadisticas.promedio)}
                                </c:when>
                                <c:otherwise>0.00</c:otherwise>
                            </c:choose>
                        </p>
                        <div class="estrellas-promedio">
                            <c:set var="promedioRedondeado" value="${Math.round(estadisticas.promedio)}" />
                            <c:forEach begin="1" end="${promedioRedondeado}">⭐</c:forEach>
                            <c:forEach begin="${promedioRedondeado + 1}" end="5">☆</c:forEach>
                        </div>
                    </div>
                </div>

                <!-- Total Evaluaciones -->
                <div class="resumen-card total">
                    <div class="resumen-icon">📝</div>
                    <div class="resumen-content">
                        <h3>Total Evaluaciones</h3>
                        <p class="resumen-numero">${estadisticas.total}</p>
                        <p class="resumen-texto">evaluaciones recibidas</p>
                    </div>
                </div>

                <!-- Calificación más común -->
                <div class="resumen-card estado">
                    <div class="resumen-icon">📈</div>
                    <div class="resumen-content">
                        <h3>Tendencia</h3>
                        <p class="resumen-numero">
                            <c:choose>
                                <c:when test="${estadisticas.promedio >= 4.5}">Excelente</c:when>
                                <c:when test="${estadisticas.promedio >= 4.0}">Muy Bueno</c:when>
                                <c:when test="${estadisticas.promedio >= 3.0}">Bueno</c:when>
                                <c:otherwise>Regular</c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </div>
            </div>
        </section>

        <!-- Distribución de Calificaciones -->
        <section class="reporte-distribucion">
            <h2>📊 Distribución de Calificaciones</h2>
            <div class="distribucion-tabla">
                <!-- 5 estrellas -->
                <div class="distribucion-fila">
                    <div class="distribucion-label">⭐⭐⭐⭐⭐</div>
                    <div class="distribucion-barra-container">
                        <div class="distribucion-barra" style="width: ${estadisticas.porcentaje_5}%"></div>
                    </div>
                    <div class="distribucion-stats">
                        <span class="distribucion-count">${estadisticas.estrellas_5}</span>
                        <span class="distribucion-porcentaje">(${String.format("%.1f", estadisticas.porcentaje_5)}%)</span>
                    </div>
                </div>
                
                <!-- 4 estrellas -->
                <div class="distribucion-fila">
                    <div class="distribucion-label">⭐⭐⭐⭐☆</div>
                    <div class="distribucion-barra-container">
                        <div class="distribucion-barra" style="width: ${estadisticas.porcentaje_4}%"></div>
                    </div>
                    <div class="distribucion-stats">
                        <span class="distribucion-count">${estadisticas.estrellas_4}</span>
                        <span class="distribucion-porcentaje">(${String.format("%.1f", estadisticas.porcentaje_4)}%)</span>
                    </div>
                </div>
                
                <!-- 3 estrellas -->
                <div class="distribucion-fila">
                    <div class="distribucion-label">⭐⭐⭐☆☆</div>
                    <div class="distribucion-barra-container">
                        <div class="distribucion-barra" style="width: ${estadisticas.porcentaje_3}%"></div>
                    </div>
                    <div class="distribucion-stats">
                        <span class="distribucion-count">${estadisticas.estrellas_3}</span>
                        <span class="distribucion-porcentaje">(${String.format("%.1f", estadisticas.porcentaje_3)}%)</span>
                    </div>
                </div>
                
                <!-- 2 estrellas -->
                <div class="distribucion-fila">
                    <div class="distribucion-label">⭐⭐☆☆☆</div>
                    <div class="distribucion-barra-container">
                        <div class="distribucion-barra" style="width: ${estadisticas.porcentaje_2}%"></div>
                    </div>
                    <div class="distribucion-stats">
                        <span class="distribucion-count">${estadisticas.estrellas_2}</span>
                        <span class="distribucion-porcentaje">(${String.format("%.1f", estadisticas.porcentaje_2)}%)</span>
                    </div>
                </div>
                
                <!-- 1 estrella -->
                <div class="distribucion-fila">
                    <div class="distribucion-label">⭐☆☆☆☆</div>
                    <div class="distribucion-barra-container">
                        <div class="distribucion-barra" style="width: ${estadisticas.porcentaje_1}%"></div>
                    </div>
                    <div class="distribucion-stats">
                        <span class="distribucion-count">${estadisticas.estrellas_1}</span>
                        <span class="distribucion-porcentaje">(${String.format("%.1f", estadisticas.porcentaje_1)}%)</span>
                    </div>
                </div>
            </div>
        </section>

        <!-- Lista de Evaluaciones -->
        <section class="reporte-comentarios">
            <h2>💬 Comentarios de Estudiantes</h2>
            
            <c:choose>
                <c:when test="${not empty evaluaciones}">
                    <div class="comentarios-grid">
                        <c:forEach var="eval" items="${evaluaciones}">
                            <div class="comentario-item">
                                <div class="comentario-header">
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
                                    <div class="comentario-rating">
                                        <c:forEach begin="1" end="${eval.calificacion}">⭐</c:forEach>
                                        <c:forEach begin="${eval.calificacion + 1}" end="5">☆</c:forEach>
                                    </div>
                                </div>
                                
                                <p class="comentario-texto">
                                    <c:choose>
                                        <c:when test="${not empty eval.comentario}">
                                            "${eval.comentario}"
                                        </c:when>
                                        <c:otherwise>
                                            <em>Sin comentario</em>
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                                
                                <div class="comentario-fecha">
                                    📅 ${eval.fechaFormateada}
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
        </section>

        <!-- Botón de Regreso -->
        <div class="reporte-acciones">
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