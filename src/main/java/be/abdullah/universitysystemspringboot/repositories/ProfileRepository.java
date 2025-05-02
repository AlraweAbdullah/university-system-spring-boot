package be.abdullah.universitysystemspringboot.repositories;


import be.abdullah.universitysystemspringboot.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByEmail(String email);
    Optional<Profile> findByEmail(String email);
}
