package ru.otus.hw.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

//@Data
//@Getter
@PropertySource("classpath:application.properties")
@Component
public class AppProperties implements TestConfig, TestFileNameProvider{

    @Value("${test.rightAnswersCountToPass}")
    //@Value("#{new Integer('${test.rightAnswersCountToPass}')}")
    private int rightAnswersCountToPass;

    //@Value("#{new String('$(test.fileName):questions.csv')}")
    @Value("${test.fileName}")
    private String testFileName;

    public int getRightAnswersCountToPass() {
        return this.rightAnswersCountToPass;
    }

    public String getTestFileName() {
        return this.testFileName;
    }
    public AppProperties(){}
}