package application.browser;

import com.aa.mtg.game.player.PlayerType;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.StringUtils;

import java.util.List;

public class MtgBrowser {
    private final WebDriver webDriver;

    public MtgBrowser(int port) {
        webDriver = getWebDriver();
        webDriver.get("http://localhost:" + port);
    }

    public void close() {
        webDriver.close();
    }

    public MessageHelper getMessageHelper() {
        return new MessageHelper(this);
    }

    public StatusHelper getStatusHelper() {
        return new StatusHelper(this);
    }

    public HandHelper getHandHelper(PlayerType playerType) {
        return new HandHelper(this, playerType);
    }

    public BattlefieldHelper getBattlefieldHelper(PlayerType playerType, String lineType) {
        return new BattlefieldHelper(this, playerType, lineType);
    }

    public GraveyardHelper getGraveyardHelper(PlayerType playerType) {
        return new GraveyardHelper(this, playerType);
    }

    public PlayerInfoHelper getPlayerInfoHelper(PlayerType playerType) {
        return new PlayerInfoHelper(this, playerType);
    }

    private WebDriver getWebDriver() {
        String property = System.getProperty("webdriver.chrome.driver");
        if (StringUtils.isEmpty(property)) {
            return new HtmlUnitDriver(BrowserVersion.CHROME, true);
        } else {
            return new ChromeDriver();
        }
    }

    WebElement findElement(By element) {
        return webDriver.findElement(element);
    }

    List<WebElement> findElements(By element) {
        return webDriver.findElements(element);
    }

    void wait(ExpectedCondition<Boolean> condition) {
        new WebDriverWait(webDriver, 5).until(condition);
    }
}
