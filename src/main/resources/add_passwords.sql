-- ============================================
-- SCRIPT PARA AGREGAR CONTRASEÑAS A USUARIOS
-- Base de datos: agendamiento_politecnico
-- ============================================

USE agendamiento_politecnico;

-- ============================================
-- 1. AGREGAR CAMPO PASSWORD A TABLA ESTUDIANTE
-- ============================================

-- Verificar si la columna ya existe antes de agregarla
SET @dbname = DATABASE();
SET @tablename = "estudiante";
SET @columnname = "password_estudiante";

SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  "SELECT 'La columna password_estudiante ya existe' AS mensaje",
  CONCAT("ALTER TABLE ", @tablename, " ADD COLUMN ", @columnname, " VARCHAR(255) NOT NULL DEFAULT '123456'")
));

PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Actualizar contraseñas existentes a 123456
UPDATE estudiante SET password_estudiante = '123456' WHERE password_estudiante IS NULL OR password_estudiante = '';

-- ============================================
-- 2. AGREGAR CAMPO PASSWORD A TABLA DOCTOR
-- ============================================

SET @tablename = "doctor";
SET @columnname = "password_doctor";

SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  "SELECT 'La columna password_doctor ya existe' AS mensaje",
  CONCAT("ALTER TABLE ", @tablename, " ADD COLUMN ", @columnname, " VARCHAR(255) NOT NULL DEFAULT '123456'")
));

PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Actualizar contraseñas existentes a 123456
UPDATE doctor SET password_doctor = '123456' WHERE password_doctor IS NULL OR password_doctor = '';

-- ============================================
-- 3. VERIFICAR/ACTUALIZAR TABLA ADMINISTRADOR
-- ============================================
-- La tabla administrador ya tiene password_admin, solo actualizamos valores

UPDATE administrador SET password_admin = '123456' WHERE password_admin != '123456';

-- ============================================
-- VERIFICACIÓN DE CAMBIOS
-- ============================================

SELECT '=== ESTUDIANTES CON CONTRASEÑA ===' AS info;
SELECT 
    id_estudiante,
    id_paciente,
    CONCAT(nombre_estudiante, ' ', apellido_estudiante) AS nombre_completo,
    correo_estudiante,
    password_estudiante
FROM estudiante
LIMIT 5;

SELECT '=== DOCTORES CON CONTRASEÑA ===' AS info;
SELECT 
    id_doctor,
    cedula,
    CONCAT(nombre, ' ', apellido) AS nombre_completo,
    email,
    password_doctor,
    activo
FROM doctor
LIMIT 5;

SELECT '=== ADMINISTRADORES CON CONTRASEÑA ===' AS info;
SELECT 
    id_administrador,
    id_admin,
    CONCAT(nombre_admin, ' ', apellido_admin) AS nombre_completo,
    correo_admin,
    password_admin,
    rol,
    activo
FROM administrador;

-- ============================================
-- RESUMEN
-- ============================================

SELECT 
    '✅ Script ejecutado exitosamente' AS resultado,
    'Todas las contraseñas establecidas a: 123456' AS contraseña,
    'Tablas modificadas: estudiante, doctor, administrador' AS tablas_modificadas;

-- ============================================
-- EJEMPLOS DE CREDENCIALES PARA LOGIN
-- ============================================

SELECT '=== CREDENCIALES DE EJEMPLO PARA LOGIN ===' AS info;

SELECT 'ESTUDIANTES' AS tipo_usuario, '' AS id, '' AS password
UNION ALL
SELECT 
    'Estudiante' AS tipo_usuario,
    id_paciente AS id,
    password_estudiante AS password
FROM estudiante
LIMIT 3

UNION ALL
SELECT '', '', ''
UNION ALL
SELECT 'DOCTORES' AS tipo_usuario, '' AS id, '' AS password
UNION ALL
SELECT 
    'Doctor' AS tipo_usuario,
    cedula AS id,
    password_doctor AS password
FROM doctor
LIMIT 3

UNION ALL
SELECT '', '', ''
UNION ALL
SELECT 'ADMINISTRADORES' AS tipo_usuario, '' AS id, '' AS password
UNION ALL
SELECT 
    'Admin' AS tipo_usuario,
    id_admin AS id,
    password_admin AS password
FROM administrador;
