# EcoTrack

EcoTrack is a Java 25 MVP for tracking IT energy consumption and calculating its carbon footprint.

## Project structure

- `src/main/java/com/ecotrack/carbon`: independent carbon calculation domain.
- `src/test/java/com/ecotrack/carbon`: JUnit 5 tests for the carbon calculation rules.
- `EcoTrack-DDL.sql`: SQL Server relational model.
- `src/main/resources/db/migration`: Flyway PostgreSQL migrations (local development target).

## PostgreSQL (local development)

1. Copy `.env.example` to `.env` and set `POSTGRES_PASSWORD`. Never commit `.env`.
2. Start PostgreSQL 17: `docker compose up -d`.
3. Apply migrations: `mvn flyway:migrate` (reads `ECOTRACK_DB_URL`, `POSTGRES_USER`, `POSTGRES_PASSWORD` from the environment).
4. Stop the database: `docker compose down` (add `-v` to also remove `postgres_data/`).

The schema in `src/main/resources/db/migration/V1__create_ecotrack_schema.sql` mirrors `EcoTrack-DDL.sql`, translated to PostgreSQL with `snake_case` identifiers. No Java persistence layer is introduced yet.

## Carbon calculation

The carbon footprint is calculated with the following rule:

```text
carbon footprint (kgCO2e) = energy consumption (kWh) x emission factor (kgCO2e/kWh)
```

The result is rounded to four decimal places, matching `DailyConsumption.CarbonKgCo2e`.

## Build and test

```powershell
mvn test
```