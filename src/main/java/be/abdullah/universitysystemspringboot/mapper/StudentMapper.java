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
    @Mapping(source = "password", target = "profile.password")
    @Mapping(source = "email", target = "profile.email")
    @Mapping(source = "lastname", target = "profile.lastname")
    @Mapping(source = "name", target = "profile.name")
    Student toEntity(RegisterStudentRequest request);

    @Mapping(source = "profile.email", target = "email")
    @Mapping(source = "profile.lastname", target = "lastname")
    @Mapping(source = "profile.name", target = "name")
    @Mapping(source = "profile.role", target = "role")
    StudentDto toDto(Student student);

    @Mapping(source = "email", target = "profile.email")
    @Mapping(source = "name", target = "profile.name")
    @Mapping(source = "lastname", target = "profile.lastname")
    void update(UpdateStudentRequest request, @MappingTarget Student student);
}
