package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookDtoWeb;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final BookConverter bookConverter;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String listAllBooks(Model model) {
        List<BookDto> books = bookService.findAll();
        List<BookDtoWeb> booksWeb = books.stream().map(bookDto -> bookConverter.toDtoWeb(bookDto)).toList();
        model.addAttribute("books", booksWeb);

        return "bookList";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(BookNotFoundException::new);
        model.addAttribute("book", book);
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);

        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);


        return "bookEdit";
    }

    @PostMapping("/edit")
    public String saveBook(Book book) {
        bookService.save(book);
        return "redirect:/";
    }

    @GetMapping("/insert")
    public String insertBook(Model model) {
        Book book = new Book(0,null,null,new ArrayList<Genre>());

        model.addAttribute("book", book);
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);


        return "bookEdit";
    }

    @PostMapping("/delete/{id}")
    public String deletePage(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
