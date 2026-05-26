// ===== JavaScript para mejoras de UX en panel admin (sin AJAX) =====

document.addEventListener('DOMContentLoaded', function() {
    
    // ===== AUTO-OCULTAR MENSAJES =====
    const mensajes = document.querySelectorAll('.mensaje');
    mensajes.forEach(mensaje => {
        setTimeout(() => {
            mensaje.style.transition = 'opacity 0.5s';
            mensaje.style. opacity = '0';
            setTimeout(() => mensaje.remove(), 500);
        }, 5000); // 5 segundos
    });
    
    // ===== VALIDACIÓN EN TIEMPO REAL DEL CAMPO "NOMBRE" =====
    const nombreInput = document.getElementById('nombre');
    if (nombreInput) {
        nombreInput.addEventListener('input', function() {
            // Convertir a minúsculas y reemplazar espacios por guiones
            this.value = this.value
                .toLowerCase()
                .replace(/\s+/g, '-')
                .replace(/[^a-z-]/g, ''); // Solo minúsculas y guiones
        });
    }
    
    // ===== CONTADOR DE CARACTERES PARA DESCRIPCIÓN =====
    const descripcionTextarea = document.getElementById('descripcion');
    if (descripcionTextarea) {
        const counterDiv = document.createElement('small');
        counterDiv.style.float = 'right';
        counterDiv.style.color = '#6c757d';
        descripcionTextarea.parentNode.appendChild(counterDiv);
        
        function updateCounter() {
            const length = descripcionTextarea.value.length;
            counterDiv.textContent = `${length}caracteres`;
            
            // Cambiar color según longitud
            if (length < 50) {
                counterDiv.style.color = '#dc3545'; // Rojo
            } else if (length < 100) {
                counterDiv.style.color = '#ffc107'; // Amarillo
            } else {
                counterDiv.style.color = '#28a745'; // Verde
            }
        }
        
        descripcionTextarea.addEventListener('input', updateCounter);
        updateCounter();
    }
    
    // ===== VALIDAR FORMATO DE SERVICIOS (con pipes |) =====
    const serviciosTextarea = document.getElementById('servicios');
    if (serviciosTextarea) {
        const helperDiv = document.createElement('small');
        helperDiv.style.float = 'right';
        helperDiv.style.color = '#6c757d';
        serviciosTextarea.parentNode.appendChild(helperDiv);
        
        function updateServiciosHelper() {
            const text = serviciosTextarea.value;
            const servicios = text.split('|').filter(s => s.trim());
            const count = servicios.length;
            
            helperDiv.textContent = `${count}servicio${count !== 1 ? 's' : ''}`;
            
            if (count < 3) {
                helperDiv.style.color = '#ffc107'; // Amarillo
            } else {
                helperDiv.style.color = '#28a745'; // Verde
            }
        }
        
        serviciosTextarea.addEventListener('input', updateServiciosHelper);
        updateServiciosHelper();
    }
    
    // ===== PREVIEW DE ÍCONO EMOJI =====
    const iconoInput = document.getElementById('icono');
    if (iconoInput) {
        const previewDiv = document.createElement('div');
        previewDiv.style.marginTop = '0.5rem';
        previewDiv.style.fontSize = '2rem';
        iconoInput.parentNode.appendChild(previewDiv);
        
        function updateIconoPreview() {
            const icono = iconoInput.value.trim();
            if (icono) {
                previewDiv.textContent = `Vista previa:${icono}`;
            } else {
                previewDiv.textContent = '';
            }
        }
        
        iconoInput.addEventListener('input', updateIconoPreview);
        updateIconoPreview();
    }
});