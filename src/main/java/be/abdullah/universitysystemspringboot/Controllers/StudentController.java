package be.abdullah.universitysystemspringboot.Controllers;

import be.abdullah.universitysystemspringboot.dtos.StudentDto;
import be.abdullah.universitysystemspringboot.mapper.StudentMapper;
import be.abdullah.universitysystemspringboot.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final StudentMapper studentMapper;
    private StudentRepository studentRepository;

    @GetMapping
    public List<StudentDto> getAllStudents() {
        var students = studentRepository.findAll();
        return students.stream().map(studentMapper::toDto).toList();
    }
}
