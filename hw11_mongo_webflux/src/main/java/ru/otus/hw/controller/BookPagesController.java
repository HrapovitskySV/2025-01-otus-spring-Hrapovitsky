package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookPagesController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String listAllBooks(Model model) {
        return "bookList";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") String id, Model model) {
        Book book = bookService.findById(id).orElseThrow(BookNotFoundException::new);
        model.addAttribute("book", book);
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);

        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        model.addAttribute("method", "PUT");
        model.addAttribute("redirectUrl", "../");


        return "bookEdit";
    }


    @GetMapping("/add")
    public String insertBook(Model model) {
        Book book = new Book(null,null,null,new ArrayList<Genre>());

        model.addAttribute("book", book);
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);

        model.addAttribute("method", "POST");
        model.addAttribute("redirectUrl", "./");

        return "bookEdit";
    }
}
