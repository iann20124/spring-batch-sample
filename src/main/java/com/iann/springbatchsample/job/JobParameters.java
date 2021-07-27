package com.iann.springbatchsample.job;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ToString
@Getter
@NoArgsConstructor
public class JobParameters {

    @Value("#{jobParameters[status]}")
    private String status;

    @Value("#{jobParameters[createDate]}")
    private LocalDateTime createTime;

    public String getCreateTime() {
        return createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
    }
}
