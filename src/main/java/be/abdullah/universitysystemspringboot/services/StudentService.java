package be.abdullah.universitysystemspringboot.services;

import be.abdullah.universitysystemspringboot.dtos.*;
import be.abdullah.universitysystemspringboot.entities.StudentCourse;
import be.abdullah.universitysystemspringboot.entities.StudentCourseId;
import be.abdullah.universitysystemspringboot.exceptions.*;
import be.abdullah.universitysystemspringboot.mapper.StudentCourseMapper;
import be.abdullah.universitysystemspringboot.mapper.StudentMapper;
import be.abdullah.universitysystemspringboot.repositories.CourseRepository;
import be.abdullah.universitysystemspringboot.repositories.CredentialRepository;
import be.abdullah.universitysystemspringboot.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final StudentCourseMapper studentCourseMapper;
    private final CourseRepository courseRepository;
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final CredintialService credintialService;


    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream().map(studentMapper::toDto).toList();
    }

    public StudentDto getStudent(Long id) {
        var student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        return studentMapper.toDto(student);
    }

    public StudentDto registerStudent(RegisterStudentRequest request) {
        if (credentialRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateStudentException();
        }
        var student = studentMapper.toEntity(request);
        student.getCredential().setPassword(passwordEncoder.encode(request.getPassword()));
        return studentMapper.toDto(studentRepository.save(student));
    }

    public StudentDto updateStudent(@PathVariable Long id, @Valid @RequestBody UpdateStudentRequest request) {
        var student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);


        var  credential = credentialRepository.findByEmail(request.getEmail()).orElse(null);
            if (credential != null && !credential.getId().equals(student.getCredential().getId())) {
                throw new DuplicateStudentException();
            }

        studentMapper.update(request, student);
        System.out.println(student.getCredential().getEmail());
        studentRepository.save(student);
        return studentMapper.toDto(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        studentRepository.deleteById(id);
    }

    public void changePassword(Long id, ChangePasswordRequest request) {
        credintialService.changePassword(id, request, "student");
    }

    @Transactional
    public List<StudentCourseDto> getStudentCourses(Long studentId) {
        var student = studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);
        return student.getStudentCourses().stream().map(studentCourseMapper::toDto).toList();
    }

    @Transactional
    public StudentCourseDto enrollStudent(@PathVariable Long id, @Valid @RequestBody AddCourseToStudentRequest request) {
        var student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        var course = courseRepository.findById(request.getCourseId()).orElseThrow(CourseNotFoundException::new);

        if (student.getStudentCourses().stream().anyMatch(s -> s.getCourse().getId().equals(request.getCourseId()))) {
            throw new DuplicateEnrolledCourseException();
        }

        //TODO Needs an appropriate MapStruct mapper
        var studentCourse = new StudentCourse();
        studentCourse.setId(new StudentCourseId(student.getId(), course.getId()));
        studentCourse.setStudent(student);
        studentCourse.setCourse(course);
        studentCourse.setCreatedAt(LocalDateTime.now());


        student.getStudentCourses().add(studentCourse);
        studentRepository.save(student);

        return studentCourseMapper.toDto(studentCourse);
    }

    @Transactional
    public void withdrawStudent(Long studentId, Long courseId) {
        var student = studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);
        courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);

        var studentCourse = student.getStudentCourses().stream().filter(sc -> sc.getCourse().getId().equals(courseId)).findFirst().orElseThrow(StudentNotEnrolledException::new);

        student.getStudentCourses().remove(studentCourse);
        studentRepository.save(student);
    }

    @Transactional
    public StudentCourseDto getStudentCourse(Long id, Long courseId) {
        var student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
        var studentCourse = student.getStudentCourses().stream().filter(sc -> sc.getCourse().getId().equals(courseId)).findFirst().orElseThrow(StudentNotEnrolledException::new);
        return studentCourseMapper.toDto(studentCourse);
    }
}

