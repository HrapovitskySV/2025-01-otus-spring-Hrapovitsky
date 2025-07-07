package ru.otus.hw.services;

import ru.otus.hw.models.dto.BookDto;

import java.util.List;

public interface BookServiceWrapperService {

    List<BookDto> findAll();

}
