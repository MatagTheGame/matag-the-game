package application.browser;

import org.openqa.selenium.By;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

public class MessageHelper {
    private MtgBrowser mtgBrowser;

    MessageHelper(MtgBrowser mtgBrowser) {
        this.mtgBrowser = mtgBrowser;
    }

    public void hasMessage(String message) {
        mtgBrowser.wait(textToBe(By.id("message-text"), message));
    }

    public void hasNoMessage() {
        mtgBrowser.wait(invisibilityOfElementLocated(By.id("message")));
    }

    public void close() {
        mtgBrowser.findElement(By.id("message-close")).click();
    }
}
