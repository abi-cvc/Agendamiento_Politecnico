i# ✅ IMPLEMENTACIÓN COMPLETA: Cancelar Cita

## 📊 SEGÚN DIAGRAMA DE ROBUSTEZ

### 🔄 FLUJO IMPLEMENTADO:

```
1. Actor → cancelarCita(idCita)
   ↓
   [JavaScript muestra confirmación]
   
2. Sistema → mostrarConfirmacion
   ↓
   [Diálogo: "¿Está seguro?"]
   
3. Actor → confirmarCancelacion(idCita)
   ↓
   [Usuario confirma]
   
4. CancelarCitaController → actualizarEstadoCita(idCita)
   ↓
   [Estado cambia a "Cancelada"]
   [Agregar observación]
   
5. Controller → liberarHorario(idHorario)
   ↓
   [Disponibilidad.disponible = true]
   
6. Controller → mostrarExitoCancelar
   ↓
   [Mensaje: "✅ Cita cancelada exitosamente"]
   
7. Controller → actualizarVista
   ↓
   [Redirige a la vista de origen]
```

---

## 📁 ARCHIVOS CREADOS/MODIFICADOS

### ✅ NUEVOS:
1. **`CancelarCitaController.java`**
   - Controller principal del flujo de cancelación
   - Maneja POST con parámetros: `idCita`, `confirmar`, `from`
   - Actualiza estado de cita a "Cancelada"
   - Libera horario asociado
   - Redirige con mensaje de éxito o error

2. **`cancelar-cita.js`**
   - JavaScript para manejar confirmación
   - Muestra mensajes de éxito/error
   - Envía formulario POST al controller

### ✅ MODIFICADOS:
3. **`DisponibilidadDAO.java`**
   - ✅ `obtenerPorDoctorYFechaYHora()` - Para liberar horario

4. **`consultar-citas.jsp`**
   - ✅ Botón "Cancelar Cita" agregado
   - ✅ Solo visible si estado es "Agendada" o "Confirmada"
   - ✅ Script `cancelar-cita.js` incluido

5. **`atender-cita.jsp`**
   - ✅ Botón "Cancelar" actualizado
   - ✅ Script `cancelar-cita.js` incluido

---

## 🎯 CARACTERÍSTICAS IMPLEMENTADAS

### 1. **Validaciones**
```java
// ✅ Verificar que la cita existe
if (cita == null) {
    enviarError("Cita no encontrada");
}

// ✅ No cancelar citas ya canceladas
if ("Cancelada".equals(cita.getEstadoCita())) {
    enviarError("La cita ya está cancelada");
}

// ✅ No cancelar citas completadas
if ("Completada".equals(cita.getEstadoCita())) {
    enviarError("No se puede cancelar una cita completada");
}
```

### 2. **Actualización de Estado**
```java
// Guardar estado anterior
String estadoAnterior = cita.getEstadoCita();

// Cambiar estado
cita.setEstadoCita("Cancelada");

// Agregar observación
cita.setObservacionCita(
    (observacionPrevia != null ? observacionPrevia + "\n" : "") +
    "Cita cancelada. Estado anterior: " + estadoAnterior
);

// Actualizar en BD
citaDAO.actualizar(cita);
```

### 3. **Liberación de Horario**
```java
// Buscar disponibilidad asociada
Disponibilidad disp = disponibilidadDAO.obtenerPorDoctorYFechaYHora(
    cita.getDoctor().getIdDoctor(),
    cita.getFechaCita(),
    cita.getHoraCita()
);

if (disp != null) {
    // Marcar como disponible
    disp.setDisponible(true);
    disponibilidadDAO.actualizar(disp);
}
```

### 4. **Confirmación JavaScript**
```javascript
const confirmacion = confirm(
    '¿Está seguro que desea cancelar esta cita?\n\n' +
    'Esta acción no se puede deshacer.\n' +
    'El horario quedará disponible nuevamente.'
);

if (!confirmacion) {
    return; // Abortar
}

// Usuario confirmó → enviar form
```

### 5. **Mensajes de Éxito/Error**
```javascript
// Mensaje de éxito
if (urlParams.get('success') === 'cancelada') {
    mostrarMensaje('✅ Cita cancelada exitosamente', 'success');
}

// Mensaje de error
const errorMsg = urlParams.get('error');
if (errorMsg) {
    mostrarMensaje('❌ Error: ' + errorMsg, 'error');
}
```

---

## 🚀 CÓMO FUNCIONA

### **Desde Consultar Citas (Estudiante):**

1. Usuario ve su cita
2. Click en botón "🚫 Cancelar Cita"
3. Aparece confirmación
4. Usuario confirma
5. POST a `/CancelarCitaController?idCita=X&confirmar=true&from=consultar`
6. Controller cancela la cita
7. Redirige a `/ConsultarCitasAgendadasController?success=cancelada`
8. Muestra mensaje: "✅ Cita cancelada exitosamente"

### **Desde Atender Cita (Doctor):**

1. Doctor ve lista de citas
2. Click en botón "Cancelar"
3. Aparece confirmación
4. Doctor confirma
5. POST a `/CancelarCitaController?idCita=X&confirmar=true&from=atender`
6. Controller cancela la cita
7. Redirige a `/ConsultarCitaAsignadaController?success=cancelada`
8. Muestra mensaje: "✅ Cita cancelada exitosamente"

---

## 📊 PARÁMETROS DEL CONTROLLER

### **POST /CancelarCitaController:**

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| `idCita` | int | Sí | ID de la cita a cancelar |
| `confirmar` | String | Sí | "true" para confirmar |
| `from` | String | No | "atender" o "consultar" |

### **Respuesta:**

- **Éxito**: Redirige a vista de origen con `?success=cancelada`
- **Error**: Redirige a vista de origen con `?error=mensaje`

---

## 🎨 BOTONES EN LAS VISTAS

### **En `consultar-citas.jsp`:**
```html
<c:if test="${cita.estadoCita == 'Agendada' || cita.estadoCita == 'Confirmada'}">
    <button class="btn-cancelar-consulta" 
            onclick="cancelarCita(${cita.idCita}, 'consultar')">
        🚫 Cancelar Cita
    </button>
</c:if>
```

### **En `atender-cita.jsp`:**
```html
<c:if test="${cita.estadoCita == 'Agendada' || cita.estadoCita == 'Confirmada'}">
    <button class="btn btn-secondary btn-cancelar" 
            onclick="cancelarCita(${cita.idCita}, 'atender')">
        Cancelar
    </button>
</c:if>
```

---

## 🔍 LOGS EN CONSOLA

```
=== INICIO: CancelarCitaController ===
📋 Procesando cancelación de cita ID: 5
✅ Estado de cita actualizado a: Cancelada
✅ Horario liberado: 2026-01-15 09:00
✅ Cita cancelada exitosamente
=== FIN: CancelarCitaController ===
```

---

## ✅ CASOS DE USO CUBIERTOS

### ✅ **Caso 1: Estudiante cancela su cita**
```
Usuario: Estudiante
Vista: consultar-citas.jsp
Botón: "🚫 Cancelar Cita"
Resultado: Cita cancelada, horario liberado
```

### ✅ **Caso 2: Doctor cancela cita de un paciente**
```
Usuario: Doctor
Vista: atender-cita.jsp
Botón: "Cancelar"
Resultado: Cita cancelada, horario liberado
```

### ✅ **Caso 3: Intento de cancelar cita ya cancelada**
```
Resultado: Error → "La cita ya está cancelada"
```

### ✅ **Caso 4: Intento de cancelar cita completada**
```
Resultado: Error → "No se puede cancelar una cita completada"
```

### ✅ **Caso 5: Cita sin doctor asignado**
```
Resultado: Cita cancelada, sin liberar horario (no hay horario asociado)
```

---

## 🔐 SEGURIDAD

### **Validaciones del Controller:**
- ✅ Verificar que `idCita` sea válido
- ✅ Verificar que la cita exista
- ✅ Verificar estado actual de la cita
- ✅ Manejo de errores con try-catch

### **Cuando implementes login:**
```java
// Verificar que el usuario tenga permiso
HttpSession session = request.getSession(false);
if (session == null) {
    response.sendRedirect("/index.jsp");
    return;
}

// Verificar que sea el estudiante dueño o un doctor
Object usuario = session.getAttribute("usuario");
if (usuario instanceof Estudiante) {
    Estudiante estudiante = (Estudiante) usuario;
    if (cita.getEstudiante().getIdEstudiante() != estudiante.getIdEstudiante()) {
        enviarError("No tiene permiso para cancelar esta cita");
        return;
    }
}
```

---

## 📋 QUERY JPA EJECUTADA

### **Buscar disponibilidad:**
```sql
SELECT d FROM Disponibilidad d 
WHERE d.doctor.idDoctor = :idDoctor 
AND d.fecha = :fecha 
AND d.horaInicio <= :hora 
AND d.horaFin > :hora
```

### **Actualizar cita:**
```sql
UPDATE Cita c 
SET c.estadoCita = 'Cancelada', 
    c.observacionCita = :observacion 
WHERE c.idCita = :idCita
```

### **Actualizar disponibilidad:**
```sql
UPDATE Disponibilidad d 
SET d.disponible = true 
WHERE d.idDisponibilidad = :idDisponibilidad
```

---

## 🧪 PRUEBAS

### **Test 1: Cancelar cita agendada**
```
Estado inicial: Agendada
Acción: Cancelar
Resultado esperado: Estado = Cancelada, Horario liberado
```

### **Test 2: Cancelar cita confirmada**
```
Estado inicial: Confirmada
Acción: Cancelar
Resultado esperado: Estado = Cancelada, Horario liberado
```

### **Test 3: Intentar cancelar cita completada**
```
Estado inicial: Completada
Acción: Cancelar
Resultado esperado: Error "No se puede cancelar una cita completada"
```

### **Test 4: Verificar mensaje de éxito**
```
Acción: Cancelar cita
Resultado esperado: Mensaje verde "✅ Cita cancelada exitosamente"
Duración: 5 segundos
```

---

## 📊 ESTADO DE LA CITA DESPUÉS DE CANCELAR

| Campo | Antes | Después |
|-------|-------|---------|
| `estadoCita` | "Agendada" | "Cancelada" |
| `observacionCita` | "" | "Cita cancelada. Estado anterior: Agendada" |
| `fechaActualizacion` | fecha_anterior | LocalDateTime.now() |

### **Disponibilidad Asociada:**
| Campo | Antes | Después |
|-------|-------|---------|
| `disponible` | false | true |

---

## ✅ VENTAJAS DE ESTA IMPLEMENTACIÓN

1. ✅ **Según diagrama de robustez** - Todos los pasos implementados
2. ✅ **Confirmación obligatoria** - Previene cancelaciones accidentales
3. ✅ **Libera horario** - Otros usuarios pueden agendar
4. ✅ **Validaciones robustas** - No se pueden cancelar citas completadas
5. ✅ **Mensajes claros** - Usuario sabe qué pasó
6. ✅ **Logs detallados** - Debugging fácil
7. ✅ **Historial preservado** - Observación guarda estado anterior
8. ✅ **Funciona en ambas vistas** - Doctor y Estudiante

---

**¡Funcionalidad de Cancelar Cita completamente implementada! 🎉**
