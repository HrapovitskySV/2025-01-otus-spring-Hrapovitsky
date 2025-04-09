package ru.otus.hw.models.dto;

import lombok.Data;
import ru.otus.hw.models.Book;

import java.math.BigInteger;

@Data
public class CommentDto {
    private BigInteger id;

    private Book book;

    private String comment;
}
