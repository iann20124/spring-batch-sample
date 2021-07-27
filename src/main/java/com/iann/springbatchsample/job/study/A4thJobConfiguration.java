package com.iann.springbatchsample.job.study;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * 1. 조건의 결과에 따른(JobExecutionDecider) Step 조절/분기
 * */

@Slf4j
@RequiredArgsConstructor
@Configuration
public class A4thJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job deciderJob() {
        return jobBuilderFactory.get(StudyJobName.FOURTH_JOB)
                .start(step41())
                .next(decider())// JobExecutionDecider
                .from(decider())
                    .on("ODD")
                    .to(oddStep())
                .from(decider())
                    .on("EVEN")
                    .to(evenStep())
                .end()
                .build();
    }

    @Bean
    public Step step41() {
        return stepBuilderFactory.get(StudyJobName.FOURTH_JOB + "_start_step")
                .tasklet((contribution, chunkContext) -> {
                    log.info("start step");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step oddStep() {
        return stepBuilderFactory.get(StudyJobName.FOURTH_JOB + "_odd_step")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>>>>>> ODD NUMBER") ;
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step evenStep() {
        return stepBuilderFactory.get(StudyJobName.FOURTH_JOB + "_even_step")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>>>>>> EVEN NUMBER") ;
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new OddDecider();
    }

    public static class OddDecider implements JobExecutionDecider{
        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            Random random = new Random();
            int number = random.nextInt(50) + 1;
            log.info("RANDOM NUMBER {}", number);

            if(number % 2 == 1) {
                return new FlowExecutionStatus("ODD");
            } else {
                return new FlowExecutionStatus("EVEN");
            }
        }
    }
}
