package application.browser

import com.matag.player.PlayerType
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PhaseHelper internal constructor(private val matagBrowser: MatagBrowser) {
    fun `is`(phase: String, priority: PlayerType?) {
        matagBrowser.wait(ExpectedConditions.textToBe(PHASE_CSS_SELECTOR, phase))
        isPriority(priority)
    }

    fun isPriority(priority: PlayerType?) {
        try {
            matagBrowser.wait(
                ExpectedConditions.attributeContains(
                    PRIORITY_CSS_SELECTOR,
                    "class",
                    getClassNameLinkedToPriority(priority)
                )
            )
        } catch (e: Exception) {
            LOGGER.warn("PhaseAndPriority:{} {}", this.phase, this.priority)
            throw e
        }
    }

    val phase: String
        get() {
            matagBrowser.wait(ExpectedConditions.visibilityOfElementLocated(PHASE_CSS_SELECTOR))
            return matagBrowser.findElement(PHASE_CSS_SELECTOR).getText()
        }

    val priority: PlayerType
        get() {
            matagBrowser.wait(ExpectedConditions.visibilityOfElementLocated(PRIORITY_CSS_SELECTOR))
            val classes =
                matagBrowser.findElement(PRIORITY_CSS_SELECTOR).getAttribute("class")
            if (classes!!.contains("active-for-player")) {
                return PlayerType.PLAYER
            } else {
                return PlayerType.OPPONENT
            }
        }

    private fun getClassNameLinkedToPriority(priority: PlayerType?): String {
        if (priority == PlayerType.PLAYER) {
            return "active-for-player"
        } else {
            return "active-for-opponent"
        }
    }

    companion object {
        val PHASE_CSS_SELECTOR: By = By.cssSelector("#turn-phases .active")
        val PRIORITY_CSS_SELECTOR: By = By.cssSelector("#turn-phases .active")

        private val LOGGER: Logger = LoggerFactory.getLogger(PhaseHelper::class.java)
    }
}
