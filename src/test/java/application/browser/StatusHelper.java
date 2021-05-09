package application.browser;

import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

import org.openqa.selenium.By;

public class StatusHelper {
  private MatagBrowser matagBrowser;

  StatusHelper(MatagBrowser matagBrowser) {
    this.matagBrowser = matagBrowser;
  }

  public void hasMessage(String message) {
    matagBrowser.wait(textToBe(By.id("status-message"), message));
  }
}
