package com.aa.mtg.game.status;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class GameStatusFactory implements ApplicationContextAware {
  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  public GameStatus create(String gameId) {
    GameStatus gameStatus = applicationContext.getBean(GameStatus.class);
    gameStatus.setGameId(gameId);
    return gameStatus;
  }
}
