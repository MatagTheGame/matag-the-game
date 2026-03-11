package application.browser

import com.matag.cards.Card
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractCardContainerHelper internal constructor(val matagBrowser: MatagBrowser) {
    fun containsExactly(vararg expectedCards: Card) {
        containsExactly(*toCardNamesArray(expectedCards))
    }

    fun containsExactly(vararg expectedCardsNames: String) {
        matagBrowser.wait(ExpectedCondition {
            val actualCardNames = CardHelper.cardNames(containerElement())
            LOGGER.info("actualCardNames={}   expectedCardsNames={}", actualCardNames, expectedCardsNames.toList())
            expectedCardsNames.toList() == actualCardNames
        })
    }

    fun contains(vararg expectedCards: Card) {
        contains(*toCardNamesArray(expectedCards))
    }

    fun contains(vararg expectedCardsNames: String) {
        matagBrowser.wait(ExpectedCondition {
            val actualCardNames = CardHelper.cardNames(containerElement())
            LOGGER.info("actualCardNames={}   expectedCard={}", actualCardNames, expectedCardsNames.toList())
            actualCardNames.containsAll(expectedCardsNames.toList())
        })
    }

    fun doesNotContain(expectedCard: Card) {
        matagBrowser.wait(ExpectedCondition {
            val actualCardNames = CardHelper.cardNames(containerElement())
            LOGGER.info("actualCardNames={}   expectedCard={}", actualCardNames, expectedCard)
            !actualCardNames.contains(expectedCard.name)
        })
    }

    val isEmpty: Unit
        get() {
            toHaveSize(0)
        }

    fun toHaveSize(size: Int) {
        matagBrowser.wait(ExpectedCondition {
            val cardNames: List<String> = CardHelper.cardNames(containerElement())
            if (cardNames.size == size) {
                return@ExpectedCondition true
            } else {
                LOGGER.info("Expected {} cards but got {} ({})", size, cardNames.size, cardNames)
                return@ExpectedCondition false
            }
        })
    }

    fun getFirstCard(card: Card): CardHelper {
        return getCard(card, 0)
    }

    fun getCard(card: Card, index: Int): CardHelper {
        matagBrowser.wait(ExpectedCondition {
            CardHelper.cardNames(containerElement())
                .count { it == card.name } > index
        })
        val webElement = containerElement().findElements(By.cssSelector("[aria-label=\"" + card.name + "\"]"))[index]
        return CardHelper(webElement, matagBrowser)
    }

    protected abstract fun containerElement(): WebElement

    private fun toCardNamesArray(cards: Array<out Card>): Array<String> =
        CardHelper.cardNames(cards.toList()).toTypedArray<String>()

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(AbstractCardContainerHelper::class.java)
    }
}
