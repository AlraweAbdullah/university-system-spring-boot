package be.abdullah.universitysystemspringboot.controllers;


import be.abdullah.universitysystemspringboot.dtos.CourseDto;
import be.abdullah.universitysystemspringboot.dtos.CourseRequest;
import be.abdullah.universitysystemspringboot.mapper.CourseMapper;
import be.abdullah.universitysystemspringboot.repositories.CourseRepository;
import be.abdullah.universitysystemspringboot.repositories.LecturerRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final LecturerRepository lecturerRepository;
    private final CourseMapper courseMapper;

    public CourseController(CourseRepository courseRepository, LecturerRepository lecturerRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.lecturerRepository = lecturerRepository;
        this.courseMapper = courseMapper;
    }

    @GetMapping
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream().map(courseMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable long id) {
        var course = courseRepository.findById(id).orElse(null);

        if(course == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(courseMapper.toDto(course));
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody CourseRequest request, UriComponentsBuilder builder) {
        var lecturer = lecturerRepository.findById(request.getLecturerId()).orElse(null);
        if (lecturer == null) {
            return ResponseEntity.notFound().build();
        }

        var courseByName = courseRepository.findByName(request.getName());
        if (courseByName != null) {
            return ResponseEntity.badRequest().body(
                    Map.of("name", "Course name already exists!.")
            );
        }

        var course = courseMapper.toEntity(request);
        course.setLecturer(lecturer);

        var courseDto = courseMapper.toDto(courseRepository.save(course));
        var uri = builder.path("/courses/{id}").buildAndExpand(course.getId()).toUri();

        return ResponseEntity.created(uri).body(courseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable long id, @Valid @RequestBody CourseRequest request, UriComponentsBuilder builder) {
        var course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        var lecturer = lecturerRepository.findById(request.getLecturerId()).orElse(null);
        if(lecturer == null) {
            return ResponseEntity.notFound().build();
        }

        if(courseRepository.existsByNameAndIdNot(request.getName(), id)){
            return ResponseEntity.badRequest().body(
                    Map.of("name", "Course name already exists!.")
            );
        }
        courseMapper.update(request, course);
        course.setLecturer(lecturer);
        courseRepository.save(course);

        return ResponseEntity.ok(courseMapper.toDto(course));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        var course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        courseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
