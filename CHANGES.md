# Project Refactoring & Standardization Report

## Overview
This document details the major refactoring efforts undertaken to standardize the Employee Management System backend. The goal was to align the Java Spring Boot application with a robust, relational PostgreSQL database schema.

## Key Changes

### 1. Standardization of Entity Naming
* **Problem:** Files were inconsistently named (e.g., `PerformanceEvaluation.java` vs `PerformanceController.java`).
* **Solution:** Adopted a strict naming convention:
    * **Entity:** `[Name]Entity.java` (e.g., `DepartmentEntity.java`)
    * **Controller:** `[Name]Controller.java`
    * **Repository:** `[Name]Repository.java`

### 2. Module Splitting (Separation of Concerns)
* **Performance Module:**
    * Split the ambiguous `Performance` module into two distinct entities to match the database schema:
        1.  **`PerformanceEvaluation`**: The master record (Parent) containing the period and final rating.
        2.  **`PerformanceFeedback`**: The individual reviews (Child) linked to the master record.

### 3. Database Relationship Mapping
* **JPA Annotations:** Updated all entities to use proper JPA mappings (`@OneToOne`, `@OneToMany`, `@ManyToOne`) instead of storing loose IDs.
    * *Department <-> DepartmentHead:* Implemented circular dependency handling.
    * *Project -> Client:* Added foreign key relationships.
* **Schema Script:** Created `db_schema.sql` to automate table creation and data seeding in the correct dependency order.

### 4. API Standardization
* **Endpoints:** Standardized URL paths (e.g., `/employees`, `/departments`, `/performance-feedback`).
* **Documentation:** Integrated Swagger/OpenAPI for all controllers.

## Future Implementation & Known Limitations
The following features are currently **pending** and will be implemented once all core entities are fully established and verified:

1.  **Global Exception Handling:** * Currently, controllers handle basic exceptions locally. A `@ControllerAdvice` global handler will be added later to standardize error responses (e.g., 404 Not Found, 500 Internal Error) across the entire application.
2.  **Centralized Logging (AOP):**
    * While basic logging exists in controllers, a centralized Aspect-Oriented Programming (AOP) approach for logging request/response cycles is planned for a future update.

---

## Recent Addition: Performance Review Entity

### What was added:
* **`PerformanceReviewEntity.java`**: New entity with a self-referencing foreign key (`previousreview_id` links to another `performance_review` row).
* **`PerformanceReviewRepository.java`**: Standard JPA repository.
* **`PerformanceReviewController.java`**: Full CRUD at `/api/performance-reviews`.
* **`EmployeeEntity.java`**: Updated with a `@OneToMany` list of `performanceReviews`.
* **`db_schema.sql` / `db_schema.txt` / `queries.txt`**: Updated to 23 tables with CREATE TABLE, seed data, and analytical queries.
* **Angular `admin-dashboard.component.ts`**: Wired the new section with dropdowns for Employee and Previous Review selection.