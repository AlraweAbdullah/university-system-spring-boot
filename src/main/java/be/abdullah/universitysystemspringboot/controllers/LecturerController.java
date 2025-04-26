package be.abdullah.universitysystemspringboot.controllers;

import be.abdullah.universitysystemspringboot.dtos.ChangePasswordRequest;
import be.abdullah.universitysystemspringboot.dtos.LecturerDto;
import be.abdullah.universitysystemspringboot.dtos.RegisterLecturerRequest;
import be.abdullah.universitysystemspringboot.dtos.UpdateLecturerRequest;
import be.abdullah.universitysystemspringboot.exceptions.DuplicateLecturerException;
import be.abdullah.universitysystemspringboot.exceptions.LecturerNotFoundException;
import be.abdullah.universitysystemspringboot.mapper.LecturerMapper;
import be.abdullah.universitysystemspringboot.repositories.LecturerRepository;
import be.abdullah.universitysystemspringboot.services.LecturerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/lecturers")
public class LecturerController {
    private final LecturerService lecturerService;

    @GetMapping
    public List<LecturerDto> getAllLecturers() {
       return lecturerService.getAllLecturers();
    }

    @GetMapping("/{id}")
    public LecturerDto getLecturerById(@PathVariable Long id) {
        return lecturerService.getLecturer(id);
    }

    @PostMapping
    public ResponseEntity<LecturerDto> registerLecturer(@Valid @RequestBody RegisterLecturerRequest request, UriComponentsBuilder builder) {
        var lecturerDto = lecturerService.registerLecturer(request);
        var uri = builder.path("/lecturers/{id}").buildAndExpand(lecturerDto.getId()).toUri();
        return ResponseEntity.created(uri).body(lecturerDto);
    }

    @PutMapping("/{id}")
    public LecturerDto updateLecturer(@PathVariable Long id, @Valid @RequestBody UpdateLecturerRequest request) {
        return lecturerService.updateLecturer(id, request);
    }

    //TODO What should happen when deleting a course that has courses to give!!
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLecturer(@PathVariable Long id) {
        lecturerService.deleteLecturer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest request) {
        lecturerService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(LecturerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLecturerNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Lecturer not found"));
    }

    @ExceptionHandler(DuplicateLecturerException.class)
    public ResponseEntity<Map<String, String>> handleDuplicatedLecturer() {
        return ResponseEntity.badRequest().body(
                Map.of("error", "Email is already registered.")
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
