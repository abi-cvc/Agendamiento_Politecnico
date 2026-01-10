# 🔄 MAPEO: JSP → Controller (Flujo Correcto MVC)

## 📋 Regla de Oro: NUNCA acceder directamente a JSPs

**❌ INCORRECTO:**
```html
<a href="/views/agendamientos.jsp">Agendar Cita</a>
<a href="/consultar-citas.jsp">Mis Citas</a>
```

**✅ CORRECTO:**
```html
<a href="/AgendarCitasController?accion=listar">Agendar Cita</a>
<a href="/ConsultarCitasAgendadasController">Mis Citas</a>
```

---

## 🗺️ Mapeo Completo: JSP ↔ Controller

| JSP | Controller | URL Correcta | Acción |
|-----|-----------|--------------|---------|
| `/views/agendamientos.jsp` | `AgendarCitasController` | `/AgendarCitasController?accion=listar` | Mostrar formulario para agendar |
| `/consultar-citas.jsp` | `ConsultarCitasAgendadasController` | `/ConsultarCitasAgendadasController` | Listar citas del estudiante |
| `/citas-agendadas.jsp` | `ConsultarCitaAsignadaController` | `/ConsultarCitaAsignadaController?vista=calendario` | Ver calendario del doctor |
| `/atender-cita.jsp` | `ConsultarCitaAsignadaController` | `/ConsultarCitaAsignadaController` | Ver citas del día para atender |
| `/views/especialidades.jsp` | `EspecialidadController` | `/especialidades?accion=listar` | Listar especialidades |
| `/views/lista-doctores.jsp` | `DoctorController` | `/doctores?accion=listar` | Listar doctores |

---

## 📂 Estructura de URLs Correctas

### **1. Agendar Cita**
```
Flujo:
Usuario → /AgendarCitasController?accion=listar
       → Controller carga especialidades desde BD
       → Forward a /views/agendamientos.jsp
       → Usuario selecciona especialidad
       → /AgendarCitasController?accion=solicitarCita&especialidad=psicologia
       → Controller carga doctores
       → Forward a /views/agendamientos.jsp (con doctores)
       → Usuario completa formulario
       → POST /AgendarCitasController?accion=agendarCita
       → Controller procesa y guarda en BD
       → Redirect a /consultar-citas.jsp?exito=true
```

### **2. Consultar Mis Citas (Estudiante)**
```
Flujo:
Usuario → /ConsultarCitasAgendadasController
       → Controller obtiene citas del estudiante desde BD
       → Forward a /consultar-citas.jsp (con citas)
```

### **3. Ver Calendario (Doctor)**
```
Flujo:
Usuario → /ConsultarCitaAsignadaController?vista=calendario
       → Controller obtiene citas del mes del doctor
       → Forward a /citas-agendadas.jsp (con citasMes)
```

### **4. Atender Cita del Día (Doctor)**
```
Flujo:
Usuario → /ConsultarCitaAsignadaController
       → Controller obtiene citas del día actual
       → Forward a /atender-cita.jsp (con citas del día)
```

### **5. Cancelar Cita**
```
Flujo:
Usuario → POST /CancelarCitaController
       → idCita=123&confirmar=true&from=consultar
       → Controller actualiza estado en BD
       → Controller libera disponibilidad
       → Redirect según 'from'
```

### **6. Atender/Completar Cita**
```
Flujo:
Usuario → POST /AtenderCitaController
       → idCita=123&observacion=texto&confirmar=true
       → Controller actualiza cita con observaciones
       → Controller cambia estado a "Completada"
       → Redirect a /ConsultarCitaAsignadaController
```

---

## 🔧 Cambios Necesarios en JSPs

### **En TODOS los JSPs, cambiar:**

#### ❌ Enlaces directos a JSPs:
```html
<!-- ELIMINAR -->
<a href="${pageContext.request.contextPath}/views/agendamientos.jsp">Agendar</a>
<a href="${pageContext.request.contextPath}/consultar-citas.jsp">Mis Citas</a>
<a href="${pageContext.request.contextPath}/citas-agendadas.jsp">Calendario</a>
<a href="${pageContext.request.contextPath}/atender-cita.jsp">Atender</a>
<a href="${pageContext.request.contextPath}/views/especialidades.jsp">Especialidades</a>
```

#### ✅ Enlaces a Controllers:
```html
<!-- USAR -->
<a href="${pageContext.request.contextPath}/AgendarCitasController?accion=listar">Agendar</a>
<a href="${pageContext.request.contextPath}/ConsultarCitasAgendadasController">Mis Citas</a>
<a href="${pageContext.request.contextPath}/ConsultarCitaAsignadaController?vista=calendario">Calendario</a>
<a href="${pageContext.request.contextPath}/ConsultarCitaAsignadaController">Atender</a>
<a href="${pageContext.request.contextPath}/especialidades?accion=listar">Especialidades</a>
```

---

## 🛡️ Proteger JSPs: No Acceso Directo

### **Opción 1: Mover JSPs a WEB-INF**
```
src/main/webapp/
├── WEB-INF/
│   ├── views/
│   │   ├── agendamientos.jsp      ← NO accesible directamente
│   │   ├── consultar-citas.jsp     ← NO accesible directamente
│   │   ├── citas-agendadas.jsp     ← NO accesible directamente
│   │   └── atender-cita.jsp        ← NO accesible directamente
│   └── web.xml
└── index.html                      ← Accesible
```

**Forward desde Controller:**
```java
request.getRequestDispatcher("/WEB-INF/views/agendamientos.jsp").forward(request, response);
```

### **Opción 2: Filtro de Seguridad** (Más flexible)
```java
@WebFilter("*.jsp")
public class JSPAccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        // Permitir acceso solo si viene de un forward del controller
        if (req.getAttribute("jakarta.servlet.forward.request_uri") == null) {
            // Acceso directo → Redirigir a controller
            res.sendRedirect(req.getContextPath() + "/inicio.html");
            return;
        }
        
        chain.doFilter(request, response);
    }
}
```

---

## 📝 Template de Header para Todos los JSPs

```html
<!-- HEADER ESTÁNDAR - Copiar en todos los JSPs -->
<header>
    <div class="logo">
        <img src="${pageContext.request.contextPath}/images/logo.svg" alt="Logo">
    </div>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/inicio.html" class="font-bold">Inicio</a></li>
            <li><a href="${pageContext.request.contextPath}/especialidades?accion=listar" class="font-bold">Especialidades</a></li>
            <li><a href="${pageContext.request.contextPath}/AgendarCitasController?accion=listar" class="font-bold">Agendar Cita</a></li>
            <li><a href="${pageContext.request.contextPath}/ConsultarCitasAgendadasController" class="font-bold">Mis Citas</a></li>
            
            <!-- Solo para doctores -->
            <c:if test="${sessionScope.tipoUsuario == 'DOCTOR'}">
                <li><a href="${pageContext.request.contextPath}/ConsultarCitaAsignadaController?vista=calendario" class="font-bold">Calendario</a></li>
                <li><a href="${pageContext.request.contextPath}/ConsultarCitaAsignadaController" class="font-bold">Atender Citas</a></li>
            </c:if>
            
            <li><a href="${pageContext.request.contextPath}/index.html" class="font-bold">Login</a></li>
        </ul>
    </nav>
</header>
```

---

## ✅ Checklist de Implementación

### Para cada JSP:

- [ ] Eliminar código Java (`<% ... %>`) del JSP
- [ ] Cambiar todos los `href` que apuntan a `.jsp` por controllers
- [ ] Cambiar formularios para que envíen a controllers
- [ ] Verificar que el JSP solo use atributos del `request`
- [ ] Probar acceso directo al JSP → debe fallar o redirigir
- [ ] Probar acceso vía controller → debe funcionar

### Para cada Controller:

- [ ] Verificar que use `DAOFactory.getFactory()`
- [ ] Cargar TODOS los datos necesarios antes del forward
- [ ] Hacer `forward` al JSP con los datos en el request
- [ ] No dejar que el JSP haga consultas a BD
- [ ] Manejar errores y mostrarlos al usuario

---

## 🎯 Prioridad de Actualización

1. **Alta Prioridad** (Funcionalidad Core):
   - ✅ `agendamientos.jsp` → `AgendarCitasController`
   - ✅ `consultar-citas.jsp` → `ConsultarCitasAgendadasController`
   - ✅ `citas-agendadas.jsp` → `ConsultarCitaAsignadaController`
   - ✅ `atender-cita.jsp` → `ConsultarCitaAsignadaController`

2. **Media Prioridad**:
   - `especialidades.jsp` → `EspecialidadController`
   - `lista-doctores.jsp` → `DoctorController`

3. **Baja Prioridad**:
   - `calendario.jsp` (si no se usa)
   - `reseñas.jsp` (funcionalidad secundaria)

---

## 📊 Ejemplo Completo: Flujo de Agendar Cita

### **1. Usuario hace clic en "Agendar Cita"**
```html
<!-- En cualquier JSP -->
<a href="${pageContext.request.contextPath}/AgendarCitasController?accion=listar">
    Agendar Cita
</a>
```

### **2. Controller recibe la petición**
```java
@WebServlet("/AgendarCitasController")
public class AgendarCitasController extends HttpServlet {
    private DAOFactory factory;
    
    @Override
    public void init() {
        factory = DAOFactory.getFactory();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String accion = request.getParameter("accion");
        
        if ("listar".equals(accion) || accion == null) {
            // Cargar especialidades
            List<Especialidad> especialidades = factory.getEspecialidadDAO().getAll();
            request.setAttribute("especialidades", especialidades);
            
            // Forward al JSP
            request.getRequestDispatcher("/views/agendamientos.jsp")
                   .forward(request, response);
        }
    }
}
```

### **3. JSP solo renderiza**
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<h1>Seleccione una Especialidad</h1>

<!-- JSP NO tiene código Java, solo JSTL -->
<form action="${pageContext.request.contextPath}/AgendarCitasController" method="GET">
    <input type="hidden" name="accion" value="solicitarCita">
    
    <select name="especialidad" required>
        <c:forEach items="${especialidades}" var="esp">
            <option value="${esp.nombre}">${esp.titulo}</option>
        </c:forEach>
    </select>
    
    <button type="submit">Buscar Doctores</button>
</form>
```

---

## 🔐 Seguridad: Prevenir Acceso Directo

### **web.xml** (Recomendado)
```xml
<security-constraint>
    <web-resource-collection>
        <web-resource-name>JSPs Protegidos</web-resource-name>
        <url-pattern>/views/*.jsp</url-pattern>
        <url-pattern>/*.jsp</url-pattern>
    </web-resource-collection>
    <auth-constraint>
        <!-- Sin roles = nadie puede acceder directamente -->
    </auth-constraint>
</security-constraint>

<!-- Permitir acceso a páginas públicas -->
<security-constraint>
    <web-resource-collection>
        <web-resource-name>Páginas Públicas</web-resource-name>
        <url-pattern>/index.html</url-pattern>
        <url-pattern>/inicio.html</url-pattern>
    </web-resource-collection>
</security-constraint>
```

---

## ✅ Resumen

### **Cambios Globales:**

1. **Todos los enlaces** deben apuntar a controllers, NO a JSPs
2. **Todos los JSPs** deben eliminar código Java (`<% %>`)
3. **Todos los controllers** deben usar `DAOFactory`
4. **Todos los JSPs** deben estar en `/WEB-INF/views/` (opcional pero recomendado)
5. **Todos los formularios** deben enviar a controllers

### **Beneficios:**

✅ Separación correcta de responsabilidades (MVC)
✅ Seguridad: JSPs no accesibles directamente
✅ Testeable: Lógica en controllers
✅ Mantenible: Cambios solo en controllers
✅ Escalable: Fácil agregar validaciones, autenticación, etc.
