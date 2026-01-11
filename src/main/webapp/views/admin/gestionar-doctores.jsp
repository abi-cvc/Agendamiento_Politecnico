<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.entity.Doctor" %>
<%@ page import="model.entity.Especialidad" %>
<%
    List<Doctor> doctores = (List<Doctor>) request.getAttribute("doctores");
    List<Especialidad> especialidades = (List<Especialidad>) request.getAttribute("especialidades");
    
    if (doctores == null) {
        doctores = new java.util.ArrayList<>();
    }
    if (especialidades == null) {
        especialidades = new java.util.ArrayList<>();
    }
    
    // Determinar si mostrar el modal de creación
    String mostrarModal = request.getParameter("modal");
    boolean abrirModalCrear = "nuevo".equals(mostrarModal);
    
    // Determinar si mostrar el modal de edición
    String idEditar = request.getParameter("editar");
    boolean abrirModalEditar = idEditar != null && !idEditar.trim().isEmpty();
    Doctor doctorEditar = null;
    if (abrirModalEditar) {
        try {
            int id = Integer.parseInt(idEditar);
            for (Doctor d : doctores) {
                if (d.getIdDoctor() == id) {
                    doctorEditar = d;
                    break;
                }
            }
        } catch (NumberFormatException e) {
            abrirModalEditar = false;
        }
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Doctores - Admin</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo_epn.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/framework.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@400..700&display=swap" rel="stylesheet">
</head>
<body>

<!-- HEADER -->
<header>
    <div class="logo">
        <img src="${pageContext.request.contextPath}/images/logo.svg" alt="Logo">
    </div>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/inicio-admin.jsp">Inicio</a></li>
            <li><a href="${pageContext.request.contextPath}/DoctorAdminController?accion=listar" class="font-bold">Gestionar Doctores</a></li>
            <li><a href="${pageContext.request.contextPath}/EstudianteAdminController?accion=listar">Gestionar Estudiantes</a></li>
            <li><a href="${pageContext.request.contextPath}/especialidades?accion=listarAdmin">Gestionar Especialidades</a></li>
            <li><a href="${pageContext.request.contextPath}/EvaluacionController?accion=listar">Gestionar Evaluaciones</a></li>
            <li class="login mt-2 mb-2">
                <a href="${pageContext.request.contextPath}/index.html" class="font-bold">Cerrar Sesión</a>
            </li>
        </ul>
    </nav>
</header>

<!-- CONTENIDO PRINCIPAL -->
<main>
    <div class="admin-container">
        
        <!-- ENCABEZADO -->
        <div class="admin-header">
            <h1>👨‍⚕️ Gestionar Doctores</h1>
            <a href="${pageContext.request.contextPath}/DoctorAdminController?accion=listar&modal=nuevo" 
               class="btn btn-primary">
                ➕ Nuevo Doctor
            </a>
        </div>

        <!-- MENSAJES -->
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

        <!-- SECCIÓN DE BÚSQUEDA -->
        <div class="search-section">
            <h3>🔍 Buscar Doctor</h3>
            <form method="get" action="${pageContext.request.contextPath}/DoctorAdminController">
                <input type="hidden" name="accion" value="buscar">
                <div class="search-wrapper">
                    <div class="form-group">
                        <label class="form-label">Cédula del Doctor</label>
                        <input type="text" 
                               name="cedula" 
                               class="form-input" 
                               placeholder="Ingrese cédula para buscar..."
                               value="<%= request.getParameter("cedula") != null ? request.getParameter("cedula") : "" %>">
                    </div>
                    <button type="submit" class="btn btn-primary">🔍 Buscar</button>
                </div>
            </form>
        </div>

        <!-- TABLA DE DOCTORES -->
        <div class="card">
            <h3 class="mb-3">📋 Lista de Doctores (<%= doctores.size() %>)</h3>
            
            <% if (doctores.isEmpty()) { %>
                <div class="text-center p-4">
                    <p class="text-muted text-lg">
                        No se encontraron doctores
                        <% if (request.getParameter("cedula") != null) { %>
                            con la cédula "<%= request.getParameter("cedula") %>"
                        <% } %>
                    </p>
                </div>
            <% } else { %>
                <table class="table-admin">
                    <thead>
                        <tr>
                            <th>Cédula</th>
                            <th>Nombre</th>
                            <th>Apellido</th>
                            <th>Email</th>
                            <th>Teléfono</th>
                            <th>Especialidad</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Doctor doc : doctores) { %>
                            <tr>
                                <td><%= doc.getCedula() %></td>
                                <td><%= doc.getNombre() %></td>
                                <td><%= doc.getApellido() %></td>
                                <td><%= doc.getEmail() != null ? doc.getEmail() : "-" %></td>
                                <td><%= doc.getTelefono() != null ? doc.getTelefono() : "-" %></td>
                                <td>
                                    <% if (doc.getEspecialidad() != null) { %>
                                        <span class="badge badge-primary">
                                            <%= doc.getEspecialidad().getTitulo() %>
                                        </span>
                                    <% } else { %>
                                        <span class="text-muted">-</span>
                                    <% } %>
                                </td>
                                <td>
                                    <span class="estado-badge <%= doc.isActivo() ? "estado-activo" : "estado-inactivo" %>">
                                        <%= doc.isActivo() ? "✓ Activo" : "✗ Inactivo" %>
                                    </span>
                                </td>
                                <td>
                                    <div class="btn-actions">
                                        <a href="${pageContext.request.contextPath}/DoctorAdminController?accion=listar&editar=<%= doc.getIdDoctor() %>" 
                                           class="btn btn-sm btn-warning">
                                            ✏️ Editar
                                        </a>
                                        
                                        <form method="post" 
                                              action="${pageContext.request.contextPath}/DoctorAdminController" 
                                              style="display:inline-block;">
                                            <input type="hidden" name="accion" value="cambiarEstado">
                                            <input type="hidden" name="id" value="<%= doc.getIdDoctor() %>">
                                            <button type="submit" 
                                                    class="btn btn-sm <%= doc.isActivo() ? "btn-danger" : "btn-primary" %>"
                                                    onclick="return confirm('¿Está seguro de <%= doc.isActivo() ? "desactivar" : "activar" %> a este doctor?')">
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
<% if (abrirModalCrear) { %>
<div class="modal-overlay active">
    <div class="modal-content">
        <div class="modal-header">
            <h2>➕ Nuevo Doctor</h2>
            <a href="${pageContext.request.contextPath}/DoctorAdminController?accion=listar" 
               class="btn-close">&times;</a>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/DoctorAdminController">
            <input type="hidden" name="accion" value="crear">
            
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
            
            <div class="form-group">
                <label class="form-label">Teléfono</label>
                <input type="text" name="telefono" class="form-input" maxlength="15">
            </div>
            
            <div class="form-group">
                <label class="form-label">Foto (URL)</label>
                <input type="text" name="foto" class="form-input" maxlength="255" placeholder="https://ejemplo.com/foto.jpg">
            </div>
            
            <div class="form-group">
                <label class="form-label">Descripción</label>
                <textarea name="descripcion" class="form-textarea" rows="3"></textarea>
            </div>
            
            <div class="form-group">
                <label class="form-label">Especialidad *</label>
                <select name="idEspecialidad" class="form-select" required>
                    <option value="">Seleccionar Especialidad</option>
                    <% for (Especialidad esp : especialidades) { %>
                        <option value="<%= esp.getIdEspecialidad() %>">
                            <%= esp.getTitulo() %>
                        </option>
                    <% } %>
                </select>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">💾 Guardar</button>
                <a href="${pageContext.request.contextPath}/DoctorAdminController?accion=listar" 
                   class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>
<% } %>

<!-- MODAL EDITAR DOCTOR -->
<% if (abrirModalEditar && doctorEditar != null) { %>
<div class="modal-overlay active">
    <div class="modal-content">
        <div class="modal-header">
            <h2>✏️ Editar Doctor</h2>
            <a href="${pageContext.request.contextPath}/DoctorAdminController?accion=listar" 
               class="btn-close">&times;</a>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/DoctorAdminController">
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
                <input type="email" class="form-input" value="<%= doctorEditar.getEmail() %>" readonly>
            </div>
            
            <div class="form-group">
                <label class="form-label">Teléfono</label>
                <input type="text" name="telefono" class="form-input" 
                       value="<%= doctorEditar.getTelefono() != null ? doctorEditar.getTelefono() : "" %>" 
                       maxlength="15">
            </div>
            
            <div class="form-group">
                <label class="form-label">Foto (URL)</label>
                <input type="text" name="foto" class="form-input" 
                       value="<%= doctorEditar.getFoto() != null ? doctorEditar.getFoto() : "" %>" 
                       maxlength="255">
            </div>
            
            <div class="form-group">
                <label class="form-label">Descripción</label>
                <textarea name="descripcion" class="form-textarea" rows="3"><%= doctorEditar.getDescripcion() != null ? doctorEditar.getDescripcion() : "" %></textarea>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">💾 Actualizar</button>
                <a href="${pageContext.request.contextPath}/DoctorAdminController?accion=listar" 
                   class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>
<% } %>

<!-- FOOTER -->
<footer>
    <div class="footer-main">
        <div class="footer-section">
            <div class="footer-logo">Bienestar Politécnico</div>
            <p class="footer-tagline">Sistema de Gestión Administrativa</p>
        </div>
        <div class="footer-section">
            <h3>Panel Admin</h3>
            <ul class="footer-links">
                <li><a href="${pageContext.request.contextPath}/DoctorAdminController?accion=listar">Gestionar Doctores</a></li>
                <li><a href="${pageContext.request.contextPath}/EstudianteAdminController?accion=listar">Gestionar Estudiantes</a></li>
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

</body>
</html>