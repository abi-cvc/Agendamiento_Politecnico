# 🔐 CREDENCIALES TEMPORALES - AUTH HARDCODEADO

## ✅ Sistema de Autenticación Temporal Activo

El sistema ahora usa `auth-temporal.js` con usuarios **hardcodeados** (sin base de datos).

---

## 👥 USUARIOS DISPONIBLES

### 📚 ESTUDIANTES

```
1. Carol Velasquez
   Email: carol.velasquez@epn.edu.ec
   Password: 123456
   Rol: estudiante

2. Erick Caicedo
   Email: erick.caicedo@epn.edu.ec
   Password: 123456
   Rol: estudiante

3. Belen Cholango
   Email: belen.cholango@epn.edu.ec
   Password: 123456
   Rol: estudiante

4. Nohemy Llumiquinga
   Email: nohemy.llumiquinga@epn.edu.ec
   Password: 123456
   Rol: estudiante
```

### 👨‍⚕️ DOCTORES

```
5. Dr. Roberto García (Nutrición)
   Email: doctor.nutricion@epn.edu.ec
   Password: doc123
   Rol: doctor
   Especialidad: nutricion

6. Dra. Ana Martínez (Odontología)
   Email: doctor.odontologia@epn.edu.ec
   Password: doc123
   Rol: doctor
   Especialidad: odontologia

7. Dr. Luis Fernández (Psicología)
   Email: doctor.psicologia@epn.edu.ec
   Password: doc123
   Rol: doctor
   Especialidad: psicologia

8. Dra. María Sánchez (Medicina General)
   Email: doctor.medicina@epn.edu.ec
   Password: doc123
   Rol: doctor
   Especialidad: medicina-general

9. Enf. Patricia Ruiz (Enfermería)
   Email: doctor.enfermeria@epn.edu.ec
   Password: doc123
   Rol: doctor
   Especialidad: enfermeria
```

### 🔑 ADMINISTRADOR

```
10. Admin Bienestar
    Email: admin@epn.edu.ec
    Password: admin123
    Rol: admin
```

---

## 🚀 CÓMO USAR

### 1. Abrir el sistema
```
http://localhost:8080/01_MiProyecto/index.html
```

### 2. Seleccionar rol
- Estudiante
- Doctor
- Administrador

### 3. Ingresar credenciales
**Ejemplo como estudiante:**
- Email: `carol.velasquez@epn.edu.ec`
- Password: `123456`
- Rol: `Estudiante`

### 4. Click "Iniciar Sesión"
✅ Redirige a `inicio.html`

---

## 📝 ARCHIVOS ACTUALIZADOS

```
✅ index.html → Usa auth-temporal.js
✅ especialidades.jsp → Usa auth-temporal.js
✅ agendamientos.jsp → Usa auth-temporal.js
✅ auth-temporal.js → Sistema temporal activo
```

---

## 🔍 VERIFICAR EN CONSOLA

Abre la consola del navegador (F12) y verás:

```
✅ Auth temporal disponible - Usuarios hardcodeados listos
🚀 Auth temporal cargado
📝 Formulario de login encontrado
```

Al hacer login exitoso:
```
🔐 Login temporal - Buscando usuario: carol.velasquez@epn.edu.ec rol: estudiante
✅ Login temporal exitoso: Carol Velasquez
```

---

## ⚡ CAMBIAR ENTRE AUTH TEMPORAL Y AUTH BD

### Para usar auth temporal (actual):
```html
<script src="js/auth-temporal.js"></script>
```

### Para volver a auth con BD:
```html
<script src="js/auth.js"></script>
```

---

## 🎯 VENTAJAS DEL AUTH TEMPORAL

✅ **No requiere base de datos**
✅ **Login instantáneo**
✅ **Usuarios pre-configurados**
✅ **Perfecto para desarrollo y pruebas**
✅ **Fácil de activar/desactivar**

---

## ⚠️ NOTA IMPORTANTE

Este sistema es **TEMPORAL** y solo para desarrollo/pruebas.

**NO usar en producción** porque:
- ❌ Contraseñas en texto plano
- ❌ Usuarios hardcodeados
- ❌ Sin persistencia en servidor
- ❌ Sesión solo en sessionStorage

Para producción, usar `LoginServlet.java` con BD y encriptación.

---

## 🧪 PRUEBA RÁPIDA

```
1. Ctrl + F5 (limpiar caché)
2. Ir a: http://localhost:8080/01_MiProyecto/index.html
3. Seleccionar: Estudiante
4. Email: carol.velasquez@epn.edu.ec
5. Password: 123456
6. Click "Iniciar Sesión"
7. ✅ Debe funcionar inmediatamente
```

---

¡Sistema temporal listo para usar! 🎉
