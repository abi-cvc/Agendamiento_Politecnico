// ===== BASE DE DATOS DE DOCTORES (AHORA SE CARGA DESDE BD VÍA JSP) =====
// Los doctores ahora se cargan dinámicamente desde la base de datos
// Esto está comentado porque ya no es necesario
/*
const doctoresDB = [
    {
        id: 5,
        nombre: "Dr. Roberto García",
        email: "doctor.nutricion@epn.edu.ec",
        especialidad: "nutricion",
        especialidadNombre: "Nutrición"
    },
    // ... más doctores
];
*/

// ===== HORARIOS DISPONIBLES (8:00am - 5:00pm, rangos de una hora) =====
const horariosBase = [
    "8:00am-9:00am", "9:00am-10:00am", "10:00am-11:00am", "11:00am-12:00pm", 
    "12:00pm-1:00pm", "1:00pm-2:00pm", "2:00pm-3:00pm", "3:00pm-4:00pm", "4:00pm-5:00pm"
];

// Variables globales
let horaSeleccionada = null;
let mesActual = new Date();
let fechaSeleccionada = null;

// ===== PROTEGER PÁGINA Y CARGAR DATOS =====
document.addEventListener('DOMContentLoaded', function() {
    // Verificar sesión - si no hay, guardar página actual y redirigir
    const usuario = verificarSesion();
    
    if (!usuario) {
        sessionStorage.setItem('paginaAnterior', 'views/agendamientos.jsp');
        window.location.href = '../index.jsp';
        return;
    }

    // Mostrar nombre del usuario
    document.getElementById('userName').textContent = usuario.nombre;
    
    // Si no es estudiante, ocultar el formulario de agendar
    if (usuario.rol !== 'estudiante') {
        const formularioSection = document.querySelector('.agendamiento-form');
        if (formularioSection) {
            formularioSection.innerHTML = `
                <div class="text-center" style="padding: 2rem;">
                    <h2>🔒 Solo Estudiantes</h2>
                    <p class="text-muted">Únicamente los estudiantes pueden agendar citas</p>
                </div>
            `;
        }
        const horariosSection = document.querySelector('.horarios-disponibles');
        if (horariosSection) {
            horariosSection.style.display = 'none';
        }
    } else {
        // Verificar si la especialidad viene preseleccionada desde JSP
        const especialidadSelect = document.getElementById('especialidad');
        const doctorSelect = document.getElementById('doctor');
        const especialidadBloqueada = especialidadSelect.disabled;
        
        // Si la especialidad está bloqueada y hay doctores disponibles, habilitar el select
        if (especialidadBloqueada && doctorSelect.options.length > 1) {
            doctorSelect.disabled = false;
            // Aplicar estilo de bloqueado a la especialidad
            especialidadSelect.style.background = 'rgba(60, 141, 188, 0.1)';
            especialidadSelect.style.cursor = 'not-allowed';
            especialidadSelect.style.opacity = '0.8';
        }
        
        // Event listeners
        if (!especialidadBloqueada) {
            especialidadSelect.addEventListener('change', cargarDoctoresPorEspecialidad);
        }
        doctorSelect.addEventListener('change', function() {
            if (this.value) {
                mostrarCalendario();
            } else {
                ocultarCalendario();
            }
        });
        
        // Event listeners del calendario
        document.getElementById('mesAnterior').addEventListener('click', () => cambiarMes(-1));
        document.getElementById('mesSiguiente').addEventListener('click', () => cambiarMes(1));
        
        document.getElementById('agendamientoForm').addEventListener('submit', agendarCita);
    }
});

// ===== PRE-SELECCIONAR ESPECIALIDAD DESDE URL =====
// Ahora se maneja directamente en JSP, esta función ya no es necesaria
/*
function preseleccionarEspecialidad() {
    const urlParams = new URLSearchParams(window.location.search);
    const especialidad = urlParams.get('especialidad');
    
    if (especialidad) {
        const selectEspecialidad = document.getElementById('especialidad');
        const option = selectEspecialidad.querySelector(`option[value="${especialidad}"]`);
        
        if (option) {
            selectEspecialidad.value = especialidad;
            // Bloquear el campo en modo solo lectura
            selectEspecialidad.disabled = true;
            selectEspecialidad.style.background = 'rgba(60, 141, 188, 0.1)';
            selectEspecialidad.style.cursor = 'not-allowed';
            selectEspecialidad.style.opacity = '0.8';
            
            // Cargar doctores automáticamente
            cargarDoctoresPorEspecialidad();
        }
    }
}
*/

// ===== CARGAR DOCTORES POR ESPECIALIDAD =====
// Esta función solo se usa cuando el usuario cambia manualmente la especialidad
// Si la especialidad viene preseleccionada desde URL, los doctores ya vienen cargados desde JSP
function cargarDoctoresPorEspecialidad() {
    const especialidadSeleccionada = document.getElementById('especialidad').value;
    const especialidadId = document.getElementById('especialidad').selectedOptions[0]?.dataset.id;
    const selectDoctor = document.getElementById('doctor');
    const fechaInput = document.getElementById('fecha');
    const btnSubmit = document.querySelector('.btn-agendar');
    
    // Resetear selecciones posteriores
    selectDoctor.innerHTML = '<option value="">Seleccione un doctor</option>';
    selectDoctor.disabled = true;
    fechaInput.disabled = true;
    fechaInput.value = '';
    btnSubmit.disabled = true;
    horaSeleccionada = null;
    
    // Limpiar horarios
    document.getElementById('horariosContainer').innerHTML = '<p class="no-horarios">Seleccione un doctor y fecha para ver los horarios disponibles</p>';
    
    if (!especialidadSeleccionada) {
        selectDoctor.innerHTML = '<option value="">Primero seleccione una especialidad</option>';
        return;
    }
    
    // Hacer petición AJAX para obtener doctores de esta especialidad
    // Por ahora, recargamos la página con el parámetro de especialidad
    window.location.href = `agendamientos.jsp?especialidad=${especialidadSeleccionada}`;
}

// ===== CARGAR HORARIOS DISPONIBLES DESDE BD =====
function cargarHorariosDisponibles() {
    const doctorId = document.getElementById('doctor').value;
    const fecha = document.getElementById('fecha').value;
    const horariosContainer = document.getElementById('horariosContainer');
    
    if (!doctorId || !fecha) {
        return;
    }
    
    // Mostrar mensaje de carga
    horariosContainer.innerHTML = '<p class="no-horarios">⏳ Cargando horarios disponibles...</p>';
    
    // Construir URL de la API
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
    const apiUrl = `${contextPath}/api/disponibilidad?idDoctor=${doctorId}&fecha=${fecha}`;
    
    console.log('=== CARGANDO HORARIOS DESDE BD ===');
    console.log('Doctor ID:', doctorId);
    console.log('Fecha:', fecha);
    console.log('API URL:', apiUrl);
    
    // Hacer petición AJAX a la API
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor');
            }
            return response.json();
        })
        .then(data => {
            console.log('Respuesta de la API:', data);
            
            if (!data.success) {
                throw new Error(data.error || 'Error desconocido');
            }
            
            if (!data.horarios || data.horarios.length === 0) {
                horariosContainer.innerHTML = `
                    <p class="no-horarios">
                        ℹ️ No hay horarios disponibles para esta fecha.<br>
                        Por favor, seleccione otra fecha.
                    </p>
                `;
                return;
            }
            
            // Renderizar horarios disponibles
            let html = '<div class="horarios-grid">';
            
            data.horarios.forEach(horario => {
                const disponible = horario.disponible === 'true' || horario.disponible === true;
                const claseEstado = disponible ? '' : 'ocupado';
                const deshabilitado = disponible ? '' : 'disabled';
                
                html += `
                    <button type="button" 
                            class="horario-btn ${claseEstado}" 
                            data-hora="${horario.horaInicio}" 
                            data-id-disponibilidad="${horario.id}"
                            ${deshabilitado}
                            onclick="seleccionarHorario('${horario.horaInicio}', '${horario.horaFin}', ${horario.id})">
                        ${horario.horarioFormateado}
                        ${!disponible ? '<span class="ocupado-badge">Ocupado</span>' : ''}
                    </button>
                `;
            });
            
            html += '</div>';
            horariosContainer.innerHTML = html;
            
            // Resetear selección
            horaSeleccionada = null;
            document.querySelector('.btn-agendar').disabled = true;
        })
        .catch(error => {
            console.error('Error al cargar horarios:', error);
            horariosContainer.innerHTML = `
                <p class="no-horarios error">
                    ❌ Error al cargar horarios: ${error.message}<br>
                    Por favor, intente nuevamente.
                </p>
            `;
        });
}

// ===== SELECCIONAR HORARIO =====
function seleccionarHorario(horaInicio, horaFin, idDisponibilidad) {
    // Remover selección anterior
    document.querySelectorAll('.horario-btn').forEach(btn => {
        btn.classList.remove('seleccionado');
    });
    
    // Marcar como seleccionado
    const btnSeleccionado = document.querySelector(`[data-hora="${horaInicio}"]`);
    if (btnSeleccionado) {
        btnSeleccionado.classList.add('seleccionado');
    }
    
    // Guardar horario seleccionado (ahora incluye más info)
    horaSeleccionada = {
        inicio: horaInicio,
        fin: horaFin,
        idDisponibilidad: idDisponibilidad
    };
    
    console.log('Horario seleccionado:', horaSeleccionada);
    
    // Actualizar input de hora (formato HH:MM para el input type="time")
    const horaInput = document.getElementById('hora');
    horaInput.value = horaInicio;
    
    // Habilitar botón de agendar
    document.querySelector('.btn-agendar').disabled = false;
}
    document.querySelector('.btn-agendar').disabled = false;
}

// ===== AGENDAR CITA =====
function agendarCita(e) {
    e.preventDefault();
    
    if (!horaSeleccionada) {
        alert('Por favor seleccione un horario');
        return;
    }
    
    const usuario = verificarSesion();
    const messageDiv = document.getElementById('formMessage');
    
    const especialidadSelect = document.getElementById('especialidad');
    const doctorSelect = document.getElementById('doctor');
    const fecha = document.getElementById('fecha').value;
    const motivo = document.getElementById('motivo').value;
    
    // Obtener datos del doctor seleccionado
    const doctorOption = doctorSelect.options[doctorSelect.selectedIndex];
    const doctorId = doctorSelect.value;
    const doctorNombre = doctorOption.textContent;
    const especialidadValue = doctorOption.dataset.especialidad;
    const especialidadNombre = doctorOption.dataset.especialidadNombre;
    
    console.log('=== DEBUG AGENDAR CITA ===');
    console.log('Doctor ID seleccionado:', doctorId, 'tipo:', typeof doctorId);
    console.log('Doctor ID parseado:', parseInt(doctorId), 'tipo:', typeof parseInt(doctorId));
    console.log('Usuario ID:', usuario.id, 'tipo:', typeof usuario.id);
    
    // Crear objeto de cita (preparado para BDD)
    const nuevaCita = {
        id: Date.now().toString(),
        usuarioId: usuario.id,
        usuarioNombre: usuario.nombre,
        doctorId: parseInt(doctorId),
        doctorNombre: doctorNombre,
        especialidad: especialidadNombre,
        especialidadValue: especialidadValue,
        fecha: fecha,
        hora: horaSeleccionada,
        motivo: motivo,
        estado: 'Pendiente',
        fechaCreacion: new Date().toISOString()
    };
    
    console.log('Nueva cita creada:', nuevaCita);
    
    // Guardar en localStorage (temporal, preparado para BDD)
    let citas = JSON.parse(localStorage.getItem('citas')) || [];
    citas.push(nuevaCita);
    localStorage.setItem('citas', JSON.stringify(citas));
    
    console.log('Todas las citas en sessionStorage:', citas);
    
    // Mostrar mensaje de éxito
    messageDiv.textContent = `¡Cita agendada exitosamente para el ${formatearFecha(fecha)} a las ${formatearHora(horaSeleccionada)} con ${doctorNombre}!`;
    messageDiv.className = 'form-message success show';
    
    // Limpiar formulario
    document.getElementById('motivo').value = '';
    horaSeleccionada = null;
    
    // Recargar horarios
    cargarHorariosDisponibles();
    
    // Ocultar mensaje después de 5 segundos
    setTimeout(() => {
        messageDiv.className = 'form-message';
    }, 5000);
}

// ===== UTILIDADES =====
function formatearFecha(fechaStr) {
    const opciones = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const fecha = new Date(fechaStr + 'T00:00:00');
    return fecha.toLocaleDateString('es-EC', opciones);
}

function formatearHora(hora) {
    // Los horarios ya vienen en el formato correcto
    return hora;
}

// ===== CALENDARIO VISUAL =====

/**
 * Muestra el calendario cuando se selecciona un doctor
 */
function mostrarCalendario() {
    const calendario = document.getElementById('calendario');
    calendario.style.display = 'block';
    generarCalendario();
}

/**
 * Oculta el calendario
 */
function ocultarCalendario() {
    const calendario = document.getElementById('calendario');
    calendario.style.display = 'none';
    fechaSeleccionada = null;
    document.getElementById('fecha').value = '';
    document.getElementById('fechaSeleccionadaTexto').style.display = 'none';
    // Limpiar horarios
    document.getElementById('horariosContainer').innerHTML = '<p class="no-horarios">Seleccione un doctor y fecha para ver los horarios disponibles</p>';
}

/**
 * Cambia el mes del calendario
 */
function cambiarMes(direccion) {
    mesActual.setMonth(mesActual.getMonth() + direccion);
    generarCalendario();
}

/**
 * Genera el calendario del mes actual
 */
function generarCalendario() {
    const year = mesActual.getFullYear();
    const month = mesActual.getMonth();
    
    // Actualizar título del mes
    const meses = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 
                   'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    document.getElementById('mesActual').textContent = `${meses[month]} ${year}`;
    
    // Obtener primer y último día del mes
    const primerDia = new Date(year, month, 1);
    const ultimoDia = new Date(year, month + 1, 0);
    const diasEnMes = ultimoDia.getDate();
    const primerDiaSemana = primerDia.getDay();
    
    // Obtener último día del mes anterior
    const ultimoDiaMesAnterior = new Date(year, month, 0).getDate();
    
    // Generar días
    const calendarioDias = document.getElementById('calendarioDias');
    calendarioDias.innerHTML = '';
    
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    
    // Días del mes anterior
    for (let i = primerDiaSemana - 1; i >= 0; i--) {
        const dia = ultimoDiaMesAnterior - i;
        const diaDiv = crearDiaCalendario(dia, 'otro-mes');
        calendarioDias.appendChild(diaDiv);
    }
    
    // Días del mes actual
    for (let dia = 1; dia <= diasEnMes; dia++) {
        const fecha = new Date(year, month, dia);
        fecha.setHours(0, 0, 0, 0);
        
        let clases = '';
        
        // Marcar hoy
        if (fecha.getTime() === hoy.getTime()) {
            clases += ' hoy';
        }
        
        // Deshabilitar fechas pasadas
        if (fecha < hoy) {
            clases += ' deshabilitado';
        }
        
        // Marcar día seleccionado
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
    const diasRestantes = 42 - (primerDiaSemana + diasEnMes); // 6 filas × 7 días
    for (let dia = 1; dia <= diasRestantes; dia++) {
        const diaDiv = crearDiaCalendario(dia, 'otro-mes');
        calendarioDias.appendChild(diaDiv);
    }
}

/**
 * Crea un elemento de día del calendario
 */
function crearDiaCalendario(dia, clases, fecha) {
    const diaDiv = document.createElement('div');
    diaDiv.className = 'dia-calendario' + (clases ? ' ' + clases : '');
    diaDiv.textContent = dia;
    
    // Si es un día válido y no del otro mes, agregar evento click
    if (fecha && !clases.includes('otro-mes') && !clases.includes('deshabilitado')) {
        diaDiv.addEventListener('click', () => seleccionarFecha(fecha));
    }
    
    return diaDiv;
}

/**
 * Selecciona una fecha del calendario
 */
function seleccionarFecha(fecha) {
    fechaSeleccionada = fecha;
    
    // Formatear fecha para el input (YYYY-MM-DD)
    const year = fecha.getFullYear();
    const month = String(fecha.getMonth() + 1).padStart(2, '0');
    const day = String(fecha.getDate()).padStart(2, '0');
    const fechaFormateada = `${year}-${month}-${day}`;
    
    document.getElementById('fecha').value = fechaFormateada;
    
    // Mostrar fecha seleccionada
    const opciones = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const fechaTexto = fecha.toLocaleDateString('es-EC', opciones);
    document.getElementById('fechaTexto').textContent = fechaTexto;
    document.getElementById('fechaSeleccionadaTexto').style.display = 'block';
    
    // Regenerar calendario para actualizar selección
    generarCalendario();
    
    // Cargar horarios disponibles
    cargarHorariosDisponibles();
}
