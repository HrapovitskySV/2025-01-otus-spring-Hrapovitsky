package ru.otus.hw.models;

import lombok.Data;

@Data
public class Album {

    private Integer id;
    private Integer userId;
    private String title;

    // standard getters and setters
}
