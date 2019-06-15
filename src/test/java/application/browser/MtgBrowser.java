package application.browser;

import com.aa.mtg.game.player.PlayerType;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MtgBrowser {
    private final WebDriver webDriver;
    private final int port;

    public MtgBrowser(int port) {
        this.port = port;
        webDriver = getWebDriver();
        webDriver.get("http://localhost:" + port);
    }

    public void openSecondTab() {
        ((JavascriptExecutor)webDriver).executeScript("window.open('http://localhost:" + port + "')");
    }

    public MtgBrowser player1() {
        webDriver.switchTo().window(new ArrayList<>(webDriver.getWindowHandles()).get(0));
        return this;
    }

    public MtgBrowser player2() {
        webDriver.switchTo().window(new ArrayList<>(webDriver.getWindowHandles()).get(1));
        return this;
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

    public ActionHelper getActionHelper() {
        return new ActionHelper(this);
    }

    public PhaseHelper getPhaseHelper() {
        return new PhaseHelper(this);
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

    public StackHelper getStackHelper() {
        return new StackHelper(this);
    }

    private WebDriver getWebDriver() {
        String webdriverChromeDriver = System.getProperty("webdriver.chrome.driver");
        String webdriverUserDataDir = System.getProperty("webdriver.chrome.userDataDir");

        if (StringUtils.hasText(webdriverChromeDriver)) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("user-data-dir=" + webdriverUserDataDir);
            return new ChromeDriver(chromeOptions);

        } else {
            return new HtmlUnitDriver(BrowserVersion.CHROME, true);
        }
    }

    WebElement findElement(By element) {
        return webDriver.findElement(element);
    }

    List<WebElement> findElements(By element) {
        return webDriver.findElements(element);
    }

    void wait(ExpectedCondition<?> condition) {
        new WebDriverWait(webDriver, 5).until(condition);
    }

}
