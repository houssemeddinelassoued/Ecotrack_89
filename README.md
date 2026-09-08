# 🌿 EcoTrack: Green IT Asset & Carbon Footprint Tracker

[![Build Status](https://img.shields.io/badge/Build-passing-success.svg)](https://github.com/houssemeddinelassoued/Ecotrack_89/actions/workflows/maven-ci.yml)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

EcoTrack is a robust, Java 25 Minimum Viable Product (MVP) designed for tracking the energy consumption of IT assets and calculating their associated carbon footprint. Our mission is to provide data-driven insights for greener IT operations.

---

## 🛠️ Tech Stack
*   **Backend Language:** Java 25
*   **Build Tool:** Maven
*   **Testing:** JUnit 5
*   **Database:** PostgreSQL (Schema definition derived from SQL Server DDL)
*   **Architecture:** Layered, with clean separation between domain logic and infrastructure concerns.

## ✨ Features
*   **Asset Tracking:** Manages IT assets with unique identification methods.
*   **Consumption Logging:** Records daily energy consumption for specific assets.
*   **Carbon Footprint Calculation:** Automatically calculates $\text{CO}_2\text{e}$ based on consumption and emission factors.
*   **Workflow Automation:** Automated CI/CD pipeline on push to `develop` branch.

## 📁 Project Structure Overview
*   `src/main/java/com/ecotrack/carbon`: Houses the independent carbon calculation domain logic.
*   `src/main/java/com/ecotrack/domain`: Contains core entities, business contracts, and services.
*   `src/main/resources/db/migration`: Flyway migration scripts for PostgreSQL.
*   `src/test/java/com/ecotrack/carbon`: Unit tests covering carbon calculation rules.
*   `EcoTrack-DDL.sql`: Reference SQL Server relational model definition.

## 🟢 Database Setup (PostgreSQL)
For local development, the database setup involves:
1.  **Environment:** Ensure you have a running PostgreSQL instance (e.g., via Docker Compose).
2.  **Initialization:** Copy `.env.example` to `.env` and set `POSTGRES_PASSWORD`.
3.  **Migrations:** Apply schema migrations using Maven:
    ```bash
    mvn flyway:migrate
    ```
4.  **Cleanup:** To reset the database, use `docker compose down -v`.

The PostgreSQL schema in `src/main/resources/db/migration/V1__create_ecotrack_schema.sql` translates the logical structure from the initial `EcoTrack-DDL.sql` while adopting PostgreSQL standards (e.g., `snake_case` identifiers).

## 🔬 Carbon Calculation Logic
The core business logic for carbon accounting is defined by the following formula:

$$\text{Carbon Footprint (kgCO}_2\text{e)} = \text{Energy Consumption (kWh)} \times \text{Emission Factor (kgCO}_2\text{e/kWh)}$$

The final result is consistently rounded to **four decimal places** using $\text{RoundingMode.HALF\_UP}$ to maintain high precision across the application.

## 🚀 Getting Started
### Prerequisites
*   JDK 21+
*   Maven
*   PostgreSQL Database (via Docker recommended)

### Building and Testing
Run the following command from the project root:
```bash
mvn clean verify
```
This command executes all unit tests, compiles the code, and packages the application, guaranteeing that all components are functional together.

---
*Last Updated: 2026-09-08*