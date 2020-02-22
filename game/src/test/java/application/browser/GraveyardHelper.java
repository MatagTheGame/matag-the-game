package application.browser;

import com.aa.mtg.player.PlayerType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class GraveyardHelper extends AbstractCardContainerHelper {

  private final PlayerType playerType;

  GraveyardHelper(MtgBrowser mtgBrowser, PlayerType playerType) {
    super(mtgBrowser);
    this.playerType = playerType;
  }

  @Override
  protected WebElement containerElement() {
    if (playerType == PlayerType.PLAYER) {
      return mtgBrowser.findElement(By.id("player-graveyard"));
    } else {
      return mtgBrowser.findElement(By.id("opponent-graveyard"));
    }
  }
}
