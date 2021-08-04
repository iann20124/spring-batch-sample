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
public class UserActiveJobConfiguration {
    // 해당 사용자의 나이 상태에 따라 active 상태를 true 혹은 false 로 변경시키기

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private static final int chunkSize = 100;

    @Bean(PracticeJobName.USER_INACTIVE_JOB)
    public Job job() throws Exception {
        return jobBuilderFactory.get(PracticeJobName.USER_INACTIVE_JOB)
                .incrementer(new RunIdIncrementer())
                .start(activeStep())
                .build();
    }

    @Bean(PracticeJobName.USER_INACTIVE_JOB + "_STEP")
    public Step activeStep() throws Exception {
        return stepBuilderFactory.get(PracticeJobName.USER_INACTIVE_JOB + "_STEP")
                .<User, User>chunk(chunkSize)// 한 번에 처리될 transaction의 단위
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<User> itemReader () throws Exception {

        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("age", 20);

        log.info("READER");
        return new JdbcPagingItemReaderBuilder<User>()
                .pageSize(chunkSize) // pageSize는 read 해 올 row의 갯수를 의미함
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(User.class))
                .queryProvider(query())
                .parameterValues(parameterValues)
                .name(PracticeJobName.USER_INACTIVE_JOB + "_STEP_READER")
                .build();
    }

    @Bean
    public ItemProcessor<User, User> itemProcessor() {
        log.info("PROCESSOR");
        return item -> {
            item.inactivate();
            log.info("USER ::: {} ", item.toString());
            return item;
        };
    }

    @Bean
    public ItemWriter<User> itemWriter() {
        log.info("WRITER");
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
        queryProvider.setWhereClause("where age < :age");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("user_id", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);
        return queryProvider.getObject();
    }

}
