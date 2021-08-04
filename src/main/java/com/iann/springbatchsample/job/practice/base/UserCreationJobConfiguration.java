package com.iann.springbatchsample.job.practice.base;

import com.iann.springbatchsample.domain.User;
import com.iann.springbatchsample.domain.UserRepository;
import com.iann.springbatchsample.job.practice.PracticeJobName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class UserCreationJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private final UserRepository userRepository;

    @Bean(PracticeJobName.USER_CREATION_JOB)
    public Job firstJob() {
        return jobBuilderFactory.get(PracticeJobName.USER_CREATION_JOB) // Batch Job 생성 : firstJob
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean(PracticeJobName.USER_CREATION_JOB + "_STEP")
    @JobScope
    public Step step1() { // Job Parameter
        return stepBuilderFactory.get(PracticeJobName.USER_CREATION_JOB + "_STEP")
                .tasklet((contribution, chunkContext) -> {
                    for(int i = 0 ; i<1000 ; i++ ) {
                        User user = new User("iann20124test@gmail.com", "test_" + i, generateBirthdate(), i%100);
                        userRepository.save(user);
                    }
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    private LocalDate generateBirthdate() {
        LocalDate start = LocalDate.of(1970, Month.JANUARY, 1);
        long days = ChronoUnit.DAYS.between(start, LocalDate.now());
        LocalDate randomDate = start.plusDays(new Random().nextInt((int) days + 1));
        return randomDate;
    }
}
