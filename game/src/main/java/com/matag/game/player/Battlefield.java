package com.matag.game.player;

import com.matag.cardinstance.CardInstance;
import com.matag.cardinstance.CardListComponent;
import com.matag.cardinstance.CardInstanceSearch;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class Battlefield extends CardListComponent {

  public void removeAttacking() {
    new CardInstanceSearch(cards).attacking().getCards()
      .forEach(cardInstance -> cardInstance.getModifiers().setAttacking(false));
  }

  public void removeBlocking() {
    new CardInstanceSearch(cards).blocking().getCards()
      .forEach(cardInstance -> cardInstance.getModifiers().unsetBlockingCardId());
  }

  public List<CardInstance> getAttackingCreatures() {
    return new CardInstanceSearch(cards).attacking().getCards();
  }

  public List<CardInstance> getBlockingCreatures() {
    return new CardInstanceSearch(cards).blocking().getCards();
  }

  public List<CardInstance> getBlockingCreaturesFor(int attackingCardId) {
    return getBlockingCreatures().stream()
      .filter(cardInstance -> cardInstance.getModifiers().getBlockingCardId() == attackingCardId)
      .collect(Collectors.toList());
  }
}
