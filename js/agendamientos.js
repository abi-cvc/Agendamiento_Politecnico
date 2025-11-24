// ===== PROTEGER PÁGINA Y CARGAR DATOS =====
document.addEventListener('DOMContentLoaded', function() {
    // Verificar sesión - si no hay, guardar página actual y redirigir
    const usuario = verificarSesion();
    
    if (!usuario) {
        sessionStorage.setItem('paginaAnterior', 'agendamientos.html');
        window.location.href = 'login.html';
        return;
    }

    // Mostrar nombre del usuario
    document.getElementById('userName').textContent = usuario.nombre;
    
    // Cambiar botón de login por logout
    actualizarBotonAuth(usuario);
    
    // Si es doctor, ocultar el formulario de agendar
    if (usuario.rol === 'doctor') {
        const formularioSection = document.querySelector('.agendamiento-form');
        if (formularioSection) {
            formularioSection.style.display = 'none';
        }
        // Ajustar el layout para que las citas ocupen todo el ancho
        const container = document.querySelector('.agendamiento-container');
        if (container) {
            container.style.gridTemplateColumns = '1fr';
        }
    } else {
        // Configurar fecha mínima (hoy) solo para estudiantes
        const fechaInput = document.getElementById('fecha');
        const hoy = new Date().toISOString().split('T')[0];
        fechaInput.min = hoy;
        
        // Detectar y pre-seleccionar especialidad desde URL
        preseleccionarEspecialidad();
        
        // Manejar envío del formulario
        document.getElementById('agendamientoForm').addEventListener('submit', agendarCita);
    }
    
    // Cargar citas existentes (para todos)
    cargarCitas();
});

// ===== PRE-SELECCIONAR ESPECIALIDAD DESDE URL =====
function preseleccionarEspecialidad() {
    const urlParams = new URLSearchParams(window.location.search);
    const especialidad = urlParams.get('especialidad');
    
    if (especialidad) {
        const selectEspecialidad = document.getElementById('especialidad');
        const option = selectEspecialidad.querySelector(`option[value="${especialidad}"]`);
        
        if (option) {
            selectEspecialidad.value = especialidad;
            // Agregar efecto visual temporal
            selectEspecialidad.style.background = 'rgba(60, 141, 188, 0.1)';
            setTimeout(() => {
                selectEspecialidad.style.background = '';
            }, 2000);
        }
    }
}

// ===== ACTUALIZAR BOTÓN DE AUTENTICACIÓN =====
function actualizarBotonAuth(usuario) {
    const authButton = document.getElementById('authButton');
    if (usuario) {
        authButton.innerHTML = `<a href="#" onclick="logout(); return false;">Cerrar Sesión</a>`;
    }
}

// ===== AGENDAR CITA =====
function agendarCita(e) {
    e.preventDefault();
    
    const usuario = verificarSesion();
    const messageDiv = document.getElementById('formMessage');
    
    const especialidad = document.getElementById('especialidad');
    const fecha = document.getElementById('fecha').value;
    const hora = document.getElementById('hora').value;
    const motivo = document.getElementById('motivo').value;
    
    // Crear objeto de cita
    const nuevaCita = {
        id: Date.now(),
        usuarioId: usuario.id,
        usuarioNombre: usuario.nombre,
        especialidad: especialidad.options[especialidad.selectedIndex].text,
        especialidadValue: especialidad.value,
        fecha: fecha,
        hora: hora,
        motivo: motivo,
        estado: 'Confirmada',
        fechaCreacion: new Date().toISOString()
    };
    
    // Guardar en sessionStorage
    let citas = JSON.parse(sessionStorage.getItem('citas')) || [];
    citas.push(nuevaCita);
    sessionStorage.setItem('citas', JSON.stringify(citas));
    
    // Mostrar mensaje de éxito
    messageDiv.textContent = '¡Cita agendada exitosamente!';
    messageDiv.className = 'form-message success show';
    
    // Limpiar formulario
    document.getElementById('agendamientoForm').reset();
    
    // Recargar lista de citas
    cargarCitas();
    
    // Ocultar mensaje después de 3 segundos
    setTimeout(() => {
        messageDiv.className = 'form-message';
    }, 3000);
}

// ===== CARGAR CITAS DEL USUARIO =====
function cargarCitas() {
    const usuario = verificarSesion();
    const listaCitas = document.getElementById('listaCitas');
    const citas = JSON.parse(sessionStorage.getItem('citas')) || [];
    
    let citasFiltradas;
    
    // Si es doctor, ver solo citas de su especialidad
    if (usuario.rol === 'doctor') {
        citasFiltradas = citas.filter(c => c.especialidadValue === usuario.especialidad);
    } else {
        // Si es estudiante, ver solo sus citas
        citasFiltradas = citas.filter(c => c.usuarioId === usuario.id);
    }
    
    if (citasFiltradas.length === 0) {
        if (usuario.rol === 'doctor') {
            listaCitas.innerHTML = '<p class="no-citas">No hay citas agendadas para tu especialidad</p>';
        } else {
            listaCitas.innerHTML = '<p class="no-citas">No tienes citas agendadas</p>';
        }
        return;
    }
    
    // Ordenar por fecha
    citasFiltradas.sort((a, b) => new Date(a.fecha) - new Date(b.fecha));
    
    // Generar HTML
    let html = '';
    citasFiltradas.forEach(cita => {
        const fechaFormateada = formatearFecha(cita.fecha);
        
        // Mostrar nombre del paciente si es doctor
        const nombrePaciente = usuario.rol === 'doctor' ? `<p><strong>👤 Paciente:</strong> ${cita.usuarioNombre}</p>` : '';
        
        html += `
            <div class="cita-card">
                <div class="cita-header">
                    <span class="cita-especialidad">${cita.especialidad}</span>
                    <span class="cita-estado ${cita.estado.toLowerCase()}">${cita.estado}</span>
                </div>
                <div class="cita-detalles">
                    ${nombrePaciente}
                    <p><strong>📅 Fecha:</strong> ${fechaFormateada}</p>
                    <p><strong>🕐 Hora:</strong> ${formatearHora(cita.hora)}</p>
                    <p><strong>📝 Motivo:</strong> ${cita.motivo}</p>
                </div>
                <button class="btn-cancelar" onclick="cancelarCita(${cita.id})">
                    Cancelar Cita
                </button>
            </div>
        `;
    });
    
    listaCitas.innerHTML = html;
}

// ===== CANCELAR CITA =====
function cancelarCita(citaId) {
    if (!confirm('¿Estás seguro de que deseas cancelar esta cita?')) {
        return;
    }
    
    let citas = JSON.parse(sessionStorage.getItem('citas')) || [];
    citas = citas.filter(c => c.id !== citaId);
    sessionStorage.setItem('citas', JSON.stringify(citas));
    
    cargarCitas();
}

// ===== UTILIDADES =====
function formatearFecha(fechaStr) {
    const opciones = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const fecha = new Date(fechaStr + 'T00:00:00');
    return fecha.toLocaleDateString('es-EC', opciones);
}

function formatearHora(hora) {
    const [h, m] = hora.split(':');
    const hour = parseInt(h);
    const ampm = hour >= 12 ? 'PM' : 'AM';
    const hour12 = hour % 12 || 12;
    return `${hour12}:${m} ${ampm}`;
}