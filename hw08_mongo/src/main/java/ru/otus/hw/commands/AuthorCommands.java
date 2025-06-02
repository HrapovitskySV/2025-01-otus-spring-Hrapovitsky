package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.services.AuthorService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class AuthorCommands {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @ShellMethod(value = "Find all authors", key = "aa")
    public String findAllAuthors() {
        return authorService.findAll().stream()
                .map(authorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert author", key = "afc")
    public String findByNameOrCreateAuthors(String name) {
        var savedAuthor = authorService.findByNameOrCreate(name);
        return authorConverter.authorToString(savedAuthor);
    }

    @ShellMethod(value = "Update author", key = "aupd")
    public String updateAuthor(String id, String name) {
        var savedAuthor = authorService.save(id, name);
        return authorConverter.authorToString(savedAuthor);
    }

    @ShellMethod(value = "Delete author", key = "adel")
    public void deleteAuthor(String id) {
        authorService.deleteById(id);
    }
}
