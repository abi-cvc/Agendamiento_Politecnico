<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bienestar Politécnico</title>

    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo_epn.png">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/framework.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">

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
            <li><a href="<%= request.getContextPath() %>/index.jsp" class="font-bold">Inicio</a></li>
            <li><a href="#" class="font-bold" style="opacity: 0.5; pointer-events: none;">Especialidades</a></li>
            <li><a href="#" class="font-bold" style="opacity: 0.5; pointer-events: none;">Mis Citas</a></li>
            <li><a href="#" class="font-bold" style="opacity: 0.5; pointer-events: none;">Mis Reseñas</a></li>
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
        <form id="loginForm" class="login-form">

            <div class="form-group">
                <label for="rol">Selecciona tu rol</label>
                <select id="rol" name="rol" class="form-select" required>
                    <option value="">Selecciona una opción</option>
                    <option value="estudiante">Estudiante</option>
                    <option value="doctor">Doctor</option>
                    <option value="admin">Administrador</option>
                </select>
            </div>

            <div class="form-group">
                <label for="email">Correo institucional</label>
                <div class="input-wrapper">
                    <svg class="input-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M20 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"/>
                    </svg>
                    <input type="email" 
                           id="email" 
                           name="email"
                           placeholder="admin@epn.edu.ec" 
                           required>
                </div>
            </div>

            <div class="form-group">
                <label for="password">Contraseña</label>
                <div class="input-wrapper">
                    <svg class="input-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zM9 6c0-1.66 1.34-3 3-3s3 1.34 3 3v2H9V6zm9 14H6V10h12v10zm-6-3c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2 .9 2 2 2z"/>
                    </svg>
                    <input type="password" 
                           id="password" 
                           name="password"
                           placeholder="••••••••" 
                           required>
                    <button type="button" class="toggle-password" onclick="togglePassword()">
                        <svg id="eyeIcon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="22" height="22">
                            <path d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"/>
                        </svg>
                    </button>
                </div>
            </div>

            <!-- Mensaje de error -->
            <div id="errorMessage" class="alert alert-error"></div>

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

<!-- ✅ CARGAR AUTH TEMPORAL -->
<script src="<%= request.getContextPath() %>/js/auth-temporal.js"></script>

</body>
</html>