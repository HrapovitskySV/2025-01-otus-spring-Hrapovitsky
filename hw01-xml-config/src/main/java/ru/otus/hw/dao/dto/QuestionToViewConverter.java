package ru.otus.hw.dao.dto;

import ru.otus.hw.domain.Question;

public interface QuestionToViewConverter {
    public String convertToView(Question q);
}
