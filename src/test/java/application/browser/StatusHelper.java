package application.browser;

import org.openqa.selenium.By;

import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

public class StatusHelper {
    private MtgBrowser mtgBrowser;

    StatusHelper(MtgBrowser mtgBrowser) {
        this.mtgBrowser = mtgBrowser;
    }

    public void hasMessage(String message) {
        mtgBrowser.wait(textToBe(By.id("status-message"), message));
    }
}
