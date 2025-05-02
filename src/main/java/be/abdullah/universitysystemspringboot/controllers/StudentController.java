package be.abdullah.universitysystemspringboot.controllers;

import be.abdullah.universitysystemspringboot.dtos.*;
import be.abdullah.universitysystemspringboot.exceptions.*;
import be.abdullah.universitysystemspringboot.services.StudentService;
import be.abdullah.universitysystemspringboot.services.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Students")
public class StudentController {
    private final StudentService studentService;
    private final ProfileService profileService;

    @GetMapping
    @Operation(summary = "Get all registered students")
    public List<StudentDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a student by its id")
    public StudentDto getStudent(@Parameter(name = "id", required = true) @PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PostMapping
    @Operation(summary = "Register a student")
    public ResponseEntity<StudentDto> registerStudent(@Valid @RequestBody RegisterStudentRequest request, UriComponentsBuilder builder) {
        var studentDto = studentService.registerStudent(request);
        var uri = builder.path("/students/{id}").buildAndExpand(studentDto.getId()).toUri();
        return ResponseEntity.created(uri).body(studentDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a registered student by its id")
    public StudentDto updateStudent(@Parameter(name = "id", required = true) @PathVariable Long id, @Valid @RequestBody UpdateStudentRequest request) {
        return studentService.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a registered student by its id")
    public ResponseEntity<Void> deleteStudent(@Parameter(name = "id", required = true) @PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    @Operation(summary = "Change a registered student's password")
    public ResponseEntity<Void> changePassword(@Parameter(name = "id", required = true) @PathVariable Long id, @Valid @RequestBody ChangePasswordRequest request) {
        profileService.changePassword(id, request, "student");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/courses")
    @Operation(summary = "Get all courses of a registered student by its id")
    public List<StudentCourseDto> getStudentCourses(@Parameter(name = "id", required = true) @PathVariable Long id) {
        return studentService.getStudentCourses(id);
    }


    @GetMapping("/{id}/courses/{courseId}")
    @Operation(summary = "Get a course of a registered student by student and course id")
    public StudentCourseDto getStudentCourse(@Parameter(name = "id", required = true) @PathVariable Long id, @Parameter(name = "courseId", required = true) @PathVariable Long courseId) {
        return studentService.getStudentCourse(id, courseId);
    }


    @PostMapping("/{id}/courses")
    @Operation(summary = "Enroll a student in a course by student id")
    public ResponseEntity<StudentCourseDto> enrollStudent(@Parameter(name = "id", required = true) @PathVariable Long id, @Valid @RequestBody AddCourseToStudentRequest request, UriComponentsBuilder builder) {
        var studentCourseDto = studentService.enrollStudent(id, request);
        var uri = builder.path("/students/{id}/courses/{courseId}").buildAndExpand(id, studentCourseDto.getCourse().getId()).toUri();
        return ResponseEntity.created(uri).body(studentCourseDto);
    }

    @DeleteMapping("/{id}/courses/{courseId}")
    @Operation(summary = "Withdraw  a student from a course by student id")
    public ResponseEntity<Void> removeCourseFromStudent(@Parameter(name = "id", required = true) @PathVariable Long id,  @Parameter(name = "courseId", required = true) @PathVariable Long courseId) {
        studentService.withdrawStudent(id, courseId);
        return ResponseEntity.noContent().build();
    }

    // Exception handlers section
    @ExceptionHandler({StudentNotFoundException.class, ProfileNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleStudentNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Student not found"));
    }

    @ExceptionHandler(DuplicateStudentException.class)
    public ResponseEntity<Map<String, String>> handleDuplicatedStudent() {
        return ResponseEntity.badRequest().body(Map.of("error", "Email is already registered."));
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCourseNotFound() {
        return ResponseEntity.badRequest().body(Map.of("error", "Course not found."));
    }

    @ExceptionHandler(DuplicateEnrolledCourseException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEnrolledCourse() {
        return ResponseEntity.badRequest().body(Map.of("error", "Student already enrolled in this course."));
    }

    @ExceptionHandler(StudentNotEnrolledException.class)
    public ResponseEntity<Map<String, String>> handleStudentNotEnrolled() {
        return ResponseEntity.badRequest().body(Map.of("error", "Student not enrolled in this course."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
