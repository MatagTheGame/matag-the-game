package application.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class MessageHelper {
    private MtgBrowser mtgBrowser;

    MessageHelper(MtgBrowser mtgBrowser) {
        this.mtgBrowser = mtgBrowser;
    }

    public void hasMessage(String message) {
        mtgBrowser.wait(driver -> {
            WebElement webElement = mtgBrowser.findElement(By.id("message"));
            return webElement != null && webElement.getText().equals(message);
        });
    }

    public void hasNoMessage() {
        mtgBrowser.wait(driver -> mtgBrowser.hasNoElement(By.id("message")));
    }
}
