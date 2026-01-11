-- ============================================
-- SCRIPT: updatefinal.sql
-- Combina add_passwords.sql y añade columna 'activo' a estudiante (y asegura en doctor)
-- Idempotente: solo agrega columnas si no existen
-- Base de datos: agendamiento_politecnico
-- Fecha: 2026-01-11
-- ============================================

USE agendamiento_politecnico;

-- ================================
-- 1) AGREGAR password_estudiante SI NO EXISTE
-- ================================
SET @dbname = DATABASE();
SET @tablename = 'estudiante';
SET @columnname = 'password_estudiante';

SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  "SELECT 'La columna password_estudiante ya existe' AS mensaje",
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' VARCHAR(255) NOT NULL DEFAULT ''123456''')
));

PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Asegurar valor por defecto / actualizar existentes
UPDATE estudiante SET password_estudiante = '123456' WHERE password_estudiante IS NULL OR password_estudiante = '';

-- ================================
-- 2) AGREGAR password_doctor SI NO EXISTE
-- ================================
SET @tablename = 'doctor';
SET @columnname = 'password_doctor';

SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  "SELECT 'La columna password_doctor ya existe' AS mensaje",
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' VARCHAR(255) NOT NULL DEFAULT ''123456''')
));

PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Actualizar contraseñas existentes en doctor
UPDATE doctor SET password_doctor = '123456' WHERE password_doctor IS NULL OR password_doctor = '';

-- ================================
-- 3) AGREGAR columna activo A estudiante SI NO EXISTE
-- ================================
SET @tablename = 'estudiante';
SET @columnname = 'activo';

SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  "SELECT 'La columna activo ya existe en estudiante' AS mensaje",
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' BOOLEAN DEFAULT TRUE')
));

PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Asegurar valores para estudiantes existentes
UPDATE estudiante SET activo = TRUE WHERE activo IS NULL;

-- ================================
-- 4) ASEGURAR columna activo EN doctor (por si falta)
-- ================================
SET @tablename = 'doctor';
SET @columnname = 'activo';

SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  "SELECT 'La columna activo ya existe en doctor' AS mensaje",
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' BOOLEAN DEFAULT TRUE')
));

PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Asegurar valores para doctors existentes
UPDATE doctor SET activo = TRUE WHERE activo IS NULL;

-- ================================
-- 5) ACTUALIZAR contraseñas de administrador (si es necesario)
-- ================================
UPDATE administrador SET password_admin = '123456' WHERE password_admin IS NULL OR password_admin = '';

-- ================================
-- 6) INSERCIONES / DATOS DE PRUEBA PARA LAS NUEVAS COLUMNAS
--    (Se actualizan registros existentes para facilitar pruebas)
-- ================================

-- Asegurar que al menos 3 estudiantes tengan contraseñas y estado activo
-- Obtener IDs de estudiantes
SET @e1 = (SELECT id_estudiante FROM estudiante ORDER BY id_estudiante LIMIT 1 OFFSET 0);
SET @e2 = (SELECT id_estudiante FROM estudiante ORDER BY id_estudiante LIMIT 1 OFFSET 1);
SET @e3 = (SELECT id_estudiante FROM estudiante ORDER BY id_estudiante LIMIT 1 OFFSET 2);

UPDATE estudiante
SET password_estudiante = CONCAT('pwd_', id_estudiante)
WHERE id_estudiante IN (@e1, @e2, @e3)
  AND (password_estudiante IS NULL OR password_estudiante = '123456');


-- Asegurar que al menos 3 doctores tengan contraseñas y estado activo
-- Obtener IDs de doctores
SET @d1 = (SELECT id_doctor FROM doctor ORDER BY id_doctor LIMIT 1 OFFSET 0);
SET @d2 = (SELECT id_doctor FROM doctor ORDER BY id_doctor LIMIT 1 OFFSET 1);
SET @d3 = (SELECT id_doctor FROM doctor ORDER BY id_doctor LIMIT 1 OFFSET 2);

UPDATE doctor
SET password_doctor = CONCAT('docpwd_', id_doctor)
WHERE id_doctor IN (@d1, @d2, @d3)
  AND (password_doctor IS NULL OR password_doctor = '123456');


-- Marcar algunos estudiantes como inactivos para pruebas (si existen)
SET @e4 = (SELECT id_estudiante FROM estudiante ORDER BY id_estudiante LIMIT 1 OFFSET 3);

UPDATE estudiante
SET activo = FALSE
WHERE id_estudiante = @e4
  AND (SELECT COUNT(*) FROM estudiante) >= 4;


-- Marcar algún doctor como inactivo para pruebas
SET @d4 = (SELECT id_doctor FROM doctor ORDER BY id_doctor LIMIT 1 OFFSET 4);

UPDATE doctor
SET activo = FALSE
WHERE id_doctor = @d4
  AND (SELECT COUNT(*) FROM doctor) >= 5;


-- ================================
-- 7) CONSULTAS DE VERIFICACIÓN
-- ================================

SELECT '=== ESTUDIANTES (MUESTRA) ===' AS info;
SELECT id_estudiante, id_paciente, nombre_estudiante, correo_estudiante, password_estudiante, activo FROM estudiante LIMIT 10;

SELECT '=== DOCTORES (MUESTRA) ===' AS info;
SELECT id_doctor, cedula, CONCAT(nombre, ' ', apellido) AS nombre_completo, email, password_doctor, activo FROM doctor LIMIT 10;

SELECT '=== ADMINISTRADORES (MUESTRA) ===' AS info;
SELECT id_administrador, id_admin, CONCAT(nombre_admin,' ',apellido_admin) AS nombre_completo, correo_admin, password_admin, rol, activo FROM administrador LIMIT 10;

-- ================================
-- FIN updatefinal.sql
-- ================================
