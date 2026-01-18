/**
 * Script para gestionar doctores - Admin Panel
 * Maneja interacciones del DOM y validaciones del lado del cliente
 */

// Función para cerrar modales al hacer clic en el botón X o en el overlay
function cerrarModal() {
    var modales = document.querySelectorAll('.modal-overlay');
    for (var i = 0; i < modales.length; i++) {
        modales[i].classList.remove('active');
    }
}

// Función para validar el formulario de nuevo doctor
function validarFormularioNuevoDoctor(event) {
    var cedula = document.querySelector('input[name="cedula"]');
    var nombre = document.querySelector('input[name="nombre"]');
    var apellido = document.querySelector('input[name="apellido"]');
    var email = document.querySelector('input[name="email"]');
    var password = document.querySelector('input[name="password"]');
    
    // Validar cédula (10 dígitos)
    if (cedula && cedula.value.length !== 10) {
        alert('La cédula debe tener exactamente 10 dígitos');
        cedula.focus();
        event.preventDefault();
        return false;
    }
    
    // Validar que el nombre no esté vacío
    if (nombre && nombre.value.trim() === '') {
        alert('El nombre es requerido');
        nombre.focus();
        event.preventDefault();
        return false;
    }
    
    // Validar que el apellido no esté vacío
    if (apellido && apellido.value.trim() === '') {
        alert('El apellido es requerido');
        apellido.focus();
        event.preventDefault();
        return false;
    }
    
    // Validar email
    if (email && email.value.indexOf('@') === -1) {
        alert('Ingrese un email válido');
        email.focus();
        event.preventDefault();
        return false;
    }
    
    // Validar contraseña (mínimo 6 caracteres)
    if (password && password.value.length < 6) {
        alert('La contraseña debe tener al menos 6 caracteres');
        password.focus();
        event.preventDefault();
        return false;
    }
    
    return true;
}

// Función para confirmar desactivación/activación
function confirmarCambioEstado(event, activo) {
    var mensaje = activo 
        ? '¿Está seguro que desea desactivar este doctor?' 
        : '¿Está seguro que desea activar este doctor?';
    
    if (!confirm(mensaje)) {
        event.preventDefault();
        return false;
    }
    return true;
}

// Función para auto-cerrar alertas después de 5 segundos
function autoCerrarAlertas() {
    var alertas = document.querySelectorAll('.alert.show');
    
    for (var i = 0; i < alertas.length; i++) {
        (function(alerta) {
            setTimeout(function() {
                alerta.classList.remove('show');
            }, 5000);
        })(alertas[i]);
    }
}

// Función para inicializar el script cuando el DOM esté listo
function inicializar() {
    // Auto-cerrar alertas
    autoCerrarAlertas();
    
    // Agregar event listeners a botones de cerrar modal
    var botonesCloseModal = document.querySelectorAll('.btn-close, .modal-overlay');
    for (var i = 0; i < botonesCloseModal.length; i++) {
        botonesCloseModal[i].addEventListener('click', function(e) {
            if (e.target === this) {
                cerrarModal();
            }
        });
    }
    
    // Validar formularios al enviar
    var formNuevoDoctor = document.querySelector('form[action*="solicitarNuevoDoctor"]');
    if (formNuevoDoctor) {
        formNuevoDoctor.addEventListener('submit', validarFormularioNuevoDoctor);
    }
    
    // Agregar confirmación a botones de cambio de estado
    var botonesEstado = document.querySelectorAll('button[name="accion"][value="confirmarDesactivacion"]');
    for (var i = 0; i < botonesEstado.length; i++) {
        var boton = botonesEstado[i];
        var form = boton.closest('form');
        var doctorActivo = boton.textContent.indexOf('Desactivar') !== -1;
        
        (function(btn, esActivo) {
            btn.addEventListener('click', function(e) {
                return confirmarCambioEstado(e, esActivo);
            });
        })(boton, doctorActivo);
    }
}

// Ejecutar cuando el documento esté listo
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', inicializar);
} else {
    inicializar();
}