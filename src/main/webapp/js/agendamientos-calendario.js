// ===== HORARIOS DISPONIBLES =====
const horariosBase = [
    "8:00am-9:00am", "9:00am-10:00am", "10:00am-11:00am", "11:00am-12:00pm", 
    "12:00pm-1:00pm", "1:00pm-2:00pm", "2:00pm-3:00pm", "3:00pm-4:00pm", "4:00pm-5:00pm"
];

// Variables globales
let horaSeleccionada = null;
let mesActual = new Date();
let fechaSeleccionada = null;

// ===== INICIALIZACIÓN =====
document.addEventListener('DOMContentLoaded', function() {
    console.log('Iniciando agendamientos.js');
    
    // Verificar sesión
    const usuario = verificarSesion();
    
    if (!usuario) {
        sessionStorage.setItem('paginaAnterior', 'views/agendamientos.jsp');
        window.location.href = '../index.jsp';
        return;
    }

    // Mostrar nombre del usuario
    if (document.getElementById('userName')) {
        document.getElementById('userName').textContent = usuario.nombre;
    }
    
    // Si no es estudiante, ocultar el formulario
    if (usuario.rol !== 'estudiante') {
        const formularioSection = document.querySelector('.agendamiento-form');
        if (formularioSection) {
            formularioSection.innerHTML = '<div class="text-center" style="padding: 2rem;"><h2>Solo Estudiantes</h2><p class="text-muted">Únicamente los estudiantes pueden agendar citas</p></div>';
        }
        const horariosSection = document.querySelector('.horarios-disponibles');
        if (horariosSection) {
            horariosSection.style.display = 'none';
        }
        return;
    }
    
    // Configurar eventos
    inicializarFormulario();
});

function inicializarFormulario() {
    const especialidadSelect = document.getElementById('especialidad');
    const doctorSelect = document.getElementById('doctor');
    
    if (!especialidadSelect || !doctorSelect) {
        console.error('No se encontraron los elementos del formulario');
        return;
    }
    
    // Si la especialidad está bloqueada
    if (especialidadSelect.disabled && doctorSelect.options.length > 1) {
        doctorSelect.disabled = false;
        especialidadSelect.style.background = 'rgba(60, 141, 188, 0.1)';
        especialidadSelect.style.cursor = 'not-allowed';
        especialidadSelect.style.opacity = '0.8';
    }
    
    // Event listener para doctor
    doctorSelect.addEventListener('change', function() {
        console.log('Doctor seleccionado:', this.value);
        if (this.value) {
            mostrarCalendario();
        } else {
            ocultarCalendario();
        }
    });
    
    // Event listeners del calendario
    const btnAnterior = document.getElementById('mesAnterior');
    const btnSiguiente = document.getElementById('mesSiguiente');
    
    if (btnAnterior && btnSiguiente) {
        btnAnterior.addEventListener('click', function() { cambiarMes(-1); });
        btnSiguiente.addEventListener('click', function() { cambiarMes(1); });
    }
    
    // Event listener del formulario
    const form = document.getElementById('agendamientoForm');
    if (form) {
        form.addEventListener('submit', agendarCita);
    }
}

// ===== CALENDARIO =====

function mostrarCalendario() {
    console.log('Mostrando calendario...');
    const calendario = document.getElementById('calendario');
    if (calendario) {
        calendario.style.display = 'block';
        generarCalendario();
    } else {
        console.error('Elemento calendario no encontrado');
    }
}

function ocultarCalendario() {
    const calendario = document.getElementById('calendario');
    if (calendario) {
        calendario.style.display = 'none';
    }
    fechaSeleccionada = null;
    const fechaInput = document.getElementById('fecha');
    if (fechaInput) {
        fechaInput.value = '';
    }
    const fechaTexto = document.getElementById('fechaSeleccionadaTexto');
    if (fechaTexto) {
        fechaTexto.style.display = 'none';
    }
    const horariosContainer = document.getElementById('horariosContainer');
    if (horariosContainer) {
        horariosContainer.innerHTML = '<p class="no-horarios">Seleccione un doctor y fecha para ver los horarios disponibles</p>';
    }
}

function cambiarMes(direccion) {
    mesActual.setMonth(mesActual.getMonth() + direccion);
    generarCalendario();
}

function generarCalendario() {
    const year = mesActual.getFullYear();
    const month = mesActual.getMonth();
    
    const meses = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 
                   'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    
    const mesActualElement = document.getElementById('mesActual');
    if (mesActualElement) {
        mesActualElement.textContent = meses[month] + ' ' + year;
    }
    
    const primerDia = new Date(year, month, 1);
    const ultimoDia = new Date(year, month + 1, 0);
    const diasEnMes = ultimoDia.getDate();
    const primerDiaSemana = primerDia.getDay();
    const ultimoDiaMesAnterior = new Date(year, month, 0).getDate();
    
    const calendarioDias = document.getElementById('calendarioDias');
    if (!calendarioDias) {
        console.error('Elemento calendarioDias no encontrado');
        return;
    }
    
    calendarioDias.innerHTML = '';
    
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    
    // Días del mes anterior
    for (let i = primerDiaSemana - 1; i >= 0; i--) {
        const dia = ultimoDiaMesAnterior - i;
        const diaDiv = crearDiaCalendario(dia, 'otro-mes', null);
        calendarioDias.appendChild(diaDiv);
    }
    
    // Días del mes actual
    for (let dia = 1; dia <= diasEnMes; dia++) {
        const fecha = new Date(year, month, dia);
        fecha.setHours(0, 0, 0, 0);
        
        let clases = '';
        
        if (fecha.getTime() === hoy.getTime()) {
            clases += ' hoy';
        }
        
        if (fecha < hoy) {
            clases += ' deshabilitado';
        }
        
        if (fechaSeleccionada && 
            fecha.getFullYear() === fechaSeleccionada.getFullYear() &&
            fecha.getMonth() === fechaSeleccionada.getMonth() &&
            fecha.getDate() === fechaSeleccionada.getDate()) {
            clases += ' seleccionado';
        }
        
        const diaDiv = crearDiaCalendario(dia, clases, fecha);
        calendarioDias.appendChild(diaDiv);
    }
    
    // Días del mes siguiente
    const diasRestantes = 42 - (primerDiaSemana + diasEnMes);
    for (let dia = 1; dia <= diasRestantes; dia++) {
        const diaDiv = crearDiaCalendario(dia, 'otro-mes', null);
        calendarioDias.appendChild(diaDiv);
    }
}

function crearDiaCalendario(dia, clases, fecha) {
    const diaDiv = document.createElement('div');
    diaDiv.className = 'dia-calendario' + (clases ? ' ' + clases : '');
    diaDiv.textContent = dia;
    
    if (fecha && !clases.includes('otro-mes') && !clases.includes('deshabilitado')) {
        diaDiv.style.cursor = 'pointer';
        diaDiv.addEventListener('click', function() {
            seleccionarFecha(fecha);
        });
    }
    
    return diaDiv;
}

function seleccionarFecha(fecha) {
    console.log('Fecha seleccionada:', fecha);
    fechaSeleccionada = fecha;
    
    const year = fecha.getFullYear();
    const month = String(fecha.getMonth() + 1).padStart(2, '0');
    const day = String(fecha.getDate()).padStart(2, '0');
    const fechaFormateada = year + '-' + month + '-' + day;
    
    const fechaInput = document.getElementById('fecha');
    if (fechaInput) {
        fechaInput.value = fechaFormateada;
    }
    
    const opciones = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const fechaTexto = fecha.toLocaleDateString('es-EC', opciones);
    
    const fechaTextoElement = document.getElementById('fechaTexto');
    if (fechaTextoElement) {
        fechaTextoElement.textContent = fechaTexto;
    }
    
    const fechaSeleccionadaTexto = document.getElementById('fechaSeleccionadaTexto');
    if (fechaSeleccionadaTexto) {
        fechaSeleccionadaTexto.style.display = 'block';
    }
    
    generarCalendario();
    cargarHorariosDisponibles();
}

// ===== CARGAR HORARIOS =====

function cargarHorariosDisponibles() {
    const doctorId = document.getElementById('doctor').value;
    const fecha = document.getElementById('fecha').value;
    const horariosContainer = document.getElementById('horariosContainer');
    
    if (!doctorId || !fecha) {
        return;
    }
    
    horariosContainer.innerHTML = '<p class="no-horarios">Cargando horarios disponibles...</p>';
    
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
    const apiUrl = contextPath + '/api/disponibilidad?idDoctor=' + doctorId + '&fecha=' + fecha;
    
    console.log('Cargando horarios desde:', apiUrl);
    
    fetch(apiUrl)
        .then(function(response) {
            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor');
            }
            return response.json();
        })
        .then(function(data) {
            console.log('Respuesta de la API:', data);
            
            if (!data.success) {
                throw new Error(data.error || 'Error desconocido');
            }
            
            if (!data.horarios || data.horarios.length === 0) {
                horariosContainer.innerHTML = '<p class="no-horarios">No hay horarios disponibles para esta fecha.</p>';
                return;
            }
            
            let html = '<div class="horarios-grid">';
            
            data.horarios.forEach(function(horario) {
                const disponible = horario.disponible === 'true' || horario.disponible === true;
                const claseEstado = disponible ? '' : 'ocupado';
                const deshabilitado = disponible ? '' : 'disabled';
                
                html += '<button type="button" class="horario-btn ' + claseEstado + '" ';
                html += 'data-hora="' + horario.horaInicio + '" ';
                html += 'data-id-disponibilidad="' + horario.id + '" ';
                html += deshabilitado + ' ';
                html += 'onclick="seleccionarHorario(\'' + horario.horaInicio + '\', \'' + horario.horaFin + '\', ' + horario.id + ')">';
                html += horario.horarioFormateado;
                if (!disponible) {
                    html += '<span class="ocupado-badge">Ocupado</span>';
                }
                html += '</button>';
            });
            
            html += '</div>';
            horariosContainer.innerHTML = html;
            
            horaSeleccionada = null;
            const btnAgendar = document.querySelector('.btn-agendar');
            if (btnAgendar) {
                btnAgendar.disabled = true;
            }
        })
        .catch(function(error) {
            console.error('Error al cargar horarios:', error);
            horariosContainer.innerHTML = '<p class="no-horarios error">Error al cargar horarios. Por favor, intente nuevamente.</p>';
        });
}

function seleccionarHorario(horaInicio, horaFin, idDisponibilidad) {
    const botones = document.querySelectorAll('.horario-btn');
    botones.forEach(function(btn) {
        btn.classList.remove('seleccionado');
    });
    
    const btnSeleccionado = document.querySelector('[data-hora="' + horaInicio + '"]');
    if (btnSeleccionado) {
        btnSeleccionado.classList.add('seleccionado');
    }
    
    horaSeleccionada = {
        inicio: horaInicio,
        fin: horaFin,
        idDisponibilidad: idDisponibilidad
    };
    
    console.log('Horario seleccionado:', horaSeleccionada);
    
    const horaInput = document.getElementById('hora');
    if (horaInput) {
        horaInput.value = horaInicio;
    }
    
    const btnAgendar = document.querySelector('.btn-agendar');
    if (btnAgendar) {
        btnAgendar.disabled = false;
    }
}

// ===== AGENDAR CITA =====

function agendarCita(e) {
    e.preventDefault();
    
    if (!horaSeleccionada) {
        alert('Por favor seleccione un horario');
        return;
    }
    
    console.log('Agendando cita...');
    this.submit();
}
