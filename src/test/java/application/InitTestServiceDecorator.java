package application;

import application.InitTestService;
import com.matag.game.status.GameStatus;

public class InitTestServiceDecorator extends InitTestService {
  private InitTestService initTestService;

  @Override
  public void initGameStatus(GameStatus gameStatus) {
    initTestService.initGameStatus(gameStatus);
  }

  public void setInitTestService(InitTestService initTestService) {
    initTestService.setCardInstanceFactory(this.getCardInstanceFactory());
    initTestService.setCards(this.getCards());
    this.initTestService = initTestService;
  }
}
