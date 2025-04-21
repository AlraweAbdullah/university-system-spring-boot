package be.abdullah.universitysystemspringboot.dtos;


import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterStudentRequest extends StudentRequest{
    @Size(min = 6, max = 25, message = "Password must be between 6 and 25 characters")
    private String password;
}
