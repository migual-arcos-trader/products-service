-- docker-postgres/init.sql
CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT
);

-- Insertar 20 registros de ejemplo
INSERT INTO products (name, price, description) VALUES
('Laptop HP EliteBook', 1200.99, 'Core i7, 16GB RAM, 512GB SSD'),
('iPhone 15 Pro', 999.00, '256GB, Titanio negro'),
('Samsung Galaxy S24', 899.50, 'Pantalla AMOLED 6.2"'),
('Teclado Mecánico RGB', 89.99, 'Switches Red, QWERTY'),
('Monitor 4K 27"', 450.75, 'HDR10, 144Hz'),
('Smartwatch Garmin', 249.99, 'Resistente al agua 100m'),
('Auriculares Sony WH-1000XM5', 349.00, 'Cancelación de ruido'),
('Disco SSD 1TB', 129.99, 'Lectura 3500MB/s'),
('Cámara Canon EOS R6', 1999.00, '20MP, 4K 60fps'),
('Impresora Laser', 230.50, 'WiFi, Duplex'),
('Router WiFi 6', 199.99, 'AX5400, 8 antenas'),
('Altavoz Bluetooth JBL', 79.99, '20W, IPX7'),
('Tablet Samsung S9', 649.00, 'S-Pen incluido'),
('Webcam 4K', 120.00, 'HDR, Micrófono integrado'),
('Mouse Logitech MX Master', 99.95, 'Ergonómico inalámbrico'),
('Tarjeta Gráfica RTX 4070', 599.00, '12GB GDDR6X'),
('Disco Duro Externo 2TB', 89.99, 'USB 3.2 Gen 2'),
('Kit Limpieza PC', 19.99, 'Aire comprimido + brochas'),
('Silla Gamer', 299.00, 'Ergonómica con soporte lumbar'),
('Micrófono Blue Yeti', 129.00, 'USB, 4 patrones polares');