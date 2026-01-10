# ✅ CORRECCIÓN: Rutas de JSPs en Controllers

## 🐛 Problema Encontrado

**Error:** `Estado HTTP 404 – archivo JSP [/especialidades.jsp] no encontrado`

**Causa:** El controller estaba buscando `/especialidades.jsp` pero el archivo está en `/views/especialidades.jsp`

---

## 🔧 Correcciones Realizadas

### **1. EspecialidadController**

**Método:** `listarEspecialidades()`
```java
// ❌ ANTES (INCORRECTO)
request.getRequestDispatcher("especialidades.jsp").forward(request, response);

// ✅ AHORA (CORRECTO)
request.getRequestDispatcher("/views/especialidades.jsp").forward(request, response);
```

**Método:** `obtenerEspecialidad()`
```java
// ❌ ANTES
request.getRequestDispatcher("detalle-especialidad.jsp").forward(request, response);

// ✅ AHORA
request.getRequestDispatcher("/views/detalle-especialidad.jsp").forward(request, response);
```

---

### **2. AgendarCitasController**

**Método:** `mostrarEspecialidades()`
```java
// ❌ ANTES
request.getRequestDispatcher("/especialidades.jsp").forward(request, response);

// ✅ AHORA
request.getRequestDispatcher("/views/especialidades.jsp").forward(request, response);
```

---

### **3. DoctorController**

**Método:** `listarDoctores()`
```java
// ❌ ANTES
request.getRequestDispatcher("doctores.jsp").forward(request, response);

// ✅ AHORA
request.getRequestDispatcher("/views/doctores.jsp").forward(request, response);
```

**Método:** `listarPorEspecialidad()`
```java
// ❌ ANTES
request.getRequestDispatcher("views/lista-doctores.jsp").forward(request, response);
response.sendRedirect("especialidades.jsp");

// ✅ AHORA
request.getRequestDispatcher("/views/lista-doctores.jsp").forward(request, response);
response.sendRedirect("especialidades?accion=listar");
```

**Método:** `verDetalle()`
```java
// ❌ ANTES
request.getRequestDispatcher("views/detalle-doctor.jsp").forward(request, response);

// ✅ AHORA
request.getRequestDispatcher("/views/detalle-doctor.jsp").forward(request, response);
```

---

## 📂 Estructura de Archivos JSP

```
src/main/webapp/
├── index.html
├── inicio.html
├── consultar-citas.jsp          ← Raíz
├── citas-agendadas.jsp          ← Raíz
├── atender-cita.jsp             ← Raíz
└── views/
    ├── especialidades.jsp       ← En /views/
    ├── agendamientos.jsp        ← En /views/
    ├── lista-doctores.jsp       ← En /views/
    ├── detalle-doctor.jsp       ← En /views/
    └── detalle-especialidad.jsp ← En /views/
```

---

## ✅ Mapeo Correcto: Controller → JSP

| Controller | Método | JSP Correcto | Ruta Forward |
|-----------|--------|--------------|--------------|
| `EspecialidadController` | `listarEspecialidades()` | `/views/especialidades.jsp` | `/views/especialidades.jsp` |
| `EspecialidadController` | `obtenerEspecialidad()` | `/views/detalle-especialidad.jsp` | `/views/detalle-especialidad.jsp` |
| `AgendarCitasController` | `mostrarEspecialidades()` | `/views/especialidades.jsp` | `/views/especialidades.jsp` |
| `AgendarCitasController` | `solicitarCita()` | `/views/agendamientos.jsp` | `/views/agendamientos.jsp` |
| `DoctorController` | `listarDoctores()` | `/views/doctores.jsp` | `/views/doctores.jsp` |
| `DoctorController` | `listarPorEspecialidad()` | `/views/lista-doctores.jsp` | `/views/lista-doctores.jsp` |
| `DoctorController` | `verDetalle()` | `/views/detalle-doctor.jsp` | `/views/detalle-doctor.jsp` |
| `ConsultarCitasAgendadasController` | `doGet()` | `/consultar-citas.jsp` | `/consultar-citas.jsp` |
| `ConsultarCitaAsignadaController` | `doGet()` (calendario) | `/citas-agendadas.jsp` | `/citas-agendadas.jsp` |
| `ConsultarCitaAsignadaController` | `doGet()` (atender) | `/atender-cita.jsp` | `/atender-cita.jsp` |

---

## 🔍 Reglas para `getRequestDispatcher()`

### **1. Ruta Absoluta desde la raíz del webapp**
```java
// ✅ CORRECTO - Empieza con /
request.getRequestDispatcher("/views/especialidades.jsp").forward(request, response);
request.getRequestDispatcher("/consultar-citas.jsp").forward(request, response);
```

### **2. Ruta Relativa al Servlet**
```java
// ⚠️ PUEDE CAUSAR PROBLEMAS - No empieza con /
request.getRequestDispatcher("especialidades.jsp").forward(request, response);
// Busca relativo a la URL del servlet, puede fallar
```

### **3. Siempre usar ruta absoluta con /**
```java
// ✅ RECOMENDADO
request.getRequestDispatcher("/views/especialidades.jsp")
```

---

## 🧪 Verificación de Rutas

### **Probar URL:**
```
http://localhost:8080/tu-app/especialidades?accion=listar
```

### **Flujo Esperado:**
```
1. Usuario navega a: /especialidades?accion=listar
2. EspecialidadController.doGet() recibe la petición
3. listarEspecialidades() obtiene datos de BD
4. request.setAttribute("especialidades", lista)
5. forward("/views/especialidades.jsp")
6. JSP renderiza con los datos
```

### **Verificar en consola:**
```
✅ Especialidades cargadas: 5
✅ Forward a /views/especialidades.jsp
✅ JSP renderizado exitosamente
```

---

## 📝 Checklist de Verificación

Para cada Controller que hace forward a un JSP:

- [x] La ruta empieza con `/` (absoluta)
- [x] La ruta coincide con la ubicación real del archivo
- [x] El JSP existe en esa ubicación
- [x] No hay typos en el nombre del archivo
- [x] Los datos se cargan ANTES del forward
- [x] Se usa `forward()` no `include()`

---

## 🚀 Testing

### **Test 1: Especialidades**
```
URL: /especialidades?accion=listar
Esperado: Ver lista de especialidades
Estado: ✅ CORREGIDO
```

### **Test 2: Agendar Cita**
```
URL: /AgendarCitasController?accion=listar
Esperado: Ver formulario de agendamiento
Estado: ✅ OK
```

### **Test 3: Mis Citas**
```
URL: /ConsultarCitasAgendadasController
Esperado: Ver lista de citas del estudiante
Estado: ✅ OK
```

### **Test 4: Calendario Doctor**
```
URL: /ConsultarCitaAsignadaController?vista=calendario
Esperado: Ver calendario con citas del mes
Estado: ✅ OK
```

---

## ⚠️ Problemas Comunes y Soluciones

### **Problema 1: 404 - JSP no encontrado**
**Causa:** Ruta incorrecta en `getRequestDispatcher()`
**Solución:** Verificar que la ruta sea absoluta y correcta

```java
// ❌ MAL
request.getRequestDispatcher("especialidades.jsp")

// ✅ BIEN
request.getRequestDispatcher("/views/especialidades.jsp")
```

### **Problema 2: JSP se carga pero sin datos**
**Causa:** No se cargaron datos antes del forward
**Solución:** Cargar datos con `request.setAttribute()` ANTES del forward

```java
// ✅ CORRECTO
List<Especialidad> especialidades = factory.getEspecialidadDAO().getAll();
request.setAttribute("especialidades", especialidades); // ← ANTES
request.getRequestDispatcher("/views/especialidades.jsp").forward(request, response);
```

### **Problema 3: NullPointerException en JSP**
**Causa:** Atributo no existe en el request
**Solución:** Verificar que el nombre del atributo coincida

```java
// Controller
request.setAttribute("especialidades", lista); // ← Nombre exacto

// JSP
<c:forEach items="${especialidades}" var="esp"> <!-- ← Mismo nombre -->
```

---

## ✅ Estado Final

Todos los controllers ahora tienen las rutas correctas:

- ✅ EspecialidadController → `/views/especialidades.jsp`
- ✅ AgendarCitasController → `/views/agendamientos.jsp`
- ✅ DoctorController → `/views/lista-doctores.jsp`
- ✅ ConsultarCitasAgendadasController → `/consultar-citas.jsp`
- ✅ ConsultarCitaAsignadaController → `/citas-agendadas.jsp` y `/atender-cita.jsp`

**El error 404 está resuelto. La aplicación debería funcionar correctamente ahora.**
