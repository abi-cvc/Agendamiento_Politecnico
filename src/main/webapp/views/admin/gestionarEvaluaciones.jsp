<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consultar Evaluaciones</title>
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
                <li><a href="<%= request.getContextPath() %>/inicio-admin.jsp">Inicio</a></li>
                <li><a href="${pageContext.request.contextPath}/DoctorAdminController?accion=gestionarDoctores">Gestionar Doctores</a></li>
            	<li><a href="${pageContext.request.contextPath}/EstudianteAdminController?accion=gestionarEstudiantes" class="font-bold">Gestionar Estudiantes</a></li>
                <li><a href="<%= request.getContextPath() %>/especialidades?accion=listarAdmin">Gestionar Especialidades</a></li>
                <li><a href="<%= request.getContextPath() %>/evaluaciones?accion=listarAdmin" class="font-bold">Consultar Evaluaciones</a></li>
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
    
    <div class="admin-container">
        <div class="admin-header">
            <h1>📊 Gestionar Evaluaciones de Doctores</h1>
            <div class="admin-actions">
                <button onclick="mostrarMejoresCalificados()" class="btn btn-success">
                    ⭐ Mejores Calificados
                </button>
            </div>
        </div>

        <!-- Mensajes de éxito/error -->
        <c:if test="${not empty sessionScope.mensaje}">
            <div class="mensaje">
                ✓ ${sessionScope.mensaje}
            </div>
            <c:remove var="mensaje" scope="session" />
        </c:if>

        <c:if test="${not empty sessionScope.error}">
            <div class="mensaje error">
                ✗ ${sessionScope.error}
            </div>
            <c:remove var="error" scope="session" />
        </c:if>

        <!-- Filtros -->
        <div class="filtros-evaluaciones">
            <h3>🔍 Buscar por Doctor</h3>
            <form method="get" action="<%= request.getContextPath() %>/evaluaciones" class="form-filtro">
                <input type="hidden" name="accion" value="porDoctor">
                <div class="form-group">
                    <label for="idDoctor">Seleccione un doctor:</label>
                    <select name="idDoctor" id="idDoctor" class="form-control" required>
                        <option value="">-- Seleccione un doctor --</option>
                        <c:forEach var="doctor" items="${doctores}">
                            <option value="${doctor.idDoctor}">
                                Dr(a). ${doctor.nombre} ${doctor.apellido} - ${doctor.especialidad.titulo}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Ver Evaluaciones</button>
            </form>
        </div>

        <!-- Estadísticas Generales -->
        <div class="estadisticas-generales">
            <h3>📈 Estadísticas Generales</h3>
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon">📝</div>
                    <div class="stat-info">
                        <h4>Total Evaluaciones</h4>
                        <p class="stat-number">${evaluaciones.size()}</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">⭐</div>
                    <div class="stat-info">
                        <h4>Promedio General</h4>
                        <p class="stat-number" id="promedioGeneral">--</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">👨‍⚕️</div>
                    <div class="stat-info">
                        <h4>Doctores Evaluados</h4>
                        <p class="stat-number" id="doctoresEvaluados">--</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Lista de Evaluaciones -->
        <div class="evaluaciones-lista">
            <h3>📋 Todas las Evaluaciones</h3>
            
            <c:choose>
                <c:when test="${not empty evaluaciones}">
                    <table class="table-admin">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Doctor</th>
                                <th>Especialidad</th>
                                <th>Estudiante</th>
                                <th>Calificación</th>
                                <th>Comentario</th>
                                <th>Fecha</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="eval" items="${evaluaciones}">
                                <tr>
                                    <td>${eval.idEvaluacion}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty eval.doctor}">
                                                Dr(a). ${eval.doctor.nombre} ${eval.doctor.apellido}
                                            </c:when>
                                            <c:otherwise>
                                                No especificado
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty eval.doctor && not empty eval.doctor.especialidad}">
                                                ${eval.doctor.especialidad.titulo}
                                            </c:when>
                                            <c:otherwise>
                                                --
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty eval.estudiante}">
                                                ${eval.estudiante.nombreEstudiante} ${eval.estudiante.apellidoEstudiante}
                                            </c:when>
                                            <c:otherwise>
                                                Anónimo
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <span class="calificacion-estrellas">
                                            <c:forEach begin="1" end="${eval.calificacion}">⭐</c:forEach>
                                            <c:forEach begin="${eval.calificacion + 1}" end="5">☆</c:forEach>
                                        </span>
                                        <span class="calificacion-numero">(${eval.calificacion}/5)</span>
                                    </td>
                                    <td>
                                        <div class="comentario-preview" title="${eval.comentario}">
                                            <c:choose>
                                                <c:when test="${not empty eval.comentario}">
                                                    ${eval.comentario.length() > 50 ? eval.comentario.substring(0, 50).concat('...') : eval.comentario}
                                                </c:when>
                                                <c:otherwise>
                                                    <em>Sin comentario</em>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                    <td>
                                        ${eval.fechaFormateada}
                                    </td>
                                    <td>
                                        <a href="<%= request.getContextPath() %>/evaluaciones?accion=porDoctor&idDoctor=${eval.doctor.idDoctor}" 
                                           class="btn btn-sm btn-info">
                                            👁️ Ver Detalles
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <p>📭 No hay evaluaciones registradas en el sistema.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <footer>
        <div class="footer-bottom">
            <p>&copy; 2025 Bienestar Politécnico - Panel Administrador</p>
        </div>
    </footer>
    
    <script src="<%= request.getContextPath() %>/js/admin-evaluaciones.js"></script>
</body>
</html>