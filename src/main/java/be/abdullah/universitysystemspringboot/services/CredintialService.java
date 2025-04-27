package be.abdullah.universitysystemspringboot.services;

import be.abdullah.universitysystemspringboot.dtos.ChangePasswordRequest;
import be.abdullah.universitysystemspringboot.dtos.LoginRequest;
import be.abdullah.universitysystemspringboot.entities.Credential;
import be.abdullah.universitysystemspringboot.exceptions.LecturerNotFoundException;
import be.abdullah.universitysystemspringboot.exceptions.StudentNotFoundException;
import be.abdullah.universitysystemspringboot.repositories.CredentialRepository;
import be.abdullah.universitysystemspringboot.repositories.LecturerRepository;
import be.abdullah.universitysystemspringboot.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CredintialService {
    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final CredentialRepository credentialRepository;


    // TODO Clean hardcoded roles
    public void changePassword(Long id, ChangePasswordRequest request, String role) {
        Credential credential;
        if (role.equals("student")) {
            var student = studentRepository.findById(id)
                    .orElseThrow(StudentNotFoundException::new);
            credential = student.getCredential();
        } else if (role.equals("lecturer")) {
            var lecturer = lecturerRepository.findById(id)
                    .orElseThrow(LecturerNotFoundException::new);
            credential = lecturer.getCredential();
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        if (!passwordEncoder.matches(request.getOldPassword(), credential.getPassword())) {
            throw new AccessDeniedException("Password does not match");
        }
        credential.setPassword(passwordEncoder.encode(request.getNewPassword()));
        credentialRepository.save(credential);
    }

    public void logIn(LoginRequest request) {
        var credintial = credentialRepository.findByEmail(request.getEmail()).orElse(null);
        if(credintial == null) {
            throw new AccessDeniedException("Email is not registered");
        }
        if(!passwordEncoder.matches(request.getPassword(), credintial.getPassword())) {
            throw new AccessDeniedException("Password does not match");
        }
    }
}

