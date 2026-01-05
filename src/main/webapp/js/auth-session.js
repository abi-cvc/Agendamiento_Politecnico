// ===== AUTH CON SESIONES JSP =====
// Este archivo maneja la autenticación usando sesiones del servidor

/**
 * Verifica si hay una sesión activa
 * NOTA: Esta versión usa sessionStorage como fallback
 * pero la sesión real está en el servidor (JSP)
 */
function verificarSesion() {
    // Intentar obtener datos de la sesión desde sessionStorage
    const usuarioStr = sessionStorage.getItem('usuario');
    
    if (usuarioStr) {
        try {
            return JSON.parse(usuarioStr);
        } catch (e) {
            console.error('Error al parsear usuario:', e);
            return null;
        }
    }
    
    return null;
}

/**
 * Actualiza el botón de autenticación en el header
 */
function actualizarBotonAuth() {
    const authButton = document.getElementById('authButton');
    if (!authButton) return;
    
    const usuario = verificarSesion();
    
    if (usuario) {
        authButton.innerHTML = `
            <a href="#" class="font-bold" onclick="cerrarSesion(); return false;">
                ${usuario.nombre} - Salir
            </a>
        `;
    } else {
        authButton.innerHTML = `
            <a href="${getContextPath()}/index.jsp" class="font-bold">Login</a>
        `;
    }
}

/**
 * Cierra la sesión
 */
function cerrarSesion() {
    if (confirm('¿Estás seguro que deseas cerrar sesión?')) {
        sessionStorage.clear();
        window.location.href = getContextPath() + '/logout';
    }
}

/**
 * Obtiene el context path de la aplicación
 */
function getContextPath() {
    const path = window.location.pathname;
    const contextPath = path.substring(0, path.indexOf('/', 2));
    return contextPath || '';
}

/**
 * Guarda los datos del usuario en sessionStorage
 * (llamado desde el servidor después de login exitoso)
 */
function guardarSesion(usuario) {
    sessionStorage.setItem('usuario', JSON.stringify(usuario));
    console.log('Sesión guardada:', usuario);
}

/**
 * Redirige al login si no hay sesión
 */
function requiereLogin() {
    const usuario = verificarSesion();
    if (!usuario) {
        sessionStorage.setItem('paginaAnterior', window.location.pathname);
        window.location.href = getContextPath() + '/index.jsp';
        return false;
    }
    return true;
}

// Actualizar botón al cargar la página
document.addEventListener('DOMContentLoaded', function() {
    actualizarBotonAuth();
});
