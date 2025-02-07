package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;


    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/

        InputStream is = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName());
        CsvToBean<QuestionDto> csvToBean=new CsvToBeanBuilder<QuestionDto>(new InputStreamReader(is)).withType(QuestionDto.class).withSkipLines(1).withSeparator(';').build();
        List<Question> questions = csvToBean.stream().map(QuestionDto::toDomainObject).toList();
        //List<Question> questions = csvToBean.iterator();
        //List<Question> questions = new ArrayList<Question>();
        //QuestionDto questionDto= csvToBean.iterator().next();
        //questions.add(questionDto.toDomainObject());



        return questions;
    }
}
