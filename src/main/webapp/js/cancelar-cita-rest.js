/**
 * cancelar-cita-rest.js
 * Script para manejar la cancelación de citas mediante API REST
 * Compatible con el sistema existente - NO requiere cambios en las vistas JSP
 */

/**
 * Cancela una cita usando la API REST
 * @param {number} idCita - ID de la cita a cancelar
 * @param {string} from - Origen: 'atender', 'consultar' o 'calendario'
 */
async function cancelarCita(idCita, from) {
    console.log('🚫 Iniciando cancelación de cita:', idCita, 'desde:', from);
    
    try {
        // ===== PASO 1-2: Obtener confirmación de la API =====
        const confirmacionData = await obtenerDatosConfirmacion(idCita);
        
        if (!confirmacionData) {
            return;
        }
        
        // Mostrar confirmación al usuario con los datos obtenidos
        const mensaje = construirMensajeConfirmacion(confirmacionData);
        const confirmacion = confirm(mensaje);
        
        if (!confirmacion) {
            console.log('❌ Cancelación abortada por el usuario');
            return;
        }
        
        // ===== PASO 3-6: Confirmar cancelación vía API =====
        console.log('✅ Usuario confirmó la cancelación');
        await ejecutarCancelacion(idCita, from);
        
    } catch (error) {
        console.error('❌ Error en cancelación:', error);
        mostrarMensaje('❌ Error al cancelar la cita: ' + error.message, 'error');
    }
}

/**
 * PASO 1-2: Obtiene los datos de confirmación desde la API REST
 */
async function obtenerDatosConfirmacion(idCita) {
    try {
        console.log('📡 Solicitando datos de confirmación...');
        
        const response = await fetch(
            `${getContextPath()}/rest/api/citas/${idCita}/cancelar/confirmacion`,
            {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            }
        );
        
        const data = await response.json();
        
        if (!response.ok) {
            throw new Error(data.error || 'Error al obtener confirmación');
        }
        
        if (!data.success) {
            mostrarMensaje('❌ ' + data.error, 'error');
            return null;
        }
        
        console.log('✅ Datos de confirmación obtenidos:', data.data);
        return data.data;
        
    } catch (error) {
        console.error('❌ Error al obtener confirmación:', error);
        mostrarMensaje('❌ Error de conexión: ' + error.message, 'error');
        return null;
    }
}

/**
 * Construye el mensaje de confirmación con los datos de la cita
 */
function construirMensajeConfirmacion(datos) {
    let mensaje = '¿Está seguro que desea cancelar esta cita?\n\n';
    mensaje += `📅 Fecha: ${formatearFecha(datos.fecha)}\n`;
    mensaje += `🕐 Hora: ${formatearHora(datos.hora)}\n`;
    
    if (datos.doctor) {
        mensaje += `👨‍⚕️ Doctor: ${datos.doctor}\n`;
    }
    
    if (datos.especialidad) {
        mensaje += `🏥 Especialidad: ${datos.especialidad}\n`;
    }
    
    mensaje += '\n⚠️ Esta acción no se puede deshacer.';
    mensaje += '\n✅ El horario quedará disponible nuevamente.';
    
    return mensaje;
}

/**
 * PASO 3-6: Ejecuta la cancelación de la cita vía API REST
 */
async function ejecutarCancelacion(idCita, from) {
    try {
        console.log('📡 Ejecutando cancelación...');
        
        const response = await fetch(
            `${getContextPath()}/rest/api/citas/${idCita}/cancelar`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            }
        );
        
        const data = await response.json();
        
        if (!response.ok) {
            throw new Error(data.error || 'Error al cancelar la cita');
        }
        
        if (!data.success) {
            mostrarMensaje('❌ ' + data.error, 'error');
            return;
        }
        
        console.log('✅ Cita cancelada exitosamente:', data.data);
        
        // Mostrar mensaje de éxito
        const resultado = data.data;
        let mensajeExito = '✅ ' + resultado.mensaje;
        
        if (resultado.horarioLiberado) {
            mensajeExito += '\n✅ Horario liberado correctamente';
        }
        
        mostrarMensaje(mensajeExito, 'success');
        
        // PASO 7: Actualizar la vista después de 2 segundos
        setTimeout(() => {
            actualizarVista(from);
        }, 2000);
        
    } catch (error) {
        console.error('❌ Error al ejecutar cancelación:', error);
        mostrarMensaje('❌ Error al cancelar: ' + error.message, 'error');
    }
}

/**
 * PASO 7: Actualiza la vista según el origen
 */
function actualizarVista(from) {
    console.log('🔄 Actualizando vista desde:', from);
    
    // Recargar la página actual para reflejar los cambios
    window.location.reload();
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
 * Formatea una fecha en formato ISO a formato legible
 */
function formatearFecha(fecha) {
    if (!fecha) return 'N/A';
    
    try {
        const date = new Date(fecha);
        const opciones = { 
            year: 'numeric', 
            month: 'long', 
            day: 'numeric',
            weekday: 'long'
        };
        return date.toLocaleDateString('es-ES', opciones);
    } catch (e) {
        return fecha;
    }
}

/**
 * Formatea una hora en formato ISO a formato legible
 */
function formatearHora(hora) {
    if (!hora) return 'N/A';
    
    try {
        // Si ya viene en formato HH:MM:SS
        if (typeof hora === 'string' && hora.includes(':')) {
            const [hh, mm] = hora.split(':');
            return `${hh}:${mm}`;
        }
        return hora;
    } catch (e) {
        return hora;
    }
}

/**
 * Muestra un mensaje temporal en la parte superior de la página
 */
function mostrarMensaje(mensaje, tipo) {
    // Crear elemento de mensaje
    const div = document.createElement('div');
    div.className = `alert alert-${tipo} mensaje-temporal`;
    
    // Permitir saltos de línea en el mensaje
    div.innerHTML = mensaje.replace(/\n/g, '<br>');
    
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
        max-width: 500px;
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

// Exportar funciones globales (mantener compatibilidad con código existente)
window.cancelarCita = cancelarCita;
window.mostrarMensaje = mostrarMensaje;

console.log('✅ cancelar-cita-rest.js cargado correctamente (versión API REST)');
