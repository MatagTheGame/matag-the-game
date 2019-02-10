package com.aa.mtg.utils;

import com.aa.mtg.event.EventSender;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SpringBootTestConfiguration {

  @Bean("eventSenderMock")
  @Primary
  public EventSender eventSender() {
    return Mockito.mock(EventSender.class);
  }

}
