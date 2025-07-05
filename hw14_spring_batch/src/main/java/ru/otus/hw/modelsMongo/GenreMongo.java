package ru.otus.hw.modelsMongo;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "genres")
@AllArgsConstructor
@NoArgsConstructor
public class GenreMongo {
    @Id
    private String id;

    @Field(name = "name")
    private String name;
}
