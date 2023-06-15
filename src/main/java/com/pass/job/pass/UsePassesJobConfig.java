package com.pass.job.pass;

import com.pass.repository.booking.BookingEntity;
import com.pass.repository.booking.BookingRepository;
import com.pass.repository.booking.BookingStatus;
import com.pass.repository.pass.PassEntity;
import com.pass.repository.pass.PassRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.Future;

@Configuration
public class UsePassesJobConfig {

    private final int CHUNK_SIZE = 5;
    
    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private PassRepository passRepository;

    private BookingRepository bookingRepository;

    public UsePassesJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory, PassRepository passRepository, BookingRepository bookingRepository) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.passRepository = passRepository;
        this.bookingRepository = bookingRepository;

    }

    @Bean
    public Job usePassesJob() {

        return this.jobBuilderFactory.get("usePassesJob")
                .start(usePassesStep())
                .build();

    }

    @Bean
    public Step usePassesStep() {
        
        return this.stepBuilderFactory.get("usePassesStep")
                .<BookingEntity, Future<BookingEntity>>chunk(CHUNK_SIZE)
                .reader(usePassesItemReader())
                .processor(usePassesAsyncItemProcessor())
                .writer(usePassesAsyncItemWriter())
                .build();

    }

    @Bean
    private JpaCursorItemReader<BookingEntity> usePassesItemReader() {

        return new JpaCursorItemReaderBuilder<BookingEntity>()
                .name("usePassesItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from BookingEntity b join fetch b.passEntity where b.status = :status and b.usedPass = false and b.endedAt < :endedAt")
                .parameterValues(Map.of("status", BookingStatus.CANCELLED, "endedAt", LocalDateTime.now()))
                .build();

    }

    @Bean
    private AsyncItemProcessor<BookingEntity, BookingEntity> usePassesAsyncItemProcessor() {

        AsyncItemProcessor<BookingEntity, BookingEntity> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(usePassesItemProcessor());
        asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return asyncItemProcessor;

    }

    @Bean
    private ItemProcessor<BookingEntity, BookingEntity> usePassesItemProcessor() {

        return bookingEntity -> {
            PassEntity passEntity = bookingEntity.getPassEntity();
            passEntity.setRemainingCount(passEntity.getRemainingCount() - 1);
            bookingEntity.setPassEntity(passEntity);

            bookingEntity.setUsedPass(true);
            return bookingEntity;
        };

    }

    @Bean
    public AsyncItemWriter<BookingEntity> usePassesAsyncItemWriter() {

        AsyncItemWriter<BookingEntity> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(usePassesItemWriter());
        return asyncItemWriter;

    }

    @Bean
    public ItemWriter<BookingEntity> usePassesItemWriter() {

        return bookingEntities -> {

            for(BookingEntity bookingEntity : bookingEntities) {

                // 잔여 횟수를 업데이트
                int updatedCount = passRepository.updateRemainingCount(bookingEntity.getPassSeq(), bookingEntity.getPassEntity().getRemainingCount());

                // 잔여 횟수가 업데이트 완료되면, 이용권 사용 여부를 업데이트
                if (updatedCount > 0) {
                    bookingRepository.updateUsedPass(bookingEntity.getPassSeq(), bookingEntity.isUsedPass());
                }
            }
        };
    }
}
