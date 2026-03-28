-- Crear base de datos
CREATE DATABASE milla_inventory;

-- ========================
-- TABLA: categorias
-- ========================
CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================
-- TABLA: ubicaciones
-- ========================
CREATE TABLE ubicaciones (
    id SERIAL PRIMARY KEY,
    municipio VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================
-- TABLA: bodegas
-- ========================
CREATE TABLE bodegas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacionId INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ubicacionId) REFERENCES ubicaciones(id)
);

-- ========================
-- TABLA: proveedores
-- ========================
CREATE TABLE proveedores (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(8) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================
-- TABLA: marcas
-- ========================
CREATE TABLE marcas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================
-- TABLA: productos
-- ========================
CREATE TABLE productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoriaId INTEGER NOT NULL,
    precio NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (categoriaId) REFERENCES categorias(id)
);

-- ========================
-- TABLA: marcas_productos (N:M)
-- ========================
CREATE TABLE marcas_productos (
    productoId INTEGER NOT NULL,
    marcaId INTEGER NOT NULL,
    PRIMARY KEY (productoId, marcaId),
    FOREIGN KEY (productoId) REFERENCES productos(id),
    FOREIGN KEY (marcaId) REFERENCES marcas(id)
);

-- ========================
-- TABLA: bodegas_productos (stock)
-- ========================
CREATE TABLE bodegas_productos (
    id SERIAL PRIMARY KEY,
    productoId INTEGER NOT NULL,
    bodegaId INTEGER NOT NULL,
    cantidad INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (productoId, bodegaId),
    FOREIGN KEY (productoId) REFERENCES productos(id),
    FOREIGN KEY (bodegaId) REFERENCES bodegas(id)
);

-- ========================
-- TABLA: entradas
-- ========================
CREATE TABLE entradas (
    id SERIAL PRIMARY KEY,
    productoId INTEGER NOT NULL,
    proveedorId INTEGER NOT NULL,
    bodegaId INTEGER NOT NULL,
    fecha DATE NOT NULL,
    precio_adquisicion NUMERIC(10,2) NOT NULL,
    cantidad INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (productoId) REFERENCES productos(id),
    FOREIGN KEY (proveedorId) REFERENCES proveedores(id),
    FOREIGN KEY (bodegaId) REFERENCES bodegas(id)
);

-- ========================
-- TABLA: salidas
-- ========================
CREATE TABLE salidas (
    id SERIAL PRIMARY KEY,
    productoId INTEGER NOT NULL,
    bodegaId INTEGER NOT NULL,
    fecha DATE NOT NULL,
    cantidad INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (productoId) REFERENCES productos(id),
    FOREIGN KEY (bodegaId) REFERENCES bodegas(id)
);

-- ========================
-- TABLA: roles
-- ========================
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================
-- TABLA: users
-- ========================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- ========================
-- TABLA: users_roles
-- ========================
CREATE TABLE users_roles (
    userId INTEGER NOT NULL,
    roleId INTEGER NOT NULL,
    PRIMARY KEY (userId, roleId),
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY (roleId) REFERENCES roles(id)
);

-- ========================
-- TABLA: tokens
-- ========================
CREATE TABLE tokens (
    id SERIAL PRIMARY KEY,
    userId INTEGER NOT NULL,
    token TEXT NOT NULL,
    expired BOOLEAN DEFAULT FALSE,
    revoked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE
);

-- ========================
-- INSERTS
-- ========================

-- categorias
INSERT INTO categorias (nombre) VALUES
('Dormitorio'),
('Sala de estar'),
('Comedor'),
('Cocina'),
('Oficina');

-- ubicaciones
INSERT INTO ubicaciones (municipio) VALUES
('Santa Tecla'),
('San Salvador'),
('San Miguel'),
('Santa Ana');

-- bodegas
INSERT INTO bodegas (nombre, ubicacionId) VALUES
('Bodega A',1),
('Bodega B',2),
('Bodega C',3),
('Bodega D',4);

-- proveedores
INSERT INTO proveedores (nombre, email, telefono) VALUES
('Proveedor A','proveedora@correo.com','12345678'),
('Proveedor B','proveedorb@correo.com','87654321'),
('Proveedor C','proveedorc@correo.com','24681012'),
('Proveedor D','proveedord@correo.com','01357911');

-- marcas
INSERT INTO marcas (nombre) VALUES
('Marca A'),
('Marca B'),
('Marca C'),
('Marca D');

-- productos
INSERT INTO productos (nombre, categoriaId, precio) VALUES
('Cama matrimonial',1,600.00),
('Armario de Roble',1,250.00),
('Sofa de cuero',2,350.25),
('Mesa de centro',2,149.99),
('Mesa de comedor + sillas',3,499.99),
('Minibar',3,135.45),
('Mueble de cocina',4,245.00),
('Escritorio ejecutivo',5,425.30);

-- marcasproductos
INSERT INTO marcas_productos VALUES
(1,1),(1,2),(2,1),(3,2),(4,2),(5,3),(6,3),(7,4),(8,4);

-- bodegasproductos
INSERT INTO bodegas_productos (productoId, bodegaId, cantidad) VALUES
(1,1,3),(1,2,3),(1,3,2),(2,4,10),(2,1,6),
(3,3,7),(4,1,6),(4,4,3),(5,1,3),(5,3,8),
(5,2,4),(6,2,6),(7,1,2),(7,2,3),(7,3,6),
(7,4,7),(8,1,6),(8,4,5),(5,4,2);

-- entradas
INSERT INTO entradas (productoId, proveedorId, bodegaId, fecha, precio_adquisicion, cantidad) VALUES
(1,1,1,'2023-05-16',450.00,3),
(4,2,4,'2023-05-15',99.99,3),
(5,3,3,'2023-05-16',350.00,8),
(6,4,2,'2023-05-16',75.00,6),
(7,1,4,'2023-05-16',175.00,7),
(1,1,1,'2023-06-22',125.00,5);

-- salidas
INSERT INTO salidas (productoId, bodegaId, fecha, cantidad) VALUES
(2,1,'2023-05-09',1),
(3,2,'2023-05-10',1),
(8,3,'2023-05-11',5),
(4,4,'2023-05-12',3),
(1,1,'2023-06-22',5);

INSERT INTO roles (name) VALUES
('ROLE_ADMIN'),
('ROLE_USER'),
('ROLE_ANALYST');

-- ========================
-- INDICES
-- ========================

-- Tokens
CREATE INDEX idx_tokens_userId ON tokens(userId);

-- relaciones de usuarios
CREATE INDEX idx_users_roles_user ON users_roles(userId);
CREATE INDEX idx_users_roles_role ON users_roles(roleId);

-- productos y categorias
CREATE INDEX idx_productos_categoria ON productos(categoriaId);

-- ubicaciones y bodegas
CREATE INDEX idx_ubicaciones_municipio ON ubicaciones(municipio);
CREATE INDEX idx_bodegas_ubicacion ON bodegas(ubicacionId);

-- Stock bodegas_productos
CREATE INDEX idx_bodegas_productos_producto ON bodegas_productos(productoId);
CREATE INDEX idx_bodegas_productos_bodega ON bodegas_productos(bodegaId);

-- Relacion marcas_productos
CREATE INDEX idx_marcas_productos_producto ON marcas_productos(productoId);
CREATE INDEX idx_marcas_productos_marca ON marcas_productos(marcaId);

-- Entradas (reportes)
CREATE INDEX idx_entradas_producto ON entradas(productoId);
CREATE INDEX idx_entradas_bodega ON entradas(bodegaId);
CREATE INDEX idx_entradas_fecha ON entradas(fecha);

-- Salidas (reportes)
CREATE INDEX idx_salidas_producto ON salidas(productoId);
CREATE INDEX idx_salidas_bodega ON salidas(bodegaId);
CREATE INDEX idx_salidas_fecha ON salidas(fecha);

-- proveedores
CREATE INDEX idx_proveedores_email ON proveedores(email);

-- Búsqueda por nombre de producto
CREATE INDEX idx_productos_nombre ON productos(nombre);

-- Búsqueda por nombre de categoría
CREATE INDEX idx_categorias_nombre ON categorias(nombre);

-- Búsqueda por nombre de marca
CREATE INDEX idx_marcas_nombre ON marcas(nombre);
