package be.abdullah.universitysystemspringboot.controllers;

import be.abdullah.universitysystemspringboot.dtos.ChangePasswordRequest;
import be.abdullah.universitysystemspringboot.dtos.LecturerDto;
import be.abdullah.universitysystemspringboot.dtos.RegisterLecturerRequest;
import be.abdullah.universitysystemspringboot.dtos.UpdateLecturerRequest;
import be.abdullah.universitysystemspringboot.mapper.LecturerMapper;
import be.abdullah.universitysystemspringboot.repositories.LecturerRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/lecturers")
public class LecturerController {
    private final LecturerMapper lecturerMapper;
    private LecturerRepository lecturerRepository;

    @GetMapping
    public List<LecturerDto> getAllLecturers() {
        var lecturers = lecturerRepository.findAll();
        return lecturers.stream().map(lecturerMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LecturerDto> getLecturerById(@PathVariable Long id) {
        var lecturer = lecturerRepository.findById(id).orElse(null);
        if (lecturer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lecturerMapper.toDto(lecturer));
    }

    @PostMapping
    public ResponseEntity<?> registerLecturer(@Valid @RequestBody RegisterLecturerRequest request, UriComponentsBuilder builder) {
        if (lecturerRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(
                    Map.of("email", "Email is already registered.")
            );
        }
        var lecturer = lecturerMapper.toEntity(request);
        var lecturerDto = lecturerMapper.toDto(lecturerRepository.save(lecturer));

        var uri = builder.path("/lecturers/{id}").buildAndExpand(lecturer.getId()).toUri();
        return ResponseEntity.created(uri).body(lecturerDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLecturer(@PathVariable Long id, @Valid @RequestBody UpdateLecturerRequest request) {
        var lecturer = lecturerRepository.findById(id).orElse(null);
        if (lecturer == null) {
            return ResponseEntity.notFound().build();
        }

        if (lecturerRepository.existsByEmailAndIdNot(request.getEmail(), lecturer.getId())) {
            return ResponseEntity.badRequest().body(
                    Map.of("email", "Email is already registered.")
            );
        }
        lecturerMapper.update(request, lecturer);
        lecturerRepository.save(lecturer);
        return ResponseEntity.ok(lecturerMapper.toDto(lecturer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLecturer(@PathVariable Long id) {
        var lecturer = lecturerRepository.findById(id).orElse(null);
        if (lecturer == null) {
            return ResponseEntity.notFound().build();
        }
        lecturerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest request) {
        var lecturer = lecturerRepository.findById(id).orElse(null);
        if (lecturer == null) {
            return ResponseEntity.notFound().build();
        }

        if (!lecturer.getPassword().equals(request.getOldPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        lecturer.setPassword(request.getNewPassword());
        lecturerRepository.save(lecturer);
        return ResponseEntity.noContent().build();
    }
}
