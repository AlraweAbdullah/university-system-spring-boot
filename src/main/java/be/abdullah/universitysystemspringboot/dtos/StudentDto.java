package be.abdullah.universitysystemspringboot.dtos;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentDto {
    private Long id;
    private String email;
    private String name;
    private String lastName;
    private String studentNumber;
    private LocalDate birthDate;
}
