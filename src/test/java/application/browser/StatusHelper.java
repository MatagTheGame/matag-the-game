package application.browser;

import org.openqa.selenium.By;

import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

public class StatusHelper {
  private MatagBrowser matagBrowser;

  StatusHelper(MatagBrowser matagBrowser) {
    this.matagBrowser = matagBrowser;
  }

  public void hasMessage(String message) {
    matagBrowser.wait(textToBe(By.id("status-message"), message));
  }
}
