package application.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class UserInputsHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserInputsHelper.class);

  private final MatagBrowser matagBrowser;

  UserInputsHelper(MatagBrowser matagBrowser) {
    this.matagBrowser = matagBrowser;
  }

  public void toHaveAbilities(List<String> expectedAbilities) {
    matagBrowser.wait(driver -> {
      var abilities = userInputs().stream().map(ability -> ability.getAttribute("title")).collect(Collectors.toList());
      LOGGER.info("abilities={}   expectedAbilities={}", abilities, expectedAbilities);
      return expectedAbilities.equals(abilities);
    });
  }

  public void choose(int index) {
    userInputs().get(index).click();
  }

  private List<WebElement> userInputs() {
    return getElementContainer().findElements(By.tagName("li"));
  }

  private WebElement getElementContainer() {
    return matagBrowser.findElement(By.id("user-inputs"));
  }
}
