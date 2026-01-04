<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bienestar Politécnico</title>

    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo_epn.png">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/framework.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles.css">

    <!-- Google Font -->
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
            <li><a href="<%= request.getContextPath() %>/inicio.jsp" class="font-bold">Inicio</a></li>
            <li><a href="<%= request.getContextPath() %>/especialidades.jsp" class="font-bold">Especialidades</a></li>
            <li><a href="<%= request.getContextPath() %>/consultar-citas.jsp" class="font-bold">Mis Citas</a></li>
            <li><a href="<%= request.getContextPath() %>/reseñas.jsp" class="font-bold">Reseñas</a></li>
        </ul>
    </nav>
</header>

<main class="login-section">
    <div class="login-card">
        <div class="login-header">
            <h1>Iniciar Sesión</h1>
            <p>Ingresa con tu correo institucional</p>
        </div>

        <!-- FORMULARIO -->
        <form id="loginForm" class="login-form"
              action="<%= request.getContextPath() %>/login"
              method="post">

            <div class="form-group">
                <label for="rol">Selecciona tu rol</label>
                <select id="rol" name="rol" required>
                    <option value="">Selecciona una opción</option>
                    <option value="estudiante">Estudiante</option>
                    <option value="doctor">Doctor</option>
                    <option value="admin">Administrador</option>
                </select>
            </div>

            <div class="form-group">
                <label for="email">Correo institucional</label>
                <input type="email" id="email" name="email"
                       placeholder="usuario@epn.edu.ec" required>
            </div>

            <div class="form-group">
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password" required>
            </div>

            <!-- Mensaje de error -->
            <div class="error-message">
                <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
            </div>

            <button type="submit" class="btn-login">
                Iniciar Sesión
            </button>
        </form>

        <div class="login-footer">
            <p>¿Problemas para acceder?</p>
            <a href="mailto:bienestar@epn.edu.ec">Contacta a Bienestar Politécnico</a>
        </div>
    </div>
</main>

<footer>
    <div class="footer-bottom">
        <p>&copy; 2025 Bienestar Politécnico - Escuela Politécnica Nacional.</p>
    </div>
</footer>

<script src="<%= request.getContextPath() %>/js/auth.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const navLinks = document.querySelectorAll('nav ul li a');
        navLinks.forEach(link => {
            if (!link.href.includes('inicio.html') && !link.href.includes('index.jsp')) {
                link.style.pointerEvents = 'none';
                link.style.opacity = '0.5';
            }
        });
    });
</script>

</body>
</html>
