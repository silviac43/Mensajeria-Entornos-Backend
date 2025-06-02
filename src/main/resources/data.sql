
-- Insertar empresas de mensajería, cada una con su admin correspondiente
INSERT INTO mensajeria.empresa_mensajeria (nombre) VALUES
('Mensajería Rápida'),
('Express Entregas');

-- Insertar clientes en las respectivas mensajerias
INSERT INTO mensajeria.cliente (mensajeria_id, nombre, telefono_recogida, direccion_recogida, telefono_entrega, direccion_entrega, email) VALUES
-- Clientes para empresa_mensajeria (id=1)
(1, 'Tech Solutions Inc.', '3101234567', 'Carrera 15 #80-25, Bogotá', '3209876543', 'Calle 100 #19-55, Bogotá', 'contacto@techsolutions.com'),
(1, 'Farmacia La Salud', '6012345678', 'Avenida 19 #120-35, Bogotá', '3156789012', 'Carrera 7 #125-10, Bogotá', 'pedidos@farmacialasalud.com'),
(1, 'Restaurante El Sabor', '3187654321', 'Calle 85 #11-30, Bogotá', '3001234567', 'Carrera 11 #93-45, Bogotá', 'reservas@elsabor.com'),
(1, 'Moda Express', '3154321098', 'Centro Comercial Andino, Bogotá, Local 205', '3178901234', 'Centro Comercial Santafé, Bogotá, Local 310', 'ventas@modaexpress.com'),
(1, 'Libros y Más', '6018765432', 'Carrera 11 #86-21, Bogotá', '3145678901', 'Calle 72 #12-45, Bogotá', 'pedidos@librosymas.com'),

-- Clientes para empresa_mensajeria (id=2)
(2, 'Consultoría Global', '6019876543', 'Torre Empresarial Soho, Bogotá, Piso 12', '3102345678', 'Edificio Platinum, Bogotá, Oficina 801', 'info@consultoriaglobal.com'),
(2, 'Floristería Jardín', '3203456789', 'Carrera 13 #85-10, Bogotá', '3156789012', 'Calle 116 #15-20, Bogotá', 'pedidos@jardinflores.com'),
(2, 'Electro Hogar', '3189012345', 'Avenida Chile #72-15, Bogotá', '3004567890', 'Carrera 50 #99-30, Bogotá', 'servicio@electrohogar.com'),
(2, 'Joyas Brillantes', '3156789012', 'Centro Comercial Unicentro, Bogotá, Local 415', '3178901234', 'Centro Comercial Gran Estación, Bogotá, Local 220', 'clientes@joyasbrillantes.com'),
(2, 'Abogados & Asociados', '6012345678', 'Calle 93 #14-20, Bogotá, Oficina 302', '3105678901', 'Carrera 7 #71-52, Bogotá, Torre B, Piso 8', 'recepcion@abogadosyasociados.com');


-- Insert for admin_mensajeria (management)
INSERT INTO mensajeria.usuario (nombre_usuario, email, password, rol, fecha_creacion, mensajeria_id)
VALUES ('admin1', 'admin1@mensajeria.com', '$2a$10$hashedpassword123', 'admin_mensajeria', NOW(), 1);

-- Insert for operador (operator)
INSERT INTO mensajeria.usuario (nombre_usuario, email, password, rol, fecha_creacion, mensajeria_id)
VALUES ('operador1', 'operador1@mensajeria.com', '$2a$10$hashedpassword456', 'operador', NOW(), 1);

-- Insert for mensajero (courier) - will also be in mensajero table
INSERT INTO mensajeria.usuario (nombre_usuario, email, password, rol, fecha_creacion, mensajeria_id)
VALUES ('mensajero1', 'mensajero1@mensajeria.com', '$2a$10$hashedpassword789', 'mensajero', NOW(), 1);

-- Insert another mensajero
INSERT INTO mensajeria.usuario (nombre_usuario, email, password, rol, fecha_creacion, mensajeria_id)
VALUES ('mensajero2', 'mensajero2@mensajeria.com', '$2a$10$hashedpasswordabc', 'mensajero', NOW(), 1);

-- Insert for second company's admin
INSERT INTO mensajeria.usuario (nombre_usuario, email, password, rol, fecha_creacion, mensajeria_id)
VALUES ('admin2', 'admin2@otramensajeria.com', '$2a$10$hashedpassworddef', 'admin_mensajeria', NOW(), 2);

-- Insert for second company's operador
INSERT INTO mensajeria.usuario (nombre_usuario, email, password, rol, fecha_creacion, mensajeria_id)
VALUES ('operador2', 'operador2@otramensajeria.com', '$2a$10$hashedpasswordghi', 'operador', NOW(), 2);

-- Insert for second company's mensajero
INSERT INTO mensajeria.usuario (nombre_usuario, email, password, rol, fecha_creacion, mensajeria_id)
VALUES ('mensajero3', 'mensajero3@otramensajeria.com', '$2a$10$hashedpasswordjkl', 'mensajero', NOW(), 2);

-- Insert another mensajero for second company
INSERT INTO mensajeria.usuario (nombre_usuario, email, password, rol, fecha_creacion, mensajeria_id)
VALUES ('mensajero4', 'mensajero4@otramensajeria.com', '$2a$10$hashedpasswordmno', 'mensajero', NOW(), 2);

-- Insert additional operador for first company
INSERT INTO mensajeria.usuario (nombre_usuario, email, password, rol, fecha_creacion, mensajeria_id)
VALUES ('operador3', 'operador3@mensajeria.com', '$2a$10$hashedpasswordpqr', 'operador', NOW(), 1);

-- Insert additional mensajero for first company
INSERT INTO mensajeria.usuario (nombre_usuario, email, password, rol, fecha_creacion, mensajeria_id)
VALUES ('mensajero5', 'mensajero5@mensajeria.com', '$2a$10$hashedpasswordstu', 'mensajero', NOW(), 1);

-- Inserción inicial para tabla pedido
INSERT INTO mensajeria.pedido 
    (cliente_id, mensajeria_id, usuario_mensajero_id, direccion_recogida, telefono_recogida, direccion_entrega, telefono_entrega, tipo_paquete, estado, notas)
VALUES
    -- Pedido 1 asignado a mensajero1 (id 3) para cliente 'Tech Solutions Inc.' (id 1)
    (1, 1, 3, 'Carrera 15 #80-25, Bogotá', '3101234567', 'Calle 100 #19-55, Bogotá', '3209876543', 'Documentos', 'PENDIENTE', 'Entrega urgente'),

    -- Pedido 2 sin mensajero asignado aún para cliente 'Farmacia La Salud' (id 2)
    (2, 1, NULL, 'Avenida 19 #120-35, Bogotá', '6012345678', 'Carrera 7 #125-10, Bogotá', '3156789012', 'Medicamentos', 'PENDIENTE', NULL),

    -- Pedido 3 asignado a mensajero2 (id 4) para cliente 'Floristería Jardín' (id 7)
    (7, 2, 4, 'Carrera 13 #85-10, Bogotá', '3203456789', 'Calle 116 #15-20, Bogotá', '3156789012', 'Flores', 'ASIGNADO', 'Entregar antes de las 5 pm'),

    -- Pedido 4 con cliente custom sin cliente_id (direcciones directas)
    (NULL, 1, 3, 'Calle 85 #11-30, Bogotá', '3187654321', 'Carrera 11 #93-45, Bogotá', '3001234567', 'Alimentos', 'EN_TRANSITO', 'Entrega en puerta trasera'),

    -- Pedido 5 asignado a mensajero3 (id 7) para cliente 'Electro Hogar' (id 9)
    (9, 2, 7, 'Avenida Chile #72-15, Bogotá', '3189012345', 'Carrera 50 #99-30, Bogotá', '3004567890', 'Electrodomésticos', 'PENDIENTE', NULL);

-- Inserción inicial para tabla historial_pedido
INSERT INTO mensajeria.historial_pedido
    (pedido_id, tipo_cambio, valor_anterior, valor_nuevo, usuario_id, fecha_cambio)
VALUES
    -- Cambio de estado del pedido 1 por operador1 (id 2)
    (1, 'ESTADO', 'PENDIENTE', 'ASIGNADO', 2, NOW()),

    -- Asignación de mensajero en pedido 1
    (1, 'MENSAJERO', NULL, 'mensajero1', 2, NOW()),

    -- Cambio de estado del pedido 3 por operador2 (id 6)
    (3, 'ESTADO', 'PENDIENTE', 'ASIGNADO', 6, NOW()),

    -- Cambio de notas en pedido 4 por admin1 (id 1)
    (4, 'NOTAS', NULL, 'Entrega en puerta trasera', 1, NOW()),

    -- Cambio de estado del pedido 5 por operador2 (id 6)
    (5, 'ESTADO', 'PENDIENTE', 'PENDIENTE', 6, NOW());
