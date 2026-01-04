<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Atender Cita - Bienestar Politécnico</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo_epn.png">

    <!-- CSS (NO se modifica, solo rutas correctas) -->
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
                <a href="${pageContext.request.contextPath}/citas-agendadas.jsp" class="font-bold">Citas Agendadas</a>
            </li>
            <li class="login mt-2 mb-2" id="authButton">
                <a href="${pageContext.request.contextPath}/index.jsp" class="font-bold">Login</a>
            </li>
        </ul>
    </nav>
</header>

<!-- Main Content -->
<main class="atender-cita-page">
    <h1 class="text-center mb-lg" id="nombreDoctor">
        ${doctor != null ? doctor : "Doctor"}
    </h1>

    <p class="text-center subtitle-text mb-2xl">
        Atiende y completa las citas programadas
    </p>

    <!-- Selector de fecha -->
    <div class="container-sm mb-2xl">
        <div class="date-selector-card">
            <input type="date" id="filtroFechaAtencion" class="date-input-hidden">
            <div id="fechaDisplay" class="date-display-box">
                <div class="date-display-content">
                    <span id="diaNumero" class="date-day">22</span>
                    <span id="mesTexto" class="date-month">diciembre</span>
                    <span id="numeroCitas" class="date-count">5 citas</span>
                </div>
            </div>
        </div>
    </div>

    <!-- Listado de Citas -->
    <div class="container-sm">
        <div id="listadoCitasAtencion" class="atender-citas-list">
            <!-- JS cargará las citas aquí -->
        </div>

        <!-- Mensaje vacío -->
        <div id="mensajeSinCitasAtencion" class="mensaje-vacio card">
            <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                <line x1="16" y1="2" x2="16" y2="6"></line>
                <line x1="8" y1="2" x2="8" y2="6"></line>
                <line x1="3" y1="10" x2="21" y2="10"></line>
            </svg>
            <p class="text-center mt-lg">
                No hay citas pendientes para la fecha seleccionada
            </p>
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
<script src="${pageContext.request.contextPath}/js/atender-cita.js"></script>

</body>
</html>
