# 🚀 GUÍA COMPLETA DE DESPLIEGUE - Sistema de Agendamiento Politécnico

## 📋 Tabla de Contenidos

1. [Requisitos del Sistema](#requisitos-del-sistema)
2. [Tecnologías Utilizadas](#tecnologías-utilizadas)
3. [Instalación de Herramientas](#instalación-de-herramientas)
4. [Configuración de la Base de Datos](#configuración-de-la-base-de-datos)
5. [Configuración del Proyecto](#configuración-del-proyecto)
6. [Compilación y Despliegue](#compilación-y-despliegue)
7. [Verificación del Sistema](#verificación-del-sistema)
8. [Solución de Problemas](#solución-de-problemas)

---

## 📦 Requisitos del Sistema

### **Hardware Mínimo:**
- **CPU**: Intel Core i3 o equivalente
- **RAM**: 4 GB (recomendado 8 GB)
- **Disco**: 10 GB libres
- **Sistema Operativo**: Windows 10/11, Linux, macOS

### **Software Requerido:**
- Java JDK 21+
- Apache Maven 3.8+
- Apache Tomcat 10.1+
- MySQL 8.0+ (XAMPP o standalone)
- Eclipse IDE / IntelliJ IDEA (opcional)
- Git (para clonar el repositorio)

---

## 🛠️ Tecnologías Utilizadas

### **Backend:**
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 21 | Lenguaje de programación |
| **Jakarta EE** | 10 | APIs empresariales |
| **JPA / Hibernate** | 4.0.2 (EclipseLink) | ORM - Persistencia |
| **Servlets** | 6.0 | Controladores web |
| **JSTL** | 3.0 | Etiquetas JSP |

### **Frontend:**
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **JSP** | 3.1 | Vistas dinámicas |
| **HTML5** | - | Estructura |
| **CSS3** | - | Estilos |
| **JavaScript** | ES6+ | Interactividad |

### **Base de Datos:**
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **MySQL** | 8.0.3+ | Base de datos relacional |
| **JDBC** | 8.3.0 | Conector MySQL |

### **Servidor:**
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Apache Tomcat** | 10.1 | Servidor de aplicaciones |

### **Build Tool:**
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Maven** | 3.8+ | Gestión de dependencias |

---

## 📥 Instalación de Herramientas

### **1. Instalar Java JDK 21**

#### **Windows:**
```bash
# Descargar desde: https://www.oracle.com/java/technologies/downloads/#java21
# O usar OpenJDK: https://adoptium.net/

# Verificar instalación:
java -version
javac -version
```

#### **Variables de Entorno:**
```
JAVA_HOME = C:\Program Files\Java\jdk-21
PATH = %JAVA_HOME%\bin;%PATH%
```

#### **Linux/macOS:**
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-21-jdk

# macOS (Homebrew)
brew install openjdk@21

# Verificar
java -version
```

---

### **2. Instalar Apache Maven**

#### **Windows:**
```bash
# Descargar desde: https://maven.apache.org/download.cgi
# Extraer en: C:\apache-maven-3.9.6

# Variables de Entorno:
MAVEN_HOME = C:\apache-maven-3.9.6
PATH = %MAVEN_HOME%\bin;%PATH%

# Verificar
mvn -version
```

#### **Linux/macOS:**
```bash
# Ubuntu/Debian
sudo apt install maven

# macOS (Homebrew)
brew install maven

# Verificar
mvn -version
```

---

### **3. Instalar Apache Tomcat 10.1**

#### **Opción A: Instalación Standalone**

**Windows:**
```bash
# Descargar desde: https://tomcat.apache.org/download-10.cgi
# Descargar: apache-tomcat-10.1.x.zip
# Extraer en: C:\apache-tomcat-10.1.x

# Variables de Entorno:
CATALINA_HOME = C:\apache-tomcat-10.1.x

# Iniciar servidor:
cd C:\apache-tomcat-10.1.x\bin
startup.bat

# Detener servidor:
shutdown.bat
```

**Linux/macOS:**
```bash
# Descargar y extraer
wget https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.x/bin/apache-tomcat-10.1.x.tar.gz
tar -xzf apache-tomcat-10.1.x.tar.gz
sudo mv apache-tomcat-10.1.x /opt/tomcat

# Iniciar servidor:
/opt/tomcat/bin/startup.sh

# Detener servidor:
/opt/tomcat/bin/shutdown.sh
```

#### **Opción B: Tomcat en Eclipse**

1. **Descargar Eclipse IDE for Enterprise Java**
   - https://www.eclipse.org/downloads/

2. **Agregar Tomcat en Eclipse:**
   - `Window` → `Preferences` → `Server` → `Runtime Environments`
   - Click en `Add...`
   - Seleccionar `Apache Tomcat v10.1`
   - Indicar la ruta de instalación de Tomcat
   - Click en `Finish`

3. **Crear Servidor:**
   - Vista `Servers` → Click derecho → `New` → `Server`
   - Seleccionar `Tomcat v10.1 Server`
   - Click en `Finish`

---

### **4. Instalar MySQL (XAMPP)**

#### **Windows:**
```bash
# Descargar desde: https://www.apachefriends.org/
# Instalar XAMPP (incluye MySQL, phpMyAdmin)
# Ruta de instalación: C:\xampp

# Iniciar MySQL:
# 1. Abrir XAMPP Control Panel
# 2. Click en "Start" junto a MySQL

# Acceder a phpMyAdmin:
# http://localhost/phpmyadmin/
```

#### **Linux:**
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install mysql-server

# Iniciar MySQL
sudo systemctl start mysql
sudo systemctl enable mysql

# Configurar contraseña root
sudo mysql_secure_installation
```

#### **macOS:**
```bash
# Homebrew
brew install mysql

# Iniciar MySQL
brew services start mysql

# Configurar
mysql_secure_installation
```

---

## 🗄️ Configuración de la Base de Datos

### **Paso 1: Crear la Base de Datos**

#### **Opción A: Usando phpMyAdmin (XAMPP)**

1. Abrir navegador: `http://localhost/phpmyadmin/`
2. Click en `Nueva` (crear base de datos)
3. Nombre: `agendamiento_politecnico`
4. Cotejamiento: `utf8mb4_general_ci`
5. Click en `Crear`

#### **Opción B: Usando MySQL CLI**

```sql
-- Abrir terminal MySQL
mysql -u root -p

-- Crear base de datos
CREATE DATABASE agendamiento_politecnico CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- Verificar
SHOW DATABASES;

-- Usar la base de datos
USE agendamiento_politecnico;
```

---

### **Paso 2: Ejecutar Scripts SQL**

Los scripts están en: `src/main/resources/`

#### **Orden de Ejecución:**

1. **`init_database.sql`** - Crea todas las tablas
2. **`especialidades.sql`** - Inserta especialidades médicas
3. **`doctores_disponibilidad.sql`** - Inserta doctores
4. **`insert_disponibilidades.sql`** - Inserta horarios disponibles
5. **`crear_usuario_prueba.sql`** - Crea usuarios de prueba
6. **`insert_citas_prueba.sql`** - Inserta citas de ejemplo (opcional)

#### **Ejecutar Scripts:**

**Opción A: phpMyAdmin**
```
1. Abrir phpMyAdmin
2. Seleccionar base de datos "agendamiento_politecnico"
3. Click en pestaña "SQL"
4. Copiar contenido del archivo .sql
5. Pegar en el campo de texto
6. Click en "Continuar"
7. Repetir para cada script en orden
```

**Opción B: MySQL CLI**
```bash
# Opción 1: Desde línea de comandos
mysql -u root -p agendamiento_politecnico < src/main/resources/init_database.sql
mysql -u root -p agendamiento_politecnico < src/main/resources/especialidades.sql
mysql -u root -p agendamiento_politecnico < src/main/resources/doctores_disponibilidad.sql
mysql -u root -p agendamiento_politecnico < src/main/resources/insert_disponibilidades.sql
mysql -u root -p agendamiento_politecnico < src/main/resources/crear_usuario_prueba.sql

# Opción 2: Desde MySQL prompt
mysql -u root -p
USE agendamiento_politecnico;
SOURCE C:/ruta/completa/src/main/resources/init_database.sql;
SOURCE C:/ruta/completa/src/main/resources/especialidades.sql;
-- Repetir para cada script
```

---

### **Paso 3: Verificar Datos**

```sql
-- Conectar a MySQL
mysql -u root -p
USE agendamiento_politecnico;

-- Verificar tablas creadas
SHOW TABLES;

-- Verificar datos
SELECT * FROM especialidad;
SELECT * FROM doctor;
SELECT * FROM estudiante;
SELECT * FROM disponibilidad LIMIT 10;
SELECT * FROM cita;

-- Contar registros
SELECT 
    (SELECT COUNT(*) FROM especialidad) AS especialidades,
    (SELECT COUNT(*) FROM doctor) AS doctores,
    (SELECT COUNT(*) FROM estudiante) AS estudiantes,
    (SELECT COUNT(*) FROM disponibilidad) AS disponibilidades,
    (SELECT COUNT(*) FROM cita) AS citas;
```

**Resultado esperado:**
```
+-----------------+----------+-------------+------------------+-------+
| especialidades  | doctores | estudiantes | disponibilidades | citas |
+-----------------+----------+-------------+------------------+-------+
|              6  |    6-12  |     3+      |       50+        |  0-6  |
+-----------------+----------+-------------+------------------+-------+
```

---

## ⚙️ Configuración del Proyecto

### **Paso 1: Clonar/Descargar el Proyecto**

#### **Opción A: Con Git**
```bash
git clone https://github.com/tu-usuario/Agendamiento_Politecnico5.git
cd Agendamiento_Politecnico5
```

#### **Opción B: Descargar ZIP**
```
1. Descargar ZIP del repositorio
2. Extraer en: C:\Users\TU_USUARIO\git\Agendamiento_Politecnico5
```

---

### **Paso 2: Configurar `persistence.xml`**

**Archivo:** `src/main/resources/META-INF/persistence.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="3.0" 
    xmlns="https://jakarta.ee/xml/ns/persistence"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence 
                        https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd">
    
    <persistence-unit name="AgendamientoPU" transaction-type="RESOURCE_LOCAL">
        <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
        
        <!-- ===== ENTIDADES JPA ===== -->
        <class>model.entity.Especialidad</class>
        <class>model.entity.Doctor</class>
        <class>model.entity.Disponibilidad</class>
        <class>model.entity.Cita</class>
        <class>model.entity.Estudiante</class>
        <class>model.entity.Administrador</class>
        
        <properties>
            <!-- ===== CONFIGURACIÓN DE BASE DE DATOS ===== -->
            <!-- ⚠️ MODIFICAR ESTOS VALORES SEGÚN TU CONFIGURACIÓN ⚠️ -->
            
            <!-- URL de conexión -->
            <property name="jakarta.persistence.jdbc.url" 
                      value="jdbc:mysql://localhost:3306/agendamiento_politecnico?useSSL=false&amp;serverTimezone=America/Guayaquil&amp;allowPublicKeyRetrieval=true"/>
            
            <!-- Usuario de MySQL -->
            <property name="jakarta.persistence.jdbc.user" value="root"/>
            
            <!-- Contraseña de MySQL -->
            <!-- ⚠️ CAMBIAR "peysi123" POR TU CONTRASEÑA ⚠️ -->
            <property name="jakarta.persistence.jdbc.password" value="peysi123"/>
            
            <!-- Driver JDBC -->
            <property name="jakarta.persistence.jdbc.driver" 
                      value="com.mysql.cj.jdbc.Driver"/>
            
            <!-- ===== CONFIGURACIÓN DE ECLIPSELINK ===== -->
            <property name="eclipselink.logging.level" value="FINE"/>
            <property name="eclipselink.logging.parameters" value="true"/>
            <property name="eclipselink.logging.timestamp" value="true"/>
            
            <!-- NO crear/actualizar tablas automáticamente (ya existen) -->
            <property name="eclipselink.ddl-generation" value="none"/>
        </properties>
    </persistence-unit>
</persistence>
```

#### **⚠️ Valores a Modificar:**

| Propiedad | Descripción | Valor por defecto |
|-----------|-------------|-------------------|
| `jdbc.url` | URL de MySQL | `localhost:3306` |
| `jdbc.user` | Usuario MySQL | `root` |
| `jdbc.password` | Contraseña MySQL | `peysi123` ⚠️ **CAMBIAR** |
| `serverTimezone` | Zona horaria | `America/Guayaquil` |

---

### **Paso 3: Verificar `pom.xml`**

**Archivo:** `pom.xml`

Verificar que contenga todas las dependencias:

```xml
<dependencies>
    <!-- Jakarta Servlet API -->
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- JSP y JSTL -->
    <dependency>
        <groupId>jakarta.servlet.jsp.jstl</groupId>
        <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
        <version>3.0.0</version>
    </dependency>
    
    <dependency>
        <groupId>org.glassfish.web</groupId>
        <artifactId>jakarta.servlet.jsp.jstl</artifactId>
        <version>3.0.1</version>
    </dependency>
    
    <!-- JPA / EclipseLink -->
    <dependency>
        <groupId>org.eclipse.persistence</groupId>
        <artifactId>eclipselink</artifactId>
        <version>4.0.2</version>
    </dependency>
    
    <!-- MySQL Connector -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>8.3.0</version>
    </dependency>
</dependencies>
```

---

## 🔨 Compilación y Despliegue

### **Método 1: Usando Maven desde Línea de Comandos**

```bash
# 1. Navegar al directorio del proyecto
cd C:\Users\ERICK CAICEDO\git\Agendamiento_Politecnico5

# 2. Limpiar compilaciones anteriores
mvn clean

# 3. Compilar el proyecto
mvn compile

# 4. Empaquetar (crear WAR)
mvn package

# 5. Verificar que se creó el WAR
# Buscar en: target/01_MiProyecto.war
dir target\*.war

# 6. Copiar WAR a Tomcat
copy target\01_MiProyecto.war C:\apache-tomcat-10.1.x\webapps\

# 7. Iniciar Tomcat
cd C:\apache-tomcat-10.1.x\bin
startup.bat

# 8. Ver logs
tail.bat
```

#### **Verificar Despliegue:**
```
1. Abrir navegador
2. Ir a: http://localhost:8080/01_MiProyecto/
3. Debe cargar la página de inicio
```

---

### **Método 2: Usando Eclipse IDE**

#### **Paso 1: Importar Proyecto**

```
1. File → Import
2. Seleccionar "Maven" → "Existing Maven Projects"
3. Next
4. Browse: Seleccionar carpeta del proyecto
5. Finish
```

#### **Paso 2: Actualizar Dependencias**

```
1. Click derecho en el proyecto
2. Maven → Update Project
3. Seleccionar "Force Update of Snapshots/Releases"
4. OK
```

#### **Paso 3: Configurar Tomcat**

```
1. Vista "Servers" (Window → Show View → Servers)
2. Click derecho en servidor Tomcat
3. "Add and Remove..."
4. Seleccionar el proyecto
5. "Add >"
6. Finish
```

#### **Paso 4: Desplegar**

```
1. Click derecho en servidor Tomcat
2. "Clean..."
3. "Publish"
4. Click derecho nuevamente
5. "Start" o "Restart"
```

#### **Paso 5: Ver Logs**

```
Vista "Console" mostrará los logs de Tomcat
Buscar mensajes como:
- "Deployment of web application directory has finished"
- "Server startup in [XXXX] milliseconds"
```

---

### **Método 3: IntelliJ IDEA**

#### **Paso 1: Abrir Proyecto**

```
1. File → Open
2. Seleccionar carpeta del proyecto
3. Esperar a que Maven descargue dependencias
```

#### **Paso 2: Configurar Tomcat**

```
1. Run → Edit Configurations
2. + (Add New Configuration)
3. Tomcat Server → Local
4. Configure → Seleccionar ruta de Tomcat
5. Deployment tab → + → Artifact
6. Seleccionar "01_MiProyecto:war exploded"
7. Application context: /01_MiProyecto
8. Apply → OK
```

#### **Paso 3: Ejecutar**

```
1. Click en botón "Run" (▶️)
2. O presionar Shift + F10
3. Navegador se abrirá automáticamente
```

---

## ✅ Verificación del Sistema

### **1. Verificar Servidor**

```bash
# Verificar que Tomcat está corriendo
# Windows
netstat -ano | findstr :8080

# Linux/macOS
netstat -an | grep 8080

# Debe mostrar:
# TCP    0.0.0.0:8080    0.0.0.0:0    LISTENING
```

### **2. Verificar Base de Datos**

```sql
-- Conectar a MySQL
mysql -u root -p

-- Verificar conexiones
SHOW PROCESSLIST;

-- Debe aparecer una conexión desde el pool de JPA
```

### **3. Verificar Aplicación**

#### **URLs a Probar:**

| URL | Descripción | Esperado |
|-----|-------------|----------|
| `http://localhost:8080/01_MiProyecto/` | Página principal | Carga sin errores |
| `http://localhost:8080/01_MiProyecto/views/inicio.jsp` | Inicio | Muestra información |
| `http://localhost:8080/01_MiProyecto/views/especialidades.jsp` | Especialidades | Lista de especialidades |
| `http://localhost:8080/01_MiProyecto/ConsultarCitasAgendadasController` | Consultar citas | Lista o mensaje "sin citas" |
| `http://localhost:8080/01_MiProyecto/ConsultarCitaAsignadaController` | Atender citas | Lista de citas del día |

### **4. Verificar Logs**

#### **Tomcat Logs:**

**Windows:**
```
C:\apache-tomcat-10.1.x\logs\catalina.out
```

**Linux/macOS:**
```
/opt/tomcat/logs/catalina.out
```

#### **Buscar en Logs:**

✅ **Mensajes de Éxito:**
```
INFO: Deploying web application
INFO: Deployment of web application has finished
✅ ConsultarCitasAgendadasController inicializado
✅ CitaDAO creado correctamente
```

❌ **Mensajes de Error:**
```
ERROR: Cannot create PoolableConnectionFactory
ERROR: Access denied for user 'root'@'localhost'
ERROR: Unknown database 'agendamiento_politecnico'
```

---

## 🧪 Pruebas Funcionales

### **Test 1: Consultar Citas**

```
1. Ir a: http://localhost:8080/01_MiProyecto/ConsultarCitasAgendadasController
2. Debe mostrar:
   - Lista de todas las citas en el sistema
   - O mensaje "No hay citas agendadas"
3. Si hay citas, cada tarjeta debe mostrar:
   - Fecha y hora
   - Especialidad
   - Doctor asignado
   - Estudiante
   - Motivo
   - Badge de estado
```

### **Test 2: Atender Cita**

```
1. Ir a: http://localhost:8080/01_MiProyecto/ConsultarCitaAsignadaController
2. Seleccionar una fecha con citas
3. Click en "Atender Cita"
4. Debe abrir modal
5. Ingresar observaciones
6. Click en "Confirmar"
7. Debe mostrar: "✅ Cita atendida exitosamente"
8. Cita debe cambiar a estado "Completada"
```

### **Test 3: Cancelar Cita**

```
1. En cualquier vista con citas
2. Click en "Cancelar Cita"
3. Confirmar en el diálogo
4. Debe mostrar: "✅ Cita cancelada exitosamente"
5. Cita debe cambiar a estado "Cancelada"
```

### **Test 4: Agendar Cita**

```
1. Ir a: http://localhost:8080/01_MiProyecto/AgendarCitasController
2. Seleccionar especialidad
3. Seleccionar doctor
4. Seleccionar fecha
5. Seleccionar hora disponible
6. Ingresar motivo
7. Click en "Agendar"
8. Debe mostrar confirmación
9. Verificar en base de datos
```

---

## 🐛 Solución de Problemas

### **Problema 1: Error de Conexión a MySQL**

#### **Error:**
```
Cannot create PoolableConnectionFactory
Access denied for user 'root'@'localhost'
```

#### **Solución:**

```sql
-- Verificar contraseña
mysql -u root -p

-- Si no puedes conectar, resetear contraseña:
-- Windows (XAMPP):
1. Abrir XAMPP Control Panel
2. Stop MySQL
3. Click en "Config" → "my.ini"
4. Buscar [mysqld]
5. Agregar: skip-grant-tables
6. Guardar y reiniciar MySQL
7. Conectar sin contraseña:
mysql -u root

-- Cambiar contraseña:
ALTER USER 'root'@'localhost' IDENTIFIED BY 'nueva_contraseña';
FLUSH PRIVILEGES;

-- Linux/macOS:
sudo mysql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'nueva_contraseña';
FLUSH PRIVILEGES;
```

**Actualizar `persistence.xml`:**
```xml
<property name="jakarta.persistence.jdbc.password" value="nueva_contraseña"/>
```

---

### **Problema 2: Base de Datos No Existe**

#### **Error:**
```
Unknown database 'agendamiento_politecnico'
```

#### **Solución:**

```sql
-- Conectar a MySQL
mysql -u root -p

-- Crear base de datos
CREATE DATABASE agendamiento_politecnico CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- Verificar
SHOW DATABASES;

-- Ejecutar scripts SQL en orden
```

---

### **Problema 3: Puerto 8080 en Uso**

#### **Error:**
```
Address already in use: bind
Port 8080 is already in use
```

#### **Solución:**

**Opción A: Cambiar Puerto de Tomcat**

**Archivo:** `conf/server.xml`
```xml
<!-- Buscar y cambiar 8080 por 8081 o 9090 -->
<Connector port="8081" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

**Opción B: Liberar Puerto 8080**

**Windows:**
```cmd
# Ver qué proceso usa el puerto
netstat -ano | findstr :8080

# Terminar proceso (sustituir PID)
taskkill /PID [número_pid] /F
```

**Linux/macOS:**
```bash
# Ver proceso
lsof -i :8080

# Terminar proceso
kill -9 [PID]
```

---

### **Problema 4: Error 404 - Aplicación No Encontrada**

#### **Error:**
```
HTTP Status 404 – Not Found
The requested resource [/01_MiProyecto/] is not available
```

#### **Solución:**

```
1. Verificar que el WAR se desplegó:
   - Buscar carpeta: webapps/01_MiProyecto/
   - Debe existir

2. Reiniciar Tomcat:
   shutdown.bat
   startup.bat

3. Ver logs de despliegue:
   logs/catalina.out

4. Verificar context name en Eclipse:
   - Properties del proyecto
   - Web Project Settings
   - Context root: /01_MiProyecto
```

---

### **Problema 5: Error 500 - Internal Server Error**

#### **Error:**
```
HTTP Status 500 – Internal Server Error
```

#### **Solución:**

```
1. Ver logs de Tomcat para detalles del error

2. Errores comunes:
   - persistence.xml mal configurado
   - Entidades JPA sin anotaciones
   - Clases no encontradas (ClassNotFoundException)

3. Verificar que todas las dependencias están:
   mvn clean install

4. Limpiar y redesplegar:
   - Clean Tomcat work directory
   - Clean proyecto
   - Rebuild
   - Deploy nuevamente
```

---

### **Problema 6: JSP No Renderiza Correctamente**

#### **Error:**
```
JSTL tags not working
${variable} showing as literal text
```

#### **Solución:**

```xml
<!-- Verificar que el JSP tiene la directiva: -->
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!-- NO usar versión antigua: -->
<!-- ❌ <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> -->
```

```xml
<!-- Verificar dependencias en pom.xml: -->
<dependency>
    <groupId>jakarta.servlet.jsp.jstl</groupId>
    <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
    <version>3.0.0</version>
</dependency>

<dependency>
    <groupId>org.glassfish.web</groupId>
    <artifactId>jakarta.servlet.jsp.jstl</artifactId>
    <version>3.0.1</version>
</dependency>
```

---

### **Problema 7: Maven No Descarga Dependencias**

#### **Error:**
```
Could not resolve dependencies
Failed to read artifact descriptor
```

#### **Solución:**

```bash
# Limpiar caché de Maven
mvn dependency:purge-local-repository

# Forzar actualización
mvn clean install -U

# Si persiste, eliminar carpeta .m2/repository
# Windows:
rmdir /s /q C:\Users\TU_USUARIO\.m2\repository

# Linux/macOS:
rm -rf ~/.m2/repository

# Recompilar
mvn clean install
```

---

## 📊 Estructura Final del Proyecto

```
Agendamiento_Politecnico5/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controller/
│   │   │   │   ├── AgendarCitasController.java
│   │   │   │   ├── AtenderCitaController.java
│   │   │   │   ├── CancelarCitaController.java
│   │   │   │   ├── ConsultarCitaAsignadaController.java
│   │   │   │   ├── ConsultarCitasAgendadasController.java
│   │   │   │   └── LoginServlet.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── entity/
│   │   │   │   │   ├── Cita.java
│   │   │   │   │   ├── Doctor.java
│   │   │   │   │   ├── Especialidad.java
│   │   │   │   │   ├── Estudiante.java
│   │   │   │   │   ├── Disponibilidad.java
│   │   │   │   │   └── Administrador.java
│   │   │   │   │
│   │   │   │   └── dao/
│   │   │   │       ├── CitaDAO.java
│   │   │   │       ├── DoctorDAO.java
│   │   │   │       ├── EspecialidadDAO.java
│   │   │   │       ├── EstudianteDAO.java
│   │   │   │       ├── DisponibilidadDAO.java
│   │   │   │       └── AdministradorDAO.java
│   │   │   │
│   │   │   └── util/
│   │   │       └── JPAUtil.java
│   │   │
│   │   ├── resources/
│   │   │   ├── META-INF/
│   │   │   │   └── persistence.xml ⚠️ CONFIGURAR
│   │   │   │
│   │   │   └── *.sql (Scripts de BD)
│   │   │
│   │   └── webapp/
│   │       ├── atender-cita.jsp
│   │       ├── consultar-citas.jsp
│   │       ├── index.jsp
│   │       │
│   │       ├── views/
│   │       │   ├── inicio.jsp
│   │       │   ├── especialidades.jsp
│   │       │   └── agendamientos.jsp
│   │       │
│   │       ├── js/
│   │       │   ├── atender-cita-nuevo.js
│   │       │   ├── cancelar-cita.js
│   │       │   └── consultar-citas.js
│   │       │
│   │       ├── styles.css
│   │       ├── framework.css
│   │       │
│   │       ├── images/
│   │       │   └── logo_epn.png
│   │       │
│   │       └── WEB-INF/
│   │           └── web.xml
│   │
│   └── test/ (vacío por ahora)
│
├── pom.xml ⚠️ VERIFICAR
├── README.md
└── target/ (generado por Maven)
    └── 01_MiProyecto.war
```

---

## 🎯 Checklist de Despliegue

### **Pre-Despliegue:**
- [ ] Java JDK 21 instalado y configurado
- [ ] Maven 3.8+ instalado y en PATH
- [ ] Tomcat 10.1 descargado y configurado
- [ ] MySQL 8.0+ instalado (XAMPP o standalone)
- [ ] MySQL corriendo en puerto 3306

### **Base de Datos:**
- [ ] Base de datos `agendamiento_politecnico` creada
- [ ] Script `init_database.sql` ejecutado
- [ ] Script `especialidades.sql` ejecutado
- [ ] Script `doctores_disponibilidad.sql` ejecutado
- [ ] Script `insert_disponibilidades.sql` ejecutado
- [ ] Script `crear_usuario_prueba.sql` ejecutado
- [ ] Datos verificados con SELECT

### **Configuración:**
- [ ] `persistence.xml` actualizado con credenciales correctas
- [ ] Usuario MySQL configurado
- [ ] Contraseña MySQL correcta
- [ ] Zona horaria configurada

### **Compilación:**
- [ ] `mvn clean` ejecutado sin errores
- [ ] `mvn compile` ejecutado sin errores
- [ ] `mvn package` ejecutado sin errores
- [ ] Archivo WAR generado en `target/`

### **Despliegue:**
- [ ] WAR copiado a `webapps/` de Tomcat
- [ ] Tomcat iniciado correctamente
- [ ] Logs sin errores críticos
- [ ] Aplicación desplegada en `/01_MiProyecto`

### **Verificación:**
- [ ] URL principal carga: `http://localhost:8080/01_MiProyecto/`
- [ ] Consultar citas funciona
- [ ] Atender cita funciona (modal se abre)
- [ ] Cancelar cita funciona
- [ ] Datos se guardan en BD

---

## 📞 Soporte

### **Recursos Adicionales:**

- **Documentación JPA**: https://jakarta.ee/specifications/persistence/
- **Documentación Tomcat**: https://tomcat.apache.org/tomcat-10.1-doc/
- **Documentación MySQL**: https://dev.mysql.com/doc/
- **Maven Repository**: https://mvnrepository.com/

### **Comandos Útiles:**

```bash
# Ver logs de Tomcat en tiempo real (Windows)
tail -f C:\apache-tomcat-10.1.x\logs\catalina.out

# Ver logs de Tomcat (Linux/macOS)
tail -f /opt/tomcat/logs/catalina.out

# Reiniciar MySQL (XAMPP)
# Desde XAMPP Control Panel: Stop → Start

# Reiniciar MySQL (Linux)
sudo systemctl restart mysql

# Verificar versión de Java
java -version
javac -version

# Verificar versión de Maven
mvn -version

# Limpiar todo y recompilar
mvn clean install

# Ver árbol de dependencias
mvn dependency:tree
```

---

## ✅ Resumen de URLs

| Descripción | URL |
|-------------|-----|
| **Página Principal** | `http://localhost:8080/01_MiProyecto/` |
| **Inicio** | `http://localhost:8080/01_MiProyecto/views/inicio.jsp` |
| **Especialidades** | `http://localhost:8080/01_MiProyecto/views/especialidades.jsp` |
| **Consultar Citas** | `http://localhost:8080/01_MiProyecto/ConsultarCitasAgendadasController` |
| **Atender Citas** | `http://localhost:8080/01_MiProyecto/ConsultarCitaAsignadaController` |
| **Agendar Cita** | `http://localhost:8080/01_MiProyecto/AgendarCitasController` |
| **phpMyAdmin** | `http://localhost/phpmyadmin/` |
| **Tomcat Manager** | `http://localhost:8080/manager/html` |

---

## 🎉 ¡Despliegue Exitoso!

Si llegaste hasta aquí y todo funciona, **¡FELICIDADES!** 🎊

Tu aplicación está correctamente desplegada y lista para usar.

### **Próximos Pasos:**

1. Personalizar estilos y colores
2. Agregar más funcionalidades
3. Implementar sistema de login completo
4. Agregar autenticación y autorización
5. Crear reportes en PDF
6. Implementar notificaciones por email

---

**Fecha de Creación:** 2026-01-04  
**Versión del Documento:** 1.0  
**Última Actualización:** 2026-01-04

---

