o// ===== FILTRADO DE CITAS EN TIEMPO REAL =====
document.addEventListener('DOMContentLoaded', function() {
    const filtroEstado = document.getElementById('filtroEstado');
    
    if (filtroEstado) {
        filtroEstado.addEventListener('change', function() {
            filtrarCitas(this.value);
        });
    }
});

// ===== FUNCIÓN PARA FILTRAR CITAS =====
function filtrarCitas(estadoSeleccionado) {
    const todasLasCitas = document.querySelectorAll('.cita-card');
    
    todasLasCitas.forEach(function(cita) {
        const estadoCita = cita.getAttribute('data-estado');
        
        if (estadoSeleccionado === 'todas') {
            cita.style.display = '';
        } else {
            if (estadoCita === estadoSeleccionado) {
                cita.style.display = '';
            } else {
                cita.style.display = 'none';
            }
        }
    });
    
    // Verificar si hay citas visibles
    const citasVisibles = Array.from(todasLasCitas).filter(cita => cita.style.display !== 'none');
    const listaCitas = document.getElementById('listaCitas');
    const noResultadosMensaje = document.querySelector('.no-resultados-filtro');
    
    if (citasVisibles.length === 0) {
        if (!noResultadosMensaje) {
            const mensaje = document.createElement('p');
            mensaje.className = 'no-citas no-resultados-filtro';
            mensaje.textContent = `No hay citas con estado "${estadoSeleccionado}"`;
            listaCitas.appendChild(mensaje);
        }
    } else {
        if (noResultadosMensaje) {
            noResultadosMensaje.remove();
        }
    }
}

// ===== VERIFICAR SI SE PUEDE CANCELAR LA CITA =====
function puedeCancelar(fecha, hora) {
    const ahora = new Date();
    
    console.log('Fecha recibida:', fecha);
    console.log('Hora recibida:', hora);
    
    // Extraer solo la hora de inicio (antes del guion)
    // Ejemplo: "10:00am-11:00am" -> "10:00am"
    const horaInicio = hora.split('-')[0].trim();
    console.log('Hora de inicio:', horaInicio);
    
    // Separar fecha (YYYY-MM-DD)
    const [year, month, day] = fecha.split('-').map(Number);
    
    // Extraer hora y minutos del formato "10:00am" o "2:30pm"
    const horaMatch = horaInicio.match(/(\d+):(\d+)(am|pm)/i);
    
    if (!horaMatch) {
        console.error('Formato de hora inválido:', horaInicio);
        return false;
    }
    
    let hours = parseInt(horaMatch[1]);
    const minutes = parseInt(horaMatch[2]);
    const periodo = horaMatch[3].toLowerCase();
    
    // Convertir a formato 24 horas
    if (periodo === 'pm' && hours !== 12) {
        hours += 12;
    } else if (periodo === 'am' && hours === 12) {
        hours = 0;
    }
    
    console.log('Valores parseados:', { year, month, day, hours, minutes });
    
    // Crear fecha de la cita correctamente (month - 1 porque los meses van de 0-11)
    const fechaCita = new Date(year, month - 1, day, hours, minutes);
    
    console.log('Ahora:', ahora);
    console.log('Fecha cita:', fechaCita);
    
    const diferenciaMinutos = (fechaCita - ahora) / (1000 * 60);
    console.log('Diferencia en minutos:', diferenciaMinutos);
    
    return diferenciaMinutos > 30;
}


// ===== CARGAR CITAS DEL USUARIO =====
function cargarCitas() {
    const usuario = verificarSesion();
    const listaCitas = document.getElementById('listaCitas');
    const filtroEstado = document.getElementById('filtroEstado').value;
    const citas = JSON.parse(localStorage.getItem('citas')) || [];
    
    // Filtrar citas del estudiante
    let citasFiltradas = citas.filter(c => c.usuarioId === usuario.id);
    
    // Aplicar filtro de estado
    if (filtroEstado !== 'todas') {
        citasFiltradas = citasFiltradas.filter(c => c.estado === filtroEstado);
    }
    
    if (citasFiltradas.length === 0) {
        listaCitas.innerHTML = '<p class="no-citas">No tienes citas ' + (filtroEstado !== 'todas' ? filtroEstado.toLowerCase() + 's' : 'agendadas') + '</p>';
        return;
    }
    
    // Ordenar por fecha y hora (más recientes primero)
    citasFiltradas.sort((a, b) => {
        const fechaA = new Date(a.fecha + 'T' + a.hora.split('-')[0]);
        const fechaB = new Date(b.fecha + 'T' + b.hora.split('-')[0]);
        return fechaB - fechaA;
    });
    
    // Generar HTML
    let html = '';
    citasFiltradas.forEach(cita => {
        const fechaFormateada = formatearFecha(cita.fecha);
        const esPasada = new Date(cita.fecha + 'T' + cita.hora) < new Date();
        const claseAdicional = esPasada ? 'cita-pasada' : '';
        
        html += `
            <div class="cita-card-consulta ${claseAdicional}">
                <div class="cita-header-consulta">
                    <div class="cita-info-principal">
                        <h3>${cita.especialidad}</h3>
                        <p class="cita-doctor">👨‍⚕️ ${cita.doctorNombre}</p>
                    </div>
                    <span class="cita-estado ${cita.estado.toLowerCase()}">${cita.estado}</span>
                </div>
                
                <div class="cita-detalles-consulta">
                    <div class="cita-dato">
                        <span class="icono">📅</span>
                        <div>
                            <small>Fecha</small>
                            <strong>${fechaFormateada}</strong>
                        </div>
                    </div>
                    
                    <div class="cita-dato">
                        <span class="icono">🕐</span>
                        <div>
                            <small>Horario</small>
                            <strong>${cita.hora}</strong>
                        </div>
                    </div>
                    
                    <div class="cita-dato-motivo">
                        <span class="icono">📝</span>
                        <div>
                            <small>Motivo</small>
                            <p>${cita.motivo}</p>
                        </div>
                    </div>
                </div>
                
                ${cita.estado === 'Pendiente' && !esPasada ? `
                    <div class="cita-acciones">
                        <button class="btn-cancelar-consulta" onclick="cancelarCita(${cita.id})">
                            Cancelar Cita
                        </button>
                    </div>
                ` : ''}
            </div>
        `;
    });
    
    listaCitas.innerHTML = html;
}

// ===== CANCELAR CITA =====
function cancelarCita(citaId) {
    console.log('ID de cita a cancelar:', citaId);
    
    let citas = JSON.parse(localStorage.getItem('citas')) || [];
    console.log('Todas las citas:', citas);
    
    // Convertir citaId a string para la comparación
    const citaIndex = citas.findIndex(c => c.id === String(citaId));
    
    if (citaIndex === -1) {
        alert('No se encontró la cita');
        console.log('Cita no encontrada con ID:', citaId);
        return;
    }
    
    const cita = citas[citaIndex];

    // Verificar restricción de tiempo PRIMERO
    if (!puedeCancelar(cita.fecha, cita.hora)) {
        alert('⚠️ No se puede cancelar la cita.\n\nLas citas solo pueden cancelarse hasta 30 minutos antes del horario agendado.');
        return;
    }
    
    // Si pasa la verificación, entonces pedir confirmación
    if (!confirm('¿Estás seguro de que deseas cancelar esta cita?')) {
        return;
    }
    
    citas[citaIndex].estado = 'Cancelada';
    localStorage.setItem('citas', JSON.stringify(citas));
    cargarCitas();
    
    alert('✓ Cita cancelada exitosamente');
}

// ===== UTILIDADES =====
function formatearFecha(fechaStr) {
    const opciones = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const fecha = new Date(fechaStr + 'T00:00:00');
    return fecha.toLocaleDateString('es-EC', opciones);
}
