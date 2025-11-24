// ===== PROTEGER FORMULARIO Y CARGAR DATOS =====
document.addEventListener('DOMContentLoaded', function() {
    const usuario = verificarSesion();
    
    // El formulario solo es visible si hay sesión
    const formCard = document.querySelector('.resena-form-card');
    if (!usuario) {
        formCard.innerHTML = `
            <div class="resena-form-header text-center">
                <h2>🔒 Inicia Sesión</h2>
                <p class="text-muted mb-4">Para dejar una reseña, necesitas iniciar sesión</p>
                <a href="login.html" class="btn-primary" onclick="sessionStorage.setItem('paginaAnterior', 'reseñas.html')">Ir a Login</a>
            </div>
        `;
    } else {
        // Configurar sistema de calificación
        configurarCalificacion();
        
        // Manejar envío del formulario
        document.getElementById('resenaForm').addEventListener('submit', agregarResena);
    }
    
    // Cargar reseñas existentes (visible para todos)
    cargarResenas();
    
    // Manejar filtro
    document.getElementById('filtroEspecialidad').addEventListener('change', cargarResenas);
});

// ===== CONFIGURAR SISTEMA DE CALIFICACIÓN =====
function configurarCalificacion() {
    const stars = document.querySelectorAll('.star');
    const calificacionInput = document.getElementById('calificacion');
    
    stars.forEach(star => {
        star.addEventListener('click', function() {
            const value = this.getAttribute('data-value');
            calificacionInput.value = value;
            
            // Actualizar estrellas visuales
            stars.forEach(s => {
                const starValue = s.getAttribute('data-value');
                if (starValue <= value) {
                    s.classList.add('active');
                } else {
                    s.classList.remove('active');
                }
            });
        });
        
        // Hover effect
        star.addEventListener('mouseenter', function() {
            const value = this.getAttribute('data-value');
            stars.forEach(s => {
                const starValue = s.getAttribute('data-value');
                if (starValue <= value) {
                    s.classList.add('hover');
                } else {
                    s.classList.remove('hover');
                }
            });
        });
    });
    
    // Limpiar hover al salir del contenedor
    document.getElementById('ratingInput').addEventListener('mouseleave', function() {
        stars.forEach(s => s.classList.remove('hover'));
    });
}

// ===== AGREGAR RESEÑA =====
function agregarResena(e) {
    e.preventDefault();
    
    const usuario = verificarSesion();
    const messageDiv = document.getElementById('resenaMessage');
    
    const especialidad = document.getElementById('especialidadResena');
    const calificacion = document.getElementById('calificacion').value;
    const comentario = document.getElementById('comentario').value;
    
    // Validar calificación
    if (!calificacion) {
        messageDiv.textContent = 'Por favor selecciona una calificación';
        messageDiv.className = 'form-message error show';
        setTimeout(() => messageDiv.className = 'form-message', 3000);
        return;
    }
    
    // Crear objeto de reseña
    const nuevaResena = {
        id: Date.now(),
        usuarioId: usuario.id,
        usuarioNombre: usuario.nombre,
        especialidad: especialidad.options[especialidad.selectedIndex].text,
        especialidadValue: especialidad.value,
        calificacion: parseInt(calificacion),
        comentario: comentario,
        fecha: new Date().toISOString(),
        likes: 0
    };
    
    // Guardar en sessionStorage
    let resenas = JSON.parse(sessionStorage.getItem('resenas')) || [];
    resenas.push(nuevaResena);
    sessionStorage.setItem('resenas', JSON.stringify(resenas));
    
    // Mostrar mensaje de éxito
    messageDiv.textContent = '¡Reseña publicada exitosamente!';
    messageDiv.className = 'form-message success show';
    
    // Limpiar formulario
    document.getElementById('resenaForm').reset();
    document.querySelectorAll('.star').forEach(s => s.classList.remove('active'));
    
    // Recargar lista de reseñas
    cargarResenas();
    
    // Ocultar mensaje después de 3 segundos
    setTimeout(() => {
        messageDiv.className = 'form-message';
    }, 3000);
}

// ===== CARGAR RESEÑAS =====
function cargarResenas() {
    const listaResenas = document.getElementById('listaResenas');
    const filtro = document.getElementById('filtroEspecialidad').value;
    const resenas = JSON.parse(sessionStorage.getItem('resenas')) || [];
    
    // Filtrar reseñas
    let resenasFiltradas = resenas;
    if (filtro !== 'todas') {
        resenasFiltradas = resenas.filter(r => r.especialidadValue === filtro);
    }
    
    if (resenasFiltradas.length === 0) {
        listaResenas.innerHTML = `
            <div class="no-resenas text-center">
                <p class="text-muted">No hay reseñas disponibles para esta especialidad</p>
                <p class="text-muted text-sm">¡Sé el primero en compartir tu experiencia!</p>
            </div>
        `;
        return;
    }
    
    // Ordenar por fecha (más recientes primero)
    resenasFiltradas.sort((a, b) => new Date(b.fecha) - new Date(a.fecha));
    
    // Generar HTML
    let html = '';
    resenasFiltradas.forEach(resena => {
        const fechaFormateada = formatearFechaResena(resena.fecha);
        const estrellas = generarEstrellas(resena.calificacion);
        
        html += `
            <div class="resena-card card hover-lift">
                <div class="resena-header flex-between">
                    <div>
                        <h3 class="resena-usuario">${resena.usuarioNombre}</h3>
                        <span class="resena-especialidad">${resena.especialidad}</span>
                    </div>
                    <div class="resena-rating">
                        ${estrellas}
                    </div>
                </div>
                <div class="resena-body">
                    <p class="resena-comentario text-justify">${resena.comentario}</p>
                </div>
                <div class="resena-footer flex-between">
                    <span class="resena-fecha text-muted text-sm">${fechaFormateada}</span>
                    <div class="resena-actions">
                        <button class="btn-like" onclick="darLike(${resena.id})">
                            👍 <span id="likes-${resena.id}">${resena.likes}</span>
                        </button>
                    </div>
                </div>
            </div>
        `;
    });
    
    listaResenas.innerHTML = html;
}

// ===== DAR LIKE A RESEÑA =====
function darLike(resenaId) {
    let resenas = JSON.parse(sessionStorage.getItem('resenas')) || [];
    const resena = resenas.find(r => r.id === resenaId);
    
    if (resena) {
        resena.likes += 1;
        sessionStorage.setItem('resenas', JSON.stringify(resenas));
        
        // Actualizar contador visual
        const likesSpan = document.getElementById(`likes-${resenaId}`);
        if (likesSpan) {
            likesSpan.textContent = resena.likes;
        }
    }
}

// ===== UTILIDADES =====
function generarEstrellas(calificacion) {
    let estrellas = '';
    for (let i = 1; i <= 5; i++) {
        if (i <= calificacion) {
            estrellas += '<span class="star-display active">★</span>';
        } else {
            estrellas += '<span class="star-display">★</span>';
        }
    }
    return estrellas;
}

function formatearFechaResena(fechaStr) {
    const fecha = new Date(fechaStr);
    const ahora = new Date();
    const diff = ahora - fecha;
    
    // Menos de 1 minuto
    if (diff < 60000) {
        return 'Hace un momento';
    }
    
    // Menos de 1 hora
    if (diff < 3600000) {
        const minutos = Math.floor(diff / 60000);
        return `Hace ${minutos} minuto${minutos > 1 ? 's' : ''}`;
    }
    
    // Menos de 1 día
    if (diff < 86400000) {
        const horas = Math.floor(diff / 3600000);
        return `Hace ${horas} hora${horas > 1 ? 's' : ''}`;
    }
    
    // Menos de 1 semana
    if (diff < 604800000) {
        const dias = Math.floor(diff / 86400000);
        return `Hace ${dias} día${dias > 1 ? 's' : ''}`;
    }
    
    // Formato completo
    const opciones = { year: 'numeric', month: 'long', day: 'numeric' };
    return fecha.toLocaleDateString('es-EC', opciones);
}
