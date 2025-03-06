package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.dto.QuestionToViewConverter;
import ru.otus.hw.dao.dto.QuestionToViewConverterImpl;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;


import java.util.ArrayList;
import java.util.List;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class TestServiceImplTest {
    private TestServiceImpl testServiceImpl;
    private IOService ioService;
    private QuestionDao csvQuestionDao;
    private QuestionToViewConverter questionToViewConverter;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
        csvQuestionDao = mock(QuestionDao.class);
        questionToViewConverter = new QuestionToViewConverterImpl();
        testServiceImpl= new TestServiceImpl(ioService, csvQuestionDao, questionToViewConverter);
    }

    @DisplayName("executeTest")
    @Test
    void executeTestTest()  {
        List<Question> questionList = new ArrayList<>();
        List<Answer> answerList = new ArrayList<>();
        Answer answer1=new Answer("Ответ1", true);
        answerList.add(answer1);
        Answer answer2=new Answer("Ответ3", true);
        answerList.add(answer2);
        Question question=new Question("Вопрос",answerList);
        questionList.add(question);

        given(csvQuestionDao.findAll()).willReturn(questionList);

        testServiceImpl.executeTest();
        InOrder inOrder = Mockito.inOrder(ioService);
        inOrder.verify(ioService).printLine("");
        inOrder.verify(ioService).printFormattedLine("Please answer the questions below%n");
        inOrder.verify(ioService).printLine(questionToViewConverter.convertToView(question));

        verify(csvQuestionDao, times(1)).findAll();
    }
}