// JavaScript para la página de especialidades

document.addEventListener('DOMContentLoaded', function() {
    const menuItems = document.querySelectorAll('.menu-item');
    const especialidadCards = document.querySelectorAll('.especialidad-card');

    // Smooth scroll al hacer clic en el menú
    menuItems.forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            const targetElement = document.querySelector(targetId);
            
            if (targetElement) {
                // Remover clase active de todos
                menuItems.forEach(i => i.classList.remove('active'));
                // Agregar clase active al item clickeado
                this.classList.add('active');
                
                // Scroll suave
                targetElement.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

    // Actualizar menú activo al hacer scroll
    const observerOptions = {
        root: null,
        rootMargin: '-100px 0px -60% 0px',
        threshold: 0
    };

    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const id = entry.target.getAttribute('id');
                
                // Remover active de todos
                menuItems.forEach(item => item.classList.remove('active'));
                
                // Agregar active al correspondiente
                const activeItem = document.querySelector(`.menu-item[href="#${id}"]`);
                if (activeItem) {
                    activeItem.classList.add('active');
                }
            }
        });
    }, observerOptions);

    // Observar todas las tarjetas de especialidades
    especialidadCards.forEach(card => {
        observer.observe(card);
    });
});
