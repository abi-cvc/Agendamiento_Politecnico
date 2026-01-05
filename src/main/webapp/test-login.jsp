<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Test Login</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            background: #f5f5f5;
        }
        .test-card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 { color: #001f3f; margin-top: 0; }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input, select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
        }
        button {
            width: 100%;
            padding: 12px;
            background: #3c8dbc;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background: #2c7aac;
        }
        .info {
            background: #e3f2fd;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .error {
            background: #fee2e2;
            color: #dc2626;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="test-card">
        <h1>🧪 Test de Login</h1>
        
        <div class="info">
            <strong>Servlet URL:</strong> <%= request.getContextPath() %>/login<br>
            <strong>Context Path:</strong> <%= request.getContextPath() %><br>
            <strong>Method:</strong> POST
        </div>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="error">
                <strong>Error:</strong> <%= request.getAttribute("error") %>
            </div>
        <% } %>
        
        <form action="<%= request.getContextPath() %>/login" method="post">
            <div class="form-group">
                <label>Rol:</label>
                <select name="rol" required>
                    <option value="">Seleccionar...</option>
                    <option value="estudiante">Estudiante</option>
                    <option value="admin">Administrador</option>
                    <option value="doctor">Doctor</option>
                </select>
            </div>
            
            <div class="form-group">
                <label>Identificación:</label>
                <input type="text" name="identificacion" value="1725896347" required>
            </div>
            
            <div class="form-group">
                <label>Contraseña:</label>
                <input type="password" name="password" value="123456" required>
            </div>
            
            <button type="submit">🚀 Probar Login</button>
        </form>
        
        <div class="info" style="margin-top: 20px;">
            <strong>Credenciales pre-cargadas:</strong><br>
            ID: 1725896347<br>
            Password: 123456<br>
            Rol: Estudiante
        </div>
    </div>
</body>
</html>
