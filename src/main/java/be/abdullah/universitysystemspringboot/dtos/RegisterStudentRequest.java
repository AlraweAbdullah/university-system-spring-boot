package be.abdullah.universitysystemspringboot.dtos;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterStudentRequest {
    @NotBlank(message = "Email can't be empty")
    @Email(message = "Email is not valid")
    private String email;

    @Size(min = 6, max = 25, message = "Password must be between 6 and 25 characters")
    private String password;

    @NotBlank(message = "Name can't be empty")
    private String name;

    @NotBlank(message = "Lastname can't be empty")
    private String lastName;

    @NotBlank(message = "Student number can't be empty")
    private String studentNumber;

    @NotNull(message = "Birthdate can't be empty")
    @Past(message = "Birthdate must be in the past")
    private LocalDate birthDate;
}
