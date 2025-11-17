-- ===========================================
-- DATOS INICIALES PARA RED SÍSMICA
-- ===========================================

-- ROLES
INSERT INTO roles (id, nombre, descripcion_rol) VALUES 
(1, 'ResponsableReparacion', 'Encargado de coordinar y realizar reparaciones del sismógrafo'),
(2, 'Tecnico', 'Encargado de tareas técnicas generales'),
(3, 'Supervisor', 'Supervisa las tareas de inspección y mantenimiento'),
(4, 'Administrador', 'Gestiona usuarios y configuraciones del sistema');

-- ESTADOS
INSERT INTO estados (id, ambito, nombre_estado) VALUES 
(1, 'OrdenInspeccion', 'Completamente Realizada'),
(2, 'OrdenInspeccion', 'Cerrada'),
(3, 'Sismografo', 'Fuera de Servicio'),
(4, 'Sismografo', 'En Servicio'),
(5, 'OrdenInspeccion', 'En Proceso');

-- EMPLEADOS
INSERT INTO empleados (id, nombre, apellido, mail, telefono, rol_id) VALUES 
(1, 'Lucía', 'Gómez', 'lucia.gomez@sismo.gob.ar', '3511234567', 1),
(2, 'Martín', 'Fernández', 'martin.fernandez@sismo.gob.ar', '3512345678', 1),
(3, 'Sofía', 'López', 'sofia.lopez@sismo.gob.ar', '3513456789', 1),
(4, 'Carlos', 'Ramírez', 'carlos.ramirez@sismo.gob.ar', '3514567890', 3),
(5, 'Ana', 'Torres', 'ana.torres@sismo.gob.ar', '3515678901', 4);

-- USUARIOS
INSERT INTO usuarios (id, nombre_usuario, contrasena, empleado_id) VALUES 
(1, 'lucia.g', 'pass123', 1),
(2, 'martin.f', 'mantenimiento123', 2);

-- CAMBIOS DE ESTADO (estados iniciales para sismógrafos)
INSERT INTO cambios_estado (id, nombre_estado, fecha_hora_inicio, fecha_hora_fin, responsable_id, estado_id) VALUES 
(1, 'En Servicio', '2024-10-01 08:00:00', NULL, 1, 4),
(2, 'En Servicio', '2024-10-05 09:30:00', NULL, 2, 4),
(3, 'En Servicio', '2024-10-10 14:15:00', NULL, 3, 4);

-- SISMÓGRAFOS
INSERT INTO sismografos (id, fecha_adquisicion, identificador_sismografo, nro_serie, estado_actual_id, estado_id, estacion_id) VALUES 
(1, '2020-06-15', 'SIS-001', 1001, 1, 4, NULL),
(2, '2021-09-10', 'SIS-002', 1002, 2, 4, NULL),
(3, '2022-01-25', 'SIS-003', 1003, 3, 4, NULL);

-- ESTACIONES SISMOLÓGICAS
INSERT INTO estaciones_sismologicas (id, codigo_estacion, documento_certificacion_adq, fecha_solicitud_certificacion, latitud, longitud, nombre_estacion, nro_certificacion_adquisicion, sismografo_id) VALUES 
(1, 101, 'Cert-2023-0001', '2023-05-10', -34.6037, -58.3816, 'Estacion Centro', 5001, 1),
(2, 102, 'Cert-2023-0002', '2024-01-20', -33.4489, -70.6693, 'Estacion Norte', 5002, 2),
(3, 103, 'Cert-2023-0003', '2023-11-15', -31.4201, -64.1888, 'Estacion Sur', 5003, 3);

-- Actualizar los sismógrafos con la referencia a las estaciones
UPDATE sismografos SET estacion_id = 1 WHERE id = 1;
UPDATE sismografos SET estacion_id = 2 WHERE id = 2;
UPDATE sismografos SET estacion_id = 3 WHERE id = 3;

-- ÓRDENES DE INSPECCIÓN
INSERT INTO ordenes_inspeccion (id, numero_orden, fecha_hora_inicio, fecha_hora_finalizacion, fecha_hora_cierre, observacion_cliente, empleado_id, estacion_id, estado_id) VALUES 
(1, 'ORD-0001', '2025-01-10 09:30:00', '2025-05-12 16:00:00', NULL, NULL, 2, 1, 1),
(2, 'ORD-0002', '2025-02-11 14:00:00', '2025-04-11 14:00:00', NULL, NULL, 1, 1, 1),
(3, 'ORD-0003', '2025-04-11 14:00:00', '2025-05-11 14:00:00', NULL, NULL, 1, 2, 1),
(4, 'ORD-0004', '2025-03-15 10:00:00', '2025-06-20 17:00:00', NULL, NULL, 1, 3, 1);

-- SESIONES (sesión activa para pruebas)
INSERT INTO sesiones (ID, FECHAINICIO, FECHAFIN, USUARIO_ID) VALUES 
(1, TIMESTAMP '2025-11-17 08:00:00', NULL, 1);
