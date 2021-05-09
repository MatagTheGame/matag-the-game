package com.matag.game.player;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.cardinstance.CardListComponent;

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
