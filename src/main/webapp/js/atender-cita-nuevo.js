/**
 * atender-cita.js
 * Script para manejar la atención de citas médicas
 * Según diagrama de robustez
 */

/**
 * Abre el modal para atender una cita
 * @param {number} idCita - ID de la cita a atender
 */
function atenderCita(idCita) {
    console.log('🩺 Iniciando atención de cita:', idCita);
    
    // Crear modal dinámicamente
    const modal = crearModalAtenderCita(idCita);
    document.body.appendChild(modal);
    
    // Mostrar modal
    setTimeout(() => {
        modal.style.display = 'flex';
    }, 10);
}

/**
 * Crea el modal para ingresar observaciones
 */
function crearModalAtenderCita(idCita) {
    const modal = document.createElement('div');
    modal.className = 'modal-atender-cita';
    modal.id = 'modalAtenderCita';
    
    const contextPath = getContextPath();
    
    modal.innerHTML = `
        <div class="modal-content-atender">
            <div class="modal-header-atender">
                <h2>Atender Cita</h2>
                <button class="modal-close-atender" onclick="cerrarModalAtender()">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <line x1="18" y1="6" x2="6" y2="18"></line>
                        <line x1="6" y1="6" x2="18" y2="18"></line>
                    </svg>
                </button>
            </div>
            
            <div class="modal-body-atender">
                <form id="formAtenderCita" method="POST" action="${contextPath}/AtenderCitaController">
                    <input type="hidden" name="idCita" value="${idCita}">
                    <input type="hidden" name="confirmar" value="true">
                    
                    <div class="form-group">
                        <label for="observacion">
                            <strong>Observaciones de la Consulta:</strong>
                            <span class="text-muted">(Obligatorio)</span>
                        </label>
                        <textarea 
                            id="observacion" 
                            name="observacion" 
                            class="form-control" 
                            rows="8"
                            placeholder="Ingrese las observaciones de la consulta médica, diagnóstico, tratamiento, recomendaciones, etc."
                            required
                        ></textarea>
                        <small class="form-text">
                            Escriba un resumen detallado de la atención médica proporcionada.
                        </small>
                    </div>
                    
                    <div class="modal-footer-atender">
                        <button type="button" class="btn-secondary" onclick="cerrarModalAtender()">
                            Cancelar
                        </button>
                        <button type="submit" class="btn-primary">
                            ✅ Confirmar y Completar Atención
                        </button>
                    </div>
                </form>
            </div>
        </div>
    `;
    
    // Agregar estilos al modal
    agregarEstilosModal();
    
    // Cerrar al hacer click fuera del modal
    modal.addEventListener('click', function(e) {
        if (e.target === modal) {
            cerrarModalAtender();
        }
    });
    
    return modal;
}

/**
 * Cierra el modal de atender cita
 */
function cerrarModalAtender() {
    const modal = document.getElementById('modalAtenderCita');
    if (modal) {
        modal.style.opacity = '0';
        setTimeout(() => {
            modal.remove();
        }, 300);
    }
}

/**
 * Agrega estilos CSS al modal
 */
function agregarEstilosModal() {
    if (document.getElementById('estilosModalAtender')) {
        return; // Ya están agregados
    }
    
    const style = document.createElement('style');
    style.id = 'estilosModalAtender';
    style.textContent = `
        .modal-atender-cita {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.7);
            z-index: 10000;
            align-items: center;
            justify-content: center;
            opacity: 0;
            transition: opacity 0.3s ease;
        }
        
        .modal-atender-cita[style*="display: flex"] {
            opacity: 1;
        }
        
        .modal-content-atender {
            background: white;
            border-radius: 12px;
            width: 90%;
            max-width: 600px;
            max-height: 90vh;
            overflow-y: auto;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            animation: slideIn 0.3s ease-out;
        }
        
        @keyframes slideIn {
            from {
                transform: translateY(-30px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }
        
        .modal-header-atender {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 1.5rem 2rem;
            border-bottom: 2px solid #e5e7eb;
            background: linear-gradient(135deg, #001f3f 0%, #3c8dbc 100%);
            border-radius: 12px 12px 0 0;
        }
        
        .modal-header-atender h2 {
            margin: 0;
            color: white;
            font-size: 1.5rem;
        }
        
        .modal-close-atender {
            background: none;
            border: none;
            cursor: pointer;
            padding: 0.5rem;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: transform 0.2s ease;
        }
        
        .modal-close-atender:hover {
            transform: rotate(90deg);
        }
        
        .modal-close-atender svg {
            stroke: white;
        }
        
        .modal-body-atender {
            padding: 2rem;
        }
        
        .form-group {
            margin-bottom: 1.5rem;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            color: #001f3f;
            font-size: 1rem;
        }
        
        .text-muted {
            color: #6b7280;
            font-weight: normal;
            font-size: 0.9rem;
        }
        
        .form-control {
            width: 100%;
            padding: 0.75rem;
            border: 2px solid #d1d5db;
            border-radius: 8px;
            font-size: 1rem;
            font-family: inherit;
            resize: vertical;
            transition: border-color 0.2s ease;
        }
        
        .form-control:focus {
            outline: none;
            border-color: #3c8dbc;
            box-shadow: 0 0 0 4px rgba(60, 141, 188, 0.1);
        }
        
        .form-text {
            display: block;
            margin-top: 0.5rem;
            color: #6b7280;
            font-size: 0.85rem;
        }
        
        .modal-footer-atender {
            display: flex;
            gap: 1rem;
            justify-content: flex-end;
            padding-top: 1.5rem;
            border-top: 2px solid #e5e7eb;
        }
        
        .btn-primary, .btn-secondary {
            padding: 0.75rem 1.5rem;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s ease;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #3c8dbc 0%, #001f3f 100%);
            color: white;
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(60, 141, 188, 0.4);
        }
        
        .btn-secondary {
            background: #e5e7eb;
            color: #374151;
        }
        
        .btn-secondary:hover {
            background: #d1d5db;
        }
    `;
    
    document.head.appendChild(style);
}

/**
 * Obtiene el context path de la aplicación
 */
function getContextPath() {
    const path = window.location.pathname;
    const contextPath = path.substring(0, path.indexOf('/', 1));
    return contextPath || '';
}

/**
 * Muestra mensajes de éxito o error
 */
document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    
    // Mensaje de éxito
    if (urlParams.get('success') === 'atendida') {
        mostrarMensajeAtender('✅ Cita atendida exitosamente', 'success');
        window.history.replaceState({}, document.title, window.location.pathname);
    }
    
    // Mensaje de error
    const errorMsg = urlParams.get('error');
    if (errorMsg) {
        mostrarMensajeAtender('❌ Error: ' + decodeURIComponent(errorMsg), 'error');
        window.history.replaceState({}, document.title, window.location.pathname);
    }
});

/**
 * Muestra un mensaje temporal
 */
function mostrarMensajeAtender(mensaje, tipo) {
    const div = document.createElement('div');
    div.className = `mensaje-temporal-atender mensaje-${tipo}`;
    div.textContent = mensaje;
    div.style.cssText = `
        position: fixed;
        top: 100px;
        left: 50%;
        transform: translateX(-50%);
        z-index: 99999;
        padding: 1rem 2rem;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.2);
        animation: slideDown 0.3s ease-out;
        min-width: 300px;
        text-align: center;
        font-weight: 600;
        font-size: 1rem;
    `;
    
    if (tipo === 'success') {
        div.style.background = '#d1fae5';
        div.style.color = '#065f46';
        div.style.border = '2px solid #10b981';
    } else {
        div.style.background = '#fee2e2';
        div.style.color = '#dc2626';
        div.style.border = '2px solid #ef4444';
    }
    
    document.body.appendChild(div);
    
    setTimeout(() => {
        div.style.animation = 'slideUp 0.3s ease-out';
        div.style.opacity = '0';
        setTimeout(() => div.remove(), 300);
    }, 5000);
}

// Exportar funciones globales
window.atenderCita = atenderCita;
window.cerrarModalAtender = cerrarModalAtender;

console.log('✅ atender-cita.js cargado correctamente');
