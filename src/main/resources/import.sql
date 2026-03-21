-- Aseguramos que el deportista 1 existe
INSERT INTO deportista (id, nombre_completo, posicion, estado_salud) 
VALUES (1, 'Juan Pérez', 'Delantero', 'Sano') ON DUPLICATE KEY UPDATE id=id;

-- Datos para la gráfica y el PDF
INSERT INTO entrenamiento (id, tarea, fecha_hora, completado, deportista_id) VALUES 
(501, 'Físico Intensivo', '2026-02-10 09:00:00', true, 1),
(502, 'Táctica Grupal', '2026-03-05 11:00:00', true, 1),
(503, 'Control Balón', '2025-11-20 10:00:00', true, 1); -- Este es para probar el filtro 2025