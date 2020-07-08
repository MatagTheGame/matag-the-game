package application.browser;

import org.openqa.selenium.By;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

public class MessageHelper {
  private MatagBrowser matagBrowser;

  MessageHelper(MatagBrowser matagBrowser) {
    this.matagBrowser = matagBrowser;
  }

  public void hasMessage(String message) {
    matagBrowser.wait(textToBe(By.id("message-text"), message));
  }

  public void hasNoMessage() {
    matagBrowser.wait(invisibilityOfElementLocated(By.id("message")));
  }

  public void close() {
    matagBrowser.findElement(By.id("popup-close")).click();
  }
}
