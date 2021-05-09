package application.browser;

import static application.browser.CardHelper.cardNames;

import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.matag.cards.Card;

public abstract class AbstractCardContainerHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCardContainerHelper.class);

  final MatagBrowser matagBrowser;

  AbstractCardContainerHelper(MatagBrowser matagBrowser) {
    this.matagBrowser = matagBrowser;
  }

  public void containsExactly(Card... expectedCards) {
    containsExactly(cardNames(expectedCards).toArray(new String[0]));
  }

  public void containsExactly(String... expectedCardsNames) {
    var expectedCardsNamesList = Arrays.asList(expectedCardsNames);
    matagBrowser.wait(driver -> {
      var actualCardNames = cardNames(containerElement());
      LOGGER.info("actualCardNames={}   expectedCardsNames={}", actualCardNames, expectedCardsNamesList);
      return expectedCardsNamesList.equals(actualCardNames);
    });
  }

  public void contains(Card... expectedCards) {
    contains(cardNames(expectedCards).toArray(new String[0]));
  }

  public void contains(String... expectedCardsNames) {
    var expectedCardsNamesList = Arrays.asList(expectedCardsNames);
    matagBrowser.wait(driver -> {
      var actualCardNames = cardNames(containerElement());
      LOGGER.info("actualCardNames={}   expectedCard={}", actualCardNames, expectedCardsNamesList);
      return actualCardNames.containsAll(expectedCardsNamesList);
    });
  }

  public void doesNotContain(Card expectedCard) {
    matagBrowser.wait(driver -> {
      var actualCardNames = cardNames(containerElement());
      LOGGER.info("actualCardNames={}   expectedCard={}", actualCardNames, expectedCard);
      return !actualCardNames.contains(expectedCard.getName());
    });
  }

  public void isEmpty() {
    toHaveSize(0);
  }

  public void toHaveSize(int size) {
    matagBrowser.wait(driver -> {
      var cardNames = cardNames(containerElement());

      if (cardNames.size() == size) {
        return true;
      } else {
        LOGGER.info("Expected {} cards but got {} ({})", size, cardNames.size(), cardNames);
        return false;
      }
    });
  }

  public CardHelper getFirstCard(Card card) {
    return getCard(card, 0);
  }

  public CardHelper getCard(Card card, int index) {
    matagBrowser.wait(driver -> cardNames(containerElement()).stream()
      .filter(cardName -> cardName.equals(card.getName()))
      .count() > index);
    var webElement = containerElement().findElements(By.cssSelector("[aria-label=\"" + card.getName() + "\"]")).get(index);
    return new CardHelper(webElement, matagBrowser);
  }

  protected abstract WebElement containerElement();
}
