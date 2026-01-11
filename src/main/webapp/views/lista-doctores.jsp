<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bienestar Politécnico - Doctores de ${especialidad.titulo}</title>
    <link rel="icon" type="image/png" href="../images/logo_epn.png">
    <link rel="stylesheet" href="./css/framework.css">
    <link rel="stylesheet" href="./css/styles.css">
    
    <style>
        .doctores-section {
            padding: 4rem 2rem;
            max-width: 1200px;
            margin: 0 auto;
        }
        
        .doctor-card {
            background: white;
            border-radius: 12px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            display: flex;
            gap: 2rem;
            align-items: flex-start;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        
        .doctor-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 4px 16px rgba(0,0,0,0.15);
        }
        
        .doctor-foto {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 3rem;
            color: white;
            flex-shrink: 0;
        }
        
        .doctor-info {
            flex: 1;
        }
        
        .doctor-nombre {
            font-size: 1.5rem;
            color: #2c3e50;
            margin-bottom: 0.5rem;
        }
        
        .doctor-especialidad {
            color: #667eea;
            font-weight: 600;
            margin-bottom: 1rem;
        }
        
        .doctor-descripcion {
            color: #666;
            line-height: 1.6;
            margin-bottom: 1rem;
        }
        
        .doctor-contacto {
            display: flex;
            gap: 2rem;
            margin-bottom: 1rem;
            color: #666;
        }
        
        .no-doctores {
            text-align: center;
            padding: 3rem;
            background: #f8f9fa;
            border-radius: 12px;
        }
        
        .breadcrumb {
            padding: 1rem 0;
            color: #666;
        }
        
        .breadcrumb a {
            color: #667eea;
            text-decoration: none;
        }
        
        .breadcrumb a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <header>
        <div class="logo">
            <img src="../images/logo.svg" alt="Logo">
        </div>
        <nav>
            <ul>
                <li><a href="../inicio.jsp" class="font-bold">Inicio</a></li>
                <li><a href="../especialidades.jsp" class="font-bold">Especialidades</a></li>
                <li><a href="../ConsultarCitasAgendadasController" class="font-bold">Mis Citas</a></li>
                <li><a href="../reseñas.jsp" class="font-bold">Reseñas</a></li>
                <li class="login mt-2 mb-2" id="authButton">
                    <a href="../index.jsp" class="font-bold">Login</a>
                </li>
            </ul>
        </nav>
    </header>

    <main>
        <div class="breadcrumb">
            <a href="../inicio.jsp">Inicio</a> / 
            <a href="../especialidades.jsp">Especialidades</a> / 
            <span>${especialidad.titulo}</span>
        </div>
        
        <h1>Doctores de ${especialidad.titulo}</h1>
        <p>Selecciona un doctor para ver su disponibilidad y agendar tu cita</p>
    </main>

    <section class="doctores-section">
        <c:choose>
            <c:when test="${not empty doctores}">
                <c:forEach var="doctor" items="${doctores}">
                    <article class="doctor-card">
                        <div class="doctor-foto">
                            <c:choose>
                                <c:when test="${not empty doctor.foto}">
                                    <img src="${doctor.foto}" alt="${doctor.nombreCompleto}" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">
                                </c:when>
                                <c:otherwise>
                                    👨‍⚕️
                                </c:otherwise>
                            </c:choose>
                        </div>
                        
                        <div class="doctor-info">
                            <h2 class="doctor-nombre">Dr(a). ${doctor.nombreCompleto}</h2>
                            <p class="doctor-especialidad">${doctor.especialidad.titulo}</p>
                            
                            <c:if test="${not empty doctor.descripcion}">
                                <p class="doctor-descripcion">${doctor.descripcion}</p>
                            </c:if>
                            
                            <div class="doctor-contacto">
                                <c:if test="${not empty doctor.email}">
                                    <span>📧 ${doctor.email}</span>
                                </c:if>
                                <c:if test="${not empty doctor.telefono}">
                                    <span>📞 ${doctor.telefono}</span>
                                </c:if>
                            </div>
                            
                            <a href="calendario.jsp?idDoctor=${doctor.idDoctor}&especialidad=${nombreEspecialidad}" 
                               class="btn btn-primary">
                                Ver Disponibilidad y Agendar
                            </a>
                        </div>
                    </article>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="no-doctores">
                    <h3>No hay doctores disponibles</h3>
                    <p>Lo sentimos, actualmente no tenemos doctores disponibles para esta especialidad.</p>
                    <a href="../especialidades.jsp" class="btn btn-primary">Volver a Especialidades</a>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <footer>
        <div class="footer-main">
            <div class="footer-section">
                <div class="footer-logo">Bienestar Politécnico</div>
                <p class="footer-tagline">Cuidando de ti, cuidamos el futuro de la Politécnica.</p>
            </div>
            <div class="footer-section">
                <h3>Enlaces Rápidos</h3>
                <ul class="footer-links">
                    <li><a href="../inicio.jsp">Inicio</a></li>
                    <li><a href="../especialidades.jsp">Especialidades</a></li>
                    <li><a href="../ConsultarCitasAgendadasController">Mis Citas</a></li>
                </ul>
            </div>
            <div class="footer-section">
                <h3>Contacto</h3>
                <p>bienestar@epn.edu.ec</p>
                <p>(+593) 2 2976 300 ext. 1132</p>
            </div>
        </div>
        <div class="footer-bottom">
            <p>&copy; 2025 Bienestar Politécnico. Todos los derechos reservados.</p>
        </div>
    </footer>

    <script src="../js/auth.js"></script>
</body>
</html>
