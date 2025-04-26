package be.abdullah.universitysystemspringboot.controllers;

import be.abdullah.universitysystemspringboot.dtos.*;
import be.abdullah.universitysystemspringboot.exceptions.*;
import be.abdullah.universitysystemspringboot.services.StudentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<StudentDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public StudentDto getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PostMapping
    public ResponseEntity<StudentDto> registerStudent(@Valid @RequestBody RegisterStudentRequest request, UriComponentsBuilder builder) {
        var studentDto = studentService.registerStudent(request);
        var uri = builder.path("/students/{id}").buildAndExpand(studentDto.getId()).toUri();
        return ResponseEntity.created(uri).body(studentDto);
    }

    @PutMapping("/{id}")
    public StudentDto updateStudent(@PathVariable Long id, @Valid @RequestBody UpdateStudentRequest request) {
        return studentService.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest request) {
        studentService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @GetMapping("/{id}/courses")
    public List<StudentCourseDto> getStudentCourses(@PathVariable Long id) {
        return studentService.getStudentCourses(id);
    }


    @PostMapping("{id}/courses")
    public ResponseEntity<StudentCourseDto> enrollStudent(@PathVariable Long id, @Valid @RequestBody AddCourseToStudentRequest request, UriComponentsBuilder builder) {
        var studentCourseDto = studentService.enrollStudent(id, request);
        var uri = builder.path("/students/{id}/courses/" + request.getCourseId()).buildAndExpand(studentCourseDto.getCourse().getId()).toUri();
        return ResponseEntity.created(uri).body(studentCourseDto);
    }

    @Transactional
    @DeleteMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<Void> removeCourseFromStudent(@PathVariable Long studentId, @PathVariable Long courseId) {
        studentService.unEnrollStudent(studentId, courseId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(StudentNoFoundException.class)
    public ResponseEntity<Map<String, String>> handleStudentNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Student not found"));
    }

    @ExceptionHandler(DuplicateStudentException.class)
    public ResponseEntity<Map<String, String>> handleDuplicatedStudent() {
        return ResponseEntity.badRequest().body(
                Map.of("error", "Email is already registered.")
        );
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCourseNotFound() {
        return ResponseEntity.badRequest().body(
                Map.of("error", "Course not found.")
        );
    }

    @ExceptionHandler(DuplicateEnrolledCourseException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEnrolledCourse() {
        return ResponseEntity.badRequest().body(
                Map.of("error", "Student already enrolled in this course.")
        );
    }

    @ExceptionHandler(StudentNotEnrolledException.class)
    public ResponseEntity<Map<String, String>> handleStudentNotEnrolled() {
        return ResponseEntity.badRequest().body(
                Map.of("error", "Student not enrolled in this course.")
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }



}
