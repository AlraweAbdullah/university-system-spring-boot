package be.abdullah.universitysystemspringboot.services;

import be.abdullah.universitysystemspringboot.dtos.CourseDto;
import be.abdullah.universitysystemspringboot.dtos.CourseRequest;
import be.abdullah.universitysystemspringboot.entities.Course;
import be.abdullah.universitysystemspringboot.exceptions.CourseNotFoundException;
import be.abdullah.universitysystemspringboot.exceptions.DuplicateCourseException;
import be.abdullah.universitysystemspringboot.exceptions.LecturerNotFoundException;
import be.abdullah.universitysystemspringboot.mapper.CourseMapper;
import be.abdullah.universitysystemspringboot.repositories.CourseRepository;
import be.abdullah.universitysystemspringboot.repositories.LecturerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final LecturerRepository lecturerRepository;

    public List<CourseDto> getAllCourses(Long lecturerId) {
        List<Course> courses;
        if (lecturerId == null) {
            courses = courseRepository.findAll();
        } else {
            courses = courseRepository.findByLecturerId(lecturerId);
        }

        return courses.stream().map(courseMapper::toDto).toList();
    }

    public CourseDto getCourse(Long courseId) {
        var course = courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
        return courseMapper.toDto(course);
    }

    public CourseDto createCourse(CourseRequest request) {
        var lecturer = lecturerRepository.findById(request.getLecturerId()).orElseThrow(LecturerNotFoundException::new);
        var courseByName = courseRepository.findByName(request.getName());
        if (courseByName != null) {
            throw new DuplicateCourseException();
        }

        var course = courseMapper.toEntity(request);
        course.setLecturer(lecturer);

        return courseMapper.toDto(courseRepository.save(course));
    }

    public CourseDto updateCourse(Long id, CourseRequest request) {
        var course = courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
        var lecturer = lecturerRepository.findById(request.getLecturerId()).orElseThrow(LecturerNotFoundException::new);
        if (courseRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new DuplicateCourseException();
        }
        courseMapper.update(request, course);
        course.setLecturer(lecturer);
        return courseMapper.toDto(courseRepository.save(course));
    }

    public void deleteCourse(Long id) {
        courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
        courseRepository.deleteById(id);
    }
}
