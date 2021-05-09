package application.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.matag.player.PlayerType;

public class LibraryHelper extends AbstractCardContainerHelper {
  private final PlayerType playerType;

  LibraryHelper(MatagBrowser matagBrowser, PlayerType playerType) {
    super(matagBrowser);
    this.playerType = playerType;
  }

  @Override
  protected WebElement containerElement() {
    return matagBrowser.findElement(By.id(playerType.name().toLowerCase() + "-library"));
  }

  public VisibleLibraryHelper getVisibleCardsHelper() {
    return new VisibleLibraryHelper(this.matagBrowser, playerType);
  }
}
