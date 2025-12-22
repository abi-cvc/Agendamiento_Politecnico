// ===== PROTEGER PÁGINA Y CARGAR DATOS =====
document.addEventListener('DOMContentLoaded', function() {
    // Verificar sesión - si no hay, guardar página actual y redirigir
    const usuario = verificarSesion();
    
    if (!usuario) {
        sessionStorage.setItem('paginaAnterior', 'consultar-citas.html');
        window.location.href = 'login.html';
        return;
    }

    // Mostrar nombre del usuario
    document.getElementById('userName').textContent = usuario.nombre;
    
    // Configurar filtro
    document.getElementById('filtroEstado').addEventListener('change', cargarCitas);
    
    // Cargar citas al inicio
    cargarCitas();
});

// ===== CARGAR CITAS DEL USUARIO =====
function cargarCitas() {
    const usuario = verificarSesion();
    const listaCitas = document.getElementById('listaCitas');
    const filtroEstado = document.getElementById('filtroEstado').value;
    const citas = JSON.parse(sessionStorage.getItem('citas')) || [];
    
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
    
    // Ordenar por fecha (más recientes primero)
    citasFiltradas.sort((a, b) => new Date(b.fecha) - new Date(a.fecha));
    
    // Generar HTML
    let html = '';
    citasFiltradas.forEach(cita => {
        const fechaFormateada = formatearFecha(cita.fecha);
        const esPasada = new Date(cita.fecha) < new Date();
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
                
                ${cita.estado === 'Confirmada' && !esPasada ? `
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
    if (!confirm('¿Estás seguro de que deseas cancelar esta cita?')) {
        return;
    }
    
    let citas = JSON.parse(sessionStorage.getItem('citas')) || [];
    const cita = citas.find(c => c.id === citaId);
    
    if (cita) {
        cita.estado = 'Cancelada';
        sessionStorage.setItem('citas', JSON.stringify(citas));
        cargarCitas();
        
        // Mostrar mensaje de confirmación
        alert('Cita cancelada exitosamente');
    }
}

// ===== UTILIDADES =====
function formatearFecha(fechaStr) {
    const opciones = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const fecha = new Date(fechaStr + 'T00:00:00');
    return fecha.toLocaleDateString('es-EC', opciones);
}
