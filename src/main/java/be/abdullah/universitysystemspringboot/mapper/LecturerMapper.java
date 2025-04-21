package be.abdullah.universitysystemspringboot.mapper;

import be.abdullah.universitysystemspringboot.dtos.LecturerDto;
import be.abdullah.universitysystemspringboot.dtos.RegisterLecturerRequest;
import be.abdullah.universitysystemspringboot.dtos.UpdateLecturerRequest;
import be.abdullah.universitysystemspringboot.entities.Lecturer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LecturerMapper {
    Lecturer toEntity(RegisterLecturerRequest request);
    LecturerDto toDto(Lecturer lecturer);


    void update(UpdateLecturerRequest request, @MappingTarget Lecturer lecturer);
}
