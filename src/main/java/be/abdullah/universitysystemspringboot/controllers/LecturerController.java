package be.abdullah.universitysystemspringboot.controllers;

import be.abdullah.universitysystemspringboot.dtos.*;
import be.abdullah.universitysystemspringboot.exceptions.*;
import be.abdullah.universitysystemspringboot.services.LecturerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Lecturers")
public class LecturerController {
    private final LecturerService lecturerService;

    @GetMapping
    @Operation(summary = "Get all registered lecturers")
    public List<LecturerDto> getAllLecturers() {
        return lecturerService.getAllLecturers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a lecturer by its id")
    public LecturerDto getLecturerById(
            @Parameter(name = "id", required = true) @PathVariable Long id) {
        return lecturerService.getLecturer(id);
    }

    @PostMapping
    @Operation(summary = "Register a new lecturer")
    public ResponseEntity<LecturerDto> registerLecturer(
            @Valid @RequestBody RegisterLecturerRequest request,
            UriComponentsBuilder builder) {
        var lecturerDto = lecturerService.registerLecturer(request);
        var uri = builder.path("/lecturers/{id}").buildAndExpand(lecturerDto.getId()).toUri();
        return ResponseEntity.created(uri).body(lecturerDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a registered lecturer by its id")
    public LecturerDto updateLecturer(
            @Parameter(name = "id", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateLecturerRequest request) {
        return lecturerService.updateLecturer(id, request);
    }

    // TODO: What should happen when deleting a lecturer that is assigned to courses!
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a registered lecturer by its id")
    public ResponseEntity<Void> deleteLecturer(
            @Parameter(name = "id", required = true) @PathVariable Long id) {
        lecturerService.deleteLecturer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    @Operation(summary = "Change a registered lecturer's password")
    public ResponseEntity<Void> changePassword(
            @Parameter(name = "id", required = true) @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        lecturerService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }

    // Exception handlers section
    @ExceptionHandler(LecturerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLecturerNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Lecturer not found"));
    }

    @ExceptionHandler(DuplicateLecturerException.class)
    public ResponseEntity<Map<String, String>> handleDuplicatedLecturer() {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Email is already registered."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        System.out.println("ee");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}