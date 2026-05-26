-- ============================================================
-- GENERAR DISPONIBILIDADES HASTA 2028
-- Horario: Lunes-Viernes, 08:00-11:00 y 14:00-16:00
-- Ejecutar en TiDB Cloud > SQL Editor sobre la BD agendamiento_politecnico
-- ============================================================

USE agendamiento_politecnico;

-- Insertar slots usando tabla de números inline (compatible con TiDB Cloud)
-- INSERT IGNORE evita duplicados gracias al UNIQUE KEY de la tabla
INSERT IGNORE INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana)
SELECT
    d.id_doctor,
    DATE_ADD(CURDATE(), INTERVAL n.n DAY) AS fecha,
    s.hora_inicio,
    s.hora_fin,
    TRUE,
    DAYNAME(DATE_ADD(CURDATE(), INTERVAL n.n DAY))
FROM
    (SELECT id_doctor FROM doctor WHERE activo = TRUE) d
    CROSS JOIN (
        SELECT a.N + b.N * 10 + c.N * 100 AS n
        FROM
            (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a,
            (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b,
            (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c
        WHERE a.N + b.N * 10 + c.N * 100 < 970
    ) n
    CROSS JOIN (
        SELECT '08:00:00' AS hora_inicio, '09:00:00' AS hora_fin UNION ALL
        SELECT '09:00:00', '10:00:00'                             UNION ALL
        SELECT '10:00:00', '11:00:00'                             UNION ALL
        SELECT '14:00:00', '15:00:00'                             UNION ALL
        SELECT '15:00:00', '16:00:00'
    ) s
WHERE
    DAYOFWEEK(DATE_ADD(CURDATE(), INTERVAL n.n DAY)) NOT IN (1, 7)
    AND DATE_ADD(CURDATE(), INTERVAL n.n DAY) <= '2028-12-31';
