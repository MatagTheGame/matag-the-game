import React from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import './playableAbilities.scss'

function PossibleAbility(props) {
  const renderColors = (colors) => {
    return colors.map((color, index) => <img key={index} src={`/img/symbols/${color}.png`} alt={color} />)
  }

  switch (props.possibleAbility.abilityType) {
    case 'TAP_ADD_MANA':
      const colors = props.possibleAbility.parameters
      return <li onClick={props.onClick} title={`TAP: add ${colors}`}><img src='/img/symbols/TAP.png' alt='TAP' />: add {renderColors(colors)}</li>
    default:
      return <li/>
  }
}

PossibleAbility.propTypes = {
  possibleAbility: PropTypes.object.isRequired,
  onClick: PropTypes.func.isRequired
}

function PlayableAbilities(props) {
  const playAbility = (index) => {
    props.playAbilitiesAction(props.cardId, index)
  }

  const closePlayableAbilitiesOverlay = () => {
    props.closePlayableAbilitiesOverlay()
  }

  if (props.possibleAbilities.length > 1) {
    return (
      <div id='playable-abilities-overlay' onClick={closePlayableAbilitiesOverlay}>
        <div id='playable-abilities' style={{left: props.position.x + 'px', top: (props.position.y - 150) + 'px'}}>
          <ul>
            { props.possibleAbilities.map((possibleAbility, index) =>
              <PossibleAbility key={index} possibleAbility={possibleAbility} onClick={() => playAbility(index)} />) }
          </ul>
        </div>
      </div>
    )
  } else {
    return <></>
  }
}

const closePlayableAbilitiesAction = () => {
  return {
    type: 'CLOSE_PLAYABLE_ABILITIES_CLICK'
  }
}

const playAbilitiesAction = (cardId, index) => {
  return {
    type: 'PLAY_ABILITIES_CLICK',
    cardId: cardId,
    index: index
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
    closePlayableAbilitiesOverlay: bindActionCreators(closePlayableAbilitiesAction, dispatch),
    playAbilitiesAction: bindActionCreators(playAbilitiesAction, dispatch)
  }
}

PlayableAbilities.propTypes = {
  cardId: PropTypes.number.isRequired,
  possibleAbilities: PropTypes.array.isRequired,
  position: PropTypes.object.isRequired,
}


export default connect(mapStateToProps, mapDispatchToProps)(PlayableAbilities)