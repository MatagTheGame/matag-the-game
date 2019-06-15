package application.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class StackHelper extends AbstractCardContainerHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(StackHelper.class);

    StackHelper(MtgBrowser mtgBrowser) {
        super(mtgBrowser);
    }

    @Override
    protected WebElement containerElement() {
        return mtgBrowser.findElement(By.id("stack"));
    }

    public void containsAbilitiesExactly(List<String> expectedTriggeredAbilities) {
        mtgBrowser.wait(driver -> {
            List<String> actualTriggeredAbilities = triggeredAbilities(containerElement());
            LOGGER.info("actualTriggeredAbilities={}   expectedTriggeredAbilities={}", actualTriggeredAbilities, expectedTriggeredAbilities);
            return expectedTriggeredAbilities.equals(actualTriggeredAbilities);
        });
    }

    private List<String> triggeredAbilities(WebElement containerElement) {
        List<WebElement> cardElements = containerElement.findElements(By.className("triggered-ability"));
        return cardElements.stream()
                .map(WebElement::getText)
                .map(text -> text.replace("\n", " "))
                .collect(toList());
    }
}
