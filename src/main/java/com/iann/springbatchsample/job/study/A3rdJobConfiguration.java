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
 * 1. 특정 step이 실패했을 경우 혹은 성공했을 경우의 처리 방법
 * 2. specific 한 경우를 앞 순서에 넣어 앞에서 분기를 쳐 실행되도록 한다.
 * */

@Slf4j
@RequiredArgsConstructor
@Configuration
public class A3rdJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job thirdJob() {
        return jobBuilderFactory.get(StudyJobName.THIRD_JOB)
                .start(step31())
                    .on("FAILED")
                    .to(step33())
                    .on("*")
                    .end()
                .from(step31())
                    .on("*")// FAILED 이외의 경우는 to step2로 가고, 그 다음은 step3로 간다.
                    .to(step32())
                    .next(step33())
                    .on("*")
                    .end()
                .end()
                .build();
    }

    @Bean
    public Step step31() {
        return stepBuilderFactory.get(StudyJobName.THIRD_JOB + "_step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>>> Third Job > step1");
//                    contribution.setExitStatus(ExitStatus.FAILED); // 실패했을 경우
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step32() {
        return stepBuilderFactory.get(StudyJobName.THIRD_JOB + "_step2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>>> Third Job > step2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step33() {
        return stepBuilderFactory.get(StudyJobName.THIRD_JOB + "_step3")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>>> Third Job > step3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
