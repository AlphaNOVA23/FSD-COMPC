-- =============================================
-- 1. CLEANUP (Drop tables in reverse dependency order)
-- =============================================
DROP TABLE IF EXISTS performance_feedback CASCADE;
DROP TABLE IF EXISTS performance_evaluation CASCADE;
DROP TABLE IF EXISTS account_activity CASCADE;
DROP TABLE IF EXISTS login_details CASCADE;
DROP TABLE IF EXISTS account_info CASCADE;
DROP TABLE IF EXISTS credential CASCADE;
DROP TABLE IF EXISTS task CASCADE;
DROP TABLE IF EXISTS responsibility CASCADE;
DROP TABLE IF EXISTS project CASCADE;
DROP TABLE IF EXISTS client CASCADE;
DROP TABLE IF EXISTS position_details CASCADE;
DROP TABLE IF EXISTS timesheet CASCADE;
DROP TABLE IF EXISTS shift CASCADE;
DROP TABLE IF EXISTS salaries CASCADE;
DROP TABLE IF EXISTS department CASCADE;
DROP TABLE IF EXISTS department_head CASCADE;
DROP TABLE IF EXISTS employee CASCADE;

-- =============================================
-- 2. CORE ORGANIZATION STRUCTURE
-- =============================================

-- Employee Table (Base Entity)
CREATE TABLE employee (
    employee_id SERIAL PRIMARY KEY,
    employee_name VARCHAR(100) NOT NULL
);

-- Department Head (Circular dependency - create first without FK)
CREATE TABLE department_head (
    head_id SERIAL PRIMARY KEY,
    employee_id INTEGER UNIQUE REFERENCES employee(employee_id),
    head_role VARCHAR(100),
    head_term VARCHAR(50),
    department_id INTEGER UNIQUE -- FK added later
);

-- Department Table
CREATE TABLE department (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL,
    department_location VARCHAR(255),
    head_id INTEGER REFERENCES department_head(head_id),
    department_capacity INTEGER,
    department_contact VARCHAR(255)
);

-- Establish Circular Link
ALTER TABLE department_head 
ADD CONSTRAINT fk_head_department FOREIGN KEY (department_id) REFERENCES department(department_id);

-- =============================================
-- 3. PAYROLL & POSITION
-- =============================================

-- Salaries Table
CREATE TABLE salaries (
    salary_id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL UNIQUE REFERENCES employee(employee_id) ON DELETE CASCADE,
    base_salary DOUBLE PRECISION,
    bonus DOUBLE PRECISION,
    deductions DOUBLE PRECISION,
    net_salary DOUBLE PRECISION
);

-- Position Details
CREATE TABLE position_details (
    employee_id INTEGER PRIMARY KEY REFERENCES employee(employee_id) ON DELETE CASCADE,
    title VARCHAR(100) NOT NULL,
    job_level VARCHAR(50) NOT NULL,
    salary_grade VARCHAR(50) NOT NULL,
    base_salary NUMERIC(12, 2) NOT NULL,
    bonus_eligible BOOLEAN NOT NULL DEFAULT FALSE,
    currency VARCHAR(3) NOT NULL,
    employment_type VARCHAR(50) NOT NULL,
    effective_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 4. WORK MANAGEMENT (Clients, Projects, Tasks)
-- =============================================

CREATE TABLE client (
    client_id SERIAL PRIMARY KEY,
    client_name VARCHAR(255) NOT NULL,
    organization VARCHAR(255),
    budget NUMERIC(15, 2),
    client_type VARCHAR(50),
    status VARCHAR(50)
);

CREATE TABLE project (
    project_id SERIAL PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    project_duration VARCHAR(50),
    client_id INTEGER REFERENCES client(client_id) ON DELETE SET NULL,
    department_id INTEGER REFERENCES department(department_id),
    employee_id INTEGER REFERENCES employee(employee_id) -- Project Lead
);

CREATE TABLE responsibility (
    responsibility_id SERIAL PRIMARY KEY,
    project_id INTEGER REFERENCES project(project_id) ON DELETE SET NULL,
    responsibility_type VARCHAR(50) NOT NULL,
    clearance_level VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    employee_id INTEGER NOT NULL REFERENCES employee(employee_id) ON DELETE CASCADE,
    created_by INTEGER REFERENCES employee(employee_id),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE task (
    task_id SERIAL PRIMARY KEY,
    responsibility_id INTEGER REFERENCES responsibility(responsibility_id) ON DELETE SET NULL,
    task_name VARCHAR(255) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    deadline DATE,
    assigned_to INTEGER REFERENCES employee(employee_id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 5. TIME TRACKING
-- =============================================

CREATE TABLE shift (
    shift_id SERIAL PRIMARY KEY,
    shift_type VARCHAR(50) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    break_duration INTEGER
);

CREATE TABLE timesheet (
    timesheet_id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL REFERENCES employee(employee_id) ON DELETE CASCADE,
    shift_id INTEGER NOT NULL REFERENCES shift(shift_id),
    work_date DATE NOT NULL,
    check_in TIMESTAMP,
    check_out TIMESTAMP,
    total_hours DECIMAL(5,2),
    overtime_hours DECIMAL(5,2),
    status VARCHAR(50) NOT NULL,
    remarks TEXT
);

-- =============================================
-- 6. SECURITY & ACCESS
-- =============================================

CREATE TABLE credential (
    user_id SERIAL PRIMARY KEY,
    employee_id INTEGER UNIQUE NOT NULL REFERENCES employee(employee_id) ON DELETE CASCADE
);

CREATE TABLE account_info (
    info_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL UNIQUE REFERENCES credential(user_id) ON DELETE CASCADE,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE login_details (
    login_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL UNIQUE REFERENCES credential(user_id) ON DELETE CASCADE,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE account_activity (
    activity_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES credential(user_id),
    status VARCHAR(50) NOT NULL,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 7. PERFORMANCE MANAGEMENT
-- =============================================

CREATE TABLE performance_evaluation (
    evaluation_id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL REFERENCES employee(employee_id) ON DELETE CASCADE,
    evaluation_period VARCHAR(100) NOT NULL,
    performance_rating VARCHAR(50),
    comments TEXT
);

CREATE TABLE performance_feedback (
    feedback_id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL REFERENCES employee(employee_id) ON DELETE CASCADE,
    reviewer_id INTEGER NOT NULL REFERENCES employee(employee_id) ON DELETE CASCADE,
    rating NUMERIC(3, 1) NOT NULL,
    comments TEXT,
    feedback_date DATE NOT NULL,
    evaluation_id INTEGER REFERENCES performance_evaluation(evaluation_id) ON DELETE SET NULL,
    training_id INTEGER
);

-- =============================================
-- 8. DATA INSERTION (SEED DATA)
-- =============================================

-- Employees
INSERT INTO employee (employee_name) VALUES 
('Alice Johnson'), 
('Bob Williams'), 
('Charlie Brown');

-- Department Heads & Departments
INSERT INTO department_head (employee_id, head_role, head_term) VALUES 
(1, 'HR Director', '2024-2026'),
(2, 'Lead Engineer', '2023-2025');

INSERT INTO department (department_name, department_location, head_id, department_capacity, department_contact) VALUES 
('Human Resources', 'Building A', 1, 10, 'contact@hr.com'),
('Engineering', 'Building B', 2, 50, 'tech@eng.com');

UPDATE department_head SET department_id = 1 WHERE head_id = 1;
UPDATE department_head SET department_id = 2 WHERE head_id = 2;

-- Salaries
INSERT INTO salaries (employee_id, base_salary, bonus, deductions, net_salary) VALUES 
(1, 75000, 5000, 2000, 78000),
(2, 90000, 8000, 3000, 95000),
(3, 60000, 2000, 1500, 60500);

-- Clients & Projects
INSERT INTO client (client_name, organization, budget, client_type, status) VALUES
('TechCorp', 'TechCorp International', 500000.00, 'Enterprise', 'Active');

INSERT INTO project (project_name, project_duration, client_id, department_id, employee_id) VALUES
('Cloud Migration', '12 Months', 1, 2, 2);

-- Performance Data
INSERT INTO performance_evaluation (employee_id, evaluation_period, performance_rating, comments) VALUES
(1, 'Q3 2025', 'Exceeds Expectations', 'Led HR migration successfully.');

INSERT INTO performance_feedback (employee_id, reviewer_id, rating, comments, feedback_date, evaluation_id) VALUES 
(1, 2, 4.5, 'Great collaboration on the migration project.', '2025-10-25', 1);

-- Shifts & Time
INSERT INTO shift (shift_type, start_time, end_time, break_duration) VALUES
('Morning', '08:30:00', '17:30:00', 60);

INSERT INTO timesheet (employee_id, shift_id, work_date, check_in, check_out, total_hours, overtime_hours, status, remarks) VALUES
(1, 1, '2025-10-24', '2025-10-24 08:30:00', '2025-10-24 17:30:00', 8.00, 0.00, 'Approved', 'Regular day');