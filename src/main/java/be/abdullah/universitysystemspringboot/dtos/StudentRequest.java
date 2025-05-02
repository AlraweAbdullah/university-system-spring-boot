package be.abdullah.universitysystemspringboot.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public abstract class StudentRequest extends ProfileRequest {
    @NotBlank(message = "Student number can't be empty")
    private String studentNumber;

    @NotNull(message = "Birthdate can't be empty")
    @Past(message = "Birthdate must be in the past")
    private LocalDate birthdate;
}
