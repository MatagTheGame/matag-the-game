package application.browser;

import com.matag.cards.properties.Color;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerActiveManaHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlayerActiveManaHelper.class);

  private MatagBrowser matagBrowser;

  PlayerActiveManaHelper(MatagBrowser matagBrowser) {
    this.matagBrowser = matagBrowser;
  }

  public void toHaveMana(List<Color> colors) {
    matagBrowser.wait(driver -> {
      List<WebElement> imgs = getElementContainer().findElements(By.tagName("img"));
      List<String> alts = imgs.stream().map((webElement -> webElement.getAttribute("alt"))).sorted().collect(Collectors.toList());
      List<String> expectedColors = colors.stream().map(Enum::name).sorted().collect(Collectors.toList());
      LOGGER.info("alts={}   expectedColors={}", alts, expectedColors);
      return expectedColors.equals(alts);
    });
  }

  private WebElement getElementContainer() {
    return matagBrowser.findElement(By.id("player-active-mana"));
  }
}
