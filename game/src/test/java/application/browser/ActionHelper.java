package application.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class ActionHelper {
    private MtgBrowser mtgBrowser;

    ActionHelper(MtgBrowser mtgBrowser) {
        this.mtgBrowser = mtgBrowser;
    }

    public void canContinue() {
        mtgBrowser.wait(elementToBeClickable(getContinueSelector()));
    }

    public void cannotContinue() {
        mtgBrowser.wait(ExpectedConditions.not(elementToBeClickable(getContinueSelector())));
    }

    public void clickContinue() {
        mtgBrowser.getPhaseHelper().isPriority(PLAYER);
        canContinue();
        mtgBrowser.findElement(getContinueSelector()).click();
    }

    private By getContinueSelector() {
        return By.id("continue-button");
    }
}
