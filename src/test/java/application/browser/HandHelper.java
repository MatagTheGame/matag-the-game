package application.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.matag.player.PlayerType;

public class HandHelper extends AbstractCardContainerHelper {
  private final PlayerType playerType;

  HandHelper(MatagBrowser matagBrowser, PlayerType playerType) {
    super(matagBrowser);
    this.playerType = playerType;
  }

  @Override
  protected WebElement containerElement() {
    if (playerType == PlayerType.PLAYER) {
      return matagBrowser.findElement(By.id("player-hand"));
    } else {
      return matagBrowser.findElement(By.id("opponent-hand"));
    }
  }
}
