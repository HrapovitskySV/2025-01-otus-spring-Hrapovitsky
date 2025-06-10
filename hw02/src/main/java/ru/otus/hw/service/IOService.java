package ru.otus.hw.service;

import org.springframework.stereotype.Service;

@Service
public interface IOService {
    void printLine(String s);

    void printFormattedLine(String s, Object ...args);

    String readStringWithPrompt(String prompt);

    int readIntWithPrompt(String prompt);
}
