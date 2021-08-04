package com.iann.springbatchsample.job.practice;

import com.iann.springbatchsample.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class UserAgingJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private static final int chunkSize = 100;

    @Bean(PracticeJobName.USER_AGING_JOB)
    public Job job() throws Exception {
        return jobBuilderFactory.get(PracticeJobName.USER_AGING_JOB)
                .incrementer(new RunIdIncrementer())
                .start(agingStep())
                .build();
    }

    @Bean(PracticeJobName.USER_AGING_JOB + "_STEP")
    public Step agingStep() throws Exception {
        return stepBuilderFactory.get(PracticeJobName.USER_AGING_JOB + "_STEP")
                .<User, User>chunk(chunkSize)// 한 번에 처리될 transaction의 단위
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean(PracticeJobName.USER_AGING_JOB + "_STEP" + "_READER")
    public JdbcPagingItemReader<User> itemReader () throws Exception {

        log.info("READER");
        return new JdbcPagingItemReaderBuilder<User>()
                .pageSize(chunkSize) // pageSize는 read 해 올 row의 갯수를 의미함
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(User.class))
                .queryProvider(query())
                .name(PracticeJobName.USER_AGING_JOB + "_STEP_READER")
                .build();
    }

    @Bean(PracticeJobName.USER_AGING_JOB + "_STEP" + "_PROCESSOR")
    public ItemProcessor<User, User> itemProcessor() {
        return item -> {
            item.addAge();
            log.info("USER ::: user - {} age - {}", item.getUserId(), item.getAge());
            return item;
        };
    }

    @Bean(PracticeJobName.USER_AGING_JOB + "_STEP" + "_WRITER")
    public ItemWriter<User> itemWriter() {
        String query = "replace into USER (user_id, email, name, active, birthdate, age) " +
                "                   values(:userId,:email, :name, :active, :birthdate, :age) ";
        return new JdbcBatchItemWriterBuilder<User>()
                .dataSource(dataSource)
                .sql(query)
                .beanMapped()
                .build();
    }

    private PagingQueryProvider query() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("select *");
        queryProvider.setFromClause("from user");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("user_id", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);
        return queryProvider.getObject();
    }
}
