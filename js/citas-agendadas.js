// Variables globales
let todasLasCitas = [];
let citasDelDoctor = [];
let mesActual = new Date();

// Cargar citas cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    // Proteger la página - solo doctores pueden acceder
    const usuario = protegerPagina(['doctor']);
    
    if (!usuario) return;
    
    // Mostrar nombre del doctor
    document.getElementById('nombreDoctor').textContent = usuario.nombre || 'Doctor';
    
    cargarCitasDelDoctor();
    configurarCalendario();
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
    
    console.log('=== DEBUG CARGAR CITAS DEL DOCTOR ===');
    console.log('Usuario doctor:', usuario);
    console.log('Usuario doctor ID:', usuario.id, 'tipo:', typeof usuario.id);
    console.log('Todas las citas:', todasLasCitas);
    
    // Filtrar solo las citas de este doctor
    citasDelDoctor = todasLasCitas.filter(cita => {
        console.log('Comparando - Cita doctorId:', cita.doctorId, 'tipo:', typeof cita.doctorId, 'vs Usuario ID:', usuario.id, 'tipo:', typeof usuario.id);
        console.log('¿Son iguales?', cita.doctorId === usuario.id);
        console.log('¿Son iguales con ==?', cita.doctorId == usuario.id);
        return cita.doctorId == usuario.id; // Usar == en lugar de === para comparar sin tipo
    });
    
    console.log('Citas del doctor filtradas:', citasDelDoctor);
    
    // Ordenar por fecha y hora (más recientes primero)
    citasDelDoctor.sort((a, b) => {
        const fechaA = new Date(a.fecha + ' ' + a.hora.split('-')[0]);
        const fechaB = new Date(b.fecha + ' ' + b.hora.split('-')[0]);
        return fechaB - fechaA;
    });
    
    // Mostrar el calendario
    mostrarCalendario();
}

// Configurar el calendario
function configurarCalendario() {
    document.getElementById('prevMonth').addEventListener('click', () => {
        mesActual.setMonth(mesActual.getMonth() - 1);
        mostrarCalendario();
    });
    
    document.getElementById('nextMonth').addEventListener('click', () => {
        mesActual.setMonth(mesActual.getMonth() + 1);
        mostrarCalendario();
    });
    
    document.getElementById('todayBtn').addEventListener('click', () => {
        mesActual = new Date();
        mostrarCalendario();
    });
}

// Mostrar el calendario del mes
function mostrarCalendario() {
    const meses = ['enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio', 
                   'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre'];
    
    const mesNombre = meses[mesActual.getMonth()];
    const año = mesActual.getFullYear();
    document.getElementById('mesActual').textContent = `${mesNombre} de ${año}`;
    
    // Obtener el primer y último día del mes
    const primerDia = new Date(mesActual.getFullYear(), mesActual.getMonth(), 1);
    const ultimoDia = new Date(mesActual.getFullYear(), mesActual.getMonth() + 1, 0);
    
    // Obtener el día de la semana del primer día (0 = domingo, ajustar a lunes = 0)
    let primerDiaSemana = primerDia.getDay() - 1;
    if (primerDiaSemana === -1) primerDiaSemana = 6; // Si es domingo, poner al final
    
    const calendarioDias = document.getElementById('calendarioDias');
    calendarioDias.innerHTML = '';
    
    // Obtener fecha de hoy
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    
    // Contar citas por día
    const citasPorDia = {};
    citasDelDoctor.forEach(cita => {
        // solo se muestre el número de citas pendientes del dia 
        document.getElementById('mesActual').textContent = `${mesNombre} de ${año}`;

        const fechaCita = new Date(cita.fecha + 'T00:00:00');
        if (fechaCita.getMonth() === mesActual.getMonth() && 
            fechaCita.getFullYear() === mesActual.getFullYear()) {
            const dia = fechaCita.getDate();
            if (!citasPorDia[dia]) {
                citasPorDia[dia] = [];
            }
            citasPorDia[dia].push(cita);
        }
    });
    
    // Agregar días del mes anterior para completar la primera semana
    const mesAnterior = new Date(mesActual.getFullYear(), mesActual.getMonth(), 0);
    const diasMesAnterior = mesAnterior.getDate();
    
    for (let i = primerDiaSemana - 1; i >= 0; i--) {
        const diaNum = diasMesAnterior - i;
        const diaDiv = document.createElement('div');
        diaDiv.className = 'calendar-day other-month';
        diaDiv.innerHTML = `<div class="calendar-day-number">${diaNum}</div>`;
        calendarioDias.appendChild(diaDiv);
    }
    
    // Agregar los días del mes actual
    for (let dia = 1; dia <= ultimoDia.getDate(); dia++) {
        const diaDiv = document.createElement('div');
        diaDiv.className = 'calendar-day';
        
        // Verificar si es hoy
        const fechaDia = new Date(mesActual.getFullYear(), mesActual.getMonth(), dia);
        fechaDia.setHours(0, 0, 0, 0);
        if (fechaDia.getTime() === hoy.getTime()) {
            diaDiv.classList.add('today');
        }
        
        let contenido = `<div class="calendar-day-number">${dia}</div>`;
        
        // Agregar contador de citas si las hay
        if (citasPorDia[dia] && citasPorDia[dia].length > 0) {
            const numCitas = citasPorDia[dia].length;
            contenido += `
                <div class="calendar-day-badge">${numCitas} cita${numCitas > 1 ? 's' : ''}</div>
            `;
            
            // Hacer el día clickeable
            diaDiv.style.cursor = 'pointer';
            diaDiv.addEventListener('click', () => {
                mostrarCitasDia(dia, citasPorDia[dia]);
            });
        }
        
        diaDiv.innerHTML = contenido;
        calendarioDias.appendChild(diaDiv);
    }
    
    // Agregar días del mes siguiente para completar la última semana
    const diasMostrados = primerDiaSemana + ultimoDia.getDate();
    const diasRestantes = diasMostrados % 7 === 0 ? 0 : 7 - (diasMostrados % 7);
    
    for (let dia = 1; dia <= diasRestantes; dia++) {
        const diaDiv = document.createElement('div');
        diaDiv.className = 'calendar-day other-month';
        diaDiv.innerHTML = `<div class="calendar-day-number">${dia}</div>`;
        calendarioDias.appendChild(diaDiv);
    }
}

// Mostrar citas de un día específico
function mostrarCitasDia(dia, citas) {
    const meses = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 
                   'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    
    const tituloModal = document.getElementById('tituloModal');
    tituloModal.textContent = `Citas del ${dia} de ${meses[mesActual.getMonth()]} de ${mesActual.getFullYear()}`;
    
    const listadoCitasDia = document.getElementById('listadoCitasDia');
    listadoCitasDia.innerHTML = '';
    
    // Si las citas vienen como string (desde onclick), parsear
    if (typeof citas === 'string') {
        citas = JSON.parse(citas);
    }
    
    citas.forEach(cita => {
        const citaCard = crearTarjetaCita(cita);
        listadoCitasDia.appendChild(citaCard);
    });
    
    document.getElementById('modalCitas').style.display = 'block';
}

// Cerrar modal
function cerrarModal() {
    document.getElementById('modalCitas').style.display = 'none';
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
    const fecha = new Date(cita.fecha + 'T00:00:00');
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
    
    // Guardar en localStorage
    localStorage.setItem('citas', JSON.stringify(todasLasCitas));
    
    // Cerrar el modal
    cerrarModal();
    
    // Recargar las citas
    cargarCitasDelDoctor();
    
    alert('La cita ha sido cancelada exitosamente');
}
