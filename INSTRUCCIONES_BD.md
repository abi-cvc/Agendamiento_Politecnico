# INSTRUCCIONES PARA CONFIGURAR LA BASE DE DATOS

## Paso 1: Asegúrate de tener MySQL corriendo
- Abre XAMPP Control Panel
- Inicia MySQL (botón "Start")
- Verifica que esté corriendo (debe mostrar un fondo verde)

## Paso 2: Ejecuta el script de actualización
Puedes hacerlo de dos formas:

### Opción A: Desde phpMyAdmin (RECOMENDADO)
1. Abre tu navegador y ve a: http://localhost/phpmyadmin
2. Inicia sesión con:
   - Usuario: root
   - Contraseña: peysi123
3. Selecciona la base de datos "agendamiento_politecnico" en el panel izquierdo
4. Ve a la pestaña "SQL"
5. Copia y pega el contenido del archivo `update_database.sql`
6. Haz clic en "Continuar" o "Go"

### Opción B: Desde línea de comandos
```cmd
mysql -u root -ppeysi123 agendamiento_politecnico < src\main\resources\update_database.sql
```

## Paso 3: Verifica que todo esté correcto
Ejecuta estas consultas en phpMyAdmin:
```sql
-- Ver todas las tablas
SHOW TABLES;

-- Ver especialidades (debe mostrar 5)
SELECT COUNT(*) FROM especialidad;

-- Ver estudiantes (debe mostrar 5)
SELECT COUNT(*) FROM estudiante;

-- Ver administradores (debe mostrar 2)
SELECT COUNT(*) FROM administrador;

-- Verificar que la columna id_estudiante existe en cita
DESCRIBE cita;
```

## Paso 4: Inicia/Reinicia tu servidor Tomcat
1. En Eclipse, ve a la vista "Servers"
2. Si el servidor está corriendo, detén Tomcat (botón rojo)
3. Inicia Tomcat nuevamente (botón verde "Start")
4. Espera a que termine de iniciar (verás "Server startup" en la consola)

## Paso 5: Prueba la aplicación
Abre tu navegador y prueba estas URLs:
1. Test de especialidades: http://localhost:8080/Agendamiento_Politecnico5/test-especialidades
2. Especialidades JSP: http://localhost:8080/Agendamiento_Politecnico5/especialidades.jsp
3. Inicio: http://localhost:8080/Agendamiento_Politecnico5/inicio.jsp

## Solución de problemas

### Si ves error de conexión:
- Verifica que MySQL esté corriendo en XAMPP
- Verifica que la contraseña sea "peysi123" en:
  * src/main/java/util/DatabaseConnection.java
  * src/main/resources/META-INF/persistence.xml

### Si no aparecen especialidades:
- Verifica que la tabla especialidad tenga datos:
  ```sql
  SELECT * FROM especialidad;
  ```
- Si está vacía, ejecuta el archivo `init_database.sql` completo

### Si ves error 404:
- Verifica que el proyecto esté desplegado en Tomcat
- Verifica la URL (debe incluir el nombre del contexto del proyecto)

## Archivos importantes del proyecto:
- **Entidades JPA**: src/main/java/model/entity/
  - Especialidad.java ✅
  - Doctor.java ✅
  - Cita.java ✅
  - Disponibilidad.java ✅
  - Estudiante.java ✅ (NUEVO)
  - Administrador.java ✅ (NUEVO)

- **DAOs**: src/main/java/model/dao/
  - EspecialidadDAO.java ✅
  - DoctorDAO.java ✅
  - CitaDAO.java ✅
  - DisponibilidadDAO.java ✅
  - EstudianteDAO.java ✅ (NUEVO)
  - AdministradorDAO.java ✅ (NUEVO)

- **Configuración**:
  - persistence.xml ✅ (actualizado con nuevas entidades)
  - DatabaseConnection.java ✅ (password actualizado)
  - init_database.sql ✅ (actualizado con nuevas tablas)
  - update_database.sql ✅ (NUEVO - para actualizar BD existente)

## Estado actual del proyecto:
✅ Todas las entidades del diagrama de clases creadas
✅ Todos los DAOs implementados con JPA/ORM
✅ Relaciones entre entidades configuradas
✅ Base de datos actualizada con nuevas tablas
✅ Configuración de conexión actualizada (password: peysi123)
✅ Servlet de prueba creado (test-especialidades)
✅ JSP de especialidades listo para usar

¡Todo está listo para funcionar! Solo falta ejecutar el script SQL y probar.
