<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Administrador - Bienestar Politécnico</title>
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
            <li><a href="${pageContext.request.contextPath}/inicio-admin.jsp" class="font-bold">Inicio</a></li>
            <li><a href="${pageContext.request.contextPath}/GestionarDoctores" class="font-bold">Gestionar Doctores</a></li>
            <li><a href="${pageContext.request.contextPath}/EstudianteAdminController" class="font-bold">Gestionar Estudiantes</a></li>
            <li><a href="${pageContext.request.contextPath}/especialidades?accion=listarAdmin" class="font-bold">Gestionar Especialidades</a></li>
            <li><a href="${pageContext.request.contextPath}/EvaluacionController?accion=listar" class="font-bold">Consultar Evaluaciones</a></li>
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

<!-- MAIN CONTENT -->
<main>
    <h1>Panel de Administración</h1>
    <p>Gestiona el sistema de <span>Bienestar Politécnico</span></p>

    <!-- MENSAJES -->
    <div id="mensaje-container">
        <div class="mensaje success">${sessionScope.mensaje}</div>
        <div class="mensaje error">${sessionScope.error}</div>
    </div>

    <!-- SECCIÓN DE INFORMACIÓN -->
    <section class="informacion-inicio">
        <div class="card hover-lift">
            <h2>👨‍⚕️ Gestionar Doctores</h2>
            <p>Administra la información de los profesionales de salud, sus especialidades y horarios de atención.</p>
            <div class="mt-4">
                <a href="${pageContext.request.contextPath}/GestionarDoctores" class="btn btn-primary">
                    Ir a Doctores
                </a>
            </div>
        </div>

        <div class="card hover-lift">
            <h2>👥 Gestionar Estudiantes</h2>
            <p>Administra los estudiantes registrados en el sistema, actualiza su información y estado.</p>
            <div class="mt-4">
                <a href="${pageContext.request.contextPath}/EstudianteAdminController" class="btn btn-primary">
                    Ir a Estudiantes
                </a>
            </div>
        </div>

        <div class="card hover-lift">
            <h2>🏥 Gestionar Especialidades</h2>
            <p>Crea, edita o elimina especialidades médicas disponibles en Bienestar Politécnico.</p>
            <div class="mt-4">
                <a href="${pageContext.request.contextPath}/especialidades?accion=listarAdmin" class="btn btn-primary">
                    Ir a Especialidades
                </a>
            </div>
        </div>

        <div class="card hover-lift">
            <h2>📊 Consultar Evaluaciones</h2>
            <p>Administra las evaluaciones y encuestas de satisfacción de los servicios médicos.</p>
            <div class="mt-4">
                <a href="${pageContext.request.contextPath}/evaluaciones?accion=listarAdmin" class="btn btn-primary">
                    Ir a Evaluaciones
                </a>
            </div>
        </div>
    </section>
</main>

<!-- FOOTER -->
<footer>
    <div class="footer-main">
        <!-- Columna 1: Marca -->
        <div class="footer-section">
            <div class="footer-logo">Bienestar Politécnico</div>
            <p class="footer-tagline">Cuidando de ti, cuidamos el futuro de la Politécnica.</p>
        </div>

        <!-- Columna 2: Enlaces -->
        <div class="footer-section">
            <h3>Panel Admin</h3>
            <ul class="footer-links">
                <li><a href="${pageContext.request.contextPath}/inicio-admin.jsp">Inicio</a></li>
                <li><a href="${pageContext.request.contextPath}/GestionarDoctores">Gestionar Doctores</a></li>
                <li><a href="${pageContext.request.contextPath}/EstudianteAdminController">Gestionar Estudiantes</a></li>
                <li><a href="${pageContext.request.contextPath}/especialidades?accion=listarAdmin">Gestionar Especialidades</a></li>
            </ul>
        </div>

        <!-- Columna 3: Contacto -->
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