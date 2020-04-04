package com.matag.admin;

import com.matag.cards.CardsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Import(CardsConfiguration.class)
public class MatagAdminConfiguration {
  @Bean
  @Profile("!test")
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
