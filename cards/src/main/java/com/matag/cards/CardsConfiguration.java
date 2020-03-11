package com.matag.cards;

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
    int endPathIndex = cd.lastIndexOf("matag");
    if (endPathIndex > 0) {
      cd = cd.substring(0, endPathIndex + 5);
    }
    return cd + "/cards/src/main/resources";
  }

  @Bean
  public ObjectMapper cardsObjectMapper() {
    return new ObjectMapper();
  }
}
