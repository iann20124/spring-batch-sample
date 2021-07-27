package com.iann.springbatchsample.job.study;

import com.iann.springbatchsample.job.JobParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * JobParameters class 만들어서 argument parameters 사용
 *
 * @Value("#{jobParameters[requestDate]}") - 이게 method param으로 작성되지 않으면 인식이 안되네...
 * private String requestDate;
 *
 * 그래서 JobParameter class를 만든 후 얘를 사용해 보기로 한다.
 */

@Slf4j
@Configuration
@RequiredArgsConstructor
public class A5thJobConfiguration {

    private final JobParameters jobParameter;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean("jobParameter")
    @JobScope
    public JobParameters jobParameters() {
        return new JobParameters();
    }

    @Bean
    public Job fifthJob() {
        return jobBuilderFactory.get(StudyJobName.FIFTH_JOB)
                .start(step1())
                .build();
    }


    /**
     * JobStepScope를 써줘야 하는 이유 : jobParameter를 받기 위해 .
     * @JobScope or @StepScope가 Job과 Step이 실행되는 시점에 Bean을 생성한다.
     * 즉 Bean의 생성 시점을 지정된 Scope가 실행되는 시점으로 지연 -> JobParameter의 Late Binding이 가능
     */
    @Bean(name=StudyJobName.FIFTH_JOB + "_step1")
    @JobScope
    public Step step1() {
        return stepBuilderFactory.get(StudyJobName.FIFTH_JOB + "_step1")
                .tasklet((contribution, chunkContext) -> {
                    // Step 안에서 단일로 수행될 Customize 하는 기능들 선언할 때 사용되는 jobParameters
                    log.info(">>>>>>>>>>>>> Fifth Job ::: Step 1");
                    log.info(">>>>>>>>>>>>> status = {}", jobParameter.getStatus());
                    log.info(">>>>>>>>>>>>> createTime = {}", jobParameter.getCreateTime());
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
