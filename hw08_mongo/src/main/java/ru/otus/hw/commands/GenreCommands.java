package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.services.GenreService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class GenreCommands {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @ShellMethod(value = "Find all genres", key = "ag")
    public String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert genre", key = "gfc")
    public String findByNameOrCreateGenre(String name) {
        var savedGenre = genreService.findByNameOrCreate(name);
        return genreConverter.genreToString(savedGenre);
    }


    @ShellMethod(value = "Update genre", key = "gupd")
    public String updateGenre(String id, String name) {
        var savedGenre = genreService.save(id, name);
        return genreConverter.genreToString(savedGenre);
    }

    @ShellMethod(value = "Delete genre", key = "gdel")
    public void deleteGenre(String id) {
        genreService.deleteById(id);
    }
}
