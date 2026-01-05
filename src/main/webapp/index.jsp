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
            <li><a href="<%= request.getContextPath() %>/ConsultarCitasAgendadasController" class="font-bold">Mis Citas</a></li>
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
                <label for="identificacion">ID / Cédula</label>
                <input type="text" id="identificacion" name="identificacion"
                       placeholder="Ej: 1725896347" required>
            </div>

            <div class="form-group">
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password"
                       placeholder="Ingresa tu contraseña" required>
            </div>

            <!-- Mensaje de error -->
            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message" style="background: #fee2e2; color: #dc2626; padding: 12px; border-radius: 8px; margin-bottom: 15px;">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>

            <button type="submit" class="btn-login">
                Iniciar Sesión
            </button>
        </form>

        <div class="login-footer">
            <p>¿Problemas para acceder?</p>
            <a href="mailto:bienestar@epn.edu.ec">Contacta a Bienestar Politécnico</a>
            <p style="margin-top: 15px; font-size: 0.85rem; color: #666;">
                <strong>Credenciales de prueba:</strong><br>
                Estudiante: 1725896347 / 123456<br>
                Admin: admin001 / 123456
            </p>
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
        
        // Debug del formulario de login
        const loginForm = document.getElementById('loginForm');
        console.log('Formulario de login encontrado:', loginForm);
        console.log('Action del formulario:', loginForm.action);
        
        loginForm.addEventListener('submit', function(e) {
            console.log('=== FORMULARIO ENVIÁNDOSE ===');
            console.log('Rol:', document.getElementById('rol').value);
            console.log('Identificación:', document.getElementById('identificacion').value);
            console.log('Password:', document.getElementById('password').value ? '****' : 'vacío');
            console.log('Action URL:', this.action);
            
            // Validar que todos los campos estén llenos
            const rol = document.getElementById('rol').value;
            const id = document.getElementById('identificacion').value;
            const pass = document.getElementById('password').value;
            
            if (!rol || !id || !pass) {
                e.preventDefault();
                alert('Por favor completa todos los campos');
                return false;
            }
            
            console.log('Formulario válido, enviando...');
        });
    });
</script>

</body>
</html>
