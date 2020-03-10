package com.aa.mtg.admin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;

@Configuration
public class MtgAdminConfiguration {
  @Bean
  @Profile("!test")
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
