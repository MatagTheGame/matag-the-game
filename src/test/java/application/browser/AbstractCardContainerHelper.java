package application.browser;

import com.aa.mtg.cards.Card;
import com.aa.mtg.game.player.PlayerType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static application.browser.CardHelper.cardNames;

public abstract class AbstractCardContainerHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCardContainerHelper.class);

    final MtgBrowser mtgBrowser;
    final PlayerType playerType;

    AbstractCardContainerHelper(MtgBrowser mtgBrowser, PlayerType playerType) {
        this.mtgBrowser = mtgBrowser;
        this.playerType = playerType;
    }

    public void containsExactly(List<String> expectedCards) {
        mtgBrowser.wait(driver -> {
            List<String> actualCardNames = cardNames(containerElement());
            LOGGER.info("actualCardNames={}   expectedCards={}", actualCardNames, expectedCards);
            return expectedCards.equals(actualCardNames);
        });
    }

    public void clickCard(Card card) {
        getFirstCardByName(card);
    }

    private void getFirstCardByName(Card card) {
        mtgBrowser.wait(driver -> {
            List<String> actualCardNames = cardNames(containerElement());
            return actualCardNames.contains(card.getName());
        });
        mtgBrowser.findElements(By.cssSelector("[aria-label=" + card.getName() + "]")).get(0).click();
    }

    private WebElement containerElement() {
        return mtgBrowser.findElement(By.id(getCardContainerId()));
    }

    protected abstract String getCardContainerId();
}
