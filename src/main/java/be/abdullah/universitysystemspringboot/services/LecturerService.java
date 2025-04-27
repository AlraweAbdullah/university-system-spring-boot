package be.abdullah.universitysystemspringboot.services;


import be.abdullah.universitysystemspringboot.dtos.ChangePasswordRequest;
import be.abdullah.universitysystemspringboot.dtos.LecturerDto;
import be.abdullah.universitysystemspringboot.dtos.RegisterLecturerRequest;
import be.abdullah.universitysystemspringboot.dtos.UpdateLecturerRequest;
import be.abdullah.universitysystemspringboot.exceptions.DuplicateLecturerException;
import be.abdullah.universitysystemspringboot.exceptions.LecturerNotFoundException;
import be.abdullah.universitysystemspringboot.mapper.LecturerMapper;
import be.abdullah.universitysystemspringboot.repositories.CredentialRepository;
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
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final CredintialService credintialService;


    public List<LecturerDto> getAllLecturers() {
        var lecturers = lecturerRepository.findAll();
        return lecturers.stream().map(lecturerMapper::toDto).toList();
    }

    public LecturerDto getLecturer(Long id) {
        var lecturer = lecturerRepository.findById(id).orElseThrow(LecturerNotFoundException::new);

        return lecturerMapper.toDto(lecturer);
    }

    public LecturerDto registerLecturer(RegisterLecturerRequest request) {
        if (credentialRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateLecturerException();
        }

        var lecturer = lecturerMapper.toEntity(request);
        lecturer.getCredential().setPassword(passwordEncoder.encode(lecturer.getCredential().getPassword()));
        return lecturerMapper.toDto(lecturerRepository.save(lecturer));
    }

    public LecturerDto updateLecturer(Long id, UpdateLecturerRequest request) {
        var lecturer =  lecturerRepository.findById(id).orElseThrow(LecturerNotFoundException::new);
        var credential = credentialRepository.findByEmail(request.getEmail()).orElse(null);

        if(credential != null && !credential.getId().equals(id)){
                throw new DuplicateLecturerException();
        }

        lecturerMapper.update(request, lecturer);
        return lecturerMapper.toDto(lecturerRepository.save(lecturer));
    }

    public void deleteLecturer(Long id) {
        lecturerRepository.findById(id).orElseThrow(LecturerNotFoundException::new);
        lecturerRepository.deleteById(id);
    }

    public void changePassword(Long id, ChangePasswordRequest request) {
        credintialService.changePassword(id, request, "lecturer");
    }

}
