package application.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
        canContinue();
        mtgBrowser.findElement(getContinueSelector()).click();
    }

    private By getContinueSelector() {
        return By.id("continue-button");
    }
}
