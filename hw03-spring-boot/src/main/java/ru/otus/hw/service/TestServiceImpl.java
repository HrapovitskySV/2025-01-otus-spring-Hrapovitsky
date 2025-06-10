package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.dto.QuestionToViewConverter;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final QuestionToViewConverter questionToViewConverter;


    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var q: questions) {
            ioService.printLine(questionToViewConverter.convertToView(q));

            int answerNumber = ioService.readIntForRangeWithPromptLocalized(1,
                                                                            q.answers().size(),
                                                                            "TestService.enter.the.response.number",
                                                                            "TestService.error.entering.number");
            testResult.applyAnswer(q, q.answers().get(answerNumber - 1).isCorrect());
        }
        return testResult;
    }

}
