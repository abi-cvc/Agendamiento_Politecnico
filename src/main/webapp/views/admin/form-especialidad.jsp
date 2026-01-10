<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<! DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${accion == 'editar' ? 'Editar' : 'Nueva'} Especialidad - Admin</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo_epn.png">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/framework.css">
    <link rel="stylesheet" href="<%= request. getContextPath() %>/styles.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/admin.css">
</head>
<body>
    <header>
        <div class="logo">
            <h2>Panel Administrador</h2>
        </div>
        <nav>
            <ul>
                <li><a href="<%= request.getContextPath() %>/admin/dashboard.jsp">Dashboard</a></li>
                <li><a href="<%= request.getContextPath() %>/especialidades?accion=listarAdmin" class="font-bold">Especialidades</a></li>
                <li><a href="<%= request.getContextPath() %>/admin/gestionar-doctores.jsp">Doctores</a></li>
                <li><a href="<%= request.getContextPath() %>/LogoutServlet">Cerrar Sesión</a></li>
            </ul>
        </nav>
    </header>

    <div class="form-container">
        <h1>${accion == 'editar' ? 'Editar':'Nueva'} Especialidad</h1>
        
        <form action="<%= request. getContextPath() %>/especialidades" method="post">
            <input type="hidden" name="accion" value="${accion == 'editar' ? 'actualizar' : 'crear'}">
            
            <c:if test="${accion == 'editar'}">
                <input type="hidden" name="id" value="${especialidad.idEspecialidad}">
            </c: if>
            
            <div class="form-group">
                <label for="nombre">Nombre* (identificador único)</label>
                <input type="text" 
                       id="nombre" 
                       name="nombre" 
                       value="${especialidad != null ? especialidad.nombre : ''}"
                       required
                       pattern="[a-z-]+"
                       placeholder="Ej: nutricion, medicina-general">
                <small>Solo minúsculas y guiones. Usado en URLs.</small>
            </div>
            
            <div class="form-group">
                <label for="titulo">Título*</label>
                <input type="text" 
                       id="titulo" 
                       name="titulo" 
                       value="${especialidad!=null?especialidad.titulo:''}"
                       required
                       maxlength="100"
                       placeholder="Ej: Nutrición">
                <small>Nombre que se muestra al público. </small>
            </div>
            
            <div class="form-group">
                <label for="descripcion">Descripción*</label>
                <textarea id="descripcion" 
                          name="descripcion" 
                          required
                          placeholder="Descripción detallada de la especialidad...">${especialidad != null ? especialidad. descripcion : ''}</textarea>
            </div>
            
            <div class="form-group">
                <label for="servicios">Servicios*</label>
                <textarea id="servicios" 
                          name="servicios" 
                          required
                          placeholder="Servicio 1|Servicio 2|Servicio 3">${especialidad != null ? especialidad. servicios : ''}</textarea>
                <small>Separar cada servicio con el símbolo | (pipe)</small>
            </div>
            
            <div class="form-group">
                <label for="icono">Ícono (emoji)</label>
                <input type="text" 
                       id="icono" 
                       name="icono" 
                       value="${especialidad != null ?  especialidad.icono : ''}"
                       maxlength="10"
                       placeholder="🥗">
                <small>Un emoji representativo.  Puedes copiar desde <a href="https://emojipedia.org/" target="_blank">Emojipedia</a></small>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    ${accion == 'editar' ? '💾 Guardar Cambios' : '➕ Crear Especialidad'}
                </button>
                <a href="<%= request.getContextPath() %>/especialidades?accion=listarAdmin" class="btn btn-secondary">
                    Cancelar
                </a>
            </div>
        </form>
    </div>

    <footer>
        <div class="footer-bottom">
            <p>&copy; 2025 Bienestar Politécnico - Panel Administrador</p>
        </div>
    </footer>
    
    <!-- JavaScript opcional para mejoras UX -->
    <script src="<%= request.getContextPath() %>/js/admin-especialidades.js"></script>
</body>
</html>