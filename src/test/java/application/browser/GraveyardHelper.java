package application.browser;

import com.aa.mtg.game.player.PlayerType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class GraveyardHelper extends AbstractCardContainerHelper {

    GraveyardHelper(MtgBrowser mtgBrowser, PlayerType playerType) {
        super(mtgBrowser, playerType);
    }

    @Override
    protected WebElement containerElement() {
        if (playerType == PlayerType.PLAYER) {
            return mtgBrowser.findElement(By.id("player-graveyard"));
        } else {
            return mtgBrowser.findElement(By.id("opponent-graveyard"));
        }
    }
}