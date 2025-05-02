package be.abdullah.universitysystemspringboot.mapper;

import be.abdullah.universitysystemspringboot.dtos.StudentCourseDto;
import be.abdullah.universitysystemspringboot.entities.StudentCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentCourseMapper {
    @Mapping(source = "createdAt", target = "registeredAt")
    @Mapping(source = "course.lecturer.profile.name", target = "course.lecturer.name")
    @Mapping(source = "course.lecturer.profile.lastname", target = "course.lecturer.lastname")
    @Mapping(source = "course.lecturer.profile.email", target = "course.lecturer.email")
    StudentCourseDto toDto(StudentCourse studentCourse);

}
