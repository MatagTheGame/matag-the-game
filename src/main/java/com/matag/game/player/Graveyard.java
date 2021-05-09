package com.matag.game.player;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardListComponent;

@Component
@Scope("prototype")
public class Graveyard extends CardListComponent {
}
