package com.iann.springbatchsample.job.study;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1. batch 가 잘 동작하는지 체크하기 위한 Job
 * 2. program argument로 주어지는 JobParameter가 잘 들어오는지 체크
 * */

@Slf4j
@RequiredArgsConstructor
@Configuration
public class A1stJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job firstJob() {
        return jobBuilderFactory.get(StudyJobName.FIRST_JOB) // Batch Job 생성 : firstJob
                .start(step11(null))
                .build();
    }

    @Bean
    @JobScope
    public Step step11(@Value("#{jobParameters[requestDate]}") String requestDate) { // Job Parameter
        return stepBuilderFactory.get(StudyJobName.FIRST_JOB + "_step1") // Batch Step 생성 : step1
                .tasklet((contribution, chunkContext) -> {
                    // Step 안에서 단일로 수행될 Custom 기능들 선언할 때 사용
                    log.info(">>>>>>>>>>>>> First Job ::: Step 1");
                    log.info(">>>>>>>>>>>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
