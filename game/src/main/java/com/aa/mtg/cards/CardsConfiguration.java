package com.aa.mtg.cards;

import com.aa.mtg.cardinstance.CardInstance;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {CardsConfiguration.class, CardInstance.class})
public class CardsConfiguration {
}
