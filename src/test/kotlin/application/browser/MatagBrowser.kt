package application.browser

import com.matag.player.PlayerType
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class MatagBrowser(private val port: Int) {
    private val webDriver: WebDriver

    private var player1TabIndex = 0
    private var player2TabIndex = 1

    init {
        webDriver = getWebDriver()
        webDriver.get(this.url)
    }

    fun openSecondTab() {
        (webDriver as JavascriptExecutor).executeScript("window.open('" + this.url + "')")
        // Wait for it to fully load
        this.messageHelper.hasNoMessage()
    }

    fun swapTabs() {
        player1TabIndex = 1
        player2TabIndex = 0
    }

    fun player1(): MatagBrowser {
        tabAt(player1TabIndex)
        return this
    }

    fun player2(): MatagBrowser {
        tabAt(player2TabIndex)
        return this
    }

    fun close() {
        val numOfTabs = ArrayList<String?>(webDriver.getWindowHandles()).size
        for (i in 0..<numOfTabs) {
            tabAt(0)
            webDriver.close()
        }
    }

    val messageHelper: MessageHelper
        get() = MessageHelper(this)

    val statusHelper: StatusHelper
        get() = StatusHelper(this)

    val actionHelper: ActionHelper
        get() = ActionHelper(this)

    val phaseHelper: PhaseHelper
        get() = PhaseHelper(this)

    fun getHandHelper(playerType: PlayerType?): HandHelper {
        return HandHelper(this, playerType)
    }

    fun getBattlefieldHelper(playerType: PlayerType?, lineType: String?): BattlefieldHelper {
        return BattlefieldHelper(this, playerType, lineType)
    }

    fun getGraveyardHelper(playerType: PlayerType?): GraveyardHelper {
        return GraveyardHelper(this, playerType)
    }

    fun getPlayerInfoHelper(playerType: PlayerType?): PlayerInfoHelper {
        return PlayerInfoHelper(this, playerType)
    }

    val playerActiveManaHelper: PlayerActiveManaHelper
        get() = PlayerActiveManaHelper(this)

    val playableAbilitiesHelper: UserInputsHelper
        get() = UserInputsHelper(this)

    val stackHelper: StackHelper
        get() = StackHelper(this)

    fun getLibraryHelper(playerType: PlayerType?): LibraryHelper {
        return LibraryHelper(this, playerType)
    }

    val javascriptExecutor: JavascriptExecutor?
        get() = webDriver as JavascriptExecutor?

    private val url: String
        get() = "http://localhost:" + port + "/matag/game/ui/test-game"

    private fun tabAt(index: Int) {
        webDriver.switchTo().window(ArrayList<String?>(webDriver.getWindowHandles()).get(index)!!)
    }

    private fun getWebDriver(): WebDriver {
        val chromeOptions = ChromeOptions()

        val webdriverUserDataDir = System.getProperty("webdriver.chrome.userDataDir")
        chromeOptions.addArguments("user-data-dir=" + webdriverUserDataDir)

        val isHeadless = "true".equals(System.getProperty("webdriver.chrome.headless"), ignoreCase = true)
        if (isHeadless) {
            chromeOptions.addArguments("--headless=new")
            chromeOptions.addArguments("--no-sandbox")
            chromeOptions.addArguments("--disable-dev-shm-usage")
            chromeOptions.addArguments("--window-size=1920,1080")
            chromeOptions.addArguments("--start-maximized")
        }

        return ChromeDriver(chromeOptions)
    }

    fun findElement(element: By): WebElement {
        wait(ExpectedConditions.presenceOfElementLocated(element))
        return webDriver.findElement(element)
    }

    fun wait(condition: ExpectedCondition<*>?) {
        WebDriverWait(webDriver, Duration.ofMillis(1500L), Duration.ofMillis(250L)).until(condition)
    }

    fun dumpContent() {
        println(webDriver.getPageSource())
    }
}
