package application.browser

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.stream.Collectors

class StackHelper internal constructor(matagBrowser: MatagBrowser?) : AbstractCardContainerHelper(matagBrowser) {
    override fun containerElement(): WebElement? {
        return matagBrowser.findElement(By.id("stack"))
    }

    fun containsAbility(ability: String?) {
        containsAbilitiesExactly(mutableListOf<String?>(ability))
    }

    fun containsAbilitiesExactly(expectedTriggeredAbilities: MutableList<String?>) {
        matagBrowser.wait(ExpectedCondition { driver: WebDriver? ->
            val actualTriggeredAbilities = triggeredAbilities(containerElement()!!)
            LOGGER.info(
                "actualTriggeredAbilities={}   expectedTriggeredAbilities={}",
                actualTriggeredAbilities,
                expectedTriggeredAbilities
            )
            expectedTriggeredAbilities == actualTriggeredAbilities
        })
    }

    private fun triggeredAbilities(containerElement: WebElement): MutableList<String?> {
        return containerElement.findElements(By.className("triggered-ability")).stream()
            .map<String?> { obj: WebElement? -> obj!!.getText() }
            .map<String?> { text: String? -> text!!.replace("\n", " ") }
            .collect(Collectors.toList())
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(StackHelper::class.java)
    }
}
