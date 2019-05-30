package application.browser;

import com.aa.mtg.game.player.PlayerType;

public class BattlefieldHelper extends AbstractCardContainerHelper {

    BattlefieldHelper(MtgBrowser mtgBrowser, PlayerType playerType) {
        super(mtgBrowser, playerType);
    }

    @Override
    protected String getCardContainerId() {
        if (playerType == PlayerType.PLAYER) {
            return "player-battlefield";
        } else {
            return "opponent-battlefield";
        }
    }
}
