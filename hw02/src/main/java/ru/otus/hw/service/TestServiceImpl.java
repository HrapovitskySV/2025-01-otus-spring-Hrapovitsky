package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.dto.QuestionToViewConverter;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.domain.TestResult;

import java.util.List;


@RequiredArgsConstructor
@Service
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

    @Override
    public TestResult executeTestFor(Student student) {
        var testResult = new TestResult(student);
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        try {
            List<Question> questionList = csvQuestionDao.findAll();
            for (Question q : questionList) {
                ioService.printLine(questionToViewConverter.convertToView(q));

                int answerNumber = ioService.readIntWithPrompt("Enter the response number");
                while (!(answerNumber > 0 && answerNumber <= q.answers().size())) {
                    ioService.printLine("Entered number is not exist.");
                    answerNumber = ioService.readIntWithPrompt("Enter the response number");
                }
                testResult.applyAnswer(q, q.answers().get(answerNumber-1).isCorrect());
            }
        } catch (QuestionReadException e) {
            ioService.printLine("Couldn't read the questions.");
        }

        return testResult;
    }
}
