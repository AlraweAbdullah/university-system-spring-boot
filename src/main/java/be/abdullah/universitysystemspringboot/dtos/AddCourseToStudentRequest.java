package be.abdullah.universitysystemspringboot.dtos;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddCourseToStudentRequest {
    @NotNull(message = "Course id must not be empty")
    @Positive(message = "Course id must be a positive number")
    private Long courseId;
}
