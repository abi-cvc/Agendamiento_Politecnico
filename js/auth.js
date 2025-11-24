// ===== BASE DE DATOS SIMULADA =====
const usuariosDB = [
    {
        id: 1,
        nombre: "Carol Velasquez",
        email: "carol.velasquez@epn.edu.ec",
        password: "123456",
        carrera: "Ingeniería en Sistemas",
        rol: "estudiante"
    },
    {
        id: 2,
        nombre: "Erick Caicedo",
        email: "erick.caicedo@epn.edu.ec",
        password: "123456",
        carrera: "Ingeniería en Sistemas",
        rol: "estudiante"
    },
    {
        id: 3,
        nombre: "Belen Cholango",
        email: "belen.cholango@epn.edu.ec",
        password: "123456",
        carrera: "Ingeniería en Sistemas",
        rol: "estudiante"
    },
    {
        id: 4,
        nombre: "Nohemy Llumiquinga",
        email: "nohemy.llumiquinga@epn.edu.ec",
        password: "123456",
        carrera: "Ingeniería en Sistemas",
        rol: "estudiante"
    },
    // Doctores por especialidad
    {
        id: 5,
        nombre: "Dr. Roberto García",
        email: "doctor.nutricion@epn.edu.ec",
        password: "doc123",
        carrera: "Nutrición",
        rol: "doctor",
        especialidad: "nutricion"
    },
    {
        id: 6,
        nombre: "Dra. Ana Martínez",
        email: "doctor.odontologia@epn.edu.ec",
        password: "doc123",
        carrera: "Odontología",
        rol: "doctor",
        especialidad: "odontologia"
    },
    {
        id: 7,
        nombre: "Dr. Luis Fernández",
        email: "doctor.psicologia@epn.edu.ec",
        password: "doc123",
        carrera: "Psicología",
        rol: "doctor",
        especialidad: "psicologia"
    },
    {
        id: 8,
        nombre: "Dra. María Sánchez",
        email: "doctor.medicina@epn.edu.ec",
        password: "doc123",
        carrera: "Medicina General",
        rol: "doctor",
        especialidad: "medicina-general"
    },
    {
        id: 9,
        nombre: "Enf. Patricia Ruiz",
        email: "doctor.enfermeria@epn.edu.ec",
        password: "doc123",
        carrera: "Enfermería",
        rol: "doctor",
        especialidad: "enfermeria"
    },
    // Administrador
    {
        id: 10,
        nombre: "Admin Bienestar",
        email: "admin@epn.edu.ec",
        password: "admin123",
        carrera: "Administración",
        rol: "admin"
    }
];

// ===== FUNCIONES DE AUTENTICACIÓN =====

// Iniciar sesión
function login(email, password) {
    const usuario = usuariosDB.find(u => u.email === email && u.password === password);
    
    if (usuario) {
        // Guardar sesión (sin la contraseña)
        const sesion = {
            id: usuario.id,
            nombre: usuario.nombre,
            email: usuario.email,
            carrera: usuario.carrera,
            rol: usuario.rol,
            especialidad: usuario.especialidad || null,
            loginTime: new Date().toISOString()
        };
        sessionStorage.setItem('usuarioActual', JSON.stringify(sesion));
        return { success: true, usuario: sesion };
    } else {
        return { success: false, mensaje: 'Correo o contraseña incorrectos' };
    }
}

// Cerrar sesión
function logout() {
    sessionStorage.removeItem('usuarioActual');
    window.location.href = 'index.html';
}

// Verificar si hay sesión activa
function verificarSesion() {
    const sesion = sessionStorage.getItem('usuarioActual');
    return sesion ? JSON.parse(sesion) : null;
}

// Proteger página (redirige al login si no hay sesión)
function protegerPagina() {
    const usuario = verificarSesion();
    if (!usuario) {
        window.location.href = 'login.html';
        return null;
    }
    return usuario;
}

// ===== ACTUALIZAR HEADER CON ESTADO DE SESIÓN =====
function actualizarHeader() {
    const usuario = verificarSesion();
    const authButton = document.getElementById('authButton');
    
    if (!authButton) return;
    
    if (usuario) {
        // Usuario logueado - mostrar imagen y nombre
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
                    <a href="agendamientos.html">📅 Mis Citas</a>
                    <a href="#" onclick="logout(); return false;">🚪 Cerrar Sesión</a>
                </div>
            </div>
        `;
    } else {
        // No logueado - mostrar botón de login
        authButton.className = 'login';
        authButton.innerHTML = '<a href="login.html" class="font-bold">Login</a>';
    }
}

// ===== EJECUTAR AL CARGAR CUALQUIER PÁGINA =====
document.addEventListener('DOMContentLoaded', function() {
    // Actualizar header en todas las páginas
    actualizarHeader();
    
    // Si estamos en la página de login
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        // Si ya hay sesión, redirigir al inicio
        if (verificarSesion()) {
            window.location.href = 'index.html';
            return;
        }

        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value;
            const errorDiv = document.getElementById('errorMessage');
            
            // Validar dominio
            if (!email.endsWith('@epn.edu.ec')) {
                errorDiv.textContent = 'Debes usar tu correo institucional (@epn.edu.ec)';
                errorDiv.classList.add('show');
                return;
            }
            
            // Intentar login
            const resultado = login(email, password);
            
            if (resultado.success) {
                errorDiv.classList.remove('show');
                // Redirigir a página anterior o inicio
                const urlAnterior = sessionStorage.getItem('paginaAnterior');
                window.location.href = urlAnterior || 'index.html';
                sessionStorage.removeItem('paginaAnterior');
            } else {
                errorDiv.textContent = resultado.mensaje;
                errorDiv.classList.add('show');
            }
        });
    }
});

// ===== MOSTRAR/OCULTAR CONTRASEÑA =====
function togglePassword() {
    const input = document.getElementById('password');
    const icon = document.getElementById('eyeIcon');
    
    if (input.type === 'password') {
        input.type = 'text';
        icon.innerHTML = '<path d="M12 7c2.76 0 5 2.24 5 5 0 .65-.13 1.26-.36 1.83l2.92 2.92c1.51-1.26 2.7-2.89 3.43-4.75-1.73-4.39-6-7.5-11-7.5-1.4 0-2.74.25-3.98.7l2.16 2.16C10.74 7.13 11.35 7 12 7zM2 4.27l2.28 2.28.46.46C3.08 8.3 1.78 10.02 1 12c1.73 4.39 6 7.5 11 7.5 1.55 0 3.03-.3 4.38-.84l.42.42L19.73 22 21 20.73 3.27 3 2 4.27zM7.53 9.8l1.55 1.55c-.05.21-.08.43-.08.65 0 1.66 1.34 3 3 3 .22 0 .44-.03.65-.08l1.55 1.55c-.67.33-1.41.53-2.2.53-2.76 0-5-2.24-5-5 0-.79.2-1.53.53-2.2zm4.31-.78l3.15 3.15.02-.16c0-1.66-1.34-3-3-3l-.17.01z"/>';
    } else {
        input.type = 'password';
        icon.innerHTML = '<path d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"/>';
    }
}