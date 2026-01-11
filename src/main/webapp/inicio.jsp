<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bienestar Politécnico</title>
    <link rel="icon" type="image/png" href="images/logo_epn.png">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/framework.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
    
    <!--Tipo de letra para bienvenida-->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@400..700&display=swap" rel="stylesheet">


</head>
<body>
    <header class="flex-between">
        <div class="logo">
            <img src="images/logo.svg" alt="Logo">
        </div>
        <nav>
            <ul>
                <li class="flex"><a href="inicio.jsp" class="font-bold">Inicio</a></li>
                <li class="flex"><a href="AgendarCitasController" class="font-bold">Especialidades</a></li>
                <li class="flex"><a href="ConsultarCitasAgendadasController" class="font-bold">Mis Citas</a></li>
                <li class="flex"><a href="reseñas.jsp" class="font-bold">Reseñas</a></li>
                <li class="login mt-2 mb-2" id="authButton"><a href="index.jsp" class="font-bold">Cerrar Sesión</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <h1> Bienvenido a Bienestar Politécnico</h1>
        <p> Tu bienestar es nuestra prioridad.</p>
    </main>

    <section class="informacion-inicio grid grid-auto-lg gap-xl">
        <div class="card hover-lift">
            <h2>Objetivo del Área de Bienestar Politécnico</h2>
            <p>El Área de Bienestar Politécnico tiene como objetivo promover el bienestar integral de la comunidad estudiantil mediante servicios y programas que apoyen su salud física, emocional y social. Nuestro compromiso es brindar acompañamiento, orientación y atención oportuna para contribuir al adecuado desarrollo académico y personal de los estudiantes.</p>
        </div>

        <div class="card hover-lift">
            <h2>Beneficios para los estudiantes</h2>
            <p>Los estudiantes de la institución cuentan con múltiples servicios diseñados para apoyar su bienestar y seguridad durante toda su trayectoria académica, entre los cuales destacan:</p>
            <ul>
                <li>Atención médica general y por especialidades, brindada por profesionales capacitados.</li>
                <li>Seguro de salud contra accidentes, disponible para todos los estudiantes activos.</li>
                <li>Acompañamiento y asesoría en procesos de salud, como derivaciones, orientaciones y seguimiento.</li>
                <li>Prevención y promoción de la salud, mediante campañas, talleres y actividades informativas.</li>
                <li>Un espacio seguro y de apoyo, donde la comunidad puede recibir orientación y atención oportuna ante cualquier eventualidad.</li>
            </ul>
        </div>
    </section>

    <!-- Hero del Seguro -->
    <section class="seguros-hero">
        <h1>Seguro de Vida y Accidentes Personales</h1>
        <p>Protección integral para todos los estudiantes de la EPN</p>
        <div class="hero-badge">
            <span class="badge-icon">🛡️</span>
            <span>Cobertura 24/7 en cualquier parte del mundo</span>
        </div>
    </section>

    <!-- Info Rápida -->
    <section class="info-rapida">
        <div class="info-card">
            <div class="info-icon">👥</div>
            <h3>¿Quiénes son beneficiarios?</h3>
            <p>Todos los estudiantes de la EPN legalmente matriculados en el periodo académico vigente.</p>
        </div>
        <div class="info-card">
            <div class="info-icon">📅</div>
            <h3>Vigencia</h3>
            <p><strong>730 días</strong><br>Del 21 agosto 2024 al 23 agosto 2026</p>
        </div>
        <div class="info-card">
            <div class="info-icon">💵</div>
            <h3>Deducible</h3>
            <p>Cancelar solo <strong>$10 USD</strong> por accidente</p>
        </div>
    </section>

    <!-- Coberturas -->
    <section class="coberturas-section">
        <h2>Principales Coberturas</h2>
        <div class="coberturas">
            <div class="cobertura-item">
                <div class="cobertura-monto">$8.800</div>
                <div class="cobertura-nombre">Muerte accidental</div>
            </div>
            <div class="cobertura-item">
                <div class="cobertura-monto">$5.500</div>
                <div class="cobertura-nombre">Desmembración accidental</div>
            </div>
            <div class="cobertura-item">
                <div class="cobertura-monto">$5.500</div>
                <div class="cobertura-nombre">Incapacidad total y permanente</div>
            </div>
            <div class="cobertura-item">
                <div class="cobertura-monto">$2.750</div>
                <div class="cobertura-nombre">Gastos médicos por accidente</div>
            </div>
            <div class="cobertura-item">
                <div class="cobertura-monto">$300</div>
                <div class="cobertura-nombre">Ambulancia por accidente</div>
            </div>
            <div class="cobertura-item">
                <div class="cobertura-monto">$1.650</div>
                <div class="cobertura-nombre">Beca por muerte del representante</div>
            </div>
            <div class="cobertura-item">
                <div class="cobertura-monto">$550</div>
                <div class="cobertura-nombre">Gastos por sepelio</div>
            </div>
        </div>
    </section>

    <!-- Qué hacer en emergencia -->
    <section class="emergencia-section">
        <h2>🚨 ¿Qué hacer en caso de emergencia?</h2>
        <div class="telefono-emergencia">
            <span>Llama al</span>
            <a href="tel:1800528462">1800 LATINA (528462)</a>
        </div>
        
        <div class="pasos-emergencia">
            <div class="paso">
                <div class="paso-numero">1</div>
                <div class="paso-contenido">
                    <h4>Llama al Call Center</h4>
                    <p>Indica tu número de cédula y ubicación actual.</p>
                </div>
            </div>
            <div class="paso">
                <div class="paso-numero">2</div>
                <div class="paso-contenido">
                    <h4>Acude a Emergencias</h4>
                    <p>Dirígete a la recepción de emergencia de la casa de salud indicada.</p>
                </div>
            </div>
            <div class="paso">
                <div class="paso-numero">3</div>
                <div class="paso-contenido">
                    <h4>Identifícate</h4>
                    <p>Presenta tu cédula e indica que eres estudiante EPN con convenio Latina Seguros.</p>
                </div>
            </div>
            <div class="paso">
                <div class="paso-numero">4</div>
                <div class="paso-contenido">
                    <h4>Recibe atención</h4>
                    <p>Cobertura hasta $2.500 USD. Paga solo $10 de deducible.</p>
                </div>
            </div>
            <div class="paso">
                <div class="paso-numero">5</div>
                <div class="paso-contenido">
                    <h4>Solicita tu certificado</h4>
                    <p>Pide el certificado médico para validar en Bienestar Politécnico.</p>
                </div>
            </div>
            <div class="paso">
                <div class="paso-numero">6</div>
                <div class="paso-contenido">
                    <h4>Notifica el accidente</h4>
                    <p>Agenda tu cita de control y notifica por correo para seguimiento.</p>
                </div>
            </div>
        </div>
    </section>

    <!-- Detalles de Coberturas -->
    <section class="detalles-section">
        <h2>Detalle de las Coberturas</h2>
        <div class="detalles-grid">
            <div class="detalle-card">
                <h4>💔 Muerte Accidental</h4>
                <p>El pago de la indemnización se efectuará por la muerte del asegurado. Este monto se entregará a los beneficiarios legales.</p>
            </div>
            <div class="detalle-card">
                <h4>🦾 Desmembración</h4>
                <p>Si el asegurado pierde una extremidad superior o inferior, la compañía pagará un porcentaje según la tabla de beneficios de la póliza.</p>
            </div>
            <div class="detalle-card">
                <h4>🏥 Gastos Médicos</h4>
                <p>Cubre tratamiento médico o quirúrgico, honorarios profesionales, medicinas, exámenes de imágenes, hospitalización, ambulancia y relacionados.</p>
            </div>
            <div class="detalle-card">
                <h4>⚱️ Gastos de Sepelio</h4>
                <p>Cubre servicios exequiales por fallecimiento de cualquier causa, las 24 horas, en cualquier lugar del mundo.</p>
            </div>
            <div class="detalle-card">
                <h4>🎓 Beca por Fallecimiento</h4>
                <p>En caso de fallecimiento del sustento económico del estudiante por accidente, se cancelará la totalidad de la beca.</p>
            </div>
            <div class="detalle-card">
                <h4>🏨 Red Hospitalaria</h4>
                <p>Servicio de crédito hospitalario en clínicas y hospitales afiliados a nivel nacional. Cualquier cambio será notificado en máximo 2 días laborables.</p>
            </div>
        </div>
    </section>

    <!-- Contacto -->
    <section class="contacto-seguros">
        <h2>📞 Contacto y Más Información</h2>
        <div class="contacto-grid">
            <div class="contacto-item-seguros">
                <h4>Correos electrónicos</h4>
                <a href="mailto:erika.heredia@epn.edu.ec">erika.heredia@epn.edu.ec</a>
                <a href="mailto:seguro.estudiantil2123@epn.edu.ec">seguro.estudiantil2123@epn.edu.ec</a>
            </div>
            <div class="contacto-item-seguros">
                <h4>Teléfonos</h4>
                <a href="tel:+593982428897">098-242-8897</a>
                <a href="tel:+59322976300">2976-300 ext. 1109</a>
            </div>
            <div class="contacto-item-seguros emergencia-box">
                <h4>Emergencias 24/7</h4>
                <a href="tel:1800528462" class="telefono-grande">1800 LATINA</a>
                <span>(528462)</span>
            </div>
        </div>
    </section>

    <footer>
        <div class="footer-main grid grid-auto gap-xl">
            <!-- Columna 1: Marca -->
            <div class="footer-section">
                <div class="footer-logo">Bienestar Politécnico</div>
                <p class="footer-tagline">Cuidando de ti, cuidamos el futuro de la Politécnica.</p>
                <div class="social-links flex gap-sm">
                    <a href="https://facebook.com/DBPEPN" class="social-link flex-center" title="Facebook">
                        <svg viewBox="0 0 24 24"><path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/></svg>
                    </a>
                    <a href="#" class="social-link" title="Instagram">
                        <svg viewBox="0 0 24 24"><path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z"/></svg>
                    </a>
                    <a href="#" class="social-link" title="Twitter/X">
                        <svg viewBox="0 0 24 24"><path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z"/></svg>
                    </a>
                </div>
            </div>

            <!-- Columna 2: Enlaces -->
            <div class="footer-section">
                <h3>Enlaces Rápidos</h3>
                <ul class="footer-links">
                    <li><a href="inicio.jsp">Inicio</a></li>
                    <li><a href="AgendarCitasController">Especialidades</a></li>
                    <li><a href="ConsultarCitasAgendadasController">Mis Citas</a></li>
                    <li><a href="reseñas.jsp">Reseñas</a></li>
                </ul>
            </div>

            <!-- Columna 3: Contacto -->
            <div class="footer-section">
                <h3>Contacto</h3>
                <div class="contact-item">
                    <div class="contact-icon">
                        <svg viewBox="0 0 24 24"><path d="M20 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"/></svg>
                    </div>
                    <div class="contact-info">
                        <span>Correo</span>
                        <p>bienestar@epn.edu.ec</p>
                    </div>
                </div>
                <div class="contact-item">
                    <div class="contact-icon">
                        <svg viewBox="0 0 24 24"><path d="M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.03.74-.25 1.02l-2.2 2.2z"/></svg>
                    </div>
                    <div class="contact-info">
                        <span>Teléfono</span>
                        <p>(+593) 2 2976 300 ext. 1132</p>
                    </div>
                </div>
                <div class="contact-item">
                    <div class="contact-icon">
                        <svg viewBox="0 0 24 24"><path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/></svg>
                    </div>
                    <div class="contact-info">
                        <span>Ubicación</span>
                        <p>Edificio N° 20 - Facultad de Sistemas<br>Edificio N° 26 - 1er piso</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="footer-bottom">
            <p>&copy; 2025 Bienestar Politécnico - Escuela Politécnica Nacional. Todos los derechos reservados.</p>
        </div>
    </footer>
    

    <script src="js/auth-temporal.js"></script>
    
</body>
</html>