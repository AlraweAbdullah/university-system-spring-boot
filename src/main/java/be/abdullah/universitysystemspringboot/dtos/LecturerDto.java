package be.abdullah.universitysystemspringboot.dtos;

import lombok.Data;

@Data
public class LecturerDto {
    private Long id;
    private String name;
    private String lastname;
    private String email;
}
