CREATE TABLE IF NOT EXISTS departments(
    id           BIGSERIAL,
    name         VARCHAR(255) UNIQUE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS employees(
    id           BIGSERIAL,
    first_name   VARCHAR(255) NOT NULL UNIQUE,
    last_name    VARCHAR(255) NOT NULL UNIQUE,
    position     VARCHAR(255) NOT NULL,
    is_full_time BOOLEAN      NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS department_employees(
    department_id BIGSERIAL REFERENCES departments (id),
    employee_id   BIGSERIAL UNIQUE REFERENCES employees(id),
    employee_first_name VARCHAR(255) REFERENCES employees(first_name),
    PRIMARY KEY (department_id, employee_id)
);

CREATE TABLE IF NOT EXISTS department_managers(
    department_id BIGSERIAL UNIQUE REFERENCES departments (id),
    employee_id   BIGSERIAL UNIQUE REFERENCES employees (id),

    PRIMARY KEY(department_id, employee_id)
);
