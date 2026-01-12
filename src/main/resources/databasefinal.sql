-- ============================================
-- DATABASEFINAL.SQL
-- Script completo que incluye: init_database.sql + update_database.sql + updatefinal.sql
-- Adaptado: se removieron columnas foto/descripcion de doctor, se agregó password_doctor y activo
-- Compatible con MySQL/MariaDB (XAMPP)
-- Fecha: 2026-01-11
-- ============================================

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS agendamiento_politecnico
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE agendamiento_politecnico;

-- ==================================================
-- TABLA: especialidad
-- ==================================================
CREATE TABLE IF NOT EXISTS especialidad (
    id_especialidad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    servicios TEXT NOT NULL,
    icono VARCHAR(50),
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==================================================
-- TABLA: estudiante
-- ==================================================
CREATE TABLE IF NOT EXISTS estudiante (
    id_estudiante INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente VARCHAR(20) NOT NULL UNIQUE,
    nombre_estudiante VARCHAR(100) NOT NULL,
    apellido_estudiante VARCHAR(100) NOT NULL,
    correo_estudiante VARCHAR(100) NOT NULL UNIQUE,
    password_estudiante VARCHAR(255) DEFAULT NULL,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_id_paciente (id_paciente),
    INDEX idx_correo_estudiante (correo_estudiante)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==================================================
-- TABLA: administrador
-- ==================================================
CREATE TABLE IF NOT EXISTS administrador (
    id_administrador INT AUTO_INCREMENT PRIMARY KEY,
    id_admin VARCHAR(20) NOT NULL UNIQUE,
    nombre_admin VARCHAR(100) NOT NULL,
    apellido_admin VARCHAR(100) NOT NULL,
    correo_admin VARCHAR(100) NOT NULL UNIQUE,
    password_admin VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL DEFAULT 'ADMIN',
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_id_admin (id_admin),
    INDEX idx_correo_admin (correo_admin)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==================================================
-- TABLA: doctor (sin foto/descripcion, con password y activo)
-- ==================================================
CREATE TABLE IF NOT EXISTS doctor (
    id_doctor INT AUTO_INCREMENT PRIMARY KEY,
    cedula VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(15),
    password_doctor VARCHAR(255) DEFAULT NULL,
    activo BOOLEAN DEFAULT TRUE,
    id_especialidad INT,
    FOREIGN KEY (id_especialidad) REFERENCES especialidad(id_especialidad)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    INDEX idx_cedula (cedula),
    INDEX idx_especialidad (id_especialidad)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==================================================
-- TABLA: disponibilidad
-- ==================================================
CREATE TABLE IF NOT EXISTS disponibilidad (
    id_disponibilidad INT AUTO_INCREMENT PRIMARY KEY,
    id_doctor INT NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    disponible BOOLEAN DEFAULT TRUE,
    dia_semana VARCHAR(20),
    FOREIGN KEY (id_doctor) REFERENCES doctor(id_doctor)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    UNIQUE KEY unique_disponibilidad (id_doctor, fecha, hora_inicio),
    INDEX idx_doctor_fecha (id_doctor, fecha),
    INDEX idx_disponible (disponible)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==================================================
-- TABLA: cita
-- ==================================================
CREATE TABLE IF NOT EXISTS cita (
    id_cita INT AUTO_INCREMENT PRIMARY KEY,
    fecha_cita DATE NOT NULL,
    hora_cita TIME NOT NULL,
    motivo_consulta TEXT NOT NULL,
    estado_cita VARCHAR(50) DEFAULT 'Agendada',
    observacion_cita TEXT,
    id_especialidad INT NOT NULL,
    id_doctor INT,
    id_estudiante INT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_especialidad) REFERENCES especialidad(id_especialidad)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    FOREIGN KEY (id_doctor) REFERENCES doctor(id_doctor)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    FOREIGN KEY (id_estudiante) REFERENCES estudiante(id_estudiante)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    INDEX idx_fecha_cita (fecha_cita),
    INDEX idx_estado (estado_cita),
    INDEX idx_especialidad_cita (id_especialidad),
    INDEX idx_doctor_cita (id_doctor)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==================================================
-- INSERCIONES INICIALES (basadas en init_database.sql, adaptadas)
-- ==================================================

-- Especialidades
INSERT IGNORE INTO especialidad (nombre, titulo, descripcion, servicios, icono) VALUES
('nutricion','Nutrición','Nuestro equipo de nutricionistas...','Evaluación nutricional personalizada|Planes de alimentación según objetivos','🥗'),
('odontologia','Odontología','Mantén tu sonrisa...','Limpieza dental y profilaxis|Tratamiento de caries','🦷'),
('psicologia','Psicología','En un entorno confidencial...','Consulta psicológica individual|Manejo de estrés','🧠'),
('medicina-general','Medicina General','Recibe atención médica integral...','Consulta médica general|Diagnóstico y tratamiento','⚕️'),
('enfermeria','Enfermería','Nuestro equipo de enfermería...','Toma de signos vitales|Curaciones','💉');

-- Doctores (sin foto/descripcion, con password y activo)
INSERT IGNORE INTO doctor (cedula, nombre, apellido, email, telefono, password_doctor, activo, id_especialidad) VALUES
('1234567890','María','González','maria.gonzalez@epn.edu.ec','0987654321','docpwd_1',TRUE,1),
('1234567891','Carlos','Ramírez','carlos.ramirez@epn.edu.ec','0987654322','docpwd_2',TRUE,1),
('1234567892','Ana','Pérez','ana.perez@epn.edu.ec','0987654323','docpwd_3',TRUE,2),
('1234567893','Luis','Torres','luis.torres@epn.edu.ec','0987654324','docpwd_4',TRUE,2),
('1234567894','Laura','Mendoza','laura.mendoza@epn.edu.ec','0987654325','docpwd_5',TRUE,3),
('1234567895','Diego','Salazar','diego.salazar@epn.edu.ec','0987654326','docpwd_6',TRUE,3),
('1234567896','Patricia','Vega','patricia.vega@epn.edu.ec','0987654327','docpwd_7',TRUE,4),
('1234567897','Roberto','Castro','roberto.castro@epn.edu.ec','0987654328','docpwd_8',TRUE,4),
('1234567898','Sofía','Morales','sofia.morales@epn.edu.ec','0987654329','docpwd_9',TRUE,5);

-- Estudiantes
INSERT IGNORE INTO estudiante (id_paciente, nombre_estudiante, apellido_estudiante, correo_estudiante, password_estudiante, activo) VALUES
('1725896347','Juan','Pérez','juan.perez@epn.edu.ec','pwd_1',TRUE),
('1723456789','María','López','maria.lopez@epn.edu.ec','pwd_2',TRUE),
('1726789012','Carlos','Martínez','carlos.martinez@epn.edu.ec','pwd_3',TRUE),
('1724567890','Ana','García','ana.garcia@epn.edu.ec','pwd_4',TRUE),
('1727890123','Luis','Rodríguez','luis.rodriguez@epn.edu.ec','pwd_5',TRUE);

-- Administradores
INSERT IGNORE INTO administrador (id_admin, nombre_admin, apellido_admin, correo_admin, password_admin, rol, activo) VALUES
('admin001','Pedro','Sánchez','admin@epn.edu.ec','admin123','SUPER_ADMIN',TRUE),
('admin002','Lucía','Fernández','lucia.fernandez@epn.edu.ec','lucia123','ADMIN',TRUE);

-- Disponibilidades (ejemplo reducido para evitar un script demasiado largo)
INSERT IGNORE INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY)));

-- ==================================================
-- Actualizaciones (similar al contenido de update_database.sql y updatefinal.sql)
-- Asegurar columna id_estudiante en cita
-- ==================================================
SET @dbname = DATABASE();
SET @tablename = 'cita';
SET @columnname = 'id_estudiante';

SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  "SELECT 1",
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' INT')
));

PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Agregar la foreign key si no existe
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
    WHERE
      (table_name = 'cita')
      AND (table_schema = @dbname)
      AND (column_name = 'id_estudiante')
      AND (referenced_table_name = 'estudiante')
  ) > 0,
  "SELECT 1",
  "ALTER TABLE cita ADD CONSTRAINT fk_cita_estudiante FOREIGN KEY (id_estudiante) REFERENCES estudiante(id_estudiante) ON DELETE SET NULL ON UPDATE CASCADE"
));

PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- ==================================================
-- Asegurar columnas password/activo como en updatefinal.sql (idempotente)
-- ==================================================
-- password_estudiante
SET @tablename = 'estudiante';
SET @columnname = 'password_estudiante';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE (table_name = @tablename) AND (table_schema = @dbname) AND (column_name = @columnname)
  ) > 0,
  "SELECT 'exists'",
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' VARCHAR(255) DEFAULT NULL')
));
PREPARE alterIfNotExists FROM @preparedStatement; EXECUTE alterIfNotExists; DEALLOCATE PREPARE alterIfNotExists;

-- password_doctor
SET @tablename = 'doctor';
SET @columnname = 'password_doctor';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE (table_name = @tablename) AND (table_schema = @dbname) AND (column_name = @columnname)
  ) > 0,
  "SELECT 'exists'",
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' VARCHAR(255) DEFAULT NULL')
));
PREPARE alterIfNotExists FROM @preparedStatement; EXECUTE alterIfNotExists; DEALLOCATE PREPARE alterIfNotExists;

-- activo en estudiante
SET @tablename = 'estudiante';
SET @columnname = 'activo';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE (table_name = @tablename) AND (table_schema = @dbname) AND (column_name = @columnname)
  ) > 0,
  "SELECT 'exists'",
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' BOOLEAN DEFAULT TRUE')
));
PREPARE alterIfNotExists FROM @preparedStatement; EXECUTE alterIfNotExists; DEALLOCATE PREPARE alterIfNotExists;

-- activo en doctor
SET @tablename = 'doctor';
SET @columnname = 'activo';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE (table_name = @tablename) AND (table_schema = @dbname) AND (column_name = @columnname)
  ) > 0,
  "SELECT 'exists'",
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' BOOLEAN DEFAULT TRUE')
));
PREPARE alterIfNotExists FROM @preparedStatement; EXECUTE alterIfNotExists; DEALLOCATE PREPARE alterIfNotExists;

-- ==================================================
-- Actualizar valores por defecto/ejemplo
-- ==================================================
UPDATE estudiante SET password_estudiante = 'pwd_' , activo = TRUE WHERE password_estudiante IS NULL OR password_estudiante = '';
UPDATE doctor SET password_doctor = 'docpwd_' , activo = TRUE WHERE password_doctor IS NULL OR password_doctor = '';

-- ==================================================
-- CONSULTAS DE VERIFICACIÓN
-- ==================================================
SELECT '=== RESUMEN TABLAS ===' AS info;
SELECT COUNT(*) AS total_especialidades FROM especialidad;
SELECT COUNT(*) AS total_doctores FROM doctor;
SELECT COUNT(*) AS total_estudiantes FROM estudiante;
SELECT COUNT(*) AS total_disponibilidades FROM disponibilidad;
SELECT COUNT(*) AS total_citas FROM cita;

-- Fin
SELECT '✅ databasefinal.sql ejecutado' AS mensaje;
