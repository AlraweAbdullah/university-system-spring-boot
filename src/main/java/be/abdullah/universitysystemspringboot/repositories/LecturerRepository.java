package be.abdullah.universitysystemspringboot.repositories;

import be.abdullah.universitysystemspringboot.entities.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {

}
