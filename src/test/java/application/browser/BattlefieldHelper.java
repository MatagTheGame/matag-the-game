package application.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.matag.player.PlayerType;

public class BattlefieldHelper extends AbstractCardContainerHelper {
  public static final String FIRST_LINE = "first-line";
  public static final String SECOND_LINE = "second-line";
  public static final String COMBAT_LINE = "combat-line";

  private final PlayerType playerType;
  private final String lineType;

  BattlefieldHelper(MatagBrowser matagBrowser, PlayerType playerType, String lineType) {
    super(matagBrowser);
    this.playerType = playerType;
    this.lineType = lineType;
  }

  @Override
  protected WebElement containerElement() {
    return playerTypeContainer().findElement(By.className(lineType));
  }

  private WebElement playerTypeContainer() {
    if (playerType == PlayerType.PLAYER) {
      return matagBrowser.findElement(By.id("player-battlefield"));
    } else {
      return matagBrowser.findElement(By.id("opponent-battlefield"));
    }
  }
}
