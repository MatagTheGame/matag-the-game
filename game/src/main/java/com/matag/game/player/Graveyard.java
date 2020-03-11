package com.matag.game.player;

import com.matag.cardinstance.CardListComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Graveyard extends CardListComponent {
}
