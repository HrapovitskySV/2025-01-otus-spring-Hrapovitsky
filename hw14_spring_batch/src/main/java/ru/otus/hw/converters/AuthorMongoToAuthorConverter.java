package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.modelsJpa.Author;
import ru.otus.hw.modelsJpa.dto.AuthorDto;
import ru.otus.hw.modelsMongo.AuthorMongo;

import java.util.Objects;

@Component
public class AuthorMongoToAuthorConverter {
    public Author convert(AuthorMongo authorMongo){
        return new Author(0,authorMongo.getFullName());
    }
}
