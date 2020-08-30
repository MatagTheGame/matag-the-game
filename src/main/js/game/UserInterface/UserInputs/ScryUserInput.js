import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import UserInputs from 'game/UserInterface/UserInputs/UserInputs'
import TurnUtils from 'game/Turn/TurnUtils'

class ScryUserInput extends Component {
  constructor(props) {
    super(props)
    this.scryAbility = this.scryAbility.bind(this)
  }

  scryAbility(cardId, action) {
    this.props.scryAbilityClick(cardId, action)
  }

  render() {
    if (this.props.isScry && this.props.cardId >= 0) {
      const userOptions = [{
        title: 'Top',
        component: 'Put on Top',
        func: () => this.scryAbility(this.props.cardId, 'TOP')
      }, {
        title: 'Bottom',
        component: 'Put on Bottom',
        func: () => this.scryAbility(this.props.cardId, 'BOTTOM')
      }]

      return <UserInputs userOptions={userOptions}/>

    } else {
      return <></>
    }
  }
}

const scryAbilityClick = (cardId, action) => {
  return {
    type: 'SCRY_ABILITY_CLICK',
    cardId: cardId,
    action: action
  }
}

const mapStateToProps = state => {
  return {
    isScry: TurnUtils.inputRequiredActionIs(state, 'SCRY'),
    cardId: get(state, 'userInterface.userInputs.cardId', -1),
  }
}

const mapDispatchToProps = dispatch => {
  return {
    scryAbilityClick: bindActionCreators(scryAbilityClick, dispatch)
  }
}

ScryUserInput.propTypes = {
  cardId: PropTypes.number.isRequired
}


export default connect(mapStateToProps, mapDispatchToProps)(ScryUserInput)