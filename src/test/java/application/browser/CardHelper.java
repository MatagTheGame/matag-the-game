package application.browser;

import com.aa.mtg.cards.Card;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CardHelper {

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
}
