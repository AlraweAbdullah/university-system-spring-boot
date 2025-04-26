package be.abdullah.universitysystemspringboot.controllers;

import be.abdullah.universitysystemspringboot.dtos.*;
import be.abdullah.universitysystemspringboot.entities.StudentCourse;
import be.abdullah.universitysystemspringboot.entities.StudentCourseId;
import be.abdullah.universitysystemspringboot.mapper.StudentCourseMapper;
import be.abdullah.universitysystemspringboot.mapper.StudentMapper;
import be.abdullah.universitysystemspringboot.repositories.CourseRepository;
import be.abdullah.universitysystemspringboot.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final StudentMapper studentMapper;
    private final StudentCourseMapper studentCourseMapper;
    private final CourseRepository courseRepository;
    private StudentRepository studentRepository;

    @GetMapping
    public List<StudentDto> getAllStudents() {
        var students = studentRepository.findAll();
        return students.stream().map(studentMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        var student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentMapper.toDto(student));
    }

    @PostMapping
    public ResponseEntity<?> registerStudent(@Valid @RequestBody RegisterStudentRequest request, UriComponentsBuilder builder) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(
                    Map.of("email", "Email is already registered.")
            );
        }
        var student = studentMapper.toEntity(request);
        var studentDto = studentMapper.toDto(studentRepository.save(student));

        var uri = builder.path("/students/{id}").buildAndExpand(student.getId()).toUri();
        return ResponseEntity.created(uri).body(studentDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @Valid @RequestBody UpdateStudentRequest request) {
        var student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        if (studentRepository.existsByEmailAndIdNot(request.getEmail(), student.getId())) {
            return ResponseEntity.badRequest().body(
                    Map.of("email", "Email is already registered.")
            );
        }
        studentMapper.update(request, student);
        studentRepository.save(student);
        return ResponseEntity.ok(studentMapper.toDto(student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        var student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest request) {
        var student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        if (!student.getPassword().equals(request.getOldPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        student.setPassword(request.getNewPassword());
        studentRepository.save(student);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @GetMapping("/{id}/courses")
    public ResponseEntity<Set<StudentCourseDto>> getStudentCourses(@PathVariable Long id) {
        var student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }


        var dtos = student.getStudentCourses().stream()
                .map(studentCourseMapper::toDto)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(dtos);
    }

    @Transactional
    @PostMapping("{id}/courses")
    public ResponseEntity<?> addCourseToStudent(@PathVariable Long id, @Valid @RequestBody AddCourseToStudentRequest request, UriComponentsBuilder builder){
        var student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        var course = courseRepository.findById(request.getCourseId()).orElse(null);
        if (course == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("course", "Course not found.")
            );
        }
        var exitedCourse = student.getStudentCourses().stream()
                .filter(sc -> sc.getCourse().getId().equals(request.getCourseId()))
                .findFirst()
                .orElse(null);


        if (exitedCourse != null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "course", "Student is already enrolled in " + course.getName()
                    )
            );
        }

        //TODO Needs an appropriate MapStruct mapper
        var studentCourse = new StudentCourse();
        studentCourse.setId(new StudentCourseId(student.getId(), course.getId()));
        studentCourse.setStudent(student);
        studentCourse.setCourse(course);
        studentCourse.setCreatedAt(LocalDateTime.now());

        student.getStudentCourses().add(studentCourse);
        studentRepository.save(student);

        var uri = builder.path("/students/{id}/courses/"+request.getCourseId()).buildAndExpand(student.getId()).toUri();
        return ResponseEntity.created(uri).body(studentCourseMapper.toDto(studentCourse));
    }

    @Transactional
    @DeleteMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<?> removeCourseFromStudent(@PathVariable Long studentId, @PathVariable Long courseId) {
        var student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        var course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        var studentCourse = student.getStudentCourses().stream()
                .filter(sc -> sc.getCourse().getId().equals(courseId))
                .findFirst()
                .orElse(null);

        if (studentCourse == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "course", "Student is not enrolled in " + course.getName()
                    )
            );
        }


        student.getStudentCourses().remove(studentCourse);

        studentRepository.save(student);
        return ResponseEntity.noContent().build();
    }


}
