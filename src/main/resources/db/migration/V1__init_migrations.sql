CREATE TABLE profiles
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY  NOT NULL,
    name       VARCHAR(255)                       NOT NULL,
    lastname   VARCHAR(255)                       NOT NULL,
    email      VARCHAR(255)                       NOT NULL UNIQUE,
    password   VARCHAR(255)                       NOT NULL,
    role       ENUM ('STUDENT', 'LECTURER', 'ADMIN')       NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE students
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    profile_id        BIGINT                            NOT NULL,
    student_number VARCHAR(255)                      NOT NULL UNIQUE,
    birthdate      DATE                              NOT NULL,
    CONSTRAINT fk_student_profile FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE
);

CREATE TABLE lecturers
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    profile_id    BIGINT                            NOT NULL,
    CONSTRAINT fk_lecturer_profile FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE
);

CREATE TABLE admins
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    profile_id    BIGINT                            NOT NULL,
    CONSTRAINT fk_admin_profile FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE
);

CREATE TABLE courses
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name         VARCHAR(255)                      NOT NULL,
    study_points INT                               NOT NULL,
    lecturer_id  BIGINT                            NOT NULL,
    CONSTRAINT fk_course_lecturer FOREIGN KEY (lecturer_id) REFERENCES lecturers (id) ON DELETE CASCADE
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
