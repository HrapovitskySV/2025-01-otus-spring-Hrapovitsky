package ru.otus.hw.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
public class StreamsIOService implements IOService {

    private final PrintStream printStream;

    private final Scanner scanner;

    public StreamsIOService(@Value("#{T(java.lang.System).out}") PrintStream printStream,
                            @Value("#{T(java.lang.System).in}") InputStream inputStream) {
        this.printStream = printStream;
        this.scanner = new Scanner(inputStream);
    }



    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        printLine(prompt);
        return scanner.nextLine();
    }

    public int readIntWithPrompt(String prompt) {
        int i;
        printLine(prompt);
        while (true) {
            try {
                String s = scanner.nextLine();
                i = Integer.parseInt(s);
                break;
            } catch (NumberFormatException e) {
                printLine("Error entering a number.");
            }
        }
        return i;
    }
}
