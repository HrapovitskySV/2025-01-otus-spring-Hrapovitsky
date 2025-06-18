package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.CustomUser;
import java.util.Optional;

public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {

    @EntityGraph(attributePaths = {"roles"})
    Optional<CustomUser> findByUsername(String username);
}

