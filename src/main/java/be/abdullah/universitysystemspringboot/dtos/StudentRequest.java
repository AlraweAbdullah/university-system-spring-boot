package be.abdullah.universitysystemspringboot.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public abstract class StudentRequest {
    @NotBlank(message = "Email can't be empty")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Name can't be empty")
    private String name;

    @NotBlank(message = "Lastname can't be empty")
    private String lastname;

    @NotBlank(message = "Student number can't be empty")
    private String studentNumber;

    @NotNull(message = "Birthdate can't be empty")
    @Past(message = "Birthdate must be in the past")
    private LocalDate birthdate;
}
