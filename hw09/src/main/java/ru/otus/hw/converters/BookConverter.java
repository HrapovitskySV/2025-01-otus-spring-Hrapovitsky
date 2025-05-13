package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookDtoWeb;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public String bookGenresToString(Book book) {
        return book.getGenres().stream()
                .map(genreConverter::genreToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
    }

    public String bookGenresToString(BookDto book) {
        return book.getGenres().stream()
                .map(genreConverter::genreDtoToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
    }

    public String bookToString(Book book) {
        var genresString = bookGenresToString(book);

        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthor()),
                genresString);
    }

    public String bookDtoToString(BookDto book) {
        var genresString = book.getGenres().stream()
                .map(genreConverter::genreDtoToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorDtoToString(book.getAuthor()),
                genresString);
    }

    public BookDto toDto(Book book) {
        var bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(authorConverter.toDto(book.getAuthor()));
        bookDto.setGenres(book.getGenres().stream().map(genreConverter::toDto).toList());
        return bookDto;
    }

    public BookDtoWeb toDtoWeb(BookDto book) {
        var bookDtoWeb = new BookDtoWeb();
        bookDtoWeb.setId(book.getId());
        bookDtoWeb.setTitle(book.getTitle());
        bookDtoWeb.setAuthor(book.getAuthor() != null ? book.getAuthor().getFullName() : "");
        bookDtoWeb.setGenres(bookGenresToString(book));
        return bookDtoWeb;
    }
}
