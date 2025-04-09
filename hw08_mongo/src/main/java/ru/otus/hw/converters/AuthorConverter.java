package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;

import java.util.Objects;

@Component
public class AuthorConverter {
    public String authorToString(Author author) {
        if (Objects.isNull(author)) {
            return "";
        }
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }

}
