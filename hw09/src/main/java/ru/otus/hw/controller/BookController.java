package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.BookDto;
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
    public String listAllAuthors(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);

        model.addAttribute("bookConverter", bookConverter);

        return "bookList";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
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
        Book book = new Book();
        book.setGenres(new ArrayList<Genre>());

        model.addAttribute("book", book);
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);


        return "bookEdit";
    }

    @GetMapping("/delete")
    public String deletePage(@RequestParam("id") long id, Model model) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
