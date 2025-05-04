package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@Controller
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;


    @GetMapping("/")
    public String listAllAuthors(Model model) {
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "authorList";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        Author author = authorService.findById(id).orElseThrow(AuthorNotFoundException::new);
        model.addAttribute("author", author);
        return "authorEdit";
    }

    @PostMapping("/edit")
    public String saveAuthor(Author author) {
        authorService.save(author);
        return "redirect:/authors/";
    }

    @GetMapping("/insert")
    public String insertAuthor(Model model) {
        Author author = new Author();
        model.addAttribute("author", author);
        return "authorEdit";
    }

    @GetMapping("/delete")
    public String deleteAuthor(@RequestParam("id") long id, Model model) {
        authorService.deleteById(id);
        return "redirect:/";
    }
}
