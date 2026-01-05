-- ============================================
-- SCRIPT COMPLETO DE INICIALIZACIÓN
-- Base de Datos: Agendamiento Politécnico
-- Proyecto: Sistema de Agendamiento de Citas - EPN
-- Autor: GitHub Copilot
-- Fecha: Enero 2026
-- ============================================
-- Este script crea todas las tablas necesarias y las llena con datos iniciales
-- ============================================

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS agendamiento_politecnico
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE agendamiento_politecnico;

-- ============================================
-- 1. TABLA: especialidad
-- ============================================
CREATE TABLE IF NOT EXISTS especialidad (
    id_especialidad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    servicios TEXT NOT NULL,
    icono VARCHAR(50),
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 2. TABLA: estudiante
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
-- 3. TABLA: administrador
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
-- 4. TABLA: doctor
-- ============================================
CREATE TABLE IF NOT EXISTS doctor (
    id_doctor INT AUTO_INCREMENT PRIMARY KEY,
    cedula VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(15),
    foto VARCHAR(255),
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE,
    id_especialidad INT NOT NULL,
    FOREIGN KEY (id_especialidad) REFERENCES especialidad(id_especialidad)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    INDEX idx_cedula (cedula),
    INDEX idx_especialidad (id_especialidad)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 5. TABLA: disponibilidad (Calendario)
-- ============================================
CREATE TABLE IF NOT EXISTS disponibilidad (
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
-- 6. TABLA: cita
-- ============================================
CREATE TABLE IF NOT EXISTS cita (
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
-- INSERCIÓN DE DATOS INICIALES
-- ============================================

-- ============================================
-- DATOS: Especialidades (5 especialidades)
-- ============================================
INSERT INTO especialidad (nombre, titulo, descripcion, servicios, icono) VALUES
(
    'nutricion',
    'Nutrición',
    'Nuestro equipo de nutricionistas certificados te ayudará a alcanzar tus metas de salud mediante planes personalizados. Ya sea que busques mejorar tu rendimiento deportivo, controlar tu peso o manejar condiciones específicas de salud, te brindamos el acompañamiento necesario con un enfoque científico y humano.',
    'Evaluación nutricional personalizada|Planes de alimentación según objetivos|Seguimiento y control de peso|Asesoría en nutrición deportiva|Educación en autocuidado',
    '🥗'
),
(
    'odontologia',
    'Odontología',
    'Mantén tu sonrisa saludable con nuestros servicios odontológicos de calidad. Contamos con tecnología moderna y profesionales experimentados que te brindarán atención preventiva, diagnóstica y terapéutica. Tu salud bucal es fundamental para tu bienestar general, y estamos aquí para cuidarla.',
    'Limpieza dental y profilaxis|Tratamiento de caries|Extracciones dentales|Endodoncia|Prevención y educación en salud bucal',
    '🦷'
),
(
    'psicologia',
    'Psicología',
    'En un entorno confidencial y de apoyo, nuestros psicólogos te acompañan en tu proceso de crecimiento personal y bienestar emocional. Entendemos los desafíos que enfrentas como estudiante y te ofrecemos herramientas efectivas para manejar el estrés, la ansiedad y otros aspectos que afectan tu desarrollo académico y personal.',
    'Consulta psicológica individual|Manejo de estrés y ansiedad|Orientación vocacional|Apoyo en crisis emocionales|Talleres de desarrollo personal',
    '🧠'
),
(
    'medicina-general',
    'Medicina General',
    'Recibe atención médica integral de profesionales capacitados para diagnosticar, tratar y prevenir enfermedades. Desde consultas de rutina hasta seguimiento de condiciones crónicas, nuestro servicio de medicina general es tu primera línea de atención en salud, con derivaciones oportunas cuando sea necesario.',
    'Consulta médica general|Diagnóstico y tratamiento|Control de enfermedades crónicas|Certificados médicos|Derivaciones a especialistas',
    '⚕️'
),
(
    'enfermeria',
    'Enfermería',
    'Nuestro equipo de enfermería profesional está disponible para brindarte cuidados de calidad, procedimientos de enfermería y educación en salud. Con calidez humana y profesionalismo, te asistimos en el control de tu salud, administración de medicamentos y prevención de enfermedades para que mantengas tu bienestar durante tu vida académica.',
    'Toma de signos vitales|Curaciones y procedimientos menores|Inyecciones y vacunación|Control de medicación|Educación en salud preventiva',
    '💉'
);

-- ============================================
-- DATOS: Doctores (9 doctores - 2 por especialidad aprox.)
-- ============================================

-- Doctores de Nutrición (id_especialidad = 1)
INSERT INTO doctor (cedula, nombre, apellido, email, telefono, descripcion, id_especialidad) VALUES
('1234567890', 'María', 'González', 'maria.gonzalez@epn.edu.ec', '0987654321', 
 'Nutricionista especializada en nutrición deportiva y clínica con 10 años de experiencia.', 1),
('1234567891', 'Carlos', 'Ramírez', 'carlos.ramirez@epn.edu.ec', '0987654322', 
 'Experto en nutrición vegetariana y vegana, enfoque en salud preventiva.', 1);

-- Doctores de Odontología (id_especialidad = 2)
INSERT INTO doctor (cedula, nombre, apellido, email, telefono, descripcion, id_especialidad) VALUES
('1234567892', 'Ana', 'Pérez', 'ana.perez@epn.edu.ec', '0987654323', 
 'Odontóloga general con especialización en ortodoncia y estética dental.', 2),
('1234567893', 'Luis', 'Torres', 'luis.torres@epn.edu.ec', '0987654324', 
 'Especialista en endodoncia y cirugía maxilofacial.', 2);

-- Doctores de Psicología (id_especialidad = 3)
INSERT INTO doctor (cedula, nombre, apellido, email, telefono, descripcion, id_especialidad) VALUES
('1234567894', 'Laura', 'Mendoza', 'laura.mendoza@epn.edu.ec', '0987654325', 
 'Psicóloga clínica especializada en terapia cognitivo-conductual y manejo de ansiedad.', 3),
('1234567895', 'Diego', 'Salazar', 'diego.salazar@epn.edu.ec', '0987654326', 
 'Psicólogo con enfoque en salud mental juvenil y orientación vocacional.', 3);

-- Doctores de Medicina General (id_especialidad = 4)
INSERT INTO doctor (cedula, nombre, apellido, email, telefono, descripcion, id_especialidad) VALUES
('1234567896', 'Patricia', 'Vega', 'patricia.vega@epn.edu.ec', '0987654327', 
 'Médico general con amplia experiencia en atención primaria y medicina preventiva.', 4),
('1234567897', 'Roberto', 'Castro', 'roberto.castro@epn.edu.ec', '0987654328', 
 'Especialista en medicina interna y enfermedades crónicas.', 4);

-- Doctor de Enfermería (id_especialidad = 5)
INSERT INTO doctor (cedula, nombre, apellido, email, telefono, descripcion, id_especialidad) VALUES
('1234567898', 'Sofía', 'Morales', 'sofia.morales@epn.edu.ec', '0987654329', 
 'Enfermera profesional especializada en cuidados intensivos y emergencias.', 5);

-- ============================================
-- DATOS: Estudiantes (5 estudiantes de ejemplo)
-- ============================================
INSERT INTO estudiante (id_paciente, nombre_estudiante, apellido_estudiante, correo_estudiante) VALUES
('1725896347', 'Juan', 'Pérez', 'juan.perez@epn.edu.ec'),
('1723456789', 'María', 'López', 'maria.lopez@epn.edu.ec'),
('1726789012', 'Carlos', 'Martínez', 'carlos.martinez@epn.edu.ec'),
('1724567890', 'Ana', 'García', 'ana.garcia@epn.edu.ec'),
('1727890123', 'Luis', 'Rodríguez', 'luis.rodriguez@epn.edu.ec');

-- ============================================
-- DATOS: Administradores (2 administradores de ejemplo)
-- ============================================
-- NOTA: Las contraseñas están en texto plano por simplicidad
-- En producción se deben usar hash (BCrypt, SHA-256, etc.)
INSERT INTO administrador (id_admin, nombre_admin, apellido_admin, correo_admin, password_admin, rol, activo) VALUES
('admin001', 'Pedro', 'Sánchez', 'admin@epn.edu.ec', 'admin123', 'SUPER_ADMIN', TRUE),
('admin002', 'Lucía', 'Fernández', 'lucia.fernandez@epn.edu.ec', 'lucia123', 'ADMIN', TRUE);

-- ============================================
-- DATOS: Disponibilidad (Horarios para los próximos 15 días)
-- ============================================

-- NOTA: Este script genera disponibilidades dinámicamente para los próximos días
-- Los horarios son de ejemplo y pueden ajustarse según las necesidades

-- ============================================
-- Disponibilidad para Dra. María González (Nutrición) - ID 1
-- ============================================
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
-- Día 1
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
-- Día 2
(1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
-- Día 3
(1, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '16:00:00', '17:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY)));

-- ============================================
-- Disponibilidad para Dr. Carlos Ramírez (Nutrición) - ID 2
-- ============================================
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00:00', '12:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(2, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY)));

-- ============================================
-- Disponibilidad para Dra. Ana Pérez (Odontología) - ID 3
-- ============================================
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(3, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY))),
(3, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY)));

-- ============================================
-- Disponibilidad para Dr. Luis Torres (Odontología) - ID 4
-- ============================================
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(4, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(4, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(4, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(4, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '11:00:00', '12:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(4, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY)));

-- ============================================
-- Disponibilidad para Dra. Laura Mendoza (Psicología) - ID 5
-- ============================================
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00:00', '12:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '16:00:00', '17:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(5, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY))),
(5, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY)));

-- ============================================
-- Disponibilidad para Dr. Diego Salazar (Psicología) - ID 6
-- ============================================
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(6, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(6, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(6, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(6, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(6, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY)));

-- ============================================
-- Disponibilidad para Dra. Patricia Vega (Medicina General) - ID 7
-- ============================================
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(7, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(7, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(7, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(7, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(7, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(7, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY))),
(7, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY)));

-- ============================================
-- Disponibilidad para Dr. Roberto Castro (Medicina General) - ID 8
-- ============================================
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(8, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(8, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00:00', '12:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(8, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(8, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(8, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY)));

-- ============================================
-- Disponibilidad para Enfermera Sofía Morales (Enfermería) - ID 9
-- ============================================
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(9, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(9, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(9, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(9, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00:00', '12:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(9, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(9, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(9, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(9, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY))),
(9, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY))),
(9, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '16:00:00', '17:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY)));

-- ============================================
-- VERIFICACIÓN Y ESTADÍSTICAS
-- ============================================

-- Mostrar mensaje de éxito
SELECT '============================================' AS '';
SELECT '✅ SCRIPT EJECUTADO EXITOSAMENTE' AS '';
SELECT '============================================' AS '';

-- Mostrar estadísticas
SELECT 'Base de datos: agendamiento_politecnico' AS 'Información';
SELECT CONCAT('Total de Especialidades: ', COUNT(*)) AS 'Estadística' FROM especialidad
UNION ALL
SELECT CONCAT('Total de Doctores: ', COUNT(*)) FROM doctor
UNION ALL
SELECT CONCAT('Total de Disponibilidades: ', COUNT(*)) FROM disponibilidad
UNION ALL
SELECT CONCAT('Total de Citas: ', COUNT(*)) FROM cita;

-- Mostrar distribución de doctores por especialidad
SELECT 
    e.titulo AS 'Especialidad',
    COUNT(d.id_doctor) AS 'Cantidad de Doctores'
FROM especialidad e
LEFT JOIN doctor d ON e.id_especialidad = d.id_especialidad
GROUP BY e.id_especialidad, e.titulo
ORDER BY e.titulo;

SELECT '============================================' AS '';
SELECT '📋 LA BASE DE DATOS ESTÁ LISTA PARA USAR' AS '';
SELECT '============================================' AS '';

-- ============================================
-- FIN DEL SCRIPT
-- ============================================
