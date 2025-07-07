package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

@Controller
@RequiredArgsConstructor
public class AuthorPagesController {

    private final AuthorRepository authorRepository;

    @GetMapping("/authors/")
    public String listAuthorsPage() {
        return "authorList";
    }


    @GetMapping("/authors/add")

     public Mono<Rendering> insertAuthor(Model model) {
        Author author = new Author();
        return Mono.just(Rendering.view("authorEdit.html")
                .modelAttribute("author", author)
                .modelAttribute("method", "POST")
                .modelAttribute("redirectUrl", "/authors/")
                .build());
    }

    @GetMapping("/authors/edit/{id}")
    public Mono<Rendering> editPage(@PathVariable("id") String id, final Model model) {
        var author = authorRepository.findById(id);

        return Mono.just(Rendering.view("authorEdit")
                .modelAttribute("author", author)
                .modelAttribute("method", "PUT")
                .modelAttribute("redirectUrl", "/authors/")
                .build());
    }
}
