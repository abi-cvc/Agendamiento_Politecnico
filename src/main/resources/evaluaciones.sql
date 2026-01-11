-- =====================================================
-- Script: evaluaciones.sql
-- Descripción: Crear tabla de evaluaciones
-- =====================================================

USE agendamiento_politecnico;

-- Crear tabla evaluacion
CREATE TABLE IF NOT EXISTS evaluacion (
    id_evaluacion INT AUTO_INCREMENT PRIMARY KEY,
    calificacion INT NOT NULL CHECK (calificacion BETWEEN 1 AND 5),
    comentario TEXT,
    fecha_evaluacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_doctor INT NOT NULL,
    id_estudiante INT NOT NULL,
    id_cita INT,
    
    -- Constraints
    CONSTRAINT fk_evaluacion_doctor FOREIGN KEY (id_doctor) 
        REFERENCES doctor(id_doctor) ON DELETE CASCADE,
    CONSTRAINT fk_evaluacion_estudiante FOREIGN KEY (id_estudiante) 
        REFERENCES estudiante(id_estudiante) ON DELETE CASCADE,
    CONSTRAINT fk_evaluacion_cita FOREIGN KEY (id_cita) 
        REFERENCES cita(id_cita) ON DELETE SET NULL,
    CONSTRAINT unique_evaluacion_cita UNIQUE (id_cita)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para optimizar consultas
CREATE INDEX idx_evaluacion_doctor ON evaluacion(id_doctor);
CREATE INDEX idx_evaluacion_estudiante ON evaluacion(id_estudiante);
CREATE INDEX idx_evaluacion_fecha ON evaluacion(fecha_evaluacion);

-- Insertar datos de prueba (opcional)
INSERT INTO evaluacion (calificacion, comentario, fecha_evaluacion, id_doctor, id_estudiante, id_cita) 
VALUES 
    (5, 'Excelente atención, muy profesional y amable. Resolvió todas mis dudas.', NOW(), 1, 1, NULL),
    (4, 'Buena atención, pero el tiempo de espera fue un poco largo.', NOW(), 1, 2, NULL),
    (5, 'Muy satisfecho con el servicio. La doctora es muy empática.', NOW(), 2, 1, NULL),
    (3, 'Atención regular, esperaba más tiempo en la consulta.', NOW(), 3, 3, NULL),
    (5, 'Excelente profesional, explica todo muy claramente.', NOW(), 4, 2, NULL);

-- Verificar datos insertados
SELECT * FROM evaluacion;