package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.dto.QuestionToViewConverter;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao csvQuestionDao;

    private final QuestionToViewConverter questionToViewConverter;



    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        try {
            List<Question> questionList = csvQuestionDao.findAll();
            for (Question q : questionList) {
                ioService.printLine(questionToViewConverter.convertToView(q));
            }
        } catch (QuestionReadException e) {
            ioService.printLine("Couldn't read the questions.");
        }
    }
}
