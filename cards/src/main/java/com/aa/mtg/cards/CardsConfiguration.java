package com.aa.mtg.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@ComponentScan(basePackageClasses = {CardsConfiguration.class})
public class CardsConfiguration {
  public static String getResourcesPath() {
    String cd = new File("").getAbsolutePath();
    int endPathIndex = cd.lastIndexOf("mtg");
    return cd.substring(0, endPathIndex + 3).concat("/cards/src/main/resources");
  }

  @Bean
  public ObjectMapper cardsObjectMapper() {
    return new ObjectMapper();
  }
}
