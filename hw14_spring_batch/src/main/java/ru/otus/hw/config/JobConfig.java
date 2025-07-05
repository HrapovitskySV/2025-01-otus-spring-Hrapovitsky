package ru.otus.hw.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.CompositeItemWriteListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.modelsJpa.Book;
import ru.otus.hw.modelsJpa.Comment;
import ru.otus.hw.modelsJpa.Genre;
import ru.otus.hw.modelsMongo.BookMongo;
import ru.otus.hw.modelsMongo.CommentMongo;
import ru.otus.hw.modelsMongo.GenreMongo;
import ru.otus.hw.processors.AuthorItemProcessor;
import ru.otus.hw.processors.BookItemProcessor;
import ru.otus.hw.processors.CommentItemProcessor;
import ru.otus.hw.processors.GerneItemProcessor;
import ru.otus.hw.services.CleanUpService;
import ru.otus.hw.modelsJpa.Author;
import ru.otus.hw.modelsMongo.AuthorMongo;
import ru.otus.hw.services.MapObjectService;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unused")
@Configuration
@RequiredArgsConstructor
public class JobConfig {
    public static final String IMPORT_BOOK_JOB_NAME = "importBookJob";

    private static final int CHUNK_SIZE = 50;

    private final Logger logger = LoggerFactory.getLogger("Batch");


    @PersistenceContext
    private final EntityManager em;

    private final JdbcTemplate jdbcTemplate;

    private final DataSource dataSource;

    private final MongoTemplate mongoTemplate;

    private final MapObjectService mapObjectService;

    private final Map<String, Integer> mapIdAuthor = new HashMap<String, Integer>();

    private final Map<String, Author> mapAuthors = new HashMap<String, Author>();

    private final Map<String, Integer> mapIdGenre = new HashMap<String, Integer>();

    private final Map<String, Integer> mapIdBook = new HashMap<String, Integer>();


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
                .pageSize(CHUNK_SIZE)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(AuthorMongo.class)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }



    @StepScope
    @Bean
    public ItemProcessor<AuthorMongo, Author> processorAuthor() {
        //return new AuthorItemProcessor(jdbcTemplate, mapIdAuthor, mapAuthors);
        return new AuthorItemProcessor(mapObjectService);
    }


    @StepScope
    @Bean
    public JpaItemWriter<Author> itemWriterAuthor() {
        return new JpaItemWriterBuilder<Author>()
                .entityManagerFactory(em.getEntityManagerFactory())
                .usePersist(true)
                .build();
    }


    @StepScope
    @Bean
    public JdbcBatchItemWriter<Author> batchWriterAuthor() {
        return new JdbcBatchItemWriterBuilder<Author>()
                //.sql("INSERT INTO authors (id, full_Name) VALUES (:id, :fullName)")
                .sql("INSERT INTO authors (full_Name) VALUES (:fullName)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Step transformAuthorsStep(MongoPagingItemReader<AuthorMongo> reader,
                                     JpaItemWriter<Author> writer,
                                     ItemProcessor<AuthorMongo, Author> itemProcessor) {
        return new StepBuilder("transformAuthorsStep", jobRepository)
                .<AuthorMongo, Author>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemWriteListener<Author>() {
                    public void afterWrite(Chunk items) {
                        mapObjectService.convertMapAutorToMapIdAutor();
                        logger.info("Конец записи");
                    }

                    public void onWriteError(@NonNull Exception e, @NonNull List<Author> list) {
                        logger.info("Ошибка записи");
                    }
                })
                .listener(getChunkListener("авторов"))
//                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @StepScope
    @Bean
    public MongoPagingItemReader<GenreMongo> readerGenre() {
        return new MongoPagingItemReaderBuilder<GenreMongo>()
                .name("genreItemReader")
                .collection("genres")
                .pageSize(CHUNK_SIZE)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(GenreMongo.class)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<GenreMongo, Genre> processorGenre() {
        //return new GerneItemProcessor(jdbcTemplate, mapIdGenre);
        return new GerneItemProcessor(mapObjectService);
    }

    @StepScope
    @Bean
    public JdbcBatchItemWriter<Genre> batchWriterGenre() {
        return new JdbcBatchItemWriterBuilder<Genre>()
                .sql("INSERT INTO genres (id, name) VALUES (:id, :name)")
                //.sql("INSERT INTO genres (name) VALUES (:name)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Step transformGenresStep(MongoPagingItemReader<GenreMongo> reader, JdbcBatchItemWriter<Genre> writer,
                                     ItemProcessor<GenreMongo, Genre> itemProcessor) {
        return new StepBuilder("transformGenresStep", jobRepository)
                .<GenreMongo, Genre>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemWriteListener<Genre>() {
                    public void afterWrite(Chunk items) {
                        mapObjectService.convertMapGenreToMapIdGenre();
                    }

                    public void onWriteError(@NonNull Exception e, @NonNull List<Genre> list) {
                        logger.info("Ошибка записи");
                    }
                })
                .listener(getChunkListener("жанров"))
//                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @StepScope
    @Bean
    public MongoPagingItemReader<CommentMongo> readerComment() {
        return new MongoPagingItemReaderBuilder<CommentMongo>()
                .name("commentItemReader")
                //.collection("comments")
                .pageSize(CHUNK_SIZE)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(CommentMongo.class)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<CommentMongo, Comment> processorComment() {
        return new CommentItemProcessor(jdbcTemplate, mapIdBook);
    }

    @StepScope
    @Bean
    public JdbcBatchItemWriter<Comment> batchWriterComment() {
        return new JdbcBatchItemWriterBuilder<Comment>()
                //.sql("INSERT INTO genres (id, name) VALUES (:id, :name)")
                .sql("INSERT INTO comments (book_id, comment) VALUES (:bookId, :comment)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Step transformCommentsStep(MongoPagingItemReader<CommentMongo> reader, JdbcBatchItemWriter<Comment> writer,
                                    ItemProcessor<CommentMongo, Comment> itemProcessor) {
        return new StepBuilder("transformCommentsStep", jobRepository)
                .<CommentMongo, Comment>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(getChunkListener("комментариев"))
//                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @StepScope
    @Bean
    public MongoPagingItemReader<BookMongo> readerBook() {
        return new MongoPagingItemReaderBuilder<BookMongo>()
                .name("BookItemReader")
                .collection("books")
                .pageSize(CHUNK_SIZE)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(BookMongo.class)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }



    @StepScope
    @Bean
    public ItemProcessor<BookMongo, Book> processorBook() {
        //return new BookItemProcessor(jdbcTemplate, mapIdAuthor, mapIdGenre, mapIdBook);
        return new BookItemProcessor(mapObjectService);
    }


    @StepScope
    @Bean
    public JpaItemWriter<Book> itemWriterBook() {
        return new JpaItemWriterBuilder<Book>()
                .entityManagerFactory(em.getEntityManagerFactory())
                .usePersist(true)
                .build();
    }

    @Bean
    public Step transformBookStep(MongoPagingItemReader<BookMongo> reader, JpaItemWriter<Book> writer,
                                    ItemProcessor<BookMongo, Book> itemProcessor) {
        return new StepBuilder("transformBookStep", jobRepository)
                .<BookMongo, Book>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemWriteListener<Book>() {
                    public void afterWrite(Chunk items) {
                        mapObjectService.convertMapBookToMapIdBook();
                    }

                    public void onWriteError(@NonNull Exception e, @NonNull List<Book> list) {
                        logger.info("Ошибка записи");
                    }
                })
                .listener(getChunkListener("книг"))
                .build();
    }

    public ChunkListener getChunkListener(String objectName) {
        return new ChunkListener() {
            public void afterChunk(@NonNull ChunkContext chunkContext) {
                var stepExecution = chunkContext.getStepContext().getStepExecution();
                logger.info("Конец пачки " + objectName + "." +
                        " Read count: " + stepExecution.getReadCount() + "" +
                        " write count: " + stepExecution.getWriteCount());
            }

            public void afterChunkError(@NonNull ChunkContext chunkContext) {
                logger.info("Ошибка пачки " + objectName);
            }
        };
    }

    @Bean
    public MethodInvokingTaskletAdapter cleanUpTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(cleanUpService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }

    @Bean
    public Flow splitFlow(Flow flowAuthors, Flow flowGenres) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                .add(flowAuthors, flowGenres)
                .build();
    }

    @Bean
    public Flow flowAuthors(Step transformAuthorsStep) {
        return new FlowBuilder<SimpleFlow>("flowAuthors")
                .start(transformAuthorsStep)
                .build();
    }

    @Bean
    public Flow flowGenres(Step transformGenresStep) {
        return new FlowBuilder<SimpleFlow>("flowGenres")
                .start(transformGenresStep)
                .build();
    }

    @Bean
    public Job importBookJob(Flow splitFlow, Step transformBookStep, Step transformCommentsStep, Step cleanUpStep) {
        return new JobBuilder(IMPORT_BOOK_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                //.flow(transformAuthorsStep)
                .start(splitFlow)
                .next(transformBookStep)
                .next(transformCommentsStep)
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
    public Step cleanUpStep() {
        return new StepBuilder("cleanUpStep", jobRepository)
                .tasklet(cleanUpTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}
