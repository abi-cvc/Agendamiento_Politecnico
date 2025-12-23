// ===== BASE DE DATOS DE DOCTORES (PREPARADO PARA BDD) =====
const doctoresDB = [
    {
        id: 5,
        nombre: "Dr. Roberto García",
        email: "doctor.nutricion@epn.edu.ec",
        especialidad: "nutricion",
        especialidadNombre: "Nutrición"
    },
    {
        id: 6,
        nombre: "Dra. Ana Martínez",
        email: "doctor.odontologia@epn.edu.ec",
        especialidad: "odontologia",
        especialidadNombre: "Odontología"
    },
    {
        id: 7,
        nombre: "Dr. Luis Fernández",
        email: "doctor.psicologia@epn.edu.ec",
        especialidad: "psicologia",
        especialidadNombre: "Psicología"
    },
    {
        id: 8,
        nombre: "Dra. María Sánchez",
        email: "doctor.medicina@epn.edu.ec",
        especialidad: "medicina-general",
        especialidadNombre: "Medicina General"
    },
    {
        id: 9,
        nombre: "Enf. Patricia Ruiz",
        email: "doctor.enfermeria@epn.edu.ec",
        especialidad: "enfermeria",
        especialidadNombre: "Enfermería"
    }
];

// ===== HORARIOS DISPONIBLES (8:00am - 5:00pm, rangos de una hora) =====
const horariosBase = [
    "8:00am-9:00am", "9:00am-10:00am", "10:00am-11:00am", "11:00am-12:00pm", 
    "12:00pm-1:00pm", "1:00pm-2:00pm", "2:00pm-3:00pm", "3:00pm-4:00pm", "4:00pm-5:00pm"
];

// Variables globales
let horaSeleccionada = null;

// ===== PROTEGER PÁGINA Y CARGAR DATOS =====
document.addEventListener('DOMContentLoaded', function() {
    // Verificar sesión - si no hay, guardar página actual y redirigir
    const usuario = verificarSesion();
    
    if (!usuario) {
        sessionStorage.setItem('paginaAnterior', 'agendamientos.html');
        window.location.href = 'index.html';
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
        // Configurar fecha mínima (hoy)
        const fechaInput = document.getElementById('fecha');
        const hoy = new Date().toISOString().split('T')[0];
        fechaInput.min = hoy;
        
        // Detectar y pre-seleccionar especialidad desde URL
        preseleccionarEspecialidad();
        
        // Event listeners
        document.getElementById('especialidad').addEventListener('change', cargarDoctoresPorEspecialidad);
        document.getElementById('doctor').addEventListener('change', habilitarFecha);
        document.getElementById('fecha').addEventListener('change', cargarHorariosDisponibles);
        document.getElementById('agendamientoForm').addEventListener('submit', agendarCita);
    }
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

// ===== CARGAR DOCTORES POR ESPECIALIDAD =====
function cargarDoctoresPorEspecialidad() {
    const especialidadSeleccionada = document.getElementById('especialidad').value;
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
    
    // Filtrar doctores por especialidad
    const doctoresFiltrados = doctoresDB.filter(d => d.especialidad === especialidadSeleccionada);
    
    if (doctoresFiltrados.length === 0) {
        selectDoctor.innerHTML = '<option value="">No hay doctores disponibles</option>';
        return;
    }
    
    // Agregar doctores al select
    doctoresFiltrados.forEach(doctor => {
        const option = document.createElement('option');
        option.value = doctor.id;
        option.textContent = doctor.nombre;
        option.dataset.especialidad = doctor.especialidad;
        option.dataset.especialidadNombre = doctor.especialidadNombre;
        selectDoctor.appendChild(option);
    });
    
    selectDoctor.disabled = false;
}

// ===== HABILITAR FECHA CUANDO SE SELECCIONA DOCTOR =====
function habilitarFecha() {
    const doctorSeleccionado = document.getElementById('doctor').value;
    const fechaInput = document.getElementById('fecha');
    
    if (doctorSeleccionado) {
        fechaInput.disabled = false;
        fechaInput.value = '';
        horaSeleccionada = null;
        document.getElementById('horariosContainer').innerHTML = '<p class="no-horarios">Seleccione una fecha para ver los horarios disponibles</p>';
    } else {
        fechaInput.disabled = true;
        fechaInput.value = '';
        document.getElementById('horariosContainer').innerHTML = '<p class="no-horarios">Seleccione un doctor y fecha para ver los horarios disponibles</p>';
    }
    
    document.querySelector('.btn-agendar').disabled = true;
}

// ===== CARGAR HORARIOS DISPONIBLES =====
function cargarHorariosDisponibles() {
    const doctorId = document.getElementById('doctor').value;
    const fecha = document.getElementById('fecha').value;
    const horariosContainer = document.getElementById('horariosContainer');
    
    if (!doctorId || !fecha) {
        return;
    }
    
    // Obtener citas existentes (preparado para BDD)
    const citasExistentes = JSON.parse(sessionStorage.getItem('citas')) || [];
    
    // Filtrar citas del doctor en la fecha seleccionada
    const citasOcupadas = citasExistentes.filter(c => 
        c.doctorId == doctorId && c.fecha === fecha
    ).map(c => c.hora);
    
    // Generar horarios disponibles
    let html = '<div class="horarios-grid">';
    
    horariosBase.forEach(hora => {
        const ocupado = citasOcupadas.includes(hora);
        const claseOcupado = ocupado ? 'ocupado' : '';
        const deshabilitado = ocupado ? 'disabled' : '';
        
        html += `
            <button type="button" 
                    class="horario-btn ${claseOcupado}" 
                    data-hora="${hora}" 
                    ${deshabilitado}
                    onclick="seleccionarHorario('${hora}')">
                ${formatearHora(hora)}
                ${ocupado ? '<span class="ocupado-badge">Ocupado</span>' : ''}
            </button>
        `;
    });
    
    html += '</div>';
    horariosContainer.innerHTML = html;
    
    // Resetear selección
    horaSeleccionada = null;
    document.querySelector('.btn-agendar').disabled = true;
}

// ===== SELECCIONAR HORARIO =====
function seleccionarHorario(hora) {
    // Remover selección anterior
    document.querySelectorAll('.horario-btn').forEach(btn => {
        btn.classList.remove('seleccionado');
    });
    
    // Marcar como seleccionado
    const btnSeleccionado = document.querySelector(`[data-hora="${hora}"]`);
    btnSeleccionado.classList.add('seleccionado');
    
    horaSeleccionada = hora;
    
    // Habilitar botón de agendar
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