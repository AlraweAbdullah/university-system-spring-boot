package be.abdullah.universitysystemspringboot.dtos;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterStudentRequest extends StudentRequest{
    @Size(min = 6, max = 25, message = "Password must be between 6 and 25 characters")
    private String password;
}
