package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.modelsMongo.AuthorMongo;
import ru.otus.hw.modelsMongo.GenreMongo;
import ru.otus.hw.modelsMongo.dto.GenreMongoDto;

import java.util.Objects;

@Component
public class AuthorConverter {
    public String authorToString(AuthorMongo author) {
        if (Objects.isNull(author)) {
            return "";
        }
        return "Id: %s, FullName: %s".formatted(author.getId(), author.getFullName());
    }

    @Component
    public static class GenreConverter {
        public String genreToString(GenreMongo genre) {
            return "Id: %s, Name: %s".formatted(genre.getId(), genre.getName());
        }

        public String genreDtoToString(GenreMongoDto genre) {
            return "Id: %s, Name: %s".formatted(genre.getId(), genre.getName());
        }

        public GenreMongoDto toDto(GenreMongo genre) {
            var genreDto = new GenreMongoDto();
            genreDto.setId(genre.getId());
            genreDto.setName(genre.getName());

            return genreDto;
        }
    }
}
