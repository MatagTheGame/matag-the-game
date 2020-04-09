package com.matag.admin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;

@Profile("!test")
@Configuration
@EnableScheduling
public class MatagAdminProdConfiguration {
  @Bean
  @Profile("!test")
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
