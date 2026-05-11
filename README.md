# Survey Platform SaaS

Plataforma web para la creación, gestión y análisis de encuestas en línea.

## Stack

| Capa | Tecnología |
|---|---|
| Frontend | Angular 21 + Angular Material |
| Backend | Spring Boot 4 · Java 21 · Maven |
| Base de datos | PostgreSQL 16 |
| Contenedores | Docker + Docker Compose |

## Estructura del repositorio

```
survey-platform-saas/
├── survey-platform/           # API REST (Spring Boot)
├── front-survey-platform/     # Aplicación web (Angular)
└── docker-compose.yml
```

- Backend: ver [survey-platform/readme.md](survey-platform/readme.md)
- Frontend: ver [front-survey-platform/README.md](front-survey-platform/README.md)

---

## Arquitectura

### Contexto
![C4 Context](Diagramas/Encuestas%20SAAS-C4%20-%20Context.png)

### Contenedores
![C4 Container](Diagramas/Encuestas%20SAAS-C4%20-%20Container.png)

### Componentes
![C4 Component](Diagramas/Encuestas%20SAAS-C4%20-%20Component.png)

### Propuesta Cloud
![Cloud](Diagramas/Encuestas%20SAAS-Cloud%20propuesta.png)

---

## Despliegue con Docker Compose

### Requisitos previos

- Docker Desktop
- Java 21 y Maven 3.9+

### Levantar el proyecto

```powershell
./deploy.ps1
```

El script compila el JAR del backend con Maven local y luego levanta todos los contenedores. Para correr en background:

```powershell
./deploy.ps1 -d
```

Una vez levantado:

| Servicio | URL |
|---|---|
| Frontend | http://localhost:4200 |
| Backend API | http://localhost:8081 |
| Swagger UI | http://localhost:8081/swagger-ui/index.html |
| PostgreSQL | localhost:5432 |

### Detener el proyecto

```powershell
docker compose down
```

Para eliminar también los datos de la base de datos:

```powershell
docker compose down -v
```

---

## Desarrollo local (sin Docker)

Requisitos: Java 21, Maven 3.9+, Node.js 20+, PostgreSQL 16 corriendo localmente.

```powershell
# Backend
cd survey-platform
./mvnw spring-boot:run

# Frontend (en otra terminal)
cd front-survey-platform
npm install
npm start
```

---

## Credenciales por defecto (desarrollo)

| Parámetro | Valor |
|---|---|
| DB host | `localhost:5432` |
| DB name | `postgres` |
| DB user | `postgres` |
| DB password | `mysecretpassword` |
