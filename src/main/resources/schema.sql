CREATE SCHEMA mensajeria;

-- Tabla empresa_mensajeria
CREATE TABLE mensajeria.empresa_mensajeria (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- Tabla usuario
CREATE TABLE mensajeria.usuario (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol ENUM('admin_mensajeria', 'operador', 'mensajero') NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    mensajeria_id BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (mensajeria_id) REFERENCES mensajeria.empresa_mensajeria(id) ON DELETE CASCADE
);

ALTER TABLE mensajeria.usuario 
ADD INDEX idx_admin_mensajeria (mensajeria_id, rol);

-- Tabla mensajero
CREATE TABLE mensajeria.mensajero (
    id BIGINT UNSIGNED PRIMARY KEY NOT NULL,
    disponibilidad BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id) REFERENCES mensajeria.usuario(id) ON DELETE CASCADE
);

-- Tabla cliente
CREATE TABLE mensajeria.cliente (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    mensajeria_id BIGINT UNSIGNED NOT NULL, 
    nombre VARCHAR(100) NOT NULL,
    telefono_recogida VARCHAR(15) NOT NULL,
    direccion_recogida VARCHAR(255) NOT NULL,
    telefono_entrega VARCHAR(15) NOT NULL,
    direccion_entrega VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    FOREIGN KEY (mensajeria_id) REFERENCES mensajeria.empresa_mensajeria(id) ON DELETE CASCADE
);

-- Tabla pedido
CREATE TABLE mensajeria.pedido (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT UNSIGNED NULL,
    mensajeria_id BIGINT UNSIGNED NOT NULL,
    usuario_mensajero_id BIGINT UNSIGNED NULL,
    direccion_recogida VARCHAR(255) NULL, 
    telefono_recogida VARCHAR(15) NULL,
    direccion_entrega VARCHAR(255) NULL,
    telefono_entrega VARCHAR(15) NULL,
    tipo_paquete VARCHAR(100) NOT NULL,
    estado ENUM('PENDIENTE', 'ASIGNADO', 'EN_TRANSITO', 'ENTREGADO') NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notas TEXT,
    FOREIGN KEY (cliente_id) REFERENCES mensajeria.cliente(id) ON DELETE CASCADE,
    FOREIGN KEY (mensajeria_id) REFERENCES mensajeria.empresa_mensajeria(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_mensajero_id) REFERENCES mensajeria.usuario(id) ON DELETE SET NULL
);

-- Tabla historial_pedido
CREATE TABLE mensajeria.historial_pedido (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT UNSIGNED NOT NULL,
    tipo_cambio ENUM('ESTADO', 'CLIENTE', 'MENSAJERO', 'EMPRESA_MENSAJERIA','DIRECCION_RECOGIDA', 'DIRECCION_ENTREGA', 'TELEFONO_RECOGIDA', 'TELEFONO_ENTREGA', 'TIPO_PAQUETE', 'NOTAS') NOT NULL,
    valor_anterior TEXT,
    valor_nuevo TEXT,
    usuario_id BIGINT UNSIGNED NULL,
    fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pedido_id) REFERENCES mensajeria.pedido(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES mensajeria.usuario(id) ON DELETE SET NULL
);

-- ALL TRIGGERS
DELIMITER //

-- Trigger que al crear un usuario con rol 'mensajero', crea el registro en mensajero
CREATE TRIGGER mensajeria.create_mensajero_after_usuario_insert
AFTER INSERT ON mensajeria.usuario
FOR EACH ROW
BEGIN
    IF NEW.rol = 'mensajero' THEN
        INSERT INTO mensajeria.mensajero (id, disponibilidad)
        VALUES (NEW.id, TRUE);
    END IF;
END//

-- Trigger que sincroniza la tabla mensajero cuando se actualiza el rol del usuario
CREATE TRIGGER mensajeria.sync_mensajero_after_usuario_update
AFTER UPDATE ON mensajeria.usuario
FOR EACH ROW
BEGIN
    IF NEW.rol = 'mensajero' AND OLD.rol <> 'mensajero' THEN
        INSERT INTO mensajeria.mensajero (id, disponibilidad)
        VALUES (NEW.id, TRUE);
    END IF;
    IF OLD.rol = 'mensajero' AND NEW.rol <> 'mensajero' THEN
        DELETE FROM mensajeria.mensajero WHERE id = NEW.id;
    END IF;
END//

-- Trigger to set mensajero availability to FALSE when assigned to a pedido
CREATE TRIGGER mensajeria.update_mensajero_availability_on_assign
AFTER UPDATE ON mensajeria.pedido
FOR EACH ROW
BEGIN
    -- When a mensajero is assigned (usuario_mensajero_id changes from NULL to a value)
    IF OLD.usuario_mensajero_id IS NULL AND NEW.usuario_mensajero_id IS NOT NULL THEN
        UPDATE mensajeria.mensajero 
        SET disponibilidad = FALSE 
        WHERE id = NEW.usuario_mensajero_id;
    END IF;
    
    -- When a mensajero is unassigned (usuario_mensajero_id changes from a value to NULL)
    IF OLD.usuario_mensajero_id IS NOT NULL AND NEW.usuario_mensajero_id IS NULL THEN
        UPDATE mensajeria.mensajero 
        SET disponibilidad = TRUE 
        WHERE id = OLD.usuario_mensajero_id;
    END IF;
    
    -- When mensajero is changed (from one mensajero to another)
    IF OLD.usuario_mensajero_id IS NOT NULL AND NEW.usuario_mensajero_id IS NOT NULL 
       AND OLD.usuario_mensajero_id != NEW.usuario_mensajero_id THEN
        -- Set old mensajero as available
        UPDATE mensajeria.mensajero 
        SET disponibilidad = TRUE 
        WHERE id = OLD.usuario_mensajero_id;
        
        -- Set new mensajero as unavailable
        UPDATE mensajeria.mensajero 
        SET disponibilidad = FALSE 
        WHERE id = NEW.usuario_mensajero_id;
    END IF;
END//

-- Trigger for INSERT operations (when creating pedido with mensajero already assigned)
CREATE TRIGGER mensajeria.update_mensajero_availability_on_insert
AFTER INSERT ON mensajeria.pedido
FOR EACH ROW
BEGIN
    -- If pedido is created with a mensajero already assigned
    IF NEW.usuario_mensajero_id IS NOT NULL THEN
        UPDATE mensajeria.mensajero 
        SET disponibilidad = FALSE 
        WHERE id = NEW.usuario_mensajero_id;
    END IF;
END//

-- Optional: Trigger to handle pedido completion/delivery
-- This sets mensajero back to available when pedido is completed
CREATE TRIGGER mensajeria.update_mensajero_availability_on_complete
AFTER UPDATE ON mensajeria.pedido
FOR EACH ROW
BEGIN
    -- When pedido status changes to ENTREGADO, make mensajero available again
    IF OLD.estado != 'ENTREGADO' AND NEW.estado = 'ENTREGADO' 
       AND NEW.usuario_mensajero_id IS NOT NULL THEN
        UPDATE mensajeria.mensajero 
        SET disponibilidad = TRUE 
        WHERE id = NEW.usuario_mensajero_id;
    END IF;
END//

DELIMITER ;