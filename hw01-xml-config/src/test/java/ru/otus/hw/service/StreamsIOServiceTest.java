package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StreamsIOServiceTest {

    //private StreamsIOService streamsIOService2;
    //private PrintStream printStream;


    @DisplayName("printLine1")
    @Test
    void printLineTest1() throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        StreamsIOService streamsIOService1= new StreamsIOService(new PrintStream(bo));
        streamsIOService1.printLine("test printLine");
        bo.flush();
        String allWrittenLines = bo.toString();
        assertTrue(allWrittenLines.contains("test printLine"));
    }

    @DisplayName("printFormattedLine")
    @Test
    void printFormattedLineTest1() throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        StreamsIOService streamsIOService1= new StreamsIOService(new PrintStream(bo));
        streamsIOService1.printFormattedLine("test printFormattedLine", 10);
        bo.flush();
        String allWrittenLines = bo.toString();
        assertTrue(allWrittenLines.contains("test printFormattedLine"));
    }
/*
    @BeforeEach
    void setUp() {
        printStream = mock(PrintStream.class);
        streamsIOService2= new StreamsIOService(printStream);
    }


    @DisplayName("printLine")
    @Test
    void printLineTest2() throws IOException {
        streamsIOService2.printLine("test printLine");
        verify(printStream, times(1)).println("test printLine");
    }

    @DisplayName("printFormattedLine")
    @Test
    void printFormattedLineTest2() throws IOException {
        //System.out.printf("test printFormattedLine %n", "rr");
        streamsIOService2.printFormattedLine("test printFormattedLine", 10);
        verify(printStream, times(1)).printf("test printFormattedLine%n",10);
    }
*/

}