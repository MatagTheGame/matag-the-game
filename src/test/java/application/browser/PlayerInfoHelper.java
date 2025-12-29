package application.browser;

import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.matag.player.PlayerType;

public class PlayerInfoHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlayerInfoHelper.class);

  private final MatagBrowser matagBrowser;
  private final PlayerType playerType;

  PlayerInfoHelper(MatagBrowser matagBrowser, PlayerType playerType) {
    this.matagBrowser = matagBrowser;
    this.playerType = playerType;
  }

  public void toHaveName() {
    matagBrowser.wait(driver -> {
      LOGGER.info("playerName={}", getPlayerName());
      return !getPlayerName().isEmpty();
    });
  }

  public void toHaveName(String expectedPlayerName) {
    matagBrowser.wait(driver -> {
      LOGGER.info("actualPlayerName={}   expectedPlayerName={}", getPlayerName(), expectedPlayerName);
      return getPlayerName().equals(expectedPlayerName);
    });
  }

  public void toHaveLife(int expectedPlayerLife) {
    matagBrowser.wait(driver -> {
      LOGGER.info("actualPlayerName={}   expectedPlayerLife={}", getPlayerLife(), expectedPlayerLife);
      return getPlayerLife().equals(String.valueOf(expectedPlayerLife));
    });
  }

  public String getPlayerName() {
    return playerInfoElement().findElements(By.tagName("span")).get(0).getText();
  }

  public String getPlayerLife() {
    return playerInfoElement().findElements(By.tagName("span")).get(1).getText();
  }

  public void toBeActive() {
    toHaveClass("active-player");
  }

  public void toBeInactive() {
    toHaveClass("inactive-player");
  }

  public void click() {
    playerInfoElement().click();
  }

  private void toHaveClass(String cssClass) {
    matagBrowser.wait(driver -> {
      var playerClasses = playerInfoElement().getAttribute("class");
      LOGGER.info("playerClasses={}", playerClasses);
      return Arrays.asList(playerClasses.split(" ")).contains(cssClass);
    });
  }

  private WebElement playerInfoElement() {
    if (playerType == PlayerType.PLAYER) {
      return matagBrowser.findElement(By.id("player-info"));
    } else {
      return matagBrowser.findElement(By.id("opponent-info"));
    }
  }
}
