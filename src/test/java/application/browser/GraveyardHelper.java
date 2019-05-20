package application.browser;

import com.aa.mtg.game.player.PlayerType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static application.browser.CardHelper.cardNames;

public class GraveyardHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraveyardHelper.class);

    private MtgBrowser mtgBrowser;
    private PlayerType playerType;

    GraveyardHelper(MtgBrowser mtgBrowser, PlayerType playerType) {
        this.mtgBrowser = mtgBrowser;
        this.playerType = playerType;
    }

    public void graveyardContainsExactly(List<String> expectedCards) {
        mtgBrowser.wait(driver -> {
            List<String> actualCardNames = cardNames(graveyardElement());
            LOGGER.info("actualCardNames={}   expectedCards={}", actualCardNames, expectedCards);
            return expectedCards.equals(actualCardNames);
        });
    }

    private WebElement graveyardElement() {
        if (playerType == PlayerType.PLAYER) {
            return mtgBrowser.findElement(By.id("player-graveyard"));
        } else {
            return mtgBrowser.findElement(By.id("opponent-graveyard"));
        }
    }
}
