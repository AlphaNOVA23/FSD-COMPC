# ☕ Java FSD Backend Study Guide

This guide breaks down the core architecture, security, and the modifications made to the backend to support the Angular frontend.

---

## 🏗️ 1. Core Architecture Pattern
The backend follows a **Controller-Repository-Entity** pattern. 

- **Entities (`com.FSD.Entity`)**: Java objects that map to database tables.
- **Repositories (`com.FSD.Repository`)**: Interfaces extending `JpaRepository` that provide automatic CRUD operations.
- **Controllers (`com.FSD.Controller`)**: REST endpoints that handle HTTP requests (`GET`, `POST`, `PATCH`, `DELETE`).

---

## 🔒 2. Authentication & Security (JWT)
The system uses **JSON Web Tokens (JWT)** for secure, stateless communication.

### Key Components:
- **`JwtUtil.java`**: Responsible for generating tokens (with custom claims like `role` and `employeeId`) and validating them against the signing key.
- **`SecurityConfig.java`**: 
    - Disables **CSRF** (as we use JWTs which are immune to CSRF).
    - Configures **CORS** to allow traffic from `http://localhost:4200` (the Angular dev server).
    - Defines which routes are public (e.g., `/api/auth/**`) and which require authentication.
- **`JwtAuthenticationFilter.java`**: A filter that runs on every request. It extracts the `Authorization` header, validates the "Bearer" token, and sets the user context in Spring Security's holder.
- **`CustomUserDetailsService.java`**: Loads user credentials from the database for the authentication manager.
- **Password Security**: Uses `BCryptPasswordEncoder` to hash passwords before they enter the database.

---

## 🔄 3. Frontend Integration Fixes

### A. Cascading Logic (`CascadeType.ALL`)
Standard JPA requires you to save a child entity before linking it to a parent. To allow the Angular frontend to send a single "Create Employee" request with nested data (like Salary and Position), we added:
```java
@OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
private SalaryEntity salary;
```
**Why?** This tells Hibernate that if an Employee is saved/updated, it should "cascade" that action to the Salary table automatically.

### B. Bidirectional Wiring (In Controllers)
In many-to-many or one-to-one relationships, Hibernate often needs the "inverse" side to be explicitly set to maintain foreign key integrity. 
In `DepartmentController.java`, we implemented:
```java
if (details.getDepartmentHead() != null) {
    details.getDepartmentHead().setDepartment(department); // Bidirectional link
    department.setDepartmentHead(details.getDepartmentHead());
}
```
This ensures that when you update a Department's head via the UI, the foreign key column in the `department_head` table is updated correctly.

### C. Recursion Prevention
To prevent the "Infinite Recursion" error (where Department links to Employee which links back to Department), we used:
- **`@JsonIgnoreProperties`**: Globally ignores fields during serialization to prevent loops.
- **LAZY Loading**: Relationships are loaded only when needed (`FetchType.LAZY`).

---

## 🚦 4. Exception Handling
The **`GlobalExceptionHandler.java`** captures system errors and returns a clean, standardized JSON response back to the frontend, preventing raw stack traces from leaking to the user.

---

## 📊 5. Performance Review Entity (Self-Referencing Relationship)
The `PerformanceReviewEntity` introduces a unique pattern not seen in the other entities: a **self-referencing foreign key**.

### Schema:
| Column | Type | Description |
|---|---|---|
| `review_id` | PK | Auto-generated primary key |
| `employee_id` | FK to Employee | The employee being reviewed |
| `review_date` | Date | When the review took place |
| `previousreview_id` | FK to self | Links to a prior review for the same employee |
| `attendanceid` | Integer | Standard attendance score |
| `scorechange` | Integer | Change in score compared to the previous review |

### Self-Referencing Logic:
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "previousreview_id")
@JsonIgnoreProperties({"previousReview", "employee"})
private PerformanceReviewEntity previousReview;
```
This allows chaining reviews together (Review 4 links back to Review 1 for the same employee), enabling historical tracking. `@JsonIgnoreProperties` on `previousReview` prevents infinite recursion during serialization, since the previous review itself could also have a previous review.
