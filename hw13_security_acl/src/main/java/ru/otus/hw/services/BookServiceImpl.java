package ru.otus.hw.services;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    private final AclServiceWrapperService aclServiceWrapperService;

    @Autowired
    //делаю самоинжекцию, поэтому не через конструктор. Самоинжекция нужна, чтобы работали директивы через проксирование
    private  BookService bookService;

    @Override
    @PostAuthorize("returnObject.isPresent() ? hasPermission(returnObject.get(), 'READ') : true")
    @Transactional(readOnly = true)
    public Optional<Book> findById(long id) {
         return bookRepository.findById(id);
    }

    @Override
    @PostFilter("hasPermission(filterObject, 'READ')")
    @Transactional(readOnly = true)
    public List<Book> findAllBook() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        var books = bookService.findAllBook();
        return books.stream().map(bookConverter::toDto).toList();

    }

    @Override
    @Transactional()
    public Book insert(String title, long authorId, Set<Long> genresIds) {
        var savedBook = save(0, title, authorId, genresIds);

        aclServiceWrapperService.createPermission(savedBook,
                List.of(BasePermission.READ,BasePermission.WRITE,BasePermission.DELETE));

        return savedBook;
    }

    @Override
    @Transactional()
    @PreAuthorize("canUpdate(#id, Book.class)")
    public Book update(@Param("id")long id, String title, long authorId, Set<Long> genresIds) {
        return save(id, title, authorId, genresIds);
    }


    @Override
    @Transactional()
    @PreAuthorize("hasPermission(#book, 'DELETE')")
    public void delete(@Param("book")Book book) {
        bookRepository.delete(book);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasPermission(#id, Book.class, 'DELETE')")
    public void deleteById(@Param("id")long id) {
        bookRepository.deleteById(id);
    }


    @Override
    @PreAuthorize("hasPermission(#book, 'WRITE')")
    public Book save(@Param("book")Book book) {
        return bookRepository.save(book);
    }


    @Override
    //@PreAuthorize("hasPermission(#book, 'CREATE')") // не понимаю как даются права на создание нового объекта,
    // наверное эо право только для отнятия, такая возможность тоже есть
    public Book create(@Param("book")Book book) {
        return bookRepository.save(book);
    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findAllByIds(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book(id, title, author, genres);
        return save(book);
    }
}
