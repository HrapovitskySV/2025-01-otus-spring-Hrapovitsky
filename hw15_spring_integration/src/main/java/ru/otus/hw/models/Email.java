package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Email {

    private String from;

    private String title;

    private String message;

}
