package com.matag.game.status;

import com.matag.game.stack.SpellStack;
import com.matag.game.turn.Turn;
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
    GameStatus gameStatus = new GameStatus(new Turn(), new SpellStack());
    gameStatus.setGameId(gameId);
    return gameStatus;
  }
}
