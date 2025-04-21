package be.abdullah.universitysystemspringboot.Controllers;

import be.abdullah.universitysystemspringboot.dtos.RegisterStudentRequest;
import be.abdullah.universitysystemspringboot.dtos.StudentDto;
import be.abdullah.universitysystemspringboot.dtos.UpdateStudentRequest;
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

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        var student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentMapper.toDto(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @RequestBody UpdateStudentRequest request) {
        var student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        studentMapper.update(request, student);
        studentRepository.save(student);
        return ResponseEntity.ok(studentMapper.toDto(student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        var user = studentRepository.findById(id).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
