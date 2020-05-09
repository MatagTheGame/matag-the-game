package application.browser;

import com.matag.player.PlayerType;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class ActionHelper {
  private MatagBrowser matagBrowser;

  ActionHelper(MatagBrowser matagBrowser) {
    this.matagBrowser = matagBrowser;
  }

  public void canContinue() {
    matagBrowser.wait(elementToBeClickable(getContinueSelector()));
  }

  public void cannotContinue() {
    matagBrowser.wait(ExpectedConditions.not(elementToBeClickable(getContinueSelector())));
  }

  public void clickContinue() {
    matagBrowser.getPhaseHelper().isPriority(PLAYER);
    canContinue();
    matagBrowser.findElement(getContinueSelector()).click();
  }

  public void clickContinueAndExpectPhase(String expectStatus, PlayerType player) {
    clickContinue();
    matagBrowser.player1().getPhaseHelper().is(expectStatus, player);
    matagBrowser.player2().getPhaseHelper().is(expectStatus, other(player));
  }

  private By getContinueSelector() {
    return By.id("continue-button");
  }

  private PlayerType other(PlayerType player) {
    if (PLAYER == player) {
      return OPPONENT;
    } else {
      return PLAYER;
    }
  }
}
