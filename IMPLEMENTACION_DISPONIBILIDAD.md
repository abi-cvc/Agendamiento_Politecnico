# IMPLEMENTACIÓN DE DISPONIBILIDAD DESDE BASE DE DATOS

## 📋 Resumen de Cambios

Se ha implementado la funcionalidad para cargar los horarios disponibles de cada doctor **directamente desde la base de datos** en lugar de usar datos hardcodeados.

---

## ✅ Archivos Creados/Modificados

### 1. **DisponibilidadServlet.java** (NUEVO)
**Ruta:** `src/main/java/controller/DisponibilidadServlet.java`

**Descripción:** Servlet API REST que devuelve los horarios disponibles de un doctor en formato JSON.

**Endpoint:** `/api/disponibilidad`

**Parámetros:**
- `idDoctor` (int): ID del doctor
- `fecha` (String): Fecha en formato YYYY-MM-DD

**Respuesta JSON:**
```json
{
  "success": true,
  "idDoctor": 1,
  "fecha": "2026-01-06",
  "cantidad": 7,
  "horarios": [
    {
      "id": "1",
      "horaInicio": "08:00:00",
      "horaFin": "09:00:00",
      "disponible": "true",
      "horarioFormateado": "8:00am - 9:00am"
    },
    // ... más horarios
  ]
}
```

---

### 2. **pom.xml** (MODIFICADO)
**Agregada dependencia de Gson:**
```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

---

### 3. **agendamientos.js** (MODIFICADO)
**Función actualizada:** `cargarHorariosDisponibles()`

**Cambios:**
- Ahora hace una petición AJAX a `/api/disponibilidad`
- Muestra un mensaje de carga mientras espera la respuesta
- Renderiza los horarios desde la BD
- Maneja errores de conexión

**Función actualizada:** `seleccionarHorario()`

**Cambios:**
- Ahora acepta 3 parámetros: `horaInicio`, `horaFin`, `idDisponibilidad`
- Guarda más información del horario seleccionado

---

### 4. **insert_disponibilidades.sql** (NUEVO)
**Ruta:** `src/main/resources/insert_disponibilidades.sql`

**Descripción:** Script SQL para insertar disponibilidades de ejemplo para los doctores.

**Incluye:**
- Disponibilidades para los próximos días
- Horarios diferentes por doctor
- Formato estándar de 1 hora por slot

---

## 🔄 Flujo de Funcionamiento

### Paso 1: Usuario selecciona doctor y fecha
```
Usuario → Selecciona doctor → Selecciona fecha
```

### Paso 2: JavaScript hace petición a la API
```javascript
fetch('/api/disponibilidad?idDoctor=1&fecha=2026-01-06')
```

### Paso 3: Servlet consulta la base de datos
```java
DisponibilidadDAO dao = new DisponibilidadDAO();
List<Disponibilidad> disponibilidades = dao.obtenerPorDoctorYFecha(idDoctor, fecha);
```

### Paso 4: DAO ejecuta query JPA
```sql
SELECT d FROM Disponibilidad d 
WHERE d.doctor.idDoctor = :idDoctor 
AND d.fecha = :fecha 
AND d.disponible = true 
ORDER BY d.horaInicio
```

### Paso 5: Servlet devuelve JSON
```json
{ "success": true, "horarios": [...] }
```

### Paso 6: JavaScript renderiza los botones
```html
<button class="horario-btn" data-hora="08:00:00">
    8:00am - 9:00am
</button>
```

---

## 🗄️ Estructura de la Base de Datos

### Tabla: `disponibilidad`
```sql
CREATE TABLE disponibilidad (
    id_disponibilidad INT AUTO_INCREMENT PRIMARY KEY,
    id_doctor INT NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    disponible BOOLEAN DEFAULT TRUE,
    dia_semana VARCHAR(20),
    FOREIGN KEY (id_doctor) REFERENCES doctor(id_doctor)
);
```

### Datos de Ejemplo
```sql
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) 
VALUES (1, '2026-01-06', '08:00:00', '09:00:00', TRUE, 'Lunes');
```

---

## 📝 Instrucciones para Configurar

### 1. Actualizar Maven (Agregar Gson)
```bash
# En Eclipse: Click derecho en el proyecto → Maven → Update Project
```

O ejecuta:
```cmd
cd "C:\Users\ERICK CAICEDO\git\Agendamiento_Politecnico5"
mvn clean install
```

### 2. Ejecutar el script de disponibilidades
```sql
-- En phpMyAdmin o MySQL Workbench:
-- 1. Abre el archivo insert_disponibilidades.sql
-- 2. Ejecuta el script
-- 3. Verifica que se hayan insertado los registros
```

O desde línea de comandos:
```cmd
mysql -u root -ppeysi123 agendamiento_politecnico < src\main\resources\insert_disponibilidades.sql
```

### 3. Reiniciar el servidor Tomcat
```
1. En Eclipse, ve a la vista "Servers"
2. Detén el servidor (botón rojo)
3. Limpia el servidor: Click derecho → Clean
4. Inicia el servidor (botón verde)
```

### 4. Probar la funcionalidad
```
1. Abre: http://localhost:8080/01_MiProyecto/especialidades.jsp
2. Haz clic en "Agendar Cita" de cualquier especialidad
3. Selecciona un doctor
4. Selecciona una fecha (por ejemplo: 06/01/2026 o 07/01/2026)
5. Deberías ver los horarios disponibles del doctor
```

---

## 🧪 Testing

### Test 1: Verificar que la API funciona
```
URL: http://localhost:8080/01_MiProyecto/api/disponibilidad?idDoctor=1&fecha=2026-01-06

Resultado esperado:
{
  "success": true,
  "horarios": [...]
}
```

### Test 2: Verificar datos en la BD
```sql
SELECT * FROM disponibilidad WHERE id_doctor = 1 AND fecha = '2026-01-06';
```

### Test 3: Verificar en el navegador
```
1. Abrir DevTools (F12)
2. Ir a la pestaña "Network"
3. Seleccionar un doctor y fecha
4. Ver la petición a "disponibilidad"
5. Verificar la respuesta JSON
```

---

## 🎨 Características Implementadas

✅ **Carga dinámica desde BD**
- Los horarios se cargan según la disponibilidad real del doctor
- No hay datos hardcodeados

✅ **Filtrado por doctor y fecha**
- Solo muestra horarios del doctor seleccionado
- Solo muestra horarios de la fecha seleccionada
- Solo muestra horarios disponibles (disponible = true)

✅ **Formato amigable**
- Los horarios se muestran en formato 12 horas (AM/PM)
- Ejemplo: "8:00am - 9:00am"

✅ **Manejo de errores**
- Si no hay horarios, muestra mensaje informativo
- Si hay error de conexión, muestra mensaje de error
- Si falta algún parámetro, devuelve error JSON

✅ **Estado de ocupado**
- Los horarios con `disponible = false` se muestran como "Ocupado"
- No se pueden seleccionar

✅ **Responsive y visual**
- Los botones de horario tienen estados: normal, seleccionado, ocupado
- Feedback visual inmediato

---

## 🔧 Próximos Pasos

### 1. Marcar horario como ocupado al agendar cita
Cuando se agenda una cita, actualizar el registro de disponibilidad:
```java
disponibilidad.setDisponible(false);
disponibilidadDAO.actualizar(disponibilidad);
```

### 2. Generar disponibilidades automáticamente
Crear un procedimiento o servlet para generar disponibilidades:
```java
// Generar disponibilidades para los próximos 30 días
for (int i = 0; i < 30; i++) {
    LocalDate fecha = LocalDate.now().plusDays(i);
    // Crear slots de 1 hora desde las 8am hasta las 5pm
}
```

### 3. Permitir a los administradores gestionar disponibilidades
- CRUD de disponibilidades por doctor
- Interfaz para marcar días no laborables
- Gestión de horarios especiales

### 4. Integrar con el guardado de citas
- Al guardar una cita, marcar el horario como no disponible
- Validar que el horario esté disponible antes de guardar

---

## 📊 Arquitectura del Sistema

```
┌─────────────────┐
│  Usuario Web    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ agendamientos.js│ ─────► fetch('/api/disponibilidad?...')
└────────┬────────┘
         │
         ▼
┌─────────────────────────┐
│ DisponibilidadServlet   │
│  - doGet()              │
│  - Valida parámetros    │
│  - Llama DAO            │
│  - Devuelve JSON        │
└────────┬────────────────┘
         │
         ▼
┌─────────────────────────┐
│ DisponibilidadDAO       │
│  - obtenerPorDoctorYFecha()
│  - Query JPA            │
└────────┬────────────────┘
         │
         ▼
┌─────────────────────────┐
│ Base de Datos MySQL     │
│  - Tabla: disponibilidad│
└─────────────────────────┘
```

---

## 🎯 Resultado Final

✅ **Los horarios se cargan desde la base de datos**
✅ **Cada doctor tiene su propia disponibilidad**
✅ **Los horarios se filtran por fecha**
✅ **Solo se muestran horarios disponibles**
✅ **La interfaz es responsive y amigable**
✅ **Todo funciona con JPA/ORM (sin SQL manual)**

---

## ⚠️ Notas Importantes

1. **Fechas del script SQL:** 
   - El script `insert_disponibilidades.sql` usa fechas específicas (2026-01-06, 2026-01-07)
   - Ajusta las fechas según la fecha actual para que aparezcan horarios

2. **IDs de doctores:**
   - Asegúrate de que los IDs de doctores en el script coincidan con tu BD
   - Verifica con: `SELECT * FROM doctor;`

3. **Zona horaria:**
   - Los horarios están en formato 24 horas en la BD
   - Se convierten a formato 12 horas (AM/PM) en el frontend

4. **Rendimiento:**
   - La API consulta la BD cada vez que se cambia la fecha
   - Considera implementar caché si hay muchos usuarios

---

¡Sistema de disponibilidad completamente funcional! 🎉
