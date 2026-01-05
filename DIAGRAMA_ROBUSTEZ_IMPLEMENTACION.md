# IMPLEMENTACIÓN SEGÚN DIAGRAMA DE ROBUSTEZ

## 📋 Mapeo del Flujo Básico

### Según el diagrama proporcionado:

```
┌──────────────────────────────────────────────────────────────┐
│ FLUJO BÁSICO (Diagrama de Robustez)                         │
└──────────────────────────────────────────────────────────────┘

1. El estudiante solicita agendar una cita
2. El sistema presenta la lista de especialidades con nombre, 
   imagen, descripción y servicios
3. El estudiante encuentra la especialidad que desea y solicita la cita
4. El sistema presenta un formulario para agendar la cita con los 
   siguientes campos a llenar: doctor, fecha y motivo de la consulta.
   Además se presenta la especialidad con su nombre (solo lectura)
5. El estudiante llena el formulario indicando el médico, la fecha, 
   agrega el motivo de consulta y solicita registrar la cita
6. El sistema muestra los horarios disponibles de acuerdo a la 
   especialidad y médico seleccionado
7. El estudiante selecciona un horario disponible y confirma
8. El sistema guarda la cita y presenta la confirmación con los 
   detalles de la cita
```

---

## 🔄 Implementación del Flujo

### 1️⃣ Estudiante solicita agendar cita

**Actor:** Estudiante
**Boundary:** especialidades.jsp
**Action:** Click en "Agendar Cita"

```html
<!-- especialidades.jsp -->
<a href="AgendarCitasController?accion=solicitarCita&especialidad=${especialidad.nombre}" 
   class="btn btn-primary">
    Agendar Cita
</a>
```

---

### 2️⃣ Sistema presenta lista de especialidades

**Control:** AgendarCitasController
**Entity:** Especialidad (ListaEspecialidades)
**Method:** `mostrarEspecialidades()`

```java
// AgendarCitasController.java - Método 3: mostrar(especialidades)
private void mostrarEspecialidades(HttpServletRequest request, HttpServletResponse response) {
    // 2: obtener(): especialidades[]
    List<Especialidad> especialidades = especialidadDAO.obtenerEspecialidades();
    request.setAttribute("especialidades", especialidades);
    request.getRequestDispatcher("/especialidades.jsp").forward(request, response);
}
```

**JSP:**
```jsp
<!-- especialidades.jsp -->
<c:forEach var="especialidad" items="${especialidades}">
    <article class="especialidad-card">
        <div class="especialidad-icon">${especialidad.icono}</div>
        <h2>${especialidad.titulo}</h2>
        <p>${especialidad.descripcion}</p>
        <!-- ... servicios ... -->
    </article>
</c:forEach>
```

---

### 3️⃣ Estudiante encuentra especialidad y solicita cita

**Actor:** Estudiante
**Boundary:** especialidades.jsp
**Control:** AgendarCitasController
**Action:** `1: agendarCita`, `4: solicitarCita(idEspecialidad)`

**URL:** `AgendarCitasController?accion=solicitarCita&especialidad=nutricion`

---

### 4️⃣ Sistema presenta formulario con especialidad bloqueada y doctores

**Control:** AgendarCitasController
**Entity:** Doctor, Especialidad
**Methods:** 
- `5: obtenerPorEspecialidad(idEspecialidad): doctores[]`
- `6: mostrar(doctores)`

```java
// AgendarCitasController.java
private void solicitarCita(HttpServletRequest request, HttpServletResponse response) {
    String nombreEspecialidad = request.getParameter("especialidad");
    
    // 2: obtener(): especialidades[]
    List<Especialidad> especialidades = especialidadDAO.obtenerEspecialidades();
    request.setAttribute("especialidades", especialidades);
    
    if (nombreEspecialidad != null && !nombreEspecialidad.trim().isEmpty()) {
        Especialidad espSeleccionada = especialidadDAO.obtenerPorNombre(nombreEspecialidad);
        
        if (espSeleccionada != null) {
            // 5: obtenerPorEspecialidad(idEspecialidad): doctores[]
            List<Doctor> doctores = doctorDAO.obtenerPorEspecialidad(espSeleccionada);
            
            request.setAttribute("especialidadSeleccionada", nombreEspecialidad);
            request.setAttribute("especialidadObj", espSeleccionada);
            request.setAttribute("doctoresDisponibles", doctores);
        }
    }
    
    // 6: mostrar(doctores)
    request.getRequestDispatcher("/views/agendamientos.jsp").forward(request, response);
}
```

**JSP:**
```jsp
<!-- agendamientos.jsp -->
<select id="especialidad" name="especialidad" required 
        <c:if test="${not empty especialidadSeleccionada}">disabled</c:if>>
    <!-- Especialidad BLOQUEADA -->
</select>

<select id="doctor" name="doctor" required>
    <!-- Solo doctores de la especialidad -->
    <c:forEach var="doc" items="${doctoresDisponibles}">
        <option value="${doc.idDoctor}">
            ${doc.nombreCompleto}
        </option>
    </c:forEach>
</select>
```

---

### 5️⃣ Estudiante llena formulario y solicita registrar

**Actor:** Estudiante
**Boundary:** agendamientos.jsp
**Action:** Submit formulario

```html
<form action="AgendarCitasController?accion=agendarCita" method="post">
    <input type="hidden" name="especialidad" value="nutricion">
    <select name="doctor">...</select>
    <input type="date" name="fecha">
    <input type="time" name="hora">
    <textarea name="motivo">...</textarea>
    <button type="submit">Agendar Cita</button>
</form>
```

---

### 6️⃣ Sistema muestra horarios disponibles

**Control:** AgendarCitasController / DisponibilidadServlet
**Entity:** Horario (Disponibilidad)
**Method:** `8: obtenerHorariosDisponiblesPorDoctor(idDoctor): horarios[]`

**API REST:**
```java
// DisponibilidadServlet.java
@WebServlet("/api/disponibilidad")
public class DisponibilidadServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        int idDoctor = Integer.parseInt(request.getParameter("idDoctor"));
        LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
        
        // 8: obtenerHorariosDisponiblesPorDoctor(idDoctor): horarios[]
        List<Disponibilidad> disponibilidades = 
            disponibilidadDAO.obtenerPorDoctorYFecha(idDoctor, fecha);
        
        // Convertir a JSON y enviar
        response.getWriter().print(gson.toJson(disponibilidades));
    }
}
```

**JavaScript:**
```javascript
// agendamientos.js
function cargarHorariosDisponibles() {
    const doctorId = document.getElementById('doctor').value;
    const fecha = document.getElementById('fecha').value;
    
    // 9: mostrar(horarios)
    fetch(`/api/disponibilidad?idDoctor=${doctorId}&fecha=${fecha}`)
        .then(response => response.json())
        .then(data => {
            // Renderizar botones de horarios
            data.horarios.forEach(horario => {
                // Crear botón por cada horario disponible
            });
        });
}
```

---

### 7️⃣ Estudiante selecciona horario y confirma

**Actor:** Estudiante
**Boundary:** agendamientos.jsp
**Action:** `10: confirmar(idHorario)`

```javascript
// agendamientos.js
function seleccionarHorario(horaInicio, horaFin, idDisponibilidad) {
    horaSeleccionada = {
        inicio: horaInicio,
        fin: horaFin,
        idDisponibilidad: idDisponibilidad
    };
    
    // Habilitar botón de agendar
    document.querySelector('.btn-agendar').disabled = false;
}
```

---

### 8️⃣ Sistema guarda cita y muestra confirmación

**Control:** AgendarCitasController
**Entity:** Cita
**Methods:** 
- `7: crear(idDoctor, fecha, motivo)`
- `11: crearCita(cita)`
- `10: confirmar(idHorario)`

```java
// AgendarCitasController.java
private void procesarAgendamiento(HttpServletRequest request, HttpServletResponse response) {
    // Obtener parámetros
    int idDoctor = Integer.parseInt(request.getParameter("doctor"));
    LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
    LocalTime hora = LocalTime.parse(request.getParameter("hora"));
    String motivo = request.getParameter("motivo");
    
    // Obtener entidades
    Especialidad especialidad = especialidadDAO.obtenerPorNombre(nombreEspecialidad);
    Doctor doctor = doctorDAO.obtenerPorId(idDoctor);
    
    // Verificar disponibilidad
    boolean disponible = disponibilidadDAO.verificarDisponibilidad(idDoctor, fecha, hora);
    
    if (!disponible) {
        // Error: horario no disponible
        return;
    }
    
    // 11: crearCita(cita)
    Cita cita = new Cita(fecha, hora, motivo, especialidad, doctor);
    
    // 7: crear(idDoctor, fecha, motivo)
    boolean creada = cita.crear(); // Valida la cita
    
    if (!creada) {
        throw new Exception("No se pudo validar la cita");
    }
    
    // Guardar en BD usando ORM
    citaDAO.guardar(cita);
    
    // 10: confirmar(idHorario)
    confirmar(request, response, cita);
}

private void confirmar(HttpServletRequest request, HttpServletResponse response, Cita cita) {
    request.setAttribute("exito", "Cita agendada exitosamente");
    request.setAttribute("cita", cita);
    
    // Redirigir a consultar citas
    response.sendRedirect(request.getContextPath() + "/consultar-citas.jsp?exito=true");
}
```

---

## 📊 Mapeo Completo de Métodos

### Boundary (Vistas JSP)
- `especialidades.jsp` → ListaEspecialidades (muestra especialidades)
- `agendamientos.jsp` → AgendarCita (formulario de agendamiento)
- `consultar-citas.jsp` → Confirmación (muestra cita agendada)

### Control (Servlets)
- `AgendarCitasController` → Controlador principal
  - `mostrarEspecialidades()` → 3: mostrar(especialidades)
  - `solicitarCita()` → 4: solicitarCita(idEspecialidad)
  - `obtenerPorEspecialidad()` → 5: obtenerPorEspecialidad()
  - `procesarAgendamiento()` → 7: crear(), 11: crearCita()
  - `confirmar()` → 10: confirmar(idHorario)

- `DisponibilidadServlet` → API de horarios
  - `doGet()` → 8: obtenerHorariosDisponiblesPorDoctor()

### Entity (Entidades JPA)
- `Especialidad` → Especialidad
- `Doctor` → Doctor
- `Disponibilidad` → Horario / HorariosDisponibles
- `Cita` → Cita, AgendarCita

### DAO (Acceso a Datos)
- `EspecialidadDAO` → 2: obtener(): especialidades[]
- `DoctorDAO` → 5: obtenerPorEspecialidad()
- `DisponibilidadDAO` → 8: obtenerHorariosDisponiblesPorDoctor()
- `CitaDAO` → 11: crearCita()

---

## 🎯 Flujo Completo

```
┌─────────────┐
│ Estudiante  │
└──────┬──────┘
       │
       │ 1: agendarCita
       ▼
┌──────────────────────────────┐
│ AgendarCitasController       │
│                              │
│ 3: mostrar(especialidades)   │◄───── 2: obtener() ──┐
└──────┬───────────────────────┘                      │
       │                                           ┌──┴───────────┐
       │ Muestra especialidades.jsp               │ Especialidad │
       ▼                                           └──────────────┘
┌─────────────┐
│ Estudiante  │
└──────┬──────┘
       │
       │ 4: solicitarCita(idEspecialidad)
       ▼
┌──────────────────────────────┐
│ AgendarCitasController       │
│                              │
│ 5: obtenerPorEspecialidad()  │◄───────────────────┐
│ 6: mostrar(doctores)         │                    │
└──────┬───────────────────────┘                 ┌──┴──────┐
       │                                         │ Doctor  │
       │ Muestra agendamientos.jsp              └─────────┘
       ▼
┌─────────────┐
│ Estudiante  │
└──────┬──────┘
       │
       │ Llena formulario (doctor, fecha, motivo)
       ▼
┌──────────────────────────────┐
│ JavaScript                   │
│                              │
│ cargarHorariosDisponibles()  │
└──────┬───────────────────────┘
       │
       │ AJAX Request
       ▼
┌──────────────────────────────┐
│ DisponibilidadServlet        │
│                              │
│ 8: obtenerHorarios...()      │◄───────────────────┐
│ 9: mostrar(horarios)         │                    │
└──────┬───────────────────────┘                 ┌──┴──────────────┐
       │                                         │ Disponibilidad  │
       │ Respuesta JSON                         └─────────────────┘
       ▼
┌─────────────┐
│ Estudiante  │
└──────┬──────┘
       │
       │ 10: confirmar(idHorario)
       ▼
┌──────────────────────────────┐
│ AgendarCitasController       │
│                              │
│ 7: crear(doctor, fecha...)   │
│ 11: crearCita(cita)          │────────────────►┌────────┐
│ 10: confirmar()              │                 │  Cita  │
└──────┬───────────────────────┘                 └────────┘
       │
       │ Redirige a consultar-citas.jsp
       ▼
┌─────────────┐
│ Estudiante  │ (Ve confirmación)
└─────────────┘
```

---

## ✅ Implementación Completada

✅ **Flujo completo según diagrama de robustez**
✅ **Especialidad bloqueada al venir del menú**
✅ **Solo doctores de la especialidad mostrados**
✅ **Horarios dinámicos desde base de datos**
✅ **Validación de disponibilidad**
✅ **Creación de cita con todas las relaciones ORM**
✅ **Confirmación al usuario**

---

## 🚀 URLs de Prueba

```
1. Ver especialidades:
   http://localhost:8080/01_MiProyecto/AgendarCitasController?accion=mostrarEspecialidades

2. Solicitar cita (con especialidad):
   http://localhost:8080/01_MiProyecto/AgendarCitasController?accion=solicitarCita&especialidad=nutricion

3. API de disponibilidad:
   http://localhost:8080/01_MiProyecto/api/disponibilidad?idDoctor=1&fecha=2026-01-06
```

---

¡Sistema implementado según el diagrama de robustez! 🎉
