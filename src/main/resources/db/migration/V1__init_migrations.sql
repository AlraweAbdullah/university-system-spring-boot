CREATE TABLE credentials
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    email    VARCHAR(255)                      NOT NULL,
    password VARCHAR(255)                      NOT NULL
);

CREATE TABLE students
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    credential_id  BIGINT                            NOT NULL,
    student_number VARCHAR(255)                      NOT NULL,
    birthdate      DATE                              NOT NULL,
    name           VARCHAR(255)                      NOT NULL,
    lastname       VARCHAR(255)                      NOT NULL,
    CONSTRAINT fk_student_credential FOREIGN KEY (credential_id) REFERENCES credentials (id) ON DELETE CASCADE
);

CREATE TABLE lecturers
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    credential_id BIGINT                            NOT NULL,
    name          VARCHAR(255)                      NOT NULL,
    lastname      VARCHAR(255)                      NOT NULL,
    CONSTRAINT fk_lecturer_credential FOREIGN KEY (credential_id) REFERENCES credentials (id) ON DELETE CASCADE
);

CREATE TABLE courses
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name         VARCHAR(255)                      NOT NULL,
    study_points INT                               NOT NULL,
    lecturer_id  BIGINT                            NOT NULL,
    CONSTRAINT fk_course_lecturer FOREIGN KEY (lecturer_id) REFERENCES lecturers (id)
);

CREATE TABLE student_courses
(
    student_id BIGINT                             NOT NULL,
    course_id  BIGINT                             NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
);
