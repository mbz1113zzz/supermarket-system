# Supermarket Management System

A Spring Boot 3 + Vue 2 platform for supermarket operations: members, cards, products, orders, procurement, and marketing.

## Project Structure

```
supermarket-system/
├── src/main/java/org/cityu/supermarket/
│   ├── controller/     # REST controllers
│   ├── service/        # Business services
│   ├── mapper/         # MyBatis mappers
│   ├── entity/         # Entity models
│   ├── dto/            # Data transfer objects
│   ├── security/       # JWT & Spring Security
│   ├── config/         # Config (Swagger, CORS, etc.)
│   └── common/         # Shared helpers, constants, exception handlers
├── src/main/resources/
│   ├── mapper/         # MyBatis XML mappings
│   └── application.properties
├── src/test/           # Unit tests (JUnit 5)
├── db/
│   ├── core/           # Core SQL (members, products, orders)
│   └── extension/      # Extension SQL (procurement, marketing)
├── frontend/           # Vue 2 + Element UI app
│   ├── src/views/      # Page components
│   ├── src/api/        # API wrappers
│   └── src/store/      # Vuex store
└── pom.xml
```

## Tech Stack

| Layer | Stack |
|-------|-------|
| Backend | Spring Boot 3.x, MyBatis |
| Security | Spring Security, JWT |
| Database | MySQL 8.x / MariaDB |
| API Docs | Knife4j (OpenAPI 3) |
| Frontend | Vue 2, Element UI, Vuex, Vue Router |
| Build Tools | Maven, npm |

## Quick Start

### 1. Prerequisites

- JDK 17+
- Maven 3.9+
- MySQL 8.x or MariaDB
- Node.js 16+

### 2. Initialize Database

```bash
# Create database and import schema + seed data
mysql -u root -p < db/init.sql
```

Or run step by step:
```bash
mysql -u root -p supermarket < db/core/schema.sql
mysql -u root -p supermarket < db/core/data.sql
```

### 3. Start Backend

```bash
# From project root
mvn spring-boot:run
```

Backend base URL: `http://localhost:8081/supermarket`

### 4. Start Frontend

```bash
cd frontend
npm install
npm run serve
```

Frontend runs at `http://localhost:8080`

### 5. Access

- **Web UI**: http://localhost:8080
- **API Docs**: http://localhost:8081/supermarket/doc.html
- **Default Admin**: `admin` / `123456`

## Configuration

Copy `.env.example` to `.env` and modify if needed:

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | `127.0.0.1` | Database host |
| `DB_PORT` | `3306` | Database port |
| `DB_NAME` | `supermarket` | Database name |
| `DB_USERNAME` | `root` | DB user |
| `DB_PASSWORD` | `123456` | DB password |
| `SERVER_PORT` | `8081` | Backend server port |

Other settings (JWT, CORS, logging) have sensible defaults in `application.properties`.

## Run Tests

```bash
# Tests use the dedicated supermarket_test database
mvn test
```

Test profile config: `src/test/resources/application-test.properties` (points to `supermarket_test` to keep prod data clean).

## Features

- Member management — register, query, edit
- Member card — issue, recharge, suspend, points
- Product management — CRUD, categories, inventory
- Order management — creation, details, balance/points payment
- Transaction records — recharge & consumption logs
- Procurement — suppliers, purchase orders, inbound tracking
- Marketing — promotions, coupons, customer behavior analysis
- Dashboard — statistics overview, analytics widgets & KPIs

