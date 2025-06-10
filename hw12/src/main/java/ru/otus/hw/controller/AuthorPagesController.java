package ru.otus.hw.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.Arrays;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class AuthorPagesController {

    private final AuthorService authorService;

    //private HttpServletRequest request;

    //private HttpServletResponse response;

    @GetMapping("/authenticated/authors/")
    public String listAuthorsPage() {
        return "authorList";
    }


    @GetMapping("/authenticated/authors/add")
    public String insertAuthor(Model model, HttpServletRequest request) {
        addSessionId(model, request);

        Author author = new Author();
        model.addAttribute("author", author);
        model.addAttribute("method", "POST");
        model.addAttribute("redirectUrl", "./");
        return "authorEdit";
    }

    public void addSessionId(Model model, HttpServletRequest request) {
        var cookies = request.getCookies();
        if (!Objects.isNull(cookies)) {
            var cookieJsessionId = Arrays.stream(cookies).
                    filter(cookie -> cookie.getName().equals("JSESSIONID")).findFirst();
            if (cookieJsessionId.isPresent()) {
                model.addAttribute("JSESSIONID", cookieJsessionId.get().getValue());
            }
        }
    }

    @GetMapping("/authenticated/authors/edit/{id}")
    public String editPage(@PathVariable("id") long id, Model model, HttpServletRequest request) {
        addSessionId(model, request);

        Author author = authorService.findById(id).orElseThrow(AuthorNotFoundException::new);
        model.addAttribute("author", author);
        model.addAttribute("method", "PUT");
        model.addAttribute("redirectUrl", "../");
        return "authorEdit";
    }
}
