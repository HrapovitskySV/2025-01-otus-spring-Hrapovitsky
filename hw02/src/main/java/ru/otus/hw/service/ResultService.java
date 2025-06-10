package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.TestResult;

@Service
public interface ResultService {
    void showResult(TestResult testResult);
}
