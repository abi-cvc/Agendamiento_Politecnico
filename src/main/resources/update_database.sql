-- ============================================
-- SCRIPT PARA AGREGAR TABLAS ESTUDIANTE Y ADMINISTRADOR
-- Base de datos: agendamiento_politecnico
-- ============================================

USE agendamiento_politecnico;

-- ============================================
-- 1. TABLA: estudiante
-- ============================================
CREATE TABLE IF NOT EXISTS estudiante (
    id_estudiante INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente VARCHAR(20) NOT NULL UNIQUE,
    nombre_estudiante VARCHAR(100) NOT NULL,
    apellido_estudiante VARCHAR(100) NOT NULL,
    correo_estudiante VARCHAR(100) NOT NULL UNIQUE,
    INDEX idx_id_paciente (id_paciente),
    INDEX idx_correo_estudiante (correo_estudiante)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 2. TABLA: administrador
-- ============================================
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

-- ============================================
-- 3. AGREGAR COLUMNA id_estudiante A TABLA cita
-- ============================================
-- Primero verificamos si la columna ya existe
SET @dbname = DATABASE();
SET @tablename = "cita";
SET @columnname = "id_estudiante";
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  "SELECT 1",
  CONCAT("ALTER TABLE ", @tablename, " ADD COLUMN ", @columnname, " INT")
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

-- ============================================
-- DATOS DE EJEMPLO: Estudiantes
-- ============================================
INSERT IGNORE INTO estudiante (id_paciente, nombre_estudiante, apellido_estudiante, correo_estudiante) VALUES
('1725896347', 'Juan', 'Pérez', 'juan.perez@epn.edu.ec'),
('1723456789', 'María', 'López', 'maria.lopez@epn.edu.ec'),
('1726789012', 'Carlos', 'Martínez', 'carlos.martinez@epn.edu.ec'),
('1724567890', 'Ana', 'García', 'ana.garcia@epn.edu.ec'),
('1727890123', 'Luis', 'Rodríguez', 'luis.rodriguez@epn.edu.ec');

-- ============================================
-- DATOS DE EJEMPLO: Administradores
-- ============================================
-- NOTA: Las contraseñas están en texto plano por simplicidad
-- En producción se deben usar hash (BCrypt, SHA-256, etc.)
INSERT IGNORE INTO administrador (id_admin, nombre_admin, apellido_admin, correo_admin, password_admin, rol, activo) VALUES
('admin001', 'Pedro', 'Sánchez', 'admin@epn.edu.ec', 'admin123', 'SUPER_ADMIN', TRUE),
('admin002', 'Lucía', 'Fernández', 'lucia.fernandez@epn.edu.ec', 'lucia123', 'ADMIN', TRUE);

-- ============================================
-- VERIFICACIÓN
-- ============================================
SELECT 'Tabla estudiante creada:' AS mensaje, COUNT(*) AS registros FROM estudiante;
SELECT 'Tabla administrador creada:' AS mensaje, COUNT(*) AS registros FROM administrador;
SELECT 'Columna id_estudiante en cita:' AS mensaje, 
       COUNT(*) AS existe 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE table_schema = DATABASE() 
  AND table_name = 'cita' 
  AND column_name = 'id_estudiante';

SELECT '¡Base de datos actualizada correctamente!' AS resultado;
