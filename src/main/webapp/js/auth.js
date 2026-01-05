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
function login(email, password, rol) {
    const usuario = usuariosDB.find(u => u.email === email && u.password === password && u.rol === rol);
    
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
    // Solo remover la sesión del usuario, NO las citas
    sessionStorage.removeItem('usuarioActual');
    sessionStorage.removeItem('paginaAnterior');
    window.location.href = 'index.html';
}

// Verificar si hay sesión activa
function verificarSesion() {
    const sesion = sessionStorage.getItem('usuarioActual');
    return sesion ? JSON.parse(sesion) : null;
}

// Proteger página (redirige al login si no hay sesión)
function protegerPagina(rolesPermitidos = null) {
    const usuario = verificarSesion();
    if (!usuario) {
        window.location.href = 'index.html';
        return null;
    }
    
    // Si se especificaron roles permitidos, verificar que el usuario tenga uno de ellos
    if (rolesPermitidos && rolesPermitidos.length > 0) {
        if (!rolesPermitidos.includes(usuario.rol)) {
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
                    <a href="index.html" onclick="logout(); return false;">🚪 Cerrar Sesión</a>
                </div>
            </div>
        `;
    } else {
        // No logueado - mostrar botón de login
        authButton.className = 'login';
        authButton.innerHTML = '<a href="index.html" class="font-bold">Login</a>';
    }
}

// ===== ACTUALIZAR NAVEGACIÓN SEGÚN ROL =====
function actualizarNavegacionPorRol() {
    const usuario = verificarSesion();
    const nav = document.querySelector('nav ul');
    
    if (!nav) return;
    
    const currentPage = window.location.pathname.split('/').pop() || 'inicio.html';
    
    // Si es doctor, mostrar solo su navegación específica
    if (usuario && usuario.rol === 'doctor') {
        // Guardar el authButton antes de limpiar
        const authButton = document.getElementById('authButton');
        const authButtonClone = authButton ? authButton.cloneNode(true) : null;
        
        // Limpiar la navegación
        nav.innerHTML = '';
        
        // Agregar links de doctor
        const navLinks = [
            { href: 'inicio.html', text: 'Inicio' },
            { href: 'citas-agendadas.html', text: 'Citas Agendadas' },
            { href: 'atender-cita.html', text: 'Atender Cita' }
        ];
        
        navLinks.forEach(link => {
            const li = document.createElement('li');
            li.className = 'flex';
            
            const a = document.createElement('a');
            a.href = link.href;
            a.className = 'font-bold';
            a.textContent = link.text;
            
            if (currentPage === link.href) {
                a.style.color = 'var(--color-3)';
            }
            
            li.appendChild(a);
            nav.appendChild(li);
        });
        
        // Restaurar el authButton
        if (authButtonClone) {
            nav.appendChild(authButtonClone);
        }
        
    } else if (usuario && usuario.rol === 'estudiante') {
        // Verificar si ya tiene la navegación correcta
        const links = nav.querySelectorAll('a');
        const hasCorrectLinks = Array.from(links).some(link => 
            link.textContent.includes('Mis Citas')
        );
        
        // Si no tiene los links correctos de estudiante, actualizar
        if (!hasCorrectLinks) {
            const authButton = document.getElementById('authButton');
            const authButtonClone = authButton ? authButton.cloneNode(true) : null;
            
            nav.innerHTML = '';
            
            const navLinks = [
                { href: 'inicio.html', text: 'Inicio' },
                { href: 'especialidades.html', text: 'Especialidades' },
                { href: 'consultar-citas.html', text: 'Mis Citas' },
                { href: 'reseñas.html', text: 'Reseñas' }
            ];
            
            navLinks.forEach(link => {
                const li = document.createElement('li');
                li.className = 'flex';
                
                const a = document.createElement('a');
                a.href = link.href;
                a.className = 'font-bold';
                a.textContent = link.text;
                
                if (currentPage === link.href) {
                    a.style.color = 'var(--color-3)';
                }
                
                li.appendChild(a);
                nav.appendChild(li);
            });
            
            if (authButtonClone) {
                nav.appendChild(authButtonClone);
            }
        }
    }
}

// ===== EJECUTAR AL CARGAR CUALQUIER PÁGINA =====
document.addEventListener('DOMContentLoaded', function() {
    // Actualizar header en todas las páginas
    actualizarHeader();
    
    // Actualizar navegación según rol
    actualizarNavegacionPorRol();
    
    // Header auto-hide con detección de scroll y hover (optimizado)
    const header = document.querySelector('header');
    if (header) {
        let lastScrollTop = 0;
        let isScrolling = false;
        let lastMouseY = -1;
        const scrollThreshold = 150; // Píxeles antes de activar
        const scrollDelta = 1000; // Diferencia mínima para detectar dirección
        
        // Función optimizada de scroll con requestAnimationFrame
        function handleScroll() {
            const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
            
            // Evitar cálculos si el scroll no cambió significativamente
            if (Math.abs(scrollTop - lastScrollTop) < scrollDelta && scrollTop > scrollThreshold) {
                isScrolling = false;
                return;
            }
            
            if (scrollTop > scrollThreshold) {
                header.classList.add('scrolled');
                
                if (scrollTop > lastScrollTop + scrollDelta) {
                    // Scrolling down - ocultar header
                    header.classList.add('header-hidden');
                    document.body.classList.add('header-is-hidden');
                } else if (scrollTop < lastScrollTop - scrollDelta) {
                    // Scrolling up - mostrar header
                    header.classList.remove('header-hidden');
                    document.body.classList.remove('header-is-hidden');
                }
            } else {
                header.classList.remove('scrolled', 'header-hidden');
                document.body.classList.remove('header-is-hidden');
            }
            
            lastScrollTop = scrollTop;
            isScrolling = false;
        }
        
        // Throttle del evento scroll usando requestAnimationFrame
        window.addEventListener('scroll', function() {
            if (!isScrolling) {
                isScrolling = true;
                requestAnimationFrame(handleScroll);
            }
        }, { passive: true });
        
        // Throttle del mousemove - solo verifica cada 100ms
        let mouseThrottle;
        document.addEventListener('mousemove', function(e) {
            if (e.clientY < 80 && e.clientY !== lastMouseY) {
                lastMouseY = e.clientY;
                
                if (!mouseThrottle) {
                    header.classList.remove('header-hidden');
                    document.body.classList.remove('header-is-hidden');
                    mouseThrottle = setTimeout(() => {
                        mouseThrottle = null;
                    }, 100);
                }
            }
        }, { passive: true });
    }
    
    // Si estamos en la página de login
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        // Si ya hay sesión, redirigir al inicio
        if (verificarSesion()) {
ia            window.location.href = 'inicio.html';
            return;
        }

        // Solo agregar listener si el formulario existe
        if (loginForm) {
            loginForm.addEventListener('submit', function(e) {
                e.preventDefault();
                
                const rolElement = document.getElementById('rol');
                const emailElement = document.getElementById('email');
                const passwordElement = document.getElementById('password');
                const errorDiv = document.getElementById('errorMessage');
                
                // Verificar que todos los elementos existan
                if (!rolElement || !emailElement || !passwordElement) {
                    console.error('Elementos del formulario no encontrados');
                    return;
                }
                
                const rol = rolElement.value;
                const email = emailElement.value.trim();
                const password = passwordElement.value;
                
                // Validar que se seleccionó un rol
                if (!rol) {
                    if (errorDiv) {
                        errorDiv.textContent = 'Por favor selecciona tu rol';
                        errorDiv.classList.add('show');
                    }
                    return;
                }
                
                // Validar dominio
                if (!email.endsWith('@epn.edu.ec')) {
                errorDiv.textContent = 'Debes usar tu correo institucional (@epn.edu.ec)';
                errorDiv.classList.add('show');
                return;
            }
            
            // Intentar login
            const resultado = login(email, password, rol);
            
            if (resultado.success) {
                errorDiv.classList.remove('show');
                // Redirigir a página anterior o inicio
                const urlAnterior = sessionStorage.getItem('paginaAnterior');
                window.location.href = urlAnterior || 'inicio.html';
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