package com.aa.mtg.game;

import com.aa.mtg.cardinstance.CardInstanceConfiguration;
import com.aa.mtg.cards.CardsConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Import({CardsConfiguration.class, CardInstanceConfiguration.class})
public class MtgGameWebConfiguration implements WebMvcConfigurer {
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("redirect:/ui/");
    registry.addViewController("/ui/game/**").setViewName("forward:/game.html");
  }
}
