package ru.otus.hw.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@Data
@RequiredArgsConstructor
@PropertySource(value = "classpath:application.properties")
public class AppProperties implements TestConfig, TestFileNameProvider {

    @Value(value = "$(test.rightAnswersCountToPass)")
    private int rightAnswersCountToPass;

    @Value(value = "$(test.fileName)")
    private String testFileName;
}