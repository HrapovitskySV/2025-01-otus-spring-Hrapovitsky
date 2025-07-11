package ru.otus.hw.indicators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.AuthorRepository;


@Component
@RequiredArgsConstructor
public class CustomHealthIndicator implements HealthIndicator {

    private final AuthorRepository authorRepository;

    @Override
    public Health health() {
        var countAuthor = authorRepository.count();

        boolean isEven = ((countAuthor % 2) == 0);
        if (isEven) {
            return Health.down().withDetail("Не люблю четное количество авторов ", 1).build();
        }
        return Health.up().build();
    }

}