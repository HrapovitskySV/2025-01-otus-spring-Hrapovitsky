package ru.otus.hw.dao.dto;

import lombok.AllArgsConstructor;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.TestRunnerService;

@AllArgsConstructor
public class QuestionToViewConverterImpl implements QuestionToViewConverter {


    public String convertToView(Question q) {
        StringBuilder stringBuilder = new StringBuilder(q.text());
        var answers = q.answers();
        if (answers != null) {
            for (Answer a : answers) {
                stringBuilder.append(String.join(" ", "    ", a.text(), Boolean.toString(a.isCorrect())));
            }
        }
        return stringBuilder.toString();
    }
}
