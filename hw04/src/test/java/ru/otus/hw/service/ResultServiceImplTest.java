package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import static org.mockito.BDDMockito.given;

@SpringBootTest()
class ResultServiceImplTest {
    @MockitoBean
    private LocalizedIOService ioService;

    @Autowired
    private ResultServiceImpl resultServiceImpl;

    @MockitoBean
    private AppProperties appProperties;




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