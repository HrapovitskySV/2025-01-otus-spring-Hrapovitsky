package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        List<Question> questions;
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName());
            CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder<QuestionDto>(new InputStreamReader(is))
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build();
            questions = csvToBean.stream().map(QuestionDto::toDomainObject).toList();

            is.close();
        } catch (IOException e) {
            throw new QuestionReadException("Error working with the question file.", e);
        }

        return questions;
    }
}
