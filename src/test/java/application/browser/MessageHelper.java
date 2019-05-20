package application.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandHelper.class);

    private MtgBrowser mtgBrowser;

    MessageHelper(MtgBrowser mtgBrowser) {
        this.mtgBrowser = mtgBrowser;
    }

    public void hasMessage(String message) {
        mtgBrowser.wait(driver -> {
            WebElement webElement = mtgBrowser.findElement(By.id("message"));

            if (webElement == null) {
                LOGGER.info("Waiting for Message to appear.");
                return false;
            }

            if (!webElement.getText().equals(message)) {
                LOGGER.info("Waiting for Message to have content: '{}'", message);
                return false;
            }

            return true;
        });
    }

    public void hasNoMessage() {
        mtgBrowser.wait(driver -> {
            if (mtgBrowser.hasNoElement(By.id("message"))) {
                return true;
            } else {
                String message = mtgBrowser.findElement(By.id("message")).getText();
                LOGGER.info("Waiting for message '{}' to disappear.", message);
                return false;
            }
        });
    }
}
