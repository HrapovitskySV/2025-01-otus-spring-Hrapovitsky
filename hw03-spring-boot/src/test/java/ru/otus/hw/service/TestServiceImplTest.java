package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.dto.QuestionToViewConverter;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class TestServiceImplTest {

    private TestServiceImpl testServiceImpl;

    private LocalizedIOService ioService;

    private QuestionDao csvQuestionDao;

    private QuestionToViewConverter questionToViewConverter;

    @BeforeEach
    void setUp() {

        ioService = mock(LocalizedIOService.class);
        csvQuestionDao = mock(QuestionDao.class);
        questionToViewConverter = mock(QuestionToViewConverter.class);
        testServiceImpl = new TestServiceImpl(ioService, csvQuestionDao, questionToViewConverter);
    }

    @DisplayName("executeTestFor")
    @Test
    void executeTestForTest()  {

        List<Question> questionList = new ArrayList<>();
        List<Answer> answerList = new ArrayList<>();
        Answer answer1 = new Answer("Ответ1", true);
        answerList.add(answer1);
        Answer answer2 = new Answer("Ответ3", true);
        answerList.add(answer2);
        Question question = new Question("Вопрос",answerList);
        questionList.add(question);
        given(csvQuestionDao.findAll()).willReturn(questionList);

        given(ioService.readIntForRangeWithPromptLocalized(1,2,
                "TestService.enter.the.response.number",
                "TestService.error.entering.number")).willReturn(1);

        Student student=new Student("Sergey","Khrapovitsky");
        testServiceImpl.executeTestFor(student);

        InOrder inOrder = Mockito.inOrder(ioService);
        inOrder.verify(ioService).printLine("");
        inOrder.verify(ioService).printLineLocalized("TestService.answer.the.questions");
        verify(questionToViewConverter, times(1)).convertToView(question);

        verify(csvQuestionDao, times(1)).findAll();
    }
}