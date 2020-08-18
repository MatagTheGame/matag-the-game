import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import Card from 'game/Card/Card'
import StackUtils from './StackUtils'
import {TriggeredAbility} from './TriggeredAbility'
import './stack.scss'

class Stack extends Component {

  renderStackItem(cardInstance, i) {
    const style = {transform: 'translateY(-150px) translateX(' + (i * 50) + 'px) translateZ(' + (i * 150) + 'px)'}
    if (StackUtils.isACastedCard(cardInstance)) {
      return (
        <span key={cardInstance.id} style={style}>
          <Card cardInstance={cardInstance} area='stack' onclick={this.playerElementClick(cardInstance.id)}/>
        </span>
      )

    } else {
      return (
        <span key={cardInstance.id} style={style}>
          <TriggeredAbility cardInstance={cardInstance} />
        </span>
      )
    }
  }

  playerElementClick(cardId) {
    return (e) => this.props.playerElementClick(cardId, {x: e.screenX, y: e.screenY})
  }

  renderStack() {
    return this.props.stack.map((cardInstance, index) => this.renderStackItem(cardInstance, index))
  }

  render() {
    return (
      <div id='stack'>
        {this.renderStack()}
      </div>
    )
  }
}

const createStackElementClickAction = (cardId, event) => {
  return {
    type: 'STACK_ELEMENT_CLICK',
    cardId: cardId,
    event: event
  }
}

const mapDispatchToProps = dispatch => {
  return {
    playerElementClick: bindActionCreators(createStackElementClickAction, dispatch)
  }
}

const mapStateToProps = state => {
  return {
    stack: get(state, 'stack', [])
  }
}

Stack.propTypes = {
  stack: PropTypes.array.isRequired
}

export default connect(mapStateToProps, mapDispatchToProps)(Stack)