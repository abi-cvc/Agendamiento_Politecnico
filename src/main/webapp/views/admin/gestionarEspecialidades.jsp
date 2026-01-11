<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Gestionar Especialidades</title>
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
                <li class="login mt-2 mb-2" id="authButton">
                    <a href="<%= request.getContextPath() %>/LogoutServlet" class="font-bold">Cerrar Sesión</a>
                </li>
            </ul>
        </nav>
    </header>
    
    <div class="admin-container">
        <div class="admin-header">
            <h1>Gestionar Especialidades</h1>
            <a href="<%=request.getContextPath()%>/especialidades?accion=nuevo" class="btn btn-primary">
                ➕ Nueva Especialidad
            </a>
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

        <!-- Tabla de especialidades -->
        <c:choose>
            <c:when test="${not empty especialidades}">
                <table class="table-admin">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Ícono</th>
                            <th>Nombre</th>
                            <th>Título</th>
                            <th>Descripción</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="esp" items="${especialidades}">
                            <tr>
                                <td>${esp.idEspecialidad}</td>
                                <td style="font-size: 1.5rem;">${esp.icono}</td>
                                <td>${esp.nombre}</td>
                                <td>${esp.titulo}</td>
                                <td>
                                    <div class="text-truncate" title="${esp.descripcion}">
                                        ${esp.descripcion}
                                    </div>
                                </td>
                                <td>
                                    <div class="btn-actions">
                                        <a href="<%= request.getContextPath() %>/especialidades?accion=editar&id=${esp.idEspecialidad}" 
                                           class="btn btn-sm btn-warning">
                                            ✏️ Editar
                                        </a>
                                        <form action="<%= request.getContextPath() %>/especialidades" 
                                              method="post" 
                                              style="display: inline;"
                                              onsubmit="return confirm('¿Está seguro de eliminar esta especialidad?\n\nEsta acción no se puede deshacer.');">
                                            <input type="hidden" name="accion" value="eliminar">
                                            <input type="hidden" name="id" value="${esp.idEspecialidad}">
                                            <button type="submit" class="btn btn-sm btn-danger">
                                                🗑️ Eliminar
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <p>No hay especialidades registradas. </p>
                    <a href="<%= request.getContextPath() %>/especialidades?accion=nuevo" class="btn btn-primary mt-3">
                        ➕ Crear la primera especialidad
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <footer>
        <div class="footer-bottom">
            <p>&copy; 2025 Bienestar Politécnico - Panel Administrador</p>
        </div>
    </footer>
    
    <script src="<%= request.getContextPath() %>/js/admin-especialidades.js"></script>
</body>

</html>