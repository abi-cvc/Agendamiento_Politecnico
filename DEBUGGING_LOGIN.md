ciono# 🔧 SOLUCIÓN: Login No Funciona - Pasos de Debugging

## 🎯 Problema
Al hacer clic en "Iniciar Sesión", el formulario no envía nada o no redirige.

---

## ✅ PASOS PARA SOLUCIONAR

### 1. **Verificar que Tomcat esté corriendo**
```
En Eclipse:
- Vista "Servers"
- Verificar que Tomcat esté "Started" (verde)
```

### 2. **Limpiar y Reiniciar Tomcat**
```
1. Click derecho en Tomcat → Stop
2. Click derecho en Tomcat → Clean
3. Click derecho en el proyecto → Clean
4. Click derecho en Tomcat → Start
```

### 3. **Verificar despliegue del proyecto**
```
En consola de Eclipse, debe aparecer:
"Deployment of web application directory [...\01_MiProyecto] has finished"
```

### 4. **Probar página de test**
```
URL: http://localhost:8080/01_MiProyecto/test-login.jsp

Esta página tiene:
- ✅ Credenciales pre-cargadas
- ✅ Información de debug
- ✅ Formulario simplificado
```

### 5. **Ver logs de Tomcat en Eclipse**
```
1. Vista "Console"
2. Buscar líneas que digan:
   "=== INTENTO DE LOGIN ==="
   
Si aparecen, el servlet SÍ está recibiendo datos.
Si NO aparecen, el servlet no está mapeado.
```

### 6. **Verificar consola del navegador (F12)**
```
1. Abrir index.jsp
2. F12 → Console
3. Llenar formulario
4. Click "Iniciar Sesión"
5. Ver mensajes:
   "Formulario de login encontrado: [HTMLFormElement]"
   "=== FORMULARIO ENVIÁNDOSE ==="
   "Rol: estudiante"
   "Identificación: 1725896347"
   "Password: ****"
   "Formulario válido, enviando..."
```

---

## 🔍 DIAGNÓSTICO SEGÚN SÍNTOMAS

### Síntoma 1: "Nada pasa al hacer click"

**Causa:** JavaScript bloqueando el submit

**Solución:**
```javascript
// Temporalmente deshabilitar JavaScript
// Comentar esta línea en index.jsp:
// <script src="<%= request.getContextPath() %>/js/auth.js"></script>
```

### Síntoma 2: "Error 404 en /login"

**Causa:** Servlet no desplegado

**Solución:**
```
1. Verificar @WebServlet("/login") en LoginServlet.java
2. Limpiar Tomcat
3. Volver a publicar proyecto
4. Reiniciar Tomcat
```

### Síntoma 3: "Recarga la misma página"

**Causa:** Error en el servlet, vuelve a index.jsp

**Solución:**
```
Ver logs de Tomcat para el error específico
```

### Síntoma 4: "Error 500"

**Causa:** Excepción en el servlet

**Solución:**
```
Ver logs de Tomcat:
java.lang.NullPointerException
o similar
```

---

## 🧪 PRUEBAS A REALIZAR

### Prueba 1: Acceso directo al servlet
```
URL: http://localhost:8080/01_MiProyecto/login

Resultado esperado:
- Redirige a index.jsp
- O muestra error 405 (Method Not Allowed) porque no es POST

Si da 404: Servlet NO está desplegado
```

### Prueba 2: Test con página simplificada
```
URL: http://localhost:8080/01_MiProyecto/test-login.jsp

Llenar y enviar:
- Rol: Estudiante
- ID: 1725896347  
- Pass: 123456

Resultado esperado:
- Redirige a login-success.jsp
- O muestra error con mensaje específico
```

### Prueba 3: Verificar BD
```sql
USE agendamiento_politecnico;

SELECT * FROM estudiante WHERE id_paciente = '1725896347';

Debe devolver 1 fila con:
- password_estudiante = '123456'

Si está vacía: No hay datos
Si password_estudiante es NULL: Falta ejecutar add_passwords.sql
```

---

## 🛠️ SOLUCIONES RÁPIDAS

### Solución 1: Republicar proyecto
```
1. Click derecho en el proyecto
2. Run As → Run on Server
3. Seleccionar Tomcat
4. Finish
```

### Solución 2: Verificar web.xml (si existe)
```xml
<!-- No debería ser necesario con @WebServlet -->
<!-- Pero si existe, verificar que no haya conflictos -->
```

### Solución 3: Crear servlet alternativo simple
```java
@WebServlet("/test-servlet")
public class TestServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.getWriter().println("Servlet funciona!");
    }
}

// Probar: http://localhost:8080/01_MiProyecto/test-servlet
```

---

## 📊 CHECKLIST DE VERIFICACIÓN

- [ ] Tomcat está corriendo (verde en Eclipse)
- [ ] Proyecto desplegado (sin errores en consola)
- [ ] LoginServlet.java compilado sin errores
- [ ] @WebServlet("/login") presente en LoginServlet
- [ ] MySQL corriendo
- [ ] BD agendamiento_politecnico existe
- [ ] Tabla estudiante tiene datos
- [ ] Campo password_estudiante existe
- [ ] Caché del navegador limpiado (Ctrl + Shift + Delete)
- [ ] Probado en ventana de incógnito

---

## 🔑 URLs DE PRUEBA

```
1. Login principal:
   http://localhost:8080/01_MiProyecto/index.jsp

2. Login de prueba:
   http://localhost:8080/01_MiProyecto/test-login.jsp

3. Servlet directo (GET):
   http://localhost:8080/01_MiProyecto/login

4. Context path:
   http://localhost:8080/01_MiProyecto/
```

---

## 💡 DEBUGGING AVANZADO

### Agregar prints en LoginServlet

```java
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    System.out.println("========================================");
    System.out.println("🔵 LoginServlet.doPost() EJECUTÁNDOSE");
    System.out.println("========================================");
    
    String rol = request.getParameter("rol");
    String identificacion = request.getParameter("identificacion");
    String password = request.getParameter("password");
    
    System.out.println("Rol recibido: " + rol);
    System.out.println("ID recibido: " + identificacion);
    System.out.println("Pass recibido: " + (password != null ? "****" : "NULL"));
    
    // ... resto del código
}
```

### Verificar logs en tiempo real

```bash
# En la consola de Eclipse
# Filtrar por: "LoginServlet" o "INTENTO DE LOGIN"
```

---

## ✅ SOLUCIÓN GARANTIZADA

Si nada funciona, seguir estos pasos EN ORDEN:

```
1. Cerrar Eclipse completamente

2. Eliminar carpeta:
   workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0

3. Abrir Eclipse

4. Limpiar proyecto:
   Project → Clean → Clean all projects

5. Maven Update:
   Click derecho en proyecto → Maven → Update Project

6. Limpiar Tomcat:
   Click derecho en Tomcat → Clean

7. Agregar proyecto a Tomcat:
   Click derecho en Tomcat → Add and Remove
   → Agregar 01_MiProyecto

8. Iniciar Tomcat

9. Esperar mensaje: "Server startup in [X] ms"

10. Probar: http://localhost:8080/01_MiProyecto/test-login.jsp
```

---

## 📞 INFORMACIÓN DE CONTACTO DEL SISTEMA

```
Project: 01_MiProyecto
Context: /01_MiProyecto
Servlet: /login
Full URL: http://localhost:8080/01_MiProyecto/login
Method: POST
Parameters: rol, identificacion, password
```

---

## 🎯 RESPUESTA ESPERADA

Cuando funciona correctamente:

```
1. Click "Iniciar Sesión"
   ↓
2. Consola de navegador:
   "=== FORMULARIO ENVIÁNDOSE ==="
   ↓
3. Consola de Tomcat:
   "=== INTENTO DE LOGIN ==="
   "✅ Login exitoso - Estudiante: Juan Pérez"
   ↓
4. Navegador:
   Redirige a login-success.jsp
   ↓
5. Después de 3 segundos:
   Redirige a inicio.jsp
```

---

¡Sigue estos pasos y el login funcionará! 🚀
