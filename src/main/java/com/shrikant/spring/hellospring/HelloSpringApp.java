package com.shrikant.spring.hellospring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class HelloSpringApp {

  public static void main(final String[] args) {
    SpringApplication.run(HelloSpringApp.class, args);
  }
}
