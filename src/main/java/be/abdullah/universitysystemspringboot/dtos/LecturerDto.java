package be.abdullah.universitysystemspringboot.dtos;

import lombok.Data;

@Data
public class LecturerDto {
    private Integer id;
    private String email;
    private String name;
    private String lastname;
}
