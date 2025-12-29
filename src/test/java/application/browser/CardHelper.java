package application.browser;

import com.matag.cards.Card;
import com.matag.cards.ability.type.AbilityType;
import com.matag.player.PlayerType;
import lombok.AllArgsConstructor;
import org.openqa.selenium.*;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@AllArgsConstructor
public class CardHelper {
  private final WebElement webElement;
  private final MatagBrowser matagBrowser;

  public static List<String> cardNames(Card... cards) {
    return Stream.of(cards).map(Card::getName).sorted().collect(toList());
  }

  public static List<String> cardNames(WebElement element) {
    var cardElements = element.findElements(By.className("card"));
    return cardElements.stream()
      .map(cardElement -> cardElement.getAttribute("aria-label"))
      .sorted()
      .collect(toList());
  }

  public void click() {
    try {
      webElement.click();
    } catch (ElementClickInterceptedException e) {
      matagBrowser.getJavascriptExecutor().executeScript("arguments[0].click()", webElement);
    }
  }

  public void tap() {
    playerHasPriority();
    click();
    isFrontendTapped();
  }

  public void select() {
    playerHasPriority();
    click();
    isSelected();
  }

  public void target() {
    playerHasPriority();
    click();
    isTargeted();
  }

  public void declareAsAttacker() {
    declareAsAttackerOrBlocker();
  }

  public void declareAsBlocker() {
    declareAsAttackerOrBlocker();
  }

  public String getCardId() {
    return webElement.getAttribute("id");
  }

  public int getCardIdNumeric() {
    return Integer.parseInt(getCardId().split("-")[1]);
  }

  public void isFrontendTapped() {
    hasClass("frontend-tapped");
  }

  public void isNotFrontendTapped() {
    doesNotHaveClass("frontend-tapped");
  }

  public void isTapped() {
    hasClass("tapped");
    doesNotHaveClass("frontend-tapped");
  }

  public void isTappedDoesNotUntapNextTurn() {
    hasClass("tapped-does-not-untap-next-turn");
  }

  public void isNotTapped() {
    doesNotHaveClass("tapped");
  }

  public void isTargeted() {
    hasClass("targeted");
  }

  public void isSelected() {
    hasClass("selected");
  }

  public void isNotSelected() {
    doesNotHaveClass("selected");
  }

  public void hasSummoningSickness() {
    matagBrowser.wait(presenceOfElementLocated(cardCssSelector(".summoning-sickness")));
  }

  public void doesNotHaveSummoningSickness() {
    waitForAbsenceOfElement(cardCssSelector(".summoning-sickness"));
  }

  public void hasFlying() {
    hasClass("flying");
  }

  public void hasDamage(int damage) {
    matagBrowser.wait(textToBe(cardCssSelector(".damage"), String.valueOf(damage)));
  }

  public void doesNotHaveDamage() {
    waitForAbsenceOfElement(cardCssSelector(".damage"));
  }

  public void hasPowerAndToughness(String powerAndToughness) {
    matagBrowser.wait(textToBe(cardCssSelector(".power-toughness"), String.valueOf(powerAndToughness)));
  }

  public void parentHasStyle(String style) {
    var parent = webElement.findElement(By.xpath("./.."));
    assertThat(parent.getAttribute("style")).contains(style);
  }

  private void hasClass(String classValue) {
    matagBrowser.wait(attributeContains(By.id(getCardId()), "class", classValue));
  }

  private void doesNotHaveClass(String classValue) {
    matagBrowser.wait(not(attributeContains(By.id(getCardId()), "class", classValue)));
  }

  private By cardCssSelector(String cssSelector) {
    return By.cssSelector("#" + getCardId() + " " + cssSelector);
  }

  private void waitForAbsenceOfElement(By locator) {
    try {
      matagBrowser.findElement(locator);
      matagBrowser.wait(invisibilityOfElementLocated(locator));

    } catch (TimeoutException | NotFoundException e) {
      System.out.println("Element " + locator + " is not present at all. That's okay.");
    }
  }

  private void playerHasPriority() {
    new PhaseHelper(matagBrowser).isPriority(PlayerType.PLAYER);
  }

  private void declareAsAttackerOrBlocker() {
    playerHasPriority();
    var cardId = getCardId();
    click();
    matagBrowser.wait(presenceOfElementLocated(By.cssSelector(".combat-line #" + cardId)));
  }

  public void hasPlus1Counters(int counters) {
    matagBrowser.wait(textToBe(cardCssSelector(".plus-1-counter"), String.valueOf(counters)));
  }

  public void hasMinus1Counters(int counters) {
    matagBrowser.wait(textToBe(cardCssSelector(".minus-1-counter"), String.valueOf(counters)));
  }

  public void hasKeywordCounters(AbilityType keywordAbility) {
    matagBrowser.wait(attributeContains(By.cssSelector("#" + this.getCardId() + " .keyword-counter"), "title", keywordAbility.name().toLowerCase()));
  }
}
