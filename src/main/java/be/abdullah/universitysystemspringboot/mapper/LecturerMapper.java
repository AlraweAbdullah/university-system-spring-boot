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
    @Mapping(source = "password", target = "profile.password")
    @Mapping(source = "email", target = "profile.email")
    @Mapping(source = "lastname", target = "profile.lastname")
    @Mapping(source = "name", target = "profile.name")
    Lecturer toEntity(RegisterLecturerRequest request);

    @Mapping(source = "profile.email", target = "email")
    @Mapping(source = "profile.lastname", target = "lastname")
    @Mapping(source = "profile.name", target = "name")
    @Mapping(source = "profile.role", target = "role")
    LecturerDto toDto(Lecturer lecturer);

    @Mapping(source = "email", target = "profile.email")
    @Mapping(source = "lastname", target = "profile.lastname")
    @Mapping(source = "name", target = "profile.name")
    void update(UpdateLecturerRequest request, @MappingTarget Lecturer lecturer);
}
