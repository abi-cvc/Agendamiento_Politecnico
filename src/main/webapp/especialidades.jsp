<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bienestar Politécnico</title>

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
    <h1>Nuestras Especialidades</h1>
    <p>Contamos con profesionales especializados para cuidar tu salud integral</p>
</main>

<section class="especialidades-section">
    <div class="especialidades-container">

        <!-- Menú lateral -->
        <aside class="especialidades-menu">
            <h3>Especialidades</h3>
            <nav class="menu-especialidades">
                <a href="#nutricion" class="menu-item active">🥗 Nutrición</a>
                <a href="#odontologia" class="menu-item">🦷 Odontología</a>
                <a href="#psicologia" class="menu-item">🧠 Psicología</a>
                <a href="#medicina" class="menu-item">⚕️ Medicina General</a>
                <a href="#enfermeria" class="menu-item">💉 Enfermería</a>
            </nav>
        </aside>

        <!-- Contenido -->
        <section class="especialidades-content">

            <!-- Nutrición -->
            <article id="nutricion" class="especialidad-card">
                <h2>🥗 Nutrición</h2>
                <p>
                    Nuestro equipo de nutricionistas certificados te ayudará a alcanzar tus metas de salud
                    mediante planes personalizados.
                </p>
                <ul>
                    <li>Evaluación nutricional</li>
                    <li>Planes personalizados</li>
                    <li>Seguimiento y control</li>
                </ul>
                <a href="${pageContext.request.contextPath}/views/agendamientos.jsp?especialidad=nutricion"
                   class="btn btn-primary">Agendar Cita</a>
            </article>

            <!-- Odontología -->
            <article id="odontologia" class="especialidad-card">
                <h2>🦷 Odontología</h2>
                <p>
                    Atención odontológica integral con profesionales capacitados y tecnología moderna.
                </p>
                <ul>
                    <li>Limpieza dental</li>
                    <li>Tratamiento de caries</li>
                    <li>Extracciones</li>
                </ul>
                <a href="${pageContext.request.contextPath}/views/agendamientos.jsp?especialidad=odontologia"
                   class="btn btn-primary">Agendar Cita</a>
            </article>

            <!-- Psicología -->
            <article id="psicologia" class="especialidad-card">
                <h2>🧠 Psicología</h2>
                <p>
                    Apoyo emocional y psicológico en un entorno seguro y confidencial.
                </p>
                <ul>
                    <li>Consulta individual</li>
                    <li>Manejo del estrés</li>
                    <li>Orientación vocacional</li>
                </ul>
                <a href="${pageContext.request.contextPath}/views/agendamientos.jsp?especialidad=psicologia"
                   class="btn btn-primary">Agendar Cita</a>
            </article>

            <!-- Medicina -->
            <article id="medicina" class="especialidad-card">
                <h2>⚕️ Medicina General</h2>
                <p>
                    Atención médica integral para diagnóstico, tratamiento y prevención.
                </p>
                <ul>
                    <li>Consulta general</li>
                    <li>Certificados médicos</li>
                    <li>Derivaciones</li>
                </ul>
                <a href="${pageContext.request.contextPath}/views/agendamientos.jsp?especialidad=medicina-general"
                   class="btn btn-primary">Agendar Cita</a>
            </article>

            <!-- Enfermería -->
            <article id="enfermeria" class="especialidad-card">
                <h2>💉 Enfermería</h2>
                <p>
                    Procedimientos de enfermería y educación en salud preventiva.
                </p>
                <ul>
                    <li>Signos vitales</li>
                    <li>Curaciones</li>
                    <li>Vacunación</li>
                </ul>
                <a href="${pageContext.request.contextPath}/views/agendamientos.jsp?especialidad=enfermeria"
                   class="btn btn-primary">Agendar Cita</a>
            </article>

        </section>
    </div>
</section>

<footer>
    <p>&copy; 2025 Bienestar Politécnico - Escuela Politécnica Nacional</p>
</footer>

<!-- JS -->
<script src="${pageContext.request.contextPath}/js/auth.js"></script>
<script src="${pageContext.request.contextPath}/js/especialidades.js"></script>

</body>
</html>
