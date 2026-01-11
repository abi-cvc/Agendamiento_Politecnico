-- datos_completos.sql
-- Datos de prueba completos para la base agendamiento_politecnico
USE agendamiento_politecnico;

-- Administradores
INSERT IGNORE INTO administrador (id_admin, nombre_admin, apellido_admin, correo_admin, password_admin, rol, activo) VALUES
('admin001','Pedro','Sánchez','admin@epn.edu.ec','admin123','SUPER_ADMIN',TRUE),
('admin002','Lucía','Fernández','lucia.fernandez@epn.edu.ec','lucia123','ADMIN',TRUE);

-- Especialidades (si no existen)
INSERT IGNORE INTO especialidad (nombre, titulo, descripcion, servicios, icono) VALUES
('nutricion','Nutrición','Nutrición clínica y deportiva','Evaluación nutricional|Plan de comidas','🥗'),
('odontologia','Odontología','Atención odontológica integral','Limpieza|Endodoncia|Ortodoncia','🦷'),
('psicologia','Psicología','Apoyo psicológico','Terapia individual|Talleres','🧠');

-- Doctores
INSERT IGNORE INTO doctor (cedula, nombre, apellido, email, telefono, password_doctor, activo, id_especialidad) VALUES
('1234567001','Test','Doctor1','doc1@test.edu.ec','0999000001','docpwd_101',TRUE,1),
('1234567002','Test','Doctor2','doc2@test.edu.ec','0999000002','docpwd_102',TRUE,2),
('1234567003','Test','Doctor3','doc3@test.edu.ec','0999000003','docpwd_103',FALSE,3);

-- Estudiantes
INSERT IGNORE INTO estudiante (id_paciente, nombre_estudiante, apellido_estudiante, correo_estudiante, password_estudiante, activo) VALUES
('2000000001','Est','Uno','est1@test.edu.ec','pwd_201',TRUE),
('2000000002','Est','Dos','est2@test.edu.ec','pwd_202',TRUE),
('2000000003','Est','Tres','est3@test.edu.ec','pwd_203',FALSE);

-- Disponibilidades
INSERT IGNORE INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY)));

-- Citas de ejemplo
INSERT IGNORE INTO cita (fecha_cita, hora_cita, motivo_consulta, estado_cita, observacion_cita, id_especialidad, id_doctor, id_estudiante) VALUES
(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', 'Consulta inicial', 'Agendada', NULL, 1, 1, 1),
(DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:00:00', 'Seguimiento', 'Agendada', NULL, 2, 2, 2);

-- Verificación rápida
SELECT 'Datos insertados' AS info;
SELECT COUNT(*) FROM administrador;
SELECT COUNT(*) FROM especialidad;
SELECT COUNT(*) FROM doctor;
SELECT COUNT(*) FROM estudiante;
SELECT COUNT(*) FROM disponibilidad;
SELECT COUNT(*) FROM cita;
