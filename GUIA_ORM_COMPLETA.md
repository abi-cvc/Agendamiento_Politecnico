# 🔄 ORM (Object-Relational Mapping) - Guía Completa del Proyecto

## 📚 ¿Qué es ORM?

**ORM (Object-Relational Mapping)** es una técnica que permite:
- Trabajar con bases de datos relacionales usando **objetos Java**
- **Eliminar SQL manual** (JPA lo genera automáticamente)
- Manejar **relaciones entre tablas** de forma natural con objetos
- **Simplificar CRUD** (Create, Read, Update, Delete)

---

## 🎯 ORM en tu Proyecto

### **Tecnologías Usadas:**
- ✅ **JPA 3.1** (Jakarta Persistence API) - Especificación ORM
- ✅ **EclipseLink 4.0.2** - Implementación de JPA (proveedor ORM)
- ✅ **MySQL 8.x** - Base de datos relacional

---

## 📊 Estructura de Datos con ORM

### **Modelo de Dominio (Objetos Java):**

```
┌─────────────────┐         ┌──────────────────┐
│  Especialidad   │         │       Cita       │
├─────────────────┤         ├──────────────────┤
│ idEspecialidad  │◄────────│ idCita           │
│ nombre          │  Many   │ fechaCita        │
│ titulo          │   to    │ horaCita         │
│ descripcion     │   One   │ motivoConsulta   │
│ servicios       │         │ estadoCita       │
│ icono           │         │ especialidad ◄───┘
└─────────────────┘         └──────────────────┘
```

### **Modelo Relacional (Base de Datos):**

```sql
-- Tabla especialidad
CREATE TABLE especialidad (
    idEspecialidad INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) UNIQUE,
    titulo VARCHAR(100),
    descripcion TEXT,
    servicios TEXT,
    icono VARCHAR(50)
);

-- Tabla cita
CREATE TABLE cita (
    idCita INT PRIMARY KEY AUTO_INCREMENT,
    fechaCita DATE,
    horaCita TIME,
    motivoConsulta TEXT,
    estadoCita VARCHAR(50),
    observacionCita TEXT,
    idEspecialidad INT,  -- ← Foreign Key
    FOREIGN KEY (idEspecialidad) REFERENCES especialidad(idEspecialidad)
);
```

---

## ✨ Ventajas del ORM en tu Proyecto

### **❌ ANTES (Sin ORM - SQL Manual)**

```java
// Guardar una cita (código largo y propenso a errores)
String sql = "INSERT INTO cita (fechaCita, horaCita, motivoConsulta, estadoCita, idEspecialidad) VALUES (?, ?, ?, ?, ?)";
Connection conn = DriverManager.getConnection(...);
PreparedStatement ps = conn.prepareStatement(sql);
ps.setDate(1, Date.valueOf(fecha));
ps.setTime(2, Time.valueOf(hora));
ps.setString(3, motivo);
ps.setString(4, estado);
ps.setInt(5, idEspecialidad);
ps.executeUpdate();
ps.close();
conn.close();

// Obtener citas con especialidad (JOIN manual)
String sql = "SELECT c.*, e.* FROM cita c " +
             "JOIN especialidad e ON c.idEspecialidad = e.idEspecialidad " +
             "WHERE c.fechaCita = ?";
// ... más código SQL manual
```

### **✅ AHORA (Con ORM - JPA)**

```java
// Guardar una cita (código simple y legible)
Cita cita = new Cita();
cita.setFechaCita(fecha);
cita.setHoraCita(hora);
cita.setMotivoConsulta(motivo);
cita.setEstadoCita("Agendada");
cita.setEspecialidad(especialidad);  // ← Relación automática
new CitaDAO().guardar(cita);  // ← JPA genera el INSERT

// Obtener citas con especialidad (JOIN automático)
List<Cita> citas = new CitaDAO().obtenerTodas();
for (Cita c : citas) {
    System.out.println(c.getEspecialidad().getTitulo());  // ← JPA carga la relación
}
```

---

## 🔧 Componentes del ORM en tu Proyecto

### **1. Entidades JPA (Clases anotadas)**

#### **Especialidad.java**
```java
@Entity  // ← Indica que es una tabla
@Table(name = "especialidad")
public class Especialidad {
    
    @Id  // ← Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ← Auto-increment
    private int idEspecialidad;
    
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
    
    @Column(nullable = false, length = 100)
    private String titulo;
    
    // ... más campos
}
```

#### **Cita.java**
```java
@Entity
@Table(name = "cita")
public class Cita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCita;
    
    private LocalDate fechaCita;
    private LocalTime horaCita;
    
    // ===== RELACIÓN ORM =====
    @ManyToOne(fetch = FetchType.EAGER)  // ← Muchas citas → Una especialidad
    @JoinColumn(name = "idEspecialidad")  // ← Foreign Key
    private Especialidad especialidad;  // ← Objeto, no ID
}
```

### **2. DAOs (Data Access Objects)**

#### **EspecialidadDAO.java**
```java
public class EspecialidadDAO {
    
    // ORM: Obtener todas
    public List<Especialidad> obtenerEspecialidades() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Especialidad> query = em.createQuery(
            "SELECT e FROM Especialidad e ORDER BY e.nombre", 
            Especialidad.class
        );
        return query.getResultList();  // ← JPA mapea automáticamente
    }
    
    // ORM: Guardar
    public void guardar(Especialidad especialidad) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(especialidad);  // ← JPA genera INSERT
        em.getTransaction().commit();
    }
}
```

#### **CitaDAO.java**
```java
public class CitaDAO {
    
    // ORM: Guardar cita con especialidad
    public void guardar(Cita cita) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(cita);  // ← JPA genera INSERT con idEspecialidad
        em.getTransaction().commit();
    }
    
    // ORM: Obtener citas por especialidad
    public List<Cita> obtenerPorEspecialidad(Especialidad especialidad) {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Cita> query = em.createQuery(
            "SELECT c FROM Cita c WHERE c.especialidad = :especialidad", 
            Cita.class
        );
        query.setParameter("especialidad", especialidad);  // ← Relación ORM
        return query.getResultList();
    }
}
```

### **3. JPAUtil (Singleton para EntityManager)**

```java
public class JPAUtil {
    
    private static final EntityManagerFactory emf = 
        Persistence.createEntityManagerFactory("AgendamientoPU");
    
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
```

### **4. persistence.xml (Configuración JPA)**

```xml
<persistence-unit name="AgendamientoPU" transaction-type="RESOURCE_LOCAL">
    <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
    
    <properties>
        <!-- Conexión MySQL -->
        <property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/agendamiento_politecnico"/>
        <property name="jakarta.persistence.jdbc.user" value="root"/>
        <property name="jakarta.persistence.jdbc.password" value=""/>
        
        <!-- ORM: Crear tablas automáticamente -->
        <property name="jakarta.persistence.schema-generation.database.action" value="create"/>
    </properties>
</persistence-unit>
```

---

## 💡 Ejemplos de Uso Real

### **Ejemplo 1: Agendar Cita con Especialidad**

```java
// En AgendarCitasController.java

private Cita crearCita(HttpServletRequest request) {
    Cita cita = new Cita();
    cita.setFechaCita(LocalDate.parse(request.getParameter("fecha")));
    cita.setHoraCita(LocalTime.parse(request.getParameter("hora")));
    cita.setMotivoConsulta(request.getParameter("motivo"));
    cita.setEstadoCita("Agendada");
    
    // ===== RELACIÓN ORM =====
    String nombreEsp = request.getParameter("especialidad");  // "nutricion"
    Especialidad esp = especialidadDAO.obtenerPorNombre(nombreEsp);
    cita.setEspecialidad(esp);  // ← Asignar objeto, no ID
    
    // Guardar (JPA genera INSERT con idEspecialidad automáticamente)
    citaDAO.guardar(cita);
    
    return cita;
}
```

**SQL generado automáticamente por JPA:**
```sql
INSERT INTO cita (fechaCita, horaCita, motivoConsulta, estadoCita, observacionCita, idEspecialidad)
VALUES ('2026-01-15', '10:00:00', 'Control general', 'Agendada', 'Sin observaciones', 1);
```

### **Ejemplo 2: Mostrar Citas con Especialidades en JSP**

```java
// En un Servlet
List<Cita> citas = citaDAO.obtenerTodas();
request.setAttribute("citas", citas);
request.getRequestDispatcher("citas.jsp").forward(request, response);
```

```jsp
<!-- En citas.jsp -->
<c:forEach var="cita" items="${citas}">
    <div class="cita-card">
        <h3>Cita #${cita.idCita}</h3>
        <p>Fecha: ${cita.fechaCita}</p>
        <p>Hora: ${cita.horaCita}</p>
        
        <!-- ← ORM carga automáticamente la especialidad -->
        <p>Especialidad: ${cita.especialidad.titulo}</p>
        <p>${cita.especialidad.icono}</p>
    </div>
</c:forEach>
```

### **Ejemplo 3: Obtener Citas de una Especialidad**

```java
// Obtener especialidad
Especialidad nutricion = especialidadDAO.obtenerPorNombre("nutricion");

// Obtener citas de esa especialidad usando ORM
List<Cita> citasNutricion = citaDAO.obtenerPorEspecialidad(nutricion);

// Mostrar
for (Cita cita : citasNutricion) {
    System.out.println("Cita: " + cita.getIdCita());
    System.out.println("Especialidad: " + cita.getEspecialidad().getTitulo());
    System.out.println("Paciente motivo: " + cita.getMotivoConsulta());
}
```

---

## 🔍 Anotaciones JPA Más Importantes

| Anotación | Propósito | Ejemplo |
|-----------|-----------|---------|
| `@Entity` | Marca una clase como tabla | `@Entity public class Cita {...}` |
| `@Table` | Especifica el nombre de la tabla | `@Table(name = "cita")` |
| `@Id` | Marca la clave primaria | `@Id private int idCita;` |
| `@GeneratedValue` | Auto-increment | `@GeneratedValue(strategy = GenerationType.IDENTITY)` |
| `@Column` | Configura columna | `@Column(nullable = false, unique = true)` |
| `@ManyToOne` | Relación muchos a uno | `@ManyToOne private Especialidad especialidad;` |
| `@JoinColumn` | Especifica FK | `@JoinColumn(name = "idEspecialidad")` |

---

## 🎯 Flujo Completo de ORM en tu Proyecto

```
1. Usuario envía formulario de agendamiento
   └─► HTTP POST → AgendarCitasController

2. Controller crea objeto Cita
   └─► cita.setEspecialidad(especialidad)  ← Relación ORM

3. Controller llama a CitaDAO.guardar(cita)
   └─► EntityManager.persist(cita)

4. JPA/EclipseLink genera SQL automáticamente
   └─► INSERT INTO cita (..., idEspecialidad) VALUES (..., 1)

5. Base de datos guarda el registro
   └─► MySQL ejecuta el INSERT

6. JPA asigna el ID generado al objeto
   └─► cita.setIdCita(generatedId)

7. Controller devuelve confirmación
   └─► forward("confirmacion.jsp")
```

---

## 🚀 Ventajas del ORM en tu Proyecto

| Aspecto | Sin ORM (SQL manual) | Con ORM (JPA) |
|---------|---------------------|---------------|
| **Código** | 50+ líneas | 5 líneas |
| **SQL** | Manual y propenso a errores | Generado automáticamente |
| **Relaciones** | JOINs manuales | Objetos navegables |
| **Mantenibilidad** | Difícil | Fácil |
| **Cambios de BD** | Muchos cambios en código | Pocos cambios |
| **Type-Safety** | No | Sí (compilador detecta errores) |

---

## 📝 Métodos CRUD con ORM

### **Create (Guardar)**
```java
Cita cita = new Cita();
// ... configurar campos
citaDAO.guardar(cita);  // ← INSERT automático
```

### **Read (Leer)**
```java
// Todas
List<Cita> todas = citaDAO.obtenerTodas();

// Por ID
Cita cita = citaDAO.obtenerPorId(1);

// Por especialidad
List<Cita> citas = citaDAO.obtenerPorEspecialidad(especialidad);
```

### **Update (Actualizar)**
```java
Cita cita = citaDAO.obtenerPorId(1);
cita.setEstadoCita("Completada");
citaDAO.actualizar(cita);  // ← UPDATE automático
```

### **Delete (Eliminar)**
```java
citaDAO.eliminar(1);  // ← DELETE automático
```

---

## ✅ Resumen

**ORM (JPA) en tu proyecto permite:**

1. ✅ **Trabajar con objetos Java** en lugar de SQL
2. ✅ **Relaciones automáticas** entre Cita y Especialidad
3. ✅ **CRUD simplificado** (guardar, obtener, actualizar, eliminar)
4. ✅ **SQL generado automáticamente** por JPA
5. ✅ **Código más limpio y mantenible**
6. ✅ **Menos errores** (compilador detecta problemas)
7. ✅ **Transacciones manejadas** (commit/rollback automático)

---

**Fecha:** 2026-01-04  
**Estado:** ✅ ORM completamente implementado  
**Tecnología:** JPA 3.1 + EclipseLink 4.0.2 + MySQL 8.x
