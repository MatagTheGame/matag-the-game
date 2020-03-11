package application.browser;

import com.matag.player.PlayerType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class GraveyardHelper extends AbstractCardContainerHelper {

  private final PlayerType playerType;

  GraveyardHelper(MatagBrowser matagBrowser, PlayerType playerType) {
    super(matagBrowser);
    this.playerType = playerType;
  }

  @Override
  protected WebElement containerElement() {
    if (playerType == PlayerType.PLAYER) {
      return matagBrowser.findElement(By.id("player-graveyard"));
    } else {
      return matagBrowser.findElement(By.id("opponent-graveyard"));
    }
  }
}
