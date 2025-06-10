package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;


    @GetMapping("/authors/")
    public String listAllAuthors(Model model) {
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "authorList";
    }

    @GetMapping("/authors/edit/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        Author author = authorService.findById(id).orElseThrow(AuthorNotFoundException::new);
        model.addAttribute("author", author);
        return "authorEdit";
    }

    @PostMapping("/authors/edit")
    public String saveAuthor(Author author) {
        authorService.save(author);
        return "redirect:/authors/";
    }

    @GetMapping("/authors/insert")
    public String insertAuthor(Model model) {
        Author author = new Author();
        model.addAttribute("author", author);
        return "authorEdit";
    }

    @PostMapping("/authors/delete/{id}")
    public String deletePage(@PathVariable("id") long id) {
        authorService.deleteById(id);
        return "redirect:/";
    }
}
