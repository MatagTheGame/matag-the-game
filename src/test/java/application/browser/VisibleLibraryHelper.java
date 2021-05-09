package application.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.matag.player.PlayerType;

public class VisibleLibraryHelper extends AbstractCardContainerHelper {
  private final PlayerType playerType;

  public VisibleLibraryHelper(MatagBrowser libraryHelper, PlayerType playerType) {
    super(libraryHelper);
    this.playerType = playerType;
  }

  @Override
  protected WebElement containerElement() {
    return matagBrowser.findElement(By.id(playerType.name().toLowerCase() + "-library"))
      .findElement(By.className("visible-cards"));
  }
}
