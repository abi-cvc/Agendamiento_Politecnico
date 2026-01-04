<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bienestar Politécnico - Mis Citas</title>

    <!-- Icono -->
    <link rel="icon" type="image/png"
          href="${pageContext.request.contextPath}/images/logo_epn.png">

    <!-- CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/framework.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/styles.css">

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@400..700&display=swap"
          rel="stylesheet">
</head>

<body>

<header>
    <div class="logo">
        <img src="${pageContext.request.contextPath}/images/logo.svg" alt="Logo">
    </div>

    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/views/inicio.jsp" class="font-bold">Inicio</a></li>
            <li><a href="${pageContext.request.contextPath}/views/especialidades.jsp" class="font-bold">Especialidades</a></li>
            <li><a href="${pageContext.request.contextPath}/views/consultar-citas.jsp" class="font-bold">Mis Citas</a></li>
            <li><a href="${pageContext.request.contextPath}/views/reseñas.jsp" class="font-bold">Reseñas</a></li>
            <li class="login mt-2 mb-2" id="authButton">
                <a href="${pageContext.request.contextPath}/index.jsp" class="font-bold">Login</a>
            </li>
        </ul>
    </nav>
</header>

<main>
    <h1>Mis Citas Médicas</h1>
    <p>Bienvenido/a, <span id="userName">Usuario</span></p>
</main>

<section class="consultar-citas-page">
    <div class="consultar-container">

        <div class="citas-header">
            <h2>Historial de Citas</h2>

            <div class="filtros">
                <select id="filtroEstado" class="filtro-select">
                    <option value="todas">Todas las citas</option>
                    <option value="Pendiente">Pendientes</option>
                    <option value="Completada">Completadas</option>
                    <option value="Cancelada">Canceladas</option>
                </select>
            </div>
        </div>

        <!-- Aquí luego se llenará desde el Servlet -->
        <div id="listaCitas" class="lista-citas-consulta">
            <p class="no-citas">No tienes citas agendadas</p>
        </div>

    </div>
</section>

<footer>
    <div class="footer-main">

        <!-- Marca -->
        <div class="footer-section">
            <div class="footer-logo">Bienestar Politécnico</div>
            <p class="footer-tagline">
                Cuidando de ti, cuidamos el futuro de la Politécnica.
            </p>

            <div class="social-links">
                <a href="https://facebook.com/DBPEPN" class="social-link" title="Facebook">
                    <!-- SVG -->
                </a>
                <a href="#" class="social-link" title="Instagram"></a>
                <a href="#" class="social-link" title="Twitter/X"></a>
            </div>
        </div>

        <!-- Enlaces -->
        <div class="footer-section">
            <h3>Enlaces Rápidos</h3>
            <ul class="footer-links">
                <li><a href="${pageContext.request.contextPath}/views/inicio.jsp">Inicio</a></li>
                <li><a href="${pageContext.request.contextPath}/views/especialidades.jsp">Especialidades</a></li>
                <li><a href="${pageContext.request.contextPath}/views/consultar-citas.jsp">Mis Citas</a></li>
                <li><a href="${pageContext.request.contextPath}/views/reseñas.jsp">Reseñas</a></li>
            </ul>
        </div>

        <!-- Contacto -->
        <div class="footer-section">
            <h3>Contacto</h3>
            <p>bienestar@epn.edu.ec</p>
            <p>(+593) 2 2976 300 ext. 1132</p>
            <p>Facultad de Sistemas – EPN</p>
        </div>

    </div>

    <div class="footer-bottom">
        <p>&copy; 2025 Bienestar Politécnico - Escuela Politécnica Nacional</p>
    </div>
</footer>

<!-- JS -->
<script src="${pageContext.request.contextPath}/js/auth.js"></script>
<script src="${pageContext.request.contextPath}/js/consultar-citas.js"></script>

</body>
</html>
