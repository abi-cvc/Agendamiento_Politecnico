// ===== JavaScript para Gestión de Evaluaciones - Admin =====

document.addEventListener('DOMContentLoaded', function() {
    
    // ===== AUTO-OCULTAR MENSAJES =====
    const mensajes = document.querySelectorAll('.mensaje');
    mensajes.forEach(mensaje => {
        setTimeout(() => {
            mensaje.style.transition = 'opacity 0.5s';
            mensaje.style.opacity = '0';
            setTimeout(() => mensaje.remove(), 500);
        }, 5000);
    });
    
    // ===== CALCULAR ESTADÍSTICAS GENERALES =====
    calcularEstadisticasGenerales();
    
    // ===== MEJORAR UX DE SELECTOR DE DOCTOR =====
    const selectorDoctor = document.getElementById('idDoctor');
    if (selectorDoctor) {
        selectorDoctor.addEventListener('change', function() {
            if (this.value) {
                this.style.borderColor = '#28a745';
            } else {
                this.style.borderColor = '';
            }
        });
    }
});

// ===== CALCULAR ESTADÍSTICAS GENERALES =====
function calcularEstadisticasGenerales() {
    const tabla = document.querySelector('.table-admin tbody');
    if (!tabla) return;
    
    const filas = tabla.querySelectorAll('tr');
    if (filas.length === 0) return;
    
    let sumaCalificaciones = 0;
    const doctoresUnicos = new Set();
    
    filas.forEach(fila => {
        // Obtener calificación (está en la columna 5)
        const celdaCalificacion = fila.cells[4];
        const numeroCalificacion = celdaCalificacion.querySelector('.calificacion-numero');
        if (numeroCalificacion) {
            const calificacion = parseInt(numeroCalificacion.textContent.match(/\d+/)[0]);
            sumaCalificaciones += calificacion;
        }
        
        // Obtener doctor único
        const celdaDoctor = fila.cells[1];
        if (celdaDoctor) {
            doctoresUnicos.add(celdaDoctor.textContent.trim());
        }
    });
    
    // Calcular promedio
    const promedio = sumaCalificaciones / filas.length;
    
    // Actualizar UI
    const promedioElement = document.getElementById('promedioGeneral');
    if (promedioElement) {
        promedioElement.textContent = promedio.toFixed(2) + ' ⭐';
    }
    
    const doctoresElement = document.getElementById('doctoresEvaluados');
    if (doctoresElement) {
        doctoresElement.textContent = doctoresUnicos.size;
    }
}

// ===== MOSTRAR MEJORES CALIFICADOS =====
function mostrarMejoresCalificados() {
    const url = window.location.origin + window.location.pathname.replace(/\/[^\/]*$/, '') + 
                '/evaluaciones?accion=mejoresCalificados&limite=10';
    
    fetch(url)
        .then(response => response.json())
        .then(data => {
            mostrarModalMejoresCalificados(data);
        })
        .catch(error => {
            console.error('Error al obtener mejores calificados:', error);
            alert('Error al cargar los datos');
        });
}

// ===== MODAL MEJORES CALIFICADOS =====
function mostrarModalMejoresCalificados(doctores) {
    // Crear modal
    const modal = document.createElement('div');
    modal.className = 'modal-overlay';
    modal.innerHTML = `
        <div class="modal-content">
            <div class="modal-header">
                <h2>🏆 Top 10 Doctores Mejor Calificados</h2>
                <button class="modal-close" onclick="this.closest('.modal-overlay').remove()">✖</button>
            </div>
            <div class="modal-body">
                ${doctores.length === 0 ? '<p>No hay datos disponibles</p>' : ''}
                <div class="ranking-lista">
                    ${doctores.map((doctor, index) => `
                        <div class="ranking-item ${index < 3 ? 'top-' + (index + 1) : ''}">
                            <div class="ranking-numero">${index + 1}</div>
                            <div class="ranking-info">
                                <h4>Dr(a). ${doctor.nombre} ${doctor.apellido}</h4>
                                <div class="ranking-stats">
                                    <span class="promedio">⭐ ${parseFloat(doctor.promedio).toFixed(2)}</span>
                                    <span class="total">📝 ${doctor.totalEvaluaciones} evaluaciones</span>
                                </div>
                            </div>
                            <div class="ranking-accion">
                                <a href="evaluaciones?accion=porDoctor&idDoctor=${doctor.idDoctor}" 
                                   class="btn btn-sm btn-primary">
                                    Ver Detalles
                                </a>
                            </div>
                        </div>
                    `).join('')}
                </div>
            </div>
        </div>
    `;
    
    document.body.appendChild(modal);
    
    // Cerrar al hacer clic fuera
    modal.addEventListener('click', function(e) {
        if (e.target === modal) {
            modal.remove();
        }
    });
}

// ===== FILTRAR EVALUACIONES EN TIEMPO REAL (OPCIONAL) =====
function filtrarEvaluaciones(filtro) {
    const filas = document.querySelectorAll('.table-admin tbody tr');
    
    filas.forEach(fila => {
        const textoFila = fila.textContent.toLowerCase();
        if (textoFila.includes(filtro.toLowerCase())) {
            fila.style.display = '';
        } else {
            fila.style.display = 'none';
        }
    });
}

// ===== EXPORTAR A CSV (OPCIONAL) =====
function exportarCSV() {
    const tabla = document.querySelector('.table-admin');
    if (!tabla) return;
    
    let csv = [];
    const filas = tabla.querySelectorAll('tr');
    
    filas.forEach(fila => {
        const celdas = fila.querySelectorAll('td, th');
        const filaData = Array.from(celdas).map(celda => {
            return '"' + celda.textContent.trim().replace(/"/g, '""') + '"';
        });
        csv.push(filaData.join(','));
    });
    
    const csvContent = csv.join('\n');
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);
    
    link.setAttribute('href', url);
    link.setAttribute('download', 'evaluaciones_' + new Date().toISOString().split('T')[0] + '.csv');
    link.style.visibility = 'hidden';
    
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

// ===== IMPRIMIR REPORTE =====
function imprimirReporte() {
    window.print();
}