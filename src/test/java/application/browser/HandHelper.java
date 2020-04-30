package application.browser;

import com.matag.player.PlayerType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
