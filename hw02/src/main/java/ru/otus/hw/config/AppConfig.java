package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.PrintStream;
import java.util.Scanner;


@Configuration
class AppConfig {

    @Bean
    public static AppProperties getAppProperties() {
        return new AppProperties();
    }

    @Bean
    public static PrintStream getPrintStream() {
        return System.out;
    }

    @Bean
    public static Scanner getScanner() {
        return new Scanner(System.in);
    }
}
