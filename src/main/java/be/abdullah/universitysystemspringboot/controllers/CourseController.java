package be.abdullah.universitysystemspringboot.controllers;

import be.abdullah.universitysystemspringboot.dtos.CourseDto;
import be.abdullah.universitysystemspringboot.dtos.CourseRequest;
import be.abdullah.universitysystemspringboot.exceptions.CourseNotFoundException;
import be.abdullah.universitysystemspringboot.exceptions.DuplicateCourseException;
import be.abdullah.universitysystemspringboot.exceptions.LecturerNotFoundException;
import be.abdullah.universitysystemspringboot.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
@Tag(name = "Courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "Get all courses, optionally filter by lecturer")
    public List<CourseDto> getAllCourses(@RequestParam(name = "lecturerId", required = false) Long lecturerId) {
        return courseService.getAllCourses(lecturerId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a course by its ID")
    public ResponseEntity<CourseDto> getCourse(@Parameter(name = "id", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    @PostMapping
    @Operation(summary = "Create a new course")
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseRequest request, UriComponentsBuilder builder) {
        var courseDto = courseService.createCourse(request);
        var uri = builder.path("/courses/{id}").buildAndExpand(courseDto.getId()).toUri();
        return ResponseEntity.created(uri).body(courseDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing course by its ID")
    public CourseDto updateCourse(@Parameter(name = "id", required = true) @PathVariable Long id,
                                  @Valid @RequestBody CourseRequest request) {
        return courseService.updateCourse(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a course by its ID")
    public ResponseEntity<Void> deleteCourse(@Parameter(name = "id", required = true) @PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    // Exception handlers section
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCourseNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Course not found"));
    }

    @ExceptionHandler(LecturerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLecturerNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Lecturer not found"));
    }

    @ExceptionHandler(DuplicateCourseException.class)
    public ResponseEntity<Map<String, String>> handleDuplicatedCourse() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Course already exists"));
    }
}
