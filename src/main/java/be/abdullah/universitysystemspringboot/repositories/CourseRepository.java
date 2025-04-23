package be.abdullah.universitysystemspringboot.repositories;

import be.abdullah.universitysystemspringboot.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}