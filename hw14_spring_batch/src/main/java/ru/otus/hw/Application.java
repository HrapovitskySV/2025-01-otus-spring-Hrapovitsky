package ru.otus.hw;

import org.h2.tools.Console;
import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.sql.SQLException;

@EnableMongock
@EnableMongoRepositories
@SpringBootApplication
public class Application {
	// --spring.shell.interactive.enabled=false --spring.batch.job.enabled=true inputFileName=entries.csv outputFileName=output_new.dat
	public static void main(String[] args) throws SQLException {
		SpringApplication.run(Application.class, args);
		Console.main(args);
	}

}
