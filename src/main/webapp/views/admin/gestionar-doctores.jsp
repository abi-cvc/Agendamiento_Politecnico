<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.entity.Doctor" %>
<%@ page import="model.entity.Especialidad" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Doctores - Admin</title>
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
            <li><a href="<%= request.getContextPath() %>/GestionarDoctores?accion=gestionarDoctores" class="font-bold">Gestionar Doctores</a></li>
            <li><a href="<%= request.getContextPath() %>/GestionarEstudiantes?accion=gestionarEstudiantes">Gestionar Estudiantes</a></li>
            <li><a href="<%= request.getContextPath() %>/especialidades?accion=listarAdmin">Gestionar Especialidades</a></li>
            <li><a href="<%= request.getContextPath() %>/evaluaciones?accion=listarAdmin">Consultar Evaluaciones</a></li>
        </ul>
    </nav>
</header>

<main>
    <div class="admin-container">
        <div class="admin-header">
            <h1>Gestionar Doctores</h1>
            <a href="<%= request.getContextPath() %>/GestionarDoctores?accion=NuevoDoctor" class="btn btn-primary">➕ Nuevo Doctor</a>
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
            <h3>🔍 Buscar Doctor</h3>
            <form method="get" action="<%= request.getContextPath() %>/GestionarDoctores">
                <input type="hidden" name="accion" value="buscar">
                <div class="search-wrapper">
                    <div class="form-group">
                        <label class="form-label">Cédula del Doctor</label>
                        <input type="text" name="cedula" class="form-input" placeholder="Ingrese cédula para buscar..." value="<%= request.getParameter("cedula") != null ? request.getParameter("cedula") : "" %>">
                    </div>
                    <button type="submit" class="btn btn-primary">🔍 Buscar</button>
                </div>
            </form>
        </div>

        <div class="card">
            <%
                List<Doctor> doctores = (List<Doctor>) request.getAttribute("doctores");
            %>
            <h3 class="mb-3">📋 Lista de Doctores (<%= doctores != null ? doctores.size() : 0 %>)</h3>

            <% if (doctores == null || doctores.isEmpty()) { %>
                <div class="empty-state">
                    <p>No se encontraron doctores</p>
                    <a href="<%= request.getContextPath() %>/GestionarDoctores?accion=NuevoDoctor" class="btn btn-primary mt-3">➕ Nuevo Doctor</a>
                </div>
            <% } else { %>
                <table class="table-admin">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Cédula</th>
                            <th>Nombre Completo</th>
                            <th>Email</th>
                            <th>Teléfono</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Doctor doc : doctores) { %>
                            <tr>
                                <td><%= doc.getIdDoctor() %></td>
                                <td><%= doc.getCedula() %></td>
                                <td><%= doc.getNombre() + " " + doc.getApellido() %></td>
                                <td><%= doc.getEmail() != null ? doc.getEmail() : "-" %></td>
                                <td><%= doc.getTelefono() != null ? doc.getTelefono() : "-" %></td>
                                <td><span class="estado-badge <%= doc.isActivo() ? "estado-activo" : "estado-inactivo" %>"><%= doc.isActivo() ? "✓ Activo" : "✗ Inactivo" %></span></td>
                                <td>
                                    <div class="btn-actions">
                                        <a href="<%= request.getContextPath() %>/GestionarDoctores?accion=solicitarEdicionDoctor&id=<%= doc.getIdDoctor() %>" class="btn btn-sm btn-warning">✏️ Editar</a>

                                        <form method="post" action="<%= request.getContextPath() %>/GestionarDoctores" style="display:inline-block; margin:0;">
                                            <input type="hidden" name="accion" value="confirmarDesactivacion">
                                            <input type="hidden" name="id" value="<%= doc.getIdDoctor() %>">
                                            <button type="submit" class="btn btn-sm <%= doc.isActivo() ? "btn-danger" : "btn-primary" %>">
                                                <%= doc.isActivo() ? "⏸ Desactivar" : "▶ Activar" %>
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

<!-- MODAL NUEVO DOCTOR -->
<%
    Boolean abrirModalCrear = (Boolean) request.getAttribute("abrirModalCrear");
    List<Especialidad> especialidades = (List<Especialidad>) request.getAttribute("especialidades");
    if (abrirModalCrear != null && abrirModalCrear) {
%>
<div class="modal-overlay active">
    <div class="modal-content">
        <div class="modal-header">
            <h2>➕ Nuevo Doctor</h2>
            <a href="<%= request.getContextPath() %>/GestionarDoctores?accion=gestionarDoctores" class="btn-close">&times;</a>
        </div>
        <form method="post" action="<%= request.getContextPath() %>/GestionarDoctores">
            <input type="hidden" name="accion" value="solicitarNuevoDoctor">

            <div class="form-group">
                <label class="form-label">Cédula *</label>
                <input type="text" name="cedula" class="form-input" required maxlength="10">
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
                <input type="email" name="email" class="form-input" required maxlength="100">
            </div>

            <div class="grid grid-2 gap-lg">
                <div class="form-group">
                    <label class="form-label">Contraseña *</label>
                    <input type="password" name="password" class="form-input" required maxlength="255" placeholder="Contraseña para el doctor">
                </div>

                <div class="form-group">
                    <label class="form-label">Teléfono</label>
                    <input type="text" name="telefono" class="form-input" maxlength="15">
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">Especialidad (opcional)</label>
                <select name="idEspecialidad" class="form-select">
                    <option value="">-- Sin especialidad --</option>
                    <% if (especialidades != null) {
                        for (Especialidad esp : especialidades) { %>
                            <option value="<%= esp.getIdEspecialidad() %>"><%= esp.getTitulo() %></option>
                    <%  }
                    } %>
                </select>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">💾 Guardar</button>
                <a href="<%= request.getContextPath() %>/GestionarDoctores?accion=gestionarDoctores" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>
<% } %>

<!-- MODAL EDITAR DOCTOR -->
<%
    Boolean abrirModalEditar = (Boolean) request.getAttribute("abrirModalEditar");
    Doctor doctorEditar = (Doctor) request.getAttribute("doctorEditar");
    if (abrirModalEditar != null && abrirModalEditar && doctorEditar != null) {
%>
<div class="modal-overlay active">
    <div class="modal-content">
        <div class="modal-header">
            <h2>✏️ Editar Doctor</h2>
            <a href="<%= request.getContextPath() %>/GestionarDoctores?accion=gestionarDoctores" class="btn-close">&times;</a>
        </div>
        <form method="post" action="<%= request.getContextPath() %>/GestionarDoctores">
            <input type="hidden" name="accion" value="actualizar">
            <input type="hidden" name="id" value="<%= doctorEditar.getIdDoctor() %>">
            
            <div class="form-group">
                <label class="form-label">Cédula</label>
                <input type="text" class="form-input" value="<%= doctorEditar.getCedula() %>" readonly>
            </div>
            
            <div class="form-group">
                <label class="form-label">Nombre</label>
                <input type="text" class="form-input" value="<%= doctorEditar.getNombre() %>" readonly>
            </div>
            
            <div class="form-group">
                <label class="form-label">Apellido</label>
                <input type="text" class="form-input" value="<%= doctorEditar.getApellido() %>" readonly>
            </div>
            
            <div class="form-group">
                <label class="form-label">Email</label>
                <input type="email" class="form-input" name="email" value="<%= doctorEditar.getEmail() != null ? doctorEditar.getEmail() : "" %>">
            </div>
            
            <div class="form-group">
                <label class="form-label">Teléfono</label>
                <input type="text" name="telefono" class="form-input" value="<%= doctorEditar.getTelefono() != null ? doctorEditar.getTelefono() : "" %>" maxlength="15">
            </div>
            
            <div class="form-group">
                <label class="form-label">Especialidad</label>
                <select name="idEspecialidad" class="form-select">
                    <option value="">-- Sin especialidad --</option>
                    <% if (especialidades != null) {
                        for (Especialidad esp : especialidades) { 
                            boolean seleccionado = (doctorEditar.getEspecialidad() != null && 
                                                   doctorEditar.getEspecialidad().getIdEspecialidad() == esp.getIdEspecialidad());
                    %>
                            <option value="<%= esp.getIdEspecialidad() %>" <%= seleccionado ? "selected" : "" %>>
                                <%= esp.getTitulo() %>
                            </option>
                    <%  }
                    } %>
                </select>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">💾 Actualizar</button>
                <a href="<%= request.getContextPath() %>/GestionarDoctores?accion=gestionarDoctores" class="btn btn-secondary">Cancelar</a>
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

<script src="<%= request.getContextPath() %>/js/gestionar-doctores.js"></script>
</body>
</html>