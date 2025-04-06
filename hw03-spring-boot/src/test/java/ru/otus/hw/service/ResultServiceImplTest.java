package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ResultServiceImplTest {
    private TestServiceImpl testServiceImpl;

    private LocalizedIOService ioService;

    private ResultServiceImpl resultServiceImpl;

    private AppProperties appProperties;

    @BeforeEach
    void setUp() {
        appProperties = mock(AppProperties.class);
        ioService = mock(LocalizedIOService.class);
        resultServiceImpl = new ResultServiceImpl(appProperties, ioService);
    }

    @DisplayName("showResult")
    @Test
    void showResultTest()  {
        given(appProperties.getRightAnswersCountToPass()).willReturn(3);
        given(appProperties.getTestFileName()).willReturn("questions_test.csv");

        Student student=new Student("Sergey","Khrapovitsky");
        TestResult testResult = new TestResult(student);
        testResult.setRightAnswersCount(3);

        resultServiceImpl.showResult(testResult);

        InOrder inOrder = Mockito.inOrder(ioService);
        inOrder.verify(ioService).printLine("");
        inOrder.verify(ioService).printLineLocalized("ResultService.test.results");
        inOrder.verify(ioService).printFormattedLineLocalized("ResultService.student", student.getFullName());
        inOrder.verify(ioService).printFormattedLineLocalized("ResultService.answered.questions.count",0);
        inOrder.verify(ioService).printFormattedLineLocalized("ResultService.right.answers.count", 3);
    }
}