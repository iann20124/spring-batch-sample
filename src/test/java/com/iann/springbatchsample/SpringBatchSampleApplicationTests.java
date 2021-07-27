package com.iann.springbatchsample;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
class SpringBatchSampleApplicationTests {

	@Bean
	public JobLauncherTestUtils jobLauncherTestUtils() {
		return new JobLauncherTestUtils();
	}

}
