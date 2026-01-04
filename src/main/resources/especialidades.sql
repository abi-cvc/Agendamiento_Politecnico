-- Script SQL para crear e inicializar la tabla de especialidades
-- Base de datos: agendamiento_politecnico

-- Crear la tabla especialidad (si no existe)
CREATE TABLE IF NOT EXISTS especialidad (
    idEspecialidad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    servicios TEXT NOT NULL,
    icono VARCHAR(50),
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar especialidades iniciales
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

-- Verificar la inserción
SELECT * FROM especialidad;
