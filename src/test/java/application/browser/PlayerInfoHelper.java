package application.browser;

import com.aa.mtg.game.player.PlayerType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class PlayerInfoHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerInfoHelper.class);

    private MtgBrowser mtgBrowser;
    private PlayerType playerType;

    PlayerInfoHelper(MtgBrowser mtgBrowser, PlayerType playerType) {
        this.mtgBrowser = mtgBrowser;
        this.playerType = playerType;
    }

    public void toHaveName() {
        mtgBrowser.wait(driver -> {
            LOGGER.info("actualPlayerName={}", getPlayerName());
            return !getPlayerName().equals("");
        });
    }

    public void toHaveName(String expectedPlayerName) {
        mtgBrowser.wait(driver -> {
            LOGGER.info("actualPlayerName={}   expectedPlayerName={}", getPlayerName(), expectedPlayerName);
            return getPlayerName().equals(expectedPlayerName);
        });
    }

    public void toHaveLife(int expectedPlayerLife) {
        mtgBrowser.wait(driver -> {
            LOGGER.info("actualPlayerName={}   expectedPlayerLife={}", getPlayerLife(), expectedPlayerLife);
            return getPlayerLife().equals(String.valueOf(expectedPlayerLife));
        });
    }

    public String getPlayerName() {
        return playerInfoElement().findElements(By.tagName("span")).get(0).getText();
    }

    public String getPlayerLife() {
        return playerInfoElement().findElements(By.tagName("span")).get(1).getText();
    }

    public void toBeActive() {
        toHaveClass("active-player");
    }

    public void toBeInactive() {
        toHaveClass("inactive-player");
    }

    private void toHaveClass(String cssClass) {
        mtgBrowser.wait(driver -> {
            String playerClasses = playerInfoElement().getAttribute("class");
            LOGGER.info("playerClasses={}", playerClasses);
            return Arrays.asList(playerClasses.split(" ")).contains(cssClass);
        });
    }

    private WebElement playerInfoElement() {
        if (playerType == PlayerType.PLAYER) {
            return mtgBrowser.findElement(By.id("player-info"));
        } else {
            return mtgBrowser.findElement(By.id("opponent-info"));
        }
    }
}
