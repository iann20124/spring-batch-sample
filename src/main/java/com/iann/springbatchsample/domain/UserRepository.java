package com.iann.springbatchsample.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findByAgeGreaterThan(Integer age);

    // birthdate가 현재 달인 사람 찾는 query도 필요
    List<User> findByBirthdateBetween(LocalDate start, LocalDate end);
}
