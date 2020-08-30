package application.browser;

import com.matag.player.PlayerType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
