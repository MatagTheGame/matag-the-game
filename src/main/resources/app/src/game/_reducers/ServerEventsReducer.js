import PlayerUtils from '../PlayerInfo/PlayerUtils'

export default class ServerEventsReducer {
  static getEvents() {
    return ['MESSAGE', 'INIT_WAITING_OPPONENT', 'OPPONENT_JOINED', 'INIT_PLAYER', 'INIT_OPPONENT', 'UPDATE_TURN', 'UPDATE_STACK',
      'UPDATE_PLAYER_BATTLEFIELD', 'UPDATE_PLAYER_HAND', 'UPDATE_PLAYER_GRAVEYARD', 'UPDATE_PLAYER_LIFE', 'UPDATE_PLAYER_LIBRARY_SIZE']
  }

  static reduceEvent(newState, action) {
    let player
    switch (action.type) {
    case 'MESSAGE':
      newState.message = action.value
      break

    case 'INIT_WAITING_OPPONENT':
      newState.message = {text: 'Waiting for opponent...', closable: false}
      break

    case 'OPPONENT_JOINED':
      newState.message.text = undefined
      break

    case 'INIT_PLAYER':
      newState.player = action.value
      break

    case 'INIT_OPPONENT':
      newState.opponent = action.value
      break

    case 'UPDATE_TURN':
      newState.turn = action.value
      newState.turn.blockingCardPosition = 0
      PlayerUtils.setStatusMessage(newState)
      break

    case 'UPDATE_STACK':
      newState.stack = action.value
      break

    case 'UPDATE_PLAYER_BATTLEFIELD':
      player = PlayerUtils.getPlayerByName(newState, action.value.playerName)
      console.log('player: ', player)
      player.battlefield = action.value.value
      break

    case 'UPDATE_PLAYER_HAND':
      player = PlayerUtils.getPlayerByName(newState, action.value.playerName)
      player.hand = action.value.value
      break

    case 'UPDATE_PLAYER_GRAVEYARD':
      player = PlayerUtils.getPlayerByName(newState, action.value.playerName)
      player.graveyard = action.value.value
      break

    case 'UPDATE_PLAYER_LIFE':
      player = PlayerUtils.getPlayerByName(newState, action.value.playerName)
      player.life = action.value.value
      break

    case 'UPDATE_PLAYER_LIBRARY_SIZE':
      player = PlayerUtils.getPlayerByName(newState, action.value.playerName)
      player.librarySize = action.value.value
      break
    }

    return newState
  }

}