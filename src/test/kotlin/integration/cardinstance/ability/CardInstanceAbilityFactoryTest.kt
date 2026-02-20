package integration.cardinstance.ability

import com.matag.cards.ability.Ability
import com.matag.cards.ability.type.AbilityType
import com.matag.game.MatagGameApplication
import com.matag.game.cardinstance.ability.CardInstanceAbilityFactory
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [MatagGameApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestUtilsConfiguration::class)
class CardInstanceAbilityFactoryTest(
    private val cardInstanceAbilityFactory: CardInstanceAbilityFactory
) {


    @Test
    fun testAbilitiesFromParameters() {
        // Given
        val parameters = listOf("+2/+2", "TRAMPLE", "DAMAGE:2", "HASTE")

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
        assertThat(ability).isNull()
    }

    @Test
    fun testTrampleAbilityFromParameter() {
        // Given
        val parameter = "TRAMPLE"

        // When
        val ability = cardInstanceAbilityFactory.abilityFromParameter(parameter)

        // Then
        assertThat(ability).isEqualTo(Ability(AbilityType.TRAMPLE))
    }
}