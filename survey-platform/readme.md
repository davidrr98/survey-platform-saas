# Survey Platform — Backend

API REST para gestión y respuesta de encuestas, construida con Spring Boot 4 y PostgreSQL.

---

## Stack

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | PostgreSQL |
| Seguridad | Spring Security |
| Documentación | SpringDoc OpenAPI (Swagger UI) |
| Build | Maven 3.9.x |

---

## Requisitos previos

- Java 21+
- Maven 3.9+
- PostgreSQL 16+ corriendo localmente

---

## Configuración

Edita `src/main/resources/application.yaml` con tus credenciales:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: mysecretpassword
  jpa:
    hibernate:
      ddl-auto: update        # crea tablas automáticamente al iniciar
    defer-datasource-initialization: true
  sql:
    init:
      mode: always            # ejecuta data.sql en cada inicio
```

> **Nota:** `ddl-auto: update` crea las tablas si no existen y las actualiza si hay cambios en las entidades. Para producción usar `validate` con Flyway.

---

## Cómo correr el proyecto

```bash
# Clonar el repositorio
git clone <url-del-repositorio>
cd survey-platform

# Compilar y ejecutar
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`

La documentación Swagger en `http://localhost:8080/swagger-ui/index.html`

---

## Datos iniciales

Al iniciar, Spring ejecuta automáticamente `src/main/resources/data.sql` que carga:

| Survey | Status | Descripción |
|---|---|---|
| Satisfacción del Cliente | `ACTIVE` | Cubre los 4 tipos de pregunta — lista para responder |
| Evaluación Interna | `DRAFT` | No respondible — para probar validación de estado |
| Encuesta Cerrada | `CLOSED` | No respondible — para probar validación de estado |

---

## Arquitectura

El proyecto sigue una arquitectura modular por dominio de negocio:

```
com.surveysaas.survey_platform/
├── shared/              # Infraestructura transversal
│   ├── config/          # SecurityConfig, OpenApiConfig
│   ├── exception/       # GlobalExceptionHandler, ResourceNotFoundException
│   ├── health/          # HealthController
│   └── response/        # ApiResponse (wrapper estándar)
├── surveys/             # CRUD de encuestas
├── questions/           # CRUD de preguntas y opciones
├── responses/           # Vista pública y envío de respuestas
└── analytics/           # Resultados y stream en tiempo real (SSE)
```

Cada módulo sigue la misma estructura interna:

```
{modulo}/
├── controller/
├── service/        # interfaz + implementación (principio D de SOLID)
├── repository/
├── domain/         # entidades JPA
└── dto/            # objetos de transferencia (nunca se expone la entidad directamente)
```

---

## Decisiones de diseño

**Separación domain/dto:** las entidades JPA nunca se exponen directamente en la API. Esto evita exponer campos internos, romper contratos de API ante cambios de modelo, y problemas de serialización con relaciones lazy.

**Interfaz + implementación en servicios:** cada servicio define una interfaz (`SurveyService`) y una implementación (`SurveyServiceImpl`). Esto aplica el principio de inversión de dependencias — los controladores dependen de la abstracción, no de la implementación concreta.

**SSE sobre WebSockets:** para los resultados en tiempo real se eligió Server-Sent Events en lugar de WebSockets. SSE es unidireccional (servidor → cliente), más simple de implementar y suficiente para el caso de uso de actualización de resultados. Menor overhead de conexión.

**respondentToken:** cada respuesta genera un UUID anónimo. Permite trazabilidad y auditoría sin exponer la identidad del respondiente — relevante en contextos donde la privacidad es un requisito regulatorio.

**URLs anidadas para questions:** `GET /api/v1/surveys/{surveyId}/questions` refleja la jerarquía real del dominio — una pregunta no existe fuera de una encuesta.

---

## Endpoints

### Health
| Método | Endpoint | Auth | Descripción |
|---|---|---|---|
| GET | `/api/v1/health` | No | Estado del servicio |

### Surveys (admin)
| Método | Endpoint | Auth | Descripción |
|---|---|---|---|
| POST | `/api/v1/surveys` | No* | Crear encuesta |
| GET | `/api/v1/surveys` | No* | Listar todas |
| GET | `/api/v1/surveys/{id}` | No* | Obtener por id |
| PUT | `/api/v1/surveys/{id}` | No* | Actualizar |
| DELETE | `/api/v1/surveys/{id}` | No* | Eliminar |

### Questions (admin)
| Método | Endpoint | Auth | Descripción |
|---|---|---|---|
| POST | `/api/v1/surveys/{surveyId}/questions` | No* | Crear pregunta |
| GET | `/api/v1/surveys/{surveyId}/questions` | No* | Listar preguntas |
| GET | `/api/v1/surveys/{surveyId}/questions/{id}` | No* | Obtener por id |
| PUT | `/api/v1/surveys/{surveyId}/questions/{id}` | No* | Actualizar |
| DELETE | `/api/v1/surveys/{surveyId}/questions/{id}` | No* | Eliminar |

### Vista Pública
| Método | Endpoint | Auth | Descripción |
|---|---|---|---|
| GET | `/api/v1/public/surveys` | No | Listar encuestas activas |
| GET | `/api/v1/public/surveys/{id}` | No | Ver encuesta con preguntas |
| POST | `/api/v1/public/surveys/{id}/respond` | No | Enviar respuestas |

### Analytics
| Método | Endpoint | Auth | Descripción |
|---|---|---|---|
| GET | `/api/v1/surveys/{id}/analytics` | No* | Snapshot de resultados |
| GET | `/api/v1/surveys/{id}/analytics/stream` | No* | Stream SSE en tiempo real |

> *Auth deshabilitada temporalmente durante desarrollo. En producción estos endpoints requieren JWT.

---

## Ejemplos de uso

### Listar encuestas disponibles
```bash
curl http://localhost:8080/api/v1/public/surveys
```

### Ver una encuesta
```bash
curl http://localhost:8080/api/v1/public/surveys/aaaaaaaa-0000-0000-0000-000000000001
```

### Responder una encuesta
```bash
curl -X POST http://localhost:8080/api/v1/public/surveys/aaaaaaaa-0000-0000-0000-000000000001/respond \
  -H "Content-Type: application/json" \
  -d '{
    "answers": [
      { "questionId": "bbbbbbbb-0000-0000-0000-000000000001", "textValue": "4" },
      { "questionId": "bbbbbbbb-0000-0000-0000-000000000002", "optionId": "cccccccc-0000-0000-0000-000000000001" },
      { "questionId": "bbbbbbbb-0000-0000-0000-000000000003", "optionId": "cccccccc-0000-0000-0000-000000000005" },
      { "questionId": "bbbbbbbb-0000-0000-0000-000000000004", "textValue": "Mejorar tiempos de respuesta" }
    ]
  }'
```

> Para preguntas `MULTIPLE_CHOICE` envía la misma pregunta varias veces en el array, una por cada opción seleccionada.

### Ver resultados en tiempo real
```bash
# Terminal 1 — suscribirse al stream
curl -N http://localhost:8080/api/v1/surveys/aaaaaaaa-0000-0000-0000-000000000001/analytics/stream

# Terminal 2 — responder (verás el evento llegar en Terminal 1)
curl -X POST http://localhost:8080/api/v1/public/surveys/aaaaaaaa-0000-0000-0000-000000000001/respond \
  -H "Content-Type: application/json" \
  -d '{ "answers": [...] }'
```

---

## Formato de respuesta

Todos los endpoints retornan el mismo wrapper:

```json
{
  "success": true,
  "data": { },
  "message": null,
  "timestamp": "2026-05-10T15:30:00Z"
}
```

En caso de error:

```json
{
  "success": false,
  "data": null,
  "message": "Survey no encontrado con id: ...",
  "timestamp": "2026-05-10T15:30:00Z"
}
```

### Códigos de respuesta

| Código | Situación |
|---|---|
| 200 | Consulta exitosa |
| 201 | Recurso creado |
| 204 | Eliminación exitosa |
| 400 | Error de validación |
| 404 | Recurso no encontrado |
| 409 | Encuesta no disponible (DRAFT o CLOSED) |
| 500 | Error interno |

---

## Tipos de pregunta

| Tipo | Descripción | Envío en respuesta |
|---|---|---|
| `SINGLE_CHOICE` | Selección única | `optionId` |
| `MULTIPLE_CHOICE` | Selección múltiple | `optionId` (una entrada por opción) |
| `TEXT` | Texto libre | `textValue` |
| `NUMERIC` | Valor numérico | `textValue` con número |

---

## Documentación interactiva

Swagger UI disponible en:

```
http://localhost:8080/swagger-ui/index.html
```