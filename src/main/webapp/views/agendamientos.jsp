<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agendar Cita Médica - Bienestar Politécnico</title>

    <!-- CSS -->
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo_epn.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/framework.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">

    <!-- Google Font -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@400..700&display=swap" rel="stylesheet">
</head>

<body>

<!-- ================= HEADER ================= -->
<header>
    <div class="logo">
        <img src="${pageContext.request.contextPath}/images/logo.svg" alt="Logo">
    </div>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/inicio.jsp" class="font-bold">Inicio</a></li>
            <li><a href="${pageContext.request.contextPath}/especialidades.jsp" class="font-bold">Especialidades</a></li>
            <li><a href="${pageContext.request.contextPath}/consultar-citas.jsp" class="font-bold">Mis Citas</a></li>
            <li><a href="${pageContext.request.contextPath}/reseñas.jsp" class="font-bold">Reseñas</a></li>
            <li class="login mt-2 mb-2" id="authButton">
                <a href="${pageContext.request.contextPath}/index.jsp" class="font-bold">Login</a>
            </li>
        </ul>
    </nav>
</header>

<!-- ================= MAIN ================= -->
<main>
    <h1>Agendar Cita Médica</h1>
    <p>Bienvenido/a, <span id="userName">Usuario</span></p>
</main>

<!-- ================= AGENDAMIENTO ================= -->
<section class="agendamientos-page">
    <div class="agendamiento-container">

        <!-- FORMULARIO -->
        <div class="agendamiento-form">
            <h3>Nueva Cita</h3>
            <div id="formMessage" class="form-message"></div>

            <form id="agendamientoForm"
                  action="${pageContext.request.contextPath}/agendarCita?accion=agendarCita"
                  method="post">

                <div class="form-group">
                    <label for="especialidad">Especialidad</label>
                    <select id="especialidad" name="especialidad" required>
                        <option value="">Seleccione una especialidad</option>
                        <option value="nutricion">Nutrición</option>
                        <option value="odontologia">Odontología</option>
                        <option value="psicologia">Psicología</option>
                        <option value="medicina-general">Medicina General</option>
                        <option value="enfermeria">Enfermería</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="doctor">Doctor</label>
                    <select id="doctor" name="doctor" required disabled>
                        <option value="">Primero seleccione una especialidad</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="fecha">Fecha</label>
                    <input type="date" id="fecha" name="fecha" required disabled>
                </div>

                <div class="form-group">
                    <label for="hora">Hora</label>
                    <input type="time" id="hora" name="hora" required disabled>
                </div>

                <div class="form-group">
                    <label for="motivo">Motivo de la consulta</label>
                    <textarea id="motivo" name="motivo"
                              placeholder="Describe brevemente el motivo de tu consulta"
                              required></textarea>
                </div>

                <button type="submit" class="btn-agendar" disabled>
                    Agendar Cita
                </button>
            </form>
        </div>

        <!-- HORARIOS -->
        <div class="horarios-disponibles">
            <h3>Horarios Disponibles</h3>
            <div id="horariosContainer" class="horarios-container">
                <p class="no-horarios">
                    Seleccione un doctor y fecha para ver los horarios disponibles
                </p>
            </div>
        </div>

    </div>
</section>

<!-- ================= FOOTER ================= -->
<footer>
    <div class="footer-main">
        <div class="footer-section">
            <div class="footer-logo">Bienestar Politécnico</div>
            <p class="footer-tagline">
                Cuidando de ti, cuidamos el futuro de la Politécnica.
            </p>
        </div>
    </div>

    <div class="footer-bottom">
        <p>
            &copy; 2025 Bienestar Politécnico - Escuela Politécnica Nacional.
            Todos los derechos reservados.
        </p>
    </div>
</footer>

<!-- ================= JS ================= -->
<script src="${pageContext.request.contextPath}/js/auth.js"></script>
<script src="${pageContext.request.contextPath}/js/agendamientos.js"></script>

</body>
</html>
