package ru.otus.hw.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.services.CleanUpService;
import ru.otus.hw.converters.AuthorMongoToAuthorConverter;
import ru.otus.hw.modelsJpa.Author;
import ru.otus.hw.modelsMongo.AuthorMongo;

import java.util.List;
import java.util.Map;

import static org.reflections.util.ConfigurationBuilder.build;


@SuppressWarnings("unused")
@Configuration
@RequiredArgsConstructor
public class JobConfig {
    private static final int CHUNK_SIZE = 5;
    private final Logger logger = LoggerFactory.getLogger("Batch");

    @PersistenceContext
    private final EntityManager em;

    public static final String IMPORT_BOOK_JOB_NAME = "importBookJob";

    private final EntityManager entityManager;

    private final MongoTemplate mongoTemplate;

    private AuthorMongoToAuthorConverter authorMongoToAuthorConverter;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;


    @Autowired
    private CleanUpService cleanUpService;

    @StepScope
    @Bean
    public MongoPagingItemReader<AuthorMongo> readerAuthor() {
        return new MongoPagingItemReaderBuilder<AuthorMongo>()
                .name("authorItemReader")
                .collection("authors")
                .pageSize(10)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(AuthorMongo.class)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    /*
    @StepScope
    @Bean
    public ItemProcessor<Person, Person> processor(HappyBirthdayService happyBirthdayService) {
        return happyBirthdayService::doHappyBirthday;
    }
    */

    @StepScope
    @Bean
    public ItemProcessor<AuthorMongo, Author> processor(AuthorMongoToAuthorConverter authorMongoToAuthorConverter) {
        return authorMongoToAuthorConverter::convert;
    }


    @StepScope
    @Bean
    public JpaItemWriter<Author> writer() {
        return new JpaItemWriterBuilder<Author>()
                .entityManagerFactory(em.getEntityManagerFactory())
                .usePersist(true)
                .build();
    }

    @Bean
    public MethodInvokingTaskletAdapter cleanUpTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(cleanUpService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }


    @Bean
    public Job importBookJob(Step transformBooksStep, Step cleanUpStep) {
        return new JobBuilder(IMPORT_BOOK_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformBooksStep)
                .next(cleanUpStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }

    @Bean
    public Step transformBooksStep(MongoPagingItemReader<AuthorMongo> reader, JpaItemWriter<Author> writer,
                                     ItemProcessor<AuthorMongo, Author> itemProcessor) {
        return new StepBuilder("transformBooksStep", jobRepository)
                .<AuthorMongo, Author>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения");
                    }

                    public void afterRead(@NonNull AuthorMongo o) {
                        logger.info("Конец чтения");
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения");
                    }
                })
                .listener(new ItemWriteListener<Author>() {
                    public void beforeWrite(@NonNull List<Author> list) {
                        logger.info("Начало записи");
                    }

                    public void afterWrite(@NonNull List<Author> list) {
                        logger.info("Конец записи");
                    }

                    public void onWriteError(@NonNull Exception e, @NonNull List<Author> list) {
                        logger.info("Ошибка записи");
                    }
                })
                .listener(new ItemProcessListener<>() {
                    public void beforeProcess(@NonNull AuthorMongo o) {
                        logger.info("Начало обработки");
                    }

                    public void afterProcess(@NonNull AuthorMongo o, Author o2) {
                        logger.info("Конец обработки");
                    }

                    public void onProcessError(@NonNull AuthorMongo o, @NonNull Exception e) {
                        logger.info("Ошибка обработки");
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Начало пачки");
                    }

                    public void afterChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Конец пачки");
                    }

                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        logger.info("Ошибка пачки");
                    }
                })
//                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public Step cleanUpStep() {
        return new StepBuilder("cleanUpStep", jobRepository)
                .tasklet(cleanUpTasklet(), platformTransactionManager)
                .build();
    }
}
