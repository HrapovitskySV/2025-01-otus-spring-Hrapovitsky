package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class BookPagesController {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;


    @GetMapping("/")
    public String listAllBooks(Model model) {
        return "bookList";
    }

    @GetMapping("/edit/{id}")
    public Mono<Rendering> editPage(@PathVariable("id") String id, Model model) {

        var book = bookRepository.findById(id);
        var authors = authorRepository.findAll();
        var genres = genreRepository.findAll();

        return Mono.just(Rendering.view("bookEdit")
                .modelAttribute("book", book)
                .modelAttribute("authors", authors)
                .modelAttribute("genres", genres)
                .modelAttribute("method", "PUT")
                .modelAttribute("redirectUrl", "../")
                .build());
    }


    @GetMapping("/add")
    public Mono<Rendering> insertBook(Model model) {
        Book book = new Book(null,null,new Author("",""),new ArrayList<Genre>());
        var authors = authorRepository.findAll();
        var genres = genreRepository.findAll();

        return Mono.just(Rendering.view("bookEdit")
                .modelAttribute("book", book)
                .modelAttribute("authors", authors)
                .modelAttribute("genres", genres)
                .modelAttribute("method", "POST")
                .modelAttribute("redirectUrl", "../")
                .build());
    }
}
