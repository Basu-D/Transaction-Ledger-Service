# Docker Setup

This document explains how to run the Transaction Ledger Service using Docker.

## Prerequisites

- Docker
- Docker Compose

## Quick Start

To build and run the entire application with database initialization:

```bash
docker-compose up --build
```

This will:
1. Start PostgreSQL database
2. Create the `ledger` database if it doesn't exist
3. Execute the schema.sql to create tables if they don't exist
4. Build and start the application

## How It Works

### Database Initialization

The setup uses a two-stage initialization:

1. **PostgreSQL Container**: Creates the database automatically via `POSTGRES_DB` environment variable. On first run, it also executes `schema.sql` from the `/docker-entrypoint-initdb.d/` directory.

2. **Init-DB Service**: A separate service that runs after PostgreSQL is healthy. It checks if the schema tables exist, and creates them if they don't. This ensures the schema is created even if the database already exists.

### Application Container

The application container:
- Builds the Java application using Gradle
- Connects to PostgreSQL using environment variables
- Exposes the REST API on port 8080

## Environment Variables

The application supports the following environment variables (with fallback to `application.properties`):

- `DB_HOST` - Database host (default: `postgres` in Docker, `localhost` locally)
- `DB_PORT` - Database port (default: `5432`)
- `DB_DATABASE` - Database name (default: `ledger`)
- `DB_USER` - Database user (default: `postgres`)
- `DB_PASSWORD` - Database password (default: `postgres`)
- `http.port` - Application HTTP port (default: `8080`)

## Stopping the Services

```bash
docker-compose down
```

To also remove the database volume:

```bash
docker-compose down -v
```

## Viewing Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f app
docker-compose logs -f postgres
```

## Testing the API

Once the services are running, you can test the API:

```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": "acc-123",
    "amount": 100,
    "type": "DEBIT",
    "idempotencyKey": "abc-123"
  }'
```

## Troubleshooting

### Database connection issues

If the application can't connect to the database, ensure:
1. The `init-db` service completed successfully
2. The `postgres` service is healthy
3. Environment variables are set correctly

### Schema not created

If tables are missing:
1. Check the `init-db` service logs: `docker-compose logs init-db`
2. Manually connect and verify: `docker-compose exec postgres psql -U postgres -d ledger`

### Rebuilding after code changes

```bash
docker-compose up --build
```

Or rebuild just the app:

```bash
docker-compose build app
docker-compose up app
```

