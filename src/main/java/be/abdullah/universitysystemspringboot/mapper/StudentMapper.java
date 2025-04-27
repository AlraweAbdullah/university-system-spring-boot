package be.abdullah.universitysystemspringboot.mapper;

import be.abdullah.universitysystemspringboot.dtos.RegisterStudentRequest;
import be.abdullah.universitysystemspringboot.dtos.StudentDto;
import be.abdullah.universitysystemspringboot.dtos.UpdateStudentRequest;
import be.abdullah.universitysystemspringboot.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(source = "password", target = "credential.password")
    @Mapping(source = "email", target = "credential.email")
    Student toEntity(RegisterStudentRequest request);

    @Mapping(source = "credential.email", target = "email")
    StudentDto toDto(Student student);


    void update(UpdateStudentRequest request, @MappingTarget Student student);
}
