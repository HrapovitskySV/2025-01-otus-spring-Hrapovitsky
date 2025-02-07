package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class StreamsIOServiceTest {

    private StreamsIOService streamsIOService;
    //private PrintStream printStream;
    private ByteArrayOutputStream bo;

    @BeforeEach
    void setUp() {
        //printStream = mock(System.out);
        //printStream = System.out;
        //Redirect System.out to buffer
        bo = new ByteArrayOutputStream();
        //System.setOut(new PrintStream(bo));
        streamsIOService= new StreamsIOService(new PrintStream(bo));

    }

    @DisplayName("printLine")
    @Test
    void printLineTest() throws IOException {
        streamsIOService.printLine("test printLine");
        bo.flush();
        String allWrittenLines = bo.toString();
        assertTrue(allWrittenLines.contains("test printLine"));


    }

    @DisplayName("printFormattedLine")
    @Test
    void printFormattedLineTest() throws IOException {
        //System.out.printf("test printFormattedLine %n", "rr");
        streamsIOService.printFormattedLine("test printFormattedLine", 10);
        bo.flush();
        String allWrittenLines = bo.toString();
        assertTrue(allWrittenLines.contains("test printFormattedLine"));
    }

}