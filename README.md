# 🧩 Unified Services API

**A Spring Boot microservice integrating multiple public APIs (Weather + Geolocation) into a single unified endpoint.**  
This project demonstrates **API integration, system orchestration, caching, containerization, CI/CD, and backend architecture best practices** — ideal for an **Integration Engineer** role.

---

## 🚀 Project Overview

The **Unified Services API** combines two public APIs:

- 🌍 **OpenStreetMap Nominatim API** — to geocode a city name into latitude & longitude.  
- ☁️ **Open-Meteo API** — to fetch real-time weather data using the coordinates.

The service exposes a unified REST endpoint:

```
GET /api/v1/unified-weather?city={cityName}&units=metric
```

This returns combined weather and location data in a single JSON response, caching results in PostgreSQL to reduce API calls.

---

## 🧠 Key Learning Objectives

| Category | Skills Demonstrated |
|-----------|---------------------|
| **Backend Development** | Spring Boot 3, REST API design, JPA, Hibernate |
| **API Integration** | Consuming and orchestrating multiple external APIs |
| **System Design** | Data caching, error handling, fault tolerance |
| **Database** | PostgreSQL schema design, Flyway migrations |
| **DevOps** | Docker Compose multi-container setup |
| **CI/CD** | GitHub Actions pipeline for build & test |
| **Security (optional)** | JWT authentication using Spring Security (disabled for demo) |
| **Documentation** | OpenAPI / Swagger for endpoint documentation |

---

## 🧩 Architecture

```
                ┌──────────────────────────────┐
                │          Client / UI         │
                └──────────────┬───────────────┘
                               │
                     (HTTP REST Request)
                               │
                ┌──────────────▼──────────────┐
                │     Unified Services API    │
                │  (Spring Boot Application)  │
                ├──────────────┬──────────────┤
                │    Service   │    Service   │
                │ (Geocoding)  │   (Weather)  │
                └──────┬───────┴───────┬──────┘
                       │               │
     ┌─────────────────▼───────────────▼────────────────┐
     │    External APIs (Nominatim / Open-Meteo)        │
     └─────────────────┬────────────────────────────────┘
                       │
            ┌──────────▼──────────┐
            │   PostgreSQL Cache  │
            │ (location + weather)│
            └─────────────────────┘
```

---

## ⚙️ Tech Stack

| Component | Technology |
|------------|-------------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3 (Web, Data JPA, Validation, Actuator) |
| **Database** | PostgreSQL 16 |
| **ORM** | Hibernate |
| **Migrations** | Flyway |
| **API Docs** | Swagger / Springdoc OpenAPI |
| **Containerization** | Docker & Docker Compose |
| **Build Tool** | Maven |
| **CI/CD** | GitHub Actions |
| **Cache Strategy** | Database-level caching with timestamp validation |
| **Auth (optional)** | JWT with Spring Security (commented out in code for easier demo) |

---

## 🧱 Project Structure

```
src/
 ├─ main/java/com/unified_services_api/
 │   ├─ controller/        → REST endpoints
 │   ├─ service/           → Business logic & API integration
 │   ├─ client/            → REST clients for external APIs
 │   ├─ entity/            → JPA entities (LocationCache, WeatherCache)
 │   ├─ repository/        → Spring Data Repositories
 │   ├─ config/            → App & Security configuration
 │   └─ dto/               → Data transfer objects
 │
 ├─ main/resources/
 │   ├─ application.yml
 │   ├─ application-docker.yml
 │   └─ db/migration/
 │        └─ V1__init.sql  → Flyway migration for table creation
 │
 └─ test/                  → Unit & integration tests
```

---

## 🐳 Docker Setup

This project runs using **Docker Compose** — including both the API and PostgreSQL.

### ▶️ Run in Docker

```bash
# 1. Build and run containers
docker compose up --build

# 2. Access the API
http://localhost:8080/api/v1/unified-weather?city=San Jose, US&units=metric

# 3. Swagger Docs
http://localhost:8080/swagger-ui/index.html

# 4. pgAdmin (optional)
http://localhost:5050
```

### 🧩 Containers

| Service | Description | Port |
|----------|--------------|------|
| `unified-api` | Spring Boot app | `8080` |
| `unified-db` | PostgreSQL database | `5432` |
| `pgadmin` | Database dashboard | `5050` |

---

## 🗃️ Database Schema (via Flyway)

```sql
CREATE TABLE location_cache (
  id BIGSERIAL PRIMARY KEY,
  normalized_city TEXT UNIQUE NOT NULL,
  latitude DOUBLE PRECISION NOT NULL,
  longitude DOUBLE PRECISION NOT NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE weather_cache (
  id BIGSERIAL PRIMARY KEY,
  normalized_city TEXT NOT NULL,
  window_start TIMESTAMP NOT NULL,
  temperature_c DOUBLE PRECISION,
  humidity_pct DOUBLE PRECISION,
  wind_speed_ms DOUBLE PRECISION,
  source TEXT NOT NULL,
  fetched_at TIMESTAMP NOT NULL DEFAULT NOW(),
  UNIQUE (normalized_city, window_start)
);
```

---

## 🧪 Sample Response

```json
{
  "city": "San Jose, US",
  "coordinates": { "latitude": 37.3361, "longitude": -121.8906 },
  "temperature_c": 23.4,
  "humidity_pct": 45,
  "wind_speed_ms": 3.2,
  "source": "Open-Meteo",
  "timestamp": "2025-11-12T06:42:03Z"
}
```

---

## 🧰 Development Highlights

✅ **REST API Integration** – Implemented service classes to consume and unify responses from two different REST APIs.  
✅ **Data Caching** – Cached API responses in PostgreSQL using JPA repositories to minimize redundant API calls.  
✅ **Error Handling** – Global exception handling and resilience strategies for external API downtime.  
✅ **Configuration Management** – Profile-based configs (`application.yml`, `application-docker.yml`) for local and container environments.  
✅ **Containerization** – Full environment reproducible via Docker Compose (API + DB + pgAdmin).  
✅ **Database Migration** – Managed schema evolution via Flyway for version-controlled DB changes.  
✅ **Testing** – Added unit tests for services and integration tests for API endpoints.  
✅ **Documentation** – Auto-generated Swagger UI documentation using Springdoc.  
✅ **CI/CD (Bonus)** – GitHub Actions workflow builds and runs Maven tests on each push.

---

## 💼 Relevance to Integration Engineer Roles

This project showcases:

- **API Orchestration**: Combining multiple third-party APIs into one cohesive endpoint.
- **Systems Integration**: Managing data flow between services and persistence layers.
- **Containerized Deployment**: Demonstrating knowledge of microservice environments.
- **Cloud Readiness**: Docker-based, easily portable to AWS ECS, Azure Container Apps, or GCP Cloud Run.
- **CI/CD Automation**: Demonstrates practical DevOps understanding.
- **Clean Architecture**: Modular layers for controllers, services, and repositories.

---

## 🔐 Optional Enhancements (Future Work)

- ✅ Re-enable JWT Authentication with Spring Security.
- ✅ Introduce Redis or Caffeine for in-memory caching.
- ✅ Add rate limiting for API protection.
- ✅ Deploy to AWS ECS / Railway / Render.
- ✅ Implement integration tests for all API paths.

---

## 👨‍💻 Author

**Richard Mulu Ndisya**  
📧 [rmulu333@gmail.com](mailto:rmulu333@gmail.com)  
🔗 [LinkedIn](https://www.linkedin.com/in/richard-ndisya-49b452255/)  
💼 Aspiring **Integration Engineer** / **Backend Developer**
