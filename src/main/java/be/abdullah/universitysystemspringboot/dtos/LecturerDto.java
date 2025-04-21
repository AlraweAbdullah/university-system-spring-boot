package be.abdullah.universitysystemspringboot.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LecturerDto {
    private Long id;
    private String email;
    private String name;
    private String lastname;
}
