package application.browser

import com.matag.player.PlayerType
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class PlayerInfoHelper internal constructor(
    private val matagBrowser: MatagBrowser,
    private val playerType: PlayerType?
) {
    fun toHaveName() {
        matagBrowser.wait(ExpectedCondition { driver: WebDriver? ->
            LOGGER.info("playerName={}", this.playerName)
            !this.playerName.isEmpty()
        })
    }

    fun toHaveName(expectedPlayerName: String?) {
        matagBrowser.wait(ExpectedCondition { driver: WebDriver? ->
            LOGGER.info(
                "actualPlayerName={}   expectedPlayerName={}",
                this.playerName, expectedPlayerName
            )
            this.playerName == expectedPlayerName
        })
    }

    fun toHaveLife(expectedPlayerLife: Int) {
        matagBrowser.wait(ExpectedCondition { driver: WebDriver? ->
            LOGGER.info(
                "actualPlayerName={}   expectedPlayerLife={}",
                this.playerLife, expectedPlayerLife
            )
            this.playerLife == expectedPlayerLife.toString()
        })
    }

    val playerName: String
        get() = playerInfoElement()!!.findElements(By.tagName("span")).get(0).getText()

    val playerLife: String
        get() = playerInfoElement()!!.findElements(By.tagName("span")).get(1).getText()

    fun toBeActive() {
        toHaveClass("active-player")
    }

    fun toBeInactive() {
        toHaveClass("inactive-player")
    }

    fun click() {
        playerInfoElement()!!.click()
    }

    private fun toHaveClass(cssClass: String?) {
        matagBrowser.wait(ExpectedCondition { driver: WebDriver? ->
            val playerClasses = playerInfoElement()!!.getAttribute("class")
            LOGGER.info("playerClasses={}", playerClasses)
            Arrays.asList<String>(*playerClasses!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                .contains(cssClass)
        })
    }

    private fun playerInfoElement(): WebElement? {
        if (playerType == PlayerType.PLAYER) {
            return matagBrowser.findElement(By.id("player-info"))
        } else {
            return matagBrowser.findElement(By.id("opponent-info"))
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(PlayerInfoHelper::class.java)
    }
}
