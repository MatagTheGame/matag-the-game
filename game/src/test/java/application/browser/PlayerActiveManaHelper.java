package application.browser;

import com.aa.mtg.cards.properties.Color;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerActiveManaHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlayerActiveManaHelper.class);

  private MtgBrowser mtgBrowser;

  PlayerActiveManaHelper(MtgBrowser mtgBrowser) {
    this.mtgBrowser = mtgBrowser;
  }

  public void toHaveMana(List<Color> colors) {
    mtgBrowser.wait(driver -> {
      List<WebElement> imgs = getElementContainer().findElements(By.tagName("img"));
      List<String> alts = imgs.stream().map((webElement -> webElement.getAttribute("alt"))).sorted().collect(Collectors.toList());
      List<String> expectedColors = colors.stream().map(Enum::name).sorted().collect(Collectors.toList());
      LOGGER.info("alts={}   expectedColors={}", alts, expectedColors);
      return expectedColors.equals(alts);
    });
  }

  private WebElement getElementContainer() {
    return mtgBrowser.findElement(By.id("player-active-mana"));
  }
}
