<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mejores Doctores Calificados</title>
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
        <div class="admin-header">
            <h1>🏆 Top 10 Doctores Mejor Calificados</h1>
            <p>Ranking basado en las evaluaciones de los estudiantes</p>
        </div>

        <c:choose>
            <c:when test="${not empty mejoresCalificados}">
                <div class="ranking-lista">
                    <c:forEach var="doctor" items="${mejoresCalificados}" varStatus="status">
                        <div class="ranking-item ${status.index < 3 ? 'top-'.concat(status.index + 1) : ''}">
                            <div class="ranking-numero">${status.index + 1}</div>
                            <div class="ranking-info">
                                <h4>Dr(a). ${doctor.nombre} ${doctor.apellido}</h4>
                                <div class="ranking-stats">
                                    <span class="promedio">⭐ <fmt:formatNumber value="${doctor.promedio}" maxFractionDigits="2" /></span>
                                    <span class="total">📝 ${doctor.totalEvaluaciones} evaluaciones</span>
                                </div>
                            </div>
                            <div class="ranking-accion">
                                <a href="<%= request.getContextPath() %>/evaluaciones?accion=porDoctor&idDoctor=${doctor.idDoctor}" 
                                   class="btn btn-sm btn-primary">
                                    👁️ Ver Detalles
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <p>📭 No hay datos disponibles</p>
                    <p>Aún no se han registrado evaluaciones en el sistema</p>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="acciones-footer">
            <a href="<%= request.getContextPath() %>/evaluaciones?accion=listarAdmin" class="btn btn-secondary">
                ← Volver a Gestionar Evaluaciones
            </a>
        </div>
    </div>

    <footer>
        <div class="footer-bottom">
            <p>&copy; 2025 Bienestar Politécnico - Panel Administrador</p>
        </div>
    </footer>
    
    <script src="<%= request.getContextPath() %>/js/auth-temporal.js"></script>
</body>
</html>