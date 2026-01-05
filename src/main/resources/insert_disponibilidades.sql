-- ============================================
-- SCRIPT PARA AGREGAR DISPONIBILIDADES DE DOCTORES
-- Base de datos: agendamiento_politecnico
-- ============================================

USE agendamiento_politecnico;

-- ============================================
-- ELIMINAR DISPONIBILIDADES EXISTENTES (OPCIONAL)
-- ============================================
-- DELETE FROM disponibilidad;

-- ============================================
-- DISPONIBILIDADES PARA LOS PRÓXIMOS 30 DÍAS
-- ============================================

-- Función helper: Generar disponibilidades para un doctor en horario de oficina
-- Horarios: 8:00-9:00, 9:00-10:00, 10:00-11:00, 11:00-12:00, 
--           14:00-15:00, 15:00-16:00, 16:00-17:00

-- ============================================
-- Dr. María González - Nutrición (ID: 1)
-- Lunes a Viernes: 8:00-12:00, 14:00-17:00
-- ============================================
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
-- Hoy y próximos días (ajusta las fechas según necesites)
(1, '2026-01-06', '08:00:00', '09:00:00', TRUE, 'Lunes'),
(1, '2026-01-06', '09:00:00', '10:00:00', TRUE, 'Lunes'),
(1, '2026-01-06', '10:00:00', '11:00:00', TRUE, 'Lunes'),
(1, '2026-01-06', '11:00:00', '12:00:00', TRUE, 'Lunes'),
(1, '2026-01-06', '14:00:00', '15:00:00', TRUE, 'Lunes'),
(1, '2026-01-06', '15:00:00', '16:00:00', TRUE, 'Lunes'),
(1, '2026-01-06', '16:00:00', '17:00:00', TRUE, 'Lunes'),

(1, '2026-01-07', '08:00:00', '09:00:00', TRUE, 'Martes'),
(1, '2026-01-07', '09:00:00', '10:00:00', TRUE, 'Martes'),
(1, '2026-01-07', '10:00:00', '11:00:00', TRUE, 'Martes'),
(1, '2026-01-07', '11:00:00', '12:00:00', TRUE, 'Martes'),
(1, '2026-01-07', '14:00:00', '15:00:00', TRUE, 'Martes'),
(1, '2026-01-07', '15:00:00', '16:00:00', TRUE, 'Martes'),
(1, '2026-01-07', '16:00:00', '17:00:00', TRUE, 'Martes');

-- ============================================
-- Dra. Ana Pérez - Odontología (ID: 3)
-- Lunes a Viernes: 9:00-13:00, 15:00-18:00
-- ============================================
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(3, '2026-01-06', '09:00:00', '10:00:00', TRUE, 'Lunes'),
(3, '2026-01-06', '10:00:00', '11:00:00', TRUE, 'Lunes'),
(3, '2026-01-06', '11:00:00', '12:00:00', TRUE, 'Lunes'),
(3, '2026-01-06', '12:00:00', '13:00:00', TRUE, 'Lunes'),
(3, '2026-01-06', '15:00:00', '16:00:00', TRUE, 'Lunes'),
(3, '2026-01-06', '16:00:00', '17:00:00', TRUE, 'Lunes'),
(3, '2026-01-06', '17:00:00', '18:00:00', TRUE, 'Lunes'),

(3, '2026-01-07', '09:00:00', '10:00:00', TRUE, 'Martes'),
(3, '2026-01-07', '10:00:00', '11:00:00', TRUE, 'Martes'),
(3, '2026-01-07', '11:00:00', '12:00:00', TRUE, 'Martes'),
(3, '2026-01-07', '12:00:00', '13:00:00', TRUE, 'Martes'),
(3, '2026-01-07', '15:00:00', '16:00:00', TRUE, 'Martes'),
(3, '2026-01-07', '16:00:00', '17:00:00', TRUE, 'Martes'),
(3, '2026-01-07', '17:00:00', '18:00:00', TRUE, 'Martes');

-- ============================================
-- Dra. Laura Mendoza - Psicología (ID: 5)
-- Lunes a Viernes: 8:00-12:00, 14:00-16:00
-- ============================================
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(5, '2026-01-06', '08:00:00', '09:00:00', TRUE, 'Lunes'),
(5, '2026-01-06', '09:00:00', '10:00:00', TRUE, 'Lunes'),
(5, '2026-01-06', '10:00:00', '11:00:00', TRUE, 'Lunes'),
(5, '2026-01-06', '11:00:00', '12:00:00', TRUE, 'Lunes'),
(5, '2026-01-06', '14:00:00', '15:00:00', TRUE, 'Lunes'),
(5, '2026-01-06', '15:00:00', '16:00:00', TRUE, 'Lunes'),

(5, '2026-01-07', '08:00:00', '09:00:00', TRUE, 'Martes'),
(5, '2026-01-07', '09:00:00', '10:00:00', TRUE, 'Martes'),
(5, '2026-01-07', '10:00:00', '11:00:00', TRUE, 'Martes'),
(5, '2026-01-07', '11:00:00', '12:00:00', TRUE, 'Martes'),
(5, '2026-01-07', '14:00:00', '15:00:00', TRUE, 'Martes'),
(5, '2026-01-07', '15:00:00', '16:00:00', TRUE, 'Martes');

-- ============================================
-- NOTA: Puedes generar más disponibilidades con un script
-- o usar un procedimiento almacenado
-- ============================================

-- Verificar las disponibilidades insertadas
SELECT 
    d.id_disponibilidad,
    doc.nombre,
    doc.apellido,
    e.titulo AS especialidad,
    d.fecha,
    d.hora_inicio,
    d.hora_fin,
    d.disponible,
    d.dia_semana
FROM disponibilidad d
INNER JOIN doctor doc ON d.id_doctor = doc.id_doctor
INNER JOIN especialidad e ON doc.id_especialidad = e.id_especialidad
ORDER BY d.fecha, d.hora_inicio;

SELECT '¡Disponibilidades creadas exitosamente!' AS resultado;
