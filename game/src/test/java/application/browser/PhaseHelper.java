package application.browser;

import com.matag.player.PlayerType;
import org.openqa.selenium.By;

import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;
import static org.openqa.selenium.support.ui.ExpectedConditions.attributeContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

public class PhaseHelper {
  public static final By PHASE_CSS_SELECTOR = By.cssSelector("#turn-phases .active");
  public static final By PRIORITY_CSS_SELECTOR = By.cssSelector("#turn-phases .active");

  private MatagBrowser matagBrowser;

  PhaseHelper(MatagBrowser matagBrowser) {
    this.matagBrowser = matagBrowser;
  }

  public void is(String phase, PlayerType priority) {
    matagBrowser.wait(textToBe(PHASE_CSS_SELECTOR, phase));
    isPriority(priority);
  }

  public void isPriority(PlayerType priority) {
    matagBrowser.wait(attributeContains(PRIORITY_CSS_SELECTOR, "class", getClassNameLinkedToPriority(priority)));
  }

  public String getPhase() {
    return matagBrowser.findElement(PHASE_CSS_SELECTOR).getText();
  }

  public PlayerType getPriority() {
    String classes = matagBrowser.findElement(PRIORITY_CSS_SELECTOR).getAttribute("class");
    if (classes.contains("active-for-player")) {
      return PLAYER;
    } else {
      return OPPONENT;
    }
  }

  private String getClassNameLinkedToPriority(PlayerType priority) {
    if (priority == PLAYER) {
      return "active-for-player";
    } else {
      return "active-for-opponent";
    }
  }
}
