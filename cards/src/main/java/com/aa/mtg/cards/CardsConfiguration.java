package com.aa.mtg.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {CardsConfiguration.class})
public class CardsConfiguration {
    public static final String RESOURCES_PATH = "../cards/src/main/resources";

    @Bean
    public ObjectMapper cardsObjectMapper() {
        return new ObjectMapper();
    }
}
