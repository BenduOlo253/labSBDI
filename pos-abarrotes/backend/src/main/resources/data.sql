INSERT IGNORE INTO rol (id_rol, nombre, descripcion, activo) VALUES
(1, 'Administrador', 'Acceso total al MVP', true),
(2, 'Cajero', 'Operación de caja y ventas', true),
(3, 'Almacen', 'Gestión de inventario', true);

INSERT IGNORE INTO usuario (id_usuario, id_rol, nombre, username, password, activo, fecha_creacion) VALUES
(1, 1, 'Administrador', 'admin', 'admin123', true, NOW());

INSERT IGNORE INTO sucursal (id_sucursal, nombre, direccion, telefono, activo) VALUES
(1, 'Sucursal principal', 'Dirección principal', '555-0000', true);

INSERT IGNORE INTO caja (id_caja, id_sucursal, nombre, activo) VALUES
(1, 1, 'Caja 1', true);

INSERT IGNORE INTO categoria (id_categoria, nombre, descripcion, activo) VALUES
(1, 'Bebidas', 'Refrescos, aguas y jugos', true),
(2, 'Lácteos', 'Leche y derivados', true),
(3, 'Botanas', 'Frituras y snacks', true),
(4, 'Limpieza', 'Artículos de limpieza', true),
(5, 'Abarrotes', 'Productos generales', true);

INSERT IGNORE INTO unidad_medida (id_unidad, nombre, abreviatura, permite_decimales) VALUES
(1, 'Pieza', 'pz', false),
(2, 'Kilogramo', 'kg', true),
(3, 'Litro', 'l', true);

INSERT IGNORE INTO impuesto (id_impuesto, nombre, porcentaje, activo) VALUES
(1, 'IVA 16%', 16.00, true),
(2, 'IVA 0%', 0.00, true);

INSERT IGNORE INTO metodo_pago (id_metodo_pago, nombre, requiere_referencia, activo) VALUES
(1, 'Efectivo', false, true),
(2, 'Tarjeta', true, true),
(3, 'Transferencia', true, true);

INSERT IGNORE INTO producto (id_producto, codigo_barras, nombre, descripcion, id_categoria, id_unidad, id_impuesto, precio_compra, precio_venta, stock_minimo, stock_maximo, controla_inventario, activo, fecha_creacion) VALUES
(1, '7501055300075', 'Coca Cola 600ml', 'Refresco Coca Cola botella 600ml', 1, 1, 1, 12.00, 18.00, 10.000, 100.000, true, true, NOW()),
(2, '7501011111111', 'Sabritas Original', 'Papas Sabritas original', 3, 1, 1, 10.00, 16.00, 8.000, 80.000, true, true, NOW()),
(3, '7501020522028', 'Leche Lala 1L', 'Leche entera Lala 1 litro', 2, 1, 2, 20.00, 28.00, 6.000, 60.000, true, true, NOW()),
(4, '7501000632011', 'Galletas Marías', 'Galletas Marías paquete', 5, 1, 1, 11.00, 17.00, 10.000, 90.000, true, true, NOW()),
(5, '7501035910018', 'Jabón Zote', 'Jabón Zote barra', 4, 1, 1, 14.00, 22.00, 5.000, 50.000, true, true, NOW());

INSERT IGNORE INTO inventario (id_inventario, id_sucursal, id_producto, existencia, fecha_actualizacion) VALUES
(1, 1, 1, 50.000, NOW()),
(2, 1, 2, 40.000, NOW()),
(3, 1, 3, 30.000, NOW()),
(4, 1, 4, 60.000, NOW()),
(5, 1, 5, 25.000, NOW());
