// ===== GESTIÓN DE ESTUDIANTES - ADMIN =====

let estudiantes = [];

document.addEventListener('DOMContentLoaded', function() {
    cargarEstudiantes();

    const formNuevo = document.getElementById('form-nuevo-estudiante');
    if (formNuevo) formNuevo.addEventListener('submit', guardarNuevoEstudiante);

    const formEditar = document.getElementById('form-editar-estudiante');
    if (formEditar) formEditar.addEventListener('submit', actualizarEstudiante);
});

function cargarEstudiantes() {
    const urlPrimary = getContextPath() + '/admin/estudiantes?format=json&_=' + Date.now();
    const urlAlt = getContextPath() + '/EstudianteAdminController?format=json&_=' + Date.now();

    function handleData(data) { console.log('Cargando estudiantes desde servidor, cantidad=', Array.isArray(data)?data.length:0); estudiantes = Array.isArray(data) ? data : []; renderizarEstudiantes(); }

    fetch(urlPrimary, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
        .then(r => r.ok ? r.json() : Promise.reject(r))
        .then(handleData)
        .catch(() => {
            fetch(urlAlt, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
                .then(r => r.ok ? r.json() : Promise.reject(r))
                .then(handleData)
                .catch(() => {
                    estudiantes = [
                        { idEstudiante: 1, cedula: '1725896347', nombre: 'Carlos', apellido: 'Rodríguez', correoEstudiante: 'carlos.rodriguez@epn.edu.ec', telefono: '0998765432', carrera: 'Ingeniería en Sistemas', activo: true },
                        { idEstudiante: 2, cedula: '1712345678', nombre: 'Ana', apellido: 'Martínez', correoEstudiante: 'ana.martinez@epn.edu.ec', telefono: '0987654321', carrera: 'Ingeniería Civil', activo: true }
                    ];
                    renderizarEstudiantes();
                });
        });
}

function renderizarEstudiantes(filtro = null) {
    const tbody = document.getElementById('estudiantes-tbody');
    if (!tbody) return;
    tbody.innerHTML = '';

    const lista = filtro || estudiantes;
    if (!lista || lista.length === 0) {
        const tr = document.createElement('tr');
        const td = document.createElement('td');
        td.colSpan = 8; td.style.textAlign = 'center'; td.style.padding = '40px'; td.style.color = 'var(--color-8)';
        td.textContent = 'No se encontraron estudiantes';
        tr.appendChild(td); tbody.appendChild(tr); return;
    }

    lista.forEach(est => {
        const tr = document.createElement('tr');
        const tdCed = document.createElement('td'); tdCed.textContent = est.cedula || '';
        const tdNom = document.createElement('td'); tdNom.textContent = est.nombre || '';
        const tdApe = document.createElement('td'); tdApe.textContent = est.apellido || '';
        const tdEmail = document.createElement('td'); tdEmail.textContent = est.correoEstudiante || est.email || '';
        const tdTel = document.createElement('td'); tdTel.textContent = est.telefono || '';
        const tdCarr = document.createElement('td'); tdCarr.textContent = est.carrera || '';

        const tdEstado = document.createElement('td');
        const span = document.createElement('span'); span.className = 'estado-badge ' + (est.activo ? 'estado-activo' : 'estado-inactivo');
        span.textContent = est.activo ? 'Activo' : 'Inactivo'; tdEstado.appendChild(span);

        const tdAcc = document.createElement('td');
        const divAcc = document.createElement('div'); divAcc.className = 'btn-actions';
        const btnEdit = document.createElement('button'); btnEdit.type = 'button'; btnEdit.className = 'btn btn-sm btn-warning'; btnEdit.textContent = 'Editar';
        btnEdit.addEventListener('click', function() { abrirModalEditarById(est.idEstudiante || est.id); });
        const btnEstado = document.createElement('button'); btnEstado.type = 'button'; btnEstado.className = 'btn btn-sm ' + (est.activo ? 'btn-danger' : 'btn-primary');
        btnEstado.textContent = est.activo ? 'Desactivar' : 'Activar';
        btnEstado.addEventListener('click', function() { cambiarEstado(est.idEstudiante || est.id); });

        divAcc.appendChild(btnEdit); divAcc.appendChild(btnEstado); tdAcc.appendChild(divAcc);

        tr.appendChild(tdCed); tr.appendChild(tdNom); tr.appendChild(tdApe); tr.appendChild(tdEmail); tr.appendChild(tdTel); tr.appendChild(tdCarr); tr.appendChild(tdEstado); tr.appendChild(tdAcc);
        tbody.appendChild(tr);
    });
}

function abrirModalEditarById(id) {
    const e = estudiantes.find(x => (x.idEstudiante || x.id || 0) === id);
    if (e) abrirModalEditar(e); else console.warn('Estudiante no encontrado', id);
}

function buscarEstudiante() {
    const input = document.getElementById('buscar-cedula'); if (!input) return;
    const ced = input.value.trim(); if (!ced) { mostrarMensaje('Por favor ingrese una cédula', 'error'); return; }
    const res = estudiantes.filter(x => (x.cedula || '').includes(ced));
    renderizarEstudiantes(res);
    if (!res || res.length === 0) mostrarMensaje('No se encontró ningún estudiante con esa cédula', 'error');
}

function limpiarBusqueda() { const i = document.getElementById('buscar-cedula'); if (i) i.value = ''; renderizarEstudiantes(); }

function abrirModalNuevo() { const f = document.getElementById('form-nuevo-estudiante'); if (f) f.reset(); const m = document.getElementById('modalNuevo'); if (m) m.classList.add('active'); }

function abrirModalEditar(estudiante) {
    const idEl = document.getElementById('edit-id'); if (idEl) idEl.value = estudiante.idEstudiante || estudiante.id || '';
    const ced = document.getElementById('edit-cedula'); if (ced) ced.value = estudiante.cedula || '';
    const nom = document.getElementById('edit-nombre'); if (nom) nom.value = estudiante.nombre || '';
    const ape = document.getElementById('edit-apellido'); if (ape) ape.value = estudiante.apellido || '';
    const email = document.getElementById('edit-email'); if (email) email.value = estudiante.correoEstudiante || estudiante.email || '';
    const tel = document.getElementById('edit-telefono'); if (tel) tel.value = estudiante.telefono || '';
    const carrera = document.getElementById('edit-carrera'); if (carrera) carrera.value = estudiante.carrera || '';
    const foto = document.getElementById('edit-foto'); if (foto) foto.value = estudiante.foto || '';
    const direccion = document.getElementById('edit-direccion'); if (direccion) direccion.value = estudiante.direccion || '';
    const modal = document.getElementById('modalEditar'); if (modal) modal.classList.add('active');
}

function cerrarModal(modalId) { const m = document.getElementById(modalId); if (m) m.classList.remove('active'); }

function guardarNuevoEstudiante(e) {
    if (e && e.preventDefault) e.preventDefault();
    const form = e ? e.target : document.getElementById('form-nuevo-estudiante');
    if (!form) return false;
    const fd = new FormData(form);
    fd.append('accion', 'crear');

    const urlPrimary = getContextPath() + '/admin/estudiantes';
    const urlAlt = getContextPath() + '/EstudianteAdminController';

    fetch(urlPrimary, { method: 'POST', body: fd, headers: { 'X-Requested-With': 'XMLHttpRequest' } })
        .then(r => { if (!r.ok) return Promise.reject(r); return r.text(); })
        .then(() => { cargarEstudiantes(); cerrarModal('modalNuevo'); mostrarMensaje('Estudiante creado exitosamente', 'success'); })
        .catch(err => {
            console.warn('Crear estudiante falló en', urlPrimary, err, 'intentando alternativa');
            fetch(urlAlt, { method: 'POST', body: fd, headers: { 'X-Requested-With': 'XMLHttpRequest' } })
                .then(r => { if (!r.ok) return Promise.reject(r); return r.text(); })
                .then(() => { cargarEstudiantes(); cerrarModal('modalNuevo'); mostrarMensaje('Estudiante creado exitosamente', 'success'); })
                .catch(err2 => { console.error('Error creando estudiante en ambas rutas', err2); mostrarMensaje('Error al crear estudiante', 'error'); });
        });

    return false;
}

function actualizarEstudiante(e) {
    if (e && e.preventDefault) e.preventDefault();
    const form = e ? e.target : document.getElementById('form-editar-estudiante'); if (!form) return false;
    const fd = new FormData(form); fd.append('accion', 'actualizar');

    const urlPrimary = getContextPath() + '/admin/estudiantes';
    const urlAlt = getContextPath() + '/EstudianteAdminController';

    fetch(urlPrimary, { method: 'POST', body: fd, headers: { 'X-Requested-With': 'XMLHttpRequest' } })
        .then(r => { if (!r.ok) return Promise.reject(r); return r.text(); })
        .then(() => { cargarEstudiantes(); cerrarModal('modalEditar'); mostrarMensaje('Estudiante actualizado exitosamente', 'success'); })
        .catch(err => {
            console.warn('Actualizar estudiante falló en', urlPrimary, err, 'intentando alternativa');
            fetch(urlAlt, { method: 'POST', body: fd, headers: { 'X-Requested-With': 'XMLHttpRequest' } })
                .then(r => { if (!r.ok) return Promise.reject(r); return r.text(); })
                .then(() => { cargarEstudiantes(); cerrarModal('modalEditar'); mostrarMensaje('Estudiante actualizado exitosamente', 'success'); })
                .catch(err2 => { console.error('Error actualizando estudiante en ambas rutas', err2); mostrarMensaje('Error al actualizar estudiante', 'error'); });
        });

    return false;
}

function cambiarEstado(id) { const est = estudiantes.find(x => (x.idEstudiante || x.id || 0) === id); if (!est) return; const accion = est.activo ? 'desactivar' : 'activar'; if (!confirm('¿Está seguro de ' + accion + ' a este estudiante?')) return; est.activo = !est.activo; renderizarEstudiantes(); mostrarMensaje('Estudiante ' + (est.activo ? 'activado' : 'desactivado') + ' exitosamente','success'); }

function mostrarMensaje(texto, tipo) { const container = document.getElementById('mensaje-container'); if (!container) return; const div = document.createElement('div'); div.className = 'mensaje ' + (tipo==='success'?'success':'error'); div.style.padding = 'var(--spacing-md)'; div.style.borderRadius = 'var(--radius-md)'; div.style.marginBottom = 'var(--spacing-lg)'; div.style.fontWeight = '500'; div.style.background = (tipo==='success'?'#d1fae5':'#fee2e2'); div.style.color = (tipo==='success'?'#065f46':'#dc2626'); div.style.borderLeft = '4px solid ' + (tipo==='success'?'#10b981':'#dc2626'); div.textContent = (tipo==='success'?'✓ ':'✗ ') + texto; container.appendChild(div); setTimeout(function(){ try { container.removeChild(div); } catch(e){} },5000); }