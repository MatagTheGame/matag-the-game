package com.matag.game.player;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.cardinstance.CardListComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class Hand extends CardListComponent {

  private final CardInstanceFactory cardInstanceFactory;

  public Hand(CardInstanceFactory cardInstanceFactory) {
    this.cardInstanceFactory = cardInstanceFactory;
  }

  public List<CardInstance> maskedHand() {
    return cardInstanceFactory.mask(this.cards);
  }
}
