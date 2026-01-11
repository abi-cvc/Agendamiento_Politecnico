// ===== AUTENTICACIÓN TEMPORAL CON USUARIOS HARDCODEADOS =====
// ACTUALIZADO: Redirige según el rol del usuario y permite enviar al servlet de login cuando se usa el formulario real

// ===== BASE DE DATOS SIMULADA (solo para desarrollo rápido) =====
const usuariosDB = [
    {
        id: 1,
        nombre: "Carol Velasquez",
        email: "carol.velasquez@epn.edu.ec",
        password: "123456",
        rol: "estudiante",
        idPaciente: '1725896347'
    },
    {
        id: 10,
        nombre: "Admin Bienestar",
        email: "admin@epn.edu.ec",
        password: "admin123",
        rol: "admin",
        idAdmin: 'admin001'
    }
];

// ===== FUNCIONES DE AUTENTICACIÓN =====

function loginTemporal(email, password, rol) {
    console.log('🔐 Login temporal - Buscando usuario:', email, 'rol:', rol);
    const usuario = usuariosDB.find(u => u.email === email && u.password === password && u.rol === rol);
    
    if (usuario) {
        const sesion = {
            id: usuario.id,
            nombre: usuario.nombre,
            email: usuario.email,
            rol: usuario.rol,
            idPaciente: usuario.idPaciente || null,
            idAdmin: usuario.idAdmin || null,
            loginTime: new Date().toISOString()
        };
        sessionStorage.setItem('usuarioActual', JSON.stringify(sesion));
        console.log('✅ Login temporal exitoso:', usuario.nombre);
        return { success: true, usuario: sesion };
    } else {
        console.log('❌ Login temporal fallido');
        return { success: false, mensaje: 'Correo, contraseña o rol incorrectos' };
    }
}

function logout() {
    console.log('🔓 Cerrando sesión');
    sessionStorage.removeItem('usuarioActual');
    sessionStorage.removeItem('paginaAnterior');
    // Redirigir al JSP/HTML de inicio
    window.location.href = 'index.html';
}

function verificarSesion() {
    const sesion = sessionStorage.getItem('usuarioActual');
    return sesion ? JSON.parse(sesion) : null;
}

function protegerPagina(rolesPermitidos = null) {
    const usuario = verificarSesion();
    if (!usuario) {
        console.log('⚠️ No hay sesión, redirigiendo a login');
        window.location.href = 'index.html';
        return null;
    }
    
    if (rolesPermitidos && rolesPermitidos.length > 0) {
        if (!rolesPermitidos.includes(usuario.rol)) {
            console.log('⚠️ Rol no autorizado:', usuario.rol);
            alert('Acceso no autorizado para tu rol de usuario');
            window.location.href = 'inicio.html';
            return null;
        }
    }
    
    return usuario;
}

// ===== ACTUALIZAR HEADER CON ESTADO DE SESIÓN =====
function actualizarHeader() {
    const usuario = verificarSesion();
    const authButton = document.getElementById('authButton');
    
    if (!authButton) return;
    
    if (usuario) {
        const primerNombre = usuario.nombre.split(' ')[0];
        authButton.className = 'user-logged';
        authButton.innerHTML = `
            <div class="user-menu">
                <img src="images/user.svg" alt="Usuario" class="user-avatar">
                <span class="user-name">${primerNombre}</span>
                <div class="user-dropdown">
                    <div class="dropdown-header">
                        <strong>${usuario.nombre}</strong>
                        <small>${usuario.email}</small>
                    </div>
                    <a href="index.html" onclick="logout(); return false;">🚪 Cerrar Sesión</a>
                </div>
            </div>
        `;
    } else {
        authButton.className = 'login';
        authButton.innerHTML = '<a href="index.html" class="font-bold">Login</a>';
    }
}

function togglePassword() {
    const input = document.getElementById('password');
    const icon = document.getElementById('eyeIcon');
    
    if (!input || !icon) return;
    
    if (input.type === 'password') {
        input.type = 'text';
        icon.innerHTML = '<path d="M12 7c2.76 0 5 2.24 5 5 0 .65-.13 1.26-.36 1.83l2.92 2.92c1.51-1.26 2.7-2.89 3.43-4.75-1.73-4.39-6-7.5-11-7.5-1.4 0-2.74.25-3.98.7l2.16 2.16C10.74 7.13 11.35 7 12 7zM2 4.27l2.28 2.28.46.46C3.08 8.3 1.78 10.02 1 12c1.73 4.39 6 7.5 11 7.5 1.55 0 3.03-.3 4.38-.84l.42.42L19.73 22 21 20.73 3.27 3 2 4.27zM7.53 9.8l1.55 1.55c-.05.21-.08.43-.08.65 0 1.66 1.34 3 3 3 .22 0 .44-.03.65-.08l1.55 1.55c-.67.33-1.41.53-2.2.53-2.76 0-5-2.24-5-5 0-.79.2-1.53.53-2.2zm4.31-.78l3.15 3.15.02-.16c0-1.66-1.34-3-3-3l-.17.01z"/>';
    } else {
        input.type = 'password';
        icon.innerHTML = '<path d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"/>';
    }
}

// ===== EJECUTAR AL CARGAR CUALQUIER PÁGINA =====
document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 Auth temporal cargado');
    
    actualizarHeader();
    
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        console.log('📝 Formulario de login encontrado');
        
        // Si ya hay sesión, redirigir según rol
        const sesionActual = verificarSesion();
        if (sesionActual) {
            console.log('✅ Ya hay sesión activa, redirigiendo según rol:', sesionActual.rol);
            redirigirSegunRol(sesionActual.rol);
            return;
        }

        // Si el formulario tiene action apuntando al servlet /login (JSP), no interceptar y dejar que el servidor lo procese.
        const action = loginForm.getAttribute('action');
        if (action && action.includes('/login')) {
            console.log('Formulario apunta a /login del servidor, no interceptando (usar servidor para autenticar)');
            return;
        }

        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            console.log('📤 Formulario enviado (temporal)');
            
            const rolElement = document.getElementById('rol');
            const emailElement = document.getElementById('email') || document.getElementById('identificacion');
            const passwordElement = document.getElementById('password');
            const errorDiv = document.getElementById('errorMessage');
            
            if (!rolElement || !emailElement || !passwordElement) {
                console.error('❌ Elementos del formulario no encontrados');
                return;
            }
            
            const rol = rolElement.value;
            const email = emailElement.value.trim();
            const password = passwordElement.value;
            
            console.log('Datos recibidos:', { rol, email, password: '****' });
            
            if (!rol) {
                if (errorDiv) {
                    errorDiv.textContent = 'Por favor selecciona tu rol';
                    errorDiv.classList.add('show');
                }
                return;
            }
            
            if (email && typeof email === 'string' && email.includes('@')) {
                // email-like value
                if (!email.endsWith('@epn.edu.ec')) {
                    if (errorDiv) {
                        errorDiv.textContent = 'Debes usar tu correo institucional (@epn.edu.ec)';
                        errorDiv.classList.add('show');
                    }
                    return;
                }
            }
            
            // Intentar login temporal (si el proyecto aún no usa servlet)
            const resultado = loginTemporal(email, password, rol);
            
            if (resultado.success) {
                if (errorDiv) {
                    errorDiv.classList.remove('show');
                }
                
                // Redirigir según el rol del usuario
                console.log('🏁 Redirigiendo según rol:', rol);
                redirigirSegunRol(rol);
            } else {
                if (errorDiv) {
                    errorDiv.textContent = resultado.mensaje;
                    errorDiv.classList.add('show');
                }
            }

        });
    }
});

// Añadir envío del formulario al servlet /login cuando el usuario presione submit
(function() {
    const loginForm = document.getElementById('loginForm');
    const rolSelect = document.getElementById('rol');
    const identificacion = document.getElementById('identificacion');
    const labelIdent = document.getElementById('label-identificacion');

    function ajustarPlaceholder() {
        const rol = rolSelect ? rolSelect.value : '';
        if (!identificacion) return;
        if (rol === 'estudiante') {
            labelIdent.textContent = 'Cédula';
            identificacion.placeholder = '1725896347';
        } else if (rol === 'doctor') {
            labelIdent.textContent = 'Cédula del Doctor';
            identificacion.placeholder = '1234567890';
        } else if (rol === 'admin') {
            labelIdent.textContent = 'ID Admin';
            identificacion.placeholder = 'admin001';
        } else {
            labelIdent.textContent = 'Correo institucional';
            identificacion.placeholder = 'usuario@epn.edu.ec';
        }
    }

    if (rolSelect) {
        rolSelect.addEventListener('change', ajustarPlaceholder);
        ajustarPlaceholder();
    }

    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            // Dejar que el formulario sea manejado por el servidor Java cuando todo esté completo
            // Cambiar la acción y método para enviar al servlet
            const path = window.location.pathname.split('/');
            const ctx = path.length > 1 && path[1] ? '/' + path[1] : '';
            loginForm.action = ctx + '/login';
            loginForm.method = 'POST';
            // El servlet espera parámetro 'identificacion' y 'password' y 'rol'
            // Dejar que el navegador envíe el POST normalmente
        });
    }
})();

/**
 * Redirige al usuario a la página correspondiente según su rol
 */
function redirigirSegunRol(rol) {
    switch(rol) {
        case 'admin':
            console.log('👤 Admin detectado, redirigiendo a inicio-admin.jsp');
            window.location.href = 'inicio-admin.jsp';
            break;
        case 'doctor':
            console.log('👨‍⚕️ Doctor detectado, redirigiendo a inicio-doctor.html');
            // Si no tienes página de doctor aún, redirige a inicio.html
            window.location.href = 'inicio.html';
            break;
        case 'estudiante':
        default:
            console.log('👨‍🎓 Estudiante detectado, redirigiendo a inicio.html');
            window.location.href = 'inicio.html';
            break;
    }
}

console.log('✅ Auth temporal disponible - Usuarios hardcodeados listos');