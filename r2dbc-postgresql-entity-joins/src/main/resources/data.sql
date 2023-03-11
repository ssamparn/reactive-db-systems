DELETE FROM departments;
DELETE FROM employees;


INSERT INTO departments(name)
VALUES ('Software Development'),
       ('HR');

INSERT INTO employees(first_name, last_name, position, is_full_time)
VALUES ('Bob', 'Steeves', 'Director of Software Development', true),
       ('Neil', 'White', 'Software Developer', true),
       ('Joanna', 'Bernier', 'Software Tester', false),
       ('Cathy', 'Ouellette', 'Director of Human Resources', true),
       ('Alysha', 'Rogers', 'Intraday Analyst', true);