package be.abdullah.universitysystemspringboot.mapper;

import be.abdullah.universitysystemspringboot.dtos.CourseDto;
import be.abdullah.universitysystemspringboot.dtos.CourseRequest;
import be.abdullah.universitysystemspringboot.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    //Not actually needed
    @Mapping(target = "lecturer", source = "lecturer")
    CourseDto toDto(Course course);

    Course toEntity(CourseRequest request);
    @Mapping(target = "lecturer.id", source = "lecturerId")
    void update(CourseRequest request, @MappingTarget Course course);
}
