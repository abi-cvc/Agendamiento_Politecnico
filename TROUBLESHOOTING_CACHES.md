# 🔧 GUÍA DE TROUBLESHOOTING: Reiniciar Caches y Resolver Enlaces Bugueados

## 🐛 Síntomas Comunes de Problemas de Cache

- ✅ Los enlaces van a páginas antiguas o incorrectas
- ✅ Los cambios en JSPs/HTML no se reflejan
- ✅ Error 404 en enlaces que deberían funcionar
- ✅ Página en blanco al hacer clic en un enlace
- ✅ Los controllers no se ejecutan aunque el enlace sea correcto

---

## 🔄 PASO 1: Limpiar Cache del Navegador

### **Opción A: Forzar Recarga (Más Rápido)**

**Chrome / Edge:**
```
Ctrl + Shift + R        (Windows/Linux)
Cmd + Shift + R         (Mac)
```

**Firefox:**
```
Ctrl + F5               (Windows/Linux)
Cmd + Shift + R         (Mac)
```

### **Opción B: Borrar Cache Completo**

**Chrome:**
1. Presiona `Ctrl + Shift + Delete`
2. Selecciona **"Todo el tiempo"**
3. Marca:
   - ☑️ Imágenes y archivos en caché
   - ☑️ Cookies y otros datos de sitios
4. Click en **"Borrar datos"**

**Firefox:**
1. Presiona `Ctrl + Shift + Delete`
2. Selecciona **"Todo"**
3. Marca:
   - ☑️ Caché
   - ☑️ Cookies
4. Click en **"Limpiar ahora"**

**Edge:**
1. Presiona `Ctrl + Shift + Delete`
2. Selecciona **"Siempre"**
3. Marca:
   - ☑️ Imágenes y archivos en caché
   - ☑️ Cookies y otros datos de sitios
4. Click en **"Borrar ahora"**

---

## 🔄 PASO 2: Limpiar Cache de Tomcat (Servidor)

### **Opción A: Desde Eclipse**

1. **Detener el servidor:**
   - Click derecho en "Tomcat v10.1 Server"
   - Selecciona **"Stop"**
   - Espera a que se detenga completamente

2. **Limpiar el servidor:**
   - Click derecho en "Tomcat v10.1 Server"
   - Selecciona **"Clean..."**
   - Click en **"OK"**

3. **Limpiar Tomcat Work Directory:**
   - Click derecho en "Tomcat v10.1 Server"
   - Selecciona **"Clean Tomcat Work Directory..."**
   - Click en **"OK"**

4. **Limpiar el proyecto:**
   ```
   Project → Clean...
   Seleccionar "Agendamiento_Politecnico5"
   Click en "Clean"
   ```

5. **Reiniciar el servidor:**
   - Click derecho en "Tomcat v10.1 Server"
   - Selecciona **"Start"**

### **Opción B: Manualmente (Limpieza Profunda)**

1. **Detener Tomcat** (desde Eclipse o cerrar Eclipse)

2. **Navegar a la carpeta work de Tomcat:**
   ```
   C:\Users\ERICK CAICEDO\eclipse-workspace\Servers\Tomcat v10.1 Server at localhost-config\work
   ```

3. **ELIMINAR todo el contenido** de la carpeta `work`:
   - Click derecho → Eliminar TODO
   - O desde CMD:
   ```cmd
   cd "C:\Users\ERICK CAICEDO\eclipse-workspace\Servers\Tomcat v10.1 Server at localhost-config"
   rmdir /s /q work
   mkdir work
   ```

4. **Eliminar archivos compilados del proyecto:**
   ```cmd
   cd "C:\Users\ERICK CAICEDO\git\Agendamiento_Politecnico5"
   rmdir /s /q target
   ```

5. **Desde Eclipse:**
   ```
   Project → Clean...
   Maven → Update Project (Alt + F5)
   ```

6. **Reiniciar Tomcat**

---

## 🔄 PASO 3: Limpiar Cache de Maven

### **Desde Eclipse:**

1. Click derecho en el proyecto **"Agendamiento_Politecnico5"**
2. Selecciona **"Maven → Update Project..."** (o `Alt + F5`)
3. Marca:
   - ☑️ Force Update of Snapshots/Releases
   - ☑️ Update project configuration from pom.xml
4. Click en **"OK"**

### **Desde Terminal/CMD:**

```cmd
cd "C:\Users\ERICK CAICEDO\git\Agendamiento_Politecnico5"
mvn clean install
```

O para limpieza más profunda:
```cmd
mvn clean install -U
```

---

## 🔄 PASO 4: Verificar Despliegue Correcto

### **Verificar que los archivos están desplegados:**

1. **Navegar a la carpeta de despliegue de Tomcat:**
   ```
   C:\Users\ERICK CAICEDO\eclipse-workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Agendamiento_Politecnico5
   ```

2. **Verificar que existen los archivos:**
   - `inicio.html` ✅
   - `views/especialidades.jsp` ✅
   - `views/agendamientos.jsp` ✅
   - `consultar-citas.jsp` ✅
   - `WEB-INF/classes/controller/` (con los .class) ✅

3. **Si faltan archivos:**
   ```
   Click derecho en el proyecto → Refresh (F5)
   Click derecho en el servidor → Clean...
   Click derecho en el servidor → Publish (Alt + Shift + X, R)
   ```

---

## 🔄 PASO 5: Reiniciar Todo Desde Cero

### **Procedimiento Completo:**

```cmd
# 1. DETENER ECLIPSE Y TOMCAT
# Cerrar Eclipse completamente

# 2. LIMPIAR CACHES DE WINDOWS
cd "C:\Users\ERICK CAICEDO\git\Agendamiento_Politecnico5"
rmdir /s /q target

cd "C:\Users\ERICK CAICEDO\eclipse-workspace\Servers\Tomcat v10.1 Server at localhost-config"
rmdir /s /q work
mkdir work

cd "C:\Users\ERICK CAICEDO\eclipse-workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0"
rmdir /s /q wtpwebapps
mkdir wtpwebapps

# 3. ABRIR ECLIPSE
# Eclipse → Window → Preferences → General → Workspace → Refresh using native hooks

# 4. LIMPIAR PROYECTO
Project → Clean... → Clean all projects

# 5. MAVEN UPDATE
Click derecho en proyecto → Maven → Update Project (Force Update)

# 6. REINICIAR TOMCAT
Start Tomcat
```

---

## 🔍 PASO 6: Verificar que los Enlaces Funcionan

### **Test Manual:**

1. **Abrir Developer Tools** (F12)
2. **Ir a la pestaña Network**
3. **Hacer clic en cada enlace del menú:**

```
✅ Inicio → Debe ir a: /inicio.html
✅ Especialidades → Debe ir a: /especialidades?accion=listar
✅ Mis Citas → Debe ir a: /ConsultarCitasAgendadasController
✅ Reseñas → Debe ir a: /#reseñas (anchor)
```

4. **Verificar en Network:**
   - Status Code: `200 OK` ✅
   - Status Code: `302 Found` (redirect) ✅
   - Status Code: `404 Not Found` ❌ (error)

### **URLs a Probar:**

```
http://localhost:8080/Agendamiento_Politecnico5/inicio.html
http://localhost:8080/Agendamiento_Politecnico5/especialidades?accion=listar
http://localhost:8080/Agendamiento_Politecnico5/ConsultarCitasAgendadasController
http://localhost:8080/Agendamiento_Politecnico5/AgendarCitasController?accion=listar
http://localhost:8080/Agendamiento_Politecnico5/ConsultarCitaAsignadaController
```

---

## 🚨 PASO 7: Solución de Problemas Específicos

### **Problema 1: Error 404 en Controllers**

**Síntoma:** `/especialidades?accion=listar` da 404

**Solución:**
```
1. Verificar en Eclipse: Servers → Tomcat → Modules
2. Verificar que el proyecto esté desplegado
3. Click derecho en Tomcat → Add and Remove...
4. Agregar el proyecto si no está
5. Restart Tomcat
```

### **Problema 2: Enlaces van a páginas viejas**

**Síntoma:** Los cambios en HTML no se reflejan

**Solución:**
```
1. Ctrl + Shift + R (forzar recarga)
2. Verificar que el archivo se guardó (Ctrl + S)
3. Refresh del proyecto en Eclipse (F5)
4. Tomcat → Clean → Publish
```

### **Problema 3: JSP muestra código Java**

**Síntoma:** Se ve `<%...%>` en la página

**Solución:**
```
1. Verificar que el archivo termine en .jsp
2. Verificar que esté en la carpeta correcta
3. Reiniciar Tomcat completamente
```

### **Problema 4: Session/Cookies problemas**

**Síntoma:** La sesión no se mantiene

**Solución:**
```
1. Borrar cookies del navegador (Ctrl + Shift + Delete)
2. Cerrar TODAS las pestañas del navegador
3. Abrir nueva ventana de incógnito
4. Probar ahí
```

---

## 📝 CHECKLIST DE VERIFICACIÓN RÁPIDA

Cuando algo no funciona, seguir este orden:

- [ ] **CTRL + SHIFT + R** (forzar recarga navegador)
- [ ] **F5** en el proyecto (refresh Eclipse)
- [ ] **Ctrl + S** (guardar todos los archivos)
- [ ] **Project → Clean**
- [ ] **Tomcat → Clean**
- [ ] **Tomcat → Restart**
- [ ] **Borrar cache del navegador**
- [ ] **Verificar URL en Developer Tools**
- [ ] **Revisar Console de Tomcat** (errores)
- [ ] Si todo falla: **Procedimiento Completo (Paso 5)**

---

## 🔧 COMANDOS ÚTILES

### **Limpiar Todo desde CMD:**

```cmd
@echo off
echo ===================================
echo LIMPIANDO CACHES COMPLETOS
echo ===================================

cd "C:\Users\ERICK CAICEDO\git\Agendamiento_Politecnico5"
echo [1/4] Limpiando target Maven...
rmdir /s /q target 2>nul

cd "C:\Users\ERICK CAICEDO\eclipse-workspace\Servers\Tomcat v10.1 Server at localhost-config"
echo [2/4] Limpiando work de Tomcat...
rmdir /s /q work 2>nul
mkdir work

cd "C:\Users\ERICK CAICEDO\eclipse-workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0"
echo [3/4] Limpiando wtpwebapps...
rmdir /s /q wtpwebapps 2>nul
mkdir wtpwebapps

cd "C:\Users\ERICK CAICEDO\git\Agendamiento_Politecnico5"
echo [4/4] Ejecutando Maven clean...
mvn clean

echo.
echo ===================================
echo LIMPIEZA COMPLETADA
echo ===================================
echo.
echo Ahora:
echo 1. Abrir Eclipse
echo 2. Project ^> Clean
echo 3. Maven ^> Update Project
echo 4. Reiniciar Tomcat
echo.
pause
```

Guardar como: `limpiar_caches.bat` y ejecutar cuando sea necesario.

---

## 🎯 PREVENCIÓN: Mejores Prácticas

### **Durante el Desarrollo:**

1. **Siempre guardar antes de probar** (Ctrl + S)
2. **Hacer Refresh del proyecto** después de cambios (F5)
3. **No modificar archivos en la carpeta target**
4. **Usar CTRL + SHIFT + R** en lugar de F5 normal
5. **Cerrar pestañas viejas** antes de probar cambios

### **Antes de Hacer Commit:**

```
1. Project → Clean
2. Maven → Update Project
3. Restart Tomcat
4. Probar todas las funcionalidades
5. Verificar que no hay errores en consola
```

### **Configuración Recomendada de Eclipse:**

```
Window → Preferences:

General → Workspace:
☑️ Refresh using native hooks or polling
☑️ Refresh on access
☑️ Build automatically

Server → Runtime Environments:
☑️ Automatically publish when resources change

Maven:
☑️ Update Maven projects on startup
☑️ Download repository index updates on startup
```

---

## ✅ VERIFICACIÓN FINAL

Después de limpiar caches, verificar:

1. ✅ Inicio.html carga correctamente
2. ✅ Especialidades carga desde controller
3. ✅ Mis Citas carga desde controller (sin página vacía)
4. ✅ Agendar cita funciona
5. ✅ No hay errores 404
6. ✅ No hay errores en consola de Tomcat
7. ✅ Los cambios en HTML/JSP se reflejan inmediatamente

---

## 🆘 Si Nada Funciona

### **Último Recurso:**

1. **Exportar código a un ZIP**
2. **Cerrar Eclipse**
3. **Eliminar workspace:**
   ```cmd
   cd "C:\Users\ERICK CAICEDO\eclipse-workspace"
   rename .metadata .metadata_backup
   ```
4. **Abrir Eclipse (creará nuevo workspace)**
5. **Import proyecto desde Git**
6. **Configurar Tomcat de nuevo**
7. **Deploy y probar**

---

## 📞 Contacto de Soporte

Si después de seguir TODOS estos pasos el problema persiste:

1. **Capturar:**
   - Screenshot del error
   - Console de Tomcat (últimas 50 líneas)
   - Network tab de Developer Tools
   - URL completa que está fallando

2. **Verificar:**
   - Versión de Java: `java -version`
   - Versión de Maven: `mvn -version`
   - Versión de Tomcat en Eclipse

3. **Documentar:**
   - ¿Qué enlace falla?
   - ¿Qué pasos seguiste?
   - ¿Qué mensaje de error ves?

---

**💡 TIP:** En el 95% de los casos, el problema se resuelve con:
```
1. Ctrl + Shift + R (navegador)
2. Project → Clean (Eclipse)
3. Tomcat → Restart
```

**Si esto no funciona, entonces seguir el Paso 5 (Reinicio Completo).**
