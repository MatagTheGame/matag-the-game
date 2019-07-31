package application.browser;

import com.aa.mtg.game.player.PlayerType;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

public class MtgBrowser {
    private final WebDriver webDriver;
    private final int port;

    private int player1TabIndex = 0;
    private int player2TabIndex = 1;

    public MtgBrowser(int port) {
        this.port = port;
        webDriver = getWebDriver();
        webDriver.get("http://localhost:" + port);
    }

    public void openSecondTab() {
        ((JavascriptExecutor)webDriver).executeScript("window.open('http://localhost:" + port + "')");
        // Wait for it to fully load
        this.getMessageHelper().hasNoMessage();
    }

    public void swapTabs() {
        player1TabIndex = 1;
        player2TabIndex = 0;
    }

    public MtgBrowser player1() {
        tabAt(player1TabIndex);
        return this;
    }

    public MtgBrowser player2() {
        tabAt(player2TabIndex);
        return this;
    }

    public void close() {
        tabAt(1);
        webDriver.close();
        tabAt(0);
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

    private void tabAt(int player2TabIndex) {
        webDriver.switchTo().window(new ArrayList<>(webDriver.getWindowHandles()).get(player2TabIndex));
    }

    private WebDriver getWebDriver() {
        String webdriverChromeDriver = System.getProperty("webdriver.chrome.driver");
        String webdriverUserDataDir = System.getProperty("webdriver.chrome.userDataDir");
        String webdriverFirefox = System.getProperty("webdriver.gecko.driver");

        if (StringUtils.hasText(webdriverChromeDriver)) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("user-data-dir=" + webdriverUserDataDir);
            return new ChromeDriver(chromeOptions);

        } else if (StringUtils.hasText(webdriverFirefox)) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            return new FirefoxDriver(options);

        } else {
            return new HtmlUnitDriver(BrowserVersion.CHROME, true);
        }
    }

    WebElement findElement(By element) {
        return webDriver.findElement(element);
    }

    void wait(ExpectedCondition<?> condition) {
        new WebDriverWait(webDriver, 5).until(condition);
    }

    public void dumpContent() {
        System.out.println(webDriver.getPageSource());
    }
}
