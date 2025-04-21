package be.abdullah.universitysystemspringboot.Controllers;

import be.abdullah.universitysystemspringboot.dtos.RegisterStudentRequest;
import be.abdullah.universitysystemspringboot.dtos.StudentDto;
import be.abdullah.universitysystemspringboot.mapper.StudentMapper;
import be.abdullah.universitysystemspringboot.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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

    @PostMapping
    public ResponseEntity<StudentDto> registerUser(@RequestBody RegisterStudentRequest request, UriComponentsBuilder builder) {
        var student = studentMapper.toEntity(request);
        var studentDto = studentMapper.toDto(studentRepository.save(student));

        var uri = builder.path("/students/{id}").buildAndExpand(student.getId()).toUri();
        return ResponseEntity.created(uri).body(studentDto);
    }

}
