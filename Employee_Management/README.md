# Java Spring Boot - Employee Management Backend

This folder contains the core API server that manages business logic, JWT authentication, and Hibernate ORM integrations.

## Tech Stack
- **Java 21**
- **Spring Boot 3.x**
- **PostgresSQL**
- **Spring JPA & Hibernate**

## Getting Started

1. Set up your **PostgreSQL** database (named `employee_management`).
2. Run the master `db_schema.sql` located in the project's root folder to pre-build the tables and seed data.
3. Update `src/main/resources/application.properties` with your system's Postgres password.
4. Run the backend via standard Maven commands:

```bash
mvn clean install
mvn spring-boot:run
```

The application defaults to `http://localhost:8080`.
The comprehensive REST API endpoints will be accessible, and error logs will cascade locally to this terminal pane.
