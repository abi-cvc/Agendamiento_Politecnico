// Variables globales
let todasLasCitas = [];
let citasDelDoctor = [];
let citaActualId = null;

// Cargar citas cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    // Proteger la página - solo doctores pueden acceder
    const usuario = protegerPagina(['doctor']);
    
    if (!usuario) return;
    
    // Establecer fecha de hoy por defecto
    const fechaInput = document.getElementById('filtroFechaAtencion');
    const hoy = new Date().toISOString().split('T')[0];
    fechaInput.value = hoy;
    
    cargarCitasDelDoctor();
    configurarFiltros();
});

// Cargar las citas del doctor actual
function cargarCitasDelDoctor() {
    const usuario = JSON.parse(sessionStorage.getItem('usuarioActual'));
    
    if (!usuario || usuario.rol !== 'doctor') {
        alert('Acceso no autorizado');
        window.location.href = 'login.html';
        return;
    }

    // Obtener todas las citas del sessionStorage
    todasLasCitas = JSON.parse(sessionStorage.getItem('citas')) || [];
    
    // Filtrar solo las citas de este doctor
    citasDelDoctor = todasLasCitas.filter(cita => cita.doctorId === usuario.id);
    
    // Mostrar las citas
    mostrarCitas();
}

// Configurar eventos de los filtros
function configurarFiltros() {
    const filtroFecha = document.getElementById('filtroFechaAtencion');
    filtroFecha.addEventListener('change', mostrarCitas);
}

// Mostrar las citas según los filtros
function mostrarCitas() {
    const filtroFecha = document.getElementById('filtroFechaAtencion').value;
    const listadoCitas = document.getElementById('listadoCitasAtencion');
    const mensajeSinCitas = document.getElementById('mensajeSinCitasAtencion');
    
    // Filtrar citas pendientes de la fecha seleccionada
    let citasFiltradas = citasDelDoctor.filter(cita => {
        // Solo mostrar citas pendientes
        if (cita.estado !== 'Pendiente') {
            return false;
        }
        
        // Filtrar por fecha seleccionada
        if (filtroFecha && cita.fecha !== filtroFecha) {
            return false;
        }
        
        return true;
    });
    
    // Ordenar por hora
    citasFiltradas.sort((a, b) => {
        const horaA = a.hora.split('-')[0];
        const horaB = b.hora.split('-')[0];
        return horaA.localeCompare(horaB);
    });
    
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
        const citaCard = crearTarjetaCitaAtencion(cita);
        listadoCitas.appendChild(citaCard);
    });
}

// Crear una tarjeta de cita para atención
function crearTarjetaCitaAtencion(cita) {
    const card = document.createElement('div');
    card.className = 'cita-card';
    card.setAttribute('data-id', cita.id);
    
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
            <span class="cita-estado pendiente">${cita.estado}</span>
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
        
        <div class="cita-acciones">
            <button class="btn-atender" onclick="abrirModalAtencion('${cita.id}')">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                    <polyline points="14 2 14 8 20 8"></polyline>
                    <line x1="16" y1="13" x2="8" y2="13"></line>
                    <line x1="16" y1="17" x2="8" y2="17"></line>
                    <polyline points="10 9 9 9 8 9"></polyline>
                </svg>
                Atender y Completar
            </button>
        </div>
    `;
    
    return card;
}

// Abrir modal para atender cita
function abrirModalAtencion(citaId) {
    citaActualId = citaId;
    const cita = todasLasCitas.find(c => c.id === citaId);
    
    if (!cita) {
        alert('No se encontró la cita');
        return;
    }
    
    // Formatear la fecha
    const fecha = new Date(cita.fecha);
    const opciones = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const fechaFormateada = fecha.toLocaleDateString('es-ES', opciones);
    
    // Mostrar información de la cita en el modal
    const infoCitaModal = document.getElementById('infoCitaModal');
    infoCitaModal.innerHTML = `
        <div class="cita-info-detalle">
            <h3 class="mb-md">${cita.usuarioNombre}</h3>
            <div class="detalle-item">
                <strong>Fecha:</strong> ${fechaFormateada}
            </div>
            <div class="detalle-item">
                <strong>Hora:</strong> ${cita.hora}
            </div>
            <div class="detalle-item">
                <strong>Especialidad:</strong> ${cita.especialidad}
            </div>
            <div class="detalle-item">
                <strong>Motivo:</strong> ${cita.motivo}
            </div>
        </div>
    `;
    
    // Limpiar el textarea
    document.getElementById('retroalimentacion').value = '';
    
    // Mostrar modal
    document.getElementById('modalAtenderCita').style.display = 'flex';
}

// Cerrar modal
function cerrarModalAtencion() {
    document.getElementById('modalAtenderCita').style.display = 'none';
    citaActualId = null;
}

// Completar cita con retroalimentación
function completarCita() {
    const retroalimentacion = document.getElementById('retroalimentacion').value.trim();
    
    if (!retroalimentacion) {
        alert('Por favor ingresa una retroalimentación antes de completar la cita');
        return;
    }
    
    if (!citaActualId) {
        alert('No se ha seleccionado ninguna cita');
        return;
    }
    
    // Buscar la cita en el array completo
    const citaIndex = todasLasCitas.findIndex(c => c.id === citaActualId);
    
    if (citaIndex === -1) {
        alert('No se encontró la cita');
        return;
    }
    
    // Actualizar el estado y agregar retroalimentación
    todasLasCitas[citaIndex].estado = 'Completada';
    todasLasCitas[citaIndex].retroalimentacion = retroalimentacion;
    todasLasCitas[citaIndex].fechaCompletada = new Date().toISOString();
    
    // Guardar en sessionStorage
    sessionStorage.setItem('citas', JSON.stringify(todasLasCitas));
    
    // Cerrar modal
    cerrarModalAtencion();
    
    // Recargar las citas
    cargarCitasDelDoctor();
    
    alert('La cita ha sido completada exitosamente');
}

// Cerrar modal al hacer clic fuera
window.onclick = function(event) {
    const modal = document.getElementById('modalAtenderCita');
    if (event.target === modal) {
        cerrarModalAtencion();
    }
}
