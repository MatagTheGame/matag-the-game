package integration.cardinstance.ability

import com.matag.cards.ability.Ability
import com.matag.cards.ability.CardInstanceAbilityFactory
import com.matag.cards.ability.type.AbilityType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class CardInstanceAbilityFactoryTest {
    private val cardInstanceAbilityFactory = CardInstanceAbilityFactory()

    @Test
    fun testAbilitiesFromParameters() {
        // Given
        val parameters = mutableListOf<String?>("+2/+2", "TRAMPLE", "DAMAGE:2", "HASTE")

        // When
        val abilities = cardInstanceAbilityFactory.abilitiesFromParameters(parameters)

        // Then
        assertThat(abilities).isEqualTo(
            listOf(
                Ability(AbilityType.TRAMPLE),
                Ability(AbilityType.HASTE)
            )
        )
    }

    @Test
    fun testNoAbilityFromParameter() {
        // Given
        val parameter = "+2/+2"

        // When
        val ability = cardInstanceAbilityFactory.abilityFromParameter(parameter)

        // Then
        assertThat(ability).isEmpty()
    }

    @Test
    fun testTrampleAbilityFromParameter() {
        // Given
        val parameter = "TRAMPLE"

        // When
        val ability = cardInstanceAbilityFactory.abilityFromParameter(parameter)

        // Then
        assertThat(ability).isEqualTo(Optional.of(Ability(AbilityType.TRAMPLE)))
    }
}