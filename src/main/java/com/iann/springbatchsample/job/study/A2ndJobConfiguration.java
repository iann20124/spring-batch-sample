package com.iann.springbatchsample.job.study;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1. 여러개의 Step으로 이루어진 Job
 * 2. 반드시 start(첫 step)로 시작, 다음은 next(다음 step)으로. chainMethod
 * */

@Slf4j
@RequiredArgsConstructor
@Configuration
public class A2ndJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job secondJob() {
        return jobBuilderFactory.get(StudyJobName.SECOND_JOB)
                .start(step21())
                .next(step22())
                .next(step23())
                .build();
    }

    @Bean
    public Step step21() { // @Bean으로 등록되는 모든 Step, Job의 명칭은 다 달라야 한다. 그런데 귀찮으면 @Bean에 이름을 지정해 주면 method의 명칭은 같아도 상관없다.
        return stepBuilderFactory.get(StudyJobName.SECOND_JOB + "_step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>>> Second Job > step1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step22() {
        return stepBuilderFactory.get(StudyJobName.SECOND_JOB + "_step2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>>> Second Job > step2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step23() {
        return stepBuilderFactory.get(StudyJobName.SECOND_JOB + "_step3")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>>> Second Job > step3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
