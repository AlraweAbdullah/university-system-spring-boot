package be.abdullah.universitysystemspringboot.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentDto{
    private Integer id;
    private String email;
    private String name;
    private String lastname;
    private String studentNumber;
    private LocalDate birthdate;
}
