package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.result.view.Rendering;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
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
    /*
    public String insertAuthor(Model model) {
        Author author = new Author();
        model.addAttribute("author", author);
        model.addAttribute("method", "POST");
        model.addAttribute("redirectUrl", "./");
        return "authorEdit";

         не работает
         */

     public Mono<Rendering> insertAuthor(Model model) {
        Author author = new Author();
        return Mono.just(Rendering.view("authorEdit.html")
                .modelAttribute("author", author)
                .modelAttribute("method", "POST")
                .modelAttribute("redirectUrl", "../")
                .build());
    }

    @GetMapping("/authors/edit/{id}")
    public Mono<Rendering> editPage(@PathVariable("id") String id, final Model model) {
        var author = authorRepository.findById(id);

        return Mono.just(Rendering.view("authorEdit")
                .modelAttribute("author", author)
                .modelAttribute("method", "PUT")
                .modelAttribute("redirectUrl", "../")
                .build());
    }
}
