package be.abdullah.universitysystemspringboot.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentDto extends ProfileDto{
    private String studentNumber;
    private LocalDate birthdate;
}
