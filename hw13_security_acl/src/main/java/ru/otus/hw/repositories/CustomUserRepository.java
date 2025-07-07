package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import ru.otus.hw.models.CustomUser;
import java.util.Optional;

public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {

    @EntityGraph(attributePaths = {"authorities"})
    Optional<CustomUser> findByUsername(String username);
}

