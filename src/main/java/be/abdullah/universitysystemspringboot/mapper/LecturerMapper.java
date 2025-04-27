package be.abdullah.universitysystemspringboot.mapper;

import be.abdullah.universitysystemspringboot.dtos.LecturerDto;
import be.abdullah.universitysystemspringboot.dtos.RegisterLecturerRequest;
import be.abdullah.universitysystemspringboot.dtos.UpdateLecturerRequest;
import be.abdullah.universitysystemspringboot.entities.Lecturer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LecturerMapper {
    @Mapping(source = "password", target = "credential.password")
    @Mapping(source = "email", target = "credential.email")
    Lecturer toEntity(RegisterLecturerRequest request);

    @Mapping(source = "credential.email", target = "email")
    LecturerDto toDto(Lecturer lecturer);
    void update(UpdateLecturerRequest request, @MappingTarget Lecturer lecturer);
}
