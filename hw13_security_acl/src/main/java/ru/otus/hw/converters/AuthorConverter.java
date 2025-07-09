package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.dto.AuthorDto;

import java.util.Objects;

@Component
public class AuthorConverter {
    public String authorToString(Author author) {
        if (Objects.isNull(author)) {
            return "";
        }
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }

    public String authorDtoToString(AuthorDto author) {
        if (Objects.isNull(author)) {
               return "";
        }
            return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }

    public AuthorDto toDto(Author author) {
        if (Objects.isNull(author)) {
            return null;
        }
        var authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setFullName(author.getFullName());

        return authorDto;
    }
}
