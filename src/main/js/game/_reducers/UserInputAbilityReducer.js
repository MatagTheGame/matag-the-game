import CardSearch from 'game/Card/CardSearch'
import CardUtils from 'game/Card/CardUtils'
import PlayerUtils from 'game/PlayerInfo/PlayerUtils'
import UserInterfaceUtils from 'game/UserInterface/UserInterfaceUtils'
import UserInputUtils from 'game/UserInterface/UserInputs/UserInputUtils'

export default class UserInputAbilityReducer {

  static getEvents() {
    return ['PLAY_ABILITIES_CLICK', 'SCRY_ABILITY_CLICK']
  }

  static reduceEvent(newState, action) {
    switch (action.type) {
      case 'PLAY_ABILITIES_CLICK':
        const cardInstance = CardSearch.cards(newState.player.battlefield).withId(action.cardId)

        const playedAbility = CardUtils.getAbilitiesForTriggerTypes(cardInstance, ['ACTIVATED_ABILITY', 'MANA_ABILITY'])[action.index]
        if (playedAbility.trigger.type === 'MANA_ABILITY') {
          CardUtils.activateManaAbility(newState, cardInstance, action.index)

        } else if (playedAbility.trigger.type === 'ACTIVATED_ABILITY') {
          PlayerUtils.castOrHandleTargets(newState, cardInstance, playedAbility)
        }

        break

      case 'SCRY_ABILITY_CLICK':
        let choice = UserInterfaceUtils.getInputRequiredActionChoice(newState)
        if (!choice) {
          choice = UserInputUtils.getDefaultChoiceForScry(newState)
        }

        let cardToMoveIndex = -1
        for (let i = 0; i < newState.player.visibleLibrary.length; i++) {
          if (newState.player.visibleLibrary[i].id === action.cardId) {
            cardToMoveIndex = i
            break
          }
        }

        // FIXME implement scry with one only choice
        if (action.action === 'TOP') {
          choice = '1'
        } else {
          choice = '-1'
        }

        UserInterfaceUtils.setInputRequiredActionChoice(newState, choice)

        break
    }

    return newState
  }

}