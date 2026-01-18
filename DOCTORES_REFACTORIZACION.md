Resumen de refactorización — Doctores (alineación con diagramas)

Objetivo:
- Renombrar y reorganizar el controlador de doctores para que sus nombres de clase, mapping y métodos coincidan con los nombres del diagrama de robustez y el diagrama de secuencia proporcionados.
- Mantener exactamente la lógica existente (persistencia y validaciones mínimas) y únicamente cambiar nombres, paths y añadir métodos 'wrapper' con los nombres del diagrama.
- Documentar cada paso en el código con comentarios estilo el archivo de agendarcitas.

Cambios realizados (archivos modificados/creados):

1) src/main/java/controller/
   - DoctorAdminController.java (modificado)
     - Ahora es un servlet de compatibilidad cuyo único propósito es redirigir peticiones de la ruta antigua
       "/DoctorAdminController" hacia la nueva ruta "/GestionarDoctores". Esto preserva enlaces existentes
       fuera de este refactor.
     - No se cambió la lógica de negocio aquí (solo redirect).

   - GestionarDoctoresController.java (nuevo)
     - Nuevo servlet principal con mapping @WebServlet("/GestionarDoctores").
     - Implementa los flujos del diagrama con métodos nombrados tal cual: gestionarDoctores, obtener, mostrar,
       solicitarNuevoDoctor, creaNuevoDoctor, crearNuevoDoctor, obtenerDoctor, mostrarConfirmacion, mostrarFormulario.
     - Los métodos son "wrappers" o renombramientos/encapsulados que llaman a la misma lógica usada anteriormente
       (DAOFactory y llamadas a los DAOs). No se añadió nueva lógica de persistencia.
     - Comentarios añadidos en cada método para mapear los pasos 1..10 del diagrama de robustez.

2) JSPs y recursos (rutas y botones)
   - src/main/webapp/views/admin/gestionar-doctores.jsp (modificado)
     - Actualizado para usar el nuevo mapping "/GestionarDoctores" en todos los enlaces y formularios.
     - El botón para abrir el formulario ahora solicita la acción GET con parametro "accion=NuevoDoctor" y su etiqueta
       es exactamente "NuevoDoctor" (tal como solicitaste en el diagrama).
     - El formulario de creación ahora envía una petición POST con "accion=solicitarNuevoDoctor" (coincide con el controlador).
     - Se dejó la visualización del formulario compatible (la JSP interpreta el atributo "mostrarNuevoDoctorForm" para
       mostrar el formulario). No se cambió la lógica de campos/validaciones del formulario.

   - src/main/webapp/inicio-admin.jsp (modificado)
   - src/main/webapp/views/admin/gestionar-estudiantes.jsp (modificado)
   - src/main/webapp/views/admin/gestionarEspecialidades.jsp (modificado)
   - src/main/webapp/views/admin/gestionarEvaluaciones.jsp (modificado)
   - src/main/webapp/js/auth-temporal.js (modificado)
     - Estas actualizaciones son cambios mínimos de enlaces que referenciaban el servlet antiguo y ahora apuntan
       al nuevo mapping /GestionarDoctores para mantener la navegación coherente.

Razones de diseño y compatibilidad:
- No cambié la lógica de negocio (DAO, entidades, validaciones esenciales) — toda la persistencia sigue usando los
  mismos DAOs y métodos (create, update, getById, obtenerPorCedula, etc.).
- Para evitar problemas de compatibilidad con enlaces externos o históricos, mantuve `DoctorAdminController` como
  un servlet de compatibilidad que redirige a `/GestionarDoctores`.
- Los nombres de los métodos añadidos (obtener, mostrar, creaNuevoDoctor, crearNuevoDoctor, etc.) no cambian la
  implementación existente, solo organizan el flujo y documentan el mapeo con los diagramas.

Notas y puntos a revisar (opcionales):
- La JSP original usaba un parámetro `modal=nuevo` para mostrar un modal. Ahora el flujo GET usa `accion=NuevoDoctor`
  y la JSP puede mostrar el formulario cuando encuentra el atributo `mostrarNuevoDoctorForm`. Esto mantiene
  compatibilidad pero cambia levemente la señal para mostrar el formulario (de query param modal a la acción GET).
- Si existen otros enlaces fuera del proyecto que apunten a "/DoctorAdminController" seguirán funcionando gracias al
  servlet de compatibilidad.

Pruebas realizadas:
- Verifiqué que los archivos Java compilaban (no hay errores de sintaxis en los controladores modificados).
- Reemplacé todas las referencias localizadas a `DoctorAdminController` en los JSPs y en el JS de navegación.

Cobertura de requisitos:
- Renombrar `DoctorAdminController` a `GestionarDoctoresController` (Done, creado nuevo servlet).
- Asegurar que la ruta visible sea `/GestionarDoctores` (Done, mapping actualizado en nuevas JSPs).
- Botón de crear debe llamarse `NuevoDoctor` (Done, texto y accion GET ajustados).
- Form POST usa `solicitarNuevoDoctor` (Done, JSP y servlet esperan este valor).
- Mantener la lógica (persistence/validaciones) sin cambios (Done).

Si deseas que además:
- Reemplace o elimine el código antiguo completamente (en vez del servlet de compatibilidad), lo puedo hacer;
- Renamear otros recursos o parámetros para una limpieza más profunda, puedo hacerlo con tu aprobación.

Gracias — si quieres, despliego o pruebo la pantalla localmente (start Tomcat) y hago una mini-guía de verificación
paso a paso para que confirmes el flujo en tu entorno.
