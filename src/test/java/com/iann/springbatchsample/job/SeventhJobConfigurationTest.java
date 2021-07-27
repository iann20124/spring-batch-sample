package com.iann.springbatchsample.job;

import com.iann.springbatchsample.domain.Pay2Repository;
import com.iann.springbatchsample.job.study.A7thJobConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"job.name=" + A7thJobConfiguration.JOB_NAME})
public class SeventhJobConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private Pay2Repository repository;

    @Test
    public void update () throws Exception {
//        for(long i = 0 ; i<50; i++) {
//            repository.save(new Pay2(i, false));
//        }

        JobExecution jobExecution =  jobLauncherTestUtils.launchJob();

        Assertions.assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        Assertions.assertThat(repository.findAllBySuccessStatus(true).size()).isEqualTo(50);

    }
}
