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
            <li><a href="${pageContext.request.contextPath}/inicio.html" class="font-bold">Inicio</a></li>
            <li><a href="${pageContext.request.contextPath}/views/especialidades.jsp" class="font-bold">Especialidades</a></li>
            <li><a href="${pageContext.request.contextPath}/ConsultarCitasAgendadasController" class="font-bold">Mis Citas</a></li>
            <li><a href="${pageContext.request.contextPath}/reseñas.jsp" class="font-bold">Reseñas</a></li>
            <li class="login mt-2 mb-2" id="authButton">
                <a href="${pageContext.request.contextPath}/index.html" class="font-bold">Login</a>
            </li>
        </ul>
    </nav>
</header>

<main>
    <h1>Reseñas de Nuestros Servicios</h1>
    <p>Comparte tu experiencia y ayuda a otros estudiantes a conocer nuestros servicios</p>
</main>

<section class="resenas-page page-section">
    <div class="resenas-container">

        <!-- Formulario -->
        <div class="resena-form-card card">
            <h3>✍️ Deja tu Reseña</h3>
            <div id="resenaMessage" class="form-message"></div>

            <form id="resenaForm" class="resena-form">
                <div class="form-group">
                    <label for="especialidadResena">Especialidad/Servicio</label>
                    <select id="especialidadResena" name="especialidad" required>
                        <option value="">Selecciona una opción</option>
                        <option value="nutricion">🥗 Nutrición</option>
                        <option value="odontologia">🦷 Odontología</option>
                        <option value="psicologia">🧠 Psicología</option>
                        <option value="medicina-general">💊 Medicina General</option>
                        <option value="enfermeria">💉 Enfermería</option>
                        <option value="servicio-general">⭐ Servicio General</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Calificación</label>
                    <div class="rating-input" id="ratingInput">
                        <span class="star" data-value="1">★</span>
                        <span class="star" data-value="2">★</span>
                        <span class="star" data-value="3">★</span>
                        <span class="star" data-value="4">★</span>
                        <span class="star" data-value="5">★</span>
                    </div>
                    <input type="hidden" id="calificacion" name="calificacion" required>
                </div>

                <div class="form-group">
                    <label for="comentario">Tu Experiencia</label>
                    <textarea id="comentario" name="comentario" rows="5"
                              placeholder="Cuéntanos sobre tu experiencia..."
                              required minlength="20"></textarea>
                </div>

                <button type="submit" class="btn-agendar">Publicar Reseña</button>
            </form>
        </div>

        <!-- Lista -->
        <div class="resenas-display">
            <h2>💬 Experiencias Compartidas</h2>
            <div id="listaResenas" class="resenas-list"></div>
        </div>

    </div>
</section>

<footer>
    <p>&copy; 2025 Bienestar Politécnico - Escuela Politécnica Nacional</p>
</footer>

<!-- JS -->
<script src="${pageContext.request.contextPath}/js/auth-temporal.js"></script>
<script src="${pageContext.request.contextPath}/js/resenas.js"></script>

</body>
</html>
