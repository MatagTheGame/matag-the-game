import React, {Component} from 'react'
import DateUtils from '../utils/DateUtils'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'

class ActiveGame extends Component {
  displayGoToGame() {
    const gameId = this.props.activeGame.gameId
    return <span>Go to <a href='#' onClick={() => this.props.goToGame(gameId)}>game #{gameId}</a></span>
  }

  render() {
    const game = this.props.activeGame

    return (
      <div>
        <p>You have already a game in progress:</p>

        <div className='matag-card active-game'>
          <dl>
            <dt>Game id: </dt>
            <dd>{game.gameId}</dd>
            <dt>Created at: </dt>
            <dd>{DateUtils.formatDateTime(DateUtils.parse(game.createdAt))}</dd>
            <dt>Player name: </dt>
            <dd>{game.playerName}</dd>
            <dt>Player options: </dt>
            <dd>{game.playerOptions}</dd>
            <dt>Opponent name: </dt>
            <dd>{game.opponentName}</dd>
            <dt>Opponent options: </dt>
            <dd>{game.opponentOptions}</dd>
          </dl>

          <div className='matag-form'>
            <div className='grid grid-50-50'>
              <input type='button' value='Go to Game' onClick={() => this.props.goToGame(game.gameId)} />
              <input type='button' value='Cancel Game' onClick={() => alert('Coming Soon')} />
            </div>
          </div>
        </div>

      </div>
    )
  }
}

const deletingActiveGame = () => {
  return {
    type: 'ACTIVE_GAME_DELETING'
  }
}

const deletedActiveGame = () => {
  return {
    type: 'ACTIVE_GAME_DELETED'
  }
}

const mapStateToProps = state => {
  return {
    deleting: get(state, 'activeGame.deleting', false),
    activeGame: get(state, 'activeGame.value', {}),
    matagGameUrl: get(state, 'config.matagGameUrl', '')
  }
}

const mapDispatchToProps = dispatch => {
  return {
    deletingActiveGame: bindActionCreators(deletingActiveGame, dispatch),
    deletedActiveGame: bindActionCreators(deletedActiveGame, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(ActiveGame)