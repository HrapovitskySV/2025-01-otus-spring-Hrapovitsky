package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.LocalizedIOService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest()
//@RequiredArgsConstructor
class CsvQuestionDaoTest {

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @MockitoBean
    private AppProperties fileNameProvider;


    @DisplayName("Test on QuestionReadException")
    @Test
    void findAllErrorFile() {
        given(fileNameProvider.getTestFileName()).willReturn("questions_test2.csv");



        Throwable exception = assertThrows(QuestionReadException.class, 	() -> {
            csvQuestionDao.findAll();
        });
        assertEquals("Error working with the question file. InputStream is NULL.", exception.getMessage());
        verify(fileNameProvider, times(1)).getTestFileName();
    }

    @DisplayName("Test on count Questions")
    @Test
    void findAllCountQuestions() {
        given(fileNameProvider.getTestFileName()).willReturn("questions_test.csv");

        List<Question> questionList = csvQuestionDao.findAll();
        assertEquals(4, questionList.size());
    }
}