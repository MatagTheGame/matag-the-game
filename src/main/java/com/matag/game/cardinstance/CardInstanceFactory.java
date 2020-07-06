package com.matag.game.cardinstance;

import com.matag.cards.Card;
import com.matag.cards.properties.Rarity;
import com.matag.game.status.GameStatus;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Component
public class CardInstanceFactory implements ApplicationContextAware {
  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  public CardInstance create(GameStatus gameStatus, int id, Card card, String owner, String controller) {
    var cardInstance = applicationContext.getBean(CardInstance.class);
    cardInstance.setGameStatus(gameStatus);
    cardInstance.setId(id);
    cardInstance.setCard(card);
    cardInstance.setOwner(owner);
    cardInstance.setController(controller);
    return cardInstance;
  }

  public CardInstance create(GameStatus gameStatus, int id, Card card, String owner) {
    return create(gameStatus, id, card, owner, null);
  }

  public CardInstance mask(CardInstance cardInstance) {
    return create(cardInstance.getGameStatus(), cardInstance.getId(), hiddenCard(), cardInstance.getOwner());
  }

  public List<CardInstance> mask(List<CardInstance> cardInstanceList) {
    return cardInstanceList.stream()
      .map(this::mask)
      .collect(Collectors.toList());
  }

  private Card hiddenCard() {
    return new Card("card", "/img/card-back.jpg", new TreeSet<>(), emptyList(), new TreeSet<>(), new TreeSet<>(), Rarity.COMMON, "", 0, 0, emptyList());
  }
}
