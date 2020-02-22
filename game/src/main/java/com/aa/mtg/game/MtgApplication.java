package com.aa.mtg.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MtgApplication {
  public static void main(String[] args) {
    SpringApplication.run(MtgApplication.class, args);
  }
}