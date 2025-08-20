CREATE TABLE IF NOT EXISTS departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    CONSTRAINT chk_department_name CHECK (name IN ('BACK', 'FRONT', 'TEST', 'DEVOPS', 'ANALYZE', 'SRE'))
);

CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    position VARCHAR(50) NOT NULL,
    salary DECIMAL(14,2) NOT NULL,
    department_id BIGINT NOT NULL,
    CONSTRAINT chk_position CHECK (position IN ('SNITCH', 'CANDYBOBER', 'INTERN', 'DEVELOPER', 'LEAD', 'ARCHITECT')),
    CONSTRAINT chk_salary_positive CHECK (salary >= 0),
    CONSTRAINT fk_employee_department FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);