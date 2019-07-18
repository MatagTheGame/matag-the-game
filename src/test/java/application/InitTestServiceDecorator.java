package application;

import com.aa.mtg.game.init.test.InitTestService;
import com.aa.mtg.game.status.GameStatus;

public class InitTestServiceDecorator extends InitTestService {
    private InitTestService initTestService;

    @Override
    public void initGameStatus(GameStatus gameStatus) {
        initTestService.initGameStatus(gameStatus);
    }

    public void setInitTestService(InitTestService initTestService) {
        this.initTestService = initTestService;
    }
}
