DROP TABLE IF EXISTS course_work;

CREATE TABLE IF NOT EXISTS course (
    course_id INT GENERATED ALWAYS AS IDENTITY,
    course_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (course_id)
);

CREATE TABLE IF NOT EXISTS student (
    student_id INT GENERATED ALWAYS AS IDENTITY,
    student_name VARCHAR(255) NOT NULL,
    registered_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status VARCHAR(11) NOT NULL,
    PRIMARY KEY (student_id)
);

CREATE TABLE IF NOT EXISTS course_work (
    course_work_id INT GENERATED ALWAYS AS IDENTITY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    marks INT NOT NULL,
    PRIMARY KEY (course_work_id),
    CONSTRAINT const_on_course_id FOREIGN KEY (course_id) REFERENCES course(course_id),
    CONSTRAINT const_on_student_id FOREIGN KEY (student_id) REFERENCES student(student_id)
);
