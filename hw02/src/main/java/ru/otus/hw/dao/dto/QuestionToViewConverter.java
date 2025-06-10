package ru.otus.hw.dao.dto;

import ru.otus.hw.domain.Question;

public interface QuestionToViewConverter {
    String convertToView(Question q);
}
