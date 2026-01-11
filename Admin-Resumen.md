# 📚 DOCUMENTACIÓN - GESTIÓN DE DOCTORES Y ESTUDIANTES

## 📋 Índice
1. [Resumen General](#resumen-general)
2. [Estructura del Proyecto](#estructura-del-proyecto)
3. [Arquitectura y Patrones](#arquitectura-y-patrones)
4. [Componentes Implementados](#componentes-implementados)
5. [Flujo de Casos de Uso](#flujo-de-casos-de-uso)
6. [Métodos Implementados](#métodos-implementados)
7. [Configuración y Deployment](#configuración-y-deployment)

---

## 🎯 Resumen General

Este módulo implementa la **gestión completa de Doctores y Estudiantes** desde el panel de administrador, siguiendo el patrón de arquitectura **MVC** con **persistencia JPA** y **DAOFactory**.

### Funcionalidades Principales:
- ✅ **Gestionar Doctores**: CRUD completo (Crear, Leer, Actualizar, Desactivar)
- ✅ **Gestionar Estudiantes**: CRUD completo (Crear, Leer, Actualizar)
- ✅ **Búsqueda por cédula** para ambos
- ✅ **Cambio de estado** (Activo/Inactivo) para doctores
- ✅ **Validaciones** en backend y frontend
- ✅ **Autenticación por rol** (Admin, Doctor, Estudiante)

---

## 📁 Estructura del Proyecto

```
proyecto/
├── src/main/java/
│   ├── controller/
│   │   ├── DoctorAdminController.java      ← Nuevo
│   │   └── EstudianteAdminController.java  ← Nuevo
│   ├── model/
│   │   ├── dao/
│   │   │   ├── IAdministradorDAO.java      ← Nuevo
│   │   │   ├── IEstudianteDAO.java         ← Nuevo
│   │   │   ├── AdministradorDAO.java       ← Actualizado
│   │   │   ├── EstudianteDAO.java          ← Actualizado
│   │   │   ├── DAOFactory.java             ← Actualizado
│   │   │   └── JPADAOFactory.java          ← Actualizado
│   │   └── entity/
│   │       └── Estudiante.java             ← Actualizado (nuevos campos)
│   └── util/
│       └── JPAUtil.java
├── src/main/webapp/
│   ├── inicio-admin.html                   ← Nuevo
│   ├── gestionar-doctores.html             ← Nuevo
│   ├── gestionar-estudiantes.html          ← Nuevo
│   ├── js/
│   │   ├── auth-temporal.js                ← Actualizado
│   │   ├── admin-doctores.js               ← Nuevo
│   │   └── admin-estudiantes.js            ← Nuevo
│   ├── framework.css
│   └── styles.css
└── database/
    └── alter_table_estudiante.sql          ← Nuevo
```

---

## 🏗️ Arquitectura y Patrones

### Patrón de Arquitectura: **MVC + DAO + Factory**

```
┌─────────────┐
│   VISTA     │  (HTML + JavaScript)
│ (Frontend)  │
└──────┬──────┘
       │ HTTP Request
       ↓
┌─────────────┐
│ CONTROLLER  │  (Servlet)
│  (Backend)  │  - DoctorAdminController
└──────┬──────┘  - EstudianteAdminController
       │
       ↓
┌─────────────┐
│ DAOFactory  │  Patrón Factory
└──────┬──────┘
       │
       ↓
┌─────────────┐
│  DAO Layer  │  - IEstudianteDAO (Interfaz)
│             │  - EstudianteDAO (Implementación JPA)
└──────┬──────┘  - IDoctorDAO
       │         - DoctorDAO
       ↓
┌─────────────┐
│   ENTITY    │  - Estudiante
│   (Modelo)  │  - Doctor
└──────┬──────┘  - Administrador
       │
       ↓
┌─────────────┐
│  DATABASE   │  MySQL (agendamiento_politecnico)
└─────────────┘
```

### Patrones Implementados:

1. **DAO (Data Access Object)**: Abstracción del acceso a datos
2. **Factory**: Creación de instancias de DAOs
3. **Generic DAO**: Operaciones CRUD reutilizables
4. **MVC**: Separación de responsabilidades
5. **Interface-Implementation**: Diseño por contrato

---

## 🔧 Componentes Implementados

### 1. **Interfaces DAO (Capa de Abstracción)**

#### `IAdministradorDAO.java`
```java
public interface IAdministradorDAO extends GenericDAO<Administrador, Integer> {
    Administrador buscarPorIdAdmin(String idAdmin);
    Administrador buscarPorCorreo(String correo);
    List<Administrador> obtenerActivos();
    Administrador validarCredenciales(String idAdmin, String password);
    void cambiarEstado(int id, boolean activo);
    boolean existePorIdAdmin(String idAdmin);
}
```

#### `IEstudianteDAO.java`
```java
public interface IEstudianteDAO extends GenericDAO<Estudiante, Integer> {
    Estudiante buscarPorIdPaciente(String idPaciente);
    Estudiante buscarPorCorreo(String correo);
    List<Estudiante> obtenerActivos();
    List<Cita> obtenerCitasDeEstudiante(int idEstudiante);
    Estudiante validarCredenciales(String idPaciente, String password);
    void cambiarEstado(int id, boolean activo);
    boolean existePorIdPaciente(String idPaciente);
}
```

### 2. **Implementaciones DAO (Capa de Persistencia)**

#### `AdministradorDAO.java`
- Extiende `JPAGenericDAO<Administrador, Integer>`
- Implementa `IAdministradorDAO`
- Usa JPA para todas las operaciones CRUD
- Maneja transacciones automáticamente

#### `EstudianteDAO.java`
- Extiende `JPAGenericDAO<Estudiante, Integer>`
- Implementa `IEstudianteDAO`
- Incluye métodos específicos para estudiantes
- Gestión de relaciones con Citas

### 3. **Controladores (Capa de Lógica de Negocio)**

#### `DoctorAdminController.java`
**Servlet Mapping**: `/admin/doctores`

**Métodos HTTP GET**:
| Acción | Descripción |
|--------|-------------|
| `listar` | Lista todos los doctores (activos e inactivos) |
| `buscar` | Busca un doctor por cédula |

**Métodos HTTP POST**:
| Acción | Descripción |
|--------|-------------|
| `crear` | Crea un nuevo doctor |
| `actualizar` | Actualiza teléfono, foto y descripción |
| `cambiarEstado` | Activa o desactiva un doctor |

#### `EstudianteAdminController.java`
**Servlet Mapping**: `/admin/estudiantes`

**Métodos HTTP GET**:
| Acción | Descripción |
|--------|-------------|
| `listar` | Lista todos los estudiantes |
| `buscar` | Busca un estudiante por cédula |

**Métodos HTTP POST**:
| Acción | Descripción |
|--------|-------------|
| `crear` | Crea un nuevo estudiante |
| `actualizar` | Actualiza teléfono, foto y dirección |

### 4. **Entidades (Modelo de Datos)**

#### `Estudiante.java` - Campos Actualizados:
```java
- idEstudiante: int (PK, Auto-increment)
- idPaciente: String (Cédula, Unique)
- nombreEstudiante: String
- apellidoEstudiante: String
- correoEstudiante: String (Unique)
- passwordEstudiante: String
- telefono: String ← NUEVO
- carrera: String ← NUEVO
- foto: String ← NUEVO
- direccion: String ← NUEVO
```

### 5. **Frontend (Vistas HTML + JavaScript)**

#### `inicio-admin.html`
- Panel principal del administrador
- 4 cards con enlaces a cada módulo de gestión
- Navegación con 5 botones: Inicio, Gestionar Doctores, Gestionar Estudiantes, Gestionar Especialidades, Gestionar Evaluaciones

#### `gestionar-doctores.html`
- **Búsqueda**: Input para buscar por cédula
- **Tabla**: Lista de doctores con columnas (Cédula, Nombre, Apellido, Email, Teléfono, Especialidad, Estado, Acciones)
- **Modal Nuevo**: Formulario para crear doctor
- **Modal Editar**: Formulario para editar (solo teléfono, foto, descripción)
- **Estados visuales**: ✓ (activo) / ✗ (inactivo)

#### `gestionar-estudiantes.html`
- Estructura similar a doctores
- Campos específicos: cédula, nombre, apellido, email, teléfono, carrera
- Edición limitada: teléfono, foto, dirección

#### `admin-doctores.js`
```javascript
Funciones principales:
- cargarDoctores()
- cargarEspecialidades()
- renderizarDoctores(doctoresFiltrados)
- buscarDoctor()
- abrirModalNuevo()
- abrirModalEditar(doctor)
- guardarNuevoDoctor(event)
- actualizarDoctor(event)
- cambiarEstado(id)
- mostrarMensaje(texto, tipo)
```

#### `admin-estudiantes.js`
```javascript
Funciones principales:
- cargarEstudiantes()
- renderizarEstudiantes(estudiantesFiltrados)
- buscarEstudiante()
- abrirModalNuevo()
- abrirModalEditar(estudiante)
- guardarNuevoEstudiante(event)
- actualizarEstudiante(event)
- mostrarMensaje(texto, tipo)
```

#### `auth-temporal.js` (Actualizado)
```javascript
Nueva función:
- redirigirSegunRol(rol): Redirige a la página apropiada
  - 'admin' → inicio-admin.html
  - 'doctor' → inicio.html (temporal)
  - 'estudiante' → inicio.html
```

---

## 🔄 Flujo de Casos de Uso

### **Caso de Uso 1: Listar Doctores**

```
┌─────────────┐
│   Usuario   │
│   (Admin)   │
└──────┬──────┘
       │ 1. Accede a "Gestionar Doctores"
       ↓
┌─────────────────────┐
│ DoctorAdminController│
│  GET: ?accion=listar │
└──────┬──────────────┘
       │ 2. factory.getDoctorDAO().getAll()
       ↓
┌─────────────┐
│  DoctorDAO  │
│  (JPA Query)│
└──────┬──────┘
       │ 3. SELECT * FROM doctor
       ↓
┌─────────────┐
│  Database   │
└──────┬──────┘
       │ 4. Retorna List<Doctor>
       ↓
┌─────────────────────┐
│   Controller        │
│   setAttribute()    │
└──────┬──────────────┘
       │ 5. Forward a JSP/HTML
       ↓
┌─────────────┐
│   Vista     │
│   (HTML)    │
└──────┬──────┘
       │ 6. JS renderiza tabla
       ↓
┌─────────────┐
│   Usuario   │
│   Ve lista  │
└─────────────┘
```

### **Caso de Uso 2: Crear Doctor**

```
┌─────────────┐
│   Usuario   │
└──────┬──────┘
       │ 1. Click "Nuevo Doctor"
       ↓
┌─────────────┐
│  Modal Form │
└──────┬──────┘
       │ 2. Llena formulario
       │ 3. Click "Guardar"
       ↓
┌─────────────────────┐
│ admin-doctores.js   │
│ guardarNuevoDoctor()│
└──────┬──────────────┘
       │ 4. POST /admin/doctores?accion=crear
       ↓
┌─────────────────────┐
│ DoctorAdminController│
│  crear()            │
└──────┬──────────────┘
       │ 5. Validaciones
       │ 6. factory.getDoctorDAO().create(doctor)
       ↓
┌─────────────┐
│  DoctorDAO  │
└──────┬──────┘
       │ 7. em.persist(doctor)
       ↓
┌─────────────┐
│  Database   │
│  INSERT     │
└──────┬──────┘
       │ 8. Confirma creación
       ↓
┌─────────────┐
│  Controller │
│  Redirect   │
└──────┬──────┘
       │ 9. Mensaje: "Doctor creado exitosamente"
       ↓
┌─────────────┐
│   Vista     │
│   Actualiza │
└─────────────┘
```

### **Caso de Uso 3: Buscar Doctor por Cédula**

```
┌─────────────┐
│   Usuario   │
└──────┬──────┘
       │ 1. Ingresa cédula
       │ 2. Click "Buscar"
       ↓
┌─────────────────────┐
│ admin-doctores.js   │
│ buscarDoctor()      │
└──────┬──────────────┘
       │ 3. GET /admin/doctores?accion=buscar&cedula=XXX
       ↓
┌─────────────────────┐
│ DoctorAdminController│
└──────┬──────────────┘
       │ 4. factory.getDoctorDAO().obtenerPorCedula(cedula)
       ↓
┌─────────────┐
│  DoctorDAO  │
└──────┬──────┘
       │ 5. SELECT WHERE cedula = :cedula
       ↓
┌─────────────┐
│  Database   │
└──────┬──────┘
       │ 6. Retorna Doctor o null
       ↓
┌─────────────┐
│  Controller │
└──────┬──────┘
       │ 7. setAttribute("doctores", resultado)
       ↓
┌─────────────┐
│   Vista     │
│   Renderiza │
└─────────────┘
```

### **Caso de Uso 4: Actualizar Doctor**

```
┌─────────────┐
│   Usuario   │
└──────┬──────┘
       │ 1. Click "Editar"
       ↓
┌─────────────┐
│  Modal Edit │
└──────┬──────┘
       │ 2. Modifica: teléfono, foto, descripción
       │ 3. Click "Actualizar"
       ↓
┌─────────────────────┐
│ admin-doctores.js   │
│ actualizarDoctor()  │
└──────┬──────────────┘
       │ 4. POST /admin/doctores?accion=actualizar
       ↓
┌─────────────────────┐
│ DoctorAdminController│
└──────┬──────────────┘
       │ 5. getById(id)
       │ 6. Actualiza campos permitidos
       │ 7. factory.getDoctorDAO().update(doctor)
       ↓
┌─────────────┐
│  DoctorDAO  │
└──────┬──────┘
       │ 8. em.merge(doctor)
       ↓
┌─────────────┐
│  Database   │
│  UPDATE     │
└──────┬──────┘
       │ 9. Confirma actualización
       ↓
┌─────────────┐
│   Vista     │
└─────────────┘
```

### **Caso de Uso 5: Cambiar Estado Doctor**

```
┌─────────────┐
│   Usuario   │
└──────┬──────┘
       │ 1. Click "Desactivar" o "Activar"
       ↓
┌─────────────────────┐
│ admin-doctores.js   │
│ cambiarEstado(id)   │
└──────┬──────────────┘
       │ 2. Confirma acción
       │ 3. POST /admin/doctores?accion=cambiarEstado
       ↓
┌─────────────────────┐
│ DoctorAdminController│
└──────┬──────────────┘
       │ 4. getById(id)
       │ 5. doctor.setActivo(!doctor.isActivo())
       │ 6. factory.getDoctorDAO().update(doctor)
       ↓
┌─────────────┐
│  DoctorDAO  │
└──────┬──────┘
       │ 7. em.merge(doctor)
       ↓
┌─────────────┐
│  Database   │
│  UPDATE     │
└──────┬──────┘
       │ 8. Cambio confirmado
       ↓
┌─────────────┐
│   Vista     │
│   ✓ → ✗    │
└─────────────┘
```

---

## 📝 Métodos Implementados

### **IAdministradorDAO**
| Método | Descripción | Retorno |
|--------|-------------|---------|
| `buscarPorIdAdmin(String)` | Busca admin por ID de usuario | `Administrador` |
| `buscarPorCorreo(String)` | Busca admin por email | `Administrador` |
| `obtenerActivos()` | Lista admins activos | `List<Administrador>` |
| `validarCredenciales(String, String)` | Valida login | `Administrador` |
| `cambiarEstado(int, boolean)` | Activa/desactiva admin | `void` |
| `existePorIdAdmin(String)` | Verifica existencia | `boolean` |

### **IEstudianteDAO**
| Método | Descripción | Retorno |
|--------|-------------|---------|
| `buscarPorIdPaciente(String)` | Busca por cédula | `Estudiante` |
| `buscarPorCorreo(String)` | Busca por email | `Estudiante` |
| `obtenerActivos()` | Lista todos (no tiene campo activo) | `List<Estudiante>` |
| `obtenerCitasDeEstudiante(int)` | Obtiene citas | `List<Cita>` |
| `validarCredenciales(String, String)` | Valida login | `Estudiante` |
| `cambiarEstado(int, boolean)` | No implementado (sin campo activo) | `void` |
| `existePorIdPaciente(String)` | Verifica existencia | `boolean` |

### **DoctorAdminController**
| Método | HTTP | Acción | Descripción |
|--------|------|--------|-------------|
| `listarDoctores()` | GET | `listar` | Lista todos los doctores |
| `buscarDoctor()` | GET | `buscar` | Busca por cédula |
| `crearDoctor()` | POST | `crear` | Crea nuevo doctor |
| `actualizarDoctor()` | POST | `actualizar` | Actualiza doctor existente |
| `cambiarEstado()` | POST | `cambiarEstado` | Activa/desactiva doctor |

### **EstudianteAdminController**
| Método | HTTP | Acción | Descripción |
|--------|------|--------|-------------|
| `listarEstudiantes()` | GET | `listar` | Lista todos los estudiantes |
| `buscarEstudiante()` | GET | `buscar` | Busca por cédula |
| `crearEstudiante()` | POST | `crear` | Crea nuevo estudiante |
| `actualizarEstudiante()` | POST | `actualizar` | Actualiza estudiante existente |

---

## ⚙️ Configuración y Deployment

### 1. **Actualizar Base de Datos**
```sql
-- Ejecutar el script: alter_table_estudiante.sql
USE agendamiento_politecnico;

ALTER TABLE estudiante
ADD COLUMN telefono VARCHAR(15) AFTER correo_estudiante,
ADD COLUMN carrera VARCHAR(200) AFTER telefono,
ADD COLUMN foto VARCHAR(255) AFTER carrera,
ADD COLUMN direccion TEXT AFTER foto;
```

### 2. **Configurar DAOFactory**
Asegúrate de que `DAOFactory` esté inicializado en el `init()` de cada controlador:
```java
private DAOFactory factory;

@Override
public void init() throws ServletException {
    factory = DAOFactory.getFactory();
}
```

### 3. **Mapeo de Servlets**
Los controladores usan anotaciones `@WebServlet`:
- `/admin/doctores` → `DoctorAdminController`
- `/admin/estudiantes` → `EstudianteAdminController`

### 4. **Archivos Frontend**
Colocar en `src/main/webapp/`:
- `inicio-admin.html`
- `gestionar-doctores.html`
- `gestionar-estudiantes.html`
- `js/auth-temporal.js`
- `js/admin-doctores.js`
- `js/admin-estudiantes.js`

### 5. **Credenciales de Prueba**
```
Administrador:
- Email: admin@epn.edu.ec
- Password: admin123
- Rol: admin

Estudiante:
- Email: carol.velasquez@epn.edu.ec
- Password: 123456
- Rol: estudiante
```

### 6. **Flujo de Autenticación**
1. Usuario accede a `index.html`
2. Ingresa email, password y **selecciona rol**
3. `auth-temporal.js` valida credenciales
4. Redirige según rol:
   - **admin** → `inicio-admin.html`
   - **estudiante** → `inicio.html`
   - **doctor** → `inicio.html` (temporal)

---

## ✅ Checklist de Implementación

- [x] Interfaces DAO creadas (IAdministradorDAO, IEstudianteDAO)
- [x] Implementaciones DAO actualizadas
- [x] DAOFactory actualizado con nuevos DAOs
- [x] Controladores creados (DoctorAdminController, EstudianteAdminController)
- [x] Entidad Estudiante actualizada con nuevos campos
- [x] Script SQL para alterar tabla estudiante
- [x] Vista inicio-admin.html
- [x] Vista gestionar-doctores.html
- [x] Vista gestionar-estudiantes.html
- [x] JavaScript admin-doctores.js
- [x] JavaScript admin-estudiantes.js
- [x] auth-temporal.js actualizado con redirección por rol
- [x] Documentación completa (este README)

---

## 🚀 Próximos Pasos

1. **Testing**: Probar todos los flujos CRUD
2. **Validaciones**: Añadir más validaciones en backend
3. **Seguridad**: Implementar hash de contraseñas (BCrypt)
4. **Paginación**: Implementar paginación para listas largas
5. **Búsqueda avanzada**: Añadir filtros adicionales
6. **Logs**: Implementar logging con SLF4J
7. **Manejo de errores**: Mejorar páginas de error personalizadas

---

**Fecha de creación**: Enero 2026  
**Versión**: 1.0  
**Autor**: Equipo de Desarrollo - Agendamiento Politécnico  
**Framework**: Jakarta EE + JPA + MySQL