<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bienestar Politécnico</title>

    <!-- Icono -->
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo_epn.png">

    <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/framework.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">

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
            <li><a href="${pageContext.request.contextPath}/inicio.jsp" class="font-bold">Inicio</a></li>
            <li><a href="${pageContext.request.contextPath}/especialidades?accion=listar" class="font-bold">Especialidades</a></li>
            <li><a href="${pageContext.request.contextPath}/ConsultarCitasAgendadasController" class="font-bold">Mis Citas</a></li>
            <li><a href="${pageContext.request.contextPath}/resenas" class="font-bold">Mis Reseñas</a></li>
            <li class="login mt-2 mb-2" id="authButton">
                <a href="${pageContext.request.contextPath}/index.jsp" class="font-bold">Login</a>
            </li>
        </ul>
    </nav>
</header>

<main>
    <h1>Reseña Nuestros Servicios</h1>
    <p>Comparte tu experiencia y ayúdanos a mejorar</p>
</main>

<section class="resenas-page page-section">
    <div class="resenas-container">

        <!-- Mensajes de éxito o error -->
        <c:if test="${not empty sessionScope.mensaje}">
            <div class="alert alert-success">
                ${sessionScope.mensaje}
            </div>
            <c:remove var="mensaje" scope="session"/>
        </c:if>
        
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-error">
                ${sessionScope.error}
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <!-- Formulario -->
        <div class="resena-form-card card">
            <h3>✍️ Deja tu Reseña</h3>
            <div id="resenaMessage" class="form-message"></div>

            <form action="${pageContext.request.contextPath}/resenas" method="post" class="resena-form" id="resenaForm">
                <input type="hidden" name="accion" value="crear">
                
                <!-- Seleccionar Especialidad -->
                <div class="form-group">
                    <label for="especialidadResena">Especialidad/Servicio <span class="required">*</span></label>
                    <select id="especialidadResena" name="idEspecialidad" required>
                        <option value="">Selecciona una especialidad</option>
                        <c:choose>
                            <c:when test="${not empty especialidades}">
                                <c:forEach var="esp" items="${especialidades}">
                                    <option value="${esp.idEspecialidad}">
                                        ${esp.icono} ${esp.titulo}
                                    </option>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <option value="">No hay especialidades disponibles</option>
                            </c:otherwise>
                        </c:choose>
                    </select>
                    
                    <!-- Debug info -->
                    <c:if test="${not empty especialidades}">
                        <small class="text-muted" style="display: block; margin-top: 0.5rem;">
                           ${especialidades.size()} especialidades disponibles
                        </small>
                    </c:if>
                    <c:if test="${empty especialidades}">
                        <small class="text-muted" style="display: block; margin-top: 0.5rem; color: red;">
                            No se cargaron especialidades. <a href="${pageContext.request.contextPath}/resenas">Recargar</a>
                        </small>
                    </c:if>
                </div>

                <!-- Seleccionar Doctor (filtrado por especialidad) -->
                <div class="form-group">
                    <label for="idDoctor">Doctor/Profesional <span class="required">*</span></label>
                    <select id="idDoctor" name="idDoctor" required disabled>
                        <option value="">Primero selecciona una especialidad</option>
                    </select>
                    <small class="text-muted">Selecciona una especialidad primero</small>
                </div>

                <!-- Calificación -->
                <div class="form-group">
                    <label>Calificación <span class="required">*</span></label>
                    <div class="rating-input" id="ratingInput">
                        <span class="star" data-value="1">★</span>
                        <span class="star" data-value="2">★</span>
                        <span class="star" data-value="3">★</span>
                        <span class="star" data-value="4">★</span>
                        <span class="star" data-value="5">★</span>
                    </div>
                    <input type="hidden" id="calificacion" name="calificacion" required>
                    <small class="text-muted">Haz clic en las estrellas para calificar</small>
                </div>

                <!-- Comentario -->
                <div class="form-group">
                    <label for="comentario">Tu Experiencia <span class="required">*</span></label>
                    <textarea id="comentario" name="comentario" rows="5"
                              placeholder="Cuéntanos sobre tu experiencia con el doctor..."
                              required minlength="20" maxlength="500"></textarea>
                    <small class="text-muted">Mínimo 20 caracteres, máximo 500</small>
                </div>

                <button type="submit" class="btn-agendar">Publicar Reseña</button>
            </form>
        </div>

        <!-- Lista de Reseñas -->
        <div class="resenas-display">
            <div class="resenas-header flex-between mb-4">
                <h2>💬 Experiencias de Nuestros Estudiantes</h2>
                <span class="badge">
                    <c:choose>
                        <c:when test="${not empty totalEvaluaciones}">
                            ${totalEvaluaciones} reseñas
                        </c:when>
                        <c:otherwise>
                            0 reseñas
                        </c:otherwise>
                    </c:choose>
                </span>
            </div>

            <!-- Filtros -->
            <div class="filtros-resenas mb-4">
                <form method="get" action="${pageContext.request.contextPath}/resenas" class="form-inline">
                    <input type="hidden" name="accion" value="filtrarPorEspecialidad">
                    
                    <div class="form-group">
                        <label for="filtroEspecialidad">Filtrar por especialidad:</label>
                        <select id="filtroEspecialidad" name="idEspecialidad" onchange="this.form.submit()">
                            <option value="">Todas las especialidades</option>
                            <c:forEach var="esp" items="${especialidades}">
                                <option value="${esp.idEspecialidad}" 
                                        ${param.idEspecialidad == esp.idEspecialidad ? 'selected' : ''}>
                                    ${esp.icono} ${esp.titulo}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </form>
            </div>

            <!-- Lista de reseñas -->
            <div id="listaResenas" class="resenas-list">
                <c:choose>
                    <c:when test="${empty evaluaciones}">
                        <div class="no-resenas text-center card">
                            <p class="text-muted">No hay reseñas disponibles</p>
                            <p class="text-muted text-sm">¡Deja tu primera Reseña!</p>
                        </div>
                    </c:when>
                    
                    <c:otherwise>
                        <c:forEach var="evaluacion" items="${evaluaciones}">
                            <div class="resena-card card hover-lift">
                                <div class="resena-header flex-between">
                                    <div>
                                        <h3 class="resena-usuario">
                                            ${evaluacion.estudiante.nombre}
                                        </h3>
                                        <span class="resena-especialidad">
                                            ${evaluacion.doctor.especialidad.icono} 
                                            Dr. ${evaluacion.doctor.nombre} - 
                                            ${evaluacion.doctor.especialidad.titulo}
                                        </span>
                                    </div>
                                    <div class="resena-rating">
                                        <c:forEach begin="1" end="5" var="i">
                                            <span class="star-display ${i <= evaluacion.calificacion ? 'active' : ''}">★</span>
                                        </c:forEach>
                                    </div>
                                </div>
                                
                                <div class="resena-body">
                                    <p class="resena-comentario text-justify">
                                        ${evaluacion.comentario}
                                    </p>
                                </div>
                                
                                <div class="resena-footer flex-between">
                                    <span class="resena-fecha text-muted text-sm">
                                        <fmt:formatDate value="${evaluacion.fechaEvaluacion}" 
                                                        pattern="dd 'de' MMMM 'de' yyyy" />
                                    </span>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

    </div>
</section>

<footer>
    <p>&copy; 2025 Bienestar Politécnico - Escuela Politécnica Nacional</p>
</footer>

<!-- IMPORTANTE: Pasar datos de doctores a JavaScript ANTES de cargar resenas.js -->
<script>
    // Doctores disponibles (desde el servidor)
    var doctoresDisponibles = [
        <c:forEach var="doctor" items="${doctores}" varStatus="status">
        {
            id: ${doctor.idDoctor},
            nombre: "${doctor.nombre}",
            especialidadId: ${doctor.especialidad.idEspecialidad},
            especialidadNombre: "${doctor.especialidad.titulo}",
            especialidadIcono: "${doctor.especialidad.icono}"
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];
    
    console.log('=== DEBUG RESENAS ===');
    console.log('Doctores disponibles:', doctoresDisponibles);
    console.log('Total doctores:', doctoresDisponibles.length);
    console.log('Especialidades en page:', ${not empty especialidades});
</script>

<!-- JS -->
<script src="${pageContext.request.contextPath}/js/auth-temporal.js"></script>
<script src="${pageContext.request.contextPath}/js/resenas.js"></script>

</body>
</html>