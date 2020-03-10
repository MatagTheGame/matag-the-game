package com.aa.mtg.admin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class MtgAdminConfiguration {
  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
