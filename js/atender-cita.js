// Variables globales
let todasLasCitas = [];
let citasDelDoctor = [];

// Cargar citas cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    // Proteger la página - solo doctores pueden acceder
    const usuario = protegerPagina(['doctor']);
    
    if (!usuario) return;
    
    // Mostrar nombre del doctor
    document.getElementById('nombreDoctor').textContent = usuario.nombre || 'Doctor';
    
    // Establecer fecha de hoy por defecto
    const fechaInput = document.getElementById('filtroFechaAtencion');
    const hoy = new Date();
    const fechaISO = hoy.toISOString().split('T')[0];
    fechaInput.value = fechaISO;
    
    // Actualizar display de fecha
    actualizarDisplayFecha(hoy);
    
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

    // Obtener todas las citas del localStorage
    todasLasCitas = JSON.parse(localStorage.getItem('citas')) || [];
    
    console.log('=== DEBUG CARGAR CITAS ATENDER ===');
    console.log('Usuario doctor:', usuario);
    console.log('Usuario doctor ID:', usuario.id, 'tipo:', typeof usuario.id);
    console.log('Todas las citas:', todasLasCitas);
    
    // Filtrar solo las citas de este doctor
    citasDelDoctor = todasLasCitas.filter(cita => {
        console.log('Comparando - Cita doctorId:', cita.doctorId, 'tipo:', typeof cita.doctorId, 'vs Usuario ID:', usuario.id, 'tipo:', typeof usuario.id);
        return cita.doctorId == usuario.id; // Usar == en lugar de === para comparar sin tipo
    });
    
    // Mostrar las citas
    mostrarCitas();
}

// Configurar eventos de los filtros
function configurarFiltros() {
    const filtroFecha = document.getElementById('filtroFechaAtencion');
    
    filtroFecha.addEventListener('change', function() {
        const fecha = new Date(this.value + 'T00:00:00');
        actualizarDisplayFecha(fecha);
        mostrarCitas();
    });
}

// Actualizar el display visual de la fecha
function actualizarDisplayFecha(fecha) {
    const meses = ['enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio',
                   'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre'];
    
    const dia = fecha.getDate();
    const mes = meses[fecha.getMonth()];
    
    document.getElementById('diaNumero').textContent = dia;
    document.getElementById('mesTexto').textContent = mes;
}

// Mostrar las citas según los filtros
function mostrarCitas() {
    const filtroFecha = document.getElementById('filtroFechaAtencion').value;
    const listadoCitas = document.getElementById('listadoCitasAtencion');
    const mensajeSinCitas = document.getElementById('mensajeSinCitasAtencion');
    
    console.log('=== DEBUG ATENDER CITA ===');
    console.log('Fecha filtro:', filtroFecha);
    console.log('Todas las citas del doctor:', citasDelDoctor);
    
    // Filtrar citas pendientes de la fecha seleccionada
    let citasFiltradas = citasDelDoctor.filter(cita => {
        console.log('Revisando cita:', cita);
        console.log('Estado:', cita.estado, 'Fecha cita:', cita.fecha, 'Fecha filtro:', filtroFecha);
        
        // Solo mostrar citas pendientes
        if (cita.estado !== 'Pendiente') {
            console.log('Rechazada por estado');
            return false;
        }
        
        // Filtrar por fecha seleccionada
        if (filtroFecha && cita.fecha !== filtroFecha) {
            console.log('Rechazada por fecha');
            return false;
        }
        
        console.log('ACEPTADA');
        return true;
    });
    
    console.log('Citas filtradas final:', citasFiltradas);
    
    // Actualizar contador en el display
    const numeroCitas = citasFiltradas.length;
    document.getElementById('numeroCitas').textContent = `${numeroCitas} cita${numeroCitas !== 1 ? 's' : ''}`;
    
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
    
    // Crear las tarjetas de citas con campos inline
    citasFiltradas.forEach(cita => {
        const citaCard = crearTarjetaCitaAtencion(cita);
        listadoCitas.appendChild(citaCard);
    });
}

// Crear una tarjeta de cita para atención con campos inline
function crearTarjetaCitaAtencion(cita) {
    const card = document.createElement('div');
    card.className = 'atender-cita-item';
    card.setAttribute('data-id', cita.id);
    
    const horaInicio = cita.hora.split('-')[0].trim();
    
    card.innerHTML = `
        <div class="atender-cita-header">
            <span class="atender-cita-time">${horaInicio}</span>
            <span class="atender-cita-patient">${cita.usuarioNombre}</span>
            <span class="atender-cita-reason">${cita.motivo}</span>
        </div>
        
        <div class="atender-cita-form">
            <textarea 
                id="retro_${cita.id}" 
                class="atender-textarea"
                placeholder="Ingresar retroalimentación (escritura)"
                rows="3"
            ></textarea>
            <button class="btn-guardar" onclick="guardarRetroalimentacion('${cita.id}')">Guardar</button>
        </div>
    `;
    
    return card;
}

// Guardar retroalimentación de una cita
function guardarRetroalimentacion(citaId) {
    const textarea = document.getElementById(`retro_${citaId}`);
    const retroalimentacion = textarea.value.trim();
    
    if (!retroalimentacion) {
        alert('Por favor ingresa una retroalimentación antes de guardar');
        return;
    }
    
    // Confirmación
    if (!confirm('¿Deseas guardar las observaciones y marcar esta cita como atendida?')) {
        return;
    }
    
    // Buscar la cita en el array completo
    const citaIndex = todasLasCitas.findIndex(c => c.id === citaId);
    
    if (citaIndex === -1) {
        alert('No se encontró la cita');
        return;
    }
    
    // Actualizar el estado y agregar retroalimentación
    todasLasCitas[citaIndex].estado = 'Completada';
    todasLasCitas[citaIndex].retroalimentacion = retroalimentacion;
    todasLasCitas[citaIndex].fechaCompletada = new Date().toISOString();
    
    // Guardar en localStorage
    localStorage.setItem('citas', JSON.stringify(todasLasCitas));
    
    // Recargar las citas
    cargarCitasDelDoctor();
    
    alert('Las observaciones han sido guardadas y la cita ha sido marcada como Atendida');
}
