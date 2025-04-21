package be.abdullah.universitysystemspringboot.mapper;

import be.abdullah.universitysystemspringboot.dtos.RegisterStudentRequest;
import be.abdullah.universitysystemspringboot.dtos.StudentDto;
import be.abdullah.universitysystemspringboot.dtos.UpdateStudentRequest;
import be.abdullah.universitysystemspringboot.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    Student toEntity(RegisterStudentRequest request);
    StudentDto toDto(Student student);
    // No need for @BeanMapping as PUT expects the full object.
    // @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE).
    void update(UpdateStudentRequest request, @MappingTarget Student student);
}
