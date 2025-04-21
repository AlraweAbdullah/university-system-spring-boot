package be.abdullah.universitysystemspringboot.mapper;

import be.abdullah.universitysystemspringboot.dtos.StudentDto;
import be.abdullah.universitysystemspringboot.entities.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentDto toDto(Student student);
}
