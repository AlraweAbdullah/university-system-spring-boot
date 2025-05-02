package be.abdullah.universitysystemspringboot.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public abstract class UserRequest {
    @NotBlank(message = "Email can't be empty")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Name can't be empty")
    private String name;

    @NotBlank(message = "Lastname can't be empty")
    private String lastname;
}
