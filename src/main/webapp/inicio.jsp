<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
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
<header class="flex-between">
    <div class="logo">
        <img src="<%= request.getContextPath() %>/images/logo.svg" alt="Logo">
    </div>
    <nav>
        <ul>
            <li class="flex"><a href="<%= request.getContextPath() %>/inicio.jsp" class="font-bold">Inicio</a></li>
            <li class="flex"><a href="<%= request.getContextPath() %>/especialidades.jsp" class="font-bold">Especialidades</a></li>
            <li class="flex"><a href="<%= request.getContextPath() %>/consultar-citas.jsp" class="font-bold">Mis Citas</a></li>
            <li class="flex"><a href="<%= request.getContextPath() %>/reseñas.jsp" class="font-bold">Reseñas</a></li>
            <li class="login mt-2 mb-2">
                <a href="<%= request.getContextPath() %>/index.jsp" class="font-bold">Cerrar Sesión</a>
            </li>
        </ul>
    </nav>
</header>

<main>
    <h1>Bienvenido a Bienestar Politécnico</h1>
    <p>Tu bienestar es nuestra prioridad.</p>
</main>

<section class="informacion-inicio grid grid-auto-lg gap-xl">
    <div class="card hover-lift">
        <h2>Objetivo del Área de Bienestar Politécnico</h2>
        <p>
            El Área de Bienestar Politécnico tiene como objetivo promover el bienestar integral
            de la comunidad estudiantil mediante servicios y programas que apoyen su salud física,
            emocional y social.
        </p>
    </div>

    <div class="card hover-lift">
        <h2>Beneficios para los estudiantes</h2>
        <ul>
            <li>Atención médica general y por especialidades.</li>
            <li>Seguro de salud contra accidentes.</li>
            <li>Acompañamiento y asesoría en procesos de salud.</li>
            <li>Prevención y promoción de la salud.</li>
            <li>Un espacio seguro de apoyo y orientación.</li>
        </ul>
    </div>
</section>

<!-- (El resto del contenido permanece igual, solo se corrigieron rutas) -->

<footer>
    <div class="footer-bottom">
        <p>&copy; 2025 Bienestar Politécnico - Escuela Politécnica Nacional.</p>
    </div>
</footer>

<script src="<%= request.getContextPath() %>/js/auth.js"></script>

</body>
</html>
