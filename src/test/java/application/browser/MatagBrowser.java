package application.browser;

import java.time.Duration;
import java.util.ArrayList;

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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.matag.player.PlayerType;

public class MatagBrowser {
  private final WebDriver webDriver;
  private final int port;

  private int player1TabIndex = 0;
  private int player2TabIndex = 1;

  public MatagBrowser(int port) {
    this.port = port;
    webDriver = getWebDriver();
    webDriver.get(getUrl());
  }

  public void openSecondTab() {
    ((JavascriptExecutor) webDriver).executeScript("window.open('" + getUrl() + "')");
    // Wait for it to fully load
    this.getMessageHelper().hasNoMessage();
  }

  public void swapTabs() {
    player1TabIndex = 1;
    player2TabIndex = 0;
  }

  public MatagBrowser player1() {
    tabAt(player1TabIndex);
    return this;
  }

  public MatagBrowser player2() {
    tabAt(player2TabIndex);
    return this;
  }

  public void close() {
    var numOfTabs = new ArrayList<>(webDriver.getWindowHandles()).size();
    for (var i = 0; i < numOfTabs; i++) {
      tabAt(0);
      webDriver.close();
    }
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

  public PlayerActiveManaHelper getPlayerActiveManaHelper() {
    return new PlayerActiveManaHelper(this);
  }

  public UserInputsHelper getPlayableAbilitiesHelper() {
    return new UserInputsHelper(this);
  }

  public StackHelper getStackHelper() {
    return new StackHelper(this);
  }

  public LibraryHelper getLibraryHelper(PlayerType playerType) {
    return new LibraryHelper(this, playerType);
  }

  public JavascriptExecutor getJavascriptExecutor() {
    return (JavascriptExecutor)webDriver;
  }

  private String getUrl() {
    return "http://localhost:" + port + "/ui/test-game";
  }

  private void tabAt(int index) {
    webDriver.switchTo().window(new ArrayList<>(webDriver.getWindowHandles()).get(index));
  }

  private WebDriver getWebDriver() {
    if (isChrome()) {
      var webdriverUserDataDir = System.getProperty("webdriver.chrome.userDataDir");
      var chromeOptions = new ChromeOptions();
      chromeOptions.addArguments("user-data-dir=" + webdriverUserDataDir);
      return new ChromeDriver(chromeOptions);

    } else {
      return new HtmlUnitDriver(BrowserVersion.CHROME, true);
    }
  }

  public boolean isChrome() {
    return StringUtils.hasText(System.getProperty("webdriver.chrome.driver"));
  }

  WebElement findElement(By element) {
    return webDriver.findElement(element);
  }

  void wait(ExpectedCondition<?> condition) {
    new WebDriverWait(webDriver, Duration.ofSeconds(1)).until(condition);
  }

  public void dumpContent() {
    System.out.println(webDriver.getPageSource());
  }
}
