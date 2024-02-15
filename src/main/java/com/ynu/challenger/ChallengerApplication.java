package com.ynu.challenger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ynu.challenger.mapper")
public class ChallengerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChallengerApplication.class, args);
    }

}
