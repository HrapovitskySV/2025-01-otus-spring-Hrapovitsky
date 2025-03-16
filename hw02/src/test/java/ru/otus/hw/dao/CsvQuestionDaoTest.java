package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class CsvQuestionDaoTest {

    private CsvQuestionDao csvQuestionDao;
    private TestFileNameProvider fileNameProvider;

    @BeforeEach
    void setUp() {
        fileNameProvider = mock(TestFileNameProvider.class);
        csvQuestionDao= new CsvQuestionDao(fileNameProvider);
    }

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