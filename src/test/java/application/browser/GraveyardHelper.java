package application.browser;

import com.aa.mtg.game.player.PlayerType;

public class GraveyardHelper extends AbstractCardContainerHelper {

    GraveyardHelper(MtgBrowser mtgBrowser, PlayerType playerType) {
        super(mtgBrowser, playerType);
    }

    @Override
    protected String getCardContainerId() {
        if (playerType == PlayerType.PLAYER) {
            return "player-graveyard";
        } else {
            return "opponent-graveyard";
        }
    }
}
