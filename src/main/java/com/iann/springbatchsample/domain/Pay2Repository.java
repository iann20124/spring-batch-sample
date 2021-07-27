package com.iann.springbatchsample.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Pay2Repository extends JpaRepository<Pay2, Long> {
    List<Pay2> findAllBySuccessStatus(boolean isSuccess);
}
