<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Citas Agendadas - Bienestar Politécnico</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo_epn.png">

    <!-- CSS (se respeta al 100%) -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/framework.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@400..700&display=swap" rel="stylesheet">
</head>
<body>

<!-- Header -->
<header class="flex-between">
    <div class="logo">
        <img src="${pageContext.request.contextPath}/images/logo.svg" alt="Logo">
    </div>
    <nav>
        <ul>
            <li class="flex">
                <a href="${pageContext.request.contextPath}/inicio.jsp" class="font-bold">Inicio</a>
            </li>
            <li class="flex">
                <a href="${pageContext.request.contextPath}/views/citas-agendadas.jsp" class="font-bold">Citas Agendadas</a>
            </li>
            <li class="login mt-2 mb-2" id="authButton">
                <a href="${pageContext.request.contextPath}/index.jsp" class="font-bold">Login</a>
            </li>
        </ul>
    </nav>
</header>

<!-- Main Content -->
<main class="citas-agendadas-doctor-page">
    <h1 class="text-center mb-lg" id="nombreDoctor">
        ${doctor != null ? doctor : "Doctor"}
    </h1>

    <p class="text-center subtitle-text mb-2xl">
        Visualiza tus citas agendadas por día
    </p>

    <!-- Calendario -->
    <div class="container-sm">
        <div class="card calendar-container">

            <!-- Navegación del mes -->
            <div class="calendar-header">
                <button id="prevMonth" class="calendar-nav-btn">&larr;</button>
                <button id="todayBtn" class="calendar-today-btn">Hoy</button>
                <h2 id="mesActual">Diciembre de 2025</h2>
                <button id="nextMonth" class="calendar-nav-btn">&rarr;</button>
            </div>

            <!-- Días de la semana -->
            <div class="calendar-weekdays">
                <div class="calendar-weekday">lun</div>
                <div class="calendar-weekday">mar</div>
                <div class="calendar-weekday">mié</div>
                <div class="calendar-weekday">jue</div>
                <div class="calendar-weekday">vie</div>
                <div class="calendar-weekday">sáb</div>
                <div class="calendar-weekday">dom</div>
            </div>

            <!-- Grid de días -->
            <div id="calendarioDias" class="calendar-grid">
                <!-- JS carga los días -->
            </div>
        </div>
    </div>

    <!-- Modal citas del día -->
    <div id="modalCitas" class="modal">
        <div class="modal-content card calendar-modal-content">
            <div class="flex-between mb-xl">
                <h3 id="tituloModal">Citas del día</h3>
                <button onclick="cerrarModal()" class="btn calendar-modal-close">✕</button>
            </div>
            <div id="listadoCitasDia">
                <!-- JS carga citas -->
            </div>
        </div>
    </div>
</main>

<!-- Footer -->
<footer class="text-center p-2xl bg-primary text-white mt-3xl">
    <div class="footer-content">
        <h3>Escuela Politécnica Nacional</h3>
        <p>Departamento de Bienestar Estudiantil</p>
        <p>Email: bienestar@epn.edu.ec | Teléfono: (02) 297-6300 Ext. 1234</p>
        <p class="mt-lg">Horario de Atención: Lunes a Viernes, 8:00 AM - 5:00 PM</p>
    </div>
    <p class="mt-xl">
        &copy; 2024 Escuela Politécnica Nacional. Todos los derechos reservados.
    </p>
</footer>

<!-- JS -->
<script src="${pageContext.request.contextPath}/js/auth.js"></script>
<script src="${pageContext.request.contextPath}/js/citas-agendadas.js"></script>

</body>
</html>
