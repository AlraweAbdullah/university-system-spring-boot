package be.abdullah.universitysystemspringboot.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email can't be empty")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password can't be empty")
    @Size(min = 6, max = 25, message = "Password must be between 6 and 25 characters")
    private String password;
}
