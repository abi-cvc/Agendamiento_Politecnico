Cambios realizados: Refactor controlador EstudianteAdminController

Resumen:
- Se refactorizó `EstudianteAdminController.java` para que los nombres de métodos y el flujo reflejen exactamente el diagrama de robustez proporcionado por el usuario (obtener, mostrar, crearNuevoEstudiante, mostrarFormulario, editarEstudiante, cambiarEstado, etc.).
- Se creó un nuevo controlador público `GestionarEstudiantesController.java` con mapeo `@WebServlet("/GestionarEstudiantes")`. Toda la lógica de gestión de estudiantes fue trasladada a este nuevo servlet.
- `EstudianteAdminController.java` fue marcado como obsoleto (`@Deprecated`) y su mapeo cambiado a `/EstudianteAdminController_obsolete`. Además sus endpoints POST/GET redirigen al nuevo controlador para evitar rutas rotas.
- Se actualizaron las vistas (JSP) y un JS de navegación para usar la nueva ruta `/GestionarEstudiantes` y los nuevos nombres de acción donde aplicaba.

Archivos principales modificados/creados:
- Creado: `src/main/java/controller/GestionarEstudiantesController.java`
  - Contiene la lógica completa de gestionar estudiantes (listado, búsqueda, mostrar formulario, crear, actualizar, cambiar estado).
  - Mapeo: `@WebServlet(urlPatterns = {"/GestionarEstudiantes"})`.

- Modificado: `src/main/java/controller/EstudianteAdminController.java`
  - Marcado como `@Deprecated` y mapeado a `/EstudianteAdminController_obsolete`.
  - Ahora redirige (GET/POST) a `/GestionarEstudiantes` para mantener compatibilidad y evitar duplicidad de lógica.

- Modificado (vistas):
  - `src/main/webapp/views/admin/gestionar-estudiantes.jsp`
    - Actualizada para usar `/GestionarEstudiantes` en enlaces y forms.
    - El formulario de creación ahora envía `accion=crearNuevoEstudiante` (coincide con el método del controlador).
    - Formularios de actualización y toggle de estado envían `accion=actualizar` y `accion=cambiarEstado` respectivamente (coinciden con el controlador).
  - `src/main/webapp/views/admin/gestionar-doctores.jsp` - referencias a la gestión de estudiantes actualizadas.
  - `src/main/webapp/views/admin/gestionarEvaluaciones.jsp` - referencia actualizada.
  - `src/main/webapp/views/admin/gestionarEspecialidades.jsp` - referencia actualizada.
  - `src/main/webapp/inicio-admin.jsp` - botones/enlaces actualizados para apuntar a `/GestionarEstudiantes`.

- Modificado (cliente):
  - `src/main/webapp/js/auth-temporal.js` - la navegación temporal para admin ahora apunta a `/GestionarEstudiantes`.

Notas de compatibilidad:
- Para mantener compatibilidad con la vista anterior, `GestionarEstudiantesController` acepta tanto `accion=crear` como `accion=crearNuevoEstudiante` para la creación; la JSP ya fue actualizada para usar `crearNuevoEstudiante`.
- Si hay otros ficheros que hagan referencia a la antigua ruta pública `/EstudianteAdminController`, fueron actualizados en este cambio; el archivo obsoleto queda solo para referencia y posibles librerías externas que aún lo apunten, pero redirige al nuevo endpoint.

Verificación:
- Ejecución de verificación estática del IDE: sin errores detectados en los archivos modificados.
- No se pudo ejecutar `mvn package` dentro de este entorno porque `mvn` no está disponible en la terminal del sistema; por favor ejecuta localmente `mvn -DskipTests package` para validar compilación completa.

Siguientes pasos (opcionales):
- Ejecutar `mvn -DskipTests package` localmente y reportar si aparecen errores de compilación.
- Actualizar cualquier documentación o scripts automatizados que dependan de la ruta antigua `/EstudianteAdminController`.
- Añadir pruebas unitarias para `GestionarEstudiantesController` (con mocks para HttpServletRequest/Response y DAO) si se desea cobertura automatizada.

Fecha: 2026-01-17
Autor: Refactor automático para ajuste de flujo y nombres según diagrama de robustez