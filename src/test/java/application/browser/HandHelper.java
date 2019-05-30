package application.browser;

import com.aa.mtg.game.player.PlayerType;

public class HandHelper extends AbstractCardContainerHelper {

    HandHelper(MtgBrowser mtgBrowser, PlayerType playerType) {
        super(mtgBrowser, playerType);
    }

    @Override
    protected String getCardContainerId() {
        if (playerType == PlayerType.PLAYER) {
            return "player-hand";
        } else {
            return "opponent-hand";
        }
    }
}
