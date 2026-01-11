// ===== INICIALIZACIÓN =====
document.addEventListener('DOMContentLoaded', function() {
    // Configurar sistema de calificación
    configurarCalificacion();
    
    // Configurar filtrado en cascada Especialidad → Doctor
    configurarFiltradoCascada();
    
    // Validar formulario antes de enviar
    configurarValidacionFormulario();
    
    // Auto-ocultar mensajes de alerta
    autoOcultarAlertas();
});

// ===== FILTRADO EN CASCADA: ESPECIALIDAD → DOCTOR =====
function configurarFiltradoCascada() {
    const especialidadSelect = document.getElementById('especialidadResena');
    const doctorSelect = document.getElementById('idDoctor');
    
    if (!especialidadSelect || !doctorSelect) {
        console.error('No se encontraron los selectores necesarios');
        return;
    }
    
    console.log('Filtrado en cascada configurado correctamente');
    
    especialidadSelect.addEventListener('change', function() {
        const especialidadId = parseInt(this.value);
        
        console.log('Especialidad seleccionada:', especialidadId);
        console.log('Doctores disponibles:', doctoresDisponibles);
        
        // Limpiar opciones del select de doctores
        doctorSelect.innerHTML = '<option value="">Selecciona un doctor</option>';
        
        if (!especialidadId || isNaN(especialidadId)) {
            doctorSelect.disabled = true;
            console.log('No hay especialidad válida seleccionada');
            return;
        }
        
        // Filtrar doctores por especialidad seleccionada
        const doctoresFiltrados = doctoresDisponibles.filter(
            doc => doc.especialidadId === especialidadId
        );
        
        console.log('Doctores filtrados:', doctoresFiltrados);
        
        if (doctoresFiltrados.length === 0) {
            doctorSelect.innerHTML = '<option value="">No hay doctores disponibles en esta especialidad</option>';
            doctorSelect.disabled = true;
            return;
        }
        
        // Agregar doctores filtrados al select
        doctoresFiltrados.forEach(doctor => {
            const option = document.createElement('option');
            option.value = doctor.id;
            option.textContent = `${doctor.especialidadIcono} Dr. ${doctor.nombre}`;
            doctorSelect.appendChild(option);
        });
        
        doctorSelect.disabled = false;
        console.log('Select de doctores habilitado con', doctoresFiltrados.length, 'opciones');
    });
}

// ===== SISTEMA DE CALIFICACIÓN CON ESTRELLAS =====
function configurarCalificacion() {
    const stars = document.querySelectorAll('.star');
    const calificacionInput = document.getElementById('calificacion');
    
    if (stars.length === 0 || !calificacionInput) return;
    
    stars.forEach(star => {
        // Click para seleccionar calificación
        star.addEventListener('click', function() {
            const value = this.getAttribute('data-value');
            calificacionInput.value = value;
            
            // Actualizar estrellas visuales
            actualizarEstrellas(stars, value);
        });
        
        // Hover effect
        star.addEventListener('mouseenter', function() {
            const value = this.getAttribute('data-value');
            stars.forEach(s => {
                const starValue = s.getAttribute('data-value');
                if (starValue <= value) {
                    s.classList.add('hover');
                } else {
                    s.classList.remove('hover');
                }
            });
        });
    });
    
    // Limpiar hover al salir del contenedor
    const ratingInput = document.getElementById('ratingInput');
    if (ratingInput) {
        ratingInput.addEventListener('mouseleave', function() {
            stars.forEach(s => s.classList.remove('hover'));
        });
    }
}

function actualizarEstrellas(stars, value) {
    stars.forEach(s => {
        const starValue = s.getAttribute('data-value');
        if (starValue <= value) {
            s.classList.add('active');
        } else {
            s.classList.remove('active');
        }
    });
}

// ===== VALIDACIÓN DEL FORMULARIO =====
function configurarValidacionFormulario() {
    const form = document.getElementById('resenaForm');
    if (!form) return;
    
    form.addEventListener('submit', function(e) {
        const especialidad = document.getElementById('especialidadResena').value;
        const doctor = document.getElementById('idDoctor').value;
        const calificacion = document.getElementById('calificacion').value;
        const comentario = document.getElementById('comentario').value;
        
        // Validar especialidad
        if (!especialidad) {
            e.preventDefault();
            mostrarMensaje('Por favor selecciona una especialidad', 'error');
            return false;
        }
        
        // Validar doctor
        if (!doctor) {
            e.preventDefault();
            mostrarMensaje('Por favor selecciona un doctor', 'error');
            return false;
        }
        
        // Validar calificación
        if (!calificacion) {
            e.preventDefault();
            mostrarMensaje('Por favor selecciona una calificación', 'error');
            return false;
        }
        
        // Validar comentario
        if (comentario.trim().length < 20) {
            e.preventDefault();
            mostrarMensaje('El comentario debe tener al menos 20 caracteres', 'error');
            return false;
        }
        
        if (comentario.trim().length > 500) {
            e.preventDefault();
            mostrarMensaje('El comentario no puede exceder 500 caracteres', 'error');
            return false;
        }
        
        // Todo válido, continuar con el submit
        return true;
    });
}

// ===== MOSTRAR MENSAJES =====
function mostrarMensaje(mensaje, tipo) {
    const messageDiv = document.getElementById('resenaMessage');
    if (!messageDiv) return;
    
    messageDiv.textContent = mensaje;
    messageDiv.className = `form-message ${tipo} show`;
    
    // Ocultar después de 5 segundos
    setTimeout(() => {
        messageDiv.className = 'form-message';
    }, 5000);
}

// ===== AUTO-OCULTAR ALERTAS =====
function autoOcultarAlertas() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.3s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 300);
        }, 5000);
    });
}

// ===== UTILIDADES PARA FORMATEO =====
function generarEstrellas(calificacion) {
    let estrellas = '';
    for (let i = 1; i <= 5; i++) {
        if (i <= calificacion) {
            estrellas += '<span class="star-display active">★</span>';
        } else {
            estrellas += '<span class="star-display">★</span>';
        }
    }
    return estrellas;
}

function formatearFecha(fechaStr) {
    const fecha = new Date(fechaStr);
    const opciones = { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric' 
    };
    return fecha.toLocaleDateString('es-EC', opciones);
}

function formatearFechaRelativa(fechaStr) {
    const fecha = new Date(fechaStr);
    const ahora = new Date();
    const diff = ahora - fecha;
    
    // Menos de 1 minuto
    if (diff < 60000) {
        return 'Hace un momento';
    }
    
    // Menos de 1 hora
    if (diff < 3600000) {
        const minutos = Math.floor(diff / 60000);
        return `Hace ${minutos} minuto${minutos > 1 ? 's' : ''}`;
    }
    
    // Menos de 1 día
    if (diff < 86400000) {
        const horas = Math.floor(diff / 3600000);
        return `Hace ${horas} hora${horas > 1 ? 's' : ''}`;
    }
    
    // Menos de 1 semana
    if (diff < 604800000) {
        const dias = Math.floor(diff / 86400000);
        return `Hace ${dias} día${dias > 1 ? 's' : ''}`;
    }
    
    // Formato completo
    return formatearFecha(fechaStr);
}