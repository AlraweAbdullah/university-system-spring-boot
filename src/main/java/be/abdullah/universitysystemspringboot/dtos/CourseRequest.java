package be.abdullah.universitysystemspringboot.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseRequest {
    @Positive(message = "Study points must be a positive number")
    @NotNull(message = "Study points can't be empty")
    private Integer studyPoints;

    @NotBlank(message = "Course name can't be empty")
    @Size(min = 2, max = 25, message = "Course name must be between 2 and 25 characters" )
    private String name;

    @NotNull(message = "Lecturer id can't be empty")
    @Positive(message = "Lecturer id must be a positive number")
    private Long lecturerId;
}
