-- ============================================
-- BASE DE DATOS COMPLETA - AGENDAMIENTO POLITÉCNICO
-- Versión consolidada y limpia
-- Fecha: 2026-01-11
-- ============================================

-- Eliminar base de datos si existe (CUIDADO: esto borra todo)
DROP DATABASE IF EXISTS agendamiento_politecnico;

-- Crear base de datos
CREATE DATABASE agendamiento_politecnico
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE agendamiento_politecnico;

-- ============================================
-- TABLA: especialidad
-- ============================================
CREATE TABLE especialidad (
    id_especialidad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    servicios TEXT NOT NULL,
    icono VARCHAR(50),
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: doctor
-- ============================================
CREATE TABLE doctor (
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
    INDEX idx_especialidad (id_especialidad),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: estudiante
-- ============================================
CREATE TABLE estudiante (
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

-- ============================================
-- TABLA: administrador
-- ============================================
CREATE TABLE administrador (
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
-- TABLA: disponibilidad
-- ============================================
CREATE TABLE disponibilidad (
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

-- ============================================
-- TABLA: cita
-- ============================================
CREATE TABLE cita (
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

-- ============================================
-- TABLA: evaluacion
-- ============================================
CREATE TABLE evaluacion (
    id_evaluacion INT AUTO_INCREMENT PRIMARY KEY,
    calificacion INT NOT NULL CHECK (calificacion BETWEEN 1 AND 5),
    comentario TEXT,
    fecha_evaluacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_doctor INT NOT NULL,
    id_estudiante INT NOT NULL,
    id_cita INT,
    CONSTRAINT fk_evaluacion_doctor FOREIGN KEY (id_doctor) 
        REFERENCES doctor(id_doctor) ON DELETE CASCADE,
    CONSTRAINT fk_evaluacion_estudiante FOREIGN KEY (id_estudiante) 
        REFERENCES estudiante(id_estudiante) ON DELETE CASCADE,
    CONSTRAINT fk_evaluacion_cita FOREIGN KEY (id_cita) 
        REFERENCES cita(id_cita) ON DELETE SET NULL,
    CONSTRAINT unique_evaluacion_cita UNIQUE (id_cita),
    INDEX idx_evaluacion_doctor (id_doctor),
    INDEX idx_evaluacion_estudiante (id_estudiante),
    INDEX idx_evaluacion_fecha (fecha_evaluacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- DATOS INICIALES
-- ============================================

-- Especialidades
INSERT INTO especialidad (nombre, titulo, descripcion, servicios, icono) VALUES
('nutricion', 'Nutrición', 
 'Nuestro equipo de nutricionistas certificados te ayudará a alcanzar tus metas de salud mediante planes personalizados. Ya sea que busques mejorar tu rendimiento deportivo, controlar tu peso o manejar condiciones específicas de salud, te brindamos el acompañamiento necesario con un enfoque científico y humano.',
 'Evaluación nutricional personalizada|Planes de alimentación según objetivos|Seguimiento y control de peso|Asesoría en nutrición deportiva|Educación en autocuidado',
 '🥗'),

('odontologia', 'Odontología',
 'Mantén tu sonrisa saludable con nuestros servicios odontológicos de calidad. Contamos con tecnología moderna y profesionales experimentados que te brindarán atención preventiva, diagnóstica y terapéutica. Tu salud bucal es fundamental para tu bienestar general, y estamos aquí para cuidarla.',
 'Limpieza dental y profilaxis|Tratamiento de caries|Extracciones dentales|Endodoncia|Prevención y educación en salud bucal',
 '🦷'),

('psicologia', 'Psicología',
 'En un entorno confidencial y de apoyo, nuestros psicólogos te acompañan en tu proceso de crecimiento personal y bienestar emocional. Entendemos los desafíos que enfrentas como estudiante y te ofrecemos herramientas efectivas para manejar el estrés, la ansiedad y otros aspectos que afectan tu desarrollo académico y personal.',
 'Consulta psicológica individual|Manejo de estrés y ansiedad|Orientación vocacional|Apoyo en crisis emocionales|Talleres de desarrollo personal',
 '🧠'),

('medicina-general', 'Medicina General',
 'Recibe atención médica integral de profesionales capacitados para diagnosticar, tratar y prevenir enfermedades. Desde consultas de rutina hasta seguimiento de condiciones crónicas, nuestro servicio de medicina general es tu primera línea de atención en salud, con derivaciones oportunas cuando sea necesario.',
 'Consulta médica general|Diagnóstico y tratamiento|Control de enfermedades crónicas|Certificados médicos|Derivaciones a especialistas',
 '⚕️'),

('enfermeria', 'Enfermería',
 'Nuestro equipo de enfermería profesional está disponible para brindarte cuidados de calidad, procedimientos de enfermería y educación en salud. Con calidez humana y profesionalismo, te asistimos en el control de tu salud, administración de medicamentos y prevención de enfermedades para que mantengas tu bienestar durante tu vida académica.',
 'Toma de signos vitales|Curaciones y procedimientos menores|Inyecciones y vacunación|Control de medicación|Educación en salud preventiva',
 '💉');

-- Doctores
INSERT INTO doctor (cedula, nombre, apellido, email, telefono, password_doctor, activo, id_especialidad) VALUES
('1234567890', 'María', 'González', 'maria.gonzalez@epn.edu.ec', '0987654321', 'docpwd_1', TRUE, 1),
('1234567891', 'Carlos', 'Ramírez', 'carlos.ramirez@epn.edu.ec', '0987654322', 'docpwd_2', TRUE, 1),
('1234567892', 'Ana', 'Pérez', 'ana.perez@epn.edu.ec', '0987654323', 'docpwd_3', TRUE, 2),
('1234567893', 'Luis', 'Torres', 'luis.torres@epn.edu.ec', '0987654324', 'docpwd_4', TRUE, 2),
('1234567894', 'Laura', 'Mendoza', 'laura.mendoza@epn.edu.ec', '0987654325', 'docpwd_5', TRUE, 3),
('1234567895', 'Diego', 'Salazar', 'diego.salazar@epn.edu.ec', '0987654326', 'docpwd_6', TRUE, 3),
('1234567896', 'Patricia', 'Vega', 'patricia.vega@epn.edu.ec', '0987654327', 'docpwd_7', TRUE, 4),
('1234567897', 'Roberto', 'Castro', 'roberto.castro@epn.edu.ec', '0987654328', 'docpwd_8', TRUE, 4),
('1234567898', 'Sofía', 'Morales', 'sofia.morales@epn.edu.ec', '0987654329', 'docpwd_9', TRUE, 5);

-- Estudiantes
INSERT INTO estudiante (id_paciente, nombre_estudiante, apellido_estudiante, correo_estudiante, password_estudiante, activo) VALUES
('1725896347', 'Juan', 'Pérez', 'juan.perez@epn.edu.ec', 'pwd_1', TRUE),
('1723456789', 'María', 'López', 'maria.lopez@epn.edu.ec', 'pwd_2', TRUE),
('1726789012', 'Carlos', 'Martínez', 'carlos.martinez@epn.edu.ec', 'pwd_3', TRUE),
('1724567890', 'Ana', 'García', 'ana.garcia@epn.edu.ec', 'pwd_4', TRUE),
('1727890123', 'Luis', 'Rodríguez', 'luis.rodriguez@epn.edu.ec', 'pwd_5', TRUE);

-- Administradores
INSERT INTO administrador (id_admin, nombre_admin, apellido_admin, correo_admin, password_admin, rol, activo) VALUES
('admin001', 'Pedro', 'Sánchez', 'admin@epn.edu.ec', 'admin123', 'SUPER_ADMIN', TRUE),
('admin002', 'Lucía', 'Fernández', 'lucia.fernandez@epn.edu.ec', 'lucia123', 'ADMIN', TRUE);

-- Disponibilidades (próximos 7 días para todos los doctores)
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
-- Dra. María González (Nutrición)
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),

-- Dr. Carlos Ramírez (Nutrición)
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00:00', '12:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '11:00:00', '12:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),

-- Dra. Ana Pérez (Odontología)
(3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),

-- Dr. Luis Torres (Odontología)
(4, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(4, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(4, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),

-- Dra. Laura Mendoza (Psicología)
(5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00:00', '12:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '16:00:00', '17:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),

-- Dra. Patricia Vega (Medicina General)
(7, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(7, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(7, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(7, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(7, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY)));

-- Citas de prueba
INSERT INTO cita (fecha_cita, hora_cita, motivo_consulta, estado_cita, observacion_cita, id_especialidad, id_doctor, id_estudiante) VALUES
(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', 'Control nutricional', 'Agendada', NULL, 1, 1, 1),
(DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00:00', 'Limpieza dental', 'Agendada', NULL, 2, 3, 2),
(DATE_ADD(CURDATE(), INTERVAL 3 DAY), '09:00:00', 'Consulta psicológica', 'Confirmada', NULL, 3, 5, 3),
(DATE_ADD(CURDATE(), INTERVAL -5 DAY), '08:30:00', 'Dolor de cabeza', 'Completada', 'Se recetó ibuprofeno', 4, 7, 1),
(DATE_ADD(CURDATE(), INTERVAL -3 DAY), '15:00:00', 'Revisión general', 'Completada', 'Todo normal', 1, 1, 2);

-- Evaluaciones de prueba
INSERT INTO evaluacion (calificacion, comentario, fecha_evaluacion, id_doctor, id_estudiante, id_cita) VALUES
(5, 'Excelente atención, muy profesional', NOW(), 1, 1, NULL),
(4, 'Buena atención, tiempo de espera un poco largo', NOW(), 3, 2, NULL),
(5, 'Muy satisfecho con el servicio', NOW(), 5, 3, NULL),
(5, 'Excelente profesional', NOW(), 7, 1, NULL);

-- ============================================
-- VERIFICACIÓN DE DATOS
-- ============================================
SELECT '✅ Base de datos creada exitosamente' AS mensaje;
SELECT '' AS '';
SELECT '📊 RESUMEN DE DATOS' AS '';
SELECT CONCAT('Especialidades: ', COUNT(*)) AS total FROM especialidad
UNION ALL
SELECT CONCAT('Doctores: ', COUNT(*)) FROM doctor
UNION ALL
SELECT CONCAT('Estudiantes: ', COUNT(*)) FROM estudiante
UNION ALL
SELECT CONCAT('Administradores: ', COUNT(*)) FROM administrador
UNION ALL
SELECT CONCAT('Disponibilidades: ', COUNT(*)) FROM disponibilidad
UNION ALL
SELECT CONCAT('Citas: ', COUNT(*)) FROM cita
UNION ALL
SELECT CONCAT('Evaluaciones: ', COUNT(*)) FROM evaluacion;