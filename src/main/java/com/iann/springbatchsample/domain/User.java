package com.iann.springbatchsample.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;
    private String name;
    private boolean active;
    private LocalDate birthdate; // random으로 만든다.
    private Integer age;

    public void activate(){
        this.active = true;
    }
    public void inactivate() {
        this.active = false;
    }
    public void addAge() {
        this.age++;
    }

    public User(String email, String name, LocalDate birthdate, Integer age) {
        this.email = email;
        this.name = name;
        this.birthdate = birthdate;
        this.age = age;
        this.active = true;
    }

}
