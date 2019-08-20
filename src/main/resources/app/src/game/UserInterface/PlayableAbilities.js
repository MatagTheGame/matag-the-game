import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import {get} from 'lodash'
import PropTypes from 'prop-types'

class PlayableAbilities extends Component {
  constructor(props) {
    super(props)
    this.closePlayableAbilitiesOverlay = this.closePlayableAbilitiesOverlay.bind(this)
  }

  closePlayableAbilitiesOverlay() {
    this.props.closePlayableAbilitiesOverlay()
  }

  render() {
    if (this.props.possibleAbilities.length > 1) {
      return (
        <div id='playable-abilities-overlay' onClick={this.closePlayableAbilitiesOverlay}>
          <div id='playable-abilities' style={{left: this.props.position.x + 'px', top: this.props.position.y + 'px'}}>
            <ul>
              <li>TAP: add 1 WHITE mana</li>
              <li>TAP: add 1 BLUE mana</li>
            </ul>
          </div>
        </div>
      )
    } else {
      return null
    }
  }
}

const closePlayableAbilitiesAction = () => {
  return {
    type: 'CLOSE_PLAYABLE_ABILITIES_CLICK'
  }
}

const mapStateToProps = state => {
  return {
    cardId: get(state, 'userInterface.playableAbilities.cardId', -1),
    possibleAbilities: get(state, 'userInterface.playableAbilities.possibleAbilities', []),
    position: get(state, 'userInterface.playableAbilities.position', {x: -1, y: -1})
  }
}

const mapDispatchToProps = dispatch => {
  return {
    closePlayableAbilitiesOverlay: bindActionCreators(closePlayableAbilitiesAction, dispatch)
  }
}

PlayableAbilities.propTypes = {
  cardId: PropTypes.number.isRequired,
  possibleAbilities: PropTypes.array.isRequired,
  position: PropTypes.object.isRequired,
}


export default connect(mapStateToProps, mapDispatchToProps)(PlayableAbilities)