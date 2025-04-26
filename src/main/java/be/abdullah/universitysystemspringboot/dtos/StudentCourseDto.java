package be.abdullah.universitysystemspringboot.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentCourseDto {
    private CourseDto course;
    private LocalDateTime registeredAt;
}
