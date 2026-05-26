CREATE DATABASE PruebaTecnica;
GO

USE PruebaTecnica;
GO

CREATE TABLE usuarios (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL
);
GO

CREATE TABLE empleados (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL,
    apellidos NVARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    genero NVARCHAR(20) NOT NULL,
    numero_identificacion NVARCHAR(50) NOT NULL UNIQUE
);
GO

INSERT INTO usuarios (username, password)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
GO
