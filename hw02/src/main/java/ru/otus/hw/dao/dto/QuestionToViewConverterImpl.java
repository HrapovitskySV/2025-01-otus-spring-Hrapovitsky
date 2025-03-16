package ru.otus.hw.dao.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@AllArgsConstructor
@Component
public class QuestionToViewConverterImpl implements QuestionToViewConverter {


    public String convertToView(Question q) {
        StringBuilder stringBuilder = new StringBuilder(q.text());
        stringBuilder.append("\n");
        var answers = q.answers();
        int num=1;
        if (answers != null) {
            for (Answer a : answers) {
                stringBuilder.append(String.join(" ", "    ",String.valueOf(num), a.text(), "\n"));
                num++;
            }
        }
        return stringBuilder.toString();
    }
}
