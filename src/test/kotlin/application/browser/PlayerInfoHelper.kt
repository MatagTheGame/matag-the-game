package application.browser

import com.matag.player.PlayerType
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PlayerInfoHelper internal constructor(
    private val matagBrowser: MatagBrowser,
    private val playerType: PlayerType
) {
    fun toHaveName() {
        matagBrowser.wait(ExpectedCondition {
            LOGGER.info("playerName={}", this.playerName)
            !this.playerName.isEmpty()
        })
    }

    fun toHaveName(expectedPlayerName: String?) {
        matagBrowser.wait(ExpectedCondition {
            LOGGER.info(
                "actualPlayerName={}   expectedPlayerName={}",
                this.playerName, expectedPlayerName
            )
            this.playerName == expectedPlayerName
        })
    }

    fun toHaveLife(expectedPlayerLife: Int) {
        matagBrowser.wait(ExpectedCondition {
            LOGGER.info(
                "actualPlayerName={}   expectedPlayerLife={}",
                this.playerLife, expectedPlayerLife
            )
            this.playerLife == expectedPlayerLife.toString()
        })
    }

    val playerName: String
        get() = playerInfoElement().findElements(By.tagName("span"))[0].text

    val playerLife: String
        get() = playerInfoElement().findElements(By.tagName("span"))[1].text

    fun toBeActive() {
        toHaveClass("active-player")
    }

    fun toBeInactive() {
        toHaveClass("inactive-player")
    }

    fun click() {
        playerInfoElement().click()
    }

    private fun toHaveClass(cssClass: String?) {
        matagBrowser.wait(ExpectedCondition {
            val playerClasses = playerInfoElement().getAttribute("class")
            LOGGER.info("playerClasses={}", playerClasses)
            playerClasses?.split(' ')?.contains(cssClass) == true
        })
    }

    private fun playerInfoElement(): WebElement {
        return if (playerType == PlayerType.PLAYER) {
            matagBrowser.findElement(By.id("player-info"))
        } else {
            matagBrowser.findElement(By.id("opponent-info"))
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(PlayerInfoHelper::class.java)
    }
}
