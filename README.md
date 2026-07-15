# Bienestar Politécnico 🌟

¡Bienvenido al proyecto **Bienestar Politécnico**! Este sitio web está diseñado para brindar a los estudiantes de la **Escuela Politécnica Nacional** una experiencia integral y accesible para gestionar su bienestar físico, emocional y social. A través de una interfaz moderna y amigable, los usuarios pueden acceder a información sobre servicios de salud, agendar citas médicas, conocer las especialidades disponibles y compartir sus experiencias.

## ✨ Características principales

- **Inicio**: Una cálida bienvenida con información sobre los objetivos y beneficios del área de Bienestar Politécnico.
- **Especialidades**: Descubre los servicios disponibles, como nutrición, odontología, psicología, medicina general y enfermería.
- **Agendamiento de citas**: Agenda tus citas médicas de manera rápida y sencilla.
- **Seguro Politécnico**: Información detallada sobre las coberturas y beneficios del seguro estudiantil.
- **Reseñas**: Comparte tu experiencia y consulta las opiniones de otros estudiantes.
- **Login**: Acceso seguro para gestionar tus citas y servicios personalizados.

## 🎯 Objetivo del proyecto

El objetivo principal de este proyecto es proporcionar una plataforma digital que facilite el acceso a los servicios de bienestar estudiantil, promoviendo la salud integral y el desarrollo académico de los estudiantes.

## 🛠️ Tecnologías utilizadas

- **Java 17** + **Jakarta Servlets**: Lógica de backend y controladores.
- **JPA (EclipseLink)**: Persistencia de datos sobre MySQL.
- **Maven**: Gestión de dependencias y empaquetado (WAR).
- **Apache Tomcat 10**: Servidor de aplicaciones.
- **MySQL** (local) / **TiDB Cloud** (producción): Base de datos.
- **HTML5**, **CSS3** y **JavaScript**: Frontend servido por el mismo WAR.
- **Docker**: Empaquetado y despliegue del proyecto completo (ver sección de abajo).

## 🐳 Cómo levantar el proyecto con Docker

El proyecto está completamente dockerizado. Con **Docker** y **Docker Compose** instalados, no necesitas instalar Java, Maven, Tomcat ni MySQL en tu máquina.

> ⚠️ **`docker-compose.yml` es solo para desarrollo/demo local.** Usa credenciales por defecto (`root`/`1234`) y no está pensado para desplegarse en un servidor con IP pública. Para producción, el proyecto se conecta a **TiDB Cloud** mediante las variables de entorno `DB_URL`, `DB_USER` y `DB_PASSWORD` (ver Notas más abajo).

### Requisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (incluye Docker Compose)

### Pasos

1. Clona el repositorio:
   ```bash
   git clone https://github.com/abi-cvc/Agendamiento_Politecnico.git
   cd Agendamiento_Politecnico
   ```

2. Levanta la aplicación y la base de datos con un solo comando:
   ```bash
   docker compose up --build
   ```
   Esto construye la imagen de la app (compila el proyecto con Maven y lo despliega en Tomcat) y levanta un contenedor de MySQL con el esquema (`agendamiento_politecnico.sql`) precargado automáticamente.

3. Cuando veas en los logs `Server startup in [...] milliseconds`, abre en el navegador:
   ```
   http://localhost:8080
   ```

4. Para detener los contenedores:
   ```bash
   docker compose down
   ```
   (agrega `-v` si además querés borrar los datos de la base: `docker compose down -v`)

### Notas

- La base de datos local usa usuario `root` / contraseña `1234` (definidos en `docker-compose.yml`), solo para desarrollo.
- El puerto de MySQL (3306) se publica únicamente en `127.0.0.1` (localhost), no en `0.0.0.0`: queda accesible desde tu propia máquina (por ejemplo con un cliente MySQL) pero no desde otros equipos de tu red.
- Para apuntar a otra base de datos (por ejemplo TiDB Cloud en producción), se pueden sobreescribir las variables de entorno `DB_URL`, `DB_USER` y `DB_PASSWORD` del servicio `app` en `docker-compose.yml`.

## 📌 ¿Qué incluye?

- **Páginas principales**:
  - `index.html`: Página de inicio con información general.
  - `especialidades.html`: Detalles de las especialidades médicas.
  - `agendamientos.html`: Formulario para agendar citas y lista de citas.
  - `seguros.html`: Información sobre el seguro estudiantil.
  - `reseñas.html`: Sistema de reseñas para compartir experiencias.
  - `login.html`: Página de inicio de sesión.
- **Estilos**:
  - `framework.css`: Estilos reutilizables y variables globales.
  - `styles.css`: Estilos específicos del proyecto.
- **Scripts**:
  - Archivos JavaScript para manejar la autenticación, agendamientos y reseñas.

## 🌐 Demo

El sitio está diseñado para ser responsivo y accesible desde cualquier dispositivo, asegurando una experiencia fluida para todos los usuarios.

---

💙 **Cuidando de ti, cuidamos el futuro de la Politécnica.**
Enlace Deploy: [https://abi-cvc.github.io/Agendamiento_Politecnico/](https://agendamiento-politecnico.onrender.com)
