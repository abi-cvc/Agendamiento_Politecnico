# ⚡ COMANDOS RÁPIDOS PARA ACTIVAR EL SISTEMA

## 1️⃣ Actualizar Maven (Instalar Gson)
```cmd
cd "C:\Users\ERICK CAICEDO\git\Agendamiento_Politecnico5"
mvn clean install
```

O en Eclipse:
```
Click derecho en proyecto → Maven → Update Project → OK
```

---

## 2️⃣ Crear/Actualizar Base de Datos

### Opción A: Desde línea de comandos
```cmd
cd "C:\Users\ERICK CAICEDO\git\Agendamiento_Politecnico5"
mysql -u root -ppeysi123 agendamiento_politecnico < src\main\resources\insert_disponibilidades.sql
```

### Opción B: phpMyAdmin
```
1. http://localhost/phpmyadmin
2. Usuario: root, Password: peysi123
3. Seleccionar BD: agendamiento_politecnico
4. Pestaña SQL
5. Copiar contenido de: src\main\resources\insert_disponibilidades.sql
6. Ejecutar
```

---

## 3️⃣ Verificar Datos
```sql
-- Ver disponibilidades insertadas
SELECT 
    d.id_disponibilidad,
    doc.nombre AS doctor,
    d.fecha,
    TIME_FORMAT(d.hora_inicio, '%h:%i%p') AS inicio,
    TIME_FORMAT(d.hora_fin, '%h:%i%p') AS fin,
    d.disponible
FROM disponibilidad d
INNER JOIN doctor doc ON d.id_doctor = doc.id_doctor
ORDER BY d.fecha, d.hora_inicio
LIMIT 20;
```

---

## 4️⃣ Reiniciar Tomcat
En Eclipse:
```
Servers (vista) → Click derecho en Tomcat → Restart
```

---

## 5️⃣ Probar en el Navegador

### URL Principal
```
http://localhost:8080/01_MiProyecto/especialidades.jsp
```

### Probar API Directamente
```
http://localhost:8080/01_MiProyecto/api/disponibilidad?idDoctor=1&fecha=2026-01-06
```

### Flujo Completo
```
1. Abrir especialidades.jsp
2. Click "Agendar Cita" → Nutrición
3. Ver que especialidad está BLOQUEADA ✅
4. Ver doctores de NUTRICIÓN solamente ✅
5. Seleccionar doctor: María González
6. Seleccionar fecha: 2026-01-06
7. Ver horarios cargados desde BD ✅
8. Seleccionar un horario
9. ¡Listo para agendar!
```

---

## 🐛 Debug Rápido

### Ver logs en consola del navegador (F12)
```javascript
// Debe mostrar:
=== CARGANDO HORARIOS DESDE BD ===
Doctor ID: 1
Fecha: 2026-01-06
Respuesta de la API: {success: true, horarios: [...]}
```

### Ver peticiones en Network (F12)
```
1. Pestaña Network
2. Filtrar por "disponibilidad"
3. Ver Request URL
4. Ver Response (debe ser JSON)
```

### Ver logs de Tomcat
```
En Eclipse:
- Consola → Servidor Tomcat
- Buscar errores o mensajes del servlet
```

---

## ✅ Checklist de Validación

- [ ] Maven actualizado (Gson instalado)
- [ ] Script SQL ejecutado
- [ ] Datos en tabla `disponibilidad` (verificar con SELECT)
- [ ] Tomcat reiniciado
- [ ] API responde: `/api/disponibilidad?idDoctor=1&fecha=2026-01-06`
- [ ] Especialidad se bloquea al venir de especialidades.jsp
- [ ] Solo doctores de la especialidad se muestran
- [ ] Horarios se cargan al seleccionar doctor y fecha
- [ ] Se puede seleccionar un horario
- [ ] Botón "Agendar Cita" se habilita

---

## 🚨 Si algo falla

### Error: "Downloading external resources is disabled"
```
Ignorar - Es un warning del validador XML de Eclipse
No afecta la ejecución
```

### Error: ClassNotFoundException: Gson
```
Solución:
1. Maven → Update Project (Force Update)
2. Verificar en "Maven Dependencies" que esté gson-2.10.1.jar
3. Reiniciar Eclipse si es necesario
```

### Error: 404 en /api/disponibilidad
```
Solución:
1. Verificar que DisponibilidadServlet tenga @WebServlet("/api/disponibilidad")
2. Clean & Republish en Tomcat
3. Verificar URL completa con contexto del proyecto
```

### Error: "No hay horarios disponibles"
```
Solución:
1. Verificar que el script SQL se ejecutó: SELECT * FROM disponibilidad;
2. Verificar que las fechas sean futuras (actualizar fechas en el script)
3. Verificar ID del doctor seleccionado
```

### Error: Cannot find symbol Gson
```
Solución:
1. Esperar a que Maven descargue la dependencia
2. Forzar actualización: mvn clean install -U
3. Refrescar proyecto en Eclipse
```

---

## 📞 Orden de Ejecución

```
1. mvn clean install             (1 vez, instala Gson)
2. Ejecutar insert_disponibilidades.sql  (1 vez, crea datos)
3. Reiniciar Tomcat              (cada cambio en Java)
4. Abrir navegador               (probar)
5. F5 en navegador               (refrescar si hay cambios en JS/JSP)
```

---

## 🎯 URL de Prueba Final

```
1. Especialidades
   http://localhost:8080/01_MiProyecto/especialidades.jsp

2. Agendamiento directo (sin especialidad bloqueada)
   http://localhost:8080/01_MiProyecto/views/agendamientos.jsp

3. Agendamiento con especialidad bloqueada
   http://localhost:8080/01_MiProyecto/views/agendamientos.jsp?especialidad=nutricion

4. API de disponibilidad
   http://localhost:8080/01_MiProyecto/api/disponibilidad?idDoctor=1&fecha=2026-01-06

5. Test de conexión
   http://localhost:8080/01_MiProyecto/test-especialidades
```

---

¡Listo para usar! 🚀
