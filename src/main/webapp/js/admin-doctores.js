// ===== GESTIÓN DE DOCTORES - ADMIN =====

let doctores = [];
let especialidades = [];

document.addEventListener('DOMContentLoaded', function() {
    cargarEspecialidades();
    cargarDoctores();

    // Delegación para formularios (si existen)
    const formNuevo = document.getElementById('form-nuevo-doctor');
    if (formNuevo) formNuevo.addEventListener('submit', guardarNuevoDoctor);

    const formEditar = document.getElementById('form-editar-doctor');
    if (formEditar) formEditar.addEventListener('submit', actualizarDoctor);
});

// ===== CARGA DE DATOS =====
function cargarEspecialidades() {
    const urlPrimary = getContextPath() + '/especialidades?accion=listarAdmin&format=json';
    const urlAlt = getContextPath() + '/EspecialidadController?accion=listarAdmin&format=json';

    function handleData(data) {
        especialidades = Array.isArray(data) ? data : [];
        llenarSelectEspecialidades();
    }

    fetch(urlPrimary, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
        .then(r => r.ok ? r.json() : Promise.reject(r))
        .then(handleData)
        .catch(() => {
            // Try alternative mapping by class name
            fetch(urlAlt, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
                .then(r => r.ok ? r.json() : Promise.reject(r))
                .then(handleData)
                .catch(() => {
                    // fallback local only if both endpoints fail
                    especialidades = [
                        { idEspecialidad: 1, titulo: 'Nutrición' },
                        { idEspecialidad: 2, titulo: 'Odontología' },
                        { idEspecialidad: 3, titulo: 'Psicología' },
                        { idEspecialidad: 4, titulo: 'Medicina General' },
                        { idEspecialidad: 5, titulo: 'Enfermería' }
                    ];
                    llenarSelectEspecialidades();
                });
        });
}

function llenarSelectEspecialidades() {
    const select = document.getElementById('select-especialidad-nuevo');
    if (!select) return;
    select.innerHTML = '';
    const opt = document.createElement('option');
    opt.value = '';
    opt.textContent = 'Seleccionar Especialidad';
    select.appendChild(opt);

    especialidades.forEach(sp => {
        const option = document.createElement('option');
        option.value = sp.idEspecialidad || sp.id || '';
        option.textContent = sp.titulo || sp.nombre || '';
        select.appendChild(option);
    });
}

function cargarDoctores() {
    const urlPrimary = getContextPath() + '/admin/doctores?format=json&_=' + Date.now();
    const urlAlt = getContextPath() + '/DoctorAdminController?format=json&_=' + Date.now();

    function handleData(data) {
        console.log('Cargando doctores desde servidor, cantidad=', Array.isArray(data) ? data.length : 0);
        doctores = Array.isArray(data) ? data : [];
        renderizarDoctores();
    }

    fetch(urlPrimary, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
        .then(r => r.ok ? r.json() : Promise.reject(r))
        .then(handleData)
        .catch(err => {
            console.warn('Fallo al obtener doctores desde', urlPrimary, err);
            // Try alternate mapping by class name
            fetch(urlAlt, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
                .then(r => r.ok ? r.json() : Promise.reject(r))
                .then(handleData)
                .catch(err2 => {
                    console.error('Fallo al obtener doctores desde', urlAlt, err2);
                    // fallback local only if both endpoints fail
                    doctores = [
                        { idDoctor: 1, cedula: '1725896347', nombre: 'Juan', apellido: 'Pérez', email: 'juan.perez@epn.edu.ec', telefono: '0998765432', especialidad: { titulo: 'Medicina General' }, activo: true },
                        { idDoctor: 2, cedula: '1712345678', nombre: 'María', apellido: 'González', email: 'maria.gonzalez@epn.edu.ec', telefono: '0987654321', especialidad: { titulo: 'Nutrición' }, activo: true }
                    ];
                    renderizarDoctores();
                });
        });
}

// ===== RENDERIZADO =====
function renderizarDoctores(filtro = null) {
    const tbody = document.getElementById('doctores-tbody');
    if (!tbody) return;
    tbody.innerHTML = '';

    const lista = filtro || doctores;
    if (!lista || lista.length === 0) {
        const tr = document.createElement('tr');
        const td = document.createElement('td');
        td.colSpan = 8;
        td.style.textAlign = 'center';
        td.style.padding = '40px';
        td.style.color = 'var(--color-8)';
        td.textContent = 'No se encontraron doctores';
        tr.appendChild(td);
        tbody.appendChild(tr);
        return;
    }

    lista.forEach(doc => {
        const tr = document.createElement('tr');

        const tdCed = document.createElement('td'); tdCed.textContent = doc.cedula || '';
        const tdNom = document.createElement('td'); tdNom.textContent = doc.nombre || '';
        const tdApe = document.createElement('td'); tdApe.textContent = doc.apellido || '';
        const tdEmail = document.createElement('td'); tdEmail.textContent = doc.email || '';
        const tdTel = document.createElement('td'); tdTel.textContent = doc.telefono || '';
        const tdEsp = document.createElement('td'); tdEsp.textContent = (doc.especialidad && (doc.especialidad.titulo || doc.especialidad.nombre)) ? (doc.especialidad.titulo || doc.especialidad.nombre) : '';

        const tdEstado = document.createElement('td');
        const spanEstado = document.createElement('span');
        spanEstado.className = 'estado-badge ' + (doc.activo ? 'estado-activo' : 'estado-inactivo');
        spanEstado.textContent = doc.activo ? 'Activo' : 'Inactivo';
        tdEstado.appendChild(spanEstado);

        const tdAcc = document.createElement('td');
        const divAcc = document.createElement('div'); divAcc.className = 'btn-actions';

        const btnEditar = document.createElement('button');
        btnEditar.className = 'btn btn-sm btn-warning';
        btnEditar.type = 'button';
        btnEditar.textContent = 'Editar';
        btnEditar.addEventListener('click', function() { abrirModalEditarById(doc.idDoctor || doc.id); });

        const btnEstado = document.createElement('button');
        btnEstado.className = 'btn btn-sm ' + (doc.activo ? 'btn-danger' : 'btn-primary');
        btnEstado.type = 'button';
        btnEstado.textContent = doc.activo ? 'Desactivar' : 'Activar';
        btnEstado.addEventListener('click', function() { cambiarEstado(doc.idDoctor || doc.id); });

        divAcc.appendChild(btnEditar);
        divAcc.appendChild(btnEstado);
        tdAcc.appendChild(divAcc);

        tr.appendChild(tdCed);
        tr.appendChild(tdNom);
        tr.appendChild(tdApe);
        tr.appendChild(tdEmail);
        tr.appendChild(tdTel);
        tr.appendChild(tdEsp);
        tr.appendChild(tdEstado);
        tr.appendChild(tdAcc);

        tbody.appendChild(tr);
    });
}

function abrirModalEditarById(id) {
    const doctor = doctores.find(d => (d.idDoctor || d.id || 0) === id);
    if (!doctor) { console.warn('Doctor no encontrado', id); return; }
    abrirModalEditar(doctor);
}

function abrirModalNuevo() {
    const form = document.getElementById('form-nuevo-doctor');
    if (form) form.reset();
    const modal = document.getElementById('modalNuevo'); if (modal) modal.classList.add('active');
}

function abrirModalEditar(doctor) {
    const idInput = document.getElementById('edit-id'); if (idInput) idInput.value = doctor.idDoctor || doctor.id || '';
    const ced = document.getElementById('edit-cedula'); if (ced) ced.value = doctor.cedula || '';
    const nom = document.getElementById('edit-nombre'); if (nom) nom.value = doctor.nombre || '';
    const ape = document.getElementById('edit-apellido'); if (ape) ape.value = doctor.apellido || '';
    const email = document.getElementById('edit-email'); if (email) email.value = doctor.email || '';
    const tel = document.getElementById('edit-telefono'); if (tel) tel.value = doctor.telefono || '';
    const foto = document.getElementById('edit-foto'); if (foto) foto.value = doctor.foto || '';
    const desc = document.getElementById('edit-descripcion'); if (desc) desc.value = doctor.descripcion || '';

    const modal = document.getElementById('modalEditar'); if (modal) modal.classList.add('active');
}

function cerrarModal(id) { const modal = document.getElementById(id); if (modal) modal.classList.remove('active'); }

// Helper to compute context path (e.g., /01_MiProyecto)
function getContextPath() {
    const path = window.location.pathname;
    const parts = path.split('/');
    // parts[0] is empty string, parts[1] is context
    return parts.length > 1 && parts[1] ? '/' + parts[1] : '';
}

// ===== GUARDAR NUEVO DOCTOR =====
function guardarNuevoDoctor(e) {
    if (e && e.preventDefault) e.preventDefault();
    const form = e ? e.target : document.getElementById('form-nuevo-doctor');
    if (!form) return false;

    const fd = new FormData(form);
    // Enviar accion al servlet
    fd.append('accion', 'crear');

    const urlPrimary = getContextPath() + '/admin/doctores';
    const urlAlt = getContextPath() + '/DoctorAdminController';

    fetch(urlPrimary, {
        method: 'POST',
        body: fd,
        headers: { 'X-Requested-With': 'XMLHttpRequest' }
    }).then(resp => {
        if (!resp.ok) return Promise.reject(resp);
        return resp.text();
    }).then(() => {
        cargarDoctores();
        cerrarModal('modalNuevo');
        mostrarMensaje('Doctor creado exitosamente', 'success');
    }).catch(err => {
        console.warn('Crear doctor falló en', urlPrimary, err, 'intentando alternativa');
        // try alternate
        fetch(urlAlt, { method: 'POST', body: fd, headers: { 'X-Requested-With': 'XMLHttpRequest' } })
            .then(r => { if (!r.ok) return Promise.reject(r); return r.text(); })
            .then(() => {
                cargarDoctores();
                cerrarModal('modalNuevo');
                mostrarMensaje('Doctor creado exitosamente', 'success');
            }).catch(err2 => {
                console.error('Error creando doctor en ambas rutas', err2);
                mostrarMensaje('Error al crear doctor', 'error');
            });
    });

    return false;
}

// ===== ACTUALIZAR DOCTOR =====
function actualizarDoctor(e) {
    if (e && e.preventDefault) e.preventDefault();
    const form = e ? e.target : document.getElementById('form-editar-doctor');
    if (!form) return false;
    const fd = new FormData(form);
    fd.append('accion', 'actualizar');

    const urlPrimary = getContextPath() + '/admin/doctores';
    const urlAlt = getContextPath() + '/DoctorAdminController';

    fetch(urlPrimary, {
        method: 'POST',
        body: fd,
        headers: { 'X-Requested-With': 'XMLHttpRequest' }
    }).then(resp => { if (!resp.ok) return Promise.reject(resp); return resp.text(); })
    .then(() => {
        cargarDoctores();
        cerrarModal('modalEditar');
        mostrarMensaje('Doctor actualizado exitosamente', 'success');
    }).catch(err => {
        console.warn('Actualizar doctor falló en', urlPrimary, err, 'intentando alternativa');
        fetch(urlAlt, { method: 'POST', body: fd, headers: { 'X-Requested-With': 'XMLHttpRequest' } })
            .then(r => { if (!r.ok) return Promise.reject(r); return r.text(); })
            .then(() => { cargarDoctores(); cerrarModal('modalEditar'); mostrarMensaje('Doctor actualizado exitosamente', 'success'); })
            .catch(err2 => { console.error('Error actualizando doctor en ambas rutas', err2); mostrarMensaje('Error al actualizar doctor', 'error'); });
    });

    return false;
}

// ===== CAMBIAR ESTADO =====
function cambiarEstado(id) {
    const doctor = doctores.find(d => (d.idDoctor || d.id || 0) === id);
    if (!doctor) return;
    const accionLocal = doctor.activo ? 'desactivar' : 'activar';
    if (!confirm('¿Está seguro de ' + accionLocal + ' a este doctor?')) return;

    // Preparar request al servlet
    const fd = new FormData();
    fd.append('accion', 'cambiarEstado');
    fd.append('id', id);

    const urlPrimary = getContextPath() + '/admin/doctores';
    const urlAlt = getContextPath() + '/DoctorAdminController';

    fetch(urlPrimary, { method: 'POST', body: fd, headers: { 'X-Requested-With': 'XMLHttpRequest' } })
        .then(r => { if (!r.ok) return Promise.reject(r); return r.text(); })
        .then(() => { cargarDoctores(); mostrarMensaje('Estado del doctor actualizado', 'success'); })
        .catch(err => {
            console.warn('Cambio de estado falló en', urlPrimary, err, 'intentando alternativa');
            fetch(urlAlt, { method: 'POST', body: fd, headers: { 'X-Requested-With': 'XMLHttpRequest' } })
                .then(r => { if (!r.ok) return Promise.reject(r); return r.text(); })
                .then(() => { cargarDoctores(); mostrarMensaje('Estado del doctor actualizado', 'success'); })
                .catch(err2 => { console.error('Error cambiando estado en ambas rutas', err2); mostrarMensaje('Error al cambiar estado', 'error'); });
        });
}

// ===== MENSAJES =====
function mostrarMensaje(texto, tipo) {
    const container = document.getElementById('mensaje-container');
    if (!container) return;
    const div = document.createElement('div');
    div.className = 'mensaje ' + (tipo === 'success' ? 'success' : 'error');
    div.style.padding = 'var(--spacing-md)';
    div.style.borderRadius = 'var(--radius-md)';
    div.style.marginBottom = 'var(--spacing-lg)';
    div.style.fontWeight = '500';
    div.style.background = (tipo === 'success' ? '#d1fae5' : '#fee2e2');
    div.style.color = (tipo === 'success' ? '#065f46' : '#dc2626');
    div.style.borderLeft = '4px solid ' + (tipo === 'success' ? '#10b981' : '#dc2626');
    div.textContent = (tipo === 'success' ? '✓ ' : '✗ ') + texto;
    container.appendChild(div);
    setTimeout(function() { try { container.removeChild(div); } catch (e) {} }, 5000);
}

// Export functions to global scope if needed
window.abrirModalNuevo = abrirModalNuevo;
window.abrirModalEditarById = abrirModalEditarById;
window.cerrarModal = cerrarModal;
window.buscarDoctor = function() {
    const input = document.getElementById('buscar-cedula');
    if (!input) return;
    const ced = input.value.trim();
    if (!ced) { mostrarMensaje('Por favor ingrese una cédula', 'error'); return; }
    const res = doctores.filter(d => (d.cedula || '').includes(ced));
    renderizarDoctores(res);
};

// End of file