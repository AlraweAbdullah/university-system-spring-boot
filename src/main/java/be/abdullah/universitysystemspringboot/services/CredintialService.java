package be.abdullah.universitysystemspringboot.services;

import be.abdullah.universitysystemspringboot.repositories.CredentialRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class CredintialService  implements UserDetailsService {
    private final CredentialRepository credentialRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var credintial = credentialRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(
                credintial.getEmail(),
                credintial.getPassword(),
                Collections.emptyList()
        );
    }
}

