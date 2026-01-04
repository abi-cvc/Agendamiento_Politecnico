-- ============================================
-- Script de Inicialización: Doctores y Disponibilidad
-- Proyecto: Agendamiento Politécnico
-- Según el diagrama de robustez
-- ============================================

USE agendamiento_politecnico;

-- ============================================
-- TABLA: doctor
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: disponibilidad (Calendario)
-- ============================================
CREATE TABLE IF NOT EXISTS disponibilidad (
    id_disponibilidad INT AUTO_INCREMENT PRIMARY KEY,
    id_doctor INT NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    disponible BOOLEAN DEFAULT TRUE,
    dia_semana VARCHAR(20),
    FOREIGN KEY (id_doctor) REFERENCES doctor(id_doctor),
    UNIQUE KEY unique_disponibilidad (id_doctor, fecha, hora_inicio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- ACTUALIZAR TABLA: cita (agregar relación con doctor)
-- ============================================
ALTER TABLE cita 
ADD COLUMN id_doctor INT AFTER id_especialidad,
ADD FOREIGN KEY (id_doctor) REFERENCES doctor(id_doctor);

-- ============================================
-- DATOS DE PRUEBA: Doctores
-- ============================================

-- Doctores de Nutrición (especialidad id=1)
INSERT INTO doctor (cedula, nombre, apellido, email, telefono, descripcion, id_especialidad) VALUES
('1234567890', 'María', 'González', 'maria.gonzalez@epn.edu.ec', '0987654321', 
 'Nutricionista especializada en nutrición deportiva y clínica con 10 años de experiencia.', 1),
('1234567891', 'Carlos', 'Ramírez', 'carlos.ramirez@epn.edu.ec', '0987654322', 
 'Experto en nutrición vegetariana y vegana, enfoque en salud preventiva.', 1);

-- Doctores de Odontología (especialidad id=2)
INSERT INTO doctor (cedula, nombre, apellido, email, telefono, descripcion, id_especialidad) VALUES
('1234567892', 'Ana', 'Pérez', 'ana.perez@epn.edu.ec', '0987654323', 
 'Odontóloga general con especialización en ortodoncia y estética dental.', 2),
('1234567893', 'Luis', 'Torres', 'luis.torres@epn.edu.ec', '0987654324', 
 'Especialista en endodoncia y cirugía maxilofacial.', 2);

-- Doctores de Psicología (especialidad id=3)
INSERT INTO doctor (cedula, nombre, apellido, email, telefono, descripcion, id_especialidad) VALUES
('1234567894', 'Laura', 'Mendoza', 'laura.mendoza@epn.edu.ec', '0987654325', 
 'Psicóloga clínica especializada en terapia cognitivo-conductual y manejo de ansiedad.', 3),
('1234567895', 'Diego', 'Salazar', 'diego.salazar@epn.edu.ec', '0987654326', 
 'Psicólogo con enfoque en salud mental juvenil y orientación vocacional.', 3);

-- Doctores de Medicina General (especialidad id=4)
INSERT INTO doctor (cedula, nombre, apellido, email, telefono, descripcion, id_especialidad) VALUES
('1234567896', 'Patricia', 'Vega', 'patricia.vega@epn.edu.ec', '0987654327', 
 'Médico general con amplia experiencia en atención primaria y medicina preventiva.', 4),
('1234567897', 'Roberto', 'Castro', 'roberto.castro@epn.edu.ec', '0987654328', 
 'Especialista en medicina interna y enfermedades crónicas.', 4);

-- Doctores de Enfermería (especialidad id=5)
INSERT INTO doctor (cedula, nombre, apellido, email, telefono, descripcion, id_especialidad) VALUES
('1234567898', 'Sofía', 'Morales', 'sofia.morales@epn.edu.ec', '0987654329', 
 'Enfermera profesional especializada en cuidados intensivos y emergencias.', 5);

-- ============================================
-- DATOS DE PRUEBA: Disponibilidad (Próximos 15 días)
-- ============================================

-- Disponibilidad para Dra. María González (Nutrición) - ID 1
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
-- Semana 1
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY))),
(1, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY)));

-- Disponibilidad para Dr. Carlos Ramírez (Nutrición) - ID 2
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00:00', '12:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY)));

-- Disponibilidad para Dra. Ana Pérez (Odontología) - ID 3
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(3, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 3 DAY)));

-- Disponibilidad para Dra. Laura Mendoza (Psicología) - ID 5
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00:00', '12:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '16:00:00', '17:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY)));

-- Disponibilidad para Dra. Patricia Vega (Medicina General) - ID 7
INSERT INTO disponibilidad (id_doctor, fecha, hora_inicio, hora_fin, disponible, dia_semana) VALUES
(7, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', '09:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(7, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(7, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 1 DAY))),
(7, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '14:00:00', '15:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY))),
(7, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '15:00:00', '16:00:00', TRUE, DAYNAME(DATE_ADD(CURDATE(), INTERVAL 2 DAY)));

-- ============================================
-- FIN DEL SCRIPT
-- ============================================

SELECT '✅ Script ejecutado exitosamente!' AS Mensaje;
SELECT '📊 Total de doctores insertados:', COUNT(*) FROM doctor;
SELECT '📅 Total de disponibilidades creadas:', COUNT(*) FROM disponibilidad;
