package be.abdullah.universitysystemspringboot.services;

import be.abdullah.universitysystemspringboot.dtos.ChangePasswordRequest;
import be.abdullah.universitysystemspringboot.entities.Profile;
import be.abdullah.universitysystemspringboot.entities.Role;
import be.abdullah.universitysystemspringboot.exceptions.ProfileNotFoundException;
import be.abdullah.universitysystemspringboot.repositories.LecturerRepository;
import be.abdullah.universitysystemspringboot.repositories.ProfileRepository;
import be.abdullah.universitysystemspringboot.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class ProfileService implements UserDetailsService {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final LecturerRepository lecturerRepository;
    private final ProfileRepository profileRepository;

    public void changePassword(Long id, ChangePasswordRequest request, Role role) {
        Profile profile = null;
        switch (role) {
            case STUDENT:
                var student = studentRepository.findById(id).orElse(null);
                if (student != null) {
                    profile = student.getProfile();
                }
                break;
            case LECTURER:
                var lecturer = lecturerRepository.findById(id).orElse(null);
                if (lecturer != null) {
                    profile = lecturer.getProfile();
                }
                break;
        }
        if (profile == null) {
            throw new ProfileNotFoundException();
        }

        if (!passwordEncoder.matches(request.getOldPassword(), profile.getPassword())) {
            throw new AccessDeniedException("Password does not match");
        }

        profile.setPassword(passwordEncoder.encode(request.getNewPassword()));
        profileRepository.save(profile);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var profile = profileRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(profile.getEmail(), profile.getPassword(), Collections.emptyList());
    }


}
