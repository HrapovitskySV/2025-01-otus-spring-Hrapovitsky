package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.models.dto.BookDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceWrapperServiceImpl implements  BookServiceWrapperService {

    private final BookService bookService;

    private final BookConverter bookConverter;

    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        var books = bookService.findAll();
        return books.stream().map(bookConverter::toDto).toList();
    }

    @Override
    public Optional<BookDto> findById(long id) {
        var book = bookService.findById(id);
        return book.map(bookConverter::toDto);
    }

    @Override
    public BookDto insert(String title, long authorId, Set<Long> genresIds) {
        var book = bookService.insert(title, authorId, genresIds);
        return bookConverter.toDto(book);
    }

    @Override
    public BookDto update(long id, String title, long authorId, Set<Long> genresIds) {
        var book = bookService.update(id, title, authorId, genresIds);
        return bookConverter.toDto(book);
    }

    @Override
    public void deleteById(long id) {
        bookService.deleteById(id);
    }
}
