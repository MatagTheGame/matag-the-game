package application.browser;

import com.aa.mtg.cards.Card;
import com.aa.mtg.game.player.PlayerType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HandHelper {
    private MtgBrowser mtgBrowser;
    private PlayerType playerType;

    HandHelper(MtgBrowser mtgBrowser, PlayerType playerType) {
        this.mtgBrowser = mtgBrowser;
        this.playerType = playerType;
    }

    public void playerHandContainsExactly(List<Card> cards) {
        mtgBrowser.wait(driver -> {
            List<WebElement> cardElements = handElement().findElements(By.tagName("div"));
            // TODO continue from here
            return true;
        });
    }

    private WebElement handElement() {
        if (playerType == PlayerType.PLAYER) {
            return mtgBrowser.findElement(By.id("player-hand"));
        } else {
            return mtgBrowser.findElement(By.id("opponent-hand"));
        }
    }
}
