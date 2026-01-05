-- ============================================
-- SCRIPT RÁPIDO: CREAR USUARIO DE PRUEBA
-- ============================================

USE agendamiento_politecnico;

-- Eliminar usuario de prueba si existe
DELETE FROM estudiante WHERE id_paciente = '1725896347';

-- Insertar usuario de prueba
INSERT INTO estudiante (id_paciente, nombre_estudiante, apellido_estudiante, correo_estudiante, password_estudiante) 
VALUES ('1725896347', 'Juan', 'Pérez', 'juan.perez@epn.edu.ec', '123456');

-- Verificar
SELECT 
    id_estudiante,
    id_paciente,
    CONCAT(nombre_estudiante, ' ', apellido_estudiante) AS nombre_completo,
    correo_estudiante,
    password_estudiante
FROM estudiante 
WHERE id_paciente = '1725896347';

SELECT 'Usuario de prueba creado: 1725896347 / 123456' AS resultado;
