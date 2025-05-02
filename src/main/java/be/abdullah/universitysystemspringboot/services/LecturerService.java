package be.abdullah.universitysystemspringboot.services;


import be.abdullah.universitysystemspringboot.dtos.LecturerDto;
import be.abdullah.universitysystemspringboot.dtos.RegisterLecturerRequest;
import be.abdullah.universitysystemspringboot.dtos.UpdateLecturerRequest;
import be.abdullah.universitysystemspringboot.entities.Role;
import be.abdullah.universitysystemspringboot.exceptions.DuplicateLecturerException;
import be.abdullah.universitysystemspringboot.exceptions.LecturerNotFoundException;
import be.abdullah.universitysystemspringboot.mapper.LecturerMapper;
import be.abdullah.universitysystemspringboot.repositories.ProfileRepository;
import be.abdullah.universitysystemspringboot.repositories.LecturerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LecturerService {
    private final LecturerRepository lecturerRepository;
    private final LecturerMapper lecturerMapper;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;


    public List<LecturerDto> getAllLecturers() {
        var lecturers = lecturerRepository.findAll();
        return lecturers.stream().map(lecturerMapper::toDto).toList();
    }

    public LecturerDto getLecturer(Long id) {
        var lecturer = lecturerRepository.findById(id).orElseThrow(LecturerNotFoundException::new);

        return lecturerMapper.toDto(lecturer);
    }

    public LecturerDto registerLecturer(RegisterLecturerRequest request) {
        if (profileRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateLecturerException();
        }

        var lecturer = lecturerMapper.toEntity(request);
        lecturer.getProfile().setPassword(passwordEncoder.encode(lecturer.getProfile().getPassword()));
        lecturer.getProfile().setRole(Role.LECTURER);
        return lecturerMapper.toDto(lecturerRepository.save(lecturer));
    }

    public LecturerDto updateLecturer(Long id, UpdateLecturerRequest request) {
        var lecturer = lecturerRepository.findById(id).orElseThrow(LecturerNotFoundException::new);
        var profile = profileRepository.findByEmail(request.getEmail()).orElse(null);

        if (profile != null && !profile.getId().equals(lecturer.getProfile().getId())) {
            throw new DuplicateLecturerException();
        }

        lecturerMapper.update(request, lecturer);
        return lecturerMapper.toDto(lecturerRepository.save(lecturer));
    }

    public void deleteLecturer(Long id) {
        lecturerRepository.findById(id).orElseThrow(LecturerNotFoundException::new);
        lecturerRepository.deleteById(id);
    }


}
