package be.abdullah.universitysystemspringboot.services;

import be.abdullah.universitysystemspringboot.dtos.ChangePasswordRequest;
import be.abdullah.universitysystemspringboot.entities.Profile;
import be.abdullah.universitysystemspringboot.exceptions.ProfileNotFoundException;
import be.abdullah.universitysystemspringboot.repositories.LecturerRepository;
import be.abdullah.universitysystemspringboot.repositories.StudentRepository;
import be.abdullah.universitysystemspringboot.repositories.ProfileRepository;
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

    //TODO Clean hardcoded roles
    public void changePassword(Long id, ChangePasswordRequest request, String role) {
        // Retrieve the Profile entity based on role
        Profile profile = null;
        if (role.equals("student")) {
            var student = studentRepository.findById(id).orElse(null);
            if (student != null) {
                profile = student.getProfile();
            }
        } else if (role.equals("lecturer")) {
            var lecturer = lecturerRepository.findById(id).orElse(null);
            if (lecturer != null) {
                profile = lecturer.getProfile();
            }
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
        return new org.springframework.security.core.userdetails.User(
                profile.getEmail(),
                profile.getPassword(),
                Collections.emptyList()
        );
    }


}
