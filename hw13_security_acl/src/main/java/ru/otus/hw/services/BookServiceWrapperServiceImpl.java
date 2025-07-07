package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.models.dto.BookDto;

import java.util.List;

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
}
