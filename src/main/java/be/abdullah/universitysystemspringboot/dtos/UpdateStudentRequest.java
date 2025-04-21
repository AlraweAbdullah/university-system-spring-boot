package be.abdullah.universitysystemspringboot.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateStudentRequest {
    private String email;
    private String password;
    private String name;
    private String lastname;
    private String studentNumber;
    private LocalDate birthdate;
}
