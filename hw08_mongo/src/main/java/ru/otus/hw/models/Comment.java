package ru.otus.hw.models;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "comments")
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    private String id;

    @DBRef
    private Book book;

    @Field(name = "comment")
    private String comment;
}
