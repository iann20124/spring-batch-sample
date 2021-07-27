package com.iann.springbatchsample.job.study;

import com.iann.springbatchsample.domain.Pay2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

import static com.iann.springbatchsample.job.study.A7thJobConfiguration.JOB_NAME;

@Slf4j
@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(name="job.name", havingValue = JOB_NAME)
public class A7thJobConfiguration {
    public static final String JOB_NAME = "pay2PagingFailJob";

    private final EntityManagerFactory entityManagerFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;

    private final int chunkSize = 10;

    @Bean
    public Job pay2PagingJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(pay2PagingStep())
                .build();
    }

    @Bean
    @JobScope
    public Step pay2PagingStep() {
        return stepBuilderFactory.get("pay2PagingStep")
                .<Pay2, Pay2> chunk(chunkSize)
                .reader(pay2PagingReader())
                .processor(pay2PagingProcessor())
                .writer(writer())
                .build();
    }


    @Bean
    @StepScope
    public JpaPagingItemReader<Pay2> pay2PagingReader() {
        return new JpaPagingItemReaderBuilder<Pay2>()
                .queryString("SELECT p FROM Pay2 p WHERE p.successStatus = false")
                .pageSize(chunkSize)
                .entityManagerFactory(entityManagerFactory)
                .name("pay2PagingReader")
                .build();
    }


    @Bean
    @StepScope
    public ItemProcessor<Pay2, Pay2> pay2PagingProcessor() {
        return item -> {
            item.success();
            return item;
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<Pay2> writer() {
        JpaItemWriter<Pay2> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

}
