package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CleanUpService {
    //private static final Logger log = LoggerFactory.getLogger(CleanUpService.class);

    private final JdbcTemplate jdbcTemplate;

    @SuppressWarnings("unused")
    public void cleanUp() throws Exception {
        log.info("Выполняю завершающие мероприятия...");
        Thread.sleep(1000);
        jdbcTemplate
                .query("SELECT count(*) as c FROM authors", new CountMapper())
                .forEach(count -> log.info("Found <{}> authors in the database.", count));
        log.info("Завершающие мероприятия закончены");
    }
}
