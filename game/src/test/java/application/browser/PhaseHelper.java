package application.browser;

import com.matag.player.PlayerType;
import org.openqa.selenium.By;

import static com.matag.player.PlayerType.PLAYER;
import static org.openqa.selenium.support.ui.ExpectedConditions.attributeContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

public class PhaseHelper {
  private MatagBrowser matagBrowser;

  PhaseHelper(MatagBrowser matagBrowser) {
    this.matagBrowser = matagBrowser;
  }

  public void is(String phase, PlayerType priority) {
    matagBrowser.wait(textToBe(By.cssSelector("#turn-phases .active"), phase));
    isPriority(priority);
  }

  public void isPriority(PlayerType priority) {
    matagBrowser.wait(attributeContains(By.cssSelector("#turn-phases .active"), "class", getClassNameLinkedToPriority(priority)));
  }

  private String getClassNameLinkedToPriority(PlayerType priority) {
    if (priority == PLAYER) {
      return "active-for-player";
    } else {
      return "active-for-opponent";
    }
  }
}
