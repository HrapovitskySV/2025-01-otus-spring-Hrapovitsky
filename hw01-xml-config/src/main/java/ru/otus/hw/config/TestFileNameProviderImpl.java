package ru.otus.hw.config;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TestFileNameProviderImpl implements TestFileNameProvider {
    private final AppProperties appProperties;

    @Override
    public String getTestFileName() {
        return appProperties.getTestFileName();
    }
}
