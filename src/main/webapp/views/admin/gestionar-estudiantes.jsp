<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.entity.Estudiante" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Estudiantes - Admin</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo_epn.png">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/framework.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@400..700&display=swap" rel="stylesheet">
</head>
<body>

<header>
    <div class="logo">
        <img src="<%= request.getContextPath() %>/images/logo.svg" alt="Logo">
    </div>
    <nav>
        <ul>
            <li><a href="<%= request.getContextPath() %>/inicio-admin.jsp">Inicio</a></li>
            <li><a href="<%= request.getContextPath() %>/GestionarDoctores?accion=gestionarDoctores">Gestionar Doctores</a></li>
            <li><a href="<%= request.getContextPath() %>/GestionarEstudiantes?accion=gestionarEstudiantes" class="font-bold">Gestionar Estudiantes</a></li>
            <li><a href="<%= request.getContextPath() %>/especialidades?accion=listarAdmin">Gestionar Especialidades</a></li>
            <li><a href="<%= request.getContextPath() %>/evaluaciones?accion=listarAdmin">Consultar Evaluaciones</a></li>
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

<main>
    <div class="admin-container">
        
        <div class="admin-header">
            <h1>Gestionar Estudiantes</h1>
            <a href="<%= request.getContextPath() %>/GestionarEstudiantes?accion=NuevoEstudiante" class="btn btn-primary">➕ Nuevo Estudiante</a>
        </div>

        <% if (session.getAttribute("mensaje") != null) { %>
            <div class="alert alert-success show">
                <%= session.getAttribute("mensaje") %>
                <% session.removeAttribute("mensaje"); %>
            </div>
        <% } %>
        
        <% if (session.getAttribute("error") != null) { %>
            <div class="alert alert-error show">
                <%= session.getAttribute("error") %>
                <% session.removeAttribute("error"); %>
            </div>
        <% } %>

        <div class="filtros-evaluaciones">
            <h3>🔍 Buscar Estudiante</h3>
            <form method="get" action="<%= request.getContextPath() %>/GestionarEstudiantes">
                <input type="hidden" name="accion" value="buscar">
                <div class="search-wrapper">
                    <div class="form-group">
                        <label class="form-label">Cédula del Estudiante (ID Paciente)</label>
                        <input type="text" name="cedula" class="form-input" placeholder="Ingrese cédula para buscar..." value="<%= request.getParameter("cedula") != null ? request.getParameter("cedula") : "" %>">
                    </div>
                    <button type="submit" class="btn btn-primary">🔍 Buscar</button>
                </div>
            </form>
        </div>

        <div class="card">
            <%
                List<Estudiante> estudiantes = (List<Estudiante>) request.getAttribute("estudiantes");
            %>
            <h3 class="mb-3">📋 Lista de Estudiantes (<%= estudiantes != null ? estudiantes.size() : 0 %>)</h3>
            
            <% if (estudiantes == null || estudiantes.isEmpty()) { %>
                <div class="text-center p-4">
                    <p class="text-muted text-lg">
                        No se encontraron estudiantes
                        <% if (request.getParameter("cedula") != null) { %>
                            con la cédula "<%= request.getParameter("cedula") %>"
                        <% } %>
                    </p>
                </div>
            <% } else { %>
                <table class="table-admin">
                    <thead>
                        <tr>
                            <th>Cédula (ID Paciente)</th>
                            <th>Nombre</th>
                            <th>Apellido</th>
                            <th>Email</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Estudiante est : estudiantes) { %>
                            <tr>
                                <td><%= est.getIdPaciente() %></td>
                                <td><%= est.getNombreEstudiante() %></td>
                                <td><%= est.getApellidoEstudiante() %></td>
                                <td><%= est.getCorreoEstudiante() != null ? est.getCorreoEstudiante() : "-" %></td>
                                <td>
                                    <span class="estado-badge <%= est.isActivo() ? "estado-activo" : "estado-inactivo" %>">
                                        <%= est.isActivo() ? "✓ Activo" : "✗ Inactivo" %>
                                    </span>
                                </td>
                                <td>
                                    <div class="btn-actions">
                                        <a href="<%= request.getContextPath() %>/GestionarEstudiantes?accion=editarEstudiante&id=<%= est.getIdEstudiante() %>" class="btn btn-sm btn-warning">✏️ Editar</a>

                                        <form method="post" action="<%= request.getContextPath() %>/GestionarEstudiantes" style="display:inline-block;">
                                            <input type="hidden" name="accion" value="confirmarDesactivacion">
                                            <input type="hidden" name="id" value="<%= est.getIdEstudiante() %>">
                                            <button type="submit" class="btn btn-sm <%= est.isActivo() ? "btn-danger" : "btn-primary" %>">
                                                <%= est.isActivo() ? "⏸ Desactivar" : "▶ Activar" %>
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %>
        </div>

    </div>
</main>

<!-- MODAL NUEVO ESTUDIANTE -->
<%
    Boolean abrirModalCrear = (Boolean) request.getAttribute("abrirModalCrear");
    if (abrirModalCrear != null && abrirModalCrear) {
%>
<div class="modal-overlay active">
    <div class="modal-content">
        <div class="modal-header">
            <h2>➕ Nuevo Estudiante</h2>
            <a href="<%= request.getContextPath() %>/GestionarEstudiantes?accion=gestionarEstudiantes" class="btn-close">&times;</a>
        </div>
        <form method="post" action="<%= request.getContextPath() %>/GestionarEstudiantes">
            <input type="hidden" name="accion" value="solicitarNuevoEstudiante">
            
            <div class="form-group">
                <label class="form-label">Cédula (ID Paciente) *</label>
                <input type="text" name="cedula" class="form-input" required maxlength="20" placeholder="Ej: 1234567890">
            </div>
            
            <div class="form-group">
                <label class="form-label">Nombre *</label>
                <input type="text" name="nombre" class="form-input" required maxlength="100">
            </div>
            
            <div class="form-group">
                <label class="form-label">Apellido *</label>
                <input type="text" name="apellido" class="form-input" required maxlength="100">
            </div>
            
            <div class="form-group">
                <label class="form-label">Email *</label>
                <input type="email" name="email" class="form-input" required maxlength="100" placeholder="estudiante@epn.edu.ec">
            </div>
            
            <div class="form-group">
                <label class="form-label">Contraseña *</label>
                <input type="password" name="password" class="form-input" required maxlength="255" placeholder="Contraseña para el estudiante">
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">💾 Guardar</button>
                <a href="<%= request.getContextPath() %>/GestionarEstudiantes?accion=gestionarEstudiantes" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>
<% } %>

<!-- MODAL EDITAR ESTUDIANTE -->
<%
    Boolean abrirModalEditar = (Boolean) request.getAttribute("abrirModalEditar");
    Estudiante estudianteEditar = (Estudiante) request.getAttribute("estudianteEditar");
    if (abrirModalEditar != null && abrirModalEditar && estudianteEditar != null) {
%>
<div class="modal-overlay active">
    <div class="modal-content">
        <div class="modal-header">
            <h2>✏️ Editar Estudiante</h2>
            <a href="<%= request.getContextPath() %>/GestionarEstudiantes?accion=gestionarEstudiantes" class="btn-close">&times;</a>
        </div>
        <form method="post" action="<%= request.getContextPath() %>/GestionarEstudiantes">
            <input type="hidden" name="accion" value="actualizar">
            <input type="hidden" name="id" value="<%= estudianteEditar.getIdEstudiante() %>">
            
            <div class="form-group">
                <label class="form-label">Cédula (ID Paciente)</label>
                <input type="text" class="form-input" value="<%= estudianteEditar.getIdPaciente() %>" readonly>
            </div>
            
            <div class="form-group">
                <label class="form-label">Nombre *</label>
                <input type="text" name="nombre" class="form-input" value="<%= estudianteEditar.getNombreEstudiante() %>" required maxlength="100">
            </div>
            
            <div class="form-group">
                <label class="form-label">Apellido *</label>
                <input type="text" name="apellido" class="form-input" value="<%= estudianteEditar.getApellidoEstudiante() %>" required maxlength="100">
            </div>
            
            <div class="form-group">
                <label class="form-label">Email *</label>
                <input type="email" name="email" class="form-input" value="<%= estudianteEditar.getCorreoEstudiante() %>" required maxlength="100">
            </div>
            
            <div class="form-group">
                <label class="form-label">Contraseña (dejar vacío para mantener)</label>
                <input type="password" name="password" class="form-input" maxlength="255">
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">💾 Actualizar</button>
                <a href="<%= request.getContextPath() %>/GestionarEstudiantes?accion=gestionarEstudiantes" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>
<% } %>

<footer>
    <div class="footer-main">
        <div class="footer-section">
            <div class="footer-logo">Bienestar Politécnico</div>
            <p class="footer-tagline">Sistema de Gestión Administrativa</p>
        </div>
        <div class="footer-section">
            <h3>Panel Admin</h3>
            <ul class="footer-links">
                <li><a href="<%= request.getContextPath() %>/GestionarDoctores?accion=gestionarDoctores">Gestionar Doctores</a></li>
                <li><a href="<%= request.getContextPath() %>/GestionarEstudiantes?accion=gestionarEstudiantes">Gestionar Estudiantes</a></li>
                <li><a href="<%= request.getContextPath() %>/especialidades?accion=listarAdmin">Gestionar Especialidades</a></li>
            	<li><a href="<%= request.getContextPath() %>/evaluaciones?accion=listarAdmin">Consultar Evaluaciones</a></li>
            </ul>
        </div>
        <div class="footer-section">
            <h3>Contacto</h3>
            <p>bienestar@epn.edu.ec</p>
        </div>
    </div>
    <div class="footer-bottom">
        <p>&copy; 2025 Bienestar Politécnico - Panel de Administración</p>
    </div>
</footer>

<script src="<%= request.getContextPath() %>/js/gestionar-estudiantes.js"></script>
</body>
</html>