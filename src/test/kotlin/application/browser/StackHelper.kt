package application.browser

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class StackHelper internal constructor(matagBrowser: MatagBrowser) : AbstractCardContainerHelper(matagBrowser) {
    override fun containerElement(): WebElement {
        return matagBrowser.findElement(By.id("stack"))
    }

    fun containsAbility(ability: String) {
        containsAbilitiesExactly(listOf(ability))
    }

    fun containsAbilitiesExactly(expectedTriggeredAbilities: List<String>) {
        matagBrowser.wait(ExpectedCondition {
            val actualTriggeredAbilities = triggeredAbilities(containerElement())
            LOGGER.info(
                "actualTriggeredAbilities={}   expectedTriggeredAbilities={}",
                actualTriggeredAbilities,
                expectedTriggeredAbilities
            )
            expectedTriggeredAbilities == actualTriggeredAbilities
        })
    }

    private fun triggeredAbilities(containerElement: WebElement): List<String> {
        return containerElement.findElements(By.className("triggered-ability"))
            .map { it.text }
            .map { it.replace("\n", " ") }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(StackHelper::class.java)
    }
}
