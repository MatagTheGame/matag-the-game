package integration.turn.action.selection

import com.matag.cards.Cards
import com.matag.cards.ability.selector.*
import com.matag.cards.ability.selector.PowerToughnessConstraint.PowerOrToughness
import com.matag.cards.ability.selector.SelectorType.PERMANENT
import com.matag.cards.ability.type.AbilityType
import com.matag.cards.properties.Color
import com.matag.cards.properties.Subtype
import com.matag.cards.properties.Type
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.selection.MagicInstancePermanentSelectorService
import com.matag.player.PlayerType
import integration.TestUtils
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [SelectionTestConfiguration::class])
class MagicInstancePermanentSelectorServiceTest(
    @param:Autowired private val selectorService: MagicInstancePermanentSelectorService,
    @param:Autowired private val cardInstanceFactory: CardInstanceFactory,
    @param:Autowired private val testUtils: TestUtils,
    @param:Autowired private val cards: Cards
) {

    @Test
    fun emptyBattlefield() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val magicInstanceSelector =
            MagicInstanceSelector(
                selectorType = PERMANENT
            )

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).isEmpty()
    }

    @Test
    fun selectAnyPermanent() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector =
            MagicInstanceSelector(
                selectorType = PERMANENT
            )
        val cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name")
        gameStatus.nonCurrentPlayer.battlefield.addCard(cardInstance)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(cardInstance)
    }

    @Test
    fun selectCreature() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(selectorType = PERMANENT, ofType = listOf(Type.CREATURE))

        val aCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name")
        gameStatus.nonCurrentPlayer.battlefield.addCard(aCreature)
        val aLand = cardInstanceFactory.create(gameStatus, 2, cards.get("Plains"), "opponent-name")
        gameStatus.nonCurrentPlayer.battlefield.addCard(aLand)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(aCreature)
    }

    @Test
    fun selectionOnEqualPower() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT,
            ofType = listOf(Type.CREATURE),
            powerToughnessConstraint =
                PowerToughnessConstraint(
                    PowerOrToughness.POWER,
                    PowerToughnessConstraintType.EQUAL,
                    3
                )
        )

        val creaturePower3 =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Angel of the Dawn"), "opponent-name")
        gameStatus.nonCurrentPlayer.battlefield.addCard(creaturePower3)
        val creaturePower4 = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent-name")
        gameStatus.nonCurrentPlayer.battlefield.addCard(creaturePower4)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(creaturePower3)
    }

    @Test
    fun selectionOnGreaterPowerFails() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector =
            MagicInstanceSelector(
                selectorType = PERMANENT, ofType = listOf(Type.CREATURE), powerToughnessConstraint =
                    PowerToughnessConstraint(
                        PowerOrToughness.POWER,
                        PowerToughnessConstraintType.GREATER,
                        3
                    )
            )

        val creaturePower3 =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Angel of the Dawn"), "opponent-name")
        gameStatus.nonCurrentPlayer.battlefield.addCard(creaturePower3)
        val creaturePower4 = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent-name")
        gameStatus.nonCurrentPlayer.battlefield.addCard(creaturePower4)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(creaturePower4)
    }

    @Test
    fun selectionOnLessOrEqualToughnessPassesOnEqual() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector =
            MagicInstanceSelector(
                selectorType = PERMANENT, ofType = listOf(Type.CREATURE), powerToughnessConstraint =
                    PowerToughnessConstraint(
                        PowerOrToughness.TOUGHNESS,
                        PowerToughnessConstraintType.LESS_OR_EQUAL,
                        3
                    )
            )

        val creaturePower3 =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Angel of the Dawn"), "opponent-name")
        gameStatus.nonCurrentPlayer.battlefield.addCard(creaturePower3)
        val creaturePower4 = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent-name")
        gameStatus.nonCurrentPlayer.battlefield.addCard(creaturePower4)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(creaturePower3)
    }

    @Test
    fun selectionPlayerCreature() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector =
            MagicInstanceSelector(
                selectorType = PERMANENT, ofType = listOf(Type.CREATURE), controllerType = PlayerType.PLAYER
            )

        val playerCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        playerCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(playerCreature)

        val opponentCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "opponent-name")
        opponentCreature.controller = "opponent-name"
        gameStatus.nonCurrentPlayer.battlefield.addCard(opponentCreature)

        // When
        val selection = selectorService.select(gameStatus, playerCreature, magicInstanceSelector).cards

        // Then
        assertThat(selection).contains(playerCreature)
    }

    @Test
    fun selectionOpponentCreature() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector =
            MagicInstanceSelector(
                selectorType = PERMANENT, ofType = listOf(Type.CREATURE), controllerType = PlayerType.OPPONENT
            )

        val playerCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        playerCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(playerCreature)

        val opponentCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "opponent-name")
        opponentCreature.controller = "opponent-name"
        gameStatus.nonCurrentPlayer.battlefield.addCard(opponentCreature)

        // When
        val selection = selectorService.select(gameStatus, playerCreature, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(opponentCreature)
    }

    @Test
    fun selectionPlayerCreatureOnOpponentTurnUsingOwnershipWhenControllerIsMissing() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        gameStatus.turn.currentTurnPlayer = "opponent-name"

        val magicInstanceSelector =
            MagicInstanceSelector(
                selectorType = PERMANENT, ofType = listOf(Type.CREATURE), controllerType = PlayerType.OPPONENT
            )

        val playerCreature =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Battlefield Promotion"), "player-name")
        gameStatus.currentPlayer.battlefield.addCard(playerCreature)

        val opponentCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "opponent-name")
        opponentCreature.controller = "opponent-name"
        gameStatus.nonCurrentPlayer.battlefield.addCard(opponentCreature)

        // When
        val selection = selectorService.select(gameStatus, playerCreature, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(opponentCreature)
    }

    @Test
    fun selectionColorlessCreature() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector =
            MagicInstanceSelector(
                selectorType = PERMANENT, colorless = true
            )

        val redCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        redCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(redCreature)

        val colorlessCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Jousting Dummy"), "player-name")
        colorlessCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(colorlessCreature)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(colorlessCreature)
    }

    @Test
    fun selectionMulticolorCreatureFails() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector =
            MagicInstanceSelector(
                selectorType = PERMANENT, multicolor = true
            )

        val redCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        redCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(redCreature)

        val multicolorCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Adeliz, the Cinder Wind"), "player-name")
        multicolorCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(multicolorCreature)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(multicolorCreature)
    }

    @Test
    fun selectionAttackingCreature() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector =
            MagicInstanceSelector(
                selectorType = PERMANENT, ofType = listOf(Type.CREATURE), statusTypes = listOf(
                    StatusType.ATTACKING
                )
            )

        val attackingCreature =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        attackingCreature.controller = "player-name"
        attackingCreature.modifiers.isAttacking = true
        gameStatus.currentPlayer.battlefield.addCard(attackingCreature)

        val nonAttackingCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name")
        nonAttackingCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(nonAttackingCreature)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(attackingCreature)
    }

    @Test
    fun selectionCreatureWhoAttacked() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT,
            ofType = listOf(Type.CREATURE),
            statusTypes = listOf(StatusType.ATTACKED)
        )


        val creatureWhoAttacked =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        creatureWhoAttacked.controller = "player-name"
        creatureWhoAttacked.modifiers.modifiersUntilEndOfTurn.isAttacked = true
        gameStatus.currentPlayer.battlefield.addCard(creatureWhoAttacked)

        val creatureWhoDidNotAttacked =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name")
        creatureWhoDidNotAttacked.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(creatureWhoDidNotAttacked)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(creatureWhoAttacked)
    }

    @Test
    fun selectionBlockingCreature() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT,
            ofType = listOf(Type.CREATURE),
            statusTypes = listOf(StatusType.BLOCKING)
        )


        val blockingCreature =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        blockingCreature.controller = "player-name"
        blockingCreature.modifiers.blockingCardId = 3
        gameStatus.currentPlayer.battlefield.addCard(blockingCreature)

        val nonBlockingCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name")
        nonBlockingCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(nonBlockingCreature)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(blockingCreature)
    }

    @Test
    fun selectionCreatureWhoBlocked() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT,
            ofType = listOf(Type.CREATURE),
            statusTypes = listOf(StatusType.BLOCKED)
        )

        val creatureWhoBlocked =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        creatureWhoBlocked.controller = "player-name"
        creatureWhoBlocked.modifiers.blockingCardId = 3
        creatureWhoBlocked.modifiers.modifiersUntilEndOfTurn.isBlocked = true
        gameStatus.currentPlayer.battlefield.addCard(creatureWhoBlocked)

        val creatureWhoDidNotBlocked =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name")
        creatureWhoDidNotBlocked.controller = "player-name"
        creatureWhoDidNotBlocked.modifiers.blockingCardId = 3
        gameStatus.currentPlayer.battlefield.addCard(creatureWhoDidNotBlocked)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(creatureWhoBlocked)
    }

    @Test
    fun selectionAttackingOrBlockingAttackingCreature() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(

            selectorType = PERMANENT,
            ofType = listOf(Type.CREATURE),
            statusTypes = listOf(StatusType.ATTACKING, StatusType.BLOCKING)
        )


        val attackingCreature =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        attackingCreature.controller = "player-name"
        attackingCreature.modifiers.isAttacking = true
        gameStatus.currentPlayer.battlefield.addCard(attackingCreature)

        val blockingCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name")
        blockingCreature.controller = "player-name"
        blockingCreature.modifiers.blockingCardId = 4
        gameStatus.currentPlayer.battlefield.addCard(blockingCreature)

        val otherCreature = cardInstanceFactory.create(gameStatus, 3, cards.get("Grazing Whiptail"), "player-name")
        otherCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(otherCreature)

        // When
        val selection = selectorService.select(gameStatus, otherCreature, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(attackingCreature, blockingCreature)
    }

    @Test
    fun selectionTappedCreature() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(

            selectorType = PERMANENT, statusTypes = listOf(StatusType.TAPPED)
        )


        val tappedCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        tappedCreature.controller = "player-name"
        tappedCreature.modifiers.isTapped = true
        gameStatus.currentPlayer.battlefield.addCard(tappedCreature)

        val untappedCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name")
        untappedCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(untappedCreature)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(tappedCreature)
    }

    @Test
    fun selectionAnother() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT, ofType =
                listOf(Type.CREATURE), others = true
        )

        val aCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        aCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(aCreature)

        val anotherCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Frenzied Raptor"), "player-name")
        anotherCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(anotherCreature)

        // When
        val selection = selectorService.select(gameStatus, aCreature, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(anotherCreature)
    }

    @Test
    fun selectionNonLand() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT, notOfType =
                listOf(Type.LAND)
        )


        val nonLand = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        nonLand.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(nonLand)

        val land = cardInstanceFactory.create(gameStatus, 2, cards.get("Plains"), "player-name")
        land.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(land)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(nonLand)
    }

    @Test
    fun selectionZombiesYouControl() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT, ofType =
                listOf(
                    Type.CREATURE
                ), ofSubtype = listOf(Subtype.ZOMBIE), controllerType = PlayerType.PLAYER, others = true
        )

        val otherZombiesCreature =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Death Baron"), "player-name")
        otherZombiesCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(otherZombiesCreature)

        val aZombie = cardInstanceFactory.create(gameStatus, 2, cards.get("Diregraf Ghoul"), "player-name")
        aZombie.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(aZombie)

        val aNonZombie = cardInstanceFactory.create(gameStatus, 3, cards.get("Daybreak Chaplain"), "player-name")
        aNonZombie.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(aNonZombie)

        val anOpponentZombie = cardInstanceFactory.create(gameStatus, 4, cards.get("Diregraf Ghoul"), "player-name")
        anOpponentZombie.controller = "opponent-name"
        gameStatus.currentPlayer.battlefield.addCard(anOpponentZombie)

        // When
        val selection = selectorService.select(gameStatus, aZombie, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(otherZombiesCreature)
    }

    @Test
    fun selectionNonZombies() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT, ofType =
                listOf(
                    Type.CREATURE
                ), notOfSubtype = listOf(Subtype.ZOMBIE)
        )

        val aZombie = cardInstanceFactory.create(gameStatus, 1, cards.get("Diregraf Ghoul"), "player-name")
        aZombie.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(aZombie)

        val aNonZombie = cardInstanceFactory.create(gameStatus, 2, cards.get("Daybreak Chaplain"), "player-name")
        aNonZombie.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(aNonZombie)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(aNonZombie)
    }

    @Test
    fun selectionFliersYouControl() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT, others = true, ofType =
                listOf(Type.CREATURE), withAbilityType = AbilityType.FLYING, controllerType = PlayerType.PLAYER
        )

        val otherFliersCreature =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name")
        otherFliersCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(otherFliersCreature)

        val aFlier = cardInstanceFactory.create(gameStatus, 2, cards.get("Dawning Angel"), "player-name")
        aFlier.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(aFlier)

        val aNonFlier = cardInstanceFactory.create(gameStatus, 3, cards.get("Daybreak Chaplain"), "player-name")
        aNonFlier.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(aNonFlier)

        val anOpponentFlier = cardInstanceFactory.create(gameStatus, 4, cards.get("Dawning Angel"), "player-name")
        anOpponentFlier.controller = "opponent-name"
        gameStatus.currentPlayer.battlefield.addCard(anOpponentFlier)

        // When
        val selection = selectorService.select(gameStatus, otherFliersCreature, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(aFlier)
    }

    @Test
    fun selectionNotFliersYouControl() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT, ofType =
                listOf(
                    Type.CREATURE
                ), withoutAbilityType = AbilityType.FLYING, controllerType = PlayerType.PLAYER
        )

        val aFlier = cardInstanceFactory.create(gameStatus, 1, cards.get("Dawning Angel"), "player-name")
        aFlier.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(aFlier)

        val aNonFlier = cardInstanceFactory.create(gameStatus, 2, cards.get("Daybreak Chaplain"), "player-name")
        aNonFlier.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(aNonFlier)

        val anOpponentFlier = cardInstanceFactory.create(gameStatus, 3, cards.get("Dawning Angel"), "player-name")
        anOpponentFlier.controller = "opponent-name"
        gameStatus.currentPlayer.battlefield.addCard(anOpponentFlier)

        // When
        val selection = selectorService.select(gameStatus, aFlier, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(aNonFlier)
    }

    @Test
    fun selectionCreatureOrPlaneswalkerGreenOrWhite() {
        // Given
        val gameStatus = testUtils.testGameStatus()


        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT, ofType =
                listOf(
                    Type.CREATURE, Type.PLANESWALKER
                ), ofColors = listOf(Color.GREEN, Color.WHITE)
        )


        val whiteCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name")
        gameStatus.currentPlayer.battlefield.addCard(whiteCreature)

        val blackCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Barony Vampire"), "player-name")
        gameStatus.currentPlayer.battlefield.addCard(blackCreature)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(whiteCreature)
    }

    @Test
    fun selectionItself() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT, itself = true
        )


        val aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name")
        gameStatus.currentPlayer.battlefield.addCard(aPermanent)

        // When
        val selection = selectorService.select(gameStatus, aPermanent, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(aPermanent)
    }

    @Test
    fun selectionItselfAsLongItsYourTurnYes() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT, itself = true, turnStatusType = TurnStatusType.YOUR_TURN
        )


        val aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name")
        aPermanent.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(aPermanent)

        // When
        val selection = selectorService.select(gameStatus, aPermanent, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(aPermanent)
    }

    @Test
    fun selectionItselfAsLongItsYourTurnNo() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT, itself = true, turnStatusType = TurnStatusType.YOUR_TURN
        )


        val aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "opponent-name")
        aPermanent.controller = "opponent-name"
        gameStatus.nonCurrentPlayer.battlefield.addCard(aPermanent)

        // When
        val selection = selectorService.select(gameStatus, aPermanent, magicInstanceSelector).cards

        // Then
        assertThat(selection).isEmpty()
    }

    @Test
    fun selectionCurrentEnchanted() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT, currentEnchanted = true
        )


        val aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name")
        aPermanent.controller = "player-name"
        val theEnchantment = cardInstanceFactory.create(gameStatus, 2, cards.get("Colossification"), "player-name")
        theEnchantment.controller = "player-name"
        theEnchantment.modifiers.attachedToId = aPermanent.id

        gameStatus.currentPlayer.battlefield.addCard(aPermanent)
        gameStatus.currentPlayer.battlefield.addCard(theEnchantment)

        // When
        val selection = selectorService.select(gameStatus, theEnchantment, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(aPermanent)
    }

    @Test
    fun selectionHistoric() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = PERMANENT, historic = true
        )


        val aCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        aCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(aCreature)

        val anArtifact = cardInstanceFactory.create(gameStatus, 2, cards.get("Jousting Dummy"), "player-name")
        anArtifact.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(anArtifact)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(anArtifact)
    }

    @Test
    fun selectionArtifactCreature() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(

            selectorType = PERMANENT, ofAllTypes = listOf(Type.ARTIFACT, Type.CREATURE)
        )


        val aCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        aCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(aCreature)

        val anArtifact = cardInstanceFactory.create(gameStatus, 1, cards.get("Brawler's Plate"), "player-name")
        anArtifact.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(aCreature)

        val anArtifactCreature =
            cardInstanceFactory.create(gameStatus, 2, cards.get("Jousting Dummy"), "player-name")
        anArtifactCreature.controller = "player-name"
        gameStatus.currentPlayer.battlefield.addCard(anArtifactCreature)

        // When
        val selection = selectorService.select(gameStatus, null, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(anArtifactCreature)
    }

    @Test
    fun selectionSpellEmpty() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector =
            MagicInstanceSelector(selectorType = SelectorType.SPELL)


        val cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name")

        // When
        val selection = selectorService.select(gameStatus, cardInstance, magicInstanceSelector).cards

        // Then
        assertThat(selection).isEmpty()
    }

    @Test
    fun selectionAnySpell() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector = MagicInstanceSelector(
            selectorType = SelectorType.SPELL
        )


        val aSpell = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        gameStatus.stack.push(aSpell)

        // When
        val selection = selectorService.select(gameStatus, aSpell, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(aSpell)
    }

    @Test
    fun selectionAnInstantNotMatched() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector =
            MagicInstanceSelector(
                selectorType = SelectorType.SPELL, ofType = listOf(Type.INSTANT)
            )


        val aSpell = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name")
        gameStatus.stack.push(aSpell)

        // When
        val selection = selectorService.select(gameStatus, aSpell, magicInstanceSelector).cards

        // Then
        assertThat(selection).isEmpty()
    }

    @Test
    fun selectionAnInstantOfSorcerySpell() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val magicInstanceSelector =
            MagicInstanceSelector(
                selectorType = SelectorType.SPELL, ofType = listOf(Type.INSTANT, Type.SORCERY)
            )


        val anInstant = cardInstanceFactory.create(gameStatus, 1, cards.get("Precision Bolt"), "player-name")
        gameStatus.stack.push(anInstant)

        // When
        val selection = selectorService.select(gameStatus, anInstant, magicInstanceSelector).cards

        // Then
        assertThat(selection).containsExactly(anInstant)
    }

    @Test
    fun selectionAsTargetSkipsHexproof() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        val playerHexproof =
            cardInstanceFactory.create(gameStatus, 1, cards.get("Cold-Water Snapper"), "player-name")
        gameStatus.player1!!.battlefield.addCard(playerHexproof)

        val playerNonHexproof = cardInstanceFactory.create(gameStatus, 2, cards.get("Yoked Ox"), "player-name")
        gameStatus.player1!!.battlefield.addCard(playerNonHexproof)

        val opponentHexproof =
            cardInstanceFactory.create(gameStatus, 3, cards.get("Cold-Water Snapper"), "opponent-name")
        gameStatus.player2!!.battlefield.addCard(opponentHexproof)

        val opponentNonHexproof = cardInstanceFactory.create(gameStatus, 4, cards.get("Yoked Ox"), "opponent-name")
        gameStatus.player2!!.battlefield.addCard(opponentNonHexproof)

        val playerInstant = cardInstanceFactory.create(gameStatus, 5, cards.get("Disfigure"), "player-name")
        val playerInstantSelector = playerInstant.abilities[0].targets[0].magicInstanceSelector!!

        // When
        val selection = selectorService.selectAsTarget(gameStatus, playerInstant, playerInstantSelector).cards

        // Then
        assertThat(selection)
            .containsExactly(playerHexproof, playerNonHexproof, opponentNonHexproof)
    }
}