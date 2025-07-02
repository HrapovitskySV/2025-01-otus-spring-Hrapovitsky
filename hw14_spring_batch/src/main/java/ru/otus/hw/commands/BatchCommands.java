package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;


import java.util.Properties;

import static java.time.LocalTime.now;
import static ru.otus.hw.config.JobConfig.IMPORT_BOOK_JOB_NAME;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    //private final AppProps appProps;
    private final Job importUserJob;

    private final JobLauncher jobLauncher;
    private final JobOperator jobOperator;
    private final JobExplorer jobExplorer;

    //http://localhost:8080/h2-console/

/*
    @SuppressWarnings("unused")
    @ShellMethod(value = "startMigrationJobWithJobLauncher", key = "sm-jl")
    public void startMigrationJobWithJobLauncher() throws Exception {
        JobExecution execution = jobLauncher.run(importUserJob, new JobParametersBuilder()
                .addString(INPUT_FILE_NAME, appProps.getInputFile())
                .addString(OUTPUT_FILE_NAME, appProps.getOutputFile())
                .toJobParameters());
        System.out.println(execution);
    }

 */

    @SuppressWarnings("unused")
    @ShellMethod(value = "startMigrationJobWithJobOperator", key = "c")
    public void startMigrationJobWithJobOperator() throws Exception {
        Properties properties = new Properties();
        properties.put("key", Integer.toString(now().getNano()));
        //properties.put(INPUT_FILE_NAME, appProps.getInputFile());
        //properties.put(OUTPUT_FILE_NAME, appProps.getOutputFile());

        Long executionId = jobOperator.start(IMPORT_BOOK_JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "showInfo", key = "i")
    public void showInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(IMPORT_BOOK_JOB_NAME));
    }
}
