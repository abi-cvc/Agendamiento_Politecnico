-- ============================================
-- SCRIPT PARA INSERTAR CITAS DE PRUEBA
-- ============================================

USE agendamiento_politecnico;

-- Insertar citas de prueba (asegúrate de que existan los doctores, especialidades y estudiantes)
-- Ajusta los IDs según tu base de datos

-- Cita 1: Medicina General
INSERT INTO cita (fecha_cita, hora_cita, motivo_consulta, estado_cita, id_especialidad, id_doctor, id_estudiante) 
VALUES 
('2026-01-15', '09:00:00', 'Control médico general y revisión de exámenes de laboratorio', 'Agendada', 1, 1, 1);

-- Cita 2: Odontología
INSERT INTO cita (fecha_cita, hora_cita, motivo_consulta, estado_cita, id_especialidad, id_doctor, id_estudiante) 
VALUES 
('2026-01-16', '10:30:00', 'Limpieza dental y revisión de caries', 'Agendada', 2, 2, 1);

-- Cita 3: Psicología
INSERT INTO cita (fecha_cita, hora_cita, motivo_consulta, estado_cita, id_especialidad, id_doctor, id_estudiante) 
VALUES 
('2026-01-17', '14:00:00', 'Consulta por ansiedad y manejo del estrés académico', 'Confirmada', 3, 3, 2);

-- Cita 4: Nutrición
INSERT INTO cita (fecha_cita, hora_cita, motivo_consulta, estado_cita, id_especialidad, id_doctor, id_estudiante) 
VALUES 
('2026-01-18', '11:00:00', 'Asesoría nutricional y plan de alimentación', 'Agendada', 4, 4, 2);

-- Cita 5: Medicina General (pasada - completada)
INSERT INTO cita (fecha_cita, hora_cita, motivo_consulta, estado_cita, observacion_cita, id_especialidad, id_doctor, id_estudiante) 
VALUES 
('2026-01-02', '08:30:00', 'Dolor de cabeza persistente', 'Completada', 'Se recetó ibuprofeno y reposo. Control en 1 semana.', 1, 1, 1);

-- Cita 6: Odontología (cancelada)
INSERT INTO cita (fecha_cita, hora_cita, motivo_consulta, estado_cita, observacion_cita, id_especialidad, id_doctor, id_estudiante) 
VALUES 
('2026-01-20', '15:00:00', 'Extracción de muela del juicio', 'Cancelada', 'Cancelada por el estudiante - reprogramar', 3, 2, 3);

-- Verificar las citas insertadas
SELECT 
    c.id_cita,
    c.fecha_cita,
    c.hora_cita,
    c.estado_cita,
    e.titulo AS especialidad,
    CONCAT(d.nombre, ' ', d.apellido) AS doctor,
    CONCAT(est.nombre_estudiante, ' ', est.apellido_estudiante) AS estudiante,
    c.motivo_consulta
FROM cita c
LEFT JOIN especialidad e ON c.id_especialidad = e.id_especialidad
LEFT JOIN doctor d ON c.id_doctor = d.id_doctor
LEFT JOIN estudiante est ON c.id_estudiante = est.id_estudiante
ORDER BY c.fecha_cita DESC;
