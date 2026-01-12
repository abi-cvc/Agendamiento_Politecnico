<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.entity.Doctor" %>
<%@ page import="model.entity.Especialidad" %>
<%
    List<Doctor> doctores = (List<Doctor>) request.getAttribute("doctores");
    
    if (doctores == null) {
        doctores = new java.util.ArrayList<>();
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/framework.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
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
            <li><a href="${pageContext.request.contextPath}/DoctorAdminController?accion=gestionarDoctores" class="font-bold">Gestionar Doctores</a></li>
            <li><a href="${pageContext.request.contextPath}/EstudianteAdminController?accion=gestionarEstudiantes">Gestionar Estudiantes</a></li>
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

<!-- CONTENIDO PRINCIPAL -->
<main>
    <div class="admin-container">
        
        <!-- ENCABEZADO -->
        <div class="admin-header">
            <h1>👨‍⚕️ Gestionar Doctores</h1>
            <a href="${pageContext.request.contextPath}/DoctorAdminController?accion=gestionarDoctores&modal=nuevo" 
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
                                <td>
                                     <span class="estado-badge <%= doc.isActivo() ? "estado-activo" : "estado-inactivo" %>">
                                         <%= doc.isActivo() ? "✓ Activo" : "✗ Inactivo" %>
                                     </span>
                                 </td>
                                <td>
                                    <div class="btn-actions">
                                        <a href="${pageContext.request.contextPath}/DoctorAdminController?accion=gestionarDoctores&editar=<%= doc.getIdDoctor() %>" 
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
            <a href="${pageContext.request.contextPath}/DoctorAdminController?accion=gestionarDoctores" 
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
                    <% for (Especialidad esp : (List<Especialidad>) request.getAttribute("especialidades")) { %>
                        <option value="<%= esp.getIdEspecialidad() %>"><%= esp.getTitulo() %></option>
                    <% } %>
                </select>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">💾 Guardar</button>
                <a href="${pageContext.request.contextPath}/DoctorAdminController?accion=gestionarDoctores" 
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
            <a href="${pageContext.request.contextPath}/DoctorAdminController?accion=gestionarDoctores" 
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
                <label class="form-label">Contraseña (dejar vacío para mantener)</label>
                <input type="password" name="password" class="form-input" maxlength="255">
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">💾 Actualizar</button>
                <a href="${pageContext.request.contextPath}/DoctorAdminController?accion=gestionarDoctores" 
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
                <li><a href="${pageContext.request.contextPath}/DoctorAdminController?accion=gestionarDoctores">Gestionar Doctores</a></li>
                <li><a href="${pageContext.request.contextPath}/EstudianteAdminController?accion=gestionarEstudiantes">Gestionar Estudiantes</a></li>
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