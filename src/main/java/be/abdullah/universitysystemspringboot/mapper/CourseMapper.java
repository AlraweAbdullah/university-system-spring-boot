package be.abdullah.universitysystemspringboot.mapper;

import be.abdullah.universitysystemspringboot.dtos.CourseDto;
import be.abdullah.universitysystemspringboot.dtos.CourseRequest;
import be.abdullah.universitysystemspringboot.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "lecturer.email", source = "lecturer.profile.email")
    @Mapping(target = "lecturer.lastname", source = "lecturer.profile.lastname")
    @Mapping(target = "lecturer.name", source = "lecturer.profile.name")
    CourseDto toDto(Course course);

    Course toEntity(CourseRequest request);

    @Mapping(target = "lecturer.id", source = "lecturerId")
    void update(CourseRequest request, @MappingTarget Course course);
}
