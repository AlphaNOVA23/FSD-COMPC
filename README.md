# Employee Management System (EMS) Backend

## Overview

This is a robust, modular REST API built with **Java Spring Boot** and **PostgreSQL**. It manages the complete lifecycle of an organization's workforce, including departmental structures, project allocations, and performance reviews.

---

## 🛠 Tech Stack

- **Language:** Java 17+
- **Framework:** Spring Boot 3.x
- **Database:** PostgreSQL
- **ORM:** Hibernate / Spring Data JPA
- **Documentation:** Swagger UI / OpenAPI 3
- **Testing:** Python (Automated API verification)

---

## 📂 Project Modules

### 1. Core Module (Organization Structure)

- **Employee:** The root entity. Handles personal info, relationships to salary, and positions.
- **Department:** Manages internal teams (IT, HR, Sales).
- **Department Head:** Tracks leadership roles (Circular dependency handled via `DepartmentHeadEntity`).

### 2. Work Allocation Module

- **Client:** External organizations requiring work.
- **Project:** Specific initiatives linked to a Client and a Lead Employee.
- **Responsibility:** Granular tasks assigned to employees with specific **Clearance Levels** and **Types** (Development, Management, etc.).
- **Task:** Individual work items linked to responsibilities.

### 3. Performance Module

- **Performance Evaluation:** Annual/Quarterly master records of employee performance.
- **Performance Feedback:** Peer-to-peer or Manager-to-Employee specific feedback records.

### 4. Time Tracking Module

- **Shift:** Defines work schedules (Morning, Evening, Night shifts).
- **Timesheet:** Records employee clock-in/out times, overtime, and attendance status.

### 5. Leave Management Module [NEW]

- **Leave Type:** Defines categories like "Sick Leave", "Casual Leave", etc.
- **Leaves:** Handles the application process. Links an **Employee** to a **Leave Type** and tracks approval status (`is_approved`).

### 6. Security Module

- **Credential:** User authentication foundation.
- **Account Info:** Email and role management.
- **Login Details:** Username and password storage.
- **Account Activity:** Tracks login history and account status.

---

## 🗄️ Database Schema

### Tables Overview

**Core Tables:**
- `employee` - Base employee information
- `department` - Organizational units
- `department_head` - Leadership tracking (handles circular dependency)
- `salaries` - Compensation details
- `position_details` - Job titles, levels, and employment types

**Work Management:**
- `client` - External clients/organizations
- `project` - Client projects with duration and budget
- `responsibility` - Employee assignments with clearance levels
- `task` - Granular work items

**Time Tracking:**
- `shift` - Shift definitions
- `timesheet` - Daily attendance records

**Leave Management:**
- `leave_type` - Leave category definitions (e.g., Sick, Casual)
- `leaves` - Employee leave requests and approval status tracking

**Security:**
- `credential` - User authentication base
- `account_info` - Account metadata
- `login_details` - Login credentials
- `account_activity` - Activity logs

**Performance:**
- `performance_evaluation` - Evaluation records
- `performance_feedback` - Peer/manager feedback

---

## 🚀 Setup & Installation

### 1. Database Setup

#### Step 1: Create the Database

1. Open **DBeaver** (or pgAdmin)
2. Right-click on your PostgreSQL connection
3. Select **Create > Database**
4. Name it: `employee_management`
5. Click **OK**

#### Step 2: Execute the Schema Script

**Method 1: Using SQL Editor (Recommended)**

1. In DBeaver, expand your PostgreSQL connection
2. Right-click on `employee_management` database
3. Select **SQL Editor > New SQL Script**
4. Copy and paste the entire content from `db_schema.sql`
5. **Important:** Select all text (Ctrl+A)
6. Click **Execute SQL Statement** (Ctrl+Enter) or press **Alt+X**
7. Check the **Output** tab at the bottom for success messages

**Method 2: Execute SQL Script**

1. Right-click on `employee_management` database
2. Select **Execute SQL Script...**
3. Browse and select your `db_schema.sql` file
4. Click **Start**

#### Step 3: Verify Tables

1. In the Database Navigator, expand `employee_management`
2. Right-click on **Tables** folder
3. Select **Refresh** (F5)
4. You should see 20 tables:
   - employee
   - department
   - department_head
   - salaries
   - position_details
   - client
   - project
   - responsibility
   - task
   - shift
   - leave_type
   - leaves
   - timesheet
   - credential
   - account_info
   - login_details
   - account_activity
   - performance_evaluation
   - performance_feedback

#### Troubleshooting Database Setup

**If tables don't appear:**

1. **Check for errors in the Output tab:**
   - Look for red error messages
   - Common issues: database not selected, permission errors

2. **Ensure you're connected to the correct database:**
   - The active database should show `employee_management` in the connection dropdown

3. **Try manual execution:**
   - Execute the script in smaller sections
   - Start with the DROP statements
   - Then CREATE TABLE statements
   - Finally INSERT statements

4. **Verify PostgreSQL connection:**
   ```sql
   SELECT current_database();
   ```
   This should return `employee_management`

5. **Check table creation:**
   ```sql
   SELECT table_name 
   FROM information_schema.tables 
   WHERE table_schema = 'public';
   ```

**If you see "NOTICE: drop cascades to..." messages:**
- These are normal cleanup operations when re-running the script
- They indicate existing tables are being dropped before recreation

### 2. Application Configuration

Update your `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/employee_management
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
```

### 3. Running the Application

Run the main class `FsdApplication.java` as a Java Application. The server will start on port `8080`.

---

## 🐍 Python Automation & Testing

This project includes a Python script (e.g., `test_api.py`) located in the root directory. This script automates the verification of API endpoints.

### Prerequisites

- Python 3.x installed
- The `requests` library:

```bash
pip install requests
```

### How to Run

Ensure the Spring Boot app is running, then execute:

```bash
python test_api.py
```

### What it does

1. Connects to `http://localhost:8080`
2. Simulates a "User Flow":
   - Creates a new Department
   - Onboards a new Employee
   - Assigns the Employee to a Project
   - Submits a Performance Review
3. Prints the JSON response for every step to the console for verification

---

## 🔗 API Documentation

Once the application is running, access the interactive API docs here:

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html

---

## ⚠️ Pending Implementation (Future Roadmap)

- **Global Exception Handling:** Currently, controllers handle exceptions locally. A global `@ControllerAdvice` to standardize error responses (404, 500) is planned.
- **Centralized Logging:** AOP-based logging for request/response tracing is yet to be implemented.

---