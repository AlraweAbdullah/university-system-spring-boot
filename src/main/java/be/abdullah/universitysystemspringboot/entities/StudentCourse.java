package be.abdullah.universitysystemspringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "student_courses")
public class StudentCourse {
    @EmbeddedId
    private StudentCourseId id;

    @MapsId("studentId")
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;


    @MapsId("courseId")
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

}