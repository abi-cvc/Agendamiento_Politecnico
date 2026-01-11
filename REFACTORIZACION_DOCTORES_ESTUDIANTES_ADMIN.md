Refactorización: Doctores, Estudiantes y Administrador (2026-01-11)

Resumen de cambios
- Eliminé la dependencia de JavaScript para la gestión administrativa de doctores y estudiantes.
- Los controladores siguen usando los mismos nombres: `DoctorAdminController` y `EstudianteAdminController`.
- Los controladores dejaron de devolver JSON; ahora siempre preparan atributos y hacen forward a JSPs en `webapp/views/admin`.
- Se añadieron las JSPs:
  - `views/admin/gestionar-doctores.jsp` — lista, formularios de creación y actualización, cambio de estado.
  - `views/admin/gestionar-estudiantes.jsp` — lista, formularios de creación y actualización.
- Quité el código JavaScript que generaba solicitudes AJAX y JSON helpers en:
  - `src/main/webapp/js/admin-doctores.js` (reemplazado por un placeholder).
  - `src/main/webapp/js/admin-estudiantes.js` (reemplazado por un placeholder).
- Implementé una relación entre `Administrador` y `Estudiante` en la entidad `Administrador` (ManyToMany) y un helper en `AdministradorDAO` para obtener estudiantes gestionados por un administrador.

Decisiones y supuestos
- No cambié los nombres de los controladores ni su mapping: los servlets mantienen `@WebServlet("/DoctorAdminController")` y `@WebServlet("/EstudianteAdminController")`.
- Para no tocar la estructura de `Estudiante` ni `Doctor`, usé los campos existentes: `Estudiante.idPaciente` es la cédula única; en formularios se llama `cedula` para mantener compatibilidad con los controladores.
- Para la relación administrador-estudiante decidí usar `@ManyToMany` con una tabla de unión `administrador_estudiante`. Esto evita modificar `Estudiante` para agregar campo `administrador` y permite que la relación sea opcional.
- Eliminé los helpers JSON de los controladores (ya no devolvemos JSON). Si en el futuro se necesita soporte API, recomiendo crear servlets/API REST separados.

Archivos modificados/añadidos
- Modificados:
  - `src/main/java/controller/DoctorAdminController.java` — eliminado manejo JSON, ahora forwards a `/views/admin/gestionar-doctores.jsp`.
  - `src/main/java/controller/EstudianteAdminController.java` — eliminado manejo JSON, ahora forwards a `/views/admin/gestionar-estudiantes.jsp`.
  - `src/main/webapp/js/admin-doctores.js` — contenido JS removido (placeholder).
  - `src/main/webapp/js/admin-estudiantes.js` — contenido JS removido (placeholder).
  - `src/main/java/model/entity/Administrador.java` — añadido `List<Estudiante> estudiantes` con @ManyToMany y getters/setters.
  - `src/main/java/model/dao/AdministradorDAO.java` — añadido método `obtenerEstudiantesPorAdministrador(int idAdmin)`.

- Añadidos:
  - `src/main/webapp/views/admin/gestionar-doctores.jsp`
  - `src/main/webapp/views/admin/gestionar-estudiantes.jsp`

Instrucciones de uso (rápido)
- Acceder a la gestión de doctores: /YourContext/DoctorAdminController?accion=listar  (o simplemente en el menú "Inicio admin" -> gestionar doctores si está enlazado)
- Acceder a la gestión de estudiantes: /YourContext/EstudianteAdminController?accion=listar
- Los formularios usan POST directo al servlet. No se requiere JS.

Cobertura de requisitos del usuario
- Evitar usar JS: He eliminado los JSON helpers y el código AJAX; las páginas usan formularios HTML en JSP. (Done)
- Crear JSPs que muestren solo la información que envía el Servlet: Añadí JSPs en `views/admin` que usan `request` attributes. (Done)
- Usar formularios HTML dentro de JSP: todos los create/update/cambiarEstado usan forms POST. (Done)
- CSS: Las JSPs referencian `css/framwork.css` y `css/styles.css` — asumo que ya existen en `webapp/css`. (Done)
- Quitar JSON helpers en controladores: eliminados. (Done)
- No cambiar nombres de controladores: mantenidos `DoctorAdminController` y `EstudianteAdminController`. (Done)
- Respetar `Estudiante.idPaciente` como cédula única: formularios usan `name="cedula"` y los controladores usan `buscarPorIdPaciente`/`existePorIdPaciente`. No añadí campos extra. (Done)
- Administradores gestionan estudiantes: añadida relación en `Administrador` y helper en `AdministradorDAO`. (Done)
- Redirigir rutas a los controladores: los formularios y forwards usan `/DoctorAdminController` y `/EstudianteAdminController` respectivamente. (Done)

Estructura actual del proyecto (reciente)
Agendamiento_Politecnico/
	ADMIN_CHANGES.md
	Admin-Resumen.md
	ARQUITECTURA_JSP_CONTROLLER_DAO_JPA.md
	CAMBIOS_JSP_A_CONTROLLERS.md
	CAMBIOS_SEGUN_DIAGRAMAS.md
	CORRECCION_ACCESO_DIRECTO_JSP.md
	CORRECCION_REFERENCIAS_AGENDARCITAS.md
	CORRECCION_RUTAS_JSP.md
	CORRECCION_RUTAS_JSP.md
	... (otros archivos de documentación)
	src/
		main/
			java/
				controller/
					DoctorAdminController.java
					EstudianteAdminController.java
				model/
					dao/
						AdministradorDAO.java
					entity/
						Administrador.java
						Estudiante.java
						Doctor.java
				...
			webapp/
				css/
					framwork.css
					styles.css
				js/
					admin-doctores.js (placeholder)
					admin-estudiantes.js (placeholder)
				views/
					admin/
						gestionar-doctores.jsp
						gestionar-estudiantes.jsp
				inicio-admin.html
				... 

Notas finales y próximos pasos recomendados
- Revisar la base de datos: la tabla de unión `administrador_estudiante` deberá crearse en la BD si aún no existe. Puedes crearla manualmente o usar migraciones.
- Si otras partes del sistema consumían `/admin/doctores?format=json` o endpoints JSON, habrá que proveer endpoints API separados si se requiere compatibilidad hacia atrás.
- Verificar plantillas y rutas relativas según el contexto de despliegue (por ejemplo `/${contextPath}/views/admin/...`).

Si quieres, puedo:
- Añadir tests simples o scripts para verificar los formularios.
- Crear la migración SQL para la tabla `administrador_estudiante`.
- Buscar y actualizar otras referencias a rutas antiguas o código JS que dependiera de JSON.

Fin del registro.
