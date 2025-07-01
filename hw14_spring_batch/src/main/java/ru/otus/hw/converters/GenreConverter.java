package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.modelsJpa.Genre;
import ru.otus.hw.modelsJpa.dto.GenreDto;

import java.util.Objects;

@Component
public class GenreConverter {
    public String genreToString(Genre genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }

    public String genreDtoToString(GenreDto genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }

    public GenreDto toDto(Genre genre) {
        if (Objects.isNull(genre)) {
            return null;
        }

        var genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());

        return genreDto;
    }
}
