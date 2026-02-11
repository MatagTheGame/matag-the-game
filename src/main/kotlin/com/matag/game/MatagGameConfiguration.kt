package com.matag.game

import com.matag.cards.CardsConfiguration
import com.matag.game.cardinstance.CardInstanceConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(CardsConfiguration::class, CardInstanceConfiguration::class, MatagGameWebSocketConfiguration::class)
class MatagGameConfiguration 
