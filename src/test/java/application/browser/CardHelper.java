package application.browser;

import com.aa.mtg.cards.Card;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.support.ui.ExpectedConditions.attributeContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

public class CardHelper {
    private WebElement webElement;
    private MtgBrowser mtgBrowser;

    CardHelper(WebElement webElement, MtgBrowser mtgBrowser) {
        this.webElement = webElement;
        this.mtgBrowser = mtgBrowser;
    }

    public static List<String> cardNames(Card... cards) {
        return Stream.of(cards).map(Card::getName).sorted().collect(toList());
    }

    public static List<String> cardNames(WebElement element) {
        List<WebElement> cardElements = element.findElements(By.className("card"));
        return cardElements.stream()
                .map(cardElement -> cardElement.getAttribute("aria-label"))
                .sorted()
                .collect(toList());
    }

    public void click() {
        try {
            webElement.click();
        } catch (ElementClickInterceptedException e) {
            webElement.findElement(By.tagName("div")).click();
        }
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

    public void isTapped() {
        hasClass("tapped");
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

    private void hasClass(String classValue) {
        mtgBrowser.wait(attributeContains(By.id(getCardId()), "class", classValue));
    }

    private void doesNotHaveClass(String classValue) {
        mtgBrowser.wait(not(attributeContains(By.id(getCardId()), "class", classValue)));
    }

    public void hasDamage(int damage) {
        mtgBrowser.wait(textToBe(By.cssSelector("#" + getCardId() + " .damage"), String.valueOf(damage)));
    }
}
