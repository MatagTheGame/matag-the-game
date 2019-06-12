package application.browser;

import com.aa.mtg.cards.Card;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static application.browser.CardHelper.cardNames;

public abstract class AbstractCardContainerHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCardContainerHelper.class);

    final MtgBrowser mtgBrowser;

    AbstractCardContainerHelper(MtgBrowser mtgBrowser) {
        this.mtgBrowser = mtgBrowser;
    }

    public void containsExactly(List<String> expectedCards) {
        mtgBrowser.wait(driver -> {
            List<String> actualCardNames = cardNames(containerElement());
            LOGGER.info("actualCardNames={}   expectedCards={}", actualCardNames, expectedCards);
            return expectedCards.equals(actualCardNames);
        });
    }

    public void contains(Card expectedCard) {
        mtgBrowser.wait(driver -> {
            List<String> actualCardNames = cardNames(containerElement());
            LOGGER.info("actualCardNames={}   expectedCard={}", actualCardNames, expectedCard);
            return actualCardNames.contains(expectedCard.getName());
        });
    }

    public void isEmpty() {
        toHaveSize(0);
    }

    public void toHaveSize(int size) {
        mtgBrowser.wait(driver -> {
            List<String> cardNames = cardNames(containerElement());

            if (cardNames.size() == size) {
                return true;
            } else {
                LOGGER.info("Expected {} cards but got {} ({})", size, cardNames.size(), cardNames);
                return false;
            }
        });
    }

    public void clickFirstCard(Card card) {
        getFirstCard(card).click();
    }

    public void clickCard(Card card, int index) {
        getCard(card, index).click();
    }

    public CardHelper getFirstCard(Card card) {
        return getCard(card, 0);
    }

    public CardHelper getCard(Card card, int index) {
        mtgBrowser.wait(driver -> cardNames(containerElement()).stream()
                .filter(cardName -> cardName.equals(card.getName()))
                .count() > index);
        WebElement webElement = mtgBrowser.findElements(By.cssSelector("[aria-label=\"" + card.getName() + "\"]")).get(index);
        return new CardHelper(webElement, mtgBrowser);
    }

    protected abstract WebElement containerElement();
}
