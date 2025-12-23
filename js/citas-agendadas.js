// Variables globales
let todasLasCitas = [];
let citasDelDoctor = [];

// Cargar citas cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    // Proteger la página - solo doctores pueden acceder
    const usuario = protegerPagina(['doctor']);
    
    if (!usuario) return;
    
    cargarCitasDelDoctor();
    configurarFiltros();
});

// Cargar las citas del doctor actual
function cargarCitasDelDoctor() {
    const usuario = JSON.parse(sessionStorage.getItem('usuarioActual'));
    
    if (!usuario || usuario.rol !== 'doctor') {
        alert('Acceso no autorizado');
        window.location.href = 'index.html';
        return;
    }

    // Obtener todas las citas del sessionStorage
    todasLasCitas = JSON.parse(sessionStorage.getItem('citas')) || [];
    
    console.log('=== DEBUG CARGAR CITAS DEL DOCTOR ===');
    console.log('Usuario doctor:', usuario);
    console.log('Usuario doctor ID:', usuario.id, 'tipo:', typeof usuario.id);
    console.log('Todas las citas:', todasLasCitas);
    
    // Filtrar solo las citas de este doctor
    citasDelDoctor = todasLasCitas.filter(cita => {
        console.log('Comparando - Cita doctorId:', cita.doctorId, 'tipo:', typeof cita.doctorId, 'vs Usuario ID:', usuario.id, 'tipo:', typeof usuario.id);
        console.log('¿Son iguales?', cita.doctorId === usuario.id);
        return cita.doctorId === usuario.id;
    });
    
    console.log('Citas del doctor filtradas:', citasDelDoctor);
    
    // Ordenar por fecha y hora (más recientes primero)
    citasDelDoctor.sort((a, b) => {
        const fechaA = new Date(a.fecha + ' ' + a.hora.split('-')[0]);
        const fechaB = new Date(b.fecha + ' ' + b.hora.split('-')[0]);
        return fechaB - fechaA;
    });
    
    // Mostrar las citas
    mostrarCitas();
}

// Configurar eventos de los filtros
function configurarFiltros() {
    const filtroEstado = document.getElementById('filtroEstado');
    const filtroFecha = document.getElementById('filtroFecha');
    
    filtroEstado.addEventListener('change', mostrarCitas);
    filtroFecha.addEventListener('change', mostrarCitas);
}

// Mostrar las citas según los filtros
function mostrarCitas() {
    const filtroEstado = document.getElementById('filtroEstado').value;
    const filtroFecha = document.getElementById('filtroFecha').value;
    const listadoCitas = document.getElementById('listadoCitas');
    const mensajeSinCitas = document.getElementById('mensajeSinCitas');
    
    console.log('=== DEBUG MOSTRAR CITAS ===');
    console.log('citasDelDoctor:', citasDelDoctor);
    console.log('Filtro estado:', filtroEstado);
    console.log('Filtro fecha:', filtroFecha);
    
    // Aplicar filtros
    let citasFiltradas = citasDelDoctor.filter(cita => {
        console.log('Filtrando cita:', cita);
        
        // Filtro por estado
        if (filtroEstado !== 'todas' && cita.estado !== filtroEstado) {
            console.log('Rechazada por estado. Esperado:', filtroEstado, 'Real:', cita.estado);
            return false;
        }
        
        // Filtro por fecha
        if (filtroFecha !== 'todas') {
            const fechaCita = new Date(cita.fecha);
            const hoy = new Date();
            hoy.setHours(0, 0, 0, 0);
            
            if (filtroFecha === 'hoy') {
                const citaHoy = new Date(fechaCita);
                citaHoy.setHours(0, 0, 0, 0);
                if (citaHoy.getTime() !== hoy.getTime()) {
                    return false;
                }
            } else if (filtroFecha === 'proximas') {
                if (fechaCita < hoy) {
                    return false;
                }
            } else if (filtroFecha === 'pasadas') {
                if (fechaCita >= hoy) {
                    return false;
                }
            }
        }
        
        console.log('Cita ACEPTADA');
        return true;
    });
    
    console.log('Citas filtradas final:', citasFiltradas);
    
    // Limpiar el listado
    listadoCitas.innerHTML = '';
    
    // Si no hay citas, mostrar mensaje
    if (citasFiltradas.length === 0) {
        mensajeSinCitas.style.display = 'block';
        return;
    }
    
    mensajeSinCitas.style.display = 'none';
    
    // Crear las tarjetas de citas
    citasFiltradas.forEach(cita => {
        const citaCard = crearTarjetaCita(cita);
        listadoCitas.appendChild(citaCard);
    });
}

// Crear una tarjeta de cita
function crearTarjetaCita(cita) {
    const card = document.createElement('div');
    card.className = 'cita-card';
    card.setAttribute('data-id', cita.id);
    
    // Determinar la clase de estado
    let estadoClass = 'pendiente';
    if (cita.estado === 'Completada') {
        estadoClass = 'completada';
    } else if (cita.estado === 'Cancelada') {
        estadoClass = 'cancelada';
    }
    
    // Formatear la fecha
    const fecha = new Date(cita.fecha);
    const opciones = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const fechaFormateada = fecha.toLocaleDateString('es-ES', opciones);
    
    card.innerHTML = `
        <div class="cita-header">
            <div class="cita-estudiante">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                    <circle cx="12" cy="7" r="4"></circle>
                </svg>
                <span>${cita.usuarioNombre}</span>
            </div>
            <span class="cita-estado ${estadoClass}">${cita.estado}</span>
        </div>
        
        <div class="cita-info">
            <div class="info-item">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                    <line x1="16" y1="2" x2="16" y2="6"></line>
                    <line x1="8" y1="2" x2="8" y2="6"></line>
                    <line x1="3" y1="10" x2="21" y2="10"></line>
                </svg>
                <span>${fechaFormateada}</span>
            </div>
            
            <div class="info-item">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="12" cy="12" r="10"></circle>
                    <polyline points="12 6 12 12 16 14"></polyline>
                </svg>
                <span>${cita.hora}</span>
            </div>
            
            <div class="info-item">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                    <polyline points="14 2 14 8 20 8"></polyline>
                    <line x1="16" y1="13" x2="8" y2="13"></line>
                    <line x1="16" y1="17" x2="8" y2="17"></line>
                    <polyline points="10 9 9 9 8 9"></polyline>
                </svg>
                <span><strong>Especialidad:</strong> ${cita.especialidad}</span>
            </div>
        </div>
        
        <div class="cita-motivo">
            <strong>Motivo de consulta:</strong>
            <p>${cita.motivo}</p>
        </div>
        
        ${cita.estado === 'Pendiente' ? `
            <div class="cita-acciones">
                <button class="btn-cancelar" onclick="cancelarCita('${cita.id}')">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <circle cx="12" cy="12" r="10"></circle>
                        <line x1="15" y1="9" x2="9" y2="15"></line>
                        <line x1="9" y1="9" x2="15" y2="15"></line>
                    </svg>
                    Cancelar Cita
                </button>
            </div>
        ` : ''}
    `;
    
    return card;
}

// Cancelar una cita
function cancelarCita(citaId) {
    if (!confirm('¿Estás seguro de que deseas cancelar esta cita?')) {
        return;
    }
    
    // Buscar la cita en el array completo
    const citaIndex = todasLasCitas.findIndex(c => c.id === citaId);
    
    if (citaIndex === -1) {
        alert('No se encontró la cita');
        return;
    }
    
    // Actualizar el estado a "Cancelada"
    todasLasCitas[citaIndex].estado = 'Cancelada';
    
    // Guardar en sessionStorage
    sessionStorage.setItem('citas', JSON.stringify(todasLasCitas));
    
    // Recargar las citas
    cargarCitasDelDoctor();
    
    alert('La cita ha sido cancelada exitosamente');
}
