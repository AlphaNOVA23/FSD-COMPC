-- =============================================
-- HR MANAGEMENT SYSTEM — FULL SCHEMA & SEED DATA
-- 22 Tables, 10 Employees, Complete Relationships
-- =============================================

-- ==================
-- 1. DROP ALL TABLES
-- ==================
DROP TABLE IF EXISTS training_feedback CASCADE;
DROP TABLE IF EXISTS employee_training CASCADE;
DROP TABLE IF EXISTS training_program CASCADE;
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
DROP TABLE IF EXISTS leaves CASCADE;
DROP TABLE IF EXISTS leave_type CASCADE;
DROP TABLE IF EXISTS department_head CASCADE;
DROP TABLE IF EXISTS employee CASCADE;
DROP TABLE IF EXISTS department CASCADE;

-- ========================
-- 2. CREATE ALL 22 TABLES
-- ========================

-- CORE
CREATE TABLE department (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL,
    department_location VARCHAR(255),
    department_capacity INTEGER,
    department_contact VARCHAR(255)
);

CREATE TABLE employee (
    employee_id SERIAL PRIMARY KEY,
    employee_name VARCHAR(100) NOT NULL,
    department_id INTEGER REFERENCES department(department_id) ON DELETE SET NULL
);

CREATE TABLE department_head (
    head_id SERIAL PRIMARY KEY,
    employee_id INTEGER UNIQUE REFERENCES employee(employee_id),
    head_role VARCHAR(100),
    head_term VARCHAR(50),
    department_id INTEGER UNIQUE REFERENCES department(department_id)
);

-- LEAVE
CREATE TABLE leave_type (
    leave_type_id SERIAL PRIMARY KEY,
    type_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE leaves (
    leave_id SERIAL PRIMARY KEY,
    employee_id INTEGER REFERENCES employee(employee_id) ON DELETE CASCADE,
    leave_type_id INTEGER REFERENCES leave_type(leave_type_id),
    leave_count INTEGER NOT NULL,
    is_approved BOOLEAN DEFAULT FALSE
);

-- PAYROLL
CREATE TABLE salaries (
    salary_id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL UNIQUE REFERENCES employee(employee_id) ON DELETE CASCADE,
    base_salary DOUBLE PRECISION,
    bonus DOUBLE PRECISION,
    deductions DOUBLE PRECISION,
    net_salary DOUBLE PRECISION
);

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

-- OPERATIONS
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
    employee_id INTEGER REFERENCES employee(employee_id)
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

-- SECURITY
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
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER'
);

CREATE TABLE account_activity (
    activity_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES credential(user_id),
    status VARCHAR(50) NOT NULL,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PERFORMANCE
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

-- TRAINING
CREATE TABLE training_program (
    training_id SERIAL PRIMARY KEY,
    program_name VARCHAR(255) NOT NULL,
    trainer VARCHAR(255),
    program_date DATE,
    duration_hours VARCHAR(50)
);

CREATE TABLE employee_training (
    employee_training_id SERIAL PRIMARY KEY,
    training_id INTEGER NOT NULL REFERENCES training_program(training_id) ON DELETE CASCADE,
    employee_id INTEGER NOT NULL REFERENCES employee(employee_id) ON DELETE CASCADE,
    grade VARCHAR(50),
    status VARCHAR(50)
);

CREATE TABLE training_feedback (
    feedback_id SERIAL PRIMARY KEY,
    training_id INTEGER NOT NULL REFERENCES training_program(training_id) ON DELETE CASCADE,
    feedback_text TEXT,
    rating INTEGER,
    suggestion TEXT
);

-- ============================
-- 3. SEED DATA — 10 EMPLOYEES
-- ============================

-- 3.1 DEPARTMENTS (5)
INSERT INTO department (department_name, department_location, department_capacity, department_contact) VALUES
('Human Resources', 'Building A, Floor 1', 15, 'hr@company.com'),
('Engineering', 'Building B, Floor 3', 60, 'engineering@company.com'),
('Marketing', 'Building A, Floor 2', 20, 'marketing@company.com'),
('Sales', 'Building C, Floor 1', 30, 'sales@company.com'),
('Finance', 'Building C, Floor 2', 15, 'finance@company.com');

-- 3.2 EMPLOYEES (10)
INSERT INTO employee (employee_name, department_id) VALUES
('Alice Johnson', 1),
('Bob Williams', 2),
('Charlie Brown', 1),
('Diana Prince', 2),
('Edward Norton', 3),
('Fiona Gallagher', 3),
('George Clooney', 4),
('Hannah Abbott', 4),
('Ian Wright', 5),
('Jane Doe', 5);

-- 3.3 DEPARTMENT HEADS (5)
INSERT INTO department_head (employee_id, head_role, head_term, department_id) VALUES
(1, 'HR Director', '2024-2026', 1),
(2, 'Chief Technology Officer', '2023-2025', 2),
(5, 'Marketing Manager', '2024-2025', 3),
(7, 'Sales Lead', '2023-2024', 4),
(9, 'Finance Controller', '2024-2026', 5);

-- 3.4 LEAVE TYPES (3)
INSERT INTO leave_type (type_name) VALUES ('Sick'), ('Casual'), ('Earned');

-- 3.5 LEAVES (6)
INSERT INTO leaves (employee_id, leave_type_id, leave_count, is_approved) VALUES
(1, 1, 2, TRUE),
(2, 2, 5, FALSE),
(3, 3, 10, TRUE),
(4, 1, 3, TRUE),
(7, 2, 1, FALSE),
(10, 3, 7, TRUE);

-- 3.6 SALARIES (10 — one per employee)
INSERT INTO salaries (employee_id, base_salary, bonus, deductions, net_salary) VALUES
(1, 75000, 5000, 2000, 78000),
(2, 120000, 15000, 5000, 130000),
(3, 60000, 2000, 1500, 60500),
(4, 95000, 8000, 3000, 100000),
(5, 85000, 6000, 2500, 88500),
(6, 70000, 4000, 2000, 72000),
(7, 110000, 12000, 4500, 117500),
(8, 65000, 3000, 1800, 66200),
(9, 105000, 10000, 4000, 111000),
(10, 55000, 1000, 1200, 54800);

-- 3.7 POSITION DETAILS (10 — one per employee)
INSERT INTO position_details (employee_id, title, job_level, salary_grade, base_salary, bonus_eligible, currency, employment_type, effective_date) VALUES
(1, 'HR Director', 'Senior', 'G12', 75000, TRUE, 'USD', 'Full-time', '2024-01-01'),
(2, 'Chief Technology Officer', 'Executive', 'G15', 120000, TRUE, 'USD', 'Full-time', '2023-01-01'),
(3, 'HR Coordinator', 'Junior', 'G06', 60000, FALSE, 'USD', 'Full-time', '2024-06-01'),
(4, 'Senior Developer', 'Senior', 'G11', 95000, TRUE, 'USD', 'Full-time', '2023-03-15'),
(5, 'Marketing Manager', 'Mid-Level', 'G10', 85000, TRUE, 'USD', 'Full-time', '2024-02-10'),
(6, 'Content Writer', 'Junior', 'G07', 70000, FALSE, 'USD', 'Contract', '2024-05-01'),
(7, 'Sales Lead', 'Senior', 'G12', 110000, TRUE, 'USD', 'Full-time', '2023-11-20'),
(8, 'Sales Associate', 'Junior', 'G06', 65000, FALSE, 'USD', 'Full-time', '2024-03-01'),
(9, 'Finance Controller', 'Senior', 'G13', 105000, TRUE, 'USD', 'Full-time', '2024-01-15'),
(10, 'Accountant', 'Junior', 'G08', 55000, FALSE, 'USD', 'Full-time', '2024-07-01');

-- 3.8 SECURITY — CREDENTIALS (10)
INSERT INTO credential (employee_id) VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10);

-- 3.9 SECURITY — ACCOUNT INFO (10)
INSERT INTO account_info (user_id, email, role) VALUES
(1, 'alice@company.com', 'HR Director'),
(2, 'bob@company.com', 'Chief Technology Officer'),
(3, 'charlie@company.com', 'HR Coordinator'),
(4, 'diana@company.com', 'Senior Developer'),
(5, 'edward@company.com', 'Marketing Manager'),
(6, 'fiona@company.com', 'Content Writer'),
(7, 'george@company.com', 'Sales Lead'),
(8, 'hannah@company.com', 'Sales Associate'),
(9, 'ian@company.com', 'Finance Controller'),
(10, 'jane@company.com', 'Accountant');

-- 3.10 SECURITY — LOGIN DETAILS (10)
-- ADMIN: alice@company.com / admin123
-- ALL OTHERS: <email> / password123
-- NOTE: PasswordHashSeeder auto-BCrypt-encodes these on Spring Boot startup
INSERT INTO login_details (user_id, username, password_hash, role) VALUES
(1, 'alice@company.com', 'admin123', 'ROLE_ADMIN'),
(2, 'bob@company.com', 'password123', 'ROLE_USER'),
(3, 'charlie@company.com', 'password123', 'ROLE_USER'),
(4, 'diana@company.com', 'password123', 'ROLE_USER'),
(5, 'edward@company.com', 'password123', 'ROLE_USER'),
(6, 'fiona@company.com', 'password123', 'ROLE_USER'),
(7, 'george@company.com', 'password123', 'ROLE_USER'),
(8, 'hannah@company.com', 'password123', 'ROLE_USER'),
(9, 'ian@company.com', 'password123', 'ROLE_USER'),
(10, 'jane@company.com', 'password123', 'ROLE_USER');

-- 3.11 CLIENTS (5)
INSERT INTO client (client_name, organization, budget, client_type, status) VALUES
('TechCorp International', 'TCI Holdings', 500000, 'Enterprise', 'Active'),
('GlobalFix Solutions', 'GlobalFix Ltd', 250000, 'Mid-Market', 'Active'),
('BioX Research', 'BioX Labs', 100000, 'Startup', 'Active'),
('EduLink Foundation', 'EduLink NGO', 50000, 'Non-Profit', 'Active'),
('MediaX Studios', 'MediaX Inc', 700000, 'Enterprise', 'Active');

-- 3.12 PROJECTS (5)
INSERT INTO project (project_name, project_duration, client_id, department_id, employee_id) VALUES
('Cloud Infrastructure Migration', '12 Months', 1, 2, 2),
('Brand Redesign Campaign', '3 Months', 1, 3, 5),
('Security Audit & Compliance', '6 Months', 2, 2, 4),
('Annual Financial Audit', '2 Months', 5, 5, 9),
('Sales Pipeline Automation', '8 Months', 3, 4, 7);

-- 3.13 RESPONSIBILITIES (5)
INSERT INTO responsibility (project_id, responsibility_type, clearance_level, start_date, end_date, employee_id, created_by) VALUES
(1, 'Technical Lead', 'High', '2024-01-15', '2024-12-31', 2, 1),
(2, 'Campaign Director', 'Medium', '2024-03-01', '2024-06-01', 5, 1),
(3, 'Security Analyst', 'Critical', '2024-02-01', '2024-08-01', 4, 2),
(4, 'Audit Manager', 'High', '2024-09-01', '2024-11-01', 9, 1),
(5, 'Sales Architect', 'Medium', '2024-04-01', '2024-12-01', 7, 2);

-- 3.14 TASKS (10)
INSERT INTO task (responsibility_id, task_name, priority, status, deadline, assigned_to) VALUES
(1, 'Set up AWS infrastructure', 'High', 'Completed', '2024-03-01', 2),
(1, 'Migrate database cluster', 'Critical', 'In Progress', '2024-06-15', 4),
(2, 'Design new brand guidelines', 'High', 'Completed', '2024-04-01', 5),
(2, 'Launch social media campaign', 'Medium', 'Pending', '2024-05-15', 6),
(3, 'Penetration testing', 'Critical', 'In Progress', '2024-04-30', 4),
(3, 'Write compliance report', 'High', 'Pending', '2024-07-01', 4),
(4, 'Review Q3 financials', 'High', 'Completed', '2024-10-01', 9),
(4, 'Prepare audit summary', 'Medium', 'In Progress', '2024-10-15', 10),
(5, 'Map current sales pipeline', 'Medium', 'Completed', '2024-05-01', 7),
(5, 'Implement CRM integration', 'High', 'Pending', '2024-09-01', 8);

-- 3.15 SHIFTS (3)
INSERT INTO shift (shift_type, start_time, end_time, break_duration) VALUES
('Morning', '08:30:00', '17:30:00', 60),
('Afternoon', '13:00:00', '21:00:00', 45),
('Night', '22:00:00', '06:00:00', 60);

-- 3.16 TIMESHEETS (5)
INSERT INTO timesheet (employee_id, shift_id, work_date, check_in, check_out, total_hours, overtime_hours, status, remarks) VALUES
(1, 1, '2025-10-20', '2025-10-20 08:28:00', '2025-10-20 17:35:00', 8.12, 0.00, 'Approved', NULL),
(2, 1, '2025-10-20', '2025-10-20 08:30:00', '2025-10-20 19:00:00', 9.50, 1.50, 'Approved', 'Sprint deadline'),
(4, 1, '2025-10-20', '2025-10-20 08:45:00', '2025-10-20 17:30:00', 7.75, 0.00, 'Approved', NULL),
(7, 2, '2025-10-20', '2025-10-20 13:05:00', '2025-10-20 21:10:00', 8.08, 0.00, 'Approved', NULL),
(9, 1, '2025-10-21', '2025-10-21 08:30:00', '2025-10-21 18:30:00', 9.00, 1.00, 'Pending', 'Quarter close');

-- 3.17 PERFORMANCE EVALUATIONS (10 — one per employee)
INSERT INTO performance_evaluation (employee_id, evaluation_period, performance_rating, comments) VALUES
(1, 'Q3 2025', 'Exceeds Expectations', 'Led the full HR digital transformation initiative.'),
(2, 'Annual 2024', 'Outstanding', 'Drove the technical vision for the entire engineering org.'),
(3, 'Q3 2025', 'Meets Expectations', 'Solid support role in HR operations.'),
(4, 'Q3 2025', 'Exceeds Expectations', 'Consistently delivers high-quality production code.'),
(5, 'Q3 2025', 'Outstanding', 'Grew marketing engagement by 42% year-over-year.'),
(6, 'Q3 2025', 'Meets Expectations', 'Good content output volume, improving quality.'),
(7, 'Q3 2025', 'Exceeds Expectations', 'Exceeded annual sales quota by 18%.'),
(8, 'Q3 2025', 'Meets Expectations', 'Strong learning curve, building client relationships.'),
(9, 'Q3 2025', 'Outstanding', 'Zero discrepancies in fiscal year audit.'),
(10, 'Q3 2025', 'Meets Expectations', 'Reliable audit support and reconciliation.');

-- 3.18 PERFORMANCE FEEDBACK (6)
INSERT INTO performance_feedback (employee_id, reviewer_id, rating, comments, feedback_date, evaluation_id) VALUES
(1, 2, 4.5, 'Excellent cross-functional collaboration.', '2025-10-25', 1),
(2, 1, 5.0, 'Visionary technical leadership.', '2025-10-25', 2),
(4, 2, 4.8, 'Best developer on the team.', '2025-10-26', 4),
(5, 1, 4.9, 'Outstanding campaign execution.', '2025-10-26', 5),
(7, 9, 4.3, 'Strong closer, reliable performer.', '2025-10-27', 7),
(9, 1, 5.0, 'Exceptional attention to financial detail.', '2025-10-27', 9);

-- 3.19 TRAINING PROGRAMS (5)
INSERT INTO training_program (program_name, trainer, program_date, duration_hours) VALUES
('Advanced Java & Spring Boot', 'John Master', '2024-03-15', '40'),
('HR Conflict Resolution', 'Sarah Smith', '2024-04-10', '15'),
('Agile Project Management', 'Dave Mitchell', '2024-02-20', '24'),
('Cybersecurity Fundamentals', 'Cyber Security Team', '2024-01-10', '10'),
('Leadership & Management 101', 'External Executive Coach', '2024-05-05', '20');

-- 3.20 EMPLOYEE TRAINING ENROLLMENTS (10)
INSERT INTO employee_training (training_id, employee_id, grade, status) VALUES
(1, 2, 'A', 'Completed'),
(1, 4, 'A+', 'Completed'),
(1, 6, 'B', 'In Progress'),
(2, 1, 'A', 'Completed'),
(3, 4, 'B+', 'Completed'),
(3, 7, 'A', 'Completed'),
(4, 1, 'A', 'Completed'),
(4, 2, 'S', 'Completed'),
(5, 5, 'B+', 'In Progress'),
(5, 9, 'A', 'Pending');

-- 3.21 TRAINING FEEDBACK (4)
INSERT INTO training_feedback (training_id, feedback_text, rating, suggestion) VALUES
(1, 'Excellent deep-dive into Spring Boot internals.', 5, 'Add more microservices content.'),
(2, 'Very practical conflict scenarios.', 4, 'Extend to 2-day workshop.'),
(3, 'Good Scrum methodology coverage.', 4, 'Include Kanban comparisons.'),
(4, 'Essential security awareness training.', 5, 'Make mandatory for all new hires.');

-- 3.22 ACCOUNT ACTIVITY (5)
INSERT INTO account_activity (user_id, status, last_login) VALUES
(1, 'Active', '2025-10-25 09:15:00'),
(2, 'Active', '2025-10-25 08:30:00'),
(4, 'Active', '2025-10-24 14:20:00'),
(7, 'Inactive', '2025-09-15 10:00:00'),
(9, 'Active', '2025-10-25 07:45:00');