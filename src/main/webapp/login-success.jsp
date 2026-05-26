<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Verificar si hay sesión activa
    String nombreUsuario = (String) session.getAttribute("nombreUsuario");
    String rol = (String) session.getAttribute("rol");
    
    if (nombreUsuario != null && rol != null) {
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login Exitoso</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            background: linear-gradient(135deg, #3c8dbc 0%, #001f3f 100%);
        }
        .success-card {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
            text-align: center;
            max-width: 400px;
        }
        .success-icon {
            font-size: 80px;
            color: #28a745;
            margin-bottom: 20px;
        }
        h1 {
            color: #001f3f;
            margin: 0 0 10px 0;
        }
        p {
            color: #666;
            margin: 10px 0;
        }
        .user-info {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin: 20px 0;
        }
        .btn {
            display: inline-block;
            padding: 12px 30px;
            background: linear-gradient(135deg, #3c8dbc, #001f3f);
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin: 10px 5px;
            transition: transform 0.2s;
        }
        .btn:hover {
            transform: translateY(-2px);
        }
    </style>
    <script>
        // Guardar datos en sessionStorage para JavaScript
        sessionStorage.setItem('usuario', JSON.stringify({
            nombre: '<%= nombreUsuario %>',
            rol: '<%= rol %>',
            id: <%= session.getAttribute("idUsuario") %>
        }));
        
        // Redirigir automáticamente después de 3 segundos
        setTimeout(function() {
            window.location.href = '<%= request.getContextPath() %>/inicio.jsp';
        }, 3000);
    </script>
</head>
<body>
    <div class="success-card">
        <div class="success-icon">✓</div>
        <h1>¡Bienvenido!</h1>
        <div class="user-info">
            <p><strong>Usuario:</strong> <%= nombreUsuario %></p>
            <p><strong>Rol:</strong> <%= rol.toUpperCase() %></p>
        </div>
        <p>Login exitoso. Redirigiendo...</p>
        <a href="<%= request.getContextPath() %>/inicio.jsp" class="btn">Ir a Inicio</a>
        <a href="<%= request.getContextPath() %>/especialidades.jsp" class="btn">Ver Especialidades</a>
    </div>
</body>
</html>
<%
    } else {
        // No hay sesión, redirigir al login
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
%>
