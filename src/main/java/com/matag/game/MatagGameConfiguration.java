package com.matag.game;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.matag.cards.CardsConfiguration;
import com.matag.game.cardinstance.CardInstanceConfiguration;

@Configuration
@Import({CardsConfiguration.class, CardInstanceConfiguration.class})
public class MatagGameConfiguration {

}
