-- Insertar un Deportista de prueba si no existe
INSERT INTO deportista (id, nombre_completo, posicion, estado_salud) VALUES (1, 'Juan Pérez', 'Delantero', 'Sano');

-- Insertar Entrenamientos para generar la gráfica (Datos 2026)
INSERT INTO entrenamiento (id, tarea, fecha_hora, completado, deportista_id) VALUES (101, 'Resistencia Aeróbica', '2026-01-10 10:00:00', true, 1);
INSERT INTO entrenamiento (id, tarea, fecha_hora, completado, deportista_id) VALUES (102, 'Control de Balón', '2026-02-15 10:00:00', true, 1);
INSERT INTO entrenamiento (id, tarea, fecha_hora, completado, deportista_id) VALUES (103, 'Potencia de Salto', '2026-03-20 10:00:00', true, 1);

-- Insertar Entrenamiento de otra temporada (2025) para probar el Filtro CP-CR-002
INSERT INTO entrenamiento (id, tarea, fecha_hora, completado, deportista_id) VALUES (104, 'Pretemporada 2025', '2025-12-20 10:00:00', true, 1);