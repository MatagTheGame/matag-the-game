package application.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class StackHelper extends AbstractCardContainerHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(StackHelper.class);

  StackHelper(MatagBrowser matagBrowser) {
    super(matagBrowser);
  }

  @Override
  protected WebElement containerElement() {
    return matagBrowser.findElement(By.id("stack"));
  }

  public void containsAbility(String ability) {
    containsAbilitiesExactly(singletonList(ability));
  }

  public void containsAbilitiesExactly(List<String> expectedTriggeredAbilities) {
    matagBrowser.wait(driver -> {
      var actualTriggeredAbilities = triggeredAbilities(containerElement());
      LOGGER.info("actualTriggeredAbilities={}   expectedTriggeredAbilities={}", actualTriggeredAbilities, expectedTriggeredAbilities);
      return expectedTriggeredAbilities.equals(actualTriggeredAbilities);
    });
  }

  private List<String> triggeredAbilities(WebElement containerElement) {
    return containerElement.findElements(By.className("triggered-ability")).stream()
      .map(WebElement::getText)
      .map(text -> text.replace("\n", " "))
      .collect(toList());
  }
}
