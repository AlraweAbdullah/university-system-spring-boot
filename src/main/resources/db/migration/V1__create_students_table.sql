CREATE TABLE students
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    email          VARCHAR(255) NOT NULL,
    password       VARCHAR(255) NOT NULL,
    name           VARCHAR(255) NOT NULL,
    last_name      VARCHAR(255) NOT NULL,
    student_number VARCHAR(255) NOT NULL,
    birth_date     DATE NOT NULL
);