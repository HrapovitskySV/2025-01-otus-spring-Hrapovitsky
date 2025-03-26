package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import static org.mockito.Mockito.mock;

class ResultServiceImplTest {
    private TestServiceImpl testServiceImpl;

    private IOService ioService;

    private ResultServiceImpl resultServiceImpl;

    //private QuestionToViewConverter questionToViewConverter;

    @BeforeEach
    void setUp() {
        AppProperties appProperties = new AppProperties(3, "questions_test.csv");
        ioService = mock(IOService.class);
        resultServiceImpl = new ResultServiceImpl(appProperties, ioService);
    }

    @DisplayName("showResult")
    @Test
    void showResultTest()  {

        Student student = new Student("Sergey","Khrapovitsky");
        TestResult testResult = new TestResult(student);
        testResult.setRightAnswersCount(3);

        resultServiceImpl.showResult(testResult);

        InOrder inOrder = Mockito.inOrder(ioService);
        inOrder.verify(ioService).printLine("");
        inOrder.verify(ioService).printLine("Test results: ");
        inOrder.verify(ioService).printFormattedLine("Student: %s", student.getFullName());
        inOrder.verify(ioService).printFormattedLine("Answered questions count: %d",0);
        inOrder.verify(ioService).printFormattedLine("Right answers count: %d", 3);
    }
}