package com.iann.springbatchsample;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class SpringBatchSampleApplicationTests {

	@Bean
	public JobLauncherTestUtils jobLauncherTestUtils(){
		return new JobLauncherTestUtils() {
			@Override
			@Autowired
			public void setJob(@Qualifier("FIRST_JOB") Job job) {
				super.setJob(job);
			}
		};
	}

}
