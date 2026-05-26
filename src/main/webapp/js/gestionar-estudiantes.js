/**
 * Script para gestionar estudiantes - Admin Panel
 * Maneja interacciones del DOM y validaciones del lado del cliente
 */

// Función para cerrar modales al hacer clic en el botón X o en el overlay
function cerrarModal() {
    var modales = document.querySelectorAll('.modal-overlay');
    for (var i = 0; i < modales.length; i++) {
        modales[i].classList.remove('active');
    }
}

// Función para validar el formulario de nuevo estudiante
function validarFormularioNuevoEstudiante(event) {
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
    
    // Validar contraseña (mínimo 6 caracteres) - solo para nuevo estudiante
    if (password && password.value.length > 0 && password.value.length < 6) {
        alert('La contraseña debe tener al menos 6 caracteres');
        password.focus();
        event.preventDefault();
        return false;
    }
    
    return true;
}

// Función para validar el formulario de editar estudiante
function validarFormularioEditarEstudiante(event) {
    var nombre = document.querySelector('input[name="nombre"]');
    var apellido = document.querySelector('input[name="apellido"]');
    var email = document.querySelector('input[name="email"]');
    var password = document.querySelector('input[name="password"]');
    
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
    
    // Validar contraseña solo si se ingresó alguna
    if (password && password.value.length > 0 && password.value.length < 6) {
        alert('La contraseña debe tener al menos 6 caracteres');
        password.focus();
        event.preventDefault();
        return false;
    }
    
    return true;
}

// Función para confirmar desactivación/activación
function confirmarCambioEstado(event, activo) {
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

// Función de logout (placeholder - implementar según tu sistema de autenticación)
function logout() {
    if (confirm('¿Está seguro que desea cerrar sesión?')) {
        // Aquí irá la lógica de logout cuando la implementes
        window.location.href = 'index.jsp';
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
    
    // Validar formulario de nuevo estudiante
    var formNuevoEstudiante = document.querySelector('form[action*="solicitarNuevoEstudiante"]');
    if (formNuevoEstudiante) {
        formNuevoEstudiante.addEventListener('submit', validarFormularioNuevoEstudiante);
    }
    
    // Validar formulario de editar estudiante
    var formEditarEstudiante = document.querySelector('form input[name="accion"][value="actualizar"]');
    if (formEditarEstudiante) {
        var formEditar = formEditarEstudiante.closest('form');
        formEditar.addEventListener('submit', validarFormularioEditarEstudiante);
    }
    
    // Agregar confirmación a botones de cambio de estado
    var botonesEstado = document.querySelectorAll('button[type="submit"]');
    for (var i = 0; i < botonesEstado.length; i++) {
        var boton = botonesEstado[i];
        var form = boton.closest('form');
        
        if (form) {
            var accionInput = form.querySelector('input[name="accion"]');
            if (accionInput && accionInput.value === 'confirmarDesactivacion') {
                var estudianteActivo = boton.textContent.indexOf('Desactivar') !== -1;
                
                (function(btn, esActivo) {
                    btn.addEventListener('click', function(e) {
                        return confirmarCambioEstado(e, esActivo);
                    });
                })(boton, estudianteActivo);
            }
        }
    }
}

// Ejecutar cuando el documento esté listo
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', inicializar);
} else {
    inicializar();
}