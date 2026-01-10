# 🏗️ ARQUITECTURA COMPLETA: JSP → Controller → DAO Factory → JPA/ORM

## 📋 Diagrama de Robustez Implementado: Agendar Cita

Según el diagrama de robustez proporcionado, el flujo es:

### **Flujo Básico: Agendar una Cita**

1. **Estudiante** solicita agendar una cita
2. El sistema presenta la lista de **especialidades** (obteniendo desde BD)
3. **Estudiante** encuentra la especialidad y solicita la cita
4. **AgendarCitasController**: `solicitarCita(idEspecialidad)`
5. Sistema: `obtenerPorEspecialidad(idEspecialidad)` → doctores[]
6. Sistema muestra los **doctores** disponibles
7. **Estudiante** selecciona un horario disponible y confirma
8. **AgendarCitasController**: `crearCita(idDoctor, fecha, motivo)`
9. Sistema muestra los **horarios disponibles**
10. **AgendarCitasController**: `confirmar(idHorario)`
11. Sistema: `crearCita(cita)` - guarda en BD
12. Sistema presenta confirmación con los detalles de la cita

---

## 🎯 Arquitectura en Capas (Actualmente Implementada)

```
┌─────────────────────────────────────────────────────────┐
│                    CAPA DE VISTA (JSP)                  │
│  ┌─────────────────────────────────────────────────┐   │
│  │  agendamientos.jsp                              │   │
│  │  - Muestra especialidades                       │   │
│  │  - Muestra doctores disponibles                 │   │
│  │  - Formulario para agendar cita                 │   │
│  └────────────────────┬────────────────────────────┘   │
└─────────────────────────┼──────────────────────────────┘
                          │ HTTP Request (POST/GET)
                          │ /AgendarCitasController?accion=...
                          ▼
┌─────────────────────────────────────────────────────────┐
│              CAPA DE CONTROL (Servlets)                 │
│  ┌─────────────────────────────────────────────────┐   │
│  │  AgendarCitasController                         │   │
│  │  ┌──────────────────────────────────────────┐   │   │
│  │  │  init() {                                │   │   │
│  │  │    factory = DAOFactory.getFactory();    │   │   │
│  │  │  }                                       │   │   │
│  │  └──────────────────────────────────────────┘   │   │
│  │                                                  │   │
│  │  Métodos:                                        │   │
│  │  • mostrarEspecialidades()                      │   │
│  │  • solicitarCita(idEspecialidad)                │   │
│  │  • procesarAgendamiento()                       │   │
│  │  • confirmar(idHorario)                         │   │
│  └────────────────────┬────────────────────────────┘   │
└─────────────────────────┼──────────────────────────────┘
                          │ Usa DAOFactory
                          ▼
┌─────────────────────────────────────────────────────────┐
│           CAPA DE FACTORY (Patrón Factory)              │
│  ┌─────────────────────────────────────────────────┐   │
│  │  DAOFactory.getFactory()                        │   │
│  │  ├── getCitaDAO() → ICitaDAO                    │   │
│  │  ├── getDoctorDAO() → IDoctorDAO                │   │
│  │  ├── getEspecialidadDAO() → IEspecialidadDAO    │   │
│  │  └── getDisponibilidadDAO() → IDisponibilidadDAO│   │
│  └────────────────────┬────────────────────────────┘   │
└─────────────────────────┼──────────────────────────────┘
                          │ Retorna implementaciones
                          ▼
┌─────────────────────────────────────────────────────────┐
│          CAPA DE PERSISTENCIA (DAO + JPA/ORM)           │
│  ┌─────────────────────────────────────────────────┐   │
│  │  CitaDAO extends JPAGenericDAO<Cita, Integer>  │   │
│  │  ├── create(cita)      [GENÉRICO]               │   │
│  │  ├── update(cita)      [GENÉRICO]               │   │
│  │  ├── getById(id)       [GENÉRICO]               │   │
│  │  ├── getAll()          [GENÉRICO]               │   │
│  │  └── obtenerPorFecha() [ESPECÍFICO]             │   │
│  │                                                  │   │
│  │  JPAGenericDAO usa JPA EntityManager            │   │
│  │  ├── em.persist()   → INSERT                    │   │
│  │  ├── em.merge()     → UPDATE                    │   │
│  │  ├── em.find()      → SELECT by ID              │   │
│  │  └── em.createQuery → SELECT custom             │   │
│  └────────────────────┬────────────────────────────┘   │
└─────────────────────────┼──────────────────────────────┘
                          │ JPA/ORM
                          ▼
┌─────────────────────────────────────────────────────────┐
│               BASE DE DATOS (MySQL/PostgreSQL)          │
│  Tablas:                                                │
│  • cita                                                 │
│  • doctor                                               │
│  • especialidad                                         │
│  • disponibilidad                                       │
│  • estudiante                                           │
└─────────────────────────────────────────────────────────┘
```

---

## 🔄 Flujo Completo de una Petición: "Agendar Cita"

### **Paso 1: Usuario carga el formulario**

**JSP → Controller**
```
Usuario navega a: /views/agendamientos.jsp
↓
JSP ejecuta código interno para cargar datos iniciales
(NOTA: Actualmente el JSP tiene lógica - debería delegarse al controller)
```

### **Paso 2: Usuario selecciona especialidad**

**JSP → Controller → DAO → JPA → BD**

```java
// 1. JSP envía request
<form action="${pageContext.request.contextPath}/AgendarCitasController" method="GET">
    <input type="hidden" name="accion" value="solicitarCita">
    <select name="especialidad">
        <option value="psicologia">Psicología</option>
    </select>
    <button type="submit">Buscar Doctores</button>
</form>

// 2. Controller recibe la petición
@WebServlet("/AgendarCitasController")
public class AgendarCitasController extends HttpServlet {
    private DAOFactory factory;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String accion = request.getParameter("accion");
        
        if ("solicitarCita".equals(accion)) {
            solicitarCita(request, response);
        }
    }
    
    private void solicitarCita(HttpServletRequest request, HttpServletResponse response) {
        String nombreEspecialidad = request.getParameter("especialidad");
        
        // 3. Controller usa Factory para obtener DAO
        Especialidad especialidad = factory.getEspecialidadDAO()
                                          .obtenerPorNombre(nombreEspecialidad);
        
        // 4. Obtener doctores de esa especialidad
        List<Doctor> doctores = factory.getDoctorDAO()
                                      .obtenerPorEspecialidad(especialidad);
        
        // 5. Pasar datos al JSP
        request.setAttribute("doctoresDisponibles", doctores);
        request.getRequestDispatcher("/views/agendamientos.jsp")
               .forward(request, response);
    }
}

// 6. DAO usa JPA para consultar la BD
public class DoctorDAO extends JPAGenericDAO<Doctor, Integer> {
    @Override
    public List<Doctor> obtenerPorEspecialidad(Especialidad especialidad) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Doctor> query = em.createQuery(
                "SELECT d FROM Doctor d WHERE d.especialidad = :especialidad 
                 AND d.activo = true ORDER BY d.apellido",
                Doctor.class
            );
            query.setParameter("especialidad", especialidad);
            return query.getResultList(); // ← JPA/ORM mapea automáticamente
        } finally {
            em.close();
        }
    }
}

// 7. JPA ejecuta SQL
// SELECT * FROM doctor WHERE especialidad_id = ? AND activo = true ORDER BY apellido
```

### **Paso 3: Usuario agenda la cita**

**JSP → Controller → DAO → JPA → BD**

```java
// 1. JSP envía formulario con datos de la cita
<form action="${pageContext.request.contextPath}/AgendarCitasController" method="POST">
    <input type="hidden" name="accion" value="agendarCita">
    <select name="doctor" required>
        <c:forEach items="${doctoresDisponibles}" var="doc">
            <option value="${doc.idDoctor}">${doc.nombreCompleto}</option>
        </c:forEach>
    </select>
    <input type="date" name="fecha" required>
    <input type="time" name="hora" required>
    <textarea name="motivo" required></textarea>
    <button type="submit">Agendar Cita</button>
</form>

// 2. Controller procesa el agendamiento
protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    String accion = request.getParameter("accion");
    
    if ("agendarCita".equals(accion)) {
        procesarAgendamiento(request, response);
    }
}

private void procesarAgendamiento(HttpServletRequest request, HttpServletResponse response) {
    // 3. Obtener parámetros
    int idDoctor = Integer.parseInt(request.getParameter("doctor"));
    LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
    LocalTime hora = LocalTime.parse(request.getParameter("hora"));
    String motivo = request.getParameter("motivo");
    
    // 4. Obtener entidades usando Factory + DAO + JPA
    Doctor doctor = factory.getDoctorDAO().getById(idDoctor);
    Especialidad especialidad = factory.getEspecialidadDAO()
                                       .obtenerPorNombre(request.getParameter("especialidad"));
    
    // 5. Verificar disponibilidad
    boolean disponible = factory.getDisponibilidadDAO()
                               .verificarDisponibilidad(idDoctor, fecha, hora);
    
    if (!disponible) {
        request.setAttribute("error", "Horario no disponible");
        return;
    }
    
    // 6. Crear entidad Cita (objeto de dominio)
    Cita cita = new Cita(fecha, hora, motivo, especialidad, doctor);
    
    // 7. Validar con lógica de negocio
    boolean validada = cita.crear(); // ← Reglas de negocio en la entidad
    
    // 8. Persistir usando Factory + DAO + JPA
    factory.getCitaDAO().create(cita);
    // ↓ JPA ejecuta: INSERT INTO cita (fecha, hora, motivo, ...) VALUES (?, ?, ?, ...)
    
    // 9. Actualizar disponibilidad
    Disponibilidad disp = factory.getDisponibilidadDAO()
                                 .obtenerPorDoctorYFechaYHora(idDoctor, fecha, hora);
    disp.setDisponible(false);
    factory.getDisponibilidadDAO().update(disp);
    // ↓ JPA ejecuta: UPDATE disponibilidad SET disponible = false WHERE id = ?
    
    // 10. Confirmar al usuario
    confirmar(request, response, cita);
}
```

---

## 📊 Comparación: Código Actual vs Ideal

### ❌ PROBLEMA: JSP con lógica de negocio

**Archivo:** `agendamientos.jsp` (líneas 1-30)
```jsp
<%
    // ❌ MAL: JSP está instanciando DAOs directamente
    EspecialidadDAO especialidadDAO = new EspecialidadDAO();
    DoctorDAO doctorDAO = new DoctorDAO();
    
    List<Especialidad> especialidades = especialidadDAO.obtenerEspecialidades();
    request.setAttribute("especialidades", especialidades);
    
    String especialidadParam = request.getParameter("especialidad");
    if (especialidadParam != null) {
        Especialidad espSeleccionada = especialidadDAO.obtenerPorNombre(especialidadParam);
        List<Doctor> doctores = doctorDAO.obtenerPorEspecialidad(espSeleccionada);
        request.setAttribute("doctoresDisponibles", doctores);
    }
%>
```

**Problemas:**
1. ❌ JSP tiene lógica de negocio (viola MVC)
2. ❌ Instancia DAOs directamente (no usa Factory)
3. ❌ Mezcla presentación con lógica
4. ❌ Difícil de testear
5. ❌ Difícil de mantener

### ✅ SOLUCIÓN: Controller maneja toda la lógica

**JSP solo renderiza:**
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Agendar Cita</title>
</head>
<body>
    <h1>Agendar Cita Médica</h1>
    
    <!-- ✅ BIEN: Solo renderiza datos del request -->
    <form action="${pageContext.request.contextPath}/AgendarCitasController" method="GET">
        <input type="hidden" name="accion" value="solicitarCita">
        
        <select name="especialidad">
            <c:forEach items="${especialidades}" var="esp">
                <option value="${esp.nombre}">${esp.titulo}</option>
            </c:forEach>
        </select>
        
        <button type="submit">Buscar Doctores</button>
    </form>
    
    <!-- Mostrar doctores si existen -->
    <c:if test="${not empty doctoresDisponibles}">
        <h2>Doctores Disponibles</h2>
        <form action="${pageContext.request.contextPath}/AgendarCitasController" method="POST">
            <input type="hidden" name="accion" value="agendarCita">
            <input type="hidden" name="especialidad" value="${especialidadSeleccionada}">
            
            <select name="doctor">
                <c:forEach items="${doctoresDisponibles}" var="doc">
                    <option value="${doc.idDoctor}">${doc.nombreCompleto}</option>
                </c:forEach>
            </select>
            
            <input type="date" name="fecha" required>
            <input type="time" name="hora" required>
            <textarea name="motivo" required></textarea>
            
            <button type="submit">Agendar Cita</button>
        </form>
    </c:if>
</body>
</html>
```

**Controller maneja TODA la lógica:**
```java
@WebServlet("/AgendarCitasController")
public class AgendarCitasController extends HttpServlet {
    private DAOFactory factory;
    
    @Override
    public void init() {
        factory = DAOFactory.getFactory(); // ✅ Una sola instancia
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String accion = request.getParameter("accion");
        
        if (accion == null || "listar".equals(accion)) {
            // ✅ Controller carga las especialidades iniciales
            List<Especialidad> especialidades = factory.getEspecialidadDAO().getAll();
            request.setAttribute("especialidades", especialidades);
            request.getRequestDispatcher("/views/agendamientos.jsp").forward(request, response);
        }
        else if ("solicitarCita".equals(accion)) {
            // ✅ Controller busca doctores
            solicitarCita(request, response);
        }
    }
}
```

---

## 🎯 Ventajas de la Arquitectura Implementada

### 1. **Separación de Responsabilidades (MVC)**
- ✅ **Vista (JSP)**: Solo renderiza HTML
- ✅ **Controlador (Servlet)**: Maneja la lógica de flujo
- ✅ **Modelo (DAO + JPA)**: Maneja la persistencia

### 2. **Factory Pattern**
```java
// ✅ Fácil cambiar implementación
DAOFactory factory = DAOFactory.getFactory(DAOFactory.JPA);  // JPA
DAOFactory factory = DAOFactory.getFactory(DAOFactory.XML);  // XML
```

### 3. **Generic DAO**
```java
// ✅ CRUD heredado automáticamente
factory.getCitaDAO().create(cita);     // ← Genérico
factory.getCitaDAO().getById(1);       // ← Genérico
factory.getCitaDAO().obtenerPorFecha() // ← Específico
```

### 4. **JPA/ORM Automático**
```java
// ✅ JPA mapea automáticamente
@Entity
@Table(name = "cita")
public class Cita {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor; // ← Relación cargada automáticamente
}

// Al hacer: cita.getDoctor().getNombre()
// JPA ya cargó el Doctor, no necesitas JOIN manual
```

---

## 📝 Recomendaciones Finales

### Para `agendamientos.jsp`:

**❌ Eliminar esto del JSP:**
```jsp
<%
    EspecialidadDAO especialidadDAO = new EspecialidadDAO();
    DoctorDAO doctorDAO = new DoctorDAO();
    // ... código de lógica
%>
```

**✅ Reemplazar con:**
```jsp
<!-- JSP solo recibe datos del controller -->
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!-- El controller ya pasó ${especialidades} -->
<c:forEach items="${especialidades}" var="esp">
    <option value="${esp.nombre}">${esp.titulo}</option>
</c:forEach>
```

### Flujo Correcto:

1. **Usuario** accede a `/AgendarCitasController` (no directamente al JSP)
2. **Controller** carga datos iniciales usando Factory
3. **Controller** hace forward al JSP con los datos
4. **JSP** solo renderiza HTML
5. **Usuario** interactúa (submit form)
6. **Controller** procesa, usa DAOs, actualiza BD
7. **Controller** hace forward/redirect con resultado

---

## ✅ Estado Actual

Tu arquitectura **YA ESTÁ BIEN IMPLEMENTADA** en los controllers:
- ✅ `AgendarCitasController` usa DAOFactory
- ✅ `ConsultarCitasAgendadasController` usa DAOFactory
- ✅ `CancelarCitaController` usa DAOFactory
- ✅ `AtenderCitaController` usa DAOFactory
- ✅ JPA/ORM funciona correctamente
- ✅ Transacciones manejadas por JPA

**Solo falta:** Limpiar la lógica que quedó en algunos JSPs y asegurar que todos los flujos pasen por controllers.
