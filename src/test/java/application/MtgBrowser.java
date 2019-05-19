package application;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MtgBrowser {
    private final WebDriver webDriver;

    public MtgBrowser(int port) {
        webDriver = getWebDriver();
        webDriver.get("http://localhost:" + port);
    }

    public void close() {
        webDriver.close();
    }

    public void displaysMessage(String message) {
        wait(driver -> {
            WebElement webElement = webDriver.findElement(By.id("message"));
            return webElement != null && webElement.getText().equals(message);
        });
    }

    public void hasNoMessage() {
        wait(driver -> hasNoElement(By.id("message")));
    }

    private WebDriver getWebDriver() {
        String property = System.getProperty("webdriver.chrome.driver");
        if (StringUtils.isBlank(property)) {
            return new HtmlUnitDriver(BrowserVersion.CHROME, true);
        } else {
            return new ChromeDriver();
        }
    }

    private Boolean hasNoElement(By element) {
        try {
            webDriver.findElement(element);
            return false;
        } catch (NotFoundException e) {
            return true;
        }
    }

    private void wait(ExpectedCondition<Boolean> condition) {
        new WebDriverWait(webDriver, 5).until(condition);
    }
}
