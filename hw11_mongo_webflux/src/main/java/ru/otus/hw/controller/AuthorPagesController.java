package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.services.AuthorService;

@Controller
@RequiredArgsConstructor
public class AuthorPagesController {

    private final AuthorService authorService;

    private final AuthorRepository authorRepository;

    @GetMapping("/authors/")
    public String listAuthorsPage() {
        return "authorList";
    }


    @GetMapping("/authors/add")
    public String insertAuthor(Model model) {
        Author author = new Author();
        model.addAttribute("author", author);
        model.addAttribute("method", "POST");
        model.addAttribute("redirectUrl", "./");
        return "authorEdit";
    }

    @GetMapping("/authors/edit/{id}")
    public String editPage(@PathVariable("id") String id, Model model) {
        Mono<Author> author = authorRepository.findById(id);
                //.orElseThrow(AuthorNotFoundException::new);
        model.addAttribute("author", author);
        model.addAttribute("method", "PUT");
        model.addAttribute("redirectUrl", "../");
        return "authorEdit";
    }
}
