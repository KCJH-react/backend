package com.springstudy.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.springstudy.backend")
//@EntityScan("com.springstudy.backend.Api.Repository.Entity")
//@EnableJpaRepositories("com.springstudy.backend.Api.Repository")
//@EnableJpaAuditing
@EnableJpaAuditing
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
