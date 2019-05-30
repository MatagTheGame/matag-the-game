package application.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
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
            try {
                WebElement webElement = mtgBrowser.findElement(By.id("message"));

                if (!webElement.getText().equals(message)) {
                    LOGGER.info("Waiting for Message to have content: '{}' but is '{}'", message, webElement.getText());
                    return false;
                }

                return true;

            } catch (NotFoundException e) {
                LOGGER.info("Waiting for Message to appear...");
                return false;
            }
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
