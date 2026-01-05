/**
 * cancelar-cita.js
 * Script para manejar la cancelación de citas
 * Según diagrama de robustez
 */

/**
 * Cancela una cita mostrando confirmación primero
 * @param {number} idCita - ID de la cita a cancelar
 * @param {string} from - Origen: 'atender' o 'consultar'
 */
function cancelarCita(idCita, from) {
    console.log('🚫 Iniciando cancelación de cita:', idCita, 'desde:', from);
    
    // ===== 1 y 2. MOSTRAR CONFIRMACIÓN =====
    const confirmacion = confirm(
        '¿Está seguro que desea cancelar esta cita?\n\n' +
        'Esta acción no se puede deshacer.\n' +
        'El horario quedará disponible nuevamente.'
    );
    
    if (!confirmacion) {
        console.log('❌ Cancelación abortada por el usuario');
        return;
    }
    
    // ===== 3. CONFIRMAR CANCELACIÓN =====
    console.log('✅ Usuario confirmó la cancelación');
    
    // Crear formulario dinámico
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = getContextPath() + '/CancelarCitaController';
    
    // Agregar campos ocultos
    const inputIdCita = document.createElement('input');
    inputIdCita.type = 'hidden';
    inputIdCita.name = 'idCita';
    inputIdCita.value = idCita;
    form.appendChild(inputIdCita);
    
    const inputConfirmar = document.createElement('input');
    inputConfirmar.type = 'hidden';
    inputConfirmar.name = 'confirmar';
    inputConfirmar.value = 'true';
    form.appendChild(inputConfirmar);
    
    const inputFrom = document.createElement('input');
    inputFrom.type = 'hidden';
    inputFrom.name = 'from';
    inputFrom.value = from;
    form.appendChild(inputFrom);
    
    // Agregar al DOM y enviar
    document.body.appendChild(form);
    form.submit();
}

/**
 * Obtiene el context path de la aplicación
 */
function getContextPath() {
    // Intenta obtener del atributo data-context
    const contextMeta = document.querySelector('[data-context]');
    if (contextMeta) {
        return contextMeta.getAttribute('data-context');
    }
    
    // Fallback: obtener de la URL actual
    const path = window.location.pathname;
    const contextPath = path.substring(0, path.indexOf('/', 1));
    return contextPath || '';
}

/**
 * Muestra mensaje de éxito o error según parámetros URL
 */
document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    
    // Verificar mensaje de éxito
    if (urlParams.get('success') === 'cancelada') {
        mostrarMensaje('✅ Cita cancelada exitosamente', 'success');
        
        // Limpiar URL
        window.history.replaceState({}, document.title, window.location.pathname);
    }
    
    // Verificar mensaje de error
    const errorMsg = urlParams.get('error');
    if (errorMsg) {
        mostrarMensaje('❌ Error: ' + decodeURIComponent(errorMsg), 'error');
        
        // Limpiar URL
        window.history.replaceState({}, document.title, window.location.pathname);
    }
});

/**
 * Muestra un mensaje temporal en la parte superior de la página
 */
function mostrarMensaje(mensaje, tipo) {
    // Crear elemento de mensaje
    const div = document.createElement('div');
    div.className = `alert alert-${tipo} mensaje-temporal`;
    div.textContent = mensaje;
    div.style.cssText = `
        position: fixed;
        top: 100px;
        left: 50%;
        transform: translateX(-50%);
        z-index: 9999;
        padding: 1rem 2rem;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.2);
        animation: slideDown 0.3s ease-out;
        min-width: 300px;
        text-align: center;
        font-weight: 600;
    `;
    
    // Estilos según tipo
    if (tipo === 'success') {
        div.style.background = '#d1fae5';
        div.style.color = '#065f46';
        div.style.border = '2px solid #10b981';
    } else if (tipo === 'error') {
        div.style.background = '#fee2e2';
        div.style.color = '#dc2626';
        div.style.border = '2px solid #ef4444';
    }
    
    // Agregar al DOM
    document.body.appendChild(div);
    
    // Animación de entrada
    const style = document.createElement('style');
    style.textContent = `
        @keyframes slideDown {
            from {
                opacity: 0;
                transform: translateX(-50%) translateY(-20px);
            }
            to {
                opacity: 1;
                transform: translateX(-50%) translateY(0);
            }
        }
    `;
    document.head.appendChild(style);
    
    // Eliminar después de 5 segundos
    setTimeout(() => {
        div.style.animation = 'slideUp 0.3s ease-out';
        div.style.opacity = '0';
        div.style.transform = 'translateX(-50%) translateY(-20px)';
        
        setTimeout(() => {
            div.remove();
            style.remove();
        }, 300);
    }, 5000);
}

// Exportar funciones globales
window.cancelarCita = cancelarCita;
window.mostrarMensaje = mostrarMensaje;

console.log('✅ cancelar-cita.js cargado correctamente');
