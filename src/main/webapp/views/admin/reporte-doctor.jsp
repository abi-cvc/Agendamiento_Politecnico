<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reporte - ${doctor.nombreCompleto}</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo_epn.png">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/framework.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
    <style>
        @media print {
            header, .btn-accion, .back-button { display: none; }
            .reporte-container { max-width: 100%; box-shadow: none; }
        }
    </style>
</head>

<body>
    <header>
        <div class="logo">
            <img src="<%= request.getContextPath() %>/images/logo.svg" alt="Logo">
        </div>
        <nav>
            <ul>
                <li><a href="<%= request.getContextPath() %>/inicio.html">Inicio</a></li>
                <li><a href="<%= request.getContextPath() %>/evaluaciones?accion=listarAdmin">Volver a Evaluaciones</a></li>
                <li class="login mt-2 mb-2">
                    <a href="<%= request.getContextPath() %>/LogoutServlet" class="font-bold">Cerrar Sesión</a>
                </li>
            </ul>
        </nav>
    </header>
    
    <div class="reporte-container">
        <!-- Header del Reporte -->
        <div class="reporte-header">
            <div class="reporte-logo">
                <img src="<%= request.getContextPath() %>/images/logo_epn.png" alt="Logo EPN" class="logo-reporte">
            </div>
            <div class="reporte-title">
                <h1>📊 Reporte de Evaluaciones</h1>
                <p class="reporte-subtitle">Bienestar Politécnico - Escuela Politécnica Nacional</p>
                <p class="reporte-fecha">
                    Fecha de generación: <strong><fmt:formatDate value="${java.time.LocalDate.now()}" pattern="dd/MM/yyyy" /></strong>
                </p>
            </div>
        </div>

        <!-- Información del Doctor -->
        <div class="reporte-doctor-info">
            <h2>👨‍⚕️ Información del Doctor</h2>
            <div class="doctor-info-grid">
                <div class="info-item">
                    <span class="info-label">Nombre Completo:</span>
                    <span class="info-value">${doctor.nombreCompleto}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Especialidad:</span>
                    <span class="info-value">${doctor.especialidad.titulo}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Correo:</span>
                    <span class="info-value">${doctor.email}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Cédula:</span>
                    <span class="info-value">${doctor.cedula}</span>
                </div>
            </div>
        </div>

        <!-- Resumen Ejecutivo -->
        <div class="reporte-resumen">
            <h2>📈 Resumen Ejecutivo</h2>
            <div class="resumen-cards">
                <div class="resumen-card promedio">
                    <div class="resumen-icon">⭐</div>
                    <div class="resumen-content">
                        <h3>Calificación Promedio</h3>
                        <p class="resumen-numero">
                            <fmt:formatNumber value="${promedio}" maxFractionDigits="2" />
                        </p>
                        <div class="estrellas-promedio">
                            <c:set var="promedioRedondeado" value="${Math.round(promedio)}" />
                            <c:forEach begin="1" end="${promedioRedondeado}">⭐</c:forEach>
                            <c:forEach begin="${promedioRedondeado + 1}" end="5">☆</c:forEach>
                        </div>
                    </div>
                </div>

                <div class="resumen-card total">
                    <div class="resumen-icon">📝</div>
                    <div class="resumen-content">
                        <h3>Total Evaluaciones</h3>
                        <p class="resumen-numero">${total}</p>
                        <p class="resumen-texto">evaluaciones recibidas</p>
                    </div>
                </div>

                <div class="resumen-card estado">
                    <div class="resumen-icon">
                        <c:choose>
                            <c:when test="${estado == 'EXCELENTE'}">🏆</c:when>
                            <c:when test="${estado == 'MUY BUENO'}">✅</c:when>
                            <c:when test="${estado == 'BUENO'}">👍</c:when>
                            <c:when test="${estado == 'REGULAR'}">⚠️</c:when>
                            <c:otherwise>❗</c:otherwise>
                        </c:choose>
                    </div>
                    <div class="resumen-content">
                        <h3>Estado</h3>
                        <p class="resumen-numero estado-${estado}">${estado}</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Distribución de Calificaciones -->
        <div class="reporte-distribucion">
            <h2>📊 Distribución de Calificaciones</h2>
            <div class="distribucion-tabla">
                <c:forEach var="i" begin="5" end="1" step="-1">
                    <div class="distribucion-fila">
                        <div class="distribucion-label">
                            <c:forEach begin="1" end="${i}">⭐</c:forEach>
                        </div>
                        <div class="distribucion-barra-container">
                            <div class="distribucion-barra" 
                                 style="width: ${estadisticas['porcentaje_'.concat(i)]}%">
                            </div>
                        </div>
                        <div class="distribucion-stats">
                            <span class="distribucion-count">${estadisticas['estrellas_'.concat(i)]}</span>
                            <span class="distribucion-porcentaje">
                                (<fmt:formatNumber value="${estadisticas['porcentaje_'.concat(i)]}" maxFractionDigits="1" />%)
                            </span>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- Análisis y Recomendaciones -->
        <div class="reporte-analisis">
            <h2>💡 Análisis y Recomendaciones</h2>
            
            <c:choose>
                <c:when test="${promedio >= 4.5}">
                    <div class="analisis-card excelente">
                        <h3>✅ Desempeño Excelente</h3>
                        <ul>
                            <li>El doctor mantiene una calificación sobresaliente (${String.format("%.2f", promedio)}/5.0)</li>
                            <li>Alta satisfacción de los estudiantes</li>
                            <li>Recomendación: Mantener el nivel de atención actual</li>
                            <li>Sugerencia: Compartir mejores prácticas con otros profesionales</li>
                        </ul>
                    </div>
                </c:when>
                <c:when test="${promedio >= 4.0}">
                    <div class="analisis-card muy-bueno">
                        <h3>👍 Desempeño Muy Bueno</h3>
                        <ul>
                            <li>El doctor mantiene una calificación muy buena (${String.format("%.2f", promedio)}/5.0)</li>
                            <li>Buena aceptación por parte de los estudiantes</li>
                            <li>Recomendación: Continuar con el buen trabajo</li>
                            <li>Oportunidad: Identificar áreas específicas de mejora en comentarios</li>
                        </ul>
                    </div>
                </c:when>
                <c:when test="${promedio >= 3.0}">
                    <div class="analisis-card regular">
                        <h3>⚠️ Desempeño Regular</h3>
                        <ul>
                            <li>El doctor tiene una calificación aceptable (${String.format("%.2f", promedio)}/5.0)</li>
                            <li>Existen oportunidades de mejora</li>
                            <li>Recomendación: Revisar comentarios de estudiantes para identificar áreas problemáticas</li>
                            <li>Acción sugerida: Implementar plan de mejora en atención</li>
                            <li>Seguimiento: Monitorear evaluaciones en próximos 3 meses</li>
                        </ul>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="analisis-card atencion">
                        <h3>❗ Requiere Atención Inmediata</h3>
                        <ul>
                            <li>El doctor presenta una calificación baja (${String.format("%.2f", promedio)}/5.0)</li>
                            <li>Se requiere intervención urgente</li>
                            <li>Recomendación: Realizar entrevista con el doctor para identificar causas</li>
                            <li>Acción inmediata: Implementar plan de mejora supervisado</li>
                            <li>Seguimiento: Evaluación mensual obligatoria</li>
                            <li>Considerar: Capacitación adicional en atención al estudiante</li>
                        </ul>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Comentarios Destacados -->
        <div class="reporte-comentarios">
            <h2>💬 Comentarios Destacados</h2>
            <c:choose>
                <c:when test="${not empty evaluaciones}">
                    <div class="comentarios-grid">
                        <c:forEach var="eval" items="${evaluaciones}" end="9">
                            <div class="comentario-item">
                                <div class="comentario-header">
                                    <div class="comentario-rating">
                                        <c:forEach begin="1" end="${eval.calificacion}">⭐</c:forEach>
                                    </div>
                                    <div class="comentario-fecha">
                                        <fmt:formatDate value="${eval.fechaEvaluacion}" pattern="dd/MM/yyyy" />
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
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="no-comentarios">No hay comentarios disponibles</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Footer del Reporte -->
        <div class="reporte-footer">
            <div class="footer-info">
                <p><strong>Generado por:</strong> Sistema de Bienestar Politécnico</p>
                <p><strong>Fecha:</strong> <fmt:formatDate value="${java.time.LocalDateTime.now()}" pattern="dd/MM/yyyy HH:mm" /></p>
                <p><strong>Total de páginas:</strong> 1</p>
            </div>
            <div class="footer-firma">
                <div class="firma-linea"></div>
                <p>Firma y Sello Autorizado</p>
                <p class="firma-cargo">Coordinador/a de Bienestar Estudiantil</p>
            </div>
        </div>

        <!-- Botones de Acción -->
        <div class="reporte-acciones">
            <button onclick="window.print()" class="btn btn-primary">
                🖨️ Imprimir Reporte
            </button>
            <a href="<%= request.getContextPath() %>/evaluaciones?accion=porDoctor&idDoctor=${doctor.idDoctor}" 
               class="btn btn-secondary">
                ← Volver a Evaluaciones
            </a>
        </div>
    </div>

    <footer>
        <div class="footer-bottom">
            <p>&copy; 2025 Bienestar Politécnico - Escuela Politécnica Nacional</p>
        </div>
    </footer>
</body>
</html>