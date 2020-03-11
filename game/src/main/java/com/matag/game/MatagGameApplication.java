package com.matag.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MatagGameApplication {
  public static void main(String[] args) {
    SpringApplication.run(MatagGameApplication.class, args);
  }
}