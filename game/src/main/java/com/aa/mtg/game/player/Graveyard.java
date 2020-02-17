package com.aa.mtg.game.player;

import com.aa.mtg.cardinstance.CardListComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Graveyard extends CardListComponent {
}
