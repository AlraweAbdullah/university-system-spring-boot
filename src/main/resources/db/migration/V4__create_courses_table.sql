CREATE TABLE courses
(
    id           BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(255) NOT NULL ,
    study_points INT          NOT NULL ,
    lecturer_id  BIGINT       NOT NULL ,
    CONSTRAINT courses_lecturers_id_fk
        FOREIGN KEY  (lecturer_id) REFERENCES lecturers (id)
);

