package com.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

// git actions test
@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class SpringStartApplication {
  public static void main(String[] args) {
    SpringApplication.run(SpringStartApplication.class, args);
  }
}
