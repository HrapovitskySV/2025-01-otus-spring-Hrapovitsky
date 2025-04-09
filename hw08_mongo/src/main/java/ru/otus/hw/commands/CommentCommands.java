package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.math.BigInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comments by book", key = "cbb")
    public String findCommentsByBook(BigInteger bookId) {
        return commentService.findByBookId(bookId).stream()
                .map(commentConverter::commentDtoToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find comments by id", key = "cbi")
    public String findCommentById(BigInteger id) {
        return commentService.findById(id).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find all comments", key = "ac")
    public String findAllComment() {
        return commentService.findAll().stream()
                .map(commentConverter::commentDtoToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String name, BigInteger bookId) {
        var savedComment = commentService.insert(name, bookId);
        return commentConverter.commentToString(savedComment);
    }

    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteComment(BigInteger id) {
        commentService.deleteById(id);
    }

    @ShellMethod(value = "Delete comment by book id", key = "cbdel")
    public void deleteCommentsByBook(BigInteger bookId) {
        commentService.deleteByBookId(bookId);
    }
}
