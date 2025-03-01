package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    @Override
    public void run() {
         testService.executeTest();
    }
}
