package com.iann.springbatchsample;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing // @Required : Spring Batch 활성화 반드시 추가
@SpringBootApplication
public class BatchApplication {

	public static void main(String[] args) {
		System.out.println("Server loaded");
		SpringApplication.run(BatchApplication.class, args);
	}

}


//https://docs.spring.io/spring-batch/docs/current/reference/html/index-single.html