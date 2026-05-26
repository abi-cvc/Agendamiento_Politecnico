-- ============================================================
-- GENERAR DISPONIBILIDADES HASTA 2028
-- Horario: Lunes-Viernes, 08:00-11:00 y 14:00-16:00
-- Ejecutar en TiDB Cloud > SQL Editor sobre la BD agendamiento_politecnico
-- ============================================================

USE agendamiento_politecnico;

-- Aumentar límite de recursión para cubrir ~950 días
SET SESSION tidb_max_cte_recursion_depth = 1500;

-- Insertar slots para todos los doctores activos (INSERT IGNORE evita duplicados)
WITH RECURSIVE fechas AS (
    SELECT CURDATE() AS fecha
    UNION ALL
    SELECT DATE_ADD(fecha, INTERVAL 1 DAY)
    FROM fechas
    WHERE fecha < '2028-12-31'
)
INSERT IGNORE INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana)
SELECT
    d.id_doctor,
    f.fecha,
    s.hora_inicio,
    s.hora_fin,
    TRUE,
    DAYNAME(f.fecha)
FROM
    (SELECT id_doctor FROM doctor WHERE activo = TRUE) d
    CROSS JOIN (SELECT fecha FROM fechas WHERE DAYOFWEEK(fecha) NOT IN (1, 7)) f
    CROSS JOIN (
        SELECT '08:00:00' AS hora_inicio, '09:00:00' AS hora_fin UNION ALL
        SELECT '09:00:00', '10:00:00'                             UNION ALL
        SELECT '10:00:00', '11:00:00'                             UNION ALL
        SELECT '14:00:00', '15:00:00'                             UNION ALL
        SELECT '15:00:00', '16:00:00'
    ) s;

SELECT CONCAT('Disponibilidades insertadas correctamente') AS resultado;
