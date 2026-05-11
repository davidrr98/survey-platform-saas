# Survey Platform - Frontend

Aplicación web para la creación, gestión y análisis de encuestas en línea.

## Tecnologías

- **Angular 21** — framework principal
- **Angular Material** — componentes de UI
- **ngx-toastr** — notificaciones
- **TypeScript 5.9**

## Requisitos previos

- Node.js >= 20
- npm >= 11

## Instalación

```bash
npm install
```

## Comandos disponibles

| Comando | Descripción |
|---|---|
| `npm start` | Servidor de desarrollo en `http://localhost:4200` |
| `npm run build` | Build de producción en `/dist` |
| `npm test` | Ejecutar pruebas unitarias con Vitest |
| `npm run watch` | Build en modo watch (desarrollo) |

## Estructura del proyecto

```
src/app/
├── core/
│   ├── models/        # Interfaces y enums (Survey, Question, etc.)
│   └── services/      # Servicios HTTP (BaseApiService, SurveyService, etc.)
├── modules/
│   ├── surveys/       # Gestión de encuestas (lista, formulario, detalle)
│   ├── analytics/     # Dashboard de analíticas
│   └── public-survey/ # Vista pública para responder encuestas
├── layouts/           # Layout principal y manejo de errores
└── shared/            # Componentes y módulos compartidos
```

## Módulos principales

- **Surveys** (`/surveys`) — crear, editar y administrar encuestas
- **Analytics** (`/analytics`) — ver resultados y métricas de encuestas
- **Public Survey** (`/public`) — interfaz para que los usuarios respondan encuestas

## Tipos de preguntas soportados

`MULTIPLE_CHOICE` · `SINGLE_CHOICE` · `TEXT` · `RATING` · `YES_NO` · `NUMERIC`

## Configuración del entorno

El archivo de entorno se encuentra en `src/env/enviroment.ts`. Ajusta la variable `apiUrl` para apuntar al backend correspondiente.

```ts
export const environment = {
  apiUrl: 'http://localhost:8081/api'
};
```
