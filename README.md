# 🏥 Sistema de Agendamiento de Citas - Politécnico Nacional

Sistema web para la gestión de citas médicas del Departamento de Bienestar Estudiantil de la Escuela Politécnica Nacional.

---

## 📋 Tabla de Contenidos

- [Requisitos Previos](#-requisitos-previos)
- [Tecnologías y Versiones](#-tecnologías-y-versiones)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Configuración de la Base de Datos](#-configuración-de-la-base-de-datos)
- [Despliegue en Tomcat](#-despliegue-en-tomcat)
- [Ejecución del Proyecto](#-ejecución-del-proyecto)
- [Funcionalidades](#-funcionalidades)
- [Solución de Problemas](#-solución-de-problemas)

---

## 🔧 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

### 1. **Java Development Kit (JDK)**
- **Versión requerida:** JDK 17 o superior
- **Descargar:** [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) o [OpenJDK](https://adoptium.net/)
- **Verificar instalación:**
  ```cmd
  java -version
  javac -version
  ```
  Debería mostrar: `java version "17.x.x"` o superior

### 2. **Apache Maven**
- **Versión requerida:** Maven 3.8.x o superior
- **Descargar:** [Apache Maven](https://maven.apache.org/download.cgi)
- **Configurar variable de entorno:**
  ```cmd
  MAVEN_HOME=C:\apache-maven-3.9.6
  Path=%Path%;%MAVEN_HOME%\bin
  ```
- **Verificar instalación:**
  ```cmd
  mvn -version
  ```
  Debería mostrar: `Apache Maven 3.x.x`

### 3. **Apache Tomcat**
- **Versión requerida:** Tomcat 10.1.x
- **Descargar:** [Apache Tomcat 10.1](https://tomcat.apache.org/download-10.cgi)
- **Importante:** Usar Tomcat 10.1 (compatible con Jakarta EE 10)

### 4. **MySQL Server**
- **Versión requerida:** MySQL 8.0 o superior
- **Descargar:** [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
- **Puerto por defecto:** 3306
- **Verificar instalación:**
  ```cmd
  mysql --version
  ```

### 5. **IDE Recomendado**
- **Eclipse IDE for Enterprise Java and Web Developers** (2023-12 o superior)
- **Descargar:** [Eclipse IDE](https://www.eclipse.org/downloads/)
- **Alternativa:** IntelliJ IDEA Ultimate

---

## 🛠️ Tecnologías y Versiones

### Backend
| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| **Java** | 17+ | Lenguaje de programación |
| **Jakarta EE** | 10.0.0 | Especificaciones enterprise |
| **JPA/Hibernate** | 4.0.2 | ORM para persistencia |
| **MySQL Connector/J** | 8.3.0 | Driver JDBC para MySQL |

### Frontend
| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| **HTML5** | - | Estructura de páginas |
| **CSS3** | - | Estilos personalizados |
| **JavaScript (ES6+)** | - | Interactividad |
| **JSTL** | 3.0.1 | Tag libraries para JSP |

### Servidor
| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| **Apache Tomcat** | 10.1.x | Servidor de aplicaciones |
| **MySQL** | 8.0+ | Base de datos relacional |

### Herramientas
| Herramienta | Versión | Descripción |
|-------------|---------|-------------|
| **Maven** | 3.8+ | Gestión de dependencias |
| **Git** | 2.x | Control de versiones |

---

## 📁 Estructura del Proyecto

```
Agendamiento_Politecnico5/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controller/           # Controladores (Servlets)
│   │   │   │   ├── ConsultarCitaAsignadaController.java
│   │   │   │   ├── AtenderCitaController.java
│   │   │   │   └── CancelarCitaController.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── dao/              # Data Access Objects
│   │   │   │   │   ├── CitaDAO.java
│   │   │   │   │   ├── DoctorDAO.java
│   │   │   │   │   └── EstudianteDAO.java
│   │   │   │   │
│   │   │   │   └── entity/           # Entidades JPA
│   │   │   │       ├── Cita.java
│   │   │   │       ├── Doctor.java
│   │   │   │       ├── Estudiante.java
│   │   │   │       └── Especialidad.java
│   │   │   │
│   │   │   └── util/
│   │   │       └── JPAUtil.java      # Configuración JPA
│   │   │
│   │   ├── resources/
│   │   │   └── META-INF/
│   │   │       └── persistence.xml   # Configuración JPA/Hibernate
│   │   │
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml           # Descriptor de despliegue
│   │       │
│   │       ├── css/
│   │       │   ├── framework.css
│   │       │   └── styles.css
│   │       │
│   │       ├── js/
│   │       │   ├── auth-temporal.js
│   │       │   ├── atender-cita-nuevo.js
│   │       │   └── cancelar-cita.js
│   │       │
│   │       ├── images/
│   │       │
│   │       ├── inicio.html
│   │       ├── atender-cita.jsp
│   │       └── citas-agendadas.jsp
│   │
│   └── test/                         # Pruebas unitarias
│
├── database/
│   └── init_database.sql             # ⚠️ Script de inicialización
│
├── pom.xml                           # Configuración Maven
└── README.md                         # Este archivo
```

---

## 🚀 Instalación y Configuración

### Paso 1: Clonar el Repositorio

```cmd
git clone https://github.com/tu-usuario/Agendamiento_Politecnico5.git
cd Agendamiento_Politecnico5
```

### Paso 2: Verificar Dependencias Maven

El archivo `pom.xml` contiene todas las dependencias necesarias:

#### **Dependencias Principales:**

```xml
<!-- Jakarta Servlet API 6.0.0 -->
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
    <scope>provided</scope>
</dependency>

<!-- Jakarta Servlet JSP API 3.1.1 -->
<dependency>
    <groupId>jakarta.servlet.jsp</artifactId>
    <artifactId>jakarta.servlet.jsp-api</artifactId>
    <version>3.1.1</version>
    <scope>provided</scope>
</dependency>

<!-- JSTL 3.0.1 -->
<dependency>
    <groupId>org.glassfish.web</groupId>
    <artifactId>jakarta.servlet.jsp.jstl</artifactId>
    <version>3.0.1</version>
</dependency>

<!-- Hibernate Core 6.4.4.Final -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.4.4.Final</version>
</dependency>

<!-- MySQL Connector 8.3.0 -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.3.0</version>
</dependency>

<!-- Gson 2.10.1 -->
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

### Paso 3: Compilar el Proyecto

Abre una terminal en la carpeta del proyecto y ejecuta:

```cmd
mvn clean install
```

**Salida esperada:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXX s
```

**Si hay errores:**
- Verifica que Maven esté correctamente instalado
- Asegúrate de tener conexión a internet para descargar dependencias
- Limpia el repositorio local: `mvn clean`

---

## 💾 Configuración de la Base de Datos

### Paso 1: Crear la Base de Datos

1. **Iniciar MySQL:**
   ```cmd
   mysql -u root -p
   ```

2. **Crear la base de datos:**
   ```sql
   CREATE DATABASE agendamiento_politecnico CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Crear usuario (opcional pero recomendado):**
   ```sql
   CREATE USER 'politecnico_user'@'localhost' IDENTIFIED BY 'politecnico_2024';
   GRANT ALL PRIVILEGES ON agendamiento_politecnico.* TO 'politecnico_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

### Paso 2: Ejecutar el Script de Inicialización

**⚠️ IMPORTANTE: Este paso es OBLIGATORIO**

Ejecuta el archivo `init_database.sql` ubicado en la carpeta raíz del proyecto:

```cmd
mysql -u root -p agendamiento_politecnico < init_database.sql
```

**O desde MySQL Workbench:**
1. Abre MySQL Workbench
2. Conecta a tu servidor MySQL
3. File → Open SQL Script
4. Selecciona `init_database.sql`
5. Ejecuta el script completo (⚡ icono de rayo)

**El script crea:**
- ✅ Tablas: `doctor`, `estudiante`, `especialidad`, `cita`, `disponibilidad`
- ✅ Relaciones y claves foráneas
- ✅ Datos de prueba (doctores, estudiantes, especialidades, citas)

### Paso 3: Verificar la Creación

```sql
USE agendamiento_politecnico;
SHOW TABLES;
SELECT * FROM doctor;
SELECT * FROM cita;
```

**Deberías ver:**
- 5 tablas creadas
- Datos de prueba insertados
- Citas del doctor ID 9 en enero 2026

### Paso 4: Configurar persistence.xml

Verifica que el archivo `src/main/resources/META-INF/persistence.xml` tenga la configuración correcta:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="3.0"
    xmlns="https://jakarta.ee/xml/ns/persistence"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence 
    https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd">
    
    <persistence-unit name="agendamiento_politecnico_pu" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        
        <!-- Entidades -->
        <class>model.entity.Cita</class>
        <class>model.entity.Doctor</class>
        <class>model.entity.Estudiante</class>
        <class>model.entity.Especialidad</class>
        <class>model.entity.Disponibilidad</class>
        
        <properties>
            <!-- Conexión a MySQL -->
            <property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="jakarta.persistence.jdbc.url" 
                      value="jdbc:mysql://localhost:3306/agendamiento_politecnico?serverTimezone=America/Guayaquil"/>
            <property name="jakarta.persistence.jdbc.user" value="root"/>
            <property name="jakarta.persistence.jdbc.password" value="tu_contraseña"/>
            
            <!-- Configuración Hibernate -->
            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.hbm2ddl.auto" value="none"/>
        </properties>
    </persistence-unit>
</persistence>
```

**⚠️ IMPORTANTE:** Cambia `tu_contraseña` por tu contraseña de MySQL.

---

## 🚢 Despliegue en Tomcat

### Opción A: Despliegue desde Eclipse

1. **Importar el proyecto:**
   - File → Import → Maven → Existing Maven Projects
   - Selecciona la carpeta `Agendamiento_Politecnico5`
   - Finish

2. **Configurar Tomcat en Eclipse:**
   - Window → Preferences → Server → Runtime Environments
   - Add → Apache Tomcat v10.1
   - Tomcat installation directory: `C:\apache-tomcat-10.1.x`
   - JRE: Java 17

3. **Agregar proyecto al servidor:**
   - Click derecho en el proyecto → Run As → Run on Server
   - Selecciona Tomcat 10.1
   - Finish

4. **Iniciar el servidor:**
   - Botón "Start" en la vista Servers (abajo)
   - Espera a que muestre: `Server startup in [X] milliseconds`

### Opción B: Despliegue Manual

1. **Compilar el proyecto:**
   ```cmd
   mvn clean package
   ```

2. **Copiar el WAR:**
   ```cmd
   copy target\Agendamiento_Politecnico5.war C:\apache-tomcat-10.1.x\webapps\
   ```

3. **Iniciar Tomcat:**
   ```cmd
   cd C:\apache-tomcat-10.1.x\bin
   startup.bat
   ```

4. **Verificar despliegue:**
   - Abre: `http://localhost:8080/manager/html`
   - Verifica que aparezca `Agendamiento_Politecnico5` en la lista

---

## ▶️ Ejecución del Proyecto

### 1. Verificar que Tomcat esté corriendo

```cmd
netstat -ano | findstr :8080
```

Debería mostrar una línea con el puerto 8080 LISTENING.

### 2. Acceder a la aplicación

Abre tu navegador y ve a:

```
http://localhost:8080/Agendamiento_Politecnico5/inicio.html
```

### 3. Rutas disponibles

#### **Página Principal:**
```
http://localhost:8080/Agendamiento_Politecnico5/inicio.html
```

#### **Calendario de Citas (Doctor ID 9):**
```
http://localhost:8080/Agendamiento_Politecnico5/ConsultarCitaAsignadaController?vista=calendario
```

#### **Atender Citas del Día:**
```
http://localhost:8080/Agendamiento_Politecnico5/ConsultarCitaAsignadaController
```

O con fecha específica:
```
http://localhost:8080/Agendamiento_Politecnico5/ConsultarCitaAsignadaController?fecha=2026-01-05
```

---

## ✨ Funcionalidades

### 1. **Consultar Citas Agendadas (Calendario)**
- **Ruta:** `/ConsultarCitaAsignadaController?vista=calendario`
- **Descripción:** Muestra todas las citas del mes del doctor ID 9
- **Funcionalidades:**
  - ✅ Ver estadísticas del mes (Total, Agendadas, Completadas, Canceladas)
  - ✅ Lista de todas las citas con detalles
  - ✅ Atender cita directamente desde el calendario
  - ✅ Cancelar cita con motivo

### 2. **Atender Citas del Día**
- **Ruta:** `/ConsultarCitaAsignadaController`
- **Descripción:** Panel de atención de citas del día
- **Funcionalidades:**
  - ✅ Selector de fecha
  - ✅ Ver todas las citas del día seleccionado
  - ✅ Atender cita con observaciones médicas
  - ✅ Cancelar cita con motivo
  - ✅ Ver estado de cada cita (Agendada, Completada, Cancelada)

### 3. **Atender una Cita**
- **Controlador:** `AtenderCitaController`
- **Proceso:**
  1. Click en "Atender Cita"
  2. Modal para ingresar observaciones
  3. Guardar atención
  4. Estado cambia a "Completada"

### 4. **Cancelar una Cita**
- **Controlador:** `CancelarCitaController`
- **Proceso:**
  1. Click en "Cancelar"
  2. Modal para ingresar motivo
  3. Confirmar cancelación
  4. Estado cambia a "Cancelada"

---

## 🔍 Verificación del Despliegue

### Logs de Eclipse/Tomcat

Deberías ver en la consola:

```
✅ ConsultarCitaAsignadaController inicializado
✅ AtenderCitaController inicializado
✅ CancelarCitaController inicializado

=== INICIO: ConsultarCitaAsignadaController ===
📄 Vista solicitada: Calendario
📅 Mes actual: 2026-01
🔍 Consultando citas del doctor ID 9...
📊 Total citas del doctor ID 9: 2
✅ Citas del doctor encontradas:
   - Cita #1: 2026-01-05 08:00 - Agendada
   - Cita #2: 2026-01-05 08:00 - Cancelada
✅ Datos agregados al request
➡️ Forward a /citas-agendadas.jsp
```

### Consola del Navegador (F12)

```javascript
========================================
🔍 DEBUG CITAS-AGENDADAS: Citas del doctor desde BD
========================================
📊 Total de citas del mes: 2
👨‍⚕️ Doctor: Dr. Enf. Sofía Morales (ID: 9)
📅 Mes consultado: 2026-01

📋 CITA #1:
  ID: 1
  Fecha: 2026-01-05
  Hora: 08:00
  Estado: Agendada
  ...
```

---

## ❗ Solución de Problemas

### Problema 1: Error de conexión a MySQL

**Error:** `Communications link failure`

**Solución:**
1. Verifica que MySQL esté corriendo:
   ```cmd
   net start MySQL80
   ```
2. Verifica usuario y contraseña en `persistence.xml`
3. Prueba conexión manual:
   ```cmd
   mysql -u root -p agendamiento_politecnico
   ```

### Problema 2: ClassNotFoundException

**Error:** `java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver`

**Solución:**
1. Verifica que `mysql-connector-j` esté en `pom.xml`
2. Ejecuta:
   ```cmd
   mvn clean install
   ```
3. Reinicia Tomcat

### Problema 3: HTTP 404 - Recurso no encontrado

**Error:** La página no carga

**Solución:**
1. Verifica que el WAR se haya desplegado:
   ```
   C:\apache-tomcat-10.1.x\webapps\Agendamiento_Politecnico5\
   ```
2. Verifica el contexto en `server.xml`
3. Revisa logs de Tomcat:
   ```
   C:\apache-tomcat-10.1.x\logs\catalina.out
   ```

### Problema 4: No aparecen citas

**Error:** El calendario está vacío

**Solución:**
1. Verifica que ejecutaste `init_database.sql`
2. Consulta la BD:
   ```sql
   SELECT * FROM cita WHERE id_doctor = 9;
   ```
3. Si no hay datos, vuelve a ejecutar el script:
   ```cmd
   mysql -u root -p agendamiento_politecnico < init_database.sql
   ```

### Problema 5: Error de formato de fecha

**Error:** `jakarta.el.ELException: No puedo convertir LocalDate a Date`

**Solución:**
- Este error ya está corregido en el JSP
- Si persiste, verifica que uses `${cita.fechaCita}` en lugar de `<fmt:formatDate>`

### Problema 6: Puerto 8080 en uso

**Error:** `Address already in use: bind`

**Solución:**
1. Encuentra el proceso usando el puerto:
   ```cmd
   netstat -ano | findstr :8080
   ```
2. Mata el proceso:
   ```cmd
   taskkill /PID [número_pid] /F
   ```
3. O cambia el puerto en Tomcat:
   - Edita `server.xml`
   - Busca `<Connector port="8080"`
   - Cambia a `8081` o cualquier puerto libre

---

## 📝 Notas Importantes

### Modo Sin Login (Actual)
- El sistema actualmente NO requiere autenticación
- **Doctor hardcodeado:** ID 9 (Dr. Enf. Sofía Morales)
- Todas las citas mostradas son del doctor ID 9
- Sistema de autenticación temporal en desarrollo

### Arquitectura
- **Patrón:** MVC (Model-View-Controller)
- **ORM:** JPA/Hibernate con FetchType.EAGER
- **Persistencia:** MySQL con Hibernate
- **Frontend:** JSP + JSTL + JavaScript vanilla

### Diagrama de Robustez
El sistema sigue el diagrama de robustez proporcionado:
1. **consultarCitasAgendadasMes(idDoctor)**
2. **obtenerCitasAgendadasDoctorMes(idDoctor): citasMes[]**
3. **mostrar(citasMes)**
4. **seleccionarDiaMes(fecha)**
5. **obtenerCitasDoctorDia(fechaActual): citasDia[]**
6. **obtenerNombreEstudiante(idEstudiante)**
7. **mostrar(citasDiaDetallada)**

---

## 👥 Equipo de Desarrollo

- **Desarrollador:** Erick Caicedo
- **Institución:** Escuela Politécnica Nacional
- **Departamento:** Bienestar Estudiantil

---

## 📄 Licencia

Este proyecto es propiedad de la Escuela Politécnica Nacional. Todos los derechos reservados.

---

## 📞 Soporte

Si encuentras problemas o tienes preguntas:

1. Revisa la sección [Solución de Problemas](#-solución-de-problemas)
2. Verifica los logs de Tomcat y la consola del navegador
3. Asegúrate de haber ejecutado `init_database.sql`
4. Contacta al equipo de desarrollo

---

**Última actualización:** 2026-01-05  
**Versión:** 1.0.0