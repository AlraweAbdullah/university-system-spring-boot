package be.abdullah.universitysystemspringboot.mapper;

import be.abdullah.universitysystemspringboot.dtos.StudentCourseDto;
import be.abdullah.universitysystemspringboot.entities.StudentCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentCourseMapper {

    @Mapping(target = "registeredAt", source = "createdAt")
    StudentCourseDto toDto(StudentCourse studentCourse);

}
