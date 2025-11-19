CREATE DATABASE IF NOT EXISTS remi;
USE remi;

CREATE TABLE Usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    fechaNacimiento DATE
);

CREATE TABLE Paciente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    enfermedad ENUM('Diabetes','Hipertension','Asma','Otro'),
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id)
);

CREATE TABLE Enfermero (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    especialidad ENUM('General','Pediatría','Geriatría','Otro'),
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id)
);

CREATE TABLE Tratamiento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_enfermero INT NOT NULL,
    tipo_tratamiento ENUM('Medicamento','Terapia','Cirugía','Otro'),
    fecha_inicio DATE,
    fecha_fin DATE,
    FOREIGN KEY (id_paciente) REFERENCES Paciente(id),
    FOREIGN KEY (id_enfermero) REFERENCES Enfermero(id)
);

CREATE TABLE Seguimiento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_enfermero INT NOT NULL,
    id_tratamiento INT NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (id_paciente) REFERENCES Paciente(id),
    FOREIGN KEY (id_enfermero) REFERENCES Enfermero(id),
    FOREIGN KEY (id_tratamiento) REFERENCES Tratamiento(id)
);

CREATE TABLE Notificacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_seguimiento INT NOT NULL,
    fecha DATE NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_paciente) REFERENCES Paciente(id),
    FOREIGN KEY (id_seguimiento) REFERENCES Seguimiento(id)
);

CREATE TABLE Cita (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_enfermero INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    motivo VARCHAR(255),
    estado ENUM('Pendiente','Confirmada','Cancelada','Completada') DEFAULT 'Pendiente',
    FOREIGN KEY (id_paciente) REFERENCES Paciente(id),
    FOREIGN KEY (id_enfermero) REFERENCES Enfermero(id)
);
