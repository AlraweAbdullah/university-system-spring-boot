package be.abdullah.universitysystemspringboot.repositories;


import be.abdullah.universitysystemspringboot.entities.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    boolean existsByEmail(String email);
    List<Credential> findByEmail(String email);
}
