package be.abdullah.universitysystemspringboot.controllers;


import be.abdullah.universitysystemspringboot.dtos.CourseDto;
import be.abdullah.universitysystemspringboot.dtos.CourseRequest;
import be.abdullah.universitysystemspringboot.exceptions.CourseNotFoundException;
import be.abdullah.universitysystemspringboot.exceptions.DuplicateCourseException;
import be.abdullah.universitysystemspringboot.exceptions.LecturerNotFoundException;
import be.abdullah.universitysystemspringboot.services.CourseService;
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
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public List<CourseDto> getAllCourses(@RequestParam(name = "lecturerId", required = false) Long lecturerId) {
        return courseService.getAllCourses(lecturerId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseRequest request, UriComponentsBuilder builder) {
        var courseDto = courseService.createCourse(request);
        var uri = builder.path("/courses/{id}").buildAndExpand(courseDto.getId()).toUri();
        return ResponseEntity.created(uri).body(courseDto);
    }

    @PutMapping("/{id}")
    public CourseDto updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequest request) {
        return courseService.updateCourse(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }


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
