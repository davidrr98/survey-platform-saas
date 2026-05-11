-- ── Surveys ──────────────────────────────────────────────────────
INSERT INTO surveys (id, title, description, status, created_at, updated_at) VALUES
                                                                                 ('aaaaaaaa-0000-0000-0000-000000000001', 'Satisfacción del Cliente', 'Encuesta activa para pruebas de respuesta pública.', 'ACTIVE', NOW(), NOW()),
                                                                                 ('aaaaaaaa-0000-0000-0000-000000000002', 'Evaluación Interna',       'Encuesta en borrador, no debe ser respondible.',     'DRAFT',  NOW(), NOW()),
                                                                                 ('aaaaaaaa-0000-0000-0000-000000000003', 'Encuesta Cerrada',         'Encuesta cerrada, no debe ser respondible.',         'CLOSED', NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

-- ── Questions ────────────────────────────────────────────────────
INSERT INTO questions (id, text, type, question_order, required, survey_id) VALUES
-- Survey ACTIVE: un ejemplo de cada tipo
('bbbbbbbb-0000-0000-0000-000000000001', '¿Cómo calificarías el servicio? (1 al 5)',  'NUMERIC',         1, true,  'aaaaaaaa-0000-0000-0000-000000000001'),
('bbbbbbbb-0000-0000-0000-000000000002', '¿Nos recomendarías?',                        'SINGLE_CHOICE',   2, true,  'aaaaaaaa-0000-0000-0000-000000000001'),
('bbbbbbbb-0000-0000-0000-000000000003', '¿Qué canales has usado?',                    'MULTIPLE_CHOICE', 3, true,  'aaaaaaaa-0000-0000-0000-000000000001'),
('bbbbbbbb-0000-0000-0000-000000000004', '¿Qué mejorarías?',                           'TEXT',            4, false, 'aaaaaaaa-0000-0000-0000-000000000001'),
-- Survey DRAFT: solo una pregunta
('bbbbbbbb-0000-0000-0000-000000000005', '¿Los procesos son claros?',                  'SINGLE_CHOICE',   1, true,  'aaaaaaaa-0000-0000-0000-000000000002')
    ON CONFLICT (id) DO NOTHING;

-- ── Options ──────────────────────────────────────────────────────
INSERT INTO options (id, text, option_order, question_id) VALUES
-- Pregunta 2: SINGLE_CHOICE
('cccccccc-0000-0000-0000-000000000001', 'Sí, definitivamente', 1, 'bbbbbbbb-0000-0000-0000-000000000002'),
('cccccccc-0000-0000-0000-000000000002', 'Probablemente sí',    2, 'bbbbbbbb-0000-0000-0000-000000000002'),
('cccccccc-0000-0000-0000-000000000003', 'Probablemente no',    3, 'bbbbbbbb-0000-0000-0000-000000000002'),
('cccccccc-0000-0000-0000-000000000004', 'No',                  4, 'bbbbbbbb-0000-0000-0000-000000000002'),
-- Pregunta 3: MULTIPLE_CHOICE
('cccccccc-0000-0000-0000-000000000005', 'App móvil',           1, 'bbbbbbbb-0000-0000-0000-000000000003'),
('cccccccc-0000-0000-0000-000000000006', 'Sucursal',            2, 'bbbbbbbb-0000-0000-0000-000000000003'),
('cccccccc-0000-0000-0000-000000000007', 'Teléfono',            3, 'bbbbbbbb-0000-0000-0000-000000000003'),
('cccccccc-0000-0000-0000-000000000008', 'Portal web',          4, 'bbbbbbbb-0000-0000-0000-000000000003'),
-- Pregunta 5: SINGLE_CHOICE (survey DRAFT)
('cccccccc-0000-0000-0000-000000000009', 'Sí',          1, 'bbbbbbbb-0000-0000-0000-000000000005'),
('cccccccc-0000-0000-0000-000000000010', 'No',          2, 'bbbbbbbb-0000-0000-0000-000000000005'),
('cccccccc-0000-0000-0000-000000000011', 'Parcialmente', 3, 'bbbbbbbb-0000-0000-0000-000000000005')
    ON CONFLICT (id) DO NOTHING;

-- ── Responses ────────────────────────────────────────────────────
-- Cada respondent_token es un UUID anónimo único por participante
INSERT INTO responses (id, respondent_token, submitted_at, survey_id) VALUES
('dddddddd-0000-0000-0000-000000000001', 'eeeeeeee-0000-0000-0000-000000000001', NOW(), 'aaaaaaaa-0000-0000-0000-000000000001'),
('dddddddd-0000-0000-0000-000000000002', 'eeeeeeee-0000-0000-0000-000000000002', NOW(), 'aaaaaaaa-0000-0000-0000-000000000001'),
('dddddddd-0000-0000-0000-000000000003', 'eeeeeeee-0000-0000-0000-000000000003', NOW(), 'aaaaaaaa-0000-0000-0000-000000000001')
    ON CONFLICT (id) DO NOTHING;

-- ── Answers ──────────────────────────────────────────────────────
-- Respuesta 1: calificó 5, recomendaría, usó App móvil + Portal web, dejó comentario
-- Respuesta 2: calificó 3, probablemente no recomendaría, usó Sucursal + Teléfono, sin comentario
-- Respuesta 3: calificó 4, probablemente sí recomendaría, usó App móvil, dejó comentario
INSERT INTO answers (id, response_id, question_id, option_id, text_value) VALUES
-- ── Respuesta 1 ──
-- Q1 NUMERIC
('ffffffff-0000-0000-0000-000000000001', 'dddddddd-0000-0000-0000-000000000001', 'bbbbbbbb-0000-0000-0000-000000000001', null, '5'),
-- Q2 SINGLE_CHOICE → "Sí, definitivamente"
('ffffffff-0000-0000-0000-000000000002', 'dddddddd-0000-0000-0000-000000000001', 'bbbbbbbb-0000-0000-0000-000000000002', 'cccccccc-0000-0000-0000-000000000001', null),
-- Q3 MULTIPLE_CHOICE → "App móvil" y "Portal web" (dos filas)
('ffffffff-0000-0000-0000-000000000003', 'dddddddd-0000-0000-0000-000000000001', 'bbbbbbbb-0000-0000-0000-000000000003', 'cccccccc-0000-0000-0000-000000000005', null),
('ffffffff-0000-0000-0000-000000000004', 'dddddddd-0000-0000-0000-000000000001', 'bbbbbbbb-0000-0000-0000-000000000003', 'cccccccc-0000-0000-0000-000000000008', null),
-- Q4 TEXT
('ffffffff-0000-0000-0000-000000000005', 'dddddddd-0000-0000-0000-000000000001', 'bbbbbbbb-0000-0000-0000-000000000004', null, 'Los tiempos de espera podrían ser menores.'),
-- ── Respuesta 2 ──
-- Q1 NUMERIC
('ffffffff-0000-0000-0000-000000000006', 'dddddddd-0000-0000-0000-000000000002', 'bbbbbbbb-0000-0000-0000-000000000001', null, '3'),
-- Q2 SINGLE_CHOICE → "Probablemente no"
('ffffffff-0000-0000-0000-000000000007', 'dddddddd-0000-0000-0000-000000000002', 'bbbbbbbb-0000-0000-0000-000000000002', 'cccccccc-0000-0000-0000-000000000003', null),
-- Q3 MULTIPLE_CHOICE → "Sucursal" y "Teléfono"
('ffffffff-0000-0000-0000-000000000008', 'dddddddd-0000-0000-0000-000000000002', 'bbbbbbbb-0000-0000-0000-000000000003', 'cccccccc-0000-0000-0000-000000000006', null),
('ffffffff-0000-0000-0000-000000000009', 'dddddddd-0000-0000-0000-000000000002', 'bbbbbbbb-0000-0000-0000-000000000003', 'cccccccc-0000-0000-0000-000000000007', null),
-- Q4 TEXT (opcional, no respondió)
-- ── Respuesta 3 ──
-- Q1 NUMERIC
('ffffffff-0000-0000-0000-000000000010', 'dddddddd-0000-0000-0000-000000000003', 'bbbbbbbb-0000-0000-0000-000000000001', null, '4'),
-- Q2 SINGLE_CHOICE → "Probablemente sí"
('ffffffff-0000-0000-0000-000000000011', 'dddddddd-0000-0000-0000-000000000003', 'bbbbbbbb-0000-0000-0000-000000000002', 'cccccccc-0000-0000-0000-000000000002', null),
-- Q3 MULTIPLE_CHOICE → "App móvil"
('ffffffff-0000-0000-0000-000000000012', 'dddddddd-0000-0000-0000-000000000003', 'bbbbbbbb-0000-0000-0000-000000000003', 'cccccccc-0000-0000-0000-000000000005', null),
-- Q4 TEXT
('ffffffff-0000-0000-0000-000000000013', 'dddddddd-0000-0000-0000-000000000003', 'bbbbbbbb-0000-0000-0000-000000000004', null, 'Mejoraría la app móvil, a veces es lenta.')
    ON CONFLICT (id) DO NOTHING;