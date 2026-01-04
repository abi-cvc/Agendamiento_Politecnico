# 🔌 GUÍA DE CONEXIÓN A BASE DE DATOS - XAMPP MySQL

## 📋 CONFIGURACIÓN COMPLETADA

La conexión a la base de datos ha sido configurada para **XAMPP MySQL** con los valores por defecto.

---

## ⚙️ CONFIGURACIÓN ACTUAL

### 🗄️ Base de Datos
- **Nombre:** `agendamiento_politecnico`
- **Host:** `localhost`
- **Puerto:** `3306`
- **Usuario:** `root`
- **Contraseña:** *(vacía)*

### 📂 Archivos Configurados

#### 1️⃣ `persistence.xml` (JPA)
**Ubicación:** `src/main/resources/META-INF/persistence.xml`

**Configuración:**
```xml
- Driver: com.mysql.cj.jdbc.Driver
- URL: jdbc:mysql://localhost:3306/agendamiento_politecnico
- Parámetros: useSSL=false, serverTimezone=America/Guayaquil
- Estrategia: none (las tablas YA EXISTEN)
```

#### 2️⃣ `DatabaseConnection.java` (JDBC)
**Ubicación:** `src/main/java/util/DatabaseConnection.java`

**Patrón:** Singleton
**Propósito:** Conexión JDBC tradicional (si se necesita)

#### 3️⃣ `JPAUtil.java` (JPA)
**Ubicación:** `src/main/java/util/JPAUtil.java`

**Propósito:** Gestión de EntityManager para JPA/Hibernate

---

## 🚀 PASOS PARA PROBAR LA CONEXIÓN

### ✅ Paso 1: Iniciar XAMPP
1. Abre **XAMPP Control Panel**
2. Haz clic en **Start** en el módulo **MySQL**
3. Verifica que el puerto **3306** esté activo

### ✅ Paso 2: Crear la Base de Datos
1. Abre **phpMyAdmin** (http://localhost/phpmyadmin)
2. Ve a la pestaña **SQL**
3. Copia y pega todo el contenido de `src/main/resources/init_database.sql`
4. Haz clic en **Ejecutar** (GO)
5. Verifica que se crearon las tablas:
   - ✅ `especialidad` (5 registros)
   - ✅ `doctor` (9 registros)
   - ✅ `disponibilidad` (múltiples registros)
   - ✅ `cita` (vacía por ahora)

### ✅ Paso 3: Ejecutar Prueba de Conexión

#### Opción A: Probar JDBC (DatabaseConnection)
```bash
Ejecutar como Java Application:
src/main/java/util/DatabaseConnection.java
```

**Resultado esperado:**
```
=== TEST DE CONEXIÓN ===
✅ Estado: CONECTADO
📍 Base de datos: agendamiento_politecnico
🌐 URL: jdbc:mysql://localhost:3306/agendamiento_politecnico...
👤 Usuario: root
========================
```

#### Opción B: Probar JPA completo (TestConnection)
```bash
Ejecutar como Java Application:
src/main/java/util/TestConnection.java
```

**Resultado esperado:**
```
╔════════════════════════════════════════════════╗
║  PRUEBA DE CONEXIÓN - AGENDAMIENTO POLITÉCNICO ║
╚════════════════════════════════════════════════╝

🔹 PRUEBA 1: CONEXIÓN JDBC (DatabaseConnection)
--------------------------------------------------
✅ Conexión JDBC exitosa
   📍 Base de datos: agendamiento_politecnico
   🌐 URL: jdbc:mysql://localhost:3306/agendamiento_politecnico...
   👤 Usuario: root
   ✅ Conexión válida y funcional

==================================================

🔹 PRUEBA 2: CONEXIÓN JPA (JPAUtil + Entidades)
--------------------------------------------------
✅ EntityManager creado correctamente
✅ EntityManager está abierto y listo

📊 Consultando especialidades...
✅ Especialidades encontradas: 5
   Lista de especialidades:
   - Nutrición (ID: 1)
   - Odontología (ID: 2)
   - Psicología (ID: 3)
   - Medicina General (ID: 4)
   - Enfermería (ID: 5)

📊 Consultando doctores...
✅ Doctores encontrados: 9
   Lista de doctores:
   - Dr(a). María González - Nutrición
   - Dr(a). Carlos Ramírez - Nutrición
   ...
```

---

## 🔧 SOLUCIÓN DE PROBLEMAS

### ❌ Error: "Communications link failure"

**Causa:** MySQL no está corriendo o puerto incorrecto

**Solución:**
1. Verifica que XAMPP MySQL esté iniciado
2. Verifica que el puerto sea 3306:
   - Abre XAMPP Control Panel → Config (MySQL) → my.ini
   - Busca `port=3306`

---

### ❌ Error: "Unknown database 'agendamiento_politecnico'"

**Causa:** La base de datos no existe

**Solución:**
1. Abre phpMyAdmin
2. Ejecuta el script `init_database.sql` completo
3. Verifica que aparezca la base de datos en la lista

---

### ❌ Error: "Access denied for user 'root'@'localhost'"

**Causa:** Contraseña incorrecta

**Solución:**
1. En XAMPP, por defecto root NO tiene contraseña
2. Si configuraste una contraseña, actualiza:
   - `persistence.xml` línea con `jakarta.persistence.jdbc.password`
   - `DatabaseConnection.java` variable `DB_PASSWORD`

---

### ❌ Error: "Table 'cita' doesn't exist"

**Causa:** Las tablas no se crearon correctamente

**Solución:**
1. Ejecuta `init_database.sql` nuevamente
2. Verifica en phpMyAdmin que todas las tablas existan:
   ```sql
   SHOW TABLES FROM agendamiento_politecnico;
   ```

---

### ❌ Error: "Class not found: com.mysql.cj.jdbc.Driver"

**Causa:** Falta la dependencia MySQL Connector

**Solución:**
1. Verifica que `pom.xml` tenga:
   ```xml
   <dependency>
       <groupId>com.mysql</groupId>
       <artifactId>mysql-connector-j</artifactId>
       <version>8.3.0</version>
   </dependency>
   ```
2. Ejecuta: **Maven → Update Project** (Alt+F5)
3. Verifica que el JAR esté en **Maven Dependencies**

---

## 📊 USO EN EL CÓDIGO

### Usar JPA (RECOMENDADO)
```java
// Obtener EntityManager
EntityManager em = JPAUtil.getEntityManager();

try {
    // Iniciar transacción
    em.getTransaction().begin();
    
    // Consultar especialidades
    List<Especialidad> especialidades = em.createQuery(
        "SELECT e FROM Especialidad e", Especialidad.class
    ).getResultList();
    
    // Crear una nueva cita
    Cita cita = new Cita();
    cita.setFechaCita(LocalDate.now().plusDays(1));
    cita.setHoraCita(LocalTime.of(10, 0));
    cita.setMotivoConsulta("Consulta general");
    cita.setEspecialidad(especialidades.get(0));
    
    if (cita.crear()) {
        em.persist(cita);
        em.getTransaction().commit();
        System.out.println("✅ Cita creada exitosamente");
    }
    
} catch (Exception e) {
    if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
    }
    e.printStackTrace();
} finally {
    em.close();
}
```

### Usar JDBC (Solo si es necesario)
```java
DatabaseConnection dbConn = DatabaseConnection.getInstance();
Connection conn = dbConn.getConnection();

try {
    String sql = "SELECT * FROM especialidad";
    PreparedStatement stmt = conn.prepareStatement(sql);
    ResultSet rs = stmt.executeQuery();
    
    while (rs.next()) {
        System.out.println(rs.getString("nombre"));
    }
    
} catch (SQLException e) {
    e.printStackTrace();
} finally {
    dbConn.closeConnection();
}
```

---

## 📝 NOTAS IMPORTANTES

### ⚠️ Estrategia de Esquema
En `persistence.xml` está configurado con:
```xml
<property name="jakarta.persistence.schema-generation.database.action" value="none"/>
```

**Esto significa:**
- ✅ JPA **NO** creará ni modificará tablas automáticamente
- ✅ Las tablas deben existir previamente (con `init_database.sql`)
- ✅ Evita pérdida accidental de datos
- ⚠️ Si cambias las entidades, debes actualizar la BD manualmente

### 🔄 Si Necesitas Recrear las Tablas
**Opción 1: Manual (RECOMENDADO)**
```sql
DROP DATABASE IF EXISTS agendamiento_politecnico;
CREATE DATABASE agendamiento_politecnico;
-- Ejecutar init_database.sql completo
```

**Opción 2: Automático (¡CUIDADO! Borra datos)**
```xml
<!-- En persistence.xml cambiar a: -->
<property name="jakarta.persistence.schema-generation.database.action" value="drop-and-create"/>
```

---

## ✅ CHECKLIST FINAL

Antes de continuar con el desarrollo, verifica:

- [ ] ✅ XAMPP MySQL está corriendo
- [ ] ✅ Base de datos `agendamiento_politecnico` existe
- [ ] ✅ Las 4 tablas están creadas (especialidad, doctor, disponibilidad, cita)
- [ ] ✅ Hay datos de prueba (5 especialidades, 9 doctores)
- [ ] ✅ `TestConnection.java` ejecuta sin errores
- [ ] ✅ Las consultas JPA funcionan correctamente
- [ ] ✅ Los mapeos de entidades coinciden con las tablas

---

## 📞 SOPORTE

Si sigues teniendo problemas:
1. Verifica los logs de Eclipse en la consola
2. Revisa los errores específicos de MySQL en XAMPP
3. Comprueba que todas las dependencias Maven estén descargadas

**¡Conexión configurada y lista para usar!** 🚀
