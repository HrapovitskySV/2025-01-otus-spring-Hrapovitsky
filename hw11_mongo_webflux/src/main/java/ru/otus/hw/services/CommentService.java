package ru.otus.hw.services;

import reactor.core.publisher.Mono;
import ru.otus.hw.models.Comment;


public interface CommentService {

    Mono<Comment> save(String id, String commentText, String bookId);

}
