package application.browser

import com.matag.cards.Card
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

abstract class AbstractCardContainerHelper internal constructor(val matagBrowser: MatagBrowser) {
    fun containsExactly(vararg expectedCards: Card?) {
        containsExactly(*CardHelper.Companion.cardNames(*expectedCards).toTypedArray<String?>())
    }

    fun containsExactly(vararg expectedCardsNames: String?) {
        val expectedCardsNamesList = Arrays.asList<String?>(*expectedCardsNames)
        matagBrowser.wait(ExpectedCondition { driver: WebDriver? ->
            val actualCardNames: MutableList<String?> = CardHelper.Companion.cardNames(containerElement())
            LOGGER.info("actualCardNames={}   expectedCardsNames={}", actualCardNames, expectedCardsNamesList)
            expectedCardsNamesList == actualCardNames
        })
    }

    fun contains(vararg expectedCards: Card?) {
        contains(*CardHelper.Companion.cardNames(*expectedCards).toTypedArray<String?>())
    }

    fun contains(vararg expectedCardsNames: String?) {
        val expectedCardsNamesList = Arrays.asList<String?>(*expectedCardsNames)
        matagBrowser.wait(ExpectedCondition { driver: WebDriver? ->
            val actualCardNames: MutableList<String?> = CardHelper.Companion.cardNames(containerElement())
            LOGGER.info("actualCardNames={}   expectedCard={}", actualCardNames, expectedCardsNamesList)
            actualCardNames.containsAll(expectedCardsNamesList)
        })
    }

    fun doesNotContain(expectedCard: Card) {
        matagBrowser.wait(ExpectedCondition { driver: WebDriver? ->
            val actualCardNames: MutableList<String?> = CardHelper.Companion.cardNames(containerElement())
            LOGGER.info("actualCardNames={}   expectedCard={}", actualCardNames, expectedCard)
            !actualCardNames.contains(expectedCard.name)
        })
    }

    val isEmpty: Unit
        get() {
            toHaveSize(0)
        }

    fun toHaveSize(size: Int) {
        matagBrowser.wait(ExpectedCondition { driver: WebDriver? ->
            val cardNames: MutableList<String?> = CardHelper.Companion.cardNames(containerElement())
            if (cardNames.size == size) {
                return@wait true
            } else {
                LOGGER.info("Expected {} cards but got {} ({})", size, cardNames.size, cardNames)
                return@wait false
            }
        })
    }

    fun getFirstCard(card: Card): CardHelper {
        return getCard(card, 0)
    }

    fun getCard(card: Card, index: Int): CardHelper {
        matagBrowser.wait(ExpectedCondition { driver: WebDriver? ->
            CardHelper.Companion.cardNames(containerElement()).stream()
                .filter { cardName: String? -> cardName == card.name }
                .count() > index
        })
        val webElement =
            containerElement()!!.findElements(By.cssSelector("[aria-label=\"" + card.name + "\"]")).get(index)
        return CardHelper(webElement, matagBrowser)
    }

    protected abstract fun containerElement(): WebElement?

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(AbstractCardContainerHelper::class.java)
    }
}
