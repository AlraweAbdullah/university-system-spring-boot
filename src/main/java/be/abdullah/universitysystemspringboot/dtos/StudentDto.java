package be.abdullah.universitysystemspringboot.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentDto {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String studentNumber;
    private LocalDate birthdate;
}
