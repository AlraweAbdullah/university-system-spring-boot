package be.abdullah.universitysystemspringboot.dtos;

import be.abdullah.universitysystemspringboot.entities.Role;
import lombok.Data;

@Data
public class ProfileDto {
    private Integer id;
    private String email;
    private String name;
    private String lastname;
    private Role role;
}
