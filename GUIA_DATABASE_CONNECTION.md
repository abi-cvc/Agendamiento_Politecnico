# 🔧 GUÍA DE CONFIGURACIÓN - DATABASE CONNECTION

## 📋 Resumen

Se ha creado la clase **`DatabaseConnection`** usando el patrón **Singleton** para centralizar el acceso a la base de datos MySQL.

---

## 📍 Ubicación del Archivo

```
src/main/java/util/DatabaseConnection.java
```

---

## ⚙️ CONFIGURACIÓN REQUERIDA

### **Paso 1: Abrir el archivo DatabaseConnection.java**

Ubicación: `src/main/java/util/DatabaseConnection.java`

### **Paso 2: Configurar los parámetros de conexión**

Busca estas líneas (líneas 20-42 aprox.) y modifícalas según tu entorno:

```java
// ========================================
// CONFIGURACIÓN DE BASE DE DATOS
// ========================================

// TODO: CONFIGURAR ESTOS VALORES SEGÚN TU BASE DE DATOS

/**
 * URL de conexión a MySQL
 */
private static final String DB_URL = "jdbc:mysql://localhost:3306/agendamiento_politecnico?serverTimezone=UTC";

/**
 * Usuario de MySQL
 */
private static final String DB_USER = "root";

/**
 * Contraseña de MySQL
 */
private static final String DB_PASSWORD = "";
```

---

## 🎯 CONFIGURACIONES COMUNES

### **Configuración 1: XAMPP (Windows/Mac/Linux)**
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/agendamiento_politecnico?serverTimezone=UTC";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = ""; // Vacío por defecto en XAMPP
```

### **Configuración 2: WAMP (Windows)**
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/agendamiento_politecnico?serverTimezone=UTC";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = ""; // Vacío por defecto en WAMP
```

### **Configuración 3: MySQL Server Instalado Directamente**
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/agendamiento_politecnico?serverTimezone=UTC";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "tu_contraseña_aqui"; // La que configuraste al instalar
```

### **Configuración 4: Servidor Remoto**
```java
private static final String DB_URL = "jdbc:mysql://192.168.1.100:3306/agendamiento_politecnico?serverTimezone=UTC";
private static final String DB_USER = "usuario_remoto";
private static final String DB_PASSWORD = "contraseña_remota";
```

### **Configuración 5: MySQL en Docker**
```java
private static final String DB_URL = "jdbc:mysql://localhost:3307/agendamiento_politecnico?serverTimezone=UTC";
// Nota: El puerto puede ser diferente (3307, 3308, etc.)
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "root";
```

---

## 🧪 PROBAR LA CONEXIÓN

### **Opción 1: Ejecutar desde Eclipse**

1. Abre `DatabaseConnection.java`
2. Click derecho en el archivo
3. **Run As** → **Java Application**
4. Verás en la consola si la conexión funciona

**Salida exitosa:**
```
=================================
  PRUEBA DE CONEXIÓN A BD
=================================

🔄 Creando nueva conexión a la base de datos...
✅ Driver MySQL cargado correctamente
✅ Conexión establecida exitosamente
   📍 Base de datos: agendamiento_politecnico

=== TEST DE CONEXIÓN ===
✅ Estado: CONECTADO
📍 Base de datos: agendamiento_politecnico
🌐 URL: jdbc:mysql://localhost:3306/agendamiento_politecnico?serverTimezone=UTC
👤 Usuario: root
========================

✅ La conexión funciona correctamente
✅ Puedes usar esta configuración
✅ Conexión cerrada correctamente
```

**Salida con error:**
```
=================================
  PRUEBA DE CONEXIÓN A BD
=================================

❌ ERROR al conectar con la base de datos
   Verifica:
   1. MySQL está corriendo (XAMPP/WAMP)
   2. URL de conexión: jdbc:mysql://localhost:3306/agendamiento_politecnico?serverTimezone=UTC
   3. Usuario: root
   4. La base de datos existe

=== TEST DE CONEXIÓN ===
❌ Estado: ERROR
❌ Mensaje: Access denied for user 'root'@'localhost' (using password: NO)
========================

❌ Error en la conexión
⚠️ Revisa la configuración en DatabaseConnection.java:
   - DB_URL
   - DB_USER
   - DB_PASSWORD
```

---

## ❓ SOLUCIÓN DE PROBLEMAS

### **Error 1: "Driver MySQL no encontrado"**

**Causa:** Falta la dependencia de MySQL Connector en el `pom.xml`

**Solución:**
Verifica que tengas en tu `pom.xml`:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.3.0</version>
</dependency>
```

---

### **Error 2: "Access denied for user 'root'@'localhost'"**

**Causa:** Usuario o contraseña incorrectos

**Solución:**
1. Verifica el usuario y contraseña en MySQL
2. En XAMPP: usuario `root`, contraseña vacía `""`
3. Actualiza `DB_USER` y `DB_PASSWORD` en `DatabaseConnection.java`

---

### **Error 3: "Unknown database 'agendamiento_politecnico'"**

**Causa:** La base de datos no existe

**Solución:**
1. Abre phpMyAdmin (XAMPP) o MySQL Workbench
2. Crea la base de datos:
```sql
CREATE DATABASE agendamiento_politecnico;
```
3. Ejecuta los scripts de inicialización:
   - `especialidades.sql`
   - `doctores_disponibilidad.sql`

---

### **Error 4: "Communications link failure"**

**Causa:** MySQL no está corriendo

**Solución:**
1. Si usas XAMPP: Inicia el módulo MySQL
2. Si usas WAMP: Inicia el servicio MySQL
3. Verifica que el puerto 3306 esté disponible

---

### **Error 5: Puerto diferente**

**Causa:** MySQL está en un puerto diferente al 3306

**Solución:**
Cambia el puerto en la URL:
```java
// Puerto 3307 en lugar de 3306
private static final String DB_URL = "jdbc:mysql://localhost:3307/agendamiento_politecnico?serverTimezone=UTC";
```

---

## 💡 USO DEL SINGLETON

### **Cómo usar DatabaseConnection en tus DAOs:**

**Ejemplo: CitaDAO usando JDBC directo**
```java
public class CitaDAO {
    
    public List<Cita> obtenerTodas() {
        List<Cita> citas = new ArrayList<>();
        
        // ✅ Obtener instancia Singleton
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM cita");
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Cita cita = new Cita();
                cita.setIdCita(rs.getInt("id_cita"));
                // ... mapear campos
                citas.add(cita);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return citas;
    }
}
```

---

## ✅ VENTAJAS DEL SINGLETON

1. **✅ Una sola instancia** - No se crean múltiples conexiones innecesarias
2. **✅ Centralizado** - Un solo lugar para configurar la conexión
3. **✅ Thread-safe** - Sincronizado para múltiples hilos
4. **✅ Fácil de mantener** - Cambios en un solo archivo
5. **✅ Reutilizable** - Todos los DAOs usan la misma instancia

---

## 📊 INTEGRACIÓN ACTUAL

### **Tu proyecto usa DOS formas de acceso a BD:**

#### **1. JPA/ORM (para Cita, Doctor, Disponibilidad, Especialidad)**
- ✅ Ya configurado en `persistence.xml`
- ✅ Usa `JPAUtil.getEntityManager()`
- ✅ NO necesita cambios

#### **2. JDBC Directo (si lo usas en algunos DAOs)**
- ✅ Ahora usa `DatabaseConnection.getInstance()`
- ✅ Patrón Singleton
- ⚠️ Asegúrate de que JPA y JDBC usen la MISMA base de datos

---

## 🔄 PRÓXIMOS PASOS

1. ✅ Configurar `DatabaseConnection.java` con tus credenciales
2. ✅ Ejecutar el main de `DatabaseConnection` para probar
3. ✅ Verificar que la conexión funciona
4. ✅ (Opcional) Actualizar DAOs que usen JDBC directo

---

## 📝 CHECKLIST DE CONFIGURACIÓN

- [ ] Abrir `DatabaseConnection.java`
- [ ] Cambiar `DB_URL` si es necesario
- [ ] Cambiar `DB_USER` si es necesario
- [ ] Cambiar `DB_PASSWORD` si es necesario
- [ ] Ejecutar el `main()` de `DatabaseConnection`
- [ ] Verificar mensaje de éxito en consola
- [ ] Probar la aplicación completa

---

**🎉 Configuración completada cuando veas el mensaje de éxito en consola!**

*Fecha: 4 de Enero 2026*  
*Proyecto: Agendamiento Politécnico - EPN*
